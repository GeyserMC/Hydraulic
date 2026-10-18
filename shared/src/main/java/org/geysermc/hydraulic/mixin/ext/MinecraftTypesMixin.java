package org.geysermc.hydraulic.mixin.ext;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.netty.buffer.ByteBuf;
import org.geysermc.hydraulic.block.BlockStateRemapper;
import org.geysermc.hydraulic.sound.ModdedSounds;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.DataPalette;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.PaletteType;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.EntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.MetadataTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.IntEntityMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;
import java.util.function.IntFunction;

@Mixin(value = MinecraftTypes.class, remap = false)
public class MinecraftTypesMixin {

    // TODO: Temporary redirect for custom sound passthrough until we have a proper fix in place
    @Redirect(
        method = "readSound(Lio/netty/buffer/ByteBuf;)Lorg/geysermc/mcprotocollib/protocol/data/game/level/sound/Sound;",
        at = @At(
            value = "INVOKE",
            target = ModdedSounds.READ_BY_ID
        )
    )
    private static Object hydraulic$readModdedSound(ByteBuf buf, IntFunction<Object> byId, Function<ByteBuf, Object> direct) {
        return ModdedSounds.read(buf, byId, direct);
    }

    @ModifyReturnValue(
        method = "readDataPalette(Lio/netty/buffer/ByteBuf;Lorg/geysermc/mcprotocollib/protocol/data/game/chunk/palette/PaletteType;I)Lorg/geysermc/mcprotocollib/protocol/data/game/chunk/DataPalette;",
        at = @At("RETURN")
    )
    private static DataPalette hydraulic$remapPalette(DataPalette data, ByteBuf buf, PaletteType type, int registrySize) {
        return BlockStateRemapper.remapPalette(data, type, registrySize);
    }

    @ModifyArg(
        method = "readParticleData(Lio/netty/buffer/ByteBuf;Lorg/geysermc/mcprotocollib/protocol/data/game/level/particle/ParticleType;)Lorg/geysermc/mcprotocollib/protocol/data/game/level/particle/ParticleData;",
        at = @At(
            value = "INVOKE",
            target = "Lorg/geysermc/mcprotocollib/protocol/data/game/level/particle/BlockParticleData;<init>(I)V"
        )
    )
    private static int hydraulic$remapParticle(int state) {
        return BlockStateRemapper.remapState(state);
    }

    @ModifyReturnValue(
        method = "readMetadata(Lio/netty/buffer/ByteBuf;I)Lorg/geysermc/mcprotocollib/protocol/data/game/entity/metadata/EntityMetadata;",
        at = @At("RETURN")
    )
    private static EntityMetadata<?, ?> hydraulic$remapMetadata(EntityMetadata<?, ?> metadata, ByteBuf buf, int id) {
        if (metadata instanceof IntEntityMetadata state && (state.getType() == MetadataTypes.BLOCK_STATE || state.getType() == MetadataTypes.OPTIONAL_BLOCK_STATE)) {
            return new IntEntityMetadata(id, state.getType(), BlockStateRemapper.remapState(state.getPrimitiveValue()));
        }

        return metadata;
    }

    // Geyser skips block entities it doesn't know, rather than dropping the chunk holding a modded one
    @Redirect(
        method = "readBlockEntityType(Lio/netty/buffer/ByteBuf;)Lorg/geysermc/mcprotocollib/protocol/data/game/level/block/BlockEntityType;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Objects;requireNonNull(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;"
        )
    )
    private static Object hydraulic$allowModdedBlockEntityType(Object type, String message) {
        return type;
    }
}
