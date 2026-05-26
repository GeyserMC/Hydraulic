package org.geysermc.hydraulic.pack;

import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.pack.converter.PackConverter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Collects Polymer conversion diagnostics and writes a compact, shareable report.
 */
public final class PolymerDiagnosticReport {
    private static final Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_NAME);
    private static final DateTimeFormatter FILE_TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")
            .withZone(ZoneOffset.UTC);
    private static final int DEFAULT_MAX_WARNINGS = 25;

    private final HydraulicImpl hydraulic;
    private final Path configPath;
    private final Path reportDirectory;
    private final boolean enabled;
    private final int maxWarnings;
    private final boolean includeStacktrace;
    private final Instant createdAt = Instant.now();
    private final Map<String, PackEntry> packs = new LinkedHashMap<>();
    private final List<String> searchedPaths = new ArrayList<>();
    private final List<DiscoveryAttempt> discoveryAttempts = new ArrayList<>();
    private final List<DetailedWarning> detailedWarnings = new ArrayList<>();
    private final List<FatalError> fatalErrors = new ArrayList<>();

    private boolean polymerInstalled;
    private String serverRoot = "unknown";
    private String polymerConfigPath = "unknown";
    private String polymerResourcePackLocation = "unknown";
    private String polymerResolvedResourcePackPath = "unknown";
    private boolean polymerConfigExists;
    private boolean polymerResolvedResourcePackExists;

    private PolymerDiagnosticReport(
            @NotNull HydraulicImpl hydraulic,
            @NotNull Path configPath,
            @NotNull Path reportDirectory,
            boolean enabled,
            int maxWarnings,
            boolean includeStacktrace
    ) {
        this.hydraulic = hydraulic;
        this.configPath = configPath;
        this.reportDirectory = reportDirectory;
        this.enabled = enabled;
        this.maxWarnings = maxWarnings;
        this.includeStacktrace = includeStacktrace;
    }

    public static PolymerDiagnosticReport create(@NotNull HydraulicImpl hydraulic) {
        Path dataFolder = hydraulic.dataFolder(Constants.MOD_ID);
        Path configPath = dataFolder.resolve("polymer-debug.properties");
        Properties properties = loadConfig(configPath);
        boolean enabled = Boolean.parseBoolean(properties.getProperty("enablePolymerDebugReports", "true"));
        int maxWarnings = parsePositiveInt(properties.getProperty("polymerDebugMaxWarnings"), DEFAULT_MAX_WARNINGS);
        boolean includeStacktrace = Boolean.parseBoolean(properties.getProperty("polymerDebugIncludeStacktrace", "false"));
        return new PolymerDiagnosticReport(
                hydraulic,
                configPath,
                dataFolder.resolve("debug"),
                enabled,
                maxWarnings,
                includeStacktrace
        );
    }

    public boolean enabled() {
        return this.enabled;
    }

    public synchronized boolean shouldWrite() {
        return this.enabled && (this.polymerInstalled || !this.packs.isEmpty() || !this.fatalErrors.isEmpty());
    }

    public synchronized void recordDiscovery(boolean polymerInstalled, Path serverRoot, List<Path> searchedPaths) {
        if (!this.enabled) {
            return;
        }

        this.polymerInstalled = polymerInstalled;
        this.serverRoot = serverRoot == null ? "unknown" : safePath(serverRoot);
        this.searchedPaths.clear();
        searchedPaths.stream()
                .map(PolymerDiagnosticReport::safePath)
                .forEach(this.searchedPaths::add);
    }

    synchronized void recordPolymerConfig(Path configPath, String resourcePackLocation, Path resolvedPath, boolean configExists, boolean resolvedPathExists) {
        if (!this.enabled) {
            return;
        }

        this.polymerConfigPath = safePath(configPath);
        this.polymerResourcePackLocation = resourcePackLocation == null || resourcePackLocation.isBlank() ? "unknown" : resourcePackLocation;
        this.polymerResolvedResourcePackPath = safePath(resolvedPath);
        this.polymerConfigExists = configExists;
        this.polymerResolvedResourcePackExists = resolvedPathExists;
    }

    synchronized void recordDiscoveryAttempt(@NotNull String phase, long delayMillis, @NotNull List<Path> foundPaths) {
        if (!this.enabled) {
            return;
        }

        this.discoveryAttempts.add(new DiscoveryAttempt(
                phase,
                delayMillis,
                !foundPaths.isEmpty(),
                foundPaths.stream().map(PolymerDiagnosticReport::safePath).toList()
        ));
    }

    synchronized void recordDiscoveredPack(@NotNull PolymerResourcePackDiscovery.DiscoveredPack pack) {
        if (!this.enabled) {
            return;
        }

        PackEntry entry = this.packEntry(pack.mod().id());
        entry.packId = pack.mod().id();
        entry.packName = pack.mod().name();
        entry.sourcePath = safePath(pack.source());
        entry.sourceSha256 = pack.sha256();
        entry.sourceSize = pack.sourceSize();
        entry.modelCount = pack.counts().models();
        entry.textureCount = pack.counts().textures();
        entry.itemModelCount = pack.counts().items();
    }

    public synchronized void recordCacheStatus(@NotNull ModInfo mod, @NotNull String status, Path convertedPath) {
        if (!this.enabled) {
            return;
        }

        PackEntry entry = this.packEntry(mod.id());
        entry.packId = mod.id();
        entry.packName = mod.name();
        entry.cacheStatus = status;
        if (convertedPath != null) {
            entry.convertedPackPath = safePath(convertedPath);
        }
    }

    public synchronized void recordDirectConversionSkipped(@NotNull ModInfo mod, @NotNull String reason) {
        if (!this.enabled) {
            return;
        }

        PackEntry entry = this.packEntry(mod.id());
        entry.packId = mod.id();
        entry.packName = mod.name();
        entry.directConversionSkipped = true;
        entry.directConversionSkipReason = reason;
    }

    synchronized void recordNamespaceAssetCounts(
            @NotNull String packId,
            @NotNull String namespace,
            int itemModels,
            int blockModels,
            int textures,
            int blockstates
    ) {
        if (!this.enabled) {
            return;
        }

        PackEntry entry = this.packEntry(packId);
        entry.namespaceAssetCounts.put(namespace, new NamespaceAssetCounts(namespace, itemModels, blockModels, textures, blockstates));
    }

    synchronized void recordSanitizedRoot(
            @NotNull String packId,
            Path sourcePath,
            Path sanitizedPath,
            boolean exists,
            boolean directory,
            boolean readable,
            boolean writable,
            long listingCount,
            boolean zipSource,
            boolean zipOpened,
            @NotNull String status
    ) {
        if (!this.enabled) {
            return;
        }

        PackEntry entry = this.packEntry(packId);
        entry.sanitizedRoots.add(new SanitizedRoot(
                safePath(sourcePath),
                safePath(sanitizedPath),
                exists,
                directory,
                readable,
                writable,
                listingCount,
                zipSource,
                zipOpened,
                status
        ));
    }

    synchronized void recordConversionResult(
            @NotNull ModInfo mod,
            @NotNull Path convertedPath,
            long convertedSize,
            int skippedAssets,
            int warningCount
    ) {
        if (!this.enabled) {
            return;
        }

        PackEntry entry = this.packEntry(mod.id());
        entry.packId = mod.id();
        entry.packName = mod.name();
        entry.convertedPackPath = safePath(convertedPath);
        entry.convertedPackSize = convertedSize;
        entry.skippedAssetCount += skippedAssets;
        entry.warningCount += warningCount;
    }

    synchronized void recordTextureSkipped(
            @NotNull String packId,
            @NotNull String filePath,
            int width,
            int height,
            @NotNull String reason
    ) {
        if (!this.enabled) {
            return;
        }

        PackEntry entry = this.packEntry(packId);
        entry.skippedOversizedTextures++;
        this.addWarning(packId, filePath, reason + " (" + width + "x" + height + ")", "skipped texture");
    }

    public synchronized void recordUnsupportedSummary(
            @NotNull String packId,
            int customModelOverrides,
            int multipartBlockstates,
            int parentChainIssues,
            int missingTextures,
            int skippedOversizedTextures,
            int placeholderFallbacks,
            int skippedAssets,
            int warningCount
    ) {
        if (!this.enabled) {
            return;
        }

        PackEntry entry = this.packEntry(packId);
        entry.unsupportedCustomModelOverrides += customModelOverrides;
        entry.unsupportedMultipartBlockstates += multipartBlockstates;
        entry.parentChainIssues += parentChainIssues;
        entry.missingTextures += missingTextures;
        entry.skippedOversizedTextures += skippedOversizedTextures;
        entry.placeholderFallbacks += placeholderFallbacks;
        entry.skippedAssetCount += skippedAssets;
        entry.warningCount += warningCount;
    }

    public synchronized void recordPolymerVisualSummary(
            @NotNull String packId,
            int itemTexturesRegistered,
            int placeholderItemFallbacks,
            int placedBlockFallbacks,
            int nullableJsonDefaultsApplied,
            int nullKeyTextureEntriesSkipped
    ) {
        if (!this.enabled) {
            return;
        }

        PackEntry entry = this.packEntry(packId);
        entry.itemTexturesRegistered += itemTexturesRegistered;
        entry.placeholderItemFallbacks += placeholderItemFallbacks;
        entry.placedBlockFallbacks += placedBlockFallbacks;
        entry.nullableJsonDefaultsApplied += nullableJsonDefaultsApplied;
        entry.nullKeyTextureEntriesSkipped += nullKeyTextureEntriesSkipped;
    }

    public synchronized void recordPolymerItemTextureResolution(
            @NotNull String packId,
            int attempts,
            int successes,
            int placeholders,
            @NotNull Map<String, Integer> fallbackReasons
    ) {
        if (!this.enabled) {
            return;
        }

        PackEntry entry = this.packEntry(packId);
        entry.itemTextureResolutionAttempts += attempts;
        entry.itemTextureResolutionSuccesses += successes;
        entry.itemTextureResolutionPlaceholders += placeholders;
        fallbackReasons.forEach((reason, count) -> entry.itemTextureFallbackReasons.merge(reason, count, Integer::sum));
    }

    public synchronized void recordPolymerBlockFallbackSummary(
            @NotNull String packId,
            int inspected,
            int simpleMappingsResolved,
            int decorativeFallbacks,
            int placeholderFallbacks,
            int invisiblePlacementPreventions,
            @NotNull Map<String, Integer> fallbackReasons
    ) {
        if (!this.enabled) {
            return;
        }

        PackEntry entry = this.packEntry(packId);
        entry.polymerBlockModelsInspected += inspected;
        entry.simpleBlockMappingsResolved += simpleMappingsResolved;
        entry.decorativeBlockFallbacks += decorativeFallbacks;
        entry.placeholderBlockFallbacks += placeholderFallbacks;
        entry.invisiblePlacementPreventions += invisiblePlacementPreventions;
        fallbackReasons.forEach((reason, count) -> entry.blockFallbackReasons.merge(reason, count, Integer::sum));
    }

    public synchronized void recordWarning(
            @NotNull String packId,
            @NotNull String filePath,
            @NotNull String reason,
            @NotNull String fallback
    ) {
        if (!this.enabled) {
            return;
        }

        this.addWarning(packId, filePath, reason, fallback);
    }

    public synchronized void recordFatalError(@NotNull ModInfo mod, @NotNull Throwable throwable) {
        if (!this.enabled) {
            return;
        }

        PackEntry entry = this.packEntry(mod.id());
        entry.packId = mod.id();
        entry.packName = mod.name();
        entry.cacheStatus = entry.cacheStatus.equals("unknown") ? "failed" : entry.cacheStatus;
        this.fatalErrors.add(new FatalError(mod.id(), throwable.getClass().getName(), message(throwable), stackFrames(throwable, this.includeStacktrace)));
    }

    public Path write() {
        if (!this.enabled) {
            return null;
        }

        try {
            Files.createDirectories(this.reportDirectory);
            Path reportPath = this.reportDirectory.resolve("polymer-report-" + FILE_TIMESTAMP.format(Instant.now()) + ".md");
            try (BufferedWriter writer = Files.newBufferedWriter(reportPath)) {
                writer.write(this.render());
            }
            LOGGER.info("Polymer diagnostic report written to {}", reportPath);
            return reportPath;
        } catch (IOException | RuntimeException ex) {
            LOGGER.warn("Unable to write Polymer diagnostic report", ex);
            return null;
        }
    }

    private String render() {
        StringBuilder builder = new StringBuilder();
        builder.append("# Hydraulic Polymer Diagnostic Report\n\n");
        builder.append("Generated: `").append(this.createdAt).append("`\n\n");

        builder.append("## Environment\n\n");
        appendRow(builder, "Hydraulic version", modVersion(Constants.MOD_ID));
        appendRow(builder, "PackConverter version", packConverterVersion());
        appendRow(builder, "Minecraft version", minecraftVersion());
        appendRow(builder, "Fabric Loader version", modVersion("fabricloader"));
        appendRow(builder, "Geyser version", geyserVersion());
        appendRow(builder, "Java version", System.getProperty("java.version", "unknown"));
        appendRow(builder, "OS", System.getProperty("os.name", "unknown") + " " + System.getProperty("os.version", "") + " " + System.getProperty("os.arch", ""));
        appendRow(builder, "Report config", safePath(this.configPath));
        builder.append('\n');

        builder.append("## Discovery\n\n");
        appendRow(builder, "Polymer installed", Boolean.toString(this.polymerInstalled));
        appendRow(builder, "Server root", this.serverRoot);
        appendRow(builder, "Polymer config path", this.polymerConfigPath);
        appendRow(builder, "Polymer config exists", Boolean.toString(this.polymerConfigExists));
        appendRow(builder, "resource_pack_location", this.polymerResourcePackLocation);
        appendRow(builder, "Resolved Polymer output path", this.polymerResolvedResourcePackPath);
        appendRow(builder, "Resolved Polymer output exists", Boolean.toString(this.polymerResolvedResourcePackExists));
        builder.append("\nSearched paths:\n\n");
        if (this.searchedPaths.isEmpty()) {
            builder.append("- none recorded\n");
        } else {
            this.searchedPaths.forEach(path -> builder.append("- `").append(path).append("`\n"));
        }
        builder.append('\n');

        builder.append("Discovery attempts:\n\n");
        if (this.discoveryAttempts.isEmpty()) {
            builder.append("- none recorded\n");
        } else {
            for (DiscoveryAttempt attempt : this.discoveryAttempts) {
                builder.append("- phase=`").append(attempt.phase())
                        .append("`, delay=`").append(attempt.delayMillis())
                        .append("ms`, found=`").append(attempt.found()).append("`");
                if (!attempt.foundPaths().isEmpty()) {
                    builder.append(", paths=");
                    for (int i = 0; i < attempt.foundPaths().size(); i++) {
                        if (i > 0) {
                            builder.append(", ");
                        }
                        builder.append('`').append(attempt.foundPaths().get(i)).append('`');
                    }
                }
                builder.append('\n');
            }
        }
        builder.append('\n');

        builder.append("## Packs\n\n");
        if (this.packs.isEmpty()) {
            builder.append("No Polymer resource packs were discovered or processed.\n\n");
        } else {
            this.packs.values().stream()
                    .sorted(Comparator.comparing(entry -> entry.packId))
                    .forEach(entry -> appendPack(builder, entry));
        }

        builder.append("## Detailed Warnings\n\n");
        if (this.detailedWarnings.isEmpty()) {
            builder.append("No detailed warnings were recorded.\n\n");
        } else {
            int limit = Math.min(this.maxWarnings, this.detailedWarnings.size());
            for (int i = 0; i < limit; i++) {
                DetailedWarning warning = this.detailedWarnings.get(i);
                builder.append("- `").append(warning.packId()).append("` `")
                        .append(warning.filePath()).append("`: ")
                        .append(warning.reason())
                        .append(" (fallback: ").append(warning.fallback()).append(")\n");
            }
            if (this.detailedWarnings.size() > limit) {
                builder.append("- ... ").append(this.detailedWarnings.size() - limit).append(" more warning(s) omitted by `polymerDebugMaxWarnings`\n");
            }
            builder.append('\n');
        }

        builder.append("## Fatal Conversion Errors\n\n");
        if (this.fatalErrors.isEmpty()) {
            builder.append("No fatal Polymer conversion errors were recorded.\n");
        } else {
            for (FatalError error : this.fatalErrors) {
                builder.append("### ").append(error.packId()).append('\n');
                appendRow(builder, "Exception", error.exceptionClass());
                appendRow(builder, "Message", error.message());
                builder.append("\nRelevant stack frames:\n\n");
                for (String frame : error.stackFrames()) {
                    builder.append("- `").append(frame).append("`\n");
                }
                builder.append('\n');
            }
        }

        return builder.toString();
    }

    private static void appendPack(StringBuilder builder, PackEntry entry) {
        builder.append("### ").append(entry.packId).append("\n\n");
        appendRow(builder, "Name", entry.packName);
        appendRow(builder, "Source path", entry.sourcePath);
        appendRow(builder, "SHA256", entry.sourceSha256);
        appendRow(builder, "Source size", bytes(entry.sourceSize));
        appendRow(builder, "Cache status", entry.cacheStatus);
        appendRow(builder, "Converted pack path", entry.convertedPackPath);
        appendRow(builder, "Converted pack size", bytes(entry.convertedPackSize));
        appendRow(builder, "Models", Integer.toString(entry.modelCount));
        appendRow(builder, "Textures", Integer.toString(entry.textureCount));
        appendRow(builder, "Item models", Integer.toString(entry.itemModelCount));
        appendRow(builder, "Direct conversion skipped", Boolean.toString(entry.directConversionSkipped));
        appendRow(builder, "Direct conversion skip reason", entry.directConversionSkipReason);
        appendRow(builder, "Skipped assets", Integer.toString(entry.skippedAssetCount));
        appendRow(builder, "Warnings", Integer.toString(entry.warningCount));
        builder.append("\nNamespace asset counts:\n\n");
        if (entry.namespaceAssetCounts.isEmpty()) {
            builder.append("- none recorded\n");
        } else {
            for (NamespaceAssetCounts counts : entry.namespaceAssetCounts.values()) {
                builder.append("- `").append(counts.namespace())
                        .append("`: itemModels=`").append(counts.itemModels())
                        .append("`, blockModels=`").append(counts.blockModels())
                        .append("`, textures=`").append(counts.textures())
                        .append("`, blockstates=`").append(counts.blockstates()).append("`\n");
            }
        }
        builder.append("\nSanitized inputs:\n\n");
        if (entry.sanitizedRoots.isEmpty()) {
            builder.append("- none recorded\n");
        } else {
            for (SanitizedRoot root : entry.sanitizedRoots) {
                builder.append("- source=`").append(root.sourcePath())
                        .append("`, sanitized=`").append(root.sanitizedPath())
                        .append("`, exists=`").append(root.exists())
                        .append("`, directory=`").append(root.directory())
                        .append("`, readable=`").append(root.readable())
                        .append("`, writable=`").append(root.writable())
                        .append("`, listingCount=`").append(root.listingCount())
                        .append("`, zipSource=`").append(root.zipSource())
                        .append("`, zipOpened=`").append(root.zipOpened())
                        .append("`, status=`").append(root.status()).append("`\n");
            }
        }
        builder.append("\nUnsupported summary:\n\n");
        appendRow(builder, "Custom model overrides", Integer.toString(entry.unsupportedCustomModelOverrides));
        appendRow(builder, "Multipart blockstates", Integer.toString(entry.unsupportedMultipartBlockstates));
        appendRow(builder, "Parent chain issues", Integer.toString(entry.parentChainIssues));
        appendRow(builder, "Missing textures", Integer.toString(entry.missingTextures));
        appendRow(builder, "Oversized textures skipped", Integer.toString(entry.skippedOversizedTextures));
        appendRow(builder, "Placeholder fallbacks used", Integer.toString(entry.placeholderFallbacks));
        appendRow(builder, "Item textures registered from Polymer pack", Integer.toString(entry.itemTexturesRegistered));
        appendRow(builder, "Placeholder item fallbacks used", Integer.toString(entry.placeholderItemFallbacks));
        appendRow(builder, "Placed-block fallbacks used", Integer.toString(entry.placedBlockFallbacks));
        appendRow(builder, "Nullable JSON defaults applied", Integer.toString(entry.nullableJsonDefaultsApplied));
        appendRow(builder, "Null-key texture entries skipped", Integer.toString(entry.nullKeyTextureEntriesSkipped));
        appendRow(builder, "Item texture resolution attempts", Integer.toString(entry.itemTextureResolutionAttempts));
        appendRow(builder, "Successful item texture resolutions", Integer.toString(entry.itemTextureResolutionSuccesses));
        appendRow(builder, "Placeholder item texture resolutions", Integer.toString(entry.itemTextureResolutionPlaceholders));
        appendRow(builder, "Generated Polymer block models inspected", Integer.toString(entry.polymerBlockModelsInspected));
        appendRow(builder, "Simple block mappings resolved", Integer.toString(entry.simpleBlockMappingsResolved));
        appendRow(builder, "Decorative block fallbacks used", Integer.toString(entry.decorativeBlockFallbacks));
        appendRow(builder, "Placeholder block fallbacks used", Integer.toString(entry.placeholderBlockFallbacks));
        appendRow(builder, "Invisible placement preventions", Integer.toString(entry.invisiblePlacementPreventions));
        builder.append("\nPlaceholder fallback reasons:\n\n");
        if (entry.itemTextureFallbackReasons.isEmpty()) {
            builder.append("- none recorded\n");
        } else {
            entry.itemTextureFallbackReasons.forEach((reason, count) -> builder.append("- `").append(reason).append("`: `").append(count).append("`\n"));
        }
        builder.append("\nBlock fallback reasons:\n\n");
        if (entry.blockFallbackReasons.isEmpty()) {
            builder.append("- none recorded\n");
        } else {
            entry.blockFallbackReasons.forEach((reason, count) -> builder.append("- `").append(reason).append("`: `").append(count).append("`\n"));
        }
        builder.append('\n');
    }

    private static void appendRow(StringBuilder builder, String key, String value) {
        builder.append("- ").append(key).append(": `").append(value == null || value.isBlank() ? "unknown" : value).append("`\n");
    }

    private PackEntry packEntry(String packId) {
        return this.packs.computeIfAbsent(packId, id -> {
            PackEntry entry = new PackEntry();
            entry.packId = id;
            return entry;
        });
    }

    private void addWarning(String packId, String filePath, String reason, String fallback) {
        this.detailedWarnings.add(new DetailedWarning(packId, filePath, reason, fallback));
    }

    private String modVersion(String modId) {
        ModInfo mod = this.hydraulic.mod(modId);
        return mod == null ? "unknown" : mod.version();
    }

    private String geyserVersion() {
        ModInfo fabric = this.hydraulic.mod("geyser-fabric");
        if (fabric != null) {
            return fabric.version();
        }

        ModInfo neoforge = this.hydraulic.mod("geyser-neoforge");
        return neoforge == null ? "unknown" : neoforge.version();
    }

    private static String packConverterVersion() {
        String version = PackConverter.class.getPackage().getImplementationVersion();
        return version == null || version.isBlank() ? "unknown" : version;
    }

    private static String minecraftVersion() {
        try {
            Class<?> sharedConstants = Class.forName("net.minecraft.SharedConstants");
            Method getCurrentVersion = sharedConstants.getMethod("getCurrentVersion");
            Object version = getCurrentVersion.invoke(null);
            Method getName = version.getClass().getMethod("getName");
            Object name = getName.invoke(version);
            return name == null ? "unknown" : name.toString();
        } catch (ReflectiveOperationException | RuntimeException ex) {
            return "unknown";
        }
    }

    private static Properties loadConfig(Path configPath) {
        Properties properties = new Properties();
        properties.setProperty("enablePolymerDebugReports", "true");
        properties.setProperty("polymerDebugMaxWarnings", Integer.toString(DEFAULT_MAX_WARNINGS));
        properties.setProperty("polymerDebugIncludeStacktrace", "false");

        try {
            if (Files.isRegularFile(configPath)) {
                try (InputStream inputStream = Files.newInputStream(configPath)) {
                    properties.load(inputStream);
                }
                return properties;
            }

            Files.createDirectories(configPath.getParent());
            try (OutputStream outputStream = Files.newOutputStream(configPath)) {
                properties.store(outputStream, "Hydraulic Polymer diagnostic report settings");
            }
        } catch (IOException | RuntimeException ex) {
            LOGGER.warn("Unable to load or create Polymer diagnostic config {}; using defaults", configPath, ex);
        }

        return properties;
    }

    private static int parsePositiveInt(String value, int fallback) {
        if (value == null) {
            return fallback;
        }

        try {
            int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : fallback;
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }

    private static List<String> stackFrames(Throwable throwable, boolean includeStacktrace) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int limit = includeStacktrace ? stackTrace.length : Math.min(8, stackTrace.length);
        List<String> frames = new ArrayList<>(limit);
        for (int i = 0; i < limit; i++) {
            frames.add(stackTrace[i].toString());
        }
        return frames;
    }

    private static String message(Throwable throwable) {
        String message = throwable.getMessage();
        return message == null || message.isBlank() ? "(no message)" : message;
    }

    private static String bytes(long bytes) {
        return bytes < 0L ? "unknown" : bytes + " bytes";
    }

    private static String safePath(Path path) {
        return path == null ? "unknown" : path.toAbsolutePath().normalize().toString();
    }

    private static final class PackEntry {
        private String packId = "unknown";
        private String packName = "unknown";
        private String sourcePath = "unknown";
        private String sourceSha256 = "unknown";
        private long sourceSize = -1L;
        private String cacheStatus = "unknown";
        private String convertedPackPath = "unknown";
        private long convertedPackSize = -1L;
        private boolean directConversionSkipped;
        private String directConversionSkipReason = "unknown";
        private int modelCount;
        private int textureCount;
        private int itemModelCount;
        private int skippedAssetCount;
        private int warningCount;
        private final Map<String, NamespaceAssetCounts> namespaceAssetCounts = new LinkedHashMap<>();
        private final List<SanitizedRoot> sanitizedRoots = new ArrayList<>();
        private int unsupportedCustomModelOverrides;
        private int unsupportedMultipartBlockstates;
        private int parentChainIssues;
        private int missingTextures;
        private int skippedOversizedTextures;
        private int placeholderFallbacks;
        private int itemTexturesRegistered;
        private int placeholderItemFallbacks;
        private int placedBlockFallbacks;
        private int nullableJsonDefaultsApplied;
        private int nullKeyTextureEntriesSkipped;
        private int itemTextureResolutionAttempts;
        private int itemTextureResolutionSuccesses;
        private int itemTextureResolutionPlaceholders;
        private final Map<String, Integer> itemTextureFallbackReasons = new LinkedHashMap<>();
        private int polymerBlockModelsInspected;
        private int simpleBlockMappingsResolved;
        private int decorativeBlockFallbacks;
        private int placeholderBlockFallbacks;
        private int invisiblePlacementPreventions;
        private final Map<String, Integer> blockFallbackReasons = new LinkedHashMap<>();
    }

    private record DetailedWarning(String packId, String filePath, String reason, String fallback) {
    }

    private record FatalError(String packId, String exceptionClass, String message, List<String> stackFrames) {
    }

    private record DiscoveryAttempt(String phase, long delayMillis, boolean found, List<String> foundPaths) {
    }

    private record SanitizedRoot(
            String sourcePath,
            String sanitizedPath,
            boolean exists,
            boolean directory,
            boolean readable,
            boolean writable,
            long listingCount,
            boolean zipSource,
            boolean zipOpened,
            String status
    ) {
    }

    private record NamespaceAssetCounts(String namespace, int itemModels, int blockModels, int textures, int blockstates) {
    }
}
