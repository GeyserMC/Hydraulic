package org.geysermc.hydraulic;

import net.minecraft.server.MinecraftServer;
import org.geysermc.geyser.api.event.EventRegistrar;
import org.geysermc.hydraulic.pack.PackManager;
import org.geysermc.hydraulic.platform.HydraulicBootstrap;
import org.geysermc.hydraulic.platform.HydraulicPlatform;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.hydraulic.storage.ModStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Main class of the Hydraulic mod.
 */
public class HydraulicImpl implements EventRegistrar {
    private static final Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_NAME);

    private static HydraulicImpl instance;

    private final HydraulicPlatform platform;
    private final HydraulicBootstrap bootstrap;
    private final PackManager packManager;

    private final Map<String, ModStorage> modStorage = new HashMap<>();

    private MinecraftServer server;

    private HydraulicImpl(HydraulicPlatform platform, HydraulicBootstrap bootstrap) {
        instance = this;

        this.platform = platform;
        this.bootstrap = bootstrap;
        this.packManager = new PackManager(this);
    }

    /**
     * Called when the server is starting.
     *
     * @param server the Minecraft server instance
     */
    public void onServerStarting(@NotNull MinecraftServer server) {
        this.server = server;

        this.packManager.initialize();
    }

    /**
     * Register a listener for the stop server event
     */
    public void registerServerStop(Consumer<MinecraftServer> listenerAction) {
        bootstrap.registerServerStop(listenerAction);
    }

    /**
     * Gets all the mods loaded on this platform.
     *
     * @return the mods loaded on this platform
     */
    @NotNull
    public Collection<ModInfo> mods() {
        return this.bootstrap.mods();
    }

    /**
     * Gets the mod with the specified name, or null if not found.
     *
     * @param modName the name of the mod to get
     * @return the mod with the specified name
     */
    @Nullable
    public ModInfo mod(@NotNull String modName) {
        return this.bootstrap.mod(modName);
    }

    /**
     * Gets the Minecraft server instance.
     *
     * @return the Minecraft server instance
     */
    @NotNull
    public MinecraftServer server() {
        return this.server;
    }

    /**
     * Gets the data folder directory of this platform.
     *
     * @return the data folder directory
     */
    @NotNull
    public Path dataFolder(@NotNull String modId) {
        return this.bootstrap.dataFolder(modId);
    }

    /**
     * Gets the mod storage for the specified mod.
     *
     * @param mod the mod
     * @return the mod storage
     */
    @NotNull
    public ModStorage modStorage(@NotNull ModInfo mod) {
        return this.modStorage.computeIfAbsent(mod.id(), e -> ModStorage.load(mod));
    }

    /**
     * Loads Hydraulic.
     *
     * @param platform the platform Hydraulic is running on
     * @param bootstrap the Hydraulic platform bootstrap
     * @return the loaded Hydraulic instance
     */
    @NotNull
    public static HydraulicImpl load(@NotNull HydraulicPlatform platform, @NotNull HydraulicBootstrap bootstrap) {
        if (instance != null) {
            throw new IllegalStateException("Singleton HydraulicImpl has already been loaded!");
        }

        return new HydraulicImpl(platform, bootstrap);
    }

    /**
     * Gets the Hydraulic instance.
     *
     * @return the Hydraulic instance
     */
    @NotNull
    public static HydraulicImpl instance() {
        if (instance == null) {
            throw new IllegalStateException(Constants.MOD_NAME + " has not been loaded!");
        }

        return instance;
    }

    public PackManager getPackManager() {
        return packManager;
    }

    public boolean isDev() {
        return this.bootstrap.isDev();
    }
}
