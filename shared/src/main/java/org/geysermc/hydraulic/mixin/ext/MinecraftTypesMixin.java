package org.geysermc.hydraulic.mixin.ext;

import io.netty.buffer.ByteBuf;
import org.geysermc.hydraulic.sound.ModdedSounds;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;
import java.util.function.IntFunction;

// TODO: Temporary class for custom sound passthrough until we have a proper fix in place.
@Mixin(value = MinecraftTypes.class, remap = false)
public class MinecraftTypesMixin {
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
}
