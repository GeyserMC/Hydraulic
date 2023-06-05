package org.geysermc.hydraulic.pack.context;

import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.pack.converter.PackConverter;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Represents the context of a pack after it has
 * been created.
 *
 * @param <T> the module type
 */
public class PackProcessContext<T extends PackModule<T>> extends PackContext<T> {
    private final PackConverter converter;
    private final BedrockResourcePack pack;
    private final Path path;

    public PackProcessContext(@NotNull HydraulicImpl hydraulic, @NotNull ModInfo mod, @NotNull T module, @NotNull PackConverter converter, @NotNull BedrockResourcePack pack, @NotNull Path path) {
        super(hydraulic, mod, module);

        this.converter = converter;
        this.pack = pack;
        this.path = path;
    }

    /**
     * Gets the pack converter.
     *
     * @return the pack converter
     */
    @NotNull
    public PackConverter converter() {
        return this.converter;
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
