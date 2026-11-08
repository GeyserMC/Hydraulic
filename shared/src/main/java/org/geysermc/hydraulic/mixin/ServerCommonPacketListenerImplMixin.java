package org.geysermc.hydraulic.mixin;

import net.minecraft.core.Holder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.MetadataTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * Sanitizes packets with modded info so Bedrock players see what mods add in a form Geyser can read.
 */
@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class ServerCommonPacketListenerImplMixin {

    @ModifyVariable(
            method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private Packet<?> hydraulic$forBedrock(Packet<?> packet) {
        if (!(packet instanceof ClientboundSoundPacket || packet instanceof ClientboundSoundEntityPacket
                || packet instanceof ClientboundSetEntityDataPacket || packet instanceof ClientboundBundlePacket) || !this.hydraulic$isBedrock()) {
            return packet;
        }

        return this.hydraulic$translate(packet);
    }

    @Unique
    @SuppressWarnings("unchecked")
    private Packet<?> hydraulic$translate(Packet<?> packet) {
        if (packet instanceof ClientboundBundlePacket bundle) {
            List<Packet<? super ClientGamePacketListener>> packets = new ArrayList<>();
            for (Packet<? super ClientGamePacketListener> inner : bundle.subPackets()) {
                packets.add((Packet<? super ClientGamePacketListener>) this.hydraulic$translate(inner));
            }

            return new ClientboundBundlePacket(packets);
        }

        if (packet instanceof ClientboundSetEntityDataPacket data) {
            List<SynchedEntityData.DataValue<?>> readable = data.packedItems().stream()
                    .filter(value -> EntityDataSerializers.getSerializedId(value.serializer()) < MetadataTypes.size())
                    .toList();

            return readable.size() == data.packedItems().size() ? packet : new ClientboundSetEntityDataPacket(data.id(), readable);
        }

        if (packet instanceof ClientboundSoundPacket sound && hydraulic$isModded(sound.getSound())) {
            return new ClientboundSoundPacket(Holder.direct(sound.getSound().value()), sound.getSource(),
                    sound.getX(), sound.getY(), sound.getZ(), sound.getVolume(), sound.getPitch(), sound.getSeed());
        }

        if (packet instanceof ClientboundSoundEntityPacket sound && hydraulic$isModded(sound.getSound()) && (Object) this instanceof ServerGamePacketListenerImpl game) {
            Entity entity = game.player.level().getEntity(sound.getId());
            if (entity != null) {
                return new ClientboundSoundEntityPacket(Holder.direct(sound.getSound().value()), sound.getSource(),
                        entity, sound.getVolume(), sound.getPitch(), sound.getSeed());
            }
        }

        return packet;
    }

    @Unique
    private boolean hydraulic$isBedrock() {
        return GeyserApi.api().isBedrockPlayer(((ServerCommonPacketListenerImpl) (Object) this).getOwner().id());
    }

    @Unique
    private static boolean hydraulic$isModded(Holder<SoundEvent> sound) {
        return sound.unwrapKey().map(key -> !key.identifier().getNamespace().equals("minecraft")).orElse(false);
    }
}
