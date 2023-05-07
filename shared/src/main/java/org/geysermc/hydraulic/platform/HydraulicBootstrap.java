package org.geysermc.hydraulic.platform;

import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Represents the bootstrap of a platform.
 */
public interface HydraulicBootstrap {

    /**
     * Gets all the mods loaded on this platform.
     *
     * @return the mods loaded on this platform
     */
    @NotNull
    Set<ModInfo> mods();

    /**
     * Gets the mod with the specified name, or null if not found.
     *
     * @param modName the name of the mod to get
     * @return the mod with the specified name
     */
    @Nullable
    ModInfo mod(@NotNull String modName);
}
