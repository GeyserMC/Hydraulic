package org.geysermc.hydraulic.entity.animation;

import org.geysermc.conduit.Value;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RenderStateDefaultsTest {
    private static final String STATES = "net/minecraft/client/renderer/entity/state/";
    private static final RenderStateDefaults DEFAULTS = RenderStateDefaults.vanilla();

    @Test
    void idleOverrideWinsOverTheConstructor() {
        Value pose = DEFAULTS.idle(STATES + "IllagerRenderState", "armPose");
        assertTrue(pose instanceof Value.Constructed(String _, List<Value> args) && args.getFirst().equals(Value.literal("CROSSED")), String.valueOf(pose));
    }

    @Test
    void constructorValueIsFoundThroughSubclasses() {
        assertEquals(Value.literal(1f), DEFAULTS.idle(STATES + "ZombieRenderState", "speedValue"));
    }

    @Test
    void unassignedFieldIsItsZeroValue() {
        assertEquals(Value.literal(false), DEFAULTS.idle(STATES + "HumanoidRenderState", "isPassenger"));
    }
}
