package org.geysermc.hydraulic.pack.context;

import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Represents the context of a pack during creation.
 *
 * @param <T> the module type
 */
public class PackCreateContext<T extends PackModule<T>> extends PackContext<T> {
    private final BedrockResourcePack pack;
    private final Path path;

    public PackCreateContext(@NotNull HydraulicImpl hydraulic, @NotNull ModInfo mod, @NotNull T module, @NotNull BedrockResourcePack pack, @NotNull Path path) {
        super(hydraulic, mod, module);
        this.pack = pack;
        this.path = path;
    }

    /**
     * Gets the pack.
     *
     * @return the pack
     */
    @NotNull
    public BedrockResourcePack pack() {
        return this.pack;
    }

    /**
     * Gets the path to the pack.
     *
     * @return the path to the pack
     */
    @NotNull
    public Path path() {
        return this.path;
    }
}
