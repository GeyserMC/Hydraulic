package org.geysermc.hydraulic.util;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.mojang.logging.LogUtils;
import net.kyori.adventure.key.Key;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.pack.bedrock.resource.Manifest;
import org.geysermc.pack.bedrock.resource.manifest.Header;
import org.geysermc.pack.bedrock.resource.manifest.Modules;
import org.geysermc.pack.converter.converter.texture.TextureMappings;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Utility class for packs.
 */
public class PackUtil {
    protected static final Logger LOGGER = LogUtils.getLogger();
    private static final int FORMAT_VERSION = 2;

    /**
     * Initializes a resource pack for the specified mod.
     *
     * @param mod the mod to initialize the resource pack for
     * @return the initialized resource pack
     */
    public static BedrockResourcePack initializeForMod(@NotNull ModInfo mod, @NotNull Path packPack) {
        Manifest manifest = new Manifest();
        manifest.formatVersion(FORMAT_VERSION);

        Header header = new Header();
        header.description("Resource pack for mod " + mod.name());
        header.name(mod.name() + " Resource Pack");
        header.version(new float[] { 1, 0, 0 });
        header.minEngineVersion(new float[] { 1, 16, 0 });
        header.uuid(UUID.randomUUID().toString());

        manifest.header(header);

        Modules module = new Modules();
        module.description("Resource pack for mod " + mod.name());
        module.type("resources");
        module.uuid(UUID.randomUUID().toString());
        module.version(new float[] { 1, 0, 0 });

        manifest.modules(List.of(module));

        BedrockResourcePack pack = new BedrockResourcePack(packPack);
        pack.manifest(manifest);
        return pack;
    }

    public static String getTextureName(@NotNull String modelName) {
        if (modelName.startsWith(Key.MINECRAFT_NAMESPACE)) {
            String modelValue = modelName.split(":")[1];

            String type = modelValue.substring(0, modelValue.indexOf("/"));
            String value = modelValue.substring(modelValue.indexOf("/") + 1);

            // Need to use the Bedrock value for vanilla textures
            Map<String, String> textures = TextureMappings.textureMappings().textures(type);
            if (textures != null) {
                String textureName = textures.getOrDefault(value, "");
                if (textureName.isEmpty()) {
                    textureName = value;
                } else {
                    textureName = "hydraulic:" + textureName;
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
            stream.flatMap(IOUtil.uncheckFunction(Files::walk)).forEachOrdered(p -> {
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
