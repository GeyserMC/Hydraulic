package org.geysermc.hydraulic.mixin.geyser;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Return null for modded block entity types instead of throwing
 */
@Mixin(value = MinecraftTypes.class, remap = false)
public abstract class MinecraftTypesMixin {
    @Redirect(
        method = "readBlockEntityType",
        at = @At(value = "INVOKE", target = "Ljava/util/Objects;requireNonNull(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;"),
        remap = false
    )
    private static Object allowUnknownBlockEntityType(Object type, String message) {
        return type;
    }
}
