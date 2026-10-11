package org.geysermc.hydraulic.sound;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.BuiltinSound;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.CustomSound;

import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * Reads sounds sent by registry id, including the ids of sound events mods register, which come
 * after vanilla's and so aren't among the sounds the protocol library knows.
 */
public final class ModdedSounds {
    public static final String READ_BY_ID = "Lorg/geysermc/mcprotocollib/protocol/codec/MinecraftTypes;readById(Lio/netty/buffer/ByteBuf;Ljava/util/function/IntFunction;Ljava/util/function/Function;)Ljava/lang/Object;";

    private static final int BUILTIN = BuiltinSound.values().length;
    private static final float DEFAULT_RANGE = 16f;

    private ModdedSounds() {
    }

    /**
     * Reads a sound the way {@code MinecraftTypes.readById} does, resolving a modded id against the
     * server's own registry.
     *
     * @param buf the buffer
     * @param byId reads a vanilla sound by id
     * @param direct reads a sound sent inline
     * @param <T> the sound type
     * @return the sound
     */
    @SuppressWarnings("unchecked")
    public static <T> T read(ByteBuf buf, IntFunction<T> byId, Function<ByteBuf, T> direct) {
        int id = MinecraftTypes.readVarInt(buf);
        if (id == 0) {
            return direct.apply(buf);
        }

        int registryId = id - 1;
        SoundEvent event = registryId >= BUILTIN ? BuiltInRegistries.SOUND_EVENT.byId(registryId) : null;
        if (event == null) {
            return byId.apply(registryId);
        }

        return (T) new CustomSound(event.location().toString(), event.fixedRange().isPresent(), event.fixedRange().orElse(DEFAULT_RANGE));
    }
}
