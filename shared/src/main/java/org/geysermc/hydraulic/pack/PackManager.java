package org.geysermc.hydraulic.pack;

import com.mojang.logging.LogUtils;
import org.geysermc.event.Event;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.pack.context.PackCreateContext;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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
            "vanilla"
    );

    private final HydraulicImpl hydraulic;
    private final List<PackModule<?>> modules = new ArrayList<>();

    public PackManager(HydraulicImpl hydraulic) {
        this.hydraulic = hydraulic;
        for (PackModule<?> module : ServiceLoader.load(PackModule.class)) {
            this.modules.add(module);

            GeyserApi.api().eventBus().register(hydraulic, module);
        }

        GeyserApi.api().eventBus().register(hydraulic, new PackListener(hydraulic, this));
    }

    /**
     * Creates the pack for the given mod.
     *
     * @param mod the mod to create the pack for
     * @param packPath the path to the pack
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void createPack(@NotNull ModInfo mod, @NotNull Path packPath) {
        // TODO: Create manifest
        // TODO: Other global stuff

        for (PackModule<?> module : this.modules) {
            module.create(new PackCreateContext(this.hydraulic, mod, module, packPath));
        }
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
