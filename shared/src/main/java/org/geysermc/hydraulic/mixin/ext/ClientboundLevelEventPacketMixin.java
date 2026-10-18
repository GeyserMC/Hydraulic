package org.geysermc.hydraulic.mixin.ext;

import org.geysermc.hydraulic.block.BlockStateRemapper;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundLevelEventPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = ClientboundLevelEventPacket.class, remap = false)
public class ClientboundLevelEventPacketMixin {

    @ModifyArg(
        method = "<init>(Lio/netty/buffer/ByteBuf;)V",
        at = @At(
            value = "INVOKE",
            target = "Lorg/geysermc/mcprotocollib/protocol/data/game/level/event/BreakBlockEventData;<init>(I)V"
        )
    )
    private int hydraulic$remapBlockState(int state) {
        return BlockStateRemapper.remapState(state);
    }
}
