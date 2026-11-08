package org.geysermc.hydraulic.platform;

import net.minecraft.server.MinecraftServer;
import org.geysermc.hydraulic.entity.EntityRendererRegistryInfo;
import org.geysermc.hydraulic.entity.ModelLayerRegistryInfo;
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

    /**
     * Registers a listener called at the end of every server tick.
     *
     * @param listenerAction the listener
     */
    void registerServerTick(Consumer<MinecraftServer> listenerAction);

    /**
     * Gets the {@link ModelLayerRegistryInfo} for capturing model layer registrations on
     * this platform.
     *
     * @return the model layer registry info for capturing model layer registrations
     */
    ModelLayerRegistryInfo modelLayerInfo();

    /**
     * Gets the jar every loaded mod was read from, including libraries left out of {@link #mods()},
     * so code calling into them can be followed. Minecraft itself is not included.
     *
     * @return the jars on disk of every loaded mod
     */
    @NotNull
    Collection<Path> modJars();

    /**
     * Gets the {@link EntityRendererRegistryInfo} for capturing renderer registrations on
     * this platform.
     *
     * @return the renderer registry info for capturing renderer registrations
     */
    EntityRendererRegistryInfo entityRendererInfo();
}
