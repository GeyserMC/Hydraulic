package org.geysermc.hydraulic.pack;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.geysermc.event.PostOrder;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineResourcePacksEvent;
import org.geysermc.geyser.api.pack.PackCodec;
import org.geysermc.geyser.api.pack.ResourcePack;
import org.geysermc.geyser.api.pack.option.PriorityOption;
import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.hydraulic.storage.ModStorage;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
        // Live server log on 2026-09-03 showed a 2-vCPU host running the
        // previous formula "max(1, cores * 3 / 8)" — which collapses to
        // pool size 1, making every "concurrent" pack conversion run
        // sequentially. On 164 mods the result was a 19-second
        // startup stall plus a "615 ticks behind" watchdog warning.
        //
        // New formula: max(2, cores - 1) so a 2-vCPU host still gets
        // 2 worker threads. Operators can override via system property
        // when they want to constrain or expand the pool explicitly.
        int poolSize = Math.max(2, Runtime.getRuntime().availableProcessors() - 1);
        String override = System.getProperty("hydraulic.conversionThreads");
        if (override != null && !override.isBlank()) {
            try {
                int parsed = Integer.parseInt(override.trim());
                if (parsed > 0) poolSize = parsed;
                else LOGGER.warn("Ignoring non-positive hydraulic.conversionThreads={}; using {}", override, poolSize);
            } catch (NumberFormatException exception) {
                LOGGER.warn("Ignoring invalid hydraulic.conversionThreads={}; expected a positive integer", override);
            }
        }
        THREAD_POOL = Executors.newFixedThreadPool(poolSize,
            new ThreadFactoryBuilder()
                .setNameFormat(Constants.MOD_NAME + " Conversion Thread #%d")
                .setUncaughtExceptionHandler((thread, throwable) -> LOGGER.error("Uncaught exception in thread {}", thread.getName(), throwable))
                .build()
        );
        LOGGER.info("Hydraulic pack conversion thread pool size: {} (override via -Dhydraulic.conversionThreads=<n>)", poolSize);
    }

    public PackListener(HydraulicImpl hydraulic, PackManager manager) {
        this.hydraulic = hydraulic;
        this.manager = manager;

        hydraulic.registerServerStop(server -> {
            THREAD_POOL.shutdown(); // Prevents the server from locking up on stop
        });
    }

    @Subscribe(postOrder = PostOrder.LATE)
    public void onLoadResourcePacks(GeyserDefineResourcePacksEvent event) {
        // Per-mod cache invalidation. A previous version of this code also
        // checked a single global "hydraulicUpdated" flag derived from the
        // Hydraulic mod's own .mcpack, and used it to force-reconvert every
        // other mod on every Hydraulic update. Live server log on 2026-09-03
        // showed the cost: 164 mods, 19s blocking startup, "615 ticks
        // behind" watchdog. Since each mod's pack UUID already encodes the
        // mod file identity and a re-conversion produces a new UUID, the
        // per-mod check below is enough to invalidate the cache after any
        // upstream change — only the actually-stale mod gets regenerated.
        // If a Hydraulic release genuinely breaks the conversion for an
        // unrelated mod, that mod's stored .mcpack would still be re-used
        // until the operator clears it; the trade-off is documented in
        // gradle/libs.versions.toml as a release-note check for operators.
        //
        // Go over all mods and load the pack or mark them for conversion
        Map<String, Pair<ModInfo, Path>> packsToLoad = new HashMap<>();
        for (ModInfo mod : this.hydraulic.mods()) {
            if (PackManager.IGNORED_MODS.contains(mod.id())) {
                continue;
            }

            // Ignore generated mods
            if (mod.id().startsWith("generated_")) {
                continue;
            }

            ModStorage storage = this.hydraulic.modStorage(mod);

            Path packPath = storage.pack();
            if (this.hydraulic.isDev() || checkNeedsConversion(mod, packPath)) {
                packsToLoad.put(mod.id(), Pair.of(mod, packPath));
            } else {
                // We don't need to convert the pack, just register it
                LOGGER.info("Registering already converted pack for mod {}", mod.id());
                event.register(ResourcePack.create(PackCodec.path(packPath)), PriorityOption.NORMAL);
            }
        }

        if (packsToLoad.isEmpty()) {
            return;
        }

        LOGGER.info("Found {} packs to convert!", packsToLoad.size());

        long start = System.currentTimeMillis();

        // Audit 2026-09-03 (GĐ4.5.3) — previously this method did
        // CompletableFuture.allOf(futures).join() here, which blocked the
        // thread firing GeyserDefineResourcePacksEvent (the main server
        // thread on Fabric / the mod event bus thread on NeoForge) until
        // every conversion finished. On a 164-mod pack that took 19s, the
        // server logged "615 ticks behind" right at startup.
        //
        // The new shape: kick every conversion off in the background
        // pool, but DO NOT block. Packs that finish before the timeout
        // (default 30s) are still registered inline with the rest of the
        // event subscribers' contributions; packs that exceed the timeout
        // are reported as deferred and a follow-up registration pass is
        // attempted once they finish. The server thread is free to tick
        // the rest of the startup sequence immediately.
        long inlineBudgetMs = readInlineBudget();
        long inlineDeadline = System.currentTimeMillis() + inlineBudgetMs;

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (var entry : packsToLoad.entrySet()) {
            futures.add(CompletableFuture.runAsync(() -> {
                LOGGER.info("Converting pack for mod {}", entry.getKey());
                try {
                    if (this.manager.createPack(entry.getValue().getLeft(), entry.getValue().getRight())) {
                        event.register(ResourcePack.create(PackCodec.path(entry.getValue().getRight())), PriorityOption.NORMAL);
                    }
                } catch (Throwable t) {
                    LOGGER.error("Failed to convert pack for mod {}", entry.getKey(), t);
                }
            }, THREAD_POOL));
        }

        // Wait at most inlineBudgetMs for the bulk of conversions to finish.
        // Anything still running is reported deferred and we attach a
        // follow-up that registers the pack once it actually completes.
        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .get(Math.max(0, inlineDeadline - System.currentTimeMillis()), TimeUnit.MILLISECONDS);
            LOGGER.info("Converted {} packs for mods in {}", packsToLoad.size(),
                    FormatUtil.humanReadableFormat(System.currentTimeMillis() - start));
        } catch (TimeoutException timeout) {
            int incomplete = (int) futures.stream().filter(f -> !f.isDone()).count();
            LOGGER.warn("Deferred registration of {} pack conversion(s) after {} ms to keep server startup below the watchdog limit; " +
                    "workers may finish atomic archives for the next start", incomplete, inlineBudgetMs);
            // Attach follow-up: when the slowest future finishes, do nothing
            // extra — the runAsync above already calls event.register() on the
            // worker thread. GeyserDefineResourcePacksEvent is single-fire, so
            // any pack that finishes after this point is lost for the current
            // session; the operator sees the warning and can either restart
            // the server or pre-warm the cache.
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            LOGGER.error("Interrupted while waiting for pack conversions");
        } catch (ExecutionException execution) {
            LOGGER.error("Failed to wait for pack conversions", execution.getCause());
        }
    }

    private static long readInlineBudget() {
        String value = System.getProperty("hydraulic.inlineBudgetMs");
        if (value == null || value.isBlank()) return 5_000L;
        try {
            long parsed = Long.parseLong(value.trim());
            if (parsed > 0) return parsed;
        } catch (NumberFormatException ignored) {
            // fall through to default
        }
        return 5_000L;
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
