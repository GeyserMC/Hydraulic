package org.geysermc.hydraulic.entity.geckolib;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeckoLibPackTest {
    private static final String GEOMETRY = """
        {"format_version": "1.12.0", "minecraft:geometry": [
            {"description": {"identifier": "geometry.unknown", "texture_width": 64, "texture_height": 64}, "bones": [{"name": "head"}, {"name": "Right Arm", "parent": "head"}]},
            {"description": {"identifier": "geometry.other"}}
        ]}
        """;

    private static final String ANIMATIONS = """
        {"format_version": "1.8.0", "animations": {
            "idle": {"loop": true, "animation_length": 2, "bones": {"head": {
                "rotation": {"0.0": {"post": {"vector": [1, 2, 3]}, "lerp_mode": "catmullrom"}, "1.0": {"vector": [4, 5, 6], "easing": "easeInSine"}},
                "scale": {"vector": [1, 1, 1]}
            }}},
            "attack": {"loop": "hold_on_last_frame", "animation_length": 0.75, "bones": {"Right Arm": {"rotation": [0, 0, 0]}, "head": {"rotation": {"0.0": [0, 0, 0], "0.75": [10, 0, 0]}}}},
            "hurt": {"animation_length": 0.5}
        }}
        """;

    private static GeckoLibEntity entity(List<String> heads) {
        JsonObject animations = JsonParser.parseString(ANIMATIONS).getAsJsonObject();
        return new GeckoLibEntity(
                Identifier.fromNamespaceAndPath("mod", "beast"), JsonParser.parseString(GEOMETRY).getAsJsonObject(),
                "mod:textures/entity/beast.png", animations, heads
        );
    }

    @Test
    void clipsKeepOrderLoopAndLength() {
        List<GeckoLibEntity.Clip> clips = entity(List.of()).clips();
        assertEquals(List.of("idle", "attack", "hurt"), clips.stream().map(GeckoLibEntity.Clip::name).toList());
        assertEquals(GeckoLibEntity.Loop.LOOP, clips.get(0).loop());
        assertEquals(GeckoLibEntity.Loop.HOLD, clips.get(1).loop());
        assertEquals(0.75, clips.get(1).length());
        assertEquals(GeckoLibEntity.Loop.ONCE, clips.get(2).loop());
    }

    @Test
    void geometryIsRenamedAndAlone() {
        JsonArray geometries = GeckoLibPack.geometry(entity(List.of())).getAsJsonArray("minecraft:geometry");
        assertEquals(1, geometries.size());
        assertEquals("geometry.mod.beast", geometries.get(0).getAsJsonObject().getAsJsonObject("description").get("identifier").getAsString());
    }

    @Test
    void boneNamesHaveNoSpaces() {
        GeckoLibEntity entity = entity(List.of());
        JsonObject arm = GeckoLibPack.geometry(entity).getAsJsonArray("minecraft:geometry").get(0).getAsJsonObject().getAsJsonArray("bones").get(1).getAsJsonObject();
        assertEquals("Right_Arm", arm.get("name").getAsString());
        assertEquals("head", arm.get("parent").getAsString());

        JsonObject bones = GeckoLibPack.animations(entity).getAsJsonObject("animations")
                .getAsJsonObject("animation.mod.beast.clip" + GeckoLibEntity.state(1, GeckoLibEntity.Loop.HOLD)).getAsJsonObject("bones");
        assertTrue(bones.has("Right_Arm"));
        assertFalse(bones.has("Right Arm"));
    }

    @Test
    void everyClipIsWrittenOncePerLoop() {
        GeckoLibEntity entity = entity(List.of());
        JsonObject animations = GeckoLibPack.animations(entity).getAsJsonObject("animations");
        assertEquals(entity.states() - 1, animations.size());

        JsonObject attackLooping = animations.getAsJsonObject("animation.mod.beast.clip" + GeckoLibEntity.state(1, GeckoLibEntity.Loop.LOOP));
        assertNotNull(attackLooping);
        assertTrue(attackLooping.get("loop").getAsBoolean());
        assertTrue(attackLooping.has("bones"));
        assertFalse(animations.getAsJsonObject("animation.mod.beast.clip" + GeckoLibEntity.state(0, GeckoLibEntity.Loop.ONCE)).get("loop").getAsBoolean());
    }

    @Test
    void geckoLibKeyframesAreUnwrapped() {
        JsonObject head = GeckoLibPack.animations(entity(List.of())).getAsJsonObject("animations")
                .getAsJsonObject("animation.mod.beast.clip" + GeckoLibEntity.state(0, GeckoLibEntity.Loop.LOOP))
                .getAsJsonObject("bones").getAsJsonObject("head");
        JsonObject rotation = head.getAsJsonObject("rotation");
        assertEquals("[1,2,3]", rotation.getAsJsonObject("0.0").get("post").toString());
        assertEquals("catmullrom", rotation.getAsJsonObject("0.0").get("lerp_mode").getAsString());
        assertEquals("[4,5,6]", rotation.get("1.0").toString());
        assertEquals("[1,1,1]", head.get("scale").toString());
    }

    @Test
    void controllersFlipBetweenTwoStates() {
        GeckoLibEntity entity = entity(List.of());
        JsonObject controllers = GeckoLibPack.controllers(entity).getAsJsonObject("animation_controllers");
        assertEquals(GeckoLibEntity.CONTROLLERS, controllers.size());

        JsonObject states = controllers.getAsJsonObject("controller.animation.mod.beast.geckolib_0").getAsJsonObject("states");
        assertEquals(entity.states() - 1, states.getAsJsonObject("a").getAsJsonArray("animations").size());
        assertEquals("math.mod(query.property('mod:geckolib_0'), 2) == 1", states.getAsJsonObject("a").getAsJsonArray("transitions").get(0).getAsJsonObject().get("b").getAsString());
    }

    @Test
    void headsLookAtTheirTarget() {
        JsonObject animations = GeckoLibPack.animations(entity(List.of("head"))).getAsJsonObject("animations");
        JsonArray rotation = animations.getAsJsonObject("animation.mod.beast.look_at").getAsJsonObject("bones").getAsJsonObject("head").getAsJsonArray("rotation");
        assertEquals("query.target_x_rotation", rotation.get(0).getAsString());
    }
}
