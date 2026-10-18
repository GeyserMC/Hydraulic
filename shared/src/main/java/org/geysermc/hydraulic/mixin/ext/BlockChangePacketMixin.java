package org.geysermc.hydraulic.mixin.ext;

import org.geysermc.hydraulic.block.BlockStateRemapper;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundBlockUpdatePacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundSectionBlocksUpdatePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = {
        ClientboundBlockUpdatePacket.class,
        ClientboundSectionBlocksUpdatePacket.class
}, remap = false)
public class BlockChangePacketMixin {

    @ModifyArg(
        method = "<init>(Lio/netty/buffer/ByteBuf;)V",
        at = @At(
            value = "INVOKE",
            target = "Lorg/geysermc/mcprotocollib/protocol/data/game/level/block/BlockChangeEntry;<init>(Lorg/cloudburstmc/math/vector/Vector3i;I)V"
        ),
        index = 1
    )
    private int hydraulic$remapBlockState(int state) {
        return BlockStateRemapper.remapState(state);
    }
}
