package org.geysermc.hydraulic.item;

import com.google.auto.service.AutoService;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.kyori.adventure.key.Key;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;
import org.geysermc.geyser.api.item.custom.v2.CustomItemBedrockOptions;
import org.geysermc.geyser.api.item.custom.v2.CustomItemDefinition;
import org.geysermc.geyser.api.item.custom.v2.NonVanillaCustomItemDefinition;
import org.geysermc.geyser.api.item.custom.v2.component.geyser.GeyserBlockPlacer;
import org.geysermc.geyser.api.item.custom.v2.component.geyser.GeyserChargeable;
import org.geysermc.geyser.api.item.custom.v2.component.geyser.GeyserItemDataComponents;
import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.pack.PackLogListener;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.PolymerDiagnosticReport;
import org.geysermc.hydraulic.pack.TexturePackModule;
import org.geysermc.hydraulic.pack.context.PackContext;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.hydraulic.pack.context.PackPreProcessContext;
import org.geysermc.hydraulic.component.ComponentConverter;
import org.geysermc.hydraulic.util.HydraulicKey;
import org.geysermc.hydraulic.util.PackUtil;
import org.geysermc.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.pack.converter.type.model.ModelStitcher;
import org.geysermc.pack.converter.type.texture.TextureConverter;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.item.*;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.model.ModelTexture;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@AutoService(PackModule.class)
public class ItemPackModule extends TexturePackModule<ItemPackModule> {
    private static final String POLYMER_PLACEHOLDER_TEXTURE_ID = "hydraulic:polymer_placeholder";
    private static final String POLYMER_PLACEHOLDER_TEXTURE_PATH = "textures/items/hydraulic/polymer_placeholder";
    private static final String POLYMER_BLOCK_PLACEHOLDER_TEXTURE_ID = "hydraulic:polymer_placeholder_block";
    private static final String POLYMER_BLOCK_PLACEHOLDER_TEXTURE_PATH = "textures/blocks/hydraulic/polymer_placeholder_block";
    private static final String POLYDECORATIONS_MOD_ID = "polydecorations";
    private static final String POLYMER_SIMPLE_ITEM_CARRIER = "minecraft:trial_key";
    private static final String POLYMER_BLOCK_ITEM_CARRIER = "minecraft:rabbit_foot";
    private static final int POLYMER_MODEL_PARENT_LIMIT = 16;
    private static final byte[] POLYMER_PLACEHOLDER_TEXTURE = Base64.getDecoder().decode(
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+/p9sAAAAASUVORK5CYII="
    );

    private final List<Identifier> itemsWith2dIcon = new ArrayList<>();
    private final List<Identifier> handheldItems = new ArrayList<>();
    private final Map<String, String> itemBuiltinTexture = new HashMap<>();

    public ItemPackModule() {
        this.listenOn(GeyserDefineCustomItemsEvent.class, this::onDefineCustomItems);

        this.preProcess(this::preProcess);
        this.postProcess(this::postProcess);
    }

    private void handleModel(@NotNull PackPreProcessContext<ItemPackModule> context, ItemModel itemModel, Identifier itemLocation) {
        if (itemModel instanceof ReferenceItemModel referenceModel) {
            Key modelKey = referenceModel.model();

            List<Model> modelList = Lists.newArrayList(context.assets((pack) -> { // This can probably be done easier, but im not sure how
                Model model = pack.model(modelKey);
                if (model == null) return List.of();

                return List.of(model);
            }));
            if (modelList.isEmpty()) return;

            Model model = modelList.getFirst();
            Key modelParent = model.parent();
            if (modelParent == null) return;

            if (modelParent.value().equals("item/generated")) { // If the parent is item/generated, it's a 2D icon
                itemsWith2dIcon.add(itemLocation);
            } else if (modelParent.value().equals("item/handheld")) { // If the parent is item/handheld, it's handheld
                itemsWith2dIcon.add(itemLocation); // item/handheld has the parent item/generated, so lets assume it's 2D
                handheldItems.add(itemLocation);
            }
        } else if (itemModel instanceof SelectItemModel selectModel) { // See if we can actually do select models here
            handleModel(context, selectModel.fallback(), itemLocation);
        } else if (itemModel instanceof CompositeItemModel compositeModel) { // TODO: See if we can stitch together item models, for now this will use just the first model
            handleModel(context, compositeModel.models().getFirst(), itemLocation);
        } else if (itemModel instanceof RangeDispatchItemModel rangeDispatchModel) {
            handleModel(context, rangeDispatchModel.fallback(), itemLocation);
        }
    }

    private void preProcess(@NotNull PackPreProcessContext<ItemPackModule> context) {
        for (team.unnamed.creative.item.Item item : context.assets(ResourcePack::items)) {
            Identifier itemLocation = HydraulicKey.of(item.key()).identifier();
            handleModel(context, item.model(), itemLocation);
        }

//        for (Model model : context.assets(ResourcePack::models)) {
//            Key modelParent = model.parent();
//            if (modelParent != null) {
//                if (modelParent.value().equals("item/generated")) { // If the parent is item/generated, it's a 2D icon
//                    HydraulicKey key = HydraulicKey.of(model.key());
//                    key.path(key.path().replace("item/", ""));
//                    itemsWith2dIcon.add(key.location());
//                } else if (modelParent.value().equals("item/handheld")) { // If the parent is item/handheld, it's handheld
//                    HydraulicKey key = HydraulicKey.of(model.key());
//                    key.path(key.path().replace("item/", ""));
//                    itemsWith2dIcon.add(key.location()); // item/handheld has the parent item/generated, so lets assume it's 2D
//                    handheldItems.add(key.location());
//                }
//            }
//        }

        List<Item> items = context.registryValues(BuiltInRegistries.ITEM);
        PackLogListener packLogListener = new PackLogListener(context.logger());
        for (Item item : items) {
            Identifier itemLocation = BuiltInRegistries.ITEM.getKey(item);

            Model baseModel = context.modelProvider().model(Key.key(itemLocation.getNamespace(), "item/" + itemLocation.getPath()));
            if (baseModel == null) {
                continue;
            }

            Model model = new ModelStitcher(context.modelProvider(), baseModel, packLogListener).stitch();
            if (model == null) {
                continue;
            }

            List<ModelTexture> layers = model.textures().layers();
            if (layers == null || layers.isEmpty()) {
                continue;
            }

            Key layer0 = layers.getFirst().key();

            if (layer0 != null && layer0.namespace().equals(Key.MINECRAFT_NAMESPACE)) {
                itemBuiltinTexture.put(itemLocation.toString(), PackUtil.getTextureName(layer0.toString()));
            }
        }
    }

    private void postProcess(@NotNull PackPostProcessContext<ItemPackModule> context) {
        ResourcePack assets = context.javaResourcePack();
        BedrockResourcePack bedrockPack = context.bedrockResourcePack();

        List<Item> items = context.registryValues(BuiltInRegistries.ITEM);
        if (items.isEmpty() && isPolymerGeneratedPack(context)) {
            postProcessPolymerGeneratedPack(context, assets, bedrockPack);
            return;
        }

        context.logger().info("Items to convert: {} in mod {}", items.size(), context.mod().id());

        PackLogListener packLogListener = new PackLogListener(context.logger());
        for (Item item : items) {
            Identifier itemLocation = BuiltInRegistries.ITEM.getKey(item);

            Model baseModel = assets.model(Key.key(itemLocation.getNamespace(), "item/" + itemLocation.getPath()));
            if (baseModel == null) {
                context.logger().warn("Item {} has no item model, skipping", itemLocation);
                continue;
            }

            Model model = new ModelStitcher(context.modelProvider(), baseModel, packLogListener).stitch();

            List<ModelTexture> layers = model.textures().layers();
            if (layers == null || layers.isEmpty()) {
                // Don't warn if a block as they can use the block model
                if (!(item instanceof BlockItem)) {
                    context.logger().warn("Item {} has no layer0 texture, skipping", itemLocation);
                }

                continue;
            }

            ModelTexture layer0 = layers.getFirst();
            String outputLoc = getOutputFromModel(context, layer0.key()); // TODO: sort this out, layer0.key() can be null, but the method we use doesn't want that
            bedrockPack.addItemTexture(itemLocation.toString(), outputLoc.replace(".png", ""));
        }
    }

    @Override
    public boolean test(@NotNull PackPostProcessContext<ItemPackModule> context) {
        return !context.registryValues(BuiltInRegistries.ITEM).isEmpty() || isPolymerGeneratedPack(context);
    }

    private void postProcessPolymerGeneratedPack(
            @NotNull PackPostProcessContext<ItemPackModule> context,
            @NotNull ResourcePack assets,
            @NotNull BedrockResourcePack bedrockPack
    ) {
        int registeredTextures = 0;
        PolymerFallbackSummary summary = new PolymerFallbackSummary();
        PolymerBlockFallbackSummary blockSummary = new PolymerBlockFallbackSummary();
        PolymerDiagnosticReport diagnosticReport = context.hydraulic().getPackManager().polymerDiagnosticReport();
        PackLogListener packLogListener = new PackLogListener(context.logger());

        summary.unsupportedMultipartBlockstates = (int) assets.blockStates().stream()
                .filter(blockState -> blockState.multipart() != null && !blockState.multipart().isEmpty())
                .count();
        if (summary.unsupportedMultipartBlockstates > 0) {
            recordPolymerWarning(
                    diagnosticReport,
                    context.mod().id(),
                    "blockstates",
                    summary.unsupportedMultipartBlockstates + " multipart blockstate(s) are not converted by the Polymer bridge yet",
                    "left to PackConverter/default handling"
            );
        }

        try (PolymerItemTextureResolver textureResolver = new PolymerItemTextureResolver(context, packLogListener)) {
            for (Model baseModel : assets.models()) {
                Key modelKey = baseModel.key();
                if (modelKey == null || !modelKey.value().startsWith("item/")) {
                    continue;
                }

                PolymerTextureResolution textureResolution = textureResolver.resolve(baseModel);
                summary.textureResolutionAttempts++;
                summary.addFallbackReason(textureResolution.reason());

                if (textureResolution.status() == PolymerTextureResolutionStatus.UNRESOLVED_PARENT) {
                    summary.unsupportedParentChains++;
                    boolean fallbackUsed = registerPolymerPlaceholder(context, bedrockPack, modelKey);
                    if (fallbackUsed) {
                        summary.placeholderFallbacks++;
                    }
                    recordPolymerWarning(
                            diagnosticReport,
                            context.mod().id(),
                            modelKey.toString(),
                            textureResolution.reason(),
                            fallbackUsed ? "placeholder texture" : "skipped vanilla model"
                    );
                    continue;
                }

                Model model = textureResolution.model();
                if (model == null) {
                    summary.unsupportedParentChains++;
                    boolean fallbackUsed = registerPolymerPlaceholder(context, bedrockPack, modelKey);
                    if (fallbackUsed) {
                        summary.placeholderFallbacks++;
                    }
                    recordPolymerWarning(
                            diagnosticReport,
                            context.mod().id(),
                            modelKey.toString(),
                            textureResolution.reason(),
                            fallbackUsed ? "placeholder texture" : "skipped vanilla model"
                    );
                    continue;
                }

                if (!model.overrides().isEmpty()) {
                    summary.unsupportedCustomModelOverrides++;
                    context.logger().debug("Polymer item model {} uses overrides/custom model data; Hydraulic cannot map this to Bedrock yet", modelKey);
                    if (modelKey.namespace().equals(Key.MINECRAFT_NAMESPACE)) {
                        summary.skippedAssets++;
                        recordPolymerWarning(
                                diagnosticReport,
                                context.mod().id(),
                                modelKey.toString(),
                                "custom model overrides are not mapped to Bedrock custom item data yet",
                                "skipped vanilla override model"
                        );
                        continue;
                    }
                    recordPolymerWarning(
                            diagnosticReport,
                            context.mod().id(),
                            modelKey.toString(),
                            "custom model overrides are not mapped to Bedrock custom item data yet",
                            "used simple layer0 texture when available"
                    );
                }

                if (!textureResolution.resolved()) {
                    summary.missingTextures++;
                    boolean fallbackUsed = registerPolymerPlaceholder(context, bedrockPack, modelKey);
                    if (fallbackUsed) {
                        summary.placeholderFallbacks++;
                    }
                    recordPolymerWarning(
                            diagnosticReport,
                            context.mod().id(),
                            modelKey.toString(),
                            textureResolution.reason(),
                            fallbackUsed ? "placeholder texture" : "skipped vanilla model"
                    );
                    continue;
                }

                Key layerKey = textureResolution.textureKey();
                String itemPath = modelKey.value().substring("item/".length());
                String itemId = modelKey.namespace() + ":" + itemPath;
                String outputLoc = getOutputFromPolymerModel(context, layerKey).replace(".png", "");
                bedrockPack.addItemTexture(itemId, outputLoc);
                registeredTextures++;
                summary.textureResolutionSuccesses++;
            }

            for (Model baseModel : assets.models()) {
                Key modelKey = baseModel.key();
                if (modelKey == null || !modelKey.value().startsWith("block/")) {
                    continue;
                }

                blockSummary.inspected++;
                PolymerBlockTextureResolution blockTexture = textureResolver.resolveBlock(baseModel);
                String blockPath = modelKey.value().substring("block/".length());
                String blockId = modelKey.namespace() + ":" + blockPath;

                if (blockTexture.resolved()) {
                    String outputLoc = getOutputFromPolymerModel(context, blockTexture.textureKey()).replace(".png", "");
                    bedrockPack.addBlockTexture(blockId, outputLoc);
                    blockSummary.invisiblePlacementPreventions++;
                    if (blockTexture.simpleCube()) {
                        blockSummary.simpleMappingsResolved++;
                    } else {
                        blockSummary.decorativeFallbacks++;
                    }
                    continue;
                }

                bedrockPack.addBlockTexture(blockId, POLYMER_BLOCK_PLACEHOLDER_TEXTURE_PATH);
                blockSummary.decorativeFallbacks++;
                blockSummary.placeholderFallbacks++;
                blockSummary.invisiblePlacementPreventions++;
                blockSummary.addFallbackReason(blockTexture.reason());
                recordPolymerWarning(
                        diagnosticReport,
                        context.mod().id(),
                        modelKey.toString(),
                        blockTexture.reason(),
                        "placeholder block texture"
                );
            }
        }

        if (summary.placeholderFallbacks > 0) {
            bedrockPack.addExtraFile(POLYMER_PLACEHOLDER_TEXTURE, POLYMER_PLACEHOLDER_TEXTURE_PATH + ".png");
            bedrockPack.addItemTexture(POLYMER_PLACEHOLDER_TEXTURE_ID, POLYMER_PLACEHOLDER_TEXTURE_PATH);
        }
        if (blockSummary.placeholderFallbacks > 0) {
            bedrockPack.addExtraFile(POLYMER_PLACEHOLDER_TEXTURE, POLYMER_BLOCK_PLACEHOLDER_TEXTURE_PATH + ".png");
            bedrockPack.addBlockTexture(POLYMER_BLOCK_PLACEHOLDER_TEXTURE_ID, POLYMER_BLOCK_PLACEHOLDER_TEXTURE_PATH);
        }

        context.logger().info(
                "Polymer item texture resolution for {}: attempts={}, resolved={}, placeholder={}, fallbackReasons={}",
                context.mod().id(),
                summary.textureResolutionAttempts,
                summary.textureResolutionSuccesses,
                summary.placeholderFallbacks,
                summary.fallbackReasons
        );
        context.logger().info(
                "Polymer fallback registered {} simple item texture(s) for {}; placeholderFallbacks={}, skippedAssets={}",
                registeredTextures,
                context.mod().id(),
                summary.placeholderFallbacks,
                summary.skippedAssets
        );
        context.logger().info(
                "Polymer blockstate mapping summary for {}: inspected={}, simpleResolved={}, fallback={}, placeholder={}, invisiblePlacementPrevention={}, fallbackReasons={}",
                context.mod().id(),
                blockSummary.inspected,
                blockSummary.simpleMappingsResolved,
                blockSummary.decorativeFallbacks,
                blockSummary.placeholderFallbacks,
                blockSummary.invisiblePlacementPreventions,
                blockSummary.fallbackReasons
        );
        context.logger().info(
                "Polymer unsupported model summary for {}: unsupported custom model overrides={}, unsupported multipart blockstates={}, unsupported parent chains={}, missing textures={}, skipped oversized textures={}, warnings={}",
                context.mod().id(),
                summary.unsupportedCustomModelOverrides,
                summary.unsupportedMultipartBlockstates,
                summary.unsupportedParentChains,
                summary.missingTextures,
                summary.skippedOversizedTextures,
                summary.warnings()
        );
        if (diagnosticReport != null) {
            diagnosticReport.recordUnsupportedSummary(
                    context.mod().id(),
                    summary.unsupportedCustomModelOverrides,
                    summary.unsupportedMultipartBlockstates,
                    summary.unsupportedParentChains,
                    summary.missingTextures,
                    summary.skippedOversizedTextures,
                    summary.placeholderFallbacks,
                    summary.skippedAssets,
                    summary.warnings()
            );
            diagnosticReport.recordPolymerVisualSummary(
                    context.mod().id(),
                    registeredTextures,
                    summary.placeholderFallbacks,
                    blockSummary.decorativeFallbacks,
                    0,
                    0
            );
            diagnosticReport.recordPolymerItemTextureResolution(
                    context.mod().id(),
                    summary.textureResolutionAttempts,
                    summary.textureResolutionSuccesses,
                    summary.placeholderFallbacks,
                    summary.fallbackReasons
            );
            diagnosticReport.recordPolymerBlockFallbackSummary(
                    context.mod().id(),
                    blockSummary.inspected,
                    blockSummary.simpleMappingsResolved,
                    blockSummary.decorativeFallbacks,
                    blockSummary.placeholderFallbacks,
                    blockSummary.invisiblePlacementPreventions,
                    blockSummary.fallbackReasons
            );
        }
    }

    private static void recordPolymerWarning(
            PolymerDiagnosticReport diagnosticReport,
            String packId,
            String filePath,
            String reason,
            String fallback
    ) {
        if (diagnosticReport != null) {
            diagnosticReport.recordWarning(packId, filePath, reason, fallback);
        }
    }

    private boolean registerPolymerPlaceholder(
            @NotNull PackPostProcessContext<ItemPackModule> context,
            @NotNull BedrockResourcePack bedrockPack,
            @NotNull Key modelKey
    ) {
        if (modelKey.namespace().equals(Key.MINECRAFT_NAMESPACE)) {
            context.logger().debug("Skipping Polymer placeholder for vanilla item model {} to avoid overriding vanilla Bedrock visuals", modelKey);
            return false;
        }

        String itemPath = modelKey.value().substring("item/".length());
        String itemId = modelKey.namespace() + ":" + itemPath;
        bedrockPack.addItemTexture(itemId, POLYMER_PLACEHOLDER_TEXTURE_PATH);
        context.logger().debug("Using Polymer placeholder texture for {} because its item model could not be converted safely", itemId);
        return true;
    }

    private static boolean isPolymerGeneratedPack(@NotNull PackContext<ItemPackModule> context) {
        return context.mod().id().startsWith("polymer_resourcepack");
    }

    private static String getOutputFromPolymerModel(@NotNull PackContext<ItemPackModule> context, @NotNull Key key) {
        String value = normalizedTextureValue(key.value());
        int separator = value.indexOf('/');
        String directory = separator == -1 ? value : value.substring(0, separator);
        String remaining = separator == -1 ? value : value.substring(separator + 1);
        String bedrockDirectory = TextureConverter.DIRECTORY_LOCATIONS.getOrDefault(directory, directory);
        return String.format(Constants.BEDROCK_TEXTURE_LOCATION, bedrockDirectory + "/" + context.mod().namespace() + "/" + remaining);
    }

    private static String normalizedTextureValue(@NotNull String value) {
        String normalized = value;
        if (normalized.startsWith("textures/")) {
            normalized = normalized.substring("textures/".length());
        }
        if (normalized.endsWith(".png")) {
            normalized = normalized.substring(0, normalized.length() - ".png".length());
        }
        return normalized;
    }

    private static final class PolymerFallbackSummary {
        private int unsupportedCustomModelOverrides;
        private int unsupportedMultipartBlockstates;
        private int unsupportedParentChains;
        private int missingTextures;
        private int skippedOversizedTextures;
        private int placeholderFallbacks;
        private int skippedAssets;
        private int textureResolutionAttempts;
        private int textureResolutionSuccesses;
        private final Map<String, Integer> fallbackReasons = new LinkedHashMap<>();

        private void addFallbackReason(String reason) {
            if (reason == null || reason.isBlank() || reason.equals("resolved")) {
                return;
            }
            this.fallbackReasons.merge(reason, 1, Integer::sum);
        }

        private int warnings() {
            return this.unsupportedCustomModelOverrides
                    + this.unsupportedMultipartBlockstates
                    + this.unsupportedParentChains
                    + this.missingTextures
                    + this.skippedOversizedTextures
                    + this.placeholderFallbacks
                    + this.skippedAssets;
        }
    }

    private static final class PolymerBlockFallbackSummary {
        private int inspected;
        private int simpleMappingsResolved;
        private int decorativeFallbacks;
        private int placeholderFallbacks;
        private int invisiblePlacementPreventions;
        private final Map<String, Integer> fallbackReasons = new LinkedHashMap<>();

        private void addFallbackReason(String reason) {
            if (reason == null || reason.isBlank() || reason.equals("resolved")) {
                return;
            }
            this.fallbackReasons.merge(reason, 1, Integer::sum);
        }
    }

    private enum PolymerTextureResolutionStatus {
        RESOLVED,
        MISSING_TEXTURE,
        UNRESOLVED_PARENT,
        PARENT_LOOP,
        INVALID_JSON,
        NO_TEXTURES_OBJECT,
        NO_LAYER_TEXTURE
    }

    private record PolymerTextureResolution(
            PolymerTextureResolutionStatus status,
            Model model,
            Key textureKey,
            String reason
    ) {
        private static PolymerTextureResolution resolved(Model model, Key textureKey) {
            return new PolymerTextureResolution(PolymerTextureResolutionStatus.RESOLVED, model, textureKey, "resolved");
        }

        private static PolymerTextureResolution failed(PolymerTextureResolutionStatus status, Model model, String reason) {
            return new PolymerTextureResolution(status, model, null, reason);
        }

        private boolean resolved() {
            return this.status == PolymerTextureResolutionStatus.RESOLVED && this.textureKey != null;
        }
    }

    private record PolymerBlockTextureResolution(
            PolymerTextureResolutionStatus status,
            Key textureKey,
            boolean simpleCube,
            String reason
    ) {
        private static PolymerBlockTextureResolution resolved(Key textureKey, boolean simpleCube) {
            return new PolymerBlockTextureResolution(PolymerTextureResolutionStatus.RESOLVED, textureKey, simpleCube, "resolved");
        }

        private static PolymerBlockTextureResolution failed(PolymerTextureResolutionStatus status, String reason) {
            return new PolymerBlockTextureResolution(status, null, false, reason);
        }

        private boolean resolved() {
            return this.status == PolymerTextureResolutionStatus.RESOLVED && this.textureKey != null;
        }
    }

    private static final class PolymerItemTextureResolver implements AutoCloseable {
        private final PackPostProcessContext<ItemPackModule> context;
        private final PackLogListener packLogListener;
        private final Path source;
        private final ZipFile zipFile;
        private final Map<String, Optional<JsonObject>> modelCache = new HashMap<>();
        private final Map<String, Boolean> textureExistsCache = new HashMap<>();

        private PolymerItemTextureResolver(
            @NotNull PackPostProcessContext<ItemPackModule> context,
            @NotNull PackLogListener packLogListener
        ) {
            this.context = context;
            this.packLogListener = packLogListener;
            this.source = firstRoot(context);
            this.zipFile = openZip(this.source);
        }

        private PolymerTextureResolution resolve(@NotNull Model baseModel) {
            Key modelKey = baseModel.key();
            Model stitchedModel = new ModelStitcher(this.context.modelProvider(), baseModel, this.packLogListener).stitch();
            Key stitchedTexture = firstLayer(stitchedModel);
            if (stitchedTexture != null && this.textureExists(stitchedTexture)) {
                return PolymerTextureResolution.resolved(stitchedModel, stitchedTexture);
            }

            RawTextureResolution rawResolution = this.resolveRawModelTexture(modelKey);
            if (rawResolution.status() == PolymerTextureResolutionStatus.RESOLVED) {
                return PolymerTextureResolution.resolved(stitchedModel == null ? baseModel : stitchedModel, rawResolution.textureKey());
            }

            if (stitchedModel == null) {
                return PolymerTextureResolution.failed(PolymerTextureResolutionStatus.UNRESOLVED_PARENT, null, "unresolved parent chain");
            }
            return PolymerTextureResolution.failed(rawResolution.status(), stitchedModel, rawResolution.reason());
        }

        private PolymerBlockTextureResolution resolveBlock(@NotNull Model baseModel) {
            RawTextureResolution rawResolution = this.resolveRawModelTexture(baseModel.key(), ModelKind.BLOCK);
            if (rawResolution.status() == PolymerTextureResolutionStatus.RESOLVED) {
                return PolymerBlockTextureResolution.resolved(rawResolution.textureKey(), rawResolution.simpleBlockParent());
            }
            return PolymerBlockTextureResolution.failed(rawResolution.status(), rawResolution.reason());
        }

        private RawTextureResolution resolveRawModelTexture(Key modelKey) {
            return this.resolveRawModelTexture(modelKey, ModelKind.ITEM);
        }

        private RawTextureResolution resolveRawModelTexture(Key modelKey, ModelKind modelKind) {
            if (modelKey == null) {
                return RawTextureResolution.failed(PolymerTextureResolutionStatus.UNRESOLVED_PARENT, "model key is missing");
            }

            Set<String> seenModels = new HashSet<>();
            Map<String, String> textures = new LinkedHashMap<>();
            boolean simpleBlockParent = false;
            Key currentKey = modelKey;
            for (int depth = 0; depth < POLYMER_MODEL_PARENT_LIMIT; depth++) {
                String cacheKey = currentKey.asString();
                if (!seenModels.add(cacheKey)) {
                    return RawTextureResolution.failed(PolymerTextureResolutionStatus.PARENT_LOOP, "parent loop at " + cacheKey);
                }

                Optional<JsonObject> model = this.readModel(currentKey);
                if (model.isEmpty()) {
                    if (isSupportedVanillaTerminal(currentKey, modelKind)) {
                        simpleBlockParent |= isSimpleBlockParent(currentKey);
                        break;
                    }
                    return RawTextureResolution.failed(PolymerTextureResolutionStatus.UNRESOLVED_PARENT, "unresolved parent " + cacheKey);
                }

                JsonObject object = model.get();
                JsonElement textureElement = object.get("textures");
                if (textureElement != null && textureElement.isJsonObject()) {
                    for (Map.Entry<String, JsonElement> entry : textureElement.getAsJsonObject().entrySet()) {
                        if (entry.getValue() != null && entry.getValue().isJsonPrimitive()) {
                            textures.putIfAbsent(entry.getKey(), entry.getValue().getAsString());
                        }
                    }
                }

                JsonElement parentElement = object.get("parent");
                if (parentElement == null || !parentElement.isJsonPrimitive()) {
                    break;
                }

                Key parentKey = this.parentKey(currentKey.namespace(), parentElement.getAsString());
                if (parentKey == null) {
                    return RawTextureResolution.failed(PolymerTextureResolutionStatus.UNRESOLVED_PARENT, "invalid parent " + parentElement.getAsString());
                }
                simpleBlockParent |= modelKind == ModelKind.BLOCK && isSimpleBlockParent(parentKey);
                currentKey = parentKey;
            }

            Optional<String> textureReference = firstTextureReference(textures, modelKind);
            if (textureReference.isEmpty()) {
                return RawTextureResolution.failed(PolymerTextureResolutionStatus.NO_TEXTURES_OBJECT, "no textures object or layer texture");
            }

            Optional<String> resolvedReference = resolveTextureVariable(textureReference.get(), textures, new HashSet<>());
            if (resolvedReference.isEmpty()) {
                return RawTextureResolution.failed(PolymerTextureResolutionStatus.NO_LAYER_TEXTURE, "texture variable " + textureReference.get() + " could not be resolved");
            }

            Key textureKey = textureKey(modelKey.namespace(), resolvedReference.get());
            if (textureKey == null) {
                return RawTextureResolution.failed(PolymerTextureResolutionStatus.NO_LAYER_TEXTURE, "invalid texture reference " + resolvedReference.get());
            }
            if (!this.textureExists(textureKey)) {
                return RawTextureResolution.failed(PolymerTextureResolutionStatus.MISSING_TEXTURE, "texture file missing for " + textureKey.asString());
            }
            return RawTextureResolution.resolved(textureKey, simpleBlockParent);
        }

        private Optional<JsonObject> readModel(Key key) {
            String path = "assets/" + key.namespace() + "/models/" + key.value() + ".json";
            return this.modelCache.computeIfAbsent(path, this::readJsonObject);
        }

        private Optional<JsonObject> readJsonObject(String path) {
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
                this.context.logger().debug("Unable to read Polymer item model {} from {}", path, this.source, ex);
                return Optional.empty();
            }
        }

        private boolean textureExists(Key key) {
            String value = normalizedTextureValue(key.value());
            String path = "assets/" + key.namespace() + "/textures/" + value + ".png";
            return this.textureExistsCache.computeIfAbsent(path, this::exists);
        }

        private boolean exists(String path) {
            if (this.zipFile != null) {
                return this.zipFile.getEntry(path) != null;
            }
            if (this.source != null && Files.isDirectory(this.source)) {
                return Files.isRegularFile(this.source.resolve(path));
            }
            return false;
        }

        private InputStream open(String path) throws IOException {
            if (this.zipFile != null) {
                ZipEntry entry = this.zipFile.getEntry(path);
                return entry == null ? null : this.zipFile.getInputStream(entry);
            }
            if (this.source != null && Files.isDirectory(this.source)) {
                Path file = this.source.resolve(path);
                if (Files.isRegularFile(file)) {
                    return Files.newInputStream(file);
                }
            }
            return null;
        }

        private Key parentKey(String namespace, String value) {
            if (value == null || value.isBlank()) {
                return null;
            }
            Key sameNamespace = textureKey(namespace, value);
            if (sameNamespace != null && this.readModel(sameNamespace).isPresent()) {
                return sameNamespace;
            }
            if (!value.contains(":")) {
                return textureKey(Key.MINECRAFT_NAMESPACE, value);
            }
            return sameNamespace;
        }

        @Override
        public void close() {
            if (this.zipFile != null) {
                try {
                    this.zipFile.close();
                } catch (IOException ex) {
                    this.context.logger().debug("Unable to close Polymer resource pack {}", this.source, ex);
                }
            }
        }

        private static ZipFile openZip(Path path) {
            if (path == null || !Files.isRegularFile(path) || !isArchive(path)) {
                return null;
            }
            try {
                return new ZipFile(path.toFile());
            } catch (IOException ex) {
                return null;
            }
        }

        private static Path firstRoot(PackPostProcessContext<ItemPackModule> context) {
            Iterator<Path> iterator = context.mod().roots().iterator();
            return iterator.hasNext() ? iterator.next() : null;
        }

        private static boolean isArchive(Path path) {
            Path fileNamePath = path.getFileName();
            if (fileNamePath == null) {
                return false;
            }
            String fileName = fileNamePath.toString().toLowerCase(Locale.ROOT);
            return fileName.endsWith(".zip") || fileName.endsWith(".jar");
        }

        private static boolean isSupportedVanillaTerminal(Key key, ModelKind modelKind) {
            if (modelKind == ModelKind.BLOCK) {
                return isSimpleBlockParent(key);
            }
            return key.namespace().equals(Key.MINECRAFT_NAMESPACE)
                    && (key.value().equals("item/generated") || key.value().equals("item/handheld"));
        }

        private static boolean isSimpleBlockParent(Key key) {
            return key.namespace().equals(Key.MINECRAFT_NAMESPACE)
                    && (key.value().equals("block/cube")
                    || key.value().startsWith("block/cube_")
                    || key.value().equals("block/orientable")
                    || key.value().startsWith("block/orientable_"));
        }

        private static Key firstLayer(Model model) {
            if (model == null || model.textures() == null || model.textures().layers() == null || model.textures().layers().isEmpty()) {
                return null;
            }
            for (ModelTexture layer : model.textures().layers()) {
                if (layer != null && layer.key() != null) {
                    return layer.key();
                }
            }
            return null;
        }

        private static Optional<String> firstTextureReference(Map<String, String> textures, ModelKind modelKind) {
            if (modelKind == ModelKind.BLOCK) {
                for (String key : List.of("all", "side", "top", "front", "north", "particle", "texture", "layer0", "layer1")) {
                    String value = textures.get(key);
                    if (value != null && !value.isBlank()) {
                        return Optional.of(value);
                    }
                }
                return textures.values().stream().filter(value -> value != null && !value.isBlank()).findFirst();
            }

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

        private static Optional<String> resolveTextureVariable(String reference, Map<String, String> textures, Set<String> seenVariables) {
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

        private static Key textureKey(String currentNamespace, String reference) {
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
            value = normalizedTextureValue(value);
            try {
                return Key.key(namespace, value);
            } catch (RuntimeException ex) {
                return null;
            }
        }
    }

    private enum ModelKind {
        ITEM,
        BLOCK
    }

    private record RawTextureResolution(PolymerTextureResolutionStatus status, Key textureKey, boolean simpleBlockParent, String reason) {
        private static RawTextureResolution resolved(Key textureKey) {
            return resolved(textureKey, false);
        }

        private static RawTextureResolution resolved(Key textureKey, boolean simpleBlockParent) {
            return new RawTextureResolution(PolymerTextureResolutionStatus.RESOLVED, textureKey, simpleBlockParent, "resolved");
        }

        private static RawTextureResolution failed(PolymerTextureResolutionStatus status, String reason) {
            return new RawTextureResolution(status, null, false, reason);
        }
    }

    private void onDefineCustomItems(PackEventContext<GeyserDefineCustomItemsEvent, ItemPackModule> context) {
        GeyserDefineCustomItemsEvent event = context.event();
        List<Item> items = context.registryValues(BuiltInRegistries.ITEM);

        DefaultedRegistry<Item> registry = BuiltInRegistries.ITEM;
        int registeredMappings = 0;
        int polymerGeneratedMappings = 0;
        int polymerCarrierMappings = 0;
        List<String> examples = new ArrayList<>(3);
        List<String> polymerExamples = new ArrayList<>(3);
        List<String> polymerCarrierExamples = new ArrayList<>(3);
        for (Item item : items) {
            Identifier itemLocation = registry.getKey(item);

            try {
                NonVanillaCustomItemDefinition.Builder customItemDefinition = NonVanillaCustomItemDefinition.builder(
                        org.geysermc.geyser.api.util.Identifier.of(itemLocation.toString()),
                        org.geysermc.geyser.api.util.Identifier.of(itemLocation.toString()),
                        registry.getId(item)
                )
                        .displayName("%" + item.getDescriptionId());

                CustomItemBedrockOptions.Builder customItemOptions = CustomItemBedrockOptions.builder()
                        .allowOffhand(true);

                // Allow minecraft namespace texture to be used (remapped as hydraulic)
                String icon = null;
                if (itemBuiltinTexture.containsKey(itemLocation.toString())) {
                    icon = itemBuiltinTexture.get(itemLocation.toString());
                    customItemOptions.icon(icon);
                }

                // Add the icon if it should have an icon
                boolean is2d = itemsWith2dIcon.contains(itemLocation);
                if (is2d) {
                    icon = itemLocation.toString();
                    customItemOptions.icon(icon);
                }

                String polymerTexturePath = context.hydraulic().getPackManager().generatedPolymerItemTexturePath(itemLocation);
                if (polymerTexturePath != null) {
                    icon = itemLocation.toString();
                    customItemOptions.icon(icon);
                    polymerGeneratedMappings++;
                    if (polymerExamples.size() < 3) {
                        polymerExamples.add(itemLocation + " -> texture=" + polymerTexturePath + " -> registered=true");
                    }

                    String polymerCarrierItem = polymerCarrierItem(context.mod().id(), item);
                    if (polymerCarrierItem != null) {
                        String polymerBedrockIdentifier = polymerBedrockIdentifier(itemLocation);
                        String polymerItemModel = itemLocation.toString();
                        String polymerStackId = itemLocation.toString();
                        String polymerCarrierPhase = "build";

                        try {
                            CustomItemBedrockOptions.Builder polymerCarrierOptions = CustomItemBedrockOptions.builder()
                                    .allowOffhand(true)
                                    .icon(icon);

                            if (handheldItems.contains(itemLocation)) {
                                polymerCarrierOptions.displayHandheld(true);
                            }

                            CustomItemDefinition.Builder polymerCarrierDefinition = CustomItemDefinition.builder(
                                    org.geysermc.geyser.api.util.Identifier.of(polymerBedrockIdentifier),
                                    org.geysermc.geyser.api.util.Identifier.of(polymerItemModel)
                            )
                                    .displayName("%" + item.getDescriptionId())
                                    .priority(1000);

                            polymerCarrierPhase = "component-conversion";
                            ComponentConverter.setGeyserComponents(
                                    item.components(),
                                    polymerCarrierDefinition,
                                    polymerCarrierOptions
                            );

                            polymerCarrierPhase = "definition-build";
                            polymerCarrierDefinition.bedrockOptions(polymerCarrierOptions);
                            CustomItemDefinition builtPolymerCarrierDefinition = polymerCarrierDefinition.build();

                            polymerCarrierPhase = "event-register";
                            event.register(
                                    org.geysermc.geyser.api.util.Identifier.of(polymerCarrierItem),
                                    builtPolymerCarrierDefinition
                            );
                            polymerCarrierMappings++;
                        } catch (Exception e) {
                            context.logger().error(
                                    "Unable to register Polymer carrier custom item definition: javaId={}, carrier={}, item_model={}, polymerStackId={}, bedrockIdentifier={}, icon={}, definitionType=CustomItemDefinition, phase={}, existingDefinitionsForCarrier={}, reason={}\n{}",
                                    itemLocation,
                                    polymerCarrierItem,
                                    polymerItemModel,
                                    polymerStackId,
                                    polymerBedrockIdentifier,
                                    icon,
                                    polymerCarrierPhase,
                                    event.customItemDefinitions()
                                            .getOrDefault(org.geysermc.geyser.api.util.Identifier.of(polymerCarrierItem), List.of())
                                            .size(),
                                    e.toString(),
                                    stackTraceToString(e)
                            );
                            continue;
                        }

                        if (polymerCarrierExamples.size() < 3) {
                            polymerCarrierExamples.add(
                                    "javaId=" + itemLocation
                                            + ", carrier=" + polymerCarrierItem
                                            + ", item_model=" + polymerItemModel
                                            + ", polymerStackId=" + polymerStackId
                                            + ", bedrockIdentifier=" + polymerBedrockIdentifier
                                            + ", icon=" + icon
                                            + ", definitionType=CustomItemDefinition"
                                            + ", registered=true"
                            );
                        }
                    }
                }

                // Make it handheld if need be
                if (handheldItems.contains(itemLocation)) {
                    customItemOptions.displayHandheld(true);
                }

                // Set the creative mappings
                CreativeMappings.setup(item, customItemOptions);

                // Set all bedrock components using what java components we have
                ComponentConverter.setGeyserComponents(
                        item.components(),
                        customItemDefinition,
                        customItemOptions
                );

                // Set the needed component for bows to work correctly
                if (item instanceof BowItem) {
                    customItemDefinition.component(
                            GeyserItemDataComponents.CHARGEABLE,
                            GeyserChargeable.builder()
                                    .maxDrawDuration(1f)
                                    .chargeOnDraw(false)
                    );

                    // Include the default icon, this won't change in the hotbar when used but this works the best for now
                    icon = itemLocation.toString();
                    customItemOptions.icon(icon);
                }

                // Set the needed component for crossbows to work correctly
                if (item instanceof CrossbowItem) {
                    customItemDefinition.component(
                            GeyserItemDataComponents.CHARGEABLE,
                            GeyserChargeable.builder()
                                    .maxDrawDuration(0f)
                                    .chargeOnDraw(true)
                    );

                    // Include the default icon, this won't change in the hotbar when used but this works the best for now
                    icon = itemLocation.toString();
                    customItemOptions.icon(icon);
                }

                if (item instanceof BlockItem blockItem) {
                    // Set the block_placer component to the correct block
                    // This fixes animations sometimes not showing
                    Block block = blockItem.getBlock();

                    customItemDefinition.component(
                            GeyserItemDataComponents.BLOCK_PLACER,
                            GeyserBlockPlacer.of(HydraulicKey.of(BuiltInRegistries.BLOCK.getKey(block)), !is2d)
                    );

                    CreativeMappings.setupBlock(block, customItemOptions);
                }

                customItemDefinition.bedrockOptions(customItemOptions);

                event.register(customItemDefinition.build());
                registeredMappings++;
                if (examples.size() < 3) {
                    examples.add(itemLocation + " icon=" + (icon == null ? "<default>" : icon));
                }
            } catch (Exception e) {
                context.logger().error("Unable to register {}:", itemLocation, e);
            }
        }

        if (registeredMappings > 0) {
            context.logger().info(
                    "Registered {} Geyser custom item mapping(s) for {}; polymerGeneratedPacksAvailable={}, syntheticPolymerPackContext={}, polymerGeneratedCustomItemDefinitions={}, polymerCarrierCustomItemDefinitions={}, examples={}, polymerExamples={}, polymerCarrierExamples={}",
                    registeredMappings,
                    context.mod().id(),
                    context.hydraulic().getPackManager().hasGeneratedPolymerPacks(),
                    context.mod().id().startsWith("polymer_resourcepack"),
                    polymerGeneratedMappings,
                    polymerCarrierMappings,
                    examples,
                    polymerExamples,
                    polymerCarrierExamples
            );
        }
    }

    private static String polymerCarrierItem(String modId, Item item) {
        if (!POLYDECORATIONS_MOD_ID.equals(modId)) {
            return null;
        }

        return item instanceof BlockItem ? POLYMER_BLOCK_ITEM_CARRIER : POLYMER_SIMPLE_ITEM_CARRIER;
    }

    private static String polymerBedrockIdentifier(Identifier itemLocation) {
        return "hydraulic:polymer_" + itemLocation.getNamespace() + "_" + itemLocation.getPath().replace('/', '_');
    }

    private static String stackTraceToString(Throwable throwable) {
        StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
