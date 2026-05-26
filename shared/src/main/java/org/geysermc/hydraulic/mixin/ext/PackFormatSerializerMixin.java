package org.geysermc.hydraulic.mixin.ext;

import com.google.gson.JsonElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.unnamed.creative.metadata.pack.PackFormat;

@Mixin(targets = "team.unnamed.creative.serialize.minecraft.base.PackFormatSerializer", remap = false)
public class PackFormatSerializerMixin {
    @Inject(
        method = "deserialize(Lcom/google/gson/JsonElement;)Lteam/unnamed/creative/metadata/pack/PackFormat;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void deserialize(JsonElement el, CallbackInfoReturnable<PackFormat> cir) {
        if (el == null || el.isJsonNull()) {
            cir.setReturnValue(PackFormat.UNKNOWN);
        }
    }
}
