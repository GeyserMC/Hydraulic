package org.geysermc.hydraulic.mixin.ext;

import io.netty.buffer.ByteBuf;
import org.geysermc.hydraulic.sound.ModdedSounds;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundSoundEntityPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;
import java.util.function.IntFunction;

// TODO: Temporary class for custom sound passthrough until we have a proper fix in place.
@Mixin(value = ClientboundSoundEntityPacket.class, remap = false)
public class ClientboundSoundEntityPacketMixin {
    @Redirect(
        method = "<init>(Lio/netty/buffer/ByteBuf;)V",
        at = @At(
            value = "INVOKE",
            target = ModdedSounds.READ_BY_ID
        )
    )
    private Object hydraulic$readModdedSound(ByteBuf buf, IntFunction<Object> byId, Function<ByteBuf, Object> direct) {
        return ModdedSounds.read(buf, byId, direct);
    }
}
