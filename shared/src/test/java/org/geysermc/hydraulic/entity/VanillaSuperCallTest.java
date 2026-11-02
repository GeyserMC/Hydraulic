package org.geysermc.hydraulic.entity;

import org.geysermc.conduit.Summary;
import org.geysermc.conduit.Value;
import org.geysermc.hydraulic.entity.animation.Values;
import org.geysermc.hydraulic.entity.vanilla.VanillaModels;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VanillaSuperCallTest {
    private static final String HUMANOID = "net/minecraft/client/model/HumanoidModel";
    private static final String MOD_MODEL = "mod/GuardModel";
    private static final String PART = "net/minecraft/client/model/geom/ModelPart";

    @Test
    void constructorGainsTheVanillaParts() {
        Value nose = new Value.InstanceCall(PART, "getChild", field("head"), List.of(Value.literal("nose")));
        Summary constructor = new Summary(null,
            List.of(new Summary.Assignment(new Value.This(), MOD_MODEL, "nose", nose)),
            List.of(new Summary.Invocation(null, new Value.InstanceCall(HUMANOID, "<init>", new Value.This(), List.of(new Value.Parameter(0))))));

        Summary inlined = VanillaModels.get().inlineSuperCall(constructor, "<init>");
        assertNotNull(inlined.assigned(new Value.This(), "head"), inlined::toString);
        assertTrue(inlined.invocations().isEmpty(), inlined::toString);
    }

    @Test
    void setupAnimReadsWhatTheVanillaCallAssigned() {
        Value kicked = new Value.Operation(Value.Operator.ADD, List.of(new Value.InstanceField(field("rightLeg"), PART, "xRot"), Value.literal(1f)));
        Summary setupAnim = new Summary(null,
            List.of(new Summary.Assignment(field("rightLeg"), PART, "xRot", kicked)),
            List.of(new Summary.Invocation(null, new Value.InstanceCall(HUMANOID, "setupAnim", new Value.This(), List.of(new Value.Parameter(0))))));

        Summary inlined = VanillaModels.get().inlineSuperCall(setupAnim, "setupAnim");
        Value rightLeg = inlined.assignments().stream()
            .filter(assignment -> assignment.field().equals("xRot") && assignment.target() instanceof Value.InstanceField(Value.This _, String _, String name) && name.equals("rightLeg"))
            .findFirst().orElseThrow().value();

        assertFalse(Values.contains(rightLeg, value -> value instanceof Value.InstanceField(Value.InstanceField(Value.This _, String _, String part), String _, String name)
            && part.equals("rightLeg") && name.equals("xRot")), rightLeg::toString);
        assertTrue(Values.contains(rightLeg, value -> value instanceof Value.InstanceField(Value.Parameter _, String _, String name) && name.equals("walkAnimationPos")), rightLeg::toString);
    }

    private static Value field(String name) {
        return new Value.InstanceField(new Value.This(), MOD_MODEL, name);
    }
}
