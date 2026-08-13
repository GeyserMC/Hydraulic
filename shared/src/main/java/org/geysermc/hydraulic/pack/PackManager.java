package org.geysermc.hydraulic.pack;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.mojang.logging.LogUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.geysermc.event.Event;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.hydraulic.pack.context.PackPreProcessContext;
import org.geysermc.hydraulic.util.PackUtil;
import org.geysermc.hydraulic.pack.converter.CustomModelConverter;
import org.geysermc.hydraulic.pack.modules.MetadataPackModule;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.pack.converter.PackConverter;
import org.geysermc.pack.converter.pipeline.AssetConverters;
import org.geysermc.pack.converter.pipeline.ConverterPipeline;
import org.geysermc.pack.converter.type.model.ModelStitcher;
import org.geysermc.pack.converter.util.NioDirectoryFileTreeReader;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.geysermc.pack.converter.util.VanillaPackProvider;
import org.geysermc.pack.converter.util.WebUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Manages packs within Hydraulic. Most of the pack conversion
 * management is done within this class, and it is also responsible
 * for loading the packs onto the server.
 */
public class PackManager {
    private static final Logger LOGGER = LogUtils.getLogger();

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
    private ModelStitcher.Provider modelProvider;

    public PackManager(HydraulicImpl hydraulic) {
        this.hydraulic = hydraulic;
        this.vanillaPath = hydraulic.dataFolder(Constants.MOD_ID).resolve("cache/vanilla-assets.zip");
    }

    /**
     * Initializes the pack manager.
     */
    public void initialize() {
        initializeModLookups();

        final Collection<ModInfo> mods = this.hydraulic.mods();
        final Map<String, List<ResourcePack>> modPacks = Maps.newHashMapWithExpectedSize(mods.size());
        for (final ModInfo mod : mods) {
            try {
                modPacks.put(
                    mod.id(),
                    mod.roots()
                        .stream()
                        .map(path -> {
                            try {
                                Path readable = PackUtil.ensurePackMeta(path);
                                // Mod roots can be either extracted directories (Fabric) or jar files (NeoForge)
                                if (Files.isDirectory(readable)) {
                                    return MinecraftResourcePackReader.minecraft().read(NioDirectoryFileTreeReader.read(readable));
                                }
                                return MinecraftResourcePackReader.minecraft().readFromZipFile(readable);
                            } catch (Exception e) {
                                LOGGER.error("Failed to read resource pack from mod {} at path {}: {}", mod.id(), path, e.getMessage());
                                return null;
                            }
                        })
                        .filter(pack -> pack != null)
                        .toList()
                );
            } catch (Exception e) {
                LOGGER.error("Failed to process mod {}: {}", mod.id(), e.getMessage(), e);
            }
        }

        try {
            Files.createDirectories(this.getVanillaPath().getParent());
        } catch (IOException e) {
            LOGGER.error("Failed to create cache dir");
        }

        // The library's VanillaPackProvider hardcodes version "1.21.11" when downloading the vanilla
        // client jar (upstream limitation; de-hardcoding requires a pack-converter change), which
        // breaks parent model resolution for newer content (e.g. hanging sign templates missing in
        // 1.21.11, so custom hanging signs render as missing models). Generate the vanilla pack from
        // the server's actual Minecraft version first; the library provider then skips its own
        // download since the file exists.
        ensureVanillaPack(this.getVanillaPath());

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
        List<ConverterPipeline<?, ?>> pipelines = new ArrayList<>(packConverters);
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

        try {
            for (final Path root : mod.roots()) {
                // Mod roots can be either extracted directories (Fabric) or jar files (NeoForge)
                final Path readable = PackUtil.ensurePackMeta(root);
                converter.input(readable, !Files.isDirectory(readable)).convert();
            }
        } catch (IOException ex) {
            LOGGER.error("Failed to convert mod {} to pack", mod.id(), ex);
            return false;
        }

        // Now export the pack
        try {
            converter.pack();
        } catch (IOException ex) {
            LOGGER.error("Failed to export pack for mod {}", mod.id(), ex);
        }

        return Files.exists(packPath);
    }

    private void callEvents(@NotNull Event event) {
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

    /**
     * Re-runs the mod lookups. On NeoForge the initial run happens in the mod constructor, before
     * the item registry has been fully populated, leaving {@link #modsToItems} empty. Geyser's
     * custom item event fires after the registry is complete, so rebuild the lookups then.
     */
    public void ensureItemLookupsInitialized() {
        if (this.modsToItems.isEmpty() || this.modsToBlocks.isEmpty()) {
            initializeModLookups();
        }
    }

    private static List<String> listAssetNamespaces(Path root) {
        if (Files.isDirectory(root)) {
            final Path assets = root.resolve("assets");
            if (!Files.isDirectory(assets)) return List.of();
            try (Stream<Path> stream = Files.list(assets)) {
                return stream.filter(Files::isDirectory)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .toList();
            } catch (IOException e) {
                return List.of();
            }
        }
        // Mod roots can be jar files (NeoForge) - look inside via the zip file system
        try (FileSystem fs = FileSystems.newFileSystem(root, (ClassLoader) null)) {
            final Path assets = fs.getPath("assets");
            if (!Files.isDirectory(assets)) return List.of();
            try (Stream<Path> stream = Files.list(assets)) {
                return stream.filter(Files::isDirectory)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .toList();
            } catch (IOException e) {
                return List.of();
            }
        } catch (IOException e) {
            return List.of();
        }
    }

    private void initializeModLookups() {
        // Step 1: Lookup which namespaces are contained by which mods
        final Multimap<String, ModInfo> namespacesToMods = this.namespacesToMods;
        namespacesToMods.clear();
        for (final ModInfo mod : hydraulic.mods()) {
            for (final Path root : mod.roots()) {
                final List<String> namespaces = listAssetNamespaces(root);
                for (final String namespace : namespaces) {
                    if (!namespace.equals("minecraft")) {
                        namespacesToMods.put(namespace, mod);
                    }
                }
            }
        }

        // Step 2: Use namespace information to lookup which mods contains what block models
        final Multimap<String, Identifier> modsToBlocks = this.modsToBlocks;
        modsToBlocks.clear();
        for (final Identifier block : BuiltInRegistries.BLOCK.keySet()) {
            if (block.getNamespace().equals("minecraft")) continue;
            for (final ModInfo mod : namespacesToMods.get(block.getNamespace())) {
                final Path checkFile = mod.resolveFile("assets/" + block.getNamespace() + "/blockstates/" + block.getPath() + ".json");
                if (checkFile != null) {
                    modsToBlocks.put(mod.id(), block);
                    break;
                } else {
                    LOGGER.warn("Failed to find path for block state {}, skipping", block);
                }
            }
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

    /**
     * Generates the trimmed vanilla assets pack from the actual Minecraft version's client jar.
     * <p>
     * {@link VanillaPackProvider} hardcodes version {@code 1.21.11} when downloading the client jar,
     * which lacks models newer versions depend on (e.g. hanging sign templates), causing custom
     * hanging signs to render as missing models. This mirrors the provider's trimming logic
     * (models, blockstates, font textures) but uses the latest release client jar instead.
     *
     * @param vanillaPath the target zip path
     */
    private static void ensureVanillaPack(Path vanillaPath) {
        if (Files.exists(vanillaPath)) {
            return;
        }

        try {
            final String manifestUrl = "https://launchermeta.mojang.com/mc/game/version_manifest.json";
            final JsonObject manifest = JsonParser.parseString(WebUtils.getBody(manifestUrl)).getAsJsonObject();
            final String latestId = manifest.getAsJsonObject("latest").get("release").getAsString();
            String versionUrl = "";
            for (JsonElement version : manifest.getAsJsonArray("versions")) {
                final JsonObject versionObject = version.getAsJsonObject();
                if (versionObject.get("id").getAsString().equals(latestId)) {
                    versionUrl = versionObject.get("url").getAsString();
                    break;
                }
            }
            if (versionUrl.isEmpty()) {
                throw new IOException("Unable to find version " + latestId + " in the version manifest");
            }

            final String clientUrl = JsonParser.parseString(WebUtils.getBody(versionUrl)).getAsJsonObject()
                    .getAsJsonObject("downloads").getAsJsonObject("client").get("url").getAsString();

            LOGGER.info("Downloading Minecraft {} client jar for vanilla pack generation...", latestId);
            final Path tmpJar = Files.createTempFile("hydraulic-vanilla-", ".jar");
            try (InputStream in = new URL(clientUrl).openStream()) {
                Files.copy(in, tmpJar, StandardCopyOption.REPLACE_EXISTING);
            }

            Files.createDirectories(vanillaPath.getParent());
            try (FileSystem src = FileSystems.newFileSystem(tmpJar, (ClassLoader) null);
                 ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(vanillaPath))) {
                copyVanillaAssets(src.getPath("/assets/minecraft/models"), out);
                copyVanillaAssets(src.getPath("/assets/minecraft/blockstates"), out);
                copyVanillaAssets(src.getPath("/assets/minecraft/textures/font"), out);
                // The library provider injects builtin models (parents of item/generated etc.)
                // from its own resources - mirror that so parent model resolution works.
                writeBuiltinModel(out, "entity.json");
                writeBuiltinModel(out, "generated.json");
            } finally {
                Files.deleteIfExists(tmpJar);
            }
            LOGGER.info("Vanilla pack generated from Minecraft {}", latestId);
        } catch (Exception e) {
            LOGGER.error("Failed to generate vanilla pack, falling back to library provider", e);
            try {
                Files.deleteIfExists(vanillaPath);
            } catch (IOException ignored) {
            }
        }
    }

    private static void copyVanillaAssets(Path source, ZipOutputStream out) throws IOException {
        if (!Files.exists(source)) {
            return;
        }
        try (Stream<Path> walker = Files.walk(source)) {
            for (Path file : walker.filter(Files::isRegularFile).toList()) {
                final String name = file.toString().replace('\\', '/');
                final ZipEntry entry = new ZipEntry(name.startsWith("/") ? name.substring(1) : name);
                out.putNextEntry(entry);
                if (name.contains("/models/") && name.endsWith(".json")) {
                    // Newer Minecraft versions use element rotations beyond the old [-45, 45] limit
                    // which the creative serializer rejects. Clamp them when generating the vanilla pack.
                    out.write(clampModelRotations(Files.readAllBytes(file)));
                } else {
                    Files.copy(file, out);
                }
                out.closeEntry();
            }
        }
    }

    private static void writeBuiltinModel(ZipOutputStream out, String name) throws IOException {
        try (InputStream in = VanillaPackProvider.class.getResourceAsStream("/vanilla/builtin/" + name)) {
            if (in == null) {
                LOGGER.warn("Builtin model resource /vanilla/builtin/{} not found", name);
                return;
            }
            out.putNextEntry(new ZipEntry("assets/minecraft/models/builtin/" + name));
            in.transferTo(out);
            out.closeEntry();
        }
    }

    private static byte[] clampModelRotations(byte[] data) {
        try {
            final JsonObject root = JsonParser.parseString(new String(data, StandardCharsets.UTF_8)).getAsJsonObject();
            final JsonElement elements = root.get("elements");
            if (elements == null || !elements.isJsonArray()) {
                return data;
            }
            boolean changed = false;
            for (JsonElement element : elements.getAsJsonArray()) {
                if (!element.isJsonObject()) {
                    continue;
                }
                final JsonObject elementObject = element.getAsJsonObject();
                final JsonElement rotation = elementObject.get("rotation");
                if (rotation == null) {
                    continue;
                }
                if (rotation.isJsonArray()) {
                    // Old-style list rotation: [x, y, z]
                    final JsonArray rotationArray = rotation.getAsJsonArray();
                    for (int i = 0; i < rotationArray.size(); i++) {
                        final float value = rotationArray.get(i).getAsFloat();
                        if (value > 45.0f) {
                            rotationArray.set(i, new JsonPrimitive(45.0f));
                            changed = true;
                        } else if (value < -45.0f) {
                            rotationArray.set(i, new JsonPrimitive(-45.0f));
                            changed = true;
                        }
                    }
                } else if (rotation.isJsonObject() && rotation.getAsJsonObject().has("angle")) {
                    // New-style rotation object: {"origin": [...], "axis": "x", "angle": 67.5}
                    final JsonObject rotationObject = rotation.getAsJsonObject();
                    final float angle = rotationObject.get("angle").getAsFloat();
                    if (angle > 45.0f) {
                        rotationObject.addProperty("angle", 45.0f);
                        changed = true;
                    } else if (angle < -45.0f) {
                        rotationObject.addProperty("angle", -45.0f);
                        changed = true;
                    }
                }
            }
            if (changed) {
                return root.toString().getBytes(StandardCharsets.UTF_8);
            }
        } catch (Exception ignored) {
        }
        return data;
    }
}
