package org.geysermc.hydraulic.neoforge.mixin;

import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.neoforged.neoforge.network.connection.ConnectionType;
import net.neoforged.neoforge.network.filters.NetworkFilters;
import net.neoforged.neoforge.network.payload.ModdedNetworkQueryComponent;
import net.neoforged.neoforge.network.registration.ChannelAttributes;
import net.neoforged.neoforge.network.registration.NetworkPayloadSetup;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Set;

/**
 * NeoForge rejects non-modded clients on servers where any registered network channel is
 * non-optional, in two places:
 * <ol>
 *     <li>{@link NetworkRegistry#initializeOtherConnection} during the configuration handshake
 *     ("You are trying to connect to a server that is running NeoForge, but you are not.")</li>
 *     <li>{@link NetworkRegistry#initializeNeoForgeConnection} when transitioning to play for
 *     {@code ConnectionType.OTHER} connections ("Incompatible client! Please use NeoForge ...")</li>
 * </ol>
 * <p>
 * Servers running Hydraulic are explicitly set up to allow Bedrock players through Geyser, whose
 * connection appears as a vanilla client. These mixins accept the connection instead of kicking it.
 * The connection type must still be set (the original method does this before negotiating), or
 * {@link net.neoforged.neoforge.network.filters.NetworkFilters} throws a NullPointerException when
 * the connection transitions to play.
 */
@Mixin(value = NetworkRegistry.class, remap = false)
public class NetworkRegistryMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("hydraulic/neoforge");

    @Inject(method = "initializeOtherConnection", at = @At("HEAD"), cancellable = true, remap = false)
    private static void hydraulicAllowVanillaConnection(ServerConfigurationPacketListener listener, CallbackInfoReturnable<Boolean> cir) {
        LOGGER.info("Hydraulic: allowing vanilla client connection (bypassing NeoForge modded client check)");
        ChannelAttributes.setConnectionType(listener.getConnection(), ConnectionType.OTHER);
        cir.setReturnValue(true);
    }

    @Inject(method = "initializeNeoForgeConnection", at = @At("HEAD"), cancellable = true, remap = false)
    private static void hydraulicSkipEmptyNegotiation(ServerConfigurationPacketListener listener,
                                                      Map<ConnectionProtocol, Set<ModdedNetworkQueryComponent>> clientChannels,
                                                      CallbackInfo ci) {
        // NeoForge calls this with an empty channel map for OTHER (vanilla) connections when
        // transitioning to play. Any non-optional mod channel then fails the negotiation and the
        // client is kicked with "Incompatible client! Please use NeoForge". Vanilla clients don't
        // use mod channels, so skip the negotiation entirely.
        if (clientChannels == null || clientChannels.isEmpty()) {
            LOGGER.info("Hydraulic: skipping NeoForge channel negotiation for vanilla connection");
            // Mirror what the original method does on a successful negotiation so the
            // "channel negotiation not performed" check in handleConfigurationFinished passes.
            ChannelAttributes.setPayloadSetup(listener.getConnection(), NetworkPayloadSetup.empty());
            NetworkFilters.injectIfNecessary(listener.getConnection());
            ci.cancel();
        }
    }
}
