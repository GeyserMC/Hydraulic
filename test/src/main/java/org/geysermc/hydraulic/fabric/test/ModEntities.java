package org.geysermc.hydraulic.fabric.test;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.geysermc.hydraulic.fabric.test.entity.Critter;
import org.geysermc.hydraulic.fabric.test.entity.Golem;

public class ModEntities {
    public static final ResourceKey<EntityType<?>> CRITTER_KEY = key("critter");
    public static final EntityType<Critter> CRITTER = Registry.register(BuiltInRegistries.ENTITY_TYPE, CRITTER_KEY,
            EntityType.Builder.of(Critter::new, MobCategory.CREATURE).sized(0.5F, 0.5F).build(CRITTER_KEY));

    public static final ResourceKey<EntityType<?>> GOLEM_KEY = key("golem");
    public static final EntityType<Golem> GOLEM = Registry.register(BuiltInRegistries.ENTITY_TYPE, GOLEM_KEY,
            EntityType.Builder.of(Golem::new, MobCategory.CREATURE).sized(0.8F, 1.6F).build(GOLEM_KEY));

    private static ResourceKey<EntityType<?>> key(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(HydraulicTestMod.MOD_ID, name));
    }

    public static void init() {
        FabricDefaultAttributeRegistry.register(CRITTER, Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.25));
        FabricDefaultAttributeRegistry.register(GOLEM, Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.2));
    }
}
