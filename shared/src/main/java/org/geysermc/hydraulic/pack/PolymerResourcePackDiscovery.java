package org.geysermc.hydraulic.pack;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HexFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Finds Polymer-generated Java resource packs so they can be converted by Hydraulic.
 */
final class PolymerResourcePackDiscovery {
    private static final Logger LOGGER = LogUtils.getLogger();

    private PolymerResourcePackDiscovery() {
    }

    static List<DiscoveredPack> discover(
            @NotNull HydraulicImpl hydraulic,
            @NotNull Collection<ModInfo> loadedMods,
            @NotNull PolymerDiagnosticReport diagnosticReport,
            @NotNull String phase,
            long delayMillis
    ) {
        boolean polymerInstalled = isPolymerInstalled(loadedMods);
        LOGGER.info("Polymer installed: {}", polymerInstalled);

        Path serverRoot = serverRoot(hydraulic);
        if (serverRoot == null) {
            diagnosticReport.recordDiscovery(polymerInstalled, null, List.of());
            diagnosticReport.recordDiscoveryAttempt(phase, delayMillis, List.of());
            LOGGER.debug("Skipping Polymer resource pack discovery because the server root could not be inferred");
            return List.of();
        }

        DiscoveryCandidates discoveryCandidates = candidatePaths(serverRoot);
        List<Path> candidates = discoveryCandidates.paths();
        diagnosticReport.recordDiscovery(polymerInstalled, serverRoot, candidates);
        diagnosticReport.recordPolymerConfig(
                discoveryCandidates.configPath(),
                discoveryCandidates.resourcePackLocation(),
                discoveryCandidates.resolvedResourcePackPath(),
                discoveryCandidates.configExists(),
                discoveryCandidates.resolvedResourcePackExists()
        );
        LOGGER.debug("Searching {} candidate Polymer resource pack location(s) below {}", candidates.size(), serverRoot);

        Set<Path> discovered = new LinkedHashSet<>();
        for (Path candidate : candidates) {
            LOGGER.debug("Searching for Polymer resource pack at {}", candidate);
            if (isResourcePack(candidate)) {
                discovered.add(normalize(candidate));
            }
        }

        if (discovered.isEmpty()) {
            diagnosticReport.recordDiscoveryAttempt(phase, delayMillis, List.of());
            if (phase.equals("initial")) {
                LOGGER.info("No Polymer-generated resource packs were found during initial scan. Hydraulic will check again during resource-pack registration if Polymer assets should be converted.");
            } else {
                LOGGER.info("No Polymer-generated resource packs were found during {} discovery.", phase);
            }
            return List.of();
        }

        List<DiscoveredPack> packs = new ArrayList<>();
        int index = 0;
        for (Path root : discovered) {
            AssetCounts counts = countAssets(root);
            NamespaceAssetCounts polyDecorationsCounts = countNamespaceAssets(root, "polydecorations");
            long sourceSize = size(root);
            String hash = sha256(root);
            String id = index == 0 ? "polymer_resourcepack" : "polymer_resourcepack_" + (index + 1);
            String name = index == 0 ? "Polymer Generated Resource Pack" : "Polymer Generated Resource Pack " + (index + 1);

            LOGGER.info(
                    "Found Polymer resource pack {} ({} bytes, sha256 {}) with {} model(s), {} texture(s), and {} item definition(s)",
                    root,
                    sourceSize,
                    hash,
                    counts.models(),
                    counts.textures(),
                    counts.items()
            );
            if (!polyDecorationsCounts.isEmpty()) {
                LOGGER.info(
                        "Polymer resource pack {} contains PolyDecorations assets: itemModels={}, blockModels={}, textures={}, blockstates={}",
                        root,
                        polyDecorationsCounts.itemModels(),
                        polyDecorationsCounts.blockModels(),
                        polyDecorationsCounts.textures(),
                        polyDecorationsCounts.blockstates()
                );
            }

            ModInfo mod = new ModInfo(
                    id,
                    "polymer",
                    name,
                    "generated",
                    null,
                    List.of(root)
            );
            DiscoveredPack pack = new DiscoveredPack(mod, root, sourceSize, hash, counts);
            diagnosticReport.recordDiscoveredPack(pack);
            if (!polyDecorationsCounts.isEmpty()) {
                diagnosticReport.recordNamespaceAssetCounts(
                        id,
                        "polydecorations",
                        polyDecorationsCounts.itemModels(),
                        polyDecorationsCounts.blockModels(),
                        polyDecorationsCounts.textures(),
                        polyDecorationsCounts.blockstates()
                );
            }
            packs.add(pack);
            index++;
        }

        diagnosticReport.recordDiscoveryAttempt(phase, delayMillis, packs.stream().map(DiscoveredPack::source).toList());
        return List.copyOf(packs);
    }

    static boolean isPolymerInstalled(@NotNull Collection<ModInfo> loadedMods) {
        return loadedMods.stream().anyMatch(PolymerResourcePackDiscovery::isPolymerMod);
    }

    private static boolean isPolymerMod(ModInfo mod) {
        String id = mod.id().toLowerCase(Locale.ROOT);
        return id.equals("polymer") || id.startsWith("polymer-");
    }

    private static Path serverRoot(HydraulicImpl hydraulic) {
        Path dataFolder = hydraulic.dataFolder(Constants.MOD_ID);
        Path configDir = dataFolder.getParent();
        if (configDir == null) {
            return null;
        }
        return normalize(configDir.getParent() == null ? configDir : configDir.getParent());
    }

    private static DiscoveryCandidates candidatePaths(Path serverRoot) {
        Path config = serverRoot.resolve("config");
        Path polymerConfig = config.resolve("polymer");
        Path resourcePackConfig = polymerConfig.resolve("resource-pack.json");
        ConfiguredResourcePack configuredPack = readConfiguredResourcePack(serverRoot, resourcePackConfig);

        List<Path> paths = new ArrayList<>(16);
        if (configuredPack.resolvedPath() != null) {
            paths.add(configuredPack.resolvedPath());
        }

        paths.addAll(List.of(
                serverRoot.resolve("polymer").resolve("resource_pack.zip"),
                serverRoot.resolve("polymer-resourcepack.zip"),
                serverRoot.resolve("resource-pack"),
                serverRoot.resolve("resource-pack").resolve("polymer-resourcepack.zip"),
                serverRoot.resolve("resource_pack"),
                serverRoot.resolve("resource_pack").resolve("polymer-resourcepack.zip"),
                serverRoot.resolve("resourcepacks").resolve("polymer-resourcepack.zip"),
                polymerConfig.resolve("polymer-resourcepack.zip"),
                polymerConfig.resolve("resource-pack"),
                polymerConfig.resolve("resource-pack.zip"),
                polymerConfig.resolve("resource_pack"),
                polymerConfig.resolve("resource_pack.zip"),
                polymerConfig.resolve("generated"),
                polymerConfig.resolve("generated").resolve("polymer-resourcepack.zip")
        ));

        return new DiscoveryCandidates(
                List.copyOf(new LinkedHashSet<>(paths)),
                resourcePackConfig,
                configuredPack.location(),
                configuredPack.resolvedPath(),
                configuredPack.configExists(),
                configuredPack.resolvedPath() != null && Files.exists(configuredPack.resolvedPath())
        );
    }

    private static ConfiguredResourcePack readConfiguredResourcePack(Path serverRoot, Path configPath) {
        boolean configExists = Files.isRegularFile(configPath);
        if (!configExists) {
            LOGGER.debug("Polymer resource pack config {} does not exist", configPath);
            return new ConfiguredResourcePack(null, null, false);
        }

        try (Reader reader = Files.newBufferedReader(configPath)) {
            JsonObject object = Constants.GSON.fromJson(reader, JsonObject.class);
            if (object == null || !object.has("resource_pack_location") || object.get("resource_pack_location").isJsonNull()) {
                LOGGER.debug("Polymer resource pack config {} does not define resource_pack_location", configPath);
                return new ConfiguredResourcePack(null, null, true);
            }

            String location = object.get("resource_pack_location").getAsString();
            if (location == null || location.isBlank()) {
                LOGGER.debug("Polymer resource pack config {} defines a blank resource_pack_location", configPath);
                return new ConfiguredResourcePack(location, null, true);
            }

            Path resolvedPath = Path.of(location);
            if (!resolvedPath.isAbsolute()) {
                resolvedPath = serverRoot.resolve(resolvedPath);
            }
            resolvedPath = normalize(resolvedPath);
            LOGGER.info(
                    "Polymer resource pack config {} defines resource_pack_location={} resolved={} exists={}",
                    configPath,
                    location,
                    resolvedPath,
                    Files.exists(resolvedPath)
            );
            return new ConfiguredResourcePack(location, resolvedPath, true);
        } catch (IOException | RuntimeException ex) {
            LOGGER.warn("Unable to read Polymer resource pack config {}", configPath, ex);
            return new ConfiguredResourcePack(null, null, true);
        }
    }

    private static boolean isResourcePack(Path path) {
        if (path == null || Files.notExists(path)) {
            return false;
        }

        if (Files.isDirectory(path)) {
            return Files.isDirectory(path.resolve("assets")) || Files.isRegularFile(path.resolve("pack.mcmeta"));
        }

        if (Files.isRegularFile(path) && isArchive(path)) {
            String fileName = path.getFileName().toString().toLowerCase(Locale.ROOT);
            if (fileName.equals("polymer-resourcepack.zip")) {
                return true;
            }
            try (FileSystem fileSystem = FileSystems.newFileSystem(path)) {
                Path root = fileSystem.getPath("/");
                return Files.isDirectory(root.resolve("assets")) || Files.isRegularFile(root.resolve("pack.mcmeta"));
            } catch (IOException | RuntimeException ex) {
                LOGGER.warn("Unable to inspect possible Polymer resource pack {}", path, ex);
            }
        }

        return false;
    }

    private static AssetCounts countAssets(Path root) {
        if (Files.isRegularFile(root) && isArchive(root)) {
            try (FileSystem fileSystem = FileSystems.newFileSystem(root)) {
                return countAssets(fileSystem.getPath("/"));
            } catch (IOException | RuntimeException ex) {
                LOGGER.warn("Unable to count assets in Polymer resource pack {}", root, ex);
                return AssetCounts.EMPTY;
            }
        }

        if (!Files.isDirectory(root.resolve("assets"))) {
            return AssetCounts.EMPTY;
        }

        int models = 0;
        int textures = 0;
        int items = 0;
        try (Stream<Path> stream = Files.walk(root.resolve("assets"))) {
            for (Path path : stream.filter(Files::isRegularFile).toList()) {
                String relativePath = normalizePath(root.relativize(path));
                if (relativePath.contains("/models/") && relativePath.endsWith(".json")) {
                    models++;
                } else if (relativePath.contains("/textures/") && relativePath.endsWith(".png")) {
                    textures++;
                } else if (relativePath.contains("/items/") && relativePath.endsWith(".json")) {
                    items++;
                }
            }
        } catch (IOException | RuntimeException ex) {
            LOGGER.warn("Unable to count assets in Polymer resource pack {}", root, ex);
            return AssetCounts.EMPTY;
        }

        return new AssetCounts(models, textures, items);
    }

    private static NamespaceAssetCounts countNamespaceAssets(Path root, String namespace) {
        if (Files.isRegularFile(root) && isArchive(root)) {
            try (FileSystem fileSystem = FileSystems.newFileSystem(root)) {
                return countNamespaceAssets(fileSystem.getPath("/"), namespace);
            } catch (IOException | RuntimeException ex) {
                LOGGER.warn("Unable to count {} assets in Polymer resource pack {}", namespace, root, ex);
                return NamespaceAssetCounts.EMPTY;
            }
        }

        Path namespaceRoot = root.resolve("assets").resolve(namespace);
        if (!Files.isDirectory(namespaceRoot)) {
            return NamespaceAssetCounts.EMPTY;
        }

        int itemModels = 0;
        int blockModels = 0;
        int textures = 0;
        int blockstates = 0;
        try (Stream<Path> stream = Files.walk(namespaceRoot)) {
            for (Path path : stream.filter(Files::isRegularFile).toList()) {
                String relativePath = normalizePath(namespaceRoot.relativize(path));
                if (relativePath.startsWith("models/item/") && relativePath.endsWith(".json")) {
                    itemModels++;
                } else if (relativePath.startsWith("models/block/") && relativePath.endsWith(".json")) {
                    blockModels++;
                } else if (relativePath.startsWith("textures/") && relativePath.endsWith(".png")) {
                    textures++;
                } else if (relativePath.startsWith("blockstates/") && relativePath.endsWith(".json")) {
                    blockstates++;
                }
            }
        } catch (IOException | RuntimeException ex) {
            LOGGER.warn("Unable to count {} assets in Polymer resource pack {}", namespace, root, ex);
            return NamespaceAssetCounts.EMPTY;
        }

        return new NamespaceAssetCounts(itemModels, blockModels, textures, blockstates);
    }

    private static long size(Path root) {
        try {
            if (Files.isRegularFile(root)) {
                return Files.size(root);
            }

            if (Files.isDirectory(root)) {
                try (Stream<Path> stream = Files.walk(root)) {
                    return stream
                            .filter(Files::isRegularFile)
                            .mapToLong(PolymerResourcePackDiscovery::sizeUnchecked)
                            .sum();
                }
            }
        } catch (IOException | RuntimeException ex) {
            LOGGER.warn("Unable to calculate Polymer resource pack size for {}", root, ex);
        }

        return -1L;
    }

    private static long sizeUnchecked(Path path) {
        try {
            return Files.size(path);
        } catch (IOException ex) {
            LOGGER.debug("Unable to read size for {}", path, ex);
            return 0L;
        }
    }

    private static String sha256(Path root) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            if (Files.isRegularFile(root)) {
                updateDigest(digest, root);
            } else if (Files.isDirectory(root)) {
                try (Stream<Path> stream = Files.walk(root)) {
                    for (Path path : stream.filter(Files::isRegularFile).sorted().toList()) {
                        digest.update(normalizePath(root.relativize(path)).getBytes(java.nio.charset.StandardCharsets.UTF_8));
                        updateDigest(digest, path);
                    }
                }
            }
            return HexFormat.of().formatHex(digest.digest());
        } catch (IOException | NoSuchAlgorithmException | RuntimeException ex) {
            LOGGER.warn("Unable to calculate Polymer resource pack SHA256 for {}", root, ex);
            return "unknown";
        }
    }

    private static void updateDigest(MessageDigest digest, Path path) throws IOException {
        try (InputStream inputStream = Files.newInputStream(path);
             DigestInputStream digestInputStream = new DigestInputStream(inputStream, digest)) {
            digestInputStream.transferTo(OutputStream.nullOutputStream());
        }
    }

    private static boolean isArchive(Path path) {
        Path fileNamePath = path.getFileName();
        if (fileNamePath == null) {
            return false;
        }

        String fileName = fileNamePath.toString().toLowerCase(Locale.ROOT);
        return fileName.endsWith(".jar") || fileName.endsWith(".zip");
    }

    private static Path normalize(Path path) {
        return path.toAbsolutePath().normalize();
    }

    private static String normalizePath(Path path) {
        return path.toString().replace('\\', '/');
    }

    record DiscoveredPack(ModInfo mod, Path source, long sourceSize, String sha256, AssetCounts counts) {
    }

    private record DiscoveryCandidates(
            List<Path> paths,
            Path configPath,
            String resourcePackLocation,
            Path resolvedResourcePackPath,
            boolean configExists,
            boolean resolvedResourcePackExists
    ) {
    }

    private record ConfiguredResourcePack(String location, Path resolvedPath, boolean configExists) {
    }

    record AssetCounts(int models, int textures, int items) {
        private static final AssetCounts EMPTY = new AssetCounts(0, 0, 0);
    }

    private record NamespaceAssetCounts(int itemModels, int blockModels, int textures, int blockstates) {
        private static final NamespaceAssetCounts EMPTY = new NamespaceAssetCounts(0, 0, 0, 0);

        private boolean isEmpty() {
            return this.itemModels == 0 && this.blockModels == 0 && this.textures == 0 && this.blockstates == 0;
        }
    }
}
