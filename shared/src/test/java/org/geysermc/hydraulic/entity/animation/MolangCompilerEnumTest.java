package org.geysermc.hydraulic.entity.animation;

import org.geysermc.conduit.Value;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MolangCompilerEnumTest {
    private static final String STATE = "net/minecraft/client/renderer/entity/state/HumanoidRenderState";
    private static final RenderStateDefaults DEFAULTS = RenderStateDefaults.vanilla();
    private static final Value RIGHT = new Value.Constructed("net/minecraft/world/entity/HumanoidArm",
        List.of(Value.literal("RIGHT"), Value.literal(1), Value.literal(1), Value.literal("right"), Value.literal("options.mainHand.right")));
    private static final Value OFF_HAND = new Value.Constructed("net/minecraft/world/InteractionHand", List.of(Value.literal("OFF_HAND"), Value.literal(1), Value.literal(1)));

    @Test
    void armPoseOrdinalFoldsToEmpty() {
        Value ordinal = new Value.InstanceCall("java/lang/Enum", "ordinal", field("rightArmPose"), List.of());
        assertEquals("1", compile(new Value.Operation(Value.Operator.EQUAL, List.of(ordinal, Value.literal(0)))));
    }

    @Test
    void armChosenAtRuntimeIsComparedPerBranch() {
        Value arm = new Value.Conditional(field("isUsingItem"), asArm(field("useItemHand")), asArm(OFF_HAND));
        assertEquals("(query.is_using_item ? 1 : 0)", compile(new Value.Operation(Value.Operator.EQUAL, List.of(arm, RIGHT))));
    }

    @Test
    void missingSwingIsNull() {
        assertEquals("0", compile(new Value.Operation(Value.Operator.NOT_EQUAL, List.of(field("currentSwing"), Value.literal(null)))));
    }

    @NotNull
    private static Value field(@NotNull String name) {
        return new Value.InstanceField(new Value.Parameter(0), STATE, name);
    }

    @NotNull
    private static Value asArm(@NotNull Value hand) {
        return new Value.InstanceCall("net/minecraft/world/InteractionHand", "asArm", hand, List.of(field("mainArm")));
    }

    private static String compile(@NotNull Value value) {
        MolangCompiler compiler = new MolangCompiler(new MolangCompiler.Inputs() {
            @Override
            public String state(@NotNull String field) {
                return RenderStateQueries.query(field);
            }

            @Override
            public Double rest(@NotNull Value receiver, @NotNull String field) {
                return null;
            }

            @Override
            public String property(@NotNull Value value) {
                return null;
            }

            @Override
            public Value idle(@NotNull String owner, @NotNull String member) {
                return DEFAULTS.idle(owner, member);
            }
        }, "t");
        compiler.add(value);
        return compiler.compile(value);
    }
}
