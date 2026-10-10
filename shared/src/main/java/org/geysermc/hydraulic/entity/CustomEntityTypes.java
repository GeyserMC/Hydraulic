package org.geysermc.hydraulic.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import org.geysermc.geyser.api.util.Identifier;
import org.geysermc.geyser.entity.BedrockEntityDefinition;
import org.geysermc.geyser.entity.GeyserEntityType;
import org.geysermc.geyser.entity.VanillaEntityType;
import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.registry.Registries;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// TODO: Temporary class for holding custom entity types until we have a proper registry system in place. This is a stopgap
//       solution to allow for custom entities to be registered and used in the game.
public final class CustomEntityTypes {
    public static final int VANILLA_TYPE_COUNT = EntityType.values().length;

    public static final EntityType PLACEHOLDER = EntityType.ALLAY;

    private static final Map<net.minecraft.resources.Identifier, GeyserEntityType> TYPES = new ConcurrentHashMap<>();

    private CustomEntityTypes() {
    }

    public static boolean isCustom(int javaTypeId) {
        return javaTypeId >= VANILLA_TYPE_COUNT;
    }

    @Nullable
    public static GeyserEntityType byJavaId(int javaTypeId) {
        net.minecraft.world.entity.EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.byId(javaTypeId);
        net.minecraft.resources.Identifier key = BuiltInRegistries.ENTITY_TYPE.getKey(type);

        return TYPES.computeIfAbsent(key, id -> define(id, type));
    }

    @Nullable
    private static GeyserEntityType define(net.minecraft.resources.Identifier key, net.minecraft.world.entity.EntityType<?> type) {
        Identifier identifier = Identifier.of(key.toString());
        BedrockEntityDefinition bedrock = Registries.BEDROCK_ENTITY_DEFINITIONS.get(identifier);
        if (bedrock == null) {
            return null;
        }

        GeyserEntityType geyserType = new GeyserEntityType(identifier, PLACEHOLDER);

        Registries.JAVA_ENTITY_TYPES.get().putIfAbsent(geyserType, new VanillaEntityType<>(
            Entity::new,
            geyserType,
            type.getWidth(),
            type.getHeight(),
            0f,
            bedrock,
            List.of()
        ));

        return geyserType;
    }

    public static int javaTypeId(@NotNull Object packet) {
        return packet instanceof JavaEntityTypeHolder holder ? holder.hydraulic$javaTypeId() : -1;
    }
}
