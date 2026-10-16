package org.geysermc.hydraulic.entity;

import org.geysermc.conduit.Value;
import org.geysermc.hydraulic.entity.animation.KeyframeDefinition;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyframeDefinitionTest {
    private static final String BUILDER = "net/minecraft/client/animation/AnimationDefinition$Builder";
    private static final String CHANNEL = "net/minecraft/client/animation/AnimationChannel";
    private static final String KEYFRAME = "net/minecraft/client/animation/Keyframe";

    @Test
    void readsABakedBuilderChain() {
        Value channel = new Value.Constructed(CHANNEL, List.of(
            new Value.Field(CHANNEL + "$Targets", "ROTATION"),
            new Value.Array("Lnet/minecraft/client/animation/Keyframe;", new ArrayList<>(List.of(
                keyframe(0f, 0f, "LINEAR"),
                keyframe(0.5f, -10f, "CATMULLROM")))))
        );
        Value built = new Value.InstanceCall(BUILDER, "build",
            new Value.InstanceCall(BUILDER, "addAnimation",
                new Value.InstanceCall(BUILDER, "looping", new Value.StaticCall(BUILDER, "withLength", List.of(Value.literal(1f))), List.of()),
                List.of(Value.literal("body"), channel)),
            List.of()
        );
        Value baked = new Value.InstanceCall("net/minecraft/client/animation/AnimationDefinition", "bake", built, List.of(new Value.Parameter(0)));

        KeyframeDefinition definition = KeyframeDefinition.read(baked);
        assertNotNull(definition);
        assertEquals(1f, definition.length());
        assertTrue(definition.looping());

        KeyframeDefinition.Channel body = definition.bones().get("body").getFirst();
        assertEquals(KeyframeDefinition.Target.ROTATION, body.target());
        assertEquals(2, body.frames().size());
        assertArrayEquals(new float[] {-10f, 0f, 0f}, body.frames().get(1).post());
        assertTrue(body.frames().get(1).catmullrom());
    }

    @Test
    void rejectsAnythingElse() {
        assertNull(KeyframeDefinition.read(new Value.Unknown("arith")));
        assertNull(KeyframeDefinition.read(null));
    }

    private static Value keyframe(float time, float x, String interpolation) {
        return new Value.Constructed(KEYFRAME, List.of(
            Value.literal(time),
            new Value.StaticCall("net/minecraft/client/animation/KeyframeAnimations", "degreeVec", List.of(Value.literal(x), Value.literal(0f), Value.literal(0f))),
            new Value.Field(CHANNEL + "$Interpolations", interpolation))
        );
    }
}
