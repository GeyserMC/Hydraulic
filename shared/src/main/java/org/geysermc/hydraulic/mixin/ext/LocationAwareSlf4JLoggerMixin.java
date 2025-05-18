package org.geysermc.hydraulic.mixin.ext;

import org.slf4j.helpers.FormattingTuple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// This just fixes a netty bug since we are on 4.1.97
// https://github.com/netty/netty/commit/323f78ae7c6fcda0c5c62c20afa77a940fb2ee26
@Mixin(targets = "io.netty.util.internal.logging.LocationAwareSlf4JLogger", remap = false)
@Pseudo
public class LocationAwareSlf4JLoggerMixin {
    @Redirect(
        method = "log(ILorg/slf4j/helpers/FormattingTuple;)V",
        at = @At(
            value = "INVOKE",
            target = "Lorg/slf4j/helpers/FormattingTuple;getArgArray()[Ljava/lang/Object;"
        )
    )
    private Object[] getArgArray(FormattingTuple instance) {
        return null;
    }
}
