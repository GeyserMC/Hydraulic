package org.geysermc.hydraulic.util;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.mojang.logging.LogUtils;
import net.kyori.adventure.key.Key;
import org.geysermc.hydraulic.Constants;
import org.geysermc.pack.converter.converter.texture.TextureMappings;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Utility class for packs.
 */
public class PackUtil {
    protected static final Logger LOGGER = LogUtils.getLogger();

    public static String getTextureName(@NotNull String modelName) {
        // TODO Sometimes things end up in the minecraft namespace when they shouldn't.
        //      We should look at the current mods resources to see if we find a match there first
        //      EG: betternether:wall_mushroom_red refrencing both mushroom_red_new (its own) and mushroom_block_inside (mc)
        if (modelName.startsWith(Key.MINECRAFT_NAMESPACE)) {
            String modelValue = modelName.split(":")[1];

            String type = modelValue.substring(0, modelValue.indexOf("/"));
            String value = modelValue.substring(modelValue.indexOf("/") + 1);

            // Need to use the Bedrock value for vanilla textures
            Map<String, String> textures = (Map<String, String>) TextureMappings.textureMappings().textures(type);
            if (textures != null) {
                String textureName = textures.getOrDefault(value, "");
                if (textureName.isEmpty()) {
                    textureName = value;
                } else {
                    textureName = Constants.MOD_ID + ":" + textureName;
                }
                return textureName;
            }

            return value;
        }

        return modelName.replace("block/", "").replace("item/", "");
    }

    public static UUID getModUUID(Collection<Path> modRoots) {
        final HashingOutputStream hos = new HashingOutputStream(Hashing.murmur3_128(), OutputStream.nullOutputStream());
        try (Stream<Path> stream = modRoots.parallelStream()) {
            stream.flatMap(IOUtil.uncheckFunction(Files::walk)).sorted().forEachOrdered(p -> {
                try {
                    hos.write(p.toString().getBytes(StandardCharsets.UTF_8));
                    if (Files.isRegularFile(p)) {
                        Files.copy(p, hos);
                    }
                } catch (IOException e) {
                    LOGGER.warn("Failed to hash {}", p, e);
                }
            });
        }
        return UUID.nameUUIDFromBytes(hos.hash().asBytes());
    }
}
