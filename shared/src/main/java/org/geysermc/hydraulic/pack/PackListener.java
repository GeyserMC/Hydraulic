package org.geysermc.hydraulic.pack;

import com.mojang.logging.LogUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserLoadResourcePacksEvent;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Listens for events related to packs.
 */
public class PackListener {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final HydraulicImpl hydraulic;
    private final PackManager manager;

    public PackListener(HydraulicImpl hydraulic, PackManager manager) {
        this.hydraulic = hydraulic;
        this.manager = manager;
    }

    @Subscribe
    public void onLoadResourcePacks(GeyserLoadResourcePacksEvent event) {
        // TODO: Add this to Geyser API
        Path packsPath = GeyserImpl.getInstance().getBootstrap().getConfigFolder().resolve("packs");

        Map<String, Pair<ModInfo, Path>> packsToLoad = new HashMap<>();
        for (ModInfo mod : this.hydraulic.mods()) {
            if (PackManager.IGNORED_MODS.contains(mod.id())) {
                continue;
            }

            Path packPath = packsPath.resolve(mod.id() + ".zip");
            if (!event.resourcePacks().contains(packPath)) {
                packsToLoad.put(mod.id(), Pair.of(mod, packPath));
            }
        }

        if (packsToLoad.isEmpty()) {
            return;
        }

        LOGGER.info("Found {} packs to convert!", packsToLoad.size());

        this.manager.callEvents(event);

        for (Map.Entry<String, Pair<ModInfo, Path>> entry : packsToLoad.entrySet()) {
            try {
                if (this.manager.createPack(entry.getValue().getLeft(), entry.getValue().getRight())) {
                    event.resourcePacks().add(entry.getValue().getRight());
                }
            } catch (Throwable t) {
                LOGGER.error("Failed to convert pack for mod {}", entry.getKey(), t);
            }
        }
    }

    @Subscribe
    public void onDefineItems(GeyserDefineCustomItemsEvent event) {
        this.manager.callEvents(event);
    }
}
