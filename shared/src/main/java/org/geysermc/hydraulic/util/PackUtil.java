package org.geysermc.hydraulic.util;

import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.pack.bedrock.resource.Manifest;
import org.geysermc.pack.bedrock.resource.manifest.Header;
import org.geysermc.pack.bedrock.resource.manifest.Modules;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

/**
 * Utility class for packs.
 */
public class PackUtil {
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
}
