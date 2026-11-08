package org.geysermc.hydraulic.entity.animation;

import org.geysermc.conduit.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A keyframe animation, as an {@code AnimationDefinition} is built.
 *
 * @param length the length in seconds
 * @param looping whether it loops
 * @param bones the channels animating each part, by part name
 */
public record KeyframeDefinition(float length, boolean looping, @NotNull Map<String, List<Channel>> bones) {

    /**
     * Reads a definition from how it was built, such as
     * {@code AnimationDefinition.Builder.withLength(1).looping().addAnimation("body", channel).build()},
     * optionally baked onto a part.
     *
     * @param value the built definition
     * @return the definition, or null if it wasn't built that way
     */
    @Nullable
    public static KeyframeDefinition read(@Nullable Value value) {
        if (value instanceof Value.InstanceCall(String _, String name, Value receiver, List<Value> _) && name.equals("bake")) {
            value = receiver;
        }

        if (!(value instanceof Value.InstanceCall(String _, String build, Value builder, List<Value> _)) || !build.equals("build")) {
            return null;
        }

        List<Value.InstanceCall> additions = new ArrayList<>();
        boolean looping = false;
        while (builder instanceof Value.InstanceCall call) {
            switch (call.methodName()) {
                case "looping" -> looping = true;
                case "addAnimation" -> additions.addFirst(call);
                default -> {
                    return null;
                }
            }

            builder = call.receiver();
        }

        if (!(builder instanceof Value.StaticCall(String _, String withLength, List<Value> lengthArgs))
                || !withLength.equals("withLength") || !(lengthArgs.getFirst() instanceof Value.Literal(Number length))) {
            return null;
        }

        Map<String, List<Channel>> bones = new LinkedHashMap<>();
        for (Value.InstanceCall addition : additions) {
            Channel channel = addition.args().size() == 2 ? Channel.read(addition.args().get(1)) : null;
            if (channel != null && addition.args().getFirst() instanceof Value.Literal(String bone)) {
                bones.computeIfAbsent(bone, _ -> new ArrayList<>()).add(channel);
            }
        }

        return new KeyframeDefinition(length.floatValue(), looping, bones);
    }

    /**
     * What a channel animates.
     */
    public enum Target {
        POSITION,
        ROTATION,
        SCALE
    }

    /**
     * The keyframes animating one property of a part.
     *
     * @param target the property animated
     * @param frames the keyframes, in order
     */
    public record Channel(@NotNull Target target, @NotNull List<Frame> frames) {

        @Nullable
        static Channel read(@NotNull Value value) {
            if (!(value instanceof Value.Constructed(String _, List<Value> args)) || args.size() != 2
                    || !(args.getFirst() instanceof Value.Field(String _, String targetName))
                    || !(args.get(1) instanceof Value.Array(String _, List<Value> keyframes))) {
                return null;
            }

            Target target;
            try {
                target = Target.valueOf(targetName);
            } catch (IllegalArgumentException e) {
                return null;
            }

            List<Frame> frames = new ArrayList<>(keyframes.size());
            for (Value keyframe : keyframes) {
                Frame frame = Frame.read(keyframe);
                if (frame == null) {
                    return null;
                }

                frames.add(frame);
            }

            return new Channel(target, frames);
        }
    }

    /**
     * One keyframe, holding the values its vector helper was given: degrees for rotation, Java's
     * offsets for position, and the scale itself for scale.
     *
     * @param time the time in seconds
     * @param pre the value arriving at the keyframe
     * @param post the value leaving it
     * @param catmullrom whether the way into this keyframe is smoothed rather than linear
     */
    public record Frame(float time, float @NotNull [] pre, float @NotNull [] post, boolean catmullrom) {

        @Nullable
        static Frame read(@NotNull Value value) {
            if (!(value instanceof Value.Constructed(String _, List<Value> args)) || args.size() < 3
                    || !(args.getFirst() instanceof Value.Literal(Number time))
                    || !(args.getLast() instanceof Value.Field(String _, String interpolation))) {
                return null;
            }

            float[] pre = vector(args.get(1));
            float[] post = args.size() == 4 ? vector(args.get(2)) : pre;
            return pre == null || post == null ? null : new Frame(time.floatValue(), pre, post, interpolation.equals("CATMULLROM"));
        }

        @Nullable
        private static float[] vector(@NotNull Value value) {
            List<Value> args = switch (value) {
                case Value.StaticCall(String _, String _, List<Value> callArgs) -> callArgs;
                case Value.Constructed(String _, List<Value> constructorArgs) -> constructorArgs;
                default -> List.of();
            };

            if (args.size() != 3) {
                return null;
            }

            float[] vector = new float[3];
            for (int i = 0; i < 3; i++) {
                if (!(args.get(i) instanceof Value.Literal(Number n))) {
                    return null;
                }

                vector[i] = n.floatValue();
            }

            return vector;
        }
    }
}
