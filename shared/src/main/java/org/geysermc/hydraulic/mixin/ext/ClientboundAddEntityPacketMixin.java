package org.geysermc.hydraulic.mixin.ext;

import org.geysermc.hydraulic.entity.CustomEntityTypes;
import org.geysermc.hydraulic.entity.JavaEntityTypeHolder;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundAddEntityPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// TODO: Temporary class for custom entity types until we have a proper registry system in place. This is a stopgap
//       solution to allow for custom entities to be registered and used in the game.
@Mixin(value = ClientboundAddEntityPacket.class, remap = false)
public class ClientboundAddEntityPacketMixin implements JavaEntityTypeHolder {

    @Unique
    private int hydraulic$javaTypeId = -1;

    @Redirect(
        method = "<init>(Lio/netty/buffer/ByteBuf;)V",
        at = @At(
            value = "INVOKE",
            target = "Lorg/geysermc/mcprotocollib/protocol/data/game/entity/type/EntityType;from(I)Lorg/geysermc/mcprotocollib/protocol/data/game/entity/type/EntityType;"
        )
    )
    private EntityType hydraulic$rememberType(int javaTypeId) {
        this.hydraulic$javaTypeId = javaTypeId;

        if (javaTypeId < 0 || CustomEntityTypes.isCustom(javaTypeId)) {
            return CustomEntityTypes.PLACEHOLDER;
        }

        return EntityType.from(javaTypeId);
    }

    @Override
    public int hydraulic$javaTypeId() {
        return this.hydraulic$javaTypeId;
    }
}
