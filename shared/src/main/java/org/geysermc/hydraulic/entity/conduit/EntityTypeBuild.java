package org.geysermc.hydraulic.entity.conduit;

/**
 * An entity type being built, such as {@code EntityType.Builder.of(Bunny::new, ...).build(key)}.
 *
 * <p>Every mod builds its entity types this way regardless of how it registers them, and the key
 * carries the type's name.
 */
public record EntityTypeBuild(Builder builder, ResourceKey key) {
}
