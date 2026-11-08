package org.geysermc.hydraulic.entity.geckolib.mixin;

import com.geckolib.renderer.base.GeoRenderState;
import org.geysermc.hydraulic.entity.geckolib.GeckoLibAnimator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.geckolib.animation.state.AnimationTest", remap = false)
public abstract class AnimationTestMixin {
    @Shadow
    public abstract GeoRenderState renderState();

    @Inject(method = "isMoving", at = @At("HEAD"), cancellable = true)
    private void hydraulic$isMoving(CallbackInfoReturnable<Boolean> callback) {
        // Ensure's GeckoLib's data tickets don't refer to client classes
        // as they can't be read on a dedicated server
        if (this.renderState() instanceof GeckoLibAnimator.ServerRenderState state) {
            callback.setReturnValue(state.moving());
        }
    }
}
