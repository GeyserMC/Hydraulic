package org.geysermc.hydraulic.mixin.converter;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.geysermc.pack.converter.util.VanillaPackProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(value = VanillaPackProvider.class, remap = false)
public class VanillaPackProviderMixin {
    /**
     * Extra paths to keep when stripping the vanilla jar.
     */
    @Unique
    private static final List<String> KEPT_PATHS = List.of(
            "/assets/minecraft/textures/colormap/" // Used to resolve item tints
    );

    @WrapOperation(
            method = "*",
            at = @At(value = "INVOKE", target = "Ljava/util/List;contains(Ljava/lang/Object;)Z")
    )
    private static boolean keepExtraPaths(List<?> instance, Object value, Operation<Boolean> original) {
        return original.call(instance, value) || (value instanceof String path && KEPT_PATHS.stream().anyMatch(path::startsWith));
    }
}
