package org.geysermc.hydraulic.entity.conduit;

import java.util.function.Supplier;

/**
 * A renderer registration seen while scanning a mod.
 *
 * @param type the entity type that the renderer is for
 * @param renderer the renderer supplier
 */
public record EntityRendererRegistration(EntityType type, Supplier<LivingEntityRenderer> renderer) {
}
