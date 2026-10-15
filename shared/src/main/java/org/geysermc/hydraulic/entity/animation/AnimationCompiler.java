package org.geysermc.hydraulic.entity.animation;

import org.geysermc.conduit.Summary;
import org.geysermc.conduit.Value;
import org.geysermc.hydraulic.entity.BedrockEntityModelConverter;
import org.geysermc.hydraulic.entity.conduit.EntityModel;
import org.geysermc.pack.bedrock.resource.animations.ActorAnimation;
import org.geysermc.pack.bedrock.resource.animations.actoranimation.Animations;
import org.geysermc.pack.bedrock.resource.animations.actoranimation.animations.Bones;
import org.geysermc.pack.bedrock.resource.animations.actoranimation.animations.bones.Position;
import org.geysermc.pack.bedrock.resource.animations.actoranimation.animations.bones.Rotation;
import org.geysermc.pack.bedrock.resource.animations.actoranimation.animations.bones.Scale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Compiles what a model's {@code setupAnim} does to its parts into a looping Bedrock animation.
 */
public final class AnimationCompiler {
    private static final String SET_POS = "setPos";
    private static final String SETUP_SUFFIX = ".setup";
    private static final String ANIMATION_STATE = "net/minecraft/world/entity/AnimationState";
    private static final String VARIABLE_PREFIX = "hyd";

    private AnimationCompiler() {
    }

    /**
     * Compiles an animation.
     *
     * @param name the animation's identifier
     * @param setupAnim the summary of the model's {@code setupAnim}
     * @param bones the model's bones
     * @param scale how much the model's mesh is scaled when baked
     * @param state resolves fields of the render state {@code setupAnim} is given
     * @param logger the logger to report parts that could not be compiled to
     * @return the animation, or null if nothing it does could be compiled
     */
    @Nullable
    public static CompiledAnimation compile(@NotNull String name, @NotNull Summary setupAnim, @NotNull BoneMap bones, float scale,
                                            @NotNull StateResolver state, @NotNull Logger logger) {
        Map<String, Map<Channel.Kind, Value[]>> channels = new LinkedHashMap<>();
        Map<String, Value> visibility = new LinkedHashMap<>();

        for (Summary.Assignment assignment : setupAnim.assignments()) {
            BoneMap.Bone bone = assignment.target() == null ? null : bones.bone(assignment.target());
            if (bone == null) {
                continue;
            }

            if (assignment.field().equals("visible")) {
                visibility.put(bone.name(), assignment.value());
                continue;
            }

            Target target = Target.of(assignment.field());
            if (target != null) {
                channels.computeIfAbsent(bone.name(), _ -> new LinkedHashMap<>())
                    .computeIfAbsent(target.kind(), _ -> new Value[3])[target.axis()] = transform(target, assignment.value(), bone.part(), scale);
            }
        }

        for (Summary.Invocation invocation : setupAnim.invocations()) {
            if (!(invocation.call() instanceof Value.InstanceCall(String _, String method, Value receiver, List<Value> args))
                    || !method.equals(SET_POS) || args.size() != 3) {
                continue;
            }

            BoneMap.Bone bone = bones.bone(receiver);
            if (bone == null) {
                continue;
            }

            Value[] position = channels.computeIfAbsent(bone.name(), _ -> new LinkedHashMap<>()).computeIfAbsent(Channel.Kind.POSITION, _ -> new Value[3]);
            for (int axis = 0; axis < 3; axis++) {
                Value value = invocation.condition() == null
                    ? args.get(axis)
                    : new Value.Conditional(invocation.condition(), args.get(axis), new Value.InstanceField(receiver, "", Target.POSITION[axis]));
                position[axis] = transform(new Target(Channel.Kind.POSITION, axis), value, bone.part(), scale);
            }
        }

        List<KeyframeUse> uses = new ArrayList<>();
        for (Summary.Invocation invocation : setupAnim.invocations()) {
            KeyframeUse use = KeyframeUse.of(invocation, bones);
            if (use != null) {
                uses.add(use);
            }
        }

        MolangCompiler compiler = new MolangCompiler(new Inputs(bones, state), VARIABLE_PREFIX);
        channels.values().forEach(kinds -> kinds.values().forEach(axes -> {
            for (Value axis : axes) {
                if (axis != null) {
                    compiler.add(axis);
                }
            }
        }));
        visibility.values().forEach(compiler::add);
        for (KeyframeUse use : uses) {
            compiler.add(use.time());
            compiler.add(use.weight());
            compiler.add(use.speed());
        }

        List<Channel> compiled = new ArrayList<>();
        channels.forEach((bone, kinds) -> kinds.forEach((kind, axes) -> {
            String[] molang = new String[3];
            boolean moves = false;
            for (int axis = 0; axis < 3; axis++) {
                molang[axis] = axes[axis] == null ? kind.identity() : compiler.compile(axes[axis]);
                if (molang[axis] == null) {
                    logger.debug("Could not compile {} {} axis {} of {}", bone, kind, axis, name);
                    molang[axis] = kind.identity();
                }
                moves |= !molang[axis].equals(kind.identity());
            }

            if (moves) {
                compiled.add(new Channel(bone, kind, axes, molang));
            }
        }));

        Map<String, String> parts = new LinkedHashMap<>();
        visibility.forEach((bone, value) -> {
            String molang = compiler.compile(value);
            if (molang == null) {
                logger.debug("Could not compile the visibility of {} in {}: {}", bone, name, value);
            } else if (!molang.equals("1")) {
                parts.put(bone, molang);
            }
        });

        String prefix = name.endsWith(SETUP_SUFFIX) ? name.substring(0, name.length() - SETUP_SUFFIX.length()) : name;
        List<KeyframeAnimation> keyframes = new ArrayList<>();
        List<String> preAnimation = new ArrayList<>();
        List<String> initialize = new ArrayList<>();
        for (KeyframeUse use : uses) {
            KeyframeAnimation keyframe = keyframe(use, keyframes.size(), prefix, compiler, state, bones, scale, preAnimation, initialize, logger);
            if (keyframe != null) {
                keyframes.add(keyframe);
            }
        }

        if (compiled.isEmpty() && parts.isEmpty() && keyframes.isEmpty()) {
            return null;
        }

        List<String> variables = new ArrayList<>(compiler.variables());
        variables.addAll(preAnimation);

        StringBuilder output = new StringBuilder();
        compiled.forEach(channel -> output.append(String.join(" ", channel.molang())).append(' '));
        variables.forEach(variable -> output.append(variable).append(' '));
        parts.values().forEach(condition -> output.append(condition).append(' '));
        keyframes.forEach(keyframe -> output.append(keyframe.weight()).append(' ').append(keyframe.time()).append(' '));
        List<StateProperty> used = state.properties().stream()
            .filter(property -> output.indexOf("'" + property.id() + "'") >= 0)
            .toList();

        return new CompiledAnimation(name, compiled, variables, initialize, parts, keyframes, used);
    }

    @Nullable
    private static KeyframeAnimation keyframe(@NotNull KeyframeUse use, int index, @NotNull String prefix, @NotNull MolangCompiler compiler,
                                              @NotNull StateResolver state, @NotNull BoneMap bones, float scale,
                                              @NotNull List<String> preAnimation, @NotNull List<String> initialize, @NotNull Logger logger) {
        String time = compiler.compile(use.time());
        String weight = compiler.compile(use.weight());
        String speed = compiler.compile(use.speed());
        if (time == null || weight == null || speed == null) {
            logger.debug("Could not compile when {} plays in {}: time={} weight={} speed={}", use.field(), prefix, use.time(), use.weight(), use.speed());
            return null;
        }

        if (use.state() != null) {
            // Playing while the AnimationState is started, from when its start tick last changed.
            Value marker = new Value.Conditional(
                new Value.InstanceCall(ANIMATION_STATE, "isStarted", use.state(), List.of()),
                new Value.InstanceField(use.state(), ANIMATION_STATE, "startTick"),
                Value.literal(-1));
            String hint = use.state() instanceof Value.InstanceField(Value _, String _, String field) ? field : null;
            String started = state.property(marker, hint);
            if (started == null) {
                logger.debug("Could not send the animation state {} plays with in {}", use.state(), prefix);
                return null;
            }

            String start = "v." + VARIABLE_PREFIX + "_k" + index + "_start";
            String last = "v." + VARIABLE_PREFIX + "_k" + index + "_last";
            initialize.add(last + " = -2;");
            preAnimation.add(start + " = (" + started + " != " + last + ") ? query.life_time : " + start + ";");
            preAnimation.add(last + " = " + started + ";");
            time = "((query.life_time - " + start + ") * " + speed + ")";
            weight = weight.equals("1") ? "(" + started + " >= 0)" : "((" + started + " >= 0) * " + weight + ")";
        }

        Map<String, Bones> animatedBones = new LinkedHashMap<>();
        use.definition().bones().forEach((part, partChannels) -> {
            BoneMap.Bone bone = bones.named(part);
            if (bone == null) {
                logger.debug("Keyframe animation {} in {} animates {}, which isn't in the model", use.field(), prefix, part);
                return;
            }

            Bones channels = animatedBones.computeIfAbsent(bone.name(), name -> new Bones());
            for (KeyframeDefinition.Channel channel : partChannels) {
                Map<String, Frame> frames = new LinkedHashMap<>();
                for (KeyframeDefinition.Frame frame : channel.frames()) {
                    frames.put(MolangCompiler.number(exact(frame.time())).text(), frame(channel.target(), frame, scale));
                }
                channel(channels, channel.target(), frames);
            }
        });

        Animations animation = new Animations();
        animation.loop(use.definition().looping() ? Boolean.TRUE : "hold_on_last_frame");
        animation.animationLength(use.definition().length());
        animation.animTimeUpdate(time);
        animation.bones(animatedBones);

        String key = "kf_" + index;
        return new KeyframeAnimation(key, prefix + "." + snake(use.field()), animation, weight, time);
    }

    private static Frame frame(@NotNull KeyframeDefinition.Target target, @NotNull KeyframeDefinition.Frame frame, float scale) {
        String[] pre = vector(target, frame.pre(), scale);
        String[] post = vector(target, frame.post(), scale);
        return new Frame(Arrays.equals(pre, post) ? null : pre, post, frame.catmullrom() ? "catmullrom" : null);
    }

    private static void channel(@NotNull Bones bone, @NotNull KeyframeDefinition.Target target, @NotNull Map<String, Frame> frames) {
        switch (target) {
            case POSITION -> {
                Map<String, Position> position = new LinkedHashMap<>();
                frames.forEach((time, frame) -> {
                    Position keyframe = new Position();
                    keyframe.pre(frame.pre());
                    keyframe.post(frame.post());
                    keyframe.lerpMode(frame.lerpMode());
                    position.put(time, keyframe);
                });
                bone.position(position);
            }
            case ROTATION -> {
                Map<String, Rotation> rotation = new LinkedHashMap<>();
                frames.forEach((time, frame) -> {
                    Rotation keyframe = new Rotation();
                    keyframe.pre(frame.pre());
                    keyframe.post(frame.post());
                    keyframe.lerpMode(frame.lerpMode());
                    rotation.put(time, keyframe);
                });
                bone.rotation(rotation);
            }
            case SCALE -> {
                Map<String, Scale> scale = new LinkedHashMap<>();
                frames.forEach((time, frame) -> {
                    Scale keyframe = new Scale();
                    keyframe.pre(frame.pre());
                    keyframe.post(frame.post());
                    keyframe.lerpMode(frame.lerpMode());
                    scale.put(time, keyframe);
                });
                bone.scale(scale);
            }
        }
    }

    // Java's position helper already negates Y, so its arguments are Bedrock's offsets as they are.
    private static String[] vector(@NotNull KeyframeDefinition.Target target, float @NotNull [] values, float scale) {
        String[] array = new String[3];
        for (int axis = 0; axis < 3; axis++) {
            float value = switch (target) {
                case ROTATION -> BedrockEntityModelConverter.ROTATION_SIGNS[axis] * values[axis];
                case POSITION -> values[axis] * scale;
                case SCALE -> values[axis];
            };
            array[axis] = MolangCompiler.number(exact(value)).text();
        }
        return array;
    }

    private static double exact(float value) {
        return Double.parseDouble(Float.toString(value));
    }

    static String snake(@NotNull String name) {
        return name.replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_]", "_");
    }

    private static Value transform(@NotNull Target target, @NotNull Value value, @NotNull EntityModel.Part part, float scale) {
        EntityModel.Pose pose = part.pose() == null ? EntityModel.Pose.ZERO : part.pose();
        return switch (target.kind()) {
            case ROTATION -> {
                double rest = target.axis() == 0 ? pose.xRot() : target.axis() == 1 ? pose.yRot() : pose.zRot();
                Value degrees = new Value.Operation(Value.Operator.MULTIPLY, List.of(value, Value.literal(MolangCompiler.DEGREES)));
                Value delta = new Value.Operation(Value.Operator.SUBTRACT, List.of(degrees, Value.literal(rest * MolangCompiler.DEGREES)));
                float sign = BedrockEntityModelConverter.ROTATION_SIGNS[target.axis()];
                yield sign == 1f ? delta : new Value.Operation(Value.Operator.MULTIPLY, List.of(delta, Value.literal(sign)));
            }
            case POSITION -> {
                double rest = target.axis() == 0 ? pose.x() : target.axis() == 1 ? pose.y() : pose.z();
                Value delta = new Value.Operation(Value.Operator.MULTIPLY, List.of(
                    new Value.Operation(Value.Operator.SUBTRACT, List.of(value, Value.literal(rest))), Value.literal(scale)));
                yield target.axis() == 1 ? new Value.Operation(Value.Operator.NEGATE, List.of(delta)) : delta;
            }
            case SCALE -> value;
        };
    }

    /**
     * Resolves what {@code setupAnim} reads from the render state it is given.
     */
    public interface StateResolver {

        /**
         * Gets the Molang for a render state field.
         *
         * @param field the field
         * @return the Molang, or null if it has no equivalent
         */
        @Nullable
        String resolve(@NotNull String field);

        /**
         * Gets the Molang reading a value computed from the render state, sent as an entity property.
         *
         * @param value the value
         * @param name what the property is named after, or null to name it after the value
         * @return the Molang, or null if it can't be sent
         */
        @Nullable
        default String property(@NotNull Value value, @Nullable String name) {
            return null;
        }

        /**
         * Gets every property {@link #property(Value, String)} has handed out.
         *
         * @return the properties
         */
        @NotNull
        default List<StateProperty> properties() {
            return List.of();
        }
    }

   private record KeyframeUse(@NotNull String field, @NotNull KeyframeDefinition definition, @NotNull Value time,
                               @NotNull Value weight, @NotNull Value speed, @Nullable Value state
   ) {
        private static final Value ONE = Value.literal(1f);

        @Nullable
        static KeyframeUse of(@NotNull Summary.Invocation invocation, @NotNull BoneMap bones) {
            if (!(invocation.call() instanceof Value.InstanceCall(String _, String method, Value receiver, List<Value> args))) {
                return null;
            }

            String field;
            Value definition;
            List<Value> rest;
            if (receiver instanceof Value.InstanceField(Value.This _, String _, String name)
                && (method.equals("applyWalk") || method.equals("apply") || method.equals("applyStatic"))) {
                field = name;
                definition = bones.field(name);
                rest = args;
            } else if (receiver instanceof Value.This && method.equals("animate") && args.size() >= 3) {
                // Model.animate(state, definition, ageInTicks[, speed]) predates baking.
                method = "apply";
                field = "keyframe";
                definition = args.get(1);
                rest = args.size() == 4 ? List.of(args.get(0), args.get(2), args.get(3)) : List.of(args.get(0), args.get(2));
            } else if (receiver instanceof Value.This && method.equals("animateWalk") && args.size() == 5) {
                method = "applyWalk";
                field = "keyframe";
                definition = args.get(0);
                rest = args.subList(1, 5);
            } else {
                return null;
            }

            KeyframeDefinition keyframes = KeyframeDefinition.read(definition);
            if (keyframes == null) {
                return null;
            }

            Value time;
            Value weight;
            Value speed = ONE;
            Value state = null;
            switch (method) {
                case "applyWalk" -> {
                    if (rest.size() != 4) {
                        return null;
                    }
                    // Java plays walk animations 50ms per unit walked, at a weight of how fast the walk is.
                    time = new Value.Operation(Value.Operator.MULTIPLY, List.of(
                        new Value.Operation(Value.Operator.MULTIPLY, List.of(rest.get(0), Value.literal(0.05f))), rest.get(2)));
                    weight = new Value.StaticCall("java/lang/Math", "min", List.of(
                        new Value.Operation(Value.Operator.MULTIPLY, List.of(rest.get(1), rest.get(3))), ONE));
                }
                case "apply" -> {
                    if (rest.size() < 2) {
                        return null;
                    }
                    time = Value.literal(0f);
                    weight = ONE;
                    speed = rest.size() == 3 ? rest.get(2) : ONE;
                    state = rest.get(0);
                }
                default -> {
                    time = Value.literal(0f);
                    weight = ONE;
                }
            }

            if (invocation.condition() != null) {
                weight = new Value.Conditional(invocation.condition(), weight, Value.literal(0f));
            }
            return new KeyframeUse(field, keyframes, time, weight, speed, state);
        }
    }

    private record Target(Channel.Kind kind, int axis) {
        static final String[] ROTATION = {"xRot", "yRot", "zRot"};
        static final String[] POSITION = {"x", "y", "z"};
        static final String[] SCALE = {"xScale", "yScale", "zScale"};

        @Nullable
        static Target of(@NotNull String field) {
            for (int axis = 0; axis < 3; axis++) {
                if (ROTATION[axis].equals(field)) {
                    return new Target(Channel.Kind.ROTATION, axis);
                }

                if (POSITION[axis].equals(field)) {
                    return new Target(Channel.Kind.POSITION, axis);
                }

                if (SCALE[axis].equals(field)) {
                    return new Target(Channel.Kind.SCALE, axis);
                }
            }
            return null;
        }
    }

    private record Frame(@NotNull String @Nullable [] pre, @NotNull String @NotNull [] post, @Nullable String lerpMode) {
    }

    private record Inputs(BoneMap bones, StateResolver state) implements MolangCompiler.Inputs {

        @Override
        public String state(@NotNull String field) {
            return this.state.resolve(field);
        }

        @Override
        public String property(@NotNull Value value) {
            return this.state.property(value, null);
        }

        @Override
        public Double rest(@NotNull Value receiver, @NotNull String field) {
            BoneMap.Bone bone = this.bones.bone(receiver);
            if (bone == null) {
                return null;
            }

            EntityModel.Pose pose = bone.part().pose() == null ? EntityModel.Pose.ZERO : bone.part().pose();
            return switch (field) {
                case "x" -> (double) pose.x();
                case "y" -> (double) pose.y();
                case "z" -> (double) pose.z();
                case "xRot" -> (double) pose.xRot();
                case "yRot" -> (double) pose.yRot();
                case "zRot" -> (double) pose.zRot();
                case "xScale", "yScale", "zScale", "visible" -> 1d;
                default -> null;
            };
        }
    }

    /**
     * A transform of one bone.
     *
     * @param bone the bone
     * @param kind what is transformed
     * @param values the value of each axis in Bedrock's space, or null where it is left alone
     * @param molang the Molang of each axis
     */
    public record Channel(@NotNull String bone, @NotNull Kind kind, @Nullable Value @NotNull [] values, @NotNull String @NotNull [] molang) {

        public enum Kind {
            ROTATION("0"),
            POSITION("0"),
            SCALE("1");

            private final String identity;

            Kind(String identity) {
                this.identity = identity;
            }

            String identity() {
                return this.identity;
            }
        }
    }

    /**
     * A compiled animation.
     *
     * @param name the animation's identifier
     * @param channels the transforms it applies
     * @param preAnimation the statements to run before it each frame
     * @param initialize the statements to run once when the entity is created
     * @param partVisibility the condition each bone is shown under, for bones that are ever hidden
     * @param keyframes the keyframe animations played alongside it
     * @param properties the entity properties it reads
     */
    public record CompiledAnimation(@NotNull String name, @NotNull List<Channel> channels,
                                    @NotNull List<String> preAnimation, @NotNull List<String> initialize,
                                    @NotNull Map<String, String> partVisibility, @NotNull List<KeyframeAnimation> keyframes,
                                    @NotNull List<StateProperty> properties
    ) {

        /**
         * Builds the animation file.
         *
         * @return the animation file's contents
         */
        @NotNull
        public ActorAnimation toAnimation() {
            Map<String, Bones> bones = new LinkedHashMap<>();
            for (Channel channel : this.channels) {
                Bones bone = bones.computeIfAbsent(channel.bone(), name -> new Bones());
                AnimationCompiler.channel(bone, KeyframeDefinition.Target.valueOf(channel.kind().name()), Map.of("0.0", new Frame(null, channel.molang(), null)));
            }

            Map<String, Animations> animations = new LinkedHashMap<>();
            if (!this.channels.isEmpty()) {
                Animations animation = new Animations();
                animation.loop(Boolean.TRUE);
                animation.bones(bones);
                animations.put(this.name, animation);
            }
            for (KeyframeAnimation keyframe : this.keyframes) {
                animations.put(keyframe.name(), keyframe.animation());
            }

            ActorAnimation file = new ActorAnimation();
            file.formatVersion("1.8.0");
            file.animations(animations);
            return file;
        }
    }

    /**
     * A keyframe animation played alongside the compiled one.
     *
     * @param key the short name the entity refers to it by
     * @param name the animation's identifier
     * @param animation the animation, as it is written to the animation file
     * @param weight how strongly it is applied, as Molang
     * @param time the time it is at, as Molang
     */
    public record KeyframeAnimation(@NotNull String key, @NotNull String name, @NotNull Animations animation,
                                    @NotNull String weight, @NotNull String time
    ) {
    }
}
