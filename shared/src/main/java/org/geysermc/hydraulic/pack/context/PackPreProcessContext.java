package org.geysermc.hydraulic.pack.context;

import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.ResourcePack;

import java.nio.file.Path;

/**
 * Represents the context of a pack before it has
 * been created.
 *
 * @param <T> the module type
 */
public class PackPreProcessContext<T extends PackModule<T>> extends PackContext<T> {
    private final ResourcePack pack;

    public PackPreProcessContext(@NotNull HydraulicImpl hydraulic, @NotNull ModInfo mod, @NotNull T module, @NotNull ResourcePack pack) {
        super(hydraulic, mod, module);

        this.pack = pack;
    }

    /**
     * Gets the pack.
     *
     * @return the pack
     */
    @NotNull
    public ResourcePack pack() {
        return this.pack;
    }
}
