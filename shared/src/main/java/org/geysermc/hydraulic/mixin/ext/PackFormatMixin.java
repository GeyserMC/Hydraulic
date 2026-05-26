package org.geysermc.hydraulic.mixin.ext;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.unnamed.creative.metadata.pack.FormatVersion;
import team.unnamed.creative.metadata.pack.PackFormat;

@Mixin(targets = "team.unnamed.creative.metadata.pack.PackFormat", remap = false)
public interface PackFormatMixin {
    @Inject(
        method = "format(Lteam/unnamed/creative/metadata/pack/FormatVersion;Lteam/unnamed/creative/metadata/pack/FormatVersion;)Lteam/unnamed/creative/metadata/pack/PackFormat;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void format(FormatVersion min, FormatVersion max, CallbackInfoReturnable<PackFormat> cir) {
        if (min != null && max != null && min.compareTo(max) > 0) {
            cir.setReturnValue(PackFormat.format(max, max));
        }
    }
}
