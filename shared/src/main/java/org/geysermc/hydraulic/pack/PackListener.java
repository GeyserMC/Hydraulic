package org.geysermc.hydraulic.pack;

import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.event.lifecycle.GeyserLoadResourcePacksEvent;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.pack.bedrock.resource.Manifest;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipFile;

/**
 * Listens for events related to packs.
 */
public class PackListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();

    private final HydraulicImpl hydraulic;
    private final PackManager manager;

    public PackListener(HydraulicImpl hydraulic, PackManager manager) {
        this.hydraulic = hydraulic;
        this.manager = manager;
    }

    @Subscribe
    public void onLoadResourcePacks(GeyserLoadResourcePacksEvent event) {
        Path packsPath = GeyserApi.api().packDirectory();

        Map<String, Pair<ModInfo, Path>> packsToLoad = new HashMap<>();
        for (ModInfo mod : this.hydraulic.mods()) {
            if (PackManager.IGNORED_MODS.contains(mod.id())) {
                continue;
            }

            Path packPath = packsPath.resolve(mod.id() + ".zip");
            if (!event.resourcePacks().contains(packPath) || checkNeedsConversion(mod, packPath)) {
                packsToLoad.put(mod.id(), Pair.of(mod, packPath));
            }
        }

        if (packsToLoad.isEmpty()) {
            return;
        }

        LOGGER.info("Found {} packs to convert!", packsToLoad.size());

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

    /**
     * Checks if the pack needs to be converted based on the generated UUID.
     * This allows pack regeneration if the mod file has changed.
     *
     * @param mod The mod to check.
     * @param packPath The path to the pack.
     * @return {@code true} if the pack needs to be converted.
     */
    private boolean checkNeedsConversion(ModInfo mod, Path packPath) {
        String modUUID = "";
        String packUUID = "";

        try {
            modUUID = UUID.nameUUIDFromBytes(Files.readAllBytes(mod.modFile())).toString();
        } catch (IOException e) {
            return true;
        }

        // Read the uuid from the pack manifest
        try {
            ZipFile zip = new ZipFile(packPath.toFile());
            try (InputStream inputStream = zip.getInputStream(zip.getEntry("manifest.json"))){
                try(InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
                    packUUID = GSON.fromJson(inputStreamReader, Manifest.class).header().uuid();
                }
            }
        } catch (IOException e) {
            return true;
        }

        return !modUUID.equals(packUUID);
    }
}
