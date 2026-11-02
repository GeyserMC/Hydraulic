package org.geysermc.hydraulic.entity.geckolib;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.LoopType;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.ToIntFunction;

/**
 * Runs a GeckoLib entity's animation controllers on the server to work out what each plays.
 */
public final class GeckoLibAnimator {
    private static final float MOTION_THRESHOLD = 0.015f;
    private static final Field HANDLER;

    static {
        try {
            Field field = AnimationController.class.getDeclaredField("stateHandler");
            field.setAccessible(true);
            HANDLER = field;
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
    }

    private final GeckoLibEntity entity;
    private final Map<String, Integer> clips = new HashMap<>();
    private final Map<Entity, AnimationEntry> trackedAnimations = new WeakHashMap<>();

    public GeckoLibAnimator(@NotNull GeckoLibEntity entity) {
        this.entity = entity;
        for (int i = 0; i < entity.clips().size(); i++) {
            this.clips.put(entity.clips().get(i).name(), i);
        }
    }

    private int value(@NotNull Entity entity, int slot) {
        AnimationEntry entry = this.trackedAnimations.computeIfAbsent(entity, AnimationEntry::new);
        if (slot >= entry.controllers.size()) {
            return 0;
        }

        AnimationController<GeoAnimatable> controller = entry.controllers.get(slot);
        ServerRenderState state = new ServerRenderState(moving(entity), new HashMap<>());
        PlayState play = handler(controller).handle(new AnimationTest<>(entry.animatable, state, entry.manager, controller));
        RawAnimation animation = play == PlayState.STOP ? null : controller.getCurrentRawAnimation();

        AnimationState playing = entry.states[slot];
        boolean restarted = !Objects.equals(animation, playing.animation);
        if (restarted) {
            playing.animation = animation;
            playing.stage = 0;
            playing.start = entity.tickCount;
        }

        int next = animation == null ? 0 : this.advance(playing, animation.getAnimationStages(), entity.tickCount);
        if (restarted || next != playing.state) {
            playing.parity ^= 1;
            playing.state = next;
        }

        return next * 2 + playing.parity;
    }

    private int advance(@NotNull GeckoLibAnimator.AnimationState playing, @NotNull List<RawAnimation.Stage> stages, int tick) {
        while (playing.stage < stages.size() - 1) {
            int length = this.ticks(stages.get(playing.stage));
            if (length < 0 || tick - playing.start < length) {
                break;
            }

            playing.start += length;
            playing.stage++;
        }

        RawAnimation.Stage stage = stages.get(playing.stage);
        Integer clip = this.clips.get(stage.animationName());
        return clip == null ? 0 : GeckoLibEntity.state(clip, this.loop(stage.loopType(), clip));
    }

    private int ticks(@NotNull RawAnimation.Stage stage) {
        if (stage.animationName().equals(RawAnimation.Stage.WAIT)) {
            return stage.waitTicks();
        }

        Integer clip = this.clips.get(stage.animationName());
        if (clip == null) {
            return 0;
        }

        if (this.loop(stage.loopType(), clip) != GeckoLibEntity.Loop.ONCE) {
            return -1;
        }

        return (int) Math.ceil(this.entity.clips().get(clip).length() * 20);
    }

    @NotNull
    private GeckoLibEntity.Loop loop(@NotNull LoopType type, int clip) {
        if (type == LoopType.LOOP) {
            return GeckoLibEntity.Loop.LOOP;
        }

        if (type == LoopType.HOLD_ON_LAST_FRAME) {
            return GeckoLibEntity.Loop.HOLD;
        }

        if (type == LoopType.PLAY_ONCE) {
            return GeckoLibEntity.Loop.ONCE;
        }

        return this.entity.clips().get(clip).loop();
    }

    // Copied from GeckoLib's GeoRenderer#getMotionAnimThreshold
    private static boolean moving(@NotNull Entity entity) {
        if (entity instanceof LivingEntity living) {
            return living.walkAnimation.speed() >= MOTION_THRESHOLD;
        }

        return entity.getDeltaMovement().lengthSqr() >= MOTION_THRESHOLD;
    }

    @NotNull
    ToIntFunction<Entity> slot(int slot) {
        return entity -> this.value(entity, slot);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    private static AnimationController.AnimationStateHandler<GeoAnimatable> handler(@NotNull AnimationController<GeoAnimatable> controller) {
        try {
            return (AnimationController.AnimationStateHandler<GeoAnimatable>) HANDLER.get(controller);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * The render state controllers are given on the server. GeckoLib's own data tickets refer to
     * client classes, so only what {@link AnimationTest#isMoving()} reports is known.
     *
     * @param moving whether the entity is moving
     * @param data anything the controllers store in it
     */
    public record ServerRenderState(boolean moving, @NotNull Map<DataTicket<?>, Object> data) implements GeoRenderState {

        @Override
        public Map<DataTicket<?>, Object> getDataMap() {
            return this.data;
        }
    }

    private static final class AnimationEntry {
        private final GeoAnimatable animatable;
        private final AnimatableManager<GeoAnimatable> manager;
        private final List<AnimationController<GeoAnimatable>> controllers;
        private final AnimationState[] states = new AnimationState[GeckoLibEntity.CONTROLLERS];

        private AnimationEntry(@NotNull Entity entity) {
            this.animatable = (GeoAnimatable) entity;
            this.manager = new AnimatableManager<>(this.animatable);
            this.controllers = List.copyOf(this.manager.getAnimationControllers().values());
            for (int i = 0; i < this.states.length; i++) {
                this.states[i] = new AnimationState();
            }
        }
    }

    private static final class AnimationState {
        @Nullable
        private RawAnimation animation;
        private int stage;
        private int start;
        private int state;
        private int parity;
    }
}
