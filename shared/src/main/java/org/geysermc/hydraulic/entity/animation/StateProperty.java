package org.geysermc.hydraulic.entity.animation;

import org.geysermc.conduit.Value;
import org.jetbrains.annotations.NotNull;

/**
 * A value an animation reads that the client can't compute itself, sent to it as an entity property.
 *
 * @param id the property's identifier, such as {@code mocreatures:bear_state}
 * @param value how to compute it from the entity, which is {@link Value.Parameter} zero
 */
public record StateProperty(@NotNull String id, @NotNull Value value) {
}
