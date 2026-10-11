package org.geysermc.hydraulic.entity;

import org.geysermc.hydraulic.entity.animation.StateProperty;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityScannerTest {
    private static Map<String, CustomEntity> entities;

    @BeforeAll
    static void scan() {
        entities = TestMod.scan(TestMod.context());
    }

    @Test
    void sharedRendererBindsItsModelReference() {
        CustomEntity critter = entities.get("critter");
        assertNotNull(critter, entities.keySet()::toString);
        assertNotNull(critter.binding());
        assertEquals(TestMod.CLIENT + "CritterModel", critter.binding().modelClass());
        assertEquals("critter", critter.model().path());
        assertEquals(6, critter.model().parts().size());
    }

    @Test
    void variantTexturesAreComputedFromTheEntity() {
        CustomEntity critter = entities.get("critter");
        assertEquals(2, critter.textures().size(), critter.textures()::toString);
        assertNotNull(critter.texture());
        assertTrue(critter.texture().toString().contains("getVariant()"), critter.texture()::toString);
    }

    @Test
    void dedicatedRendererBindsItsModel() {
        CustomEntity golem = entities.get("golem");
        assertNotNull(golem, entities.keySet()::toString);
        assertNotNull(golem.binding());
        assertEquals(TestMod.CLIENT + "GolemModel", golem.binding().modelClass());
        assertEquals(1, golem.textures().size(), golem.textures()::toString);
    }

    @Test
    void customRenderStateBecomesAPropertyComputedFromTheEntity() {
        CustomEntity golem = entities.get("golem");
        assertNotNull(golem.animation());
        StateProperty charging = golem.animation().properties().stream()
            .filter(property -> property.id().equals("hydraulic_test_mod:charging"))
            .findFirst().orElseThrow(() -> new AssertionError(golem.animation().properties()));
        assertTrue(charging.value().toString().contains("isCharging()"), charging.value()::toString);
        assertTrue(golem.animation().partVisibility().values().stream().anyMatch(condition -> condition.contains("'hydraulic_test_mod:charging'")),
            golem.animation().partVisibility()::toString);
    }

    @Test
    void keyframeAnimationsArePlayedAndTheirStateIsStartedOnTheServer() {
        CustomEntity golem = entities.get("golem");
        assertNotNull(golem.animation());
        assertEquals(2, golem.animation().keyframes().size(), golem.animation().keyframes()::toString);
        assertFalse(golem.clientCalls().isEmpty());
    }
}
