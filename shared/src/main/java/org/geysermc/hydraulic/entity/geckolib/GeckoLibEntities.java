package org.geysermc.hydraulic.entity.geckolib;

import org.geysermc.geyser.api.entity.custom.CustomEntityDefinition;
import org.geysermc.geyser.api.entity.property.type.GeyserIntEntityProperty;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineEntitiesEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineEntityPropertiesEvent;
import org.geysermc.geyser.api.util.Identifier;
import org.geysermc.hydraulic.entity.runtime.PropertySync;
import org.geysermc.pack.bedrock.resource.BedrockResourcePack;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;

/**
 * Converts the entities mods draw with GeckoLib and registers them with Geyser.
 */
public final class GeckoLibEntities {
    private final Map<String, List<GeckoLibEntity>> entities = new ConcurrentHashMap<>();

    /**
     * Pre-processes a mod's GeckoLib entities, storing them for later use.
     *
     * @param mod the mod's id
     * @param entities the entities
     * @param logger the logger to report them to
     */
    public void preProcess(@NotNull String mod, @NotNull List<GeckoLibEntity> entities, @NotNull Logger logger) {
        this.entities.put(mod, List.copyOf(entities));
        for (GeckoLibEntity entity : entities) {
            logger.info("Found GeckoLib entity {} with {} animation(s)", entity.id(), entity.clips().size());
        }
    }

    /**
     * Gets the identifiers of a mod's GeckoLib entities.
     *
     * @param mod the mod's id
     * @return the identifiers
     */
    @NotNull
    public List<String> ids(@NotNull String mod) {
        return this.entities.getOrDefault(mod, List.of()).stream().map(entity -> entity.id().toString()).toList();
    }

    /**
     * Processes a mod's GeckoLib entities.
     *
     * @param mod the mod's id
     * @param pack the pack
     * @param textures turns a texture, as {@code namespace:textures/...png}, into its path in the pack
     * @param logger the logger to report them to
     */
    public void postProcess(@NotNull String mod, @NotNull BedrockResourcePack pack, @NotNull UnaryOperator<String> textures, @NotNull Logger logger) {
        List<GeckoLibEntity> entities = this.entities.getOrDefault(mod, List.of());
        if (!entities.isEmpty()) {
            logger.info("GeckoLib entities to convert: {} in mod {}", entities.size(), mod);
        }

        for (GeckoLibEntity entity : entities) {
            GeckoLibPack.write(pack, entity, textures.apply(entity.texture()));
        }
    }

    /**
     * Registers the properties a mod's GeckoLib entities are animated from, and sends them.
     *
     * @param mod the mod's id
     * @param event the event to register them in
     * @param sync what sends them
     */
    public void defineProperties(@NotNull String mod, @NotNull GeyserDefineEntityPropertiesEvent event, @NotNull PropertySync sync) {
        List<GeckoLibEntity> entities = this.entities.getOrDefault(mod, List.of());
        for (GeckoLibEntity entity : entities) {
            Identifier id = Identifier.of(entity.id().toString());
            sync.clear(entity.id());

            GeckoLibAnimator animator = new GeckoLibAnimator(entity);
            for (int slot = 0; slot < GeckoLibEntity.CONTROLLERS; slot++) {
                GeyserIntEntityProperty property = event.registerIntegerProperty(
                        id, Identifier.of(GeckoLibPack.property(entity.id(), slot)),
                        0, entity.states() * 2 - 1, 0
                );

                sync.addProperty(entity.id(), property, animator.slot(slot));
            }
        }
    }

    /**
     * Registers a mod's GeckoLib entities with Geyser.
     *
     * @param mod the mod's id
     * @param event the event to register them in
     * @param logger the logger to report them to
     */
    public void defineEntities(@NotNull String mod, @NotNull GeyserDefineEntitiesEvent event, @NotNull Logger logger) {
        List<GeckoLibEntity> entities = this.entities.getOrDefault(mod, List.of());
        for (GeckoLibEntity entity : entities) {
            event.register(CustomEntityDefinition.of(entity.id().toString()));

            logger.debug("Registered GeckoLib entity {}", entity.id());
        }
    }
}
