package org.geysermc.hydraulic.neoforge.mixin;

import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * NeoForge disconnects vanilla clients (including Geyser's connections) from modded servers when any
 * registered network channel is non-optional, with:
 * "You are trying to connect to a server that is running NeoForge, but you are not."
 * <p>
 * Servers running Hydraulic are explicitly set up to allow Bedrock players through Geyser, whose
 * connection appears as a vanilla client during the configuration handshake. This mixin makes
 * {@link NetworkRegistry#initializeOtherConnection} accept the connection instead of kicking it.
 */
@Mixin(value = NetworkRegistry.class, remap = false)
public class NetworkRegistryMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("hydraulic/neoforge");

    @Inject(method = "initializeOtherConnection", at = @At("HEAD"), cancellable = true, remap = false)
    private static void hydraulicAllowVanillaConnection(ServerConfigurationPacketListener listener, CallbackInfoReturnable<Boolean> cir) {
        LOGGER.info("Hydraulic: allowing vanilla client connection (bypassing NeoForge modded client check)");
        cir.setReturnValue(true);
    }
}
