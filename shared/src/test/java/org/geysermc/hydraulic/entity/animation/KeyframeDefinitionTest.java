package org.geysermc.hydraulic.entity.animation;

import org.geysermc.conduit.Summary;
import org.geysermc.conduit.Value;
import org.geysermc.hydraulic.entity.JavaEntityModel;
import org.geysermc.pack.bedrock.resource.animations.actoranimation.animations.bones.Rotation;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    void linearKeyframesCarryTheirPre() {
        Value state = new Value.InstanceField(new Value.Parameter(0), "", "idleAnimationState");
        Summary setupAnim = new Summary(null, List.of(), List.of(new Summary.Invocation(null,
                new Value.InstanceCall("", "animate", new Value.This(), List.of(state, bodyAnimation(), Value.literal(0f))))
        ));

        JavaEntityModel model = new JavaEntityModel("mod", "test", "main", 64, 64,
                List.of(new JavaEntityModel.Part("body", JavaEntityModel.Pose.ZERO, List.of(), List.of())), 1f
        );

        AnimationCompiler.StateResolver resolver = new AnimationCompiler.StateResolver() {
            @Override
            public String resolve(@NotNull String field) {
                return null;
            }

            @Override
            public String property(@NotNull Value value, String name) {
                return "query.property('mod:idle')";
            }
        };

        AnimationCompiler.CompiledAnimation animation = AnimationCompiler.compile("animation.test.setup", setupAnim,
                BoneMap.of(new Summary(null, List.of(), List.of()), model), 1f, resolver, LoggerFactory.getLogger("test")
        );

        assertNotNull(animation);
        Map<String, Rotation> frames = animation.keyframes().getFirst().animation().bones().get("body").rotation();
        assertArrayEquals(frames.get("0").post(), frames.get("0").pre());
        assertNull(frames.get("0.5").pre());
    }

    @Test
    void rejectsAnythingElse() {
        assertNull(KeyframeDefinition.read(new Value.Unknown("arith")));
        assertNull(KeyframeDefinition.read(null));
    }

    private static Value bodyAnimation() {
        Value channel = new Value.Constructed(CHANNEL, List.of(
                new Value.Field(CHANNEL + "$Targets", "ROTATION"),
                new Value.Array("Lnet/minecraft/client/animation/Keyframe;", new ArrayList<>(List.of(
                        keyframe(0f, 0f, "LINEAR"),
                        keyframe(0.5f, -10f, "CATMULLROM")))
                )
        ));

        return new Value.InstanceCall(BUILDER, "build",
                new Value.InstanceCall(BUILDER, "addAnimation",
                        new Value.InstanceCall(BUILDER, "looping", new Value.StaticCall(BUILDER, "withLength", List.of(Value.literal(1f))), List.of()),
                        List.of(Value.literal("body"), channel)
                ),
                List.of()
        );
    }

    private static Value keyframe(float time, float x, String interpolation) {
        return new Value.Constructed(KEYFRAME, List.of(
            Value.literal(time),
            new Value.StaticCall("net/minecraft/client/animation/KeyframeAnimations", "degreeVec", List.of(Value.literal(x), Value.literal(0f), Value.literal(0f))),
            new Value.Field(CHANNEL + "$Interpolations", interpolation))
        );
    }
}
