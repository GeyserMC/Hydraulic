package org.geysermc.hydraulic.pack;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.event.lifecycle.GeyserLoadResourcePacksEvent;
import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.hydraulic.util.FormatUtil;
import org.geysermc.hydraulic.util.PackUtil;
import org.geysermc.pack.bedrock.resource.Manifest;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipFile;

/**
 * Listens for events related to packs.
 */
public class PackListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();
    private static final ExecutorService THREAD_POOL;

    private final HydraulicImpl hydraulic;
    private final PackManager manager;

    static {
        THREAD_POOL = Executors.newFixedThreadPool(
            Math.max(1, Runtime.getRuntime().availableProcessors() * 3 / 8),
            new ThreadFactoryBuilder()
                .setNameFormat(Constants.MOD_NAME + " Conversion Thread #%d")
                .setUncaughtExceptionHandler((thread, throwable) -> LOGGER.error("Uncaught exception in thread {}", thread.getName(), throwable))
                .build()
        );
    }

    public PackListener(HydraulicImpl hydraulic, PackManager manager) {
        this.hydraulic = hydraulic;
        this.manager = manager;

        hydraulic.registerServerStop(server -> {
            THREAD_POOL.shutdown(); // Prevents the server from locking up on stop
        });
    }

    @Subscribe
    public void onLoadResourcePacks(GeyserLoadResourcePacksEvent event) {
        Path packsPath = GeyserApi.api().packDirectory();

        Map<String, Pair<ModInfo, Path>> packsToLoad = new HashMap<>();
        for (ModInfo mod : this.hydraulic.mods()) {
            if (PackManager.IGNORED_MODS.contains(mod.id())) {
                continue;
            }

            // Ignore generated mods
            if (mod.id().startsWith("generated_")) {
                continue;
            }

            Path packPath = packsPath.resolve(mod.id() + ".zip");
            if (this.hydraulic.isDev() || !event.resourcePacks().contains(packPath) || checkNeedsConversion(mod, packPath)) {
                packsToLoad.put(mod.id(), Pair.of(mod, packPath));
            }
        }

        if (packsToLoad.isEmpty()) {
            return;
        }

        LOGGER.info("Found {} packs to convert!", packsToLoad.size());

        long start = System.currentTimeMillis();

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (var entry : packsToLoad.entrySet()) {
            futures.add(CompletableFuture.runAsync(() -> {
                LOGGER.info("Converting pack for mod {}", entry.getKey());
                try {
                    if (this.manager.createPack(entry.getValue().getLeft(), entry.getValue().getRight())) {
                        event.resourcePacks().add(entry.getValue().getRight());
                    }
                } catch (Throwable t) {
                    LOGGER.error("Failed to convert pack for mod {}", entry.getKey(), t);
                }
            }, THREAD_POOL));
        }

        // Wait for all futures to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        LOGGER.info("Converted {} packs for mods in {}", packsToLoad.size(), FormatUtil.humanReadableFormat(System.currentTimeMillis() - start));
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
        // Read the uuid from the pack manifest
        String packUUID;
        try (
            ZipFile zip = new ZipFile(packPath.toFile());
            InputStream inputStream = zip.getInputStream(zip.getEntry("manifest.json"));
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream)
        ) {
            packUUID = GSON.fromJson(inputStreamReader, Manifest.class).header().uuid();
        } catch (IOException e) {
            return true;
        }

        String modUUID = PackUtil.getModUUID(mod.roots()).toString();

        return !modUUID.equals(packUUID);
    }
}
