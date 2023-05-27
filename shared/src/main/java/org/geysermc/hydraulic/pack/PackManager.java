package org.geysermc.hydraulic.pack;

import com.mojang.logging.LogUtils;
import org.geysermc.event.Event;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.hydraulic.pack.context.PackCreateContext;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.hydraulic.util.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Manages packs within Hydraulic. Most of the pack conversion
 * management is done within this class, and it is also responsible
 * for loading the packs onto the server.
 */
public class PackManager {
    private static final Logger LOGGER = LogUtils.getLogger();

    static final Set<String> IGNORED_MODS = Set.of(
            "minecraft",
            "java",
            "hydraulic",
            "geyser-fabric",
            "geyser-forge",
            "floodgate",
            "vanilla"
    );

    private final HydraulicImpl hydraulic;
    private final List<PackModule<?>> modules = new ArrayList<>();

    private final Map<String, BedrockResourcePack> packs = new HashMap<>();

    public PackManager(HydraulicImpl hydraulic) {
        this.hydraulic = hydraulic;
    }

    /**
     * Initializes the pack manager.
     */
    public void initialize() {
        for (PackModule<?> module : ServiceLoader.load(PackModule.class)) {
            this.modules.add(module);

            GeyserApi.api().eventBus().register(this.hydraulic, module);
        }

        GeyserApi.api().eventBus().register(this.hydraulic, new PackListener(hydraulic, this));
    }

    /**
     * Creates the pack for the given mod.
     *
     * @param mod the mod to create the pack for
     * @param packPath the path to the pack
     * @return {@code true} if the pack was created, {@code false} otherwise
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    boolean createPack(@NotNull ModInfo mod, @NotNull Path packPath) {
        // Initialize our resource pack for our mod
        BedrockResourcePack pack = BedrockResourcePack.initializeForMod(mod);

        boolean shouldExportPack = false;
        for (PackModule<?> module : this.modules) {
            PackCreateContext context = new PackCreateContext(this.hydraulic, mod, module, pack, packPath);
            if (!module.test(context)) {
                continue;
            }

            module.create(context);
            shouldExportPack = true;
        }

        if (!shouldExportPack) {
            return false;
        }

        // Copy the icon if it exists
        // TODO Add a default icon?
        if (!mod.iconPath().isEmpty()) {
            FileUtil.copyFileFromMod(mod, mod.iconPath(), packPath.resolve("pack_icon.png"));
        }

        this.packs.put(mod.id(), pack);

        // Now export the pack
        try {
            pack.export(packPath);
        } catch (IOException ex) {
            LOGGER.error("Failed to export pack for mod {}", mod.id(), ex);
        }

        // TODO: NIO
        // Zip up the pack
        Path packsPath = GeyserImpl.getInstance().getBootstrap().getConfigFolder().resolve("packs");
        try {
            FileUtil.zipDirectory(packPath.toFile(), packsPath.resolve(mod.id() + ".zip").toFile());
        } catch (Exception ex) {
            LOGGER.error("Failed to zip pack for mod {}", mod.id(), ex);
        }

        return true;
    }

    void callEvents(@NotNull Event event) {
        for (ModInfo mod : this.hydraulic.mods()) {
            if (IGNORED_MODS.contains(mod.id())) {
                continue;
            }

            this.callEvent(mod, event);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void callEvent(@NotNull ModInfo mod, @NotNull Event event) {
        for (PackModule<?> module : this.modules) {
            module.call(event.getClass(), new PackEventContext(this.hydraulic, mod, module, event));
        }
    }
}
