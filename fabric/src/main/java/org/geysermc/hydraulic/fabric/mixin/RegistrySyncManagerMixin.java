package org.geysermc.hydraulic.fabric.mixin;

import net.fabricmc.fabric.impl.registry.sync.RegistrySyncManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.geysermc.geyser.api.GeyserApi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RegistrySyncManager.class)
public class RegistrySyncManagerMixin {
    @Inject(
            method = "configureClient(Lnet/minecraft/server/network/ServerConfigurationPacketListenerImpl;Lnet/minecraft/server/MinecraftServer;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void doNotConfigureBedrock(ServerConfigurationPacketListenerImpl handler, MinecraftServer server, CallbackInfo ci) {
        if (GeyserApi.api().isBedrockPlayer(handler.getOwner().getId())) ci.cancel();
    }
}
