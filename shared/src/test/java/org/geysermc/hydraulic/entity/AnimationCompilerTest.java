package org.geysermc.hydraulic.entity;

import org.cloudburstmc.mojava.MoJava;
import org.cloudburstmc.mojava.runtime.MoRuntime;
import org.geysermc.conduit.ConduitContext;
import org.geysermc.conduit.Summary;
import org.geysermc.conduit.Value;
import org.geysermc.conduit.VirtualMachine;
import org.geysermc.conduit.VirtualMachineFactory;
import org.geysermc.hydraulic.entity.animation.AnimationCompiler;
import org.geysermc.hydraulic.entity.animation.BoneMap;
import org.geysermc.hydraulic.entity.animation.RenderStateQueries;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnimationCompilerTest {
    private static final Map<String, String> QUERIES = Map.ofEntries(
        Map.entry("walkAnimationPos", "modified_distance_moved"),
        Map.entry("walkAnimationSpeed", "modified_move_speed"),
        Map.entry("yRot", "target_y_rotation"),
        Map.entry("xRot", "target_x_rotation"),
        Map.entry("bodyRot", "body_y_rotation"),
        Map.entry("deathTime", "death_ticks"),
        Map.entry("isBaby", "is_baby"),
        Map.entry("isInWater", "is_in_water"),
        Map.entry("isDiscrete", "is_sneaking"),
        Map.entry("isInvisible", "is_invisible"),
        Map.entry("displayFireAnimation", "is_on_fire")
    );

    private static final Map<String, Compiled> compiled = new HashMap<>();

    @BeforeAll
    static void compile() {
        ConduitContext context = TestMod.context();
        VirtualMachine vm = VirtualMachineFactory.create(context).await();
        for (CustomEntity entity : TestMod.scan(context).values()) {
            Summary constructor = vm.summarize(entity.binding().modelClass(), "<init>", null);
            Summary setupAnim = vm.summarize(entity.binding().modelClass(), "setupAnim", null);
            assertNotNull(constructor);
            assertNotNull(setupAnim);
            BoneMap bones = BoneMap.of(constructor, entity.model());
            AnimationCompiler.CompiledAnimation animation = AnimationCompiler.compile("animation." + entity.id().getPath(), setupAnim, bones,
                entity.model().scale(), RenderStateQueries::query, LoggerFactory.getLogger("test"));
            compiled.put(entity.id().getPath(), new Compiled(animation, bones));
        }
    }

    @Test
    void critterWalks() {
        AnimationCompiler.CompiledAnimation critter = compiled.get("critter").animation();
        assertNotNull(critter);
        AnimationCompiler.Channel leg = critter.channels().stream()
            .filter(channel -> channel.bone().equals("leg0") && channel.kind() == AnimationCompiler.Channel.Kind.ROTATION)
            .findFirst().orElseThrow(() -> new AssertionError(critter.channels()));
        assertTrue(leg.molang()[0].contains("math.cos") && leg.molang()[0].contains("query.modified_distance_moved"), leg.molang()[0]);
        assertEquals("0", leg.molang()[1]);
    }

    @Test
    void molangMatchesJava() {
        Random random = new Random(1);
        int checked = 0;
        List<String> mismatches = new ArrayList<>();
        for (Map.Entry<String, Compiled> entry : compiled.entrySet()) {
            AnimationCompiler.CompiledAnimation animation = entry.getValue().animation();
            if (animation == null) {
                continue;
            }

            for (int sample = 0; sample < 200; sample++) {
                Map<String, Double> state = sample(random);
                MoRuntime runtime = runtime(state);
                for (String statement : animation.preAnimation()) {
                    runtime.execute(MoJava.parse(statement));
                }

                for (AnimationCompiler.Channel channel : animation.channels()) {
                    for (int axis = 0; axis < 3; axis++) {
                        Value value = channel.values()[axis];
                        if (value == null || channel.molang()[axis].equals(channel.kind() == AnimationCompiler.Channel.Kind.SCALE ? "1" : "0")
                            && !(value instanceof Value.Literal)) {
                            continue;
                        }

                        double expected = evaluate(value, state, entry.getValue().bones());
                        double actual = runtime.execute(MoJava.parse(channel.molang()[axis])).asDouble();
                        checked++;
                        if (Math.abs(expected - actual) > 1e-3 * Math.max(1, Math.abs(expected))) {
                            mismatches.add(entry.getKey() + " " + channel.bone() + " " + channel.kind() + "[" + axis + "] expected "
                                + expected + " got " + actual + " for " + channel.molang()[axis]);
                        }
                    }
                }
            }
        }

        int total = checked;
        assertTrue(total > 1000, () -> "only checked " + total);
        assertTrue(mismatches.isEmpty(), () -> mismatches.size() + " mismatches, first: " + mismatches.subList(0, Math.min(5, mismatches.size())));
    }

    private static Map<String, Double> sample(Random random) {
        Map<String, Double> state = new HashMap<>();
        state.put("walkAnimationPos", random.nextDouble() * 100);
        state.put("walkAnimationSpeed", random.nextDouble());
        state.put("ageInTicks", random.nextDouble() * 1000);
        state.put("yRot", random.nextDouble() * 120 - 60);
        state.put("xRot", random.nextDouble() * 90 - 45);
        state.put("bodyRot", random.nextDouble() * 360);
        state.put("deathTime", (double) random.nextInt(20));
        for (String flag : List.of("isBaby", "isInWater", "isDiscrete", "isInvisible", "displayFireAnimation")) {
            state.put(flag, random.nextBoolean() ? 1d : 0d);
        }

        return state;
    }

    private static MoRuntime runtime(Map<String, Double> state) {
        MoRuntime runtime = MoJava.createRuntime();
        QUERIES.forEach((field, query) -> runtime.getEnvironment().query.addDoubleFunction(query, _ -> state.get(field)));
        runtime.getEnvironment().query.addDoubleFunction("life_time", _ -> state.get("ageInTicks") / 20);
        return runtime;
    }

    private static double evaluate(Value value, Map<String, Double> state, BoneMap bones) {
        return switch (value) {
            case Value.Literal(Number n) -> n instanceof Float f ? Double.parseDouble(f.toString()) : n.doubleValue();
            case Value.Literal(Boolean b) -> b ? 1 : 0;
            case Value.InstanceField(Value.Parameter _, String _, String field) -> {
                String query = RenderStateQueries.query(field);
                if (state.containsKey(field)) {
                    yield state.get(field);
                }
                yield Double.parseDouble(query.replaceAll("[()]", ""));
            }
            case Value.InstanceField(Value receiver, String _, String field) -> rest(bones.bone(receiver).part(), field);
            case Value.Conditional(Value condition, Value then, Value otherwise) ->
                evaluate(condition, state, bones) != 0 ? evaluate(then, state, bones) : evaluate(otherwise, state, bones);
            case Value.StaticCall(String _, String name, List<Value> args) -> {
                double a = evaluate(args.getFirst(), state, bones);
                yield switch (name) {
                    case "sin" -> Math.sin(a);
                    case "cos" -> Math.cos(a);
                    case "abs" -> Math.abs(a);
                    case "sqrt" -> Math.sqrt(a);
                    case "min" -> Math.min(a, evaluate(args.get(1), state, bones));
                    case "max" -> Math.max(a, evaluate(args.get(1), state, bones));
                    case "clamp" -> Math.min(evaluate(args.get(2), state, bones), Math.max(a, evaluate(args.get(1), state, bones)));
                    default -> throw new IllegalStateException(name);
                };
            }
            case Value.Operation(Value.Operator operator, List<Value> operands) -> {
                double a = evaluate(operands.getFirst(), state, bones);
                if (operands.size() == 1) {
                    yield switch (operator) {
                        case NEGATE -> -a;
                        case LOGICAL_NOT -> a == 0 ? 1 : 0;
                        case TRUNCATE -> (long) a;
                        default -> throw new IllegalStateException(operator.name());
                    };
                }

                if (operator == Value.Operator.LOGICAL_AND || operator == Value.Operator.LOGICAL_OR) {
                    boolean and = operator == Value.Operator.LOGICAL_AND;
                    for (Value operand : operands) {
                        if ((evaluate(operand, state, bones) != 0) != and) {
                            yield and ? 0 : 1;
                        }
                    }
                    yield and ? 1 : 0;
                }

                double b = evaluate(operands.get(1), state, bones);
                yield switch (operator) {
                    case ADD -> a + b;
                    case SUBTRACT -> a - b;
                    case MULTIPLY -> a * b;
                    case DIVIDE -> a / b;
                    case REMAINDER -> a % b;
                    case EQUAL -> a == b ? 1 : 0;
                    case NOT_EQUAL -> a != b ? 1 : 0;
                    case LESS -> a < b ? 1 : 0;
                    case LESS_OR_EQUAL -> a <= b ? 1 : 0;
                    case GREATER -> a > b ? 1 : 0;
                    case GREATER_OR_EQUAL -> a >= b ? 1 : 0;
                    default -> throw new IllegalStateException(operator.name());
                };
            }
            default -> throw new IllegalStateException(String.valueOf(value));
        };
    }

    private static double rest(JavaEntityModel.Part part, String field) {
        JavaEntityModel.Pose pose = part.pose() == null ? JavaEntityModel.Pose.ZERO : part.pose();
        return switch (field) {
            case "x" -> pose.x();
            case "y" -> pose.y();
            case "z" -> pose.z();
            case "xRot" -> pose.xRot();
            case "yRot" -> pose.yRot();
            case "zRot" -> pose.zRot();
            default -> 1;
        };
    }

    private record Compiled(AnimationCompiler.CompiledAnimation animation, BoneMap bones) {
    }
}
