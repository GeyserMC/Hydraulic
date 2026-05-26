package org.geysermc.hydraulic.pack;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.geysermc.event.Event;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;
import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.hydraulic.pack.context.PackPreProcessContext;
import org.geysermc.hydraulic.pack.converter.CustomModelConverter;
import org.geysermc.hydraulic.pack.modules.MetadataPackModule;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.pack.converter.PackConverter;
import org.geysermc.pack.converter.pipeline.AssetConverters;
import org.geysermc.pack.converter.pipeline.ConverterPipeline;
import org.geysermc.pack.converter.type.model.ModelStitcher;
import org.geysermc.pack.converter.type.texture.TextureConverter;
import org.geysermc.pack.converter.util.NioDirectoryFileTreeReader;
import org.geysermc.pack.converter.util.VanillaPackProvider;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackReader;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Manages packs within Hydraulic. Most of the pack conversion
 * management is done within this class, and it is also responsible
 * for loading the packs onto the server.
 */
public class PackManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int MAX_POLYMER_TEXTURE_DIMENSION = 8192;
    private static final long POLYMER_DELAYED_DISCOVERY_MILLIS = 10_000L;
    private static final String POLYMER_CACHE_METADATA = "polymer-conversion-cache.json";
    private static final Set<String> POLYMER_BACKED_DIRECT_CONVERSION_SKIP_MODS = Set.of(
            "polydecorations"
    );

    static final Set<String> IGNORED_MODS = Set.of(
            // Fabric
            "geyser-fabric",
            "fabric-permissions-api-v0",

            // NeoForge
            "geyser-neoforge",
            "neoforge",
            "minecraft",

            // Common
            "floodgate",
            "mixinextras",
            "cloud"
    );

    private final HydraulicImpl hydraulic;
    private final Path vanillaPath;
    private final List<PackModule<?>> modules = new ArrayList<>();

    private final ListMultimap<String, ModInfo> namespacesToMods = MultimapBuilder.hashKeys().arrayListValues(1).build();
    private final ListMultimap<String, Identifier> modsToBlocks = MultimapBuilder.hashKeys().arrayListValues().build();
    private final ListMultimap<String, Identifier> modsToItems = MultimapBuilder.hashKeys().arrayListValues().build();

    private List<ConverterPipeline<?, ?>> packConverters;
    private List<PolymerResourcePackDiscovery.DiscoveredPack> generatedResourcePacks = List.of();
    private Map<String, PolymerResourcePackDiscovery.DiscoveredPack> generatedResourcePackLookup = Map.of();
    private final Map<String, PolymerGeneratedItemTexture> generatedPolymerItemTextures = new LinkedHashMap<>();
    private ModelStitcher.Provider modelProvider;
    private PolymerDiagnosticReport polymerDiagnosticReport;
    private boolean polymerInstalled;
    private boolean delayedPolymerDiscoveryAttempted;
    private boolean generatedPolymerItemTexturesScanned;

    public PackManager(HydraulicImpl hydraulic) {
        this.hydraulic = hydraulic;
        this.vanillaPath = hydraulic.dataFolder(Constants.MOD_ID).resolve("cache/vanilla-assets.zip");
    }

    /**
     * Initializes the pack manager.
     */
    public void initialize() {
        final Collection<ModInfo> loadedMods = this.hydraulic.mods();
        this.polymerDiagnosticReport = PolymerDiagnosticReport.create(this.hydraulic);
        this.polymerInstalled = PolymerResourcePackDiscovery.isPolymerInstalled(loadedMods);
        this.updateGeneratedResourcePacks(PolymerResourcePackDiscovery.discover(
                this.hydraulic,
                loadedMods,
                this.polymerDiagnosticReport,
                "initial",
                0L
        ));
        initializeModLookups();
        final List<ModInfo> mods = new ArrayList<>(loadedMods.size() + this.generatedResourcePacks.size());
        mods.addAll(loadedMods);
        this.generatedResourcePacks.stream().map(PolymerResourcePackDiscovery.DiscoveredPack::mod).forEach(mods::add);

        final Map<String, List<ResourcePack>> modPacks = readModPacks(mods);

        try {
            Files.createDirectories(this.getVanillaPath().getParent());
        } catch (IOException e) {
            LOGGER.error("Failed to create cache dir");
        }

        VanillaPackProvider.create(
                this.getVanillaPath(),
                new PackLogListener(LOGGER)
        );

        modelProvider = createModelProvider(mods, modPacks, this.getVanillaPath());

        this.packConverters = new ArrayList<>(AssetConverters.converters(hydraulic.isDev()));
        this.packConverters.remove(AssetConverters.MODEL);
        this.packConverters.remove(AssetConverters.MANIFEST);
        this.packConverters.add(AssetConverters.create(
                new CustomModelConverter(modelProvider),
                AssetConverters.MODEL,
                AssetConverters.MODEL
        ));

        for (PackModule<?> module : ServiceLoader.load(PackModule.class)) {
            this.modules.add(module);

            GeyserApi.api().eventBus().register(this.hydraulic, module);
            module.eventListeners().forEach((eventClass, listeners) -> {
                GeyserApi.api().eventBus().subscribe(this.hydraulic, eventClass, this::callEvents);
            });

            for (ModInfo mod : mods) {
                if (IGNORED_MODS.contains(mod.id())) {
                    continue;
                }

                if (module.hasPreProcessors()) {
                    try {
                        module.preProcess0(new PackPreProcessContext(this.hydraulic, mod, module, modPacks.get(mod.id()), modelProvider));
                    } catch (Throwable t) {
                        LOGGER.error("Failed to pre-process mod {} for module {}", mod.id(), module.getClass().getSimpleName(), t);
                    }
                }
            }
        }

        GeyserApi.api().eventBus().register(this.hydraulic, new PackListener(this.hydraulic, this));
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
        boolean polymerPack = isPolymerGeneratedPack(mod);
        List<PolymerInputRoot> polymerInputRoots = List.of();
        if (polymerPack) {
            polymerInputRoots = preparePolymerInputRoots(mod, packPath);
            logPolymerSourceSummary(mod);
            if (polymerInputRoots.isEmpty()) {
                return false;
            }
        }

        Map<Path, Boolean> processableTextureRoots = new LinkedHashMap<>();
        for (Path root : polymerPack ? polymerInputRoots.stream().map(PolymerInputRoot::path).toList() : mod.roots()) {
            processableTextureRoots.put(root, hasProcessableTextures(root));
        }

        if (processableTextureRoots.values().stream().noneMatch(Boolean::booleanValue)) {
            LOGGER.debug("[Hydraulic] Skipping texture phase for asset-free mod: {}", mod.id());
        }

        PackConverter converter = null;
        try {
            for (var entry : processableTextureRoots.entrySet()) {
                Path root = entry.getKey();
                boolean processTextures = entry.getValue();
                if (!processTextures) {
                    LOGGER.debug("Skipping texture phase for mod {} root {} because no processable texture assets were found", mod.id(), root);
                }

                converter = createConverter(mod, packPath, processTextures);
                converter.input(root, false).convert();
            }
        } catch (IOException | RuntimeException ex) {
            LOGGER.error("Failed to convert mod {} to pack", mod.id(), ex);
            if (polymerPack && this.polymerDiagnosticReport != null) {
                this.polymerDiagnosticReport.recordFatalError(mod, ex);
            }
            return false;
        }

        // Now export the pack
        boolean exportSucceeded = true;
        try {
            if (converter != null) {
                converter.pack();
            }
        } catch (IOException ex) {
            LOGGER.error("Failed to export pack for mod {}", mod.id(), ex);
            if (polymerPack && this.polymerDiagnosticReport != null) {
                this.polymerDiagnosticReport.recordFatalError(mod, ex);
            }
            exportSucceeded = false;
        }

        if (polymerPack) {
            int skippedAssets = polymerInputRoots.stream().mapToInt(root -> root.summary().skippedAssets()).sum();
            int warnings = polymerInputRoots.stream().mapToInt(root -> root.summary().warnings()).sum();
            PolymerResourcePackDiscovery.DiscoveredPack pack = this.generatedResourcePackLookup.get(mod.id());
            long convertedSize = fileSize(packPath);
            LOGGER.info(
                    "Polymer conversion summary for {}: source={}, sourceSize={} bytes, convertedSize={} bytes, models={}, textures={}, itemModels={}, skippedAssets={}, warnings={}",
                    mod.id(),
                    pack == null ? mod.roots() : pack.source(),
                    pack == null ? -1L : pack.sourceSize(),
                    convertedSize,
                    pack == null ? -1 : pack.counts().models(),
                    pack == null ? -1 : pack.counts().textures(),
                    pack == null ? -1 : pack.counts().items(),
                    skippedAssets,
                    warnings
            );
            this.polymerDiagnosticReport.recordConversionResult(mod, packPath, convertedSize, skippedAssets, warnings);
        }

        return (!polymerPack || exportSucceeded) && Files.exists(packPath);
    }

    /**
     * Gets all pack conversion targets, including generated resource packs discovered outside mod roots.
     *
     * @return all pack conversion targets
     */
    public Collection<ModInfo> conversionTargets() {
        if (this.generatedResourcePacks.isEmpty()) {
            return this.hydraulic.mods();
        }

        List<ModInfo> mods = new ArrayList<>(this.hydraulic.mods().size() + this.generatedResourcePacks.size());
        mods.addAll(this.hydraulic.mods());
        this.generatedResourcePacks.stream().map(PolymerResourcePackDiscovery.DiscoveredPack::mod).forEach(mods::add);
        return mods;
    }

    public boolean hasPolymerInstalled() {
        return this.polymerInstalled;
    }

    public boolean hasGeneratedPolymerPacks() {
        return !this.generatedResourcePacks.isEmpty();
    }

    public boolean shouldSkipDirectConversionForGeneratedPolymerPack(@NotNull ModInfo mod) {
        return this.hasGeneratedPolymerPacks() && POLYMER_BACKED_DIRECT_CONVERSION_SKIP_MODS.contains(mod.id());
    }

    public synchronized String generatedPolymerItemTexturePath(@NotNull Identifier itemId) {
        this.ensureGeneratedPolymerItemTexturesScanned();
        PolymerGeneratedItemTexture texture = this.generatedPolymerItemTextures.get(itemId.toString());
        return texture == null ? null : texture.texturePath();
    }

    public synchronized int generatedPolymerItemTextureCount() {
        this.ensureGeneratedPolymerItemTexturesScanned();
        return this.generatedPolymerItemTextures.size();
    }

    public synchronized List<String> generatedPolymerItemTextureExamples(int limit) {
        this.ensureGeneratedPolymerItemTexturesScanned();
        return this.generatedPolymerItemTextures.entrySet().stream()
                .limit(limit)
                .map(entry -> entry.getKey() + " -> texture=" + entry.getValue().texturePath())
                .toList();
    }

    public synchronized boolean refreshPolymerResourcePacks(@NotNull String phase, long delayMillis) {
        List<PolymerResourcePackDiscovery.DiscoveredPack> discoveredPacks = PolymerResourcePackDiscovery.discover(
                this.hydraulic,
                this.hydraulic.mods(),
                this.polymerDiagnosticReport,
                phase,
                delayMillis
        );
        if (discoveredPacks.isEmpty()) {
            return false;
        }

        if (sameGeneratedResourcePacks(discoveredPacks)) {
            return true;
        }

        this.updateGeneratedResourcePacks(discoveredPacks);
        this.initializeModLookups();
        this.rebuildModelProviderForCurrentPacks();
        return true;
    }

    public synchronized boolean delayedPolymerDiscoveryIfNeeded() {
        if (!this.polymerInstalled || this.hasGeneratedPolymerPacks() || this.delayedPolymerDiscoveryAttempted) {
            return false;
        }

        this.delayedPolymerDiscoveryAttempted = true;
        LOGGER.info("Polymer pack not found during initial scan; scheduling delayed Polymer pack discovery in {} ms", POLYMER_DELAYED_DISCOVERY_MILLIS);
        try {
            Thread.sleep(POLYMER_DELAYED_DISCOVERY_MILLIS);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            LOGGER.warn("Delayed Polymer pack discovery was interrupted");
            return false;
        }

        boolean found = this.refreshPolymerResourcePacks("delayed", POLYMER_DELAYED_DISCOVERY_MILLIS);
        if (found) {
            this.generatedResourcePacks.forEach(pack -> LOGGER.info("Delayed Polymer discovery found {}", pack.source()));
        } else {
            LOGGER.info("Delayed Polymer discovery did not find a generated resource pack");
        }
        return found;
    }

    private PackConverter createConverter(@NotNull ModInfo mod, @NotNull Path packPath, boolean processTextures) {
        List<ConverterPipeline<?, ?>> pipelines = packConverters.stream()
                .filter(pipeline -> processTextures || pipeline != AssetConverters.TEXTURE)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        pipelines.add(AssetConverters.create(new MetadataPackModule(mod)));

        PackConverter converter = new PackConverter()
                .packName(mod.name())
                .logListener(new PackLogListener(LoggerFactory.getLogger(LOGGER.getName() + "/" + mod.id())))
                .converters(pipelines)
                .output(packPath)
                .vanillaPackPath(vanillaPath)
                .textureSubdirectory(mod.namespace())
                .packageHandler(new PackPackager());

        converter.postProcessor((javaPack, bedrockPack) -> {
            for (PackModule<?> module : this.modules) {
                PackPostProcessContext context = new PackPostProcessContext(this.hydraulic, mod, module, converter, javaPack, bedrockPack, packPath, modelProvider);
                if (!module.test(context)) {
                    continue;
                }

                module.postProcess0(context);
            }
        });

        return converter;
    }

    PolymerResourcePackDiscovery.DiscoveredPack polymerPackInfo(@NotNull ModInfo mod) {
        return this.generatedResourcePackLookup.get(mod.id());
    }

    public PolymerDiagnosticReport polymerDiagnosticReport() {
        return this.polymerDiagnosticReport;
    }

    private void updateGeneratedResourcePacks(@NotNull List<PolymerResourcePackDiscovery.DiscoveredPack> discoveredPacks) {
        this.generatedResourcePacks = List.copyOf(discoveredPacks);
        Map<String, PolymerResourcePackDiscovery.DiscoveredPack> generatedPackLookup = new HashMap<>();
        this.generatedResourcePacks.forEach(pack -> generatedPackLookup.put(pack.mod().id(), pack));
        this.generatedResourcePackLookup = Map.copyOf(generatedPackLookup);
        this.generatedPolymerItemTextures.clear();
        this.generatedPolymerItemTexturesScanned = false;
    }

    private boolean sameGeneratedResourcePacks(@NotNull List<PolymerResourcePackDiscovery.DiscoveredPack> discoveredPacks) {
        if (this.generatedResourcePacks.size() != discoveredPacks.size()) {
            return false;
        }

        for (int i = 0; i < this.generatedResourcePacks.size(); i++) {
            PolymerResourcePackDiscovery.DiscoveredPack current = this.generatedResourcePacks.get(i);
            PolymerResourcePackDiscovery.DiscoveredPack discovered = discoveredPacks.get(i);
            if (!current.source().equals(discovered.source()) || !current.sha256().equals(discovered.sha256())) {
                return false;
            }
        }
        return true;
    }

    private void rebuildModelProviderForCurrentPacks() {
        try {
            Collection<ModInfo> mods = this.conversionTargets();
            this.modelProvider = createModelProvider(mods, readModPacks(mods), this.getVanillaPath());
        } catch (RuntimeException ex) {
            LOGGER.warn("Unable to rebuild model provider after delayed Polymer discovery; conversion will use the existing provider", ex);
        }
    }

    private static Map<String, List<ResourcePack>> readModPacks(Collection<ModInfo> mods) {
        final Map<String, List<ResourcePack>> modPacks = Maps.newHashMapWithExpectedSize(mods.size());
        for (final ModInfo mod : mods) {
            List<ResourcePack> packs = new ArrayList<>();
            for (Path path : mod.roots()) {
                try {
                    packs.add(readResourcePack(path));
                } catch (RuntimeException ex) {
                    LOGGER.warn("Skipping resource pack root {} for mod {} because its metadata could not be read", path, mod.id(), ex);
                }
            }
            modPacks.put(mod.id(), List.copyOf(packs));
        }
        return modPacks;
    }

    boolean isPolymerCacheCurrent(@NotNull ModInfo mod, @NotNull Path packPath) {
        PolymerResourcePackDiscovery.DiscoveredPack pack = polymerPackInfo(mod);
        if (pack == null || Files.notExists(packPath)) {
            return false;
        }

        PolymerCacheMetadata metadata = readPolymerCacheMetadata(packPath);
        return metadata != null
                && pack.sha256().equals(metadata.sourceSha256)
                && polymerConverterVersion().equals(metadata.converterVersion);
    }

    boolean hasPolymerCache(@NotNull Path packPath) {
        return Files.isRegularFile(packPath) && readPolymerCacheMetadata(packPath) != null;
    }

    void writePolymerCacheMetadata(@NotNull ModInfo mod, @NotNull Path packPath) {
        PolymerResourcePackDiscovery.DiscoveredPack pack = polymerPackInfo(mod);
        if (pack == null) {
            return;
        }

        PolymerCacheMetadata metadata = new PolymerCacheMetadata();
        metadata.sourcePath = pack.source().toString();
        metadata.sourceSha256 = pack.sha256();
        metadata.sourceSize = pack.sourceSize();
        metadata.converterVersion = polymerConverterVersion();
        metadata.convertedSize = fileSize(packPath);
        metadata.models = pack.counts().models();
        metadata.textures = pack.counts().textures();
        metadata.itemModels = pack.counts().items();

        Path metadataPath = polymerCacheMetadataPath(packPath);
        try {
            Files.createDirectories(metadataPath.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(metadataPath)) {
                Constants.GSON.toJson(metadata, writer);
            }
        } catch (IOException ex) {
            LOGGER.warn("Unable to write Polymer conversion cache metadata for {}", mod.id(), ex);
        }
    }

    String polymerConverterVersion() {
        String hydraulicVersion = this.hydraulic.mod(Constants.MOD_ID).version();
        String converterVersion = PackConverter.class.getPackage().getImplementationVersion();
        if (converterVersion == null || converterVersion.isBlank()) {
            converterVersion = "unknown";
        }
        return Constants.MOD_NAME + "/" + hydraulicVersion + " pack-converter/" + converterVersion;
    }

    private PolymerCacheMetadata readPolymerCacheMetadata(@NotNull Path packPath) {
        Path metadataPath = polymerCacheMetadataPath(packPath);
        if (Files.notExists(metadataPath)) {
            return null;
        }

        try (BufferedReader reader = Files.newBufferedReader(metadataPath)) {
            return Constants.GSON.fromJson(reader, PolymerCacheMetadata.class);
        } catch (IOException | RuntimeException ex) {
            LOGGER.debug("Unable to read Polymer conversion cache metadata {}", metadataPath, ex);
            return null;
        }
    }

    private Path polymerCacheMetadataPath(Path packPath) {
        return packPath.getParent().resolve(POLYMER_CACHE_METADATA);
    }

    private List<PolymerInputRoot> preparePolymerInputRoots(@NotNull ModInfo mod, @NotNull Path packPath) {
        List<PolymerInputRoot> roots = new ArrayList<>();
        int index = 0;
        for (Path root : mod.roots()) {
            Path sanitizedRoot = packPath.getParent().resolve("polymer-sanitized-" + index);
            try {
                PolymerInputRoot inputRoot = sanitizePolymerRoot(mod, root, sanitizedRoot);
                if (isUsablePolymerInputRoot(mod, root, inputRoot)) {
                    roots.add(inputRoot);
                }
            } catch (IOException | RuntimeException ex) {
                LOGGER.warn("Skipping Polymer resource pack root {} for {} because it could not be sanitized safely", root, mod.id(), ex);
                if (this.polymerDiagnosticReport != null) {
                    this.polymerDiagnosticReport.recordSanitizedRoot(
                            mod.id(),
                            root,
                            sanitizedRoot,
                            Files.exists(sanitizedRoot),
                            Files.isDirectory(sanitizedRoot),
                            Files.isReadable(sanitizedRoot),
                            Files.isWritable(sanitizedRoot),
                            directoryListingCount(sanitizedRoot),
                            Files.isRegularFile(root) && isArchive(root),
                            false,
                            "sanitization failed: " + ex.getClass().getSimpleName()
                    );
                }
            }
            index++;
        }

        if (roots.isEmpty()) {
            LOGGER.warn("No safe Polymer resource pack roots remain for {}; conversion will be skipped unless a last known good cache is available", mod.id());
        }
        return roots;
    }

    private PolymerInputRoot sanitizePolymerRoot(@NotNull ModInfo mod, @NotNull Path sourceRoot, @NotNull Path sanitizedRoot) throws IOException {
        PolymerSanitizeSummary summary = new PolymerSanitizeSummary();

        if (Files.exists(sanitizedRoot)) {
            deleteDirectory(sanitizedRoot);
        }
        Files.createDirectories(sanitizedRoot);

        boolean archive = Files.isRegularFile(sourceRoot) && isArchive(sourceRoot);
        boolean zipOpened = false;
        if (archive) {
            try (ZipFile zipFile = new ZipFile(sourceRoot.toFile())) {
                zipOpened = true;
                copySanitizedArchive(mod, zipFile, sanitizedRoot, summary, this.polymerDiagnosticReport);
            }
        } else {
            copySanitizedTree(mod, sourceRoot, sanitizedRoot, summary, this.polymerDiagnosticReport);
        }

        LOGGER.info(
                "Polymer texture guard for {} copied {} asset(s), skipped {} oversized/invalid texture(s), warnings={}",
                mod.id(),
                summary.copiedAssets(),
                summary.skippedTextures(),
                summary.warnings()
        );
        if (this.polymerDiagnosticReport != null) {
            this.polymerDiagnosticReport.recordSanitizedRoot(
                    mod.id(),
                    sourceRoot,
                    sanitizedRoot,
                    Files.exists(sanitizedRoot),
                    Files.isDirectory(sanitizedRoot),
                    Files.isReadable(sanitizedRoot),
                    Files.isWritable(sanitizedRoot),
                    directoryListingCount(sanitizedRoot),
                    archive,
                    zipOpened,
                    Files.isDirectory(sanitizedRoot) ? "ready" : "sanitized root is not a directory"
            );
        }
        return new PolymerInputRoot(sanitizedRoot, summary);
    }

    private boolean isUsablePolymerInputRoot(@NotNull ModInfo mod, @NotNull Path sourceRoot, @NotNull PolymerInputRoot inputRoot) {
        Path path = inputRoot.path();
        if (Files.isDirectory(path) && Files.isReadable(path)) {
            return true;
        }

        LOGGER.warn(
                "Skipping sanitized Polymer root for {} from {} because {} is not a readable directory (exists={}, directory={}, readable={}, writable={}, entries={})",
                mod.id(),
                sourceRoot,
                path,
                Files.exists(path),
                Files.isDirectory(path),
                Files.isReadable(path),
                Files.isWritable(path),
                directoryListingCount(path)
        );
        if (this.polymerDiagnosticReport != null) {
            this.polymerDiagnosticReport.recordWarning(
                    mod.id(),
                    path.toString(),
                    "sanitized root is not a readable directory",
                    "skipped sanitized root"
            );
        }
        return false;
    }

    private static void copySanitizedTree(
            @NotNull ModInfo mod,
            @NotNull Path sourceRoot,
            @NotNull Path targetRoot,
            @NotNull PolymerSanitizeSummary summary,
            PolymerDiagnosticReport diagnosticReport
    ) throws IOException {
        try (Stream<Path> stream = Files.walk(sourceRoot)) {
            for (Path source : stream.filter(Files::isRegularFile).toList()) {
                Path relativePath = sourceRoot.relativize(source);
                String relative = normalizePath(relativePath);
                Path target = safeSanitizedTarget(targetRoot, relative);
                if (target == null) {
                    LOGGER.warn("Skipping unsafe Polymer asset for pack {} at {}: empty or unsafe path", mod.id(), relative.isBlank() ? "(empty path)" : relative);
                    if (diagnosticReport != null) {
                        diagnosticReport.recordWarning(mod.id(), relative.isBlank() ? "(empty path)" : relative, "empty or unsafe path in Polymer pack", "skipped asset");
                    }
                    summary.skippedAssets++;
                    summary.warnings++;
                    continue;
                }

                TextureValidation validation = validatePolymerTexture(source, relative);
                if (!validation.valid()) {
                    LOGGER.warn(
                            "Skipping Polymer texture for pack {} at {} ({}x{}): {}",
                            mod.id(),
                            relative,
                            validation.width(),
                            validation.height(),
                            validation.reason()
                    );
                    if (diagnosticReport != null) {
                        diagnosticReport.recordTextureSkipped(mod.id(), relative, validation.width(), validation.height(), validation.reason());
                    }
                    summary.skippedAssets++;
                    summary.skippedTextures++;
                    summary.warnings++;
                    continue;
                }

                Files.createDirectories(target.getParent());
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                summary.copiedAssets++;
            }
        }
    }

    private static void copySanitizedArchive(
            @NotNull ModInfo mod,
            @NotNull ZipFile zipFile,
            @NotNull Path targetRoot,
            @NotNull PolymerSanitizeSummary summary,
            PolymerDiagnosticReport diagnosticReport
    ) throws IOException {
        for (ZipEntry entry : zipFile.stream().toList()) {
            if (entry.isDirectory()) {
                continue;
            }

            String relative = entry.getName().replace('\\', '/');
            Path target = safeSanitizedTarget(targetRoot, relative);
            if (target == null) {
                LOGGER.warn("Skipping unsafe Polymer asset for pack {} at {}: empty or unsafe path", mod.id(), relative.isBlank() ? "(empty path)" : relative);
                if (diagnosticReport != null) {
                    diagnosticReport.recordWarning(mod.id(), relative.isBlank() ? "(empty path)" : relative, "empty or unsafe path in Polymer pack", "skipped asset");
                }
                summary.skippedAssets++;
                summary.warnings++;
                continue;
            }

            TextureValidation validation;
            try (InputStream inputStream = zipFile.getInputStream(entry)) {
                validation = validatePolymerTexture(inputStream, relative);
            }
            if (!validation.valid()) {
                LOGGER.warn(
                        "Skipping Polymer texture for pack {} at {} ({}x{}): {}",
                        mod.id(),
                        relative,
                        validation.width(),
                        validation.height(),
                        validation.reason()
                );
                if (diagnosticReport != null) {
                    diagnosticReport.recordTextureSkipped(mod.id(), relative, validation.width(), validation.height(), validation.reason());
                }
                summary.skippedAssets++;
                summary.skippedTextures++;
                summary.warnings++;
                continue;
            }

            Files.createDirectories(target.getParent());
            try (InputStream inputStream = zipFile.getInputStream(entry)) {
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            }
            summary.copiedAssets++;
        }
    }

    private static TextureValidation validatePolymerTexture(@NotNull Path path, @NotNull String relativePath) {
        if (!isTexturePath(relativePath)) {
            return TextureValidation.VALID;
        }

        try (InputStream inputStream = Files.newInputStream(path)) {
            return validatePolymerTexture(inputStream, relativePath);
        } catch (IOException | RuntimeException ex) {
            return new TextureValidation(false, -1, -1, "texture dimensions could not be read: " + ex.getMessage());
        }
    }

    private static TextureValidation validatePolymerTexture(@NotNull InputStream inputStream, @NotNull String relativePath) {
        if (!isTexturePath(relativePath)) {
            return TextureValidation.VALID;
        }

        try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(inputStream)) {
            if (imageInputStream == null) {
                return new TextureValidation(false, -1, -1, "ImageIO could not open the texture stream");
            }

            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInputStream);
            if (!readers.hasNext()) {
                return new TextureValidation(false, -1, -1, "no ImageIO reader is available for this texture");
            }

            ImageReader reader = readers.next();
            try {
                reader.setInput(imageInputStream, true, true);
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                if (width <= 0 || height <= 0) {
                    return new TextureValidation(false, width, height, "texture dimensions must be positive");
                }
                if (width > MAX_POLYMER_TEXTURE_DIMENSION || height > MAX_POLYMER_TEXTURE_DIMENSION) {
                    return new TextureValidation(false, width, height, "texture exceeds " + MAX_POLYMER_TEXTURE_DIMENSION + "px safety limit");
                }
                return TextureValidation.VALID;
            } finally {
                reader.dispose();
            }
        } catch (IOException | RuntimeException ex) {
            return new TextureValidation(false, -1, -1, "texture dimensions could not be read: " + ex.getMessage());
        }
    }

    private static boolean isTexturePath(String relativePath) {
        String normalized = relativePath.toLowerCase(Locale.ROOT);
        return normalized.startsWith("assets/") && normalized.contains("/textures/") && normalized.endsWith(".png");
    }

    static boolean isPolymerGeneratedPack(@NotNull ModInfo mod) {
        return mod.id().startsWith("polymer_resourcepack");
    }

    private void logPolymerSourceSummary(@NotNull ModInfo mod) {
        PolymerResourcePackDiscovery.DiscoveredPack pack = polymerPackInfo(mod);
        if (pack == null) {
            return;
        }

        LOGGER.info(
                "Polymer source summary for {}: path={}, size={} bytes, models={}, textures={}, itemModels={}, sha256={}",
                mod.id(),
                pack.source(),
                pack.sourceSize(),
                pack.counts().models(),
                pack.counts().textures(),
                pack.counts().items(),
                pack.sha256()
        );
    }

    private static long fileSize(Path path) {
        try {
            if (Files.isRegularFile(path)) {
                return Files.size(path);
            }
        } catch (IOException ex) {
            LOGGER.debug("Unable to read file size for {}", path, ex);
        }
        return -1L;
    }

    private static void deleteDirectory(Path directory) throws IOException {
        try (Stream<Path> stream = Files.walk(directory)) {
            for (Path path : stream.sorted((left, right) -> right.compareTo(left)).toList()) {
                Files.deleteIfExists(path);
            }
        }
    }

    private static String normalizePath(Path path) {
        return path.toString().replace('\\', '/');
    }

    private static boolean hasProcessableTextures(Path root) {
        if (root == null || Files.notExists(root)) {
            return false;
        }

        if (Files.isRegularFile(root) && isArchive(root)) {
            try (FileSystem fileSystem = FileSystems.newFileSystem(root)) {
                return hasProcessableTextures(fileSystem.getPath("/"));
            } catch (IOException | RuntimeException ex) {
                LOGGER.debug("Unable to inspect archive root {} for texture assets", root, ex);
                return false;
            }
        }

        Path assets = root.resolve("assets");
        if (!Files.isDirectory(assets)) {
            return false;
        }

        try (Stream<Path> namespaces = Files.list(assets)) {
            return namespaces
                    .filter(Files::isDirectory)
                    .map(namespace -> namespace.resolve("textures"))
                    .filter(Files::isDirectory)
                    .anyMatch(PackManager::hasProcessableTextureFile);
        } catch (IOException | RuntimeException ex) {
            LOGGER.debug("Unable to inspect root {} for texture assets", root, ex);
            return false;
        }
    }

    private static ResourcePack readResourcePack(Path path) {
        if (Files.isRegularFile(path) && isArchive(path)) {
            return MinecraftResourcePackReader.minecraft().readFromZipFile(path);
        }
        return MinecraftResourcePackReader.minecraft().read(NioDirectoryFileTreeReader.read(path));
    }

    private static boolean hasProcessableTextureFile(Path texturesDirectory) {
        try (Stream<Path> files = Files.walk(texturesDirectory)) {
            return files
                    .filter(Files::isRegularFile)
                    .anyMatch(PackManager::isProcessableTexture);
        } catch (IOException | RuntimeException ex) {
            LOGGER.debug("Unable to inspect texture directory {}", texturesDirectory, ex);
            return false;
        }
    }

    private static boolean isProcessableTexture(Path path) {
        String fileName = path.getFileName().toString().toLowerCase(Locale.ROOT);
        return fileName.endsWith(".png");
    }

    private static boolean isArchive(Path path) {
        Path fileNamePath = path.getFileName();
        if (fileNamePath == null) {
            return false;
        }

        String fileName = fileNamePath.toString().toLowerCase(Locale.ROOT);
        return fileName.endsWith(".jar") || fileName.endsWith(".zip");
    }

    private static Path safeSanitizedTarget(@NotNull Path targetRoot, @NotNull String relative) {
        if (relative.isBlank() || relative.equals(".")) {
            return null;
        }

        Path normalizedRoot = targetRoot.toAbsolutePath().normalize();
        Path target = normalizedRoot.resolve(relative).normalize();
        if (!target.startsWith(normalizedRoot) || target.equals(normalizedRoot)) {
            return null;
        }

        return target;
    }

    private static long directoryListingCount(@NotNull Path path) {
        if (!Files.isDirectory(path)) {
            return -1L;
        }

        try (Stream<Path> stream = Files.list(path)) {
            return stream.count();
        } catch (IOException | RuntimeException ex) {
            LOGGER.debug("Unable to list sanitized Polymer directory {}", path, ex);
            return -1L;
        }
    }

    private boolean hasGeneratedPolymerModelForBlock(@NotNull Identifier block) {
        return this.generatedPolymerAssetExists("assets/" + block.getNamespace() + "/blockstates/" + block.getPath() + ".json")
                || this.generatedPolymerAssetExists("assets/" + block.getNamespace() + "/models/block/" + block.getPath() + ".json");
    }

    private boolean generatedPolymerAssetExists(@NotNull String path) {
        for (PolymerResourcePackDiscovery.DiscoveredPack pack : this.generatedResourcePacks) {
            Path source = pack.source();
            if (Files.isDirectory(source) && Files.isRegularFile(source.resolve(path))) {
                return true;
            }

            if (Files.isRegularFile(source) && isArchive(source)) {
                try (ZipFile zipFile = new ZipFile(source.toFile())) {
                    if (zipFile.getEntry(path) != null) {
                        return true;
                    }
                } catch (IOException | RuntimeException ex) {
                    LOGGER.debug("Unable to inspect generated Polymer pack {} for {}", source, path, ex);
                }
            }
        }
        return false;
    }

    private synchronized void ensureGeneratedPolymerItemTexturesScanned() {
        if (this.generatedPolymerItemTexturesScanned) {
            return;
        }

        this.generatedPolymerItemTextures.clear();
        for (PolymerResourcePackDiscovery.DiscoveredPack pack : this.generatedResourcePacks) {
            this.indexConvertedPolymerItemTextures(pack);
            this.indexSourcePolymerItemTextures(pack);
        }
        this.generatedPolymerItemTexturesScanned = true;

        if (!this.generatedPolymerItemTextures.isEmpty()) {
            LOGGER.info(
                    "Indexed {} generated Polymer item texture mapping(s); examples={}",
                    this.generatedPolymerItemTextures.size(),
                    this.generatedPolymerItemTextureExamples(3)
            );
        }
    }

    private void indexConvertedPolymerItemTextures(@NotNull PolymerResourcePackDiscovery.DiscoveredPack pack) {
        Path packPath = this.hydraulic.modStorage(pack.mod()).pack();
        if (!Files.isRegularFile(packPath)) {
            return;
        }

        try (GeneratedPolymerPackReader reader = new GeneratedPolymerPackReader(packPath)) {
            Optional<JsonObject> itemTexture = reader.readJsonObject("textures/item_texture.json");
            if (itemTexture.isEmpty()) {
                return;
            }

            JsonElement textureDataElement = itemTexture.get().get("texture_data");
            if (textureDataElement == null || !textureDataElement.isJsonObject()) {
                return;
            }

            for (Map.Entry<String, JsonElement> entry : textureDataElement.getAsJsonObject().entrySet()) {
                String itemId = entry.getKey();
                String texturePath = readBedrockTexturePath(entry.getValue());
                if (!isGeneratedItemId(itemId) || texturePath == null || texturePath.isBlank()) {
                    continue;
                }

                this.generatedPolymerItemTextures.putIfAbsent(
                        itemId,
                        new PolymerGeneratedItemTexture(texturePath, "converted-cache:" + packPath)
                );
            }
        } catch (IOException | RuntimeException ex) {
            LOGGER.debug("Unable to inspect converted Polymer item textures at {}", packPath, ex);
        }
    }

    private void indexSourcePolymerItemTextures(@NotNull PolymerResourcePackDiscovery.DiscoveredPack pack) {
        try (GeneratedPolymerPackReader reader = new GeneratedPolymerPackReader(pack.source())) {
            for (String modelPath : reader.itemModelPaths()) {
                String itemId = itemIdFromModelPath(modelPath);
                if (itemId == null || this.generatedPolymerItemTextures.containsKey(itemId)) {
                    continue;
                }

                TextureReference texture = resolveGeneratedPolymerModelTexture(reader, modelPath);
                if (texture == null) {
                    continue;
                }

                String texturePath = generatedPolymerOutputTexturePath(pack.mod(), texture.value());
                this.generatedPolymerItemTextures.put(
                        itemId,
                        new PolymerGeneratedItemTexture(texturePath, "source-pack:" + pack.source())
                );
            }
        } catch (IOException | RuntimeException ex) {
            LOGGER.debug("Unable to inspect generated Polymer item models at {}", pack.source(), ex);
        }
    }

    private static String readBedrockTexturePath(JsonElement value) {
        if (value == null || !value.isJsonObject()) {
            return null;
        }

        JsonElement textures = value.getAsJsonObject().get("textures");
        if (textures == null) {
            return null;
        }
        if (textures.isJsonPrimitive()) {
            return textures.getAsString();
        }
        if (textures.isJsonArray() && textures.getAsJsonArray().size() > 0 && textures.getAsJsonArray().get(0).isJsonPrimitive()) {
            return textures.getAsJsonArray().get(0).getAsString();
        }
        return null;
    }

    private static boolean isGeneratedItemId(String itemId) {
        return itemId != null && itemId.indexOf(':') > 0 && !itemId.startsWith("minecraft:");
    }

    private static String itemIdFromModelPath(String modelPath) {
        String prefix = "assets/";
        String marker = "/models/item/";
        String suffix = ".json";
        if (modelPath == null || !modelPath.startsWith(prefix) || !modelPath.endsWith(suffix)) {
            return null;
        }

        int markerIndex = modelPath.indexOf(marker, prefix.length());
        if (markerIndex <= prefix.length()) {
            return null;
        }

        String namespace = modelPath.substring(prefix.length(), markerIndex);
        String value = modelPath.substring(markerIndex + marker.length(), modelPath.length() - suffix.length());
        if (namespace.isBlank() || value.isBlank()) {
            return null;
        }
        return namespace + ":" + value;
    }

    private static TextureReference resolveGeneratedPolymerModelTexture(
            @NotNull GeneratedPolymerPackReader reader,
            @NotNull String modelPath
    ) {
        return resolveGeneratedPolymerModelTexture(reader, modelPath, new LinkedHashMap<>(), new java.util.HashSet<>(), 0);
    }

    private static TextureReference resolveGeneratedPolymerModelTexture(
            @NotNull GeneratedPolymerPackReader reader,
            @NotNull String modelPath,
            @NotNull Map<String, String> textures,
            @NotNull Set<String> seenModels,
            int depth
    ) {
        if (depth >= 16 || !seenModels.add(modelPath)) {
            return null;
        }

        Optional<JsonObject> model = reader.readJsonObject(modelPath);
        if (model.isEmpty()) {
            return null;
        }

        String currentNamespace = namespaceFromModelPath(modelPath);
        JsonElement textureElement = model.get().get("textures");
        if (textureElement != null && textureElement.isJsonObject()) {
            for (Map.Entry<String, JsonElement> entry : textureElement.getAsJsonObject().entrySet()) {
                if (entry.getValue() == null || !entry.getValue().isJsonPrimitive()) {
                    continue;
                }

                String reference = entry.getValue().getAsString();
                textures.putIfAbsent(entry.getKey(), qualifyTextureReference(currentNamespace, reference));
            }
        }

        JsonElement parentElement = model.get().get("parent");
        if (parentElement != null && parentElement.isJsonPrimitive()) {
            String parentPath = modelPathFromReference(currentNamespace, parentElement.getAsString());
            if (parentPath != null) {
                resolveGeneratedPolymerModelTexture(reader, parentPath, textures, seenModels, depth + 1);
            }
        }

        Optional<String> reference = firstGeneratedItemTextureReference(textures);
        if (reference.isEmpty()) {
            return null;
        }

        Optional<String> resolvedReference = resolveGeneratedTextureVariable(reference.get(), textures, new java.util.HashSet<>());
        if (resolvedReference.isEmpty()) {
            return null;
        }

        TextureReference textureReference = textureReference(currentNamespace, resolvedReference.get());
        if (textureReference == null || !reader.exists("assets/" + textureReference.namespace() + "/textures/" + textureReference.value() + ".png")) {
            return null;
        }
        return textureReference;
    }

    private static String namespaceFromModelPath(String modelPath) {
        String prefix = "assets/";
        int end = modelPath.indexOf('/', prefix.length());
        return end == -1 ? "" : modelPath.substring(prefix.length(), end);
    }

    private static String modelPathFromReference(String currentNamespace, String reference) {
        if (reference == null || reference.isBlank()) {
            return null;
        }

        String namespace = currentNamespace;
        String value = reference;
        int separator = reference.indexOf(':');
        if (separator >= 0) {
            namespace = reference.substring(0, separator);
            value = reference.substring(separator + 1);
        }

        if (namespace.equals("minecraft") && (value.equals("item/generated") || value.equals("item/handheld"))) {
            return null;
        }
        return "assets/" + namespace + "/models/" + value + ".json";
    }

    private static String qualifyTextureReference(String namespace, String reference) {
        if (reference == null || reference.isBlank() || reference.startsWith("#") || reference.contains(":")) {
            return reference;
        }
        return namespace + ":" + reference;
    }

    private static Optional<String> firstGeneratedItemTextureReference(Map<String, String> textures) {
        String layer0 = textures.get("layer0");
        if (layer0 != null && !layer0.isBlank()) {
            return Optional.of(layer0);
        }
        String layer1 = textures.get("layer1");
        if (layer1 != null && !layer1.isBlank()) {
            return Optional.of(layer1);
        }
        return Optional.empty();
    }

    private static Optional<String> resolveGeneratedTextureVariable(String reference, Map<String, String> textures, Set<String> seenVariables) {
        String resolved = reference;
        while (resolved != null && resolved.startsWith("#")) {
            String variable = resolved.substring(1);
            if (!seenVariables.add(variable)) {
                return Optional.empty();
            }
            resolved = textures.get(variable);
        }
        return resolved == null || resolved.isBlank() ? Optional.empty() : Optional.of(resolved);
    }

    private static TextureReference textureReference(String currentNamespace, String reference) {
        if (reference == null || reference.isBlank() || reference.startsWith("#")) {
            return null;
        }

        String namespace = currentNamespace;
        String value = reference;
        int separator = reference.indexOf(':');
        if (separator >= 0) {
            namespace = reference.substring(0, separator);
            value = reference.substring(separator + 1);
        }
        value = normalizedPolymerTextureValue(value);
        if (namespace.isBlank() || value.isBlank()) {
            return null;
        }
        return new TextureReference(namespace, value);
    }

    private static String generatedPolymerOutputTexturePath(@NotNull ModInfo mod, @NotNull String textureValue) {
        String value = normalizedPolymerTextureValue(textureValue);
        int separator = value.indexOf('/');
        String directory = separator == -1 ? value : value.substring(0, separator);
        String remaining = separator == -1 ? value : value.substring(separator + 1);
        String bedrockDirectory = TextureConverter.DIRECTORY_LOCATIONS.getOrDefault(directory, directory);
        return String.format(Constants.BEDROCK_TEXTURE_LOCATION, bedrockDirectory + "/" + mod.namespace() + "/" + remaining)
                .replace(".png", "");
    }

    private static String normalizedPolymerTextureValue(@NotNull String value) {
        String normalized = value;
        int namespaceSeparator = normalized.indexOf(':');
        if (namespaceSeparator >= 0) {
            normalized = normalized.substring(namespaceSeparator + 1);
        }
        if (normalized.startsWith("textures/")) {
            normalized = normalized.substring("textures/".length());
        }
        if (normalized.endsWith(".png")) {
            normalized = normalized.substring(0, normalized.length() - ".png".length());
        }
        return normalized;
    }

    private void callEvents(@NotNull Event event) {
        for (ModInfo mod : this.hydraulic.mods()) {
            if (IGNORED_MODS.contains(mod.id())) {
                continue;
            }

            this.callEvent(mod, event);
        }

        if (event instanceof GeyserDefineCustomItemsEvent customItemsEvent) {
            int customDefinitions = customItemsEvent.customItemDefinitions().values().stream()
                    .mapToInt(Collection::size)
                    .sum();
            int nonVanillaDefinitions = customItemsEvent.nonVanillaCustomItemDefinitions().values().stream()
                    .mapToInt(Collection::size)
                    .sum();
            int polymerItemMappings = this.generatedPolymerItemTextureCount();
            LOGGER.info(
                    "Hydraulic custom item registration summary: customDefinitions={}, nonVanillaDefinitions={}, generatedPolymerPacks={}, polymerResourcepackContributesItemMappings={}, generatedPolymerItemMappings={}, examples={}",
                    customDefinitions,
                    nonVanillaDefinitions,
                    this.generatedResourcePacks.size(),
                    polymerItemMappings > 0,
                    polymerItemMappings,
                    this.generatedPolymerItemTextureExamples(3)
            );
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void callEvent(@NotNull ModInfo mod, @NotNull Event event) {
        for (PackModule<?> module : this.modules) {
            module.call(event.getClass(), new PackEventContext(this.hydraulic, mod, module, event));
        }
    }

    private void initializeModLookups() {
        // Step 1: Lookup which namespaces are contained by which mods
        final Multimap<String, ModInfo> namespacesToMods = this.namespacesToMods;
        namespacesToMods.clear();
        for (final ModInfo mod : hydraulic.mods()) {
            for (final Path root : mod.roots()) {
                final Path assets = root.resolve("assets");
                if (!Files.isDirectory(assets)) continue;
                try (Stream<Path> stream = Files.list(assets)) {
                    stream.filter(Files::isDirectory)
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .filter(namespace -> !namespace.equals("minecraft"))
                        .forEach(namespace -> namespacesToMods.put(namespace, mod));
                } catch (IOException e) {
                    LOGGER.error("Failed to list namespaces for mod {}", mod.id(), e);
                }
            }
        }

        // Step 2: Use namespace information to lookup which mods contains what block models
        final Multimap<String, Identifier> modsToBlocks = this.modsToBlocks;
        modsToBlocks.clear();
        final Map<String, Integer> missingBlockstateCounts = new LinkedHashMap<>();
        for (final Identifier block : BuiltInRegistries.BLOCK.keySet()) {
            if (block.getNamespace().equals("minecraft")) continue;
            for (final ModInfo mod : namespacesToMods.get(block.getNamespace())) {
                final Path checkFile = mod.resolveFile("assets/" + block.getNamespace() + "/blockstates/" + block.getPath() + ".json");
                if (checkFile != null) {
                    modsToBlocks.put(mod.id(), block);
                    break;
                } else if (this.hasGeneratedPolymerModelForBlock(block)) {
                    LOGGER.debug("Using generated Polymer model fallback for block state {}", block);
                    modsToBlocks.put(mod.id(), block);
                    break;
                } else {
                    LOGGER.debug("Failed to find path for block state {}, skipping", block);
                    missingBlockstateCounts.merge(block.getNamespace(), 1, Integer::sum);
                }
            }
        }
        if (!missingBlockstateCounts.isEmpty()) {
            int skippedBlockstates = missingBlockstateCounts.values().stream().mapToInt(Integer::intValue).sum();
            LOGGER.warn("Skipped {} blockstate lookup(s) without resource-pack JSON; grouped by namespace: {}", skippedBlockstates, missingBlockstateCounts);
        }

        // Step 3: Use namespace information to lookup which mods contains what item models
        // There's no ordering requirement between this and Step 2.
        final Multimap<String, Identifier> modsToItems = this.modsToItems;
        modsToItems.clear();
        for (final Identifier itemId : BuiltInRegistries.ITEM.keySet()) {
            if (itemId.getNamespace().equals("minecraft")) continue;

            Item item = BuiltInRegistries.ITEM.getValue(itemId);
            Identifier itemModel = item.components().get(DataComponents.ITEM_MODEL);
            // Item model is missing, can't do much here
            if (itemModel == null) {
                LOGGER.warn("Failed to find item model component for item {}, skipping", item);
                continue;
            }

            for (final ModInfo mod : namespacesToMods.get(itemId.getNamespace())) {
                final Path checkFile = mod.resolveFile("assets/" + itemModel.getNamespace() + "/items/" + itemModel.getPath() + ".json");
                if (checkFile != null) {
                    modsToItems.put(mod.id(), itemId);
                    break;
                } else {
                    LOGGER.warn("Failed to find path for item {}, skipping", item);
                }
            }
        }
    }

    /**
     * Creates a {@link ModelStitcher.Provider} that first searches mods, then the Vanilla pack.
     *
     * @param mods The mods to search through.
     * @param modPacks A {@link Map} from mod ID to a {@link List} of {@link ResourcePack}s contained within that mod.
     *                 There may be multiple {@link ResourcePack}s in a mod if there are multiple resource roots for the
     *                 mod.
     * @return A {@link ModelStitcher.Provider} that searches through mods and the Vanilla pack.
     */
    private static ModelStitcher.Provider createModelProvider(
        Collection<ModInfo> mods,
        Map<String, List<ResourcePack>> modPacks,
        Path vanillaPath
    ) {
        final List<ResourcePack> flattenedPacks = mods.stream()
            .map(ModInfo::id)
            .map(modPacks::get)
            .flatMap(List::stream)
            .toList();

        ResourcePack vanillaResourcePack = MinecraftResourcePackReader.minecraft().readFromZipFile(vanillaPath);

        return key -> {
            for (final ResourcePack pack : flattenedPacks) {
                final Model model = pack.model(key);
                if (model != null) {
                    return model;
                }
            }
            return vanillaResourcePack.model(key);
        };
    }

    private static final class PolymerCacheMetadata {
        private String sourcePath;
        private String sourceSha256;
        private long sourceSize;
        private String converterVersion;
        private long convertedSize;
        private int models;
        private int textures;
        private int itemModels;
    }

    private record PolymerInputRoot(Path path, PolymerSanitizeSummary summary) {
    }

    private record PolymerGeneratedItemTexture(String texturePath, String source) {
    }

    private record TextureReference(String namespace, String value) {
    }

    private static final class GeneratedPolymerPackReader implements AutoCloseable {
        private final Path source;
        private final ZipFile zipFile;
        private final Map<String, Optional<JsonObject>> jsonCache = new HashMap<>();
        private final Map<String, Boolean> existsCache = new HashMap<>();

        private GeneratedPolymerPackReader(@NotNull Path source) throws IOException {
            this.source = source;
            this.zipFile = Files.isRegularFile(source) && isArchive(source) ? new ZipFile(source.toFile()) : null;
        }

        private List<String> itemModelPaths() throws IOException {
            if (this.zipFile != null) {
                return this.zipFile.stream()
                        .filter(entry -> !entry.isDirectory())
                        .map(ZipEntry::getName)
                        .map(path -> path.replace('\\', '/'))
                        .filter(GeneratedPolymerPackReader::isItemModelPath)
                        .toList();
            }

            Path assets = this.source.resolve("assets");
            if (!Files.isDirectory(assets)) {
                return List.of();
            }

            try (Stream<Path> stream = Files.walk(assets)) {
                return stream.filter(Files::isRegularFile)
                        .map(path -> normalizePath(this.source.relativize(path)))
                        .filter(GeneratedPolymerPackReader::isItemModelPath)
                        .toList();
            }
        }

        private Optional<JsonObject> readJsonObject(String path) {
            return this.jsonCache.computeIfAbsent(path, this::readJsonObjectUncached);
        }

        private Optional<JsonObject> readJsonObjectUncached(String path) {
            try (InputStream inputStream = this.open(path)) {
                if (inputStream == null) {
                    return Optional.empty();
                }

                try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                    JsonElement element = JsonParser.parseReader(reader);
                    if (element == null || !element.isJsonObject()) {
                        return Optional.empty();
                    }
                    return Optional.of(element.getAsJsonObject());
                }
            } catch (IOException | JsonParseException | IllegalStateException ex) {
                LOGGER.debug("Unable to read generated Polymer JSON {}", path, ex);
                return Optional.empty();
            }
        }

        private boolean exists(String path) {
            return this.existsCache.computeIfAbsent(path, this::existsUncached);
        }

        private boolean existsUncached(String path) {
            if (this.zipFile != null) {
                return this.zipFile.getEntry(path) != null;
            }
            return Files.isRegularFile(this.source.resolve(path));
        }

        private InputStream open(String path) throws IOException {
            if (this.zipFile != null) {
                ZipEntry entry = this.zipFile.getEntry(path);
                return entry == null ? null : this.zipFile.getInputStream(entry);
            }
            Path file = this.source.resolve(path);
            return Files.isRegularFile(file) ? Files.newInputStream(file) : null;
        }

        @Override
        public void close() throws IOException {
            if (this.zipFile != null) {
                this.zipFile.close();
            }
        }

        private static boolean isItemModelPath(String path) {
            return path.startsWith("assets/") && path.contains("/models/item/") && path.endsWith(".json");
        }
    }

    private static final class PolymerSanitizeSummary {
        private int copiedAssets;
        private int skippedAssets;
        private int skippedTextures;
        private int warnings;

        private int copiedAssets() {
            return this.copiedAssets;
        }

        private int skippedAssets() {
            return this.skippedAssets;
        }

        private int skippedTextures() {
            return this.skippedTextures;
        }

        private int warnings() {
            return this.warnings;
        }
    }

    private record TextureValidation(boolean valid, int width, int height, String reason) {
        private static final TextureValidation VALID = new TextureValidation(true, -1, -1, "");
    }

    public ListMultimap<String, ModInfo> getNamespacesToMods() {
        return namespacesToMods;
    }

    public ListMultimap<String, Identifier> getModsToBlocks() {
        return modsToBlocks;
    }

    public ListMultimap<String, Identifier> getModsToItems() {
        return modsToItems;
    }

    public Path getVanillaPath() {
        return vanillaPath;
    }
}
