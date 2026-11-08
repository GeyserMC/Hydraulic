package org.geysermc.hydraulic.fabric.mixin.misc;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.geysermc.geyser.api.GeyserApi;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

/**
 * WorldWeaver (wover-block) runs a join check that asks the client to confirm its
 * block/item definitions match the server's. Bedrock players can't answer it and get
 * disconnected, so complete the task immediately for them.
 */
@Pseudo
@Mixin(targets = "de.ambertation.wover.block.impl.join.JoinCheckServer$Task")
public abstract class WorldWeaverJoinCheckTaskMixin {
    @Shadow(remap = false)
    @Final
    private ServerConfigurationPacketListenerImpl handler;

    @Inject(method = "start", at = @At("HEAD"), cancellable = true, require = 0)
    private void skipForBedrock(Consumer<Packet<?>> sender, CallbackInfo ci) {
        if (GeyserApi.api().isBedrockPlayer(this.handler.getOwner().id())) {
            this.handler.completeTask(((ConfigurationTask) (Object) this).type());
            ci.cancel();
        }
    }
}
