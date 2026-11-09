package org.geysermc.hydraulic.platform;

import net.minecraft.server.MinecraftServer;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Consumer;

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
    Collection<ModInfo> mods();

    /**
     * Gets all the mods loaded on this platform, including library mods
     * that are excluded from {@link #mods()}. These are used as a source
     * of assets (e.g. parent models) during conversion.
     *
     * @return all mods loaded on this platform, including libraries
     */
    @NotNull
    default Collection<ModInfo> allMods() {
        return this.mods();
    }

    /**
     * Gets the mod with the specified name, or null if not found.
     *
     * @param modName the name of the mod to get
     * @return the mod with the specified name
     */
    @Nullable
    ModInfo mod(@NotNull String modName);

    /**
     * Gets the data folder directory of this platform.
     *
     * @return the data folder directory
     */
    @NotNull
    Path dataFolder(@NotNull String modId);

    /**
     * Gets if the current environment is a development environment.
     *
     * @return if the current environment is a development environment
     */
    boolean isDev();

    /**
     * Register a listener for the stop server event
     */
    void registerServerStop(Consumer<MinecraftServer> listenerAction);
}
