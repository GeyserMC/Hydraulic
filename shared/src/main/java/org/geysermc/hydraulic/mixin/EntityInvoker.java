package org.geysermc.hydraulic.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Exposes whether an entity flies in every direction, which decides how its walk animation is worked out.
 */
@Mixin(Entity.class)
public interface EntityInvoker {

    /**
     * Gets whether the entity moves through the air in every direction, as flying mobs do.
     *
     * @return whether it does
     */
    @Invoker("omnidirectionalAirMover")
    boolean hydraulic$omnidirectionalAirMover();
}
