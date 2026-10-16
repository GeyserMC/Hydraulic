package org.geysermc.hydraulic.neoforge.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerCommonPacketListener;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import org.geysermc.geyser.api.GeyserApi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO: Revisit once Geyser's networking API is merged which should handle NeoForge's channel stuff for Bedrock
@Mixin(NetworkRegistry.class)
public class NetworkRegistryMixin {
    @ModifyExpressionValue(
            method = {"initializeNeoForgeConnection", "initializeOtherConnection"},
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/network/negotiation/NegotiationResult;success()Z")
    )
    private static boolean allowBedrock(boolean success, @Local(argsOnly = true) ServerConfigurationPacketListener listener) {
        return success || isBedrockPlayer(listener);
    }

    @Inject(
            method = "checkPacket(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/protocol/common/ServerCommonPacketListener;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void sendToBedrock(Packet<?> packet, ServerCommonPacketListener listener, CallbackInfo ci) {
        if (isBedrockPlayer(listener)) {
            ci.cancel();
        }
    }

    @Unique
    private static boolean isBedrockPlayer(ServerCommonPacketListener listener) {
        return listener instanceof ServerCommonPacketListenerImpl impl && GeyserApi.api().isBedrockPlayer(impl.getOwner().id());
    }
}
