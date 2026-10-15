package org.geysermc.hydraulic.entity.geckolib;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A modded entity drawn by a GeckoLib renderer, with the Bedrock geometry and animations it ships.
 *
 * @param id the entity type's identifier
 * @param geometry the entity's geo file
 * @param texture the texture it is drawn with, as {@code namespace:textures/...png}
 * @param animations the entity's animation file, empty if it has none
 * @param clips the animations in that file, in order
 * @param heads the bones its renderer turns to face where the entity looks
 */
public record GeckoLibEntity(@NotNull Identifier id, @NotNull JsonObject geometry, @NotNull String texture,
                             @NotNull JsonObject animations, @NotNull List<Clip> clips, @NotNull List<String> heads
) {
    static final int CONTROLLERS = 2;
    static final String ANIMATIONS = "animations";

    /**
     * Creates an entity, reading its clips from its animation file.
     *
     * @param id the entity type's identifier
     * @param geometry the entity's geo file
     * @param texture the texture it is drawn with, as {@code namespace:textures/...png}
     * @param animations the entity's animation file
     * @param heads the bones its renderer turns to face where the entity looks
     */
    public GeckoLibEntity(@NotNull Identifier id, @NotNull JsonObject geometry, @NotNull String texture,
                          @NotNull JsonObject animations, @NotNull List<String> heads) {
        this(id, geometry, texture, animations, Clip.of(animations), heads);
    }

    /**
     * Gets the controller state an animation is played in.
     *
     * @param clip the animation's index in {@link #clips()}
     * @param loop how it repeats
     * @return the state, where zero plays nothing
     */
    public static int state(int clip, @NotNull Loop loop) {
        return 1 + clip * Loop.values().length + loop.ordinal();
    }

    /**
     * Gets how many states each of the entity's controllers has, including the one playing nothing.
     *
     * @return the number of states
     */
    public int states() {
        return 1 + this.clips.size() * Loop.values().length;
    }

    /**
     * An animation in a GeckoLib animation file.
     *
     * @param name the animation's name in the file
     * @param loop how it repeats unless the controller playing it says otherwise
     * @param length how long it lasts, in seconds
     */
    public record Clip(@NotNull String name, @NotNull Loop loop, double length) {
        private static final String LOOP = "loop";
        private static final String LENGTH = "animation_length";

        /**
         * Lists the animations in an animation file.
         *
         * @param file the animation file
         * @return its animations, in the order the file declares them
         */
        @NotNull
        public static List<Clip> of(@NotNull JsonObject file) {
            List<Clip> clips = new ArrayList<>();
            JsonObject animations = file.getAsJsonObject(ANIMATIONS);
            if (animations == null) {
                return clips;
            }

            for (Map.Entry<String, JsonElement> entry : animations.entrySet()) {
                if (entry.getValue() instanceof JsonObject animation) {
                    double length = animation.get(LENGTH) instanceof JsonPrimitive primitive && primitive.isNumber() ? primitive.getAsDouble() : 0;
                    clips.add(new Clip(entry.getKey(), Loop.of(animation.get(LOOP)), length));
                }
            }

            return clips;
        }
    }

    /**
     * How an animation repeats once it reaches its end.
     */
    public enum Loop {
        ONCE,
        LOOP,
        HOLD;

        private static final String HOLD_ON_LAST_FRAME = "hold_on_last_frame";

        /**
         * Reads the {@code loop} of an animation.
         *
         * @param loop the value, or null if the animation has none
         * @return how the animation repeats
         */
        @NotNull
        public static Loop of(@Nullable JsonElement loop) {
            if (!(loop instanceof JsonPrimitive primitive)) {
                return ONCE;
            }

            if (primitive.isBoolean()) {
                return primitive.getAsBoolean() ? LOOP : ONCE;
            }

            return switch (primitive.getAsString()) {
                case "true", "loop" -> LOOP;
                case HOLD_ON_LAST_FRAME -> HOLD;
                default -> ONCE;
            };
        }

        /**
         * Gets the {@code loop} Bedrock reads for this.
         *
         * @return the value
         */
        @NotNull
        public JsonElement json() {
            return switch (this) {
                case ONCE -> new JsonPrimitive(false);
                case LOOP -> new JsonPrimitive(true);
                case HOLD -> new JsonPrimitive(HOLD_ON_LAST_FRAME);
            };
        }
    }
}
