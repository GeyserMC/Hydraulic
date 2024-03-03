package org.geysermc.hydraulic.pack.context;

import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.pack.converter.PackConverter;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.ResourcePack;

import java.nio.file.Path;

/**
 * Represents the context of a pack after it has
 * been created.
 *
 * @param <T> the module type
 */
public class PackPostProcessContext<T extends PackModule<T>> extends PackContext<T> {
    private final PackConverter converter;
    private final ResourcePack javaPack;
    private final BedrockResourcePack bedrockPack;
    private final Path path;

    public PackPostProcessContext(@NotNull HydraulicImpl hydraulic, @NotNull ModInfo mod, @NotNull T module,
                                  @NotNull PackConverter converter, @NotNull ResourcePack javaPack,
                                  @NotNull BedrockResourcePack bedrockPack, @NotNull Path path) {
        super(hydraulic, mod, module);

        this.converter = converter;
        this.javaPack = javaPack;
        this.bedrockPack = bedrockPack;
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
     * Gets the Java pack.
     *
     * @return the Java pack
     */
    @NotNull
    public ResourcePack javaResourcePack() {
        return this.javaPack;
    }

    /**
     * Gets the Bedrock pack.
     *
     * @return the Bedrock pack
     */
    @NotNull
    public BedrockResourcePack bedrockResourcePack() {
        return this.bedrockPack;
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
