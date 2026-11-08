package org.geysermc.hydraulic.neoforge.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityTypes;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.hydraulic.entity.CustomEntityTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// TODO: Temporary class for custom entity types until we have a proper registry system in place. MCProtocolLib is
//       outside the game layer on NeoForge, so Geyser can't be told the type through the packet it decodes.
@Mixin(ServerEntity.class)
public class ServerEntityMixin {

    @ModifyExpressionValue(
            method = "sendPairingData",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getAddEntityPacket(Lnet/minecraft/server/level/ServerEntity;)Lnet/minecraft/network/protocol/Packet;")
    )
    private Packet<?> sendPlaceholderToBedrock(Packet<?> packet, @Local(argsOnly = true) ServerPlayer player) {
        if (!(packet instanceof ClientboundAddEntityPacket add)) {
            return packet;
        }

        int javaTypeId = BuiltInRegistries.ENTITY_TYPE.getId(add.getType());
        if (!CustomEntityTypes.isCustom(javaTypeId) || !GeyserApi.api().isBedrockPlayer(player.getUUID())) {
            return packet;
        }

        CustomEntityTypes.spawned(player.getUUID(), add.getId(), javaTypeId);
        return new ClientboundAddEntityPacket(
                add.getId(), add.getUUID(), add.getX(), add.getY(), add.getZ(), add.getXRot(), add.getYRot(),
                EntityTypes.ALLAY, add.getData(), add.getMovement(), add.getYHeadRot()
        );
    }
}
