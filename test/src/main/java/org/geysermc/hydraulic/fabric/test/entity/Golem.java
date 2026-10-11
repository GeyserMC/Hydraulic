package org.geysermc.hydraulic.fabric.test.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.level.Level;

/**
 * A mob with a dedicated renderer, a state only it knows that hides part of its model, and a
 * keyframe animation started from an {@link AnimationState} on the client.
 */
public class Golem extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> DATA_CHARGING = SynchedEntityData.defineId(Golem.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState waveAnimationState = new AnimationState();

    public Golem(EntityType<? extends Golem> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_CHARGING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.waveAnimationState.animateWhen(this.isCharging(), this.tickCount);
        } else if (this.tickCount % 100 == 0) {
            this.entityData.set(DATA_CHARGING, !this.isCharging());
        }
    }

    public boolean isCharging() {
        return this.entityData.get(DATA_CHARGING);
    }
}
