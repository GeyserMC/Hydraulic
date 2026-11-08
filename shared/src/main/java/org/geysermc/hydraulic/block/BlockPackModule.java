package org.geysermc.hydraulic.block;

import com.google.auto.service.AutoService;
import net.kyori.adventure.key.Key;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.geysermc.geyser.api.block.custom.CustomBlockData;
import org.geysermc.geyser.api.block.custom.CustomBlockPermutation;
import org.geysermc.geyser.api.block.custom.CustomBlockState;
import org.geysermc.geyser.api.block.custom.NonVanillaCustomBlockData;
import org.geysermc.geyser.api.block.custom.component.BoxComponent;
import org.geysermc.geyser.api.block.custom.component.CustomBlockComponents;
import org.geysermc.geyser.api.block.custom.component.GeometryComponent;
import org.geysermc.geyser.api.block.custom.component.MaterialInstance;
import org.geysermc.geyser.api.block.custom.component.TransformationComponent;
import org.geysermc.geyser.api.block.custom.nonvanilla.JavaBlockState;
import org.geysermc.geyser.api.block.custom.nonvanilla.JavaBoundingBox;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomBlocksEvent;
import org.geysermc.geyser.level.physics.PistonBehavior;
import org.geysermc.geyser.util.MathUtils;
import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.item.CreativeMappings;
import org.geysermc.hydraulic.pack.PackLogListener;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.context.PackContext;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.hydraulic.pack.context.PackPreProcessContext;
import org.geysermc.hydraulic.storage.ModStorage;
import org.geysermc.hydraulic.util.ImageUtil;
import org.geysermc.hydraulic.util.PackUtil;
import org.geysermc.hydraulic.util.SingletonBlockGetter;
import org.geysermc.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.pack.converter.type.model.ModelStitcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.blockstate.Condition;
import team.unnamed.creative.blockstate.MultiVariant;
import team.unnamed.creative.blockstate.Selector;
import team.unnamed.creative.blockstate.Variant;
import team.unnamed.creative.metadata.animation.AnimationMeta;
import team.unnamed.creative.model.Element;
import team.unnamed.creative.model.ElementFace;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.model.ModelTexture;
import team.unnamed.creative.model.ModelTextures;
import team.unnamed.creative.texture.Texture;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;

@AutoService(PackModule.class)
public class BlockPackModule extends PackModule<BlockPackModule> {
    private static final String STATE_CONDITION = "query.block_property('%s') == %s";
    // TODO: Use destructible_by_mining false for unbreakable blocks once Geyser's API supports it
    private static final float UNBREAKABLE_DESTROY_TIME = 1_000_000f;

    private final Map<String, StateDefinition> blockStates = new HashMap<>();
    private final Set<String> emptyModels = new HashSet<>();
    private final Set<String> bakedModels = new HashSet<>();
    private final Map<String, ImageUtil.Transparency> textureTransparency = new HashMap<>();

    public BlockPackModule() {
        this.listenOn(GeyserDefineCustomBlocksEvent.class, this::onDefineCustomBlocks);

        this.preProcess(this::preProcess);
        this.postProcess(this::postProcess);
    }

    private void preProcess(@NotNull PackPreProcessContext<BlockPackModule> context) {
        // Track which block textures use transparency, used to pick the render method
        for (Texture texture : context.assets(ResourcePack::textures)) {
            if (!texture.key().value().startsWith("block/")) {
                continue;
            }

            ImageUtil.Transparency transparency = ImageUtil.transparency(texture.data());
            if (transparency != ImageUtil.Transparency.OPAQUE) {
                textureTransparency.put(texture.key().toString().replace(".png", ""), transparency);
            }
        }

        for (var blockState : context.assets(ResourcePack::blockStates)) {
            this.blockStates.put(blockState.key().toString(), new StateDefinition(blockState, context.modelProvider()));
        }

        ModStorage storage = context.storage();
        if (storage.materials().materials().isEmpty()) {
            PackLogListener packLogListener = new PackLogListener(context.logger());

            Materials materials = new Materials();
            for (Model model : context.assets(ResourcePack::models)) {
                Model stitchedModel = new ModelStitcher(context.modelProvider(), model, packLogListener).stitch();
                if (stitchedModel == null) {
                    context.logger().warn("Could not find a stitched model for block {}", model.key());
                    continue;
                }

                Map<String, String> textures = new HashMap<>();
                Map<String, ModelTexture> modelTextures = getTextures(stitchedModel.textures());
                for (Map.Entry<String, ModelTexture> entry : modelTextures.entrySet()) {
                    ModelTexture modelTexture = getModelTexture(modelTextures, entry.getKey());
                    if (modelTexture == null || modelTexture.key() == null) {
                        // LOGGER.warn("Could not find a texture for key {} in model {}", entry.getKey(), model.key());
                        continue;
                    }

                    textures.put(entry.getKey(), modelTexture.key().toString());
                }

                Set<String> tinted = new HashSet<>();
                for (Element element : stitchedModel.elements()) {
                    for (ElementFace face : element.faces().values()) {
                        if (face.tintIndex() == ElementFace.DEFAULT_TINT_INDEX) {
                            continue;
                        }

                        // Remove the # from the texture name if it exists
                        String texture = face.texture();
                        texture = texture.startsWith("#") ? texture.substring(1) : texture;

                        markTinted(modelTextures, texture, tinted);
                    }
                }

                Materials.Material material = new Materials.Material(textures, tinted);
                materials.addMaterial(model.key().toString(), material);
            }

            storage.materials(materials);
            storage.save();
        }

        // Check for empty models
        List<Block> blocks = context.registryValues(BuiltInRegistries.BLOCK);
        DefaultedRegistry<Block> registry = BuiltInRegistries.BLOCK;
        for (Block block : blocks) {
            Identifier blockLocation = registry.getKey(block);
            for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                ModelDefinition definition = getModel(context, blockLocation, state);
                if (definition == null) {
                    continue;
                }

                Model model = definition.model();
                Key key = model.key();

                // Skip unit cube models
                if (isUnitCube(model.parent())) {
                    continue;
                }

                Model stitchedModel = new ModelStitcher(context.modelProvider(), model, new PackLogListener(context.logger())).stitch();

                // Track which uvlocked variants get baked geometry (generated in CustomModelConverter)
                Variant variant = definition.variant();
                if (UvLockBaker.needsBaking(variant) && UvLockBaker.canBake(stitchedModel)) {
                    bakedModels.add(UvLockBaker.bakedKey(key, variant.x(), variant.y()).toString());
                }

                // Check if the model is empty
                if (!stitchedModel.elements().isEmpty()) {
                    continue;
                }

                emptyModels.add(key.toString());
            }
        }
    }

    private void postProcess(@NotNull PackPostProcessContext<BlockPackModule> context) {
        ResourcePack assets = context.javaResourcePack();
        BedrockResourcePack bedrockPack = context.bedrockResourcePack();

        for (Texture texture : assets.textures()) {
            Key key = texture.key();
            String value = key.value();

            if (value.startsWith("block/")) {
                String cleanPath = value.replace("block/", "").replace(".png", "");

                String outputLoc = String.format(Constants.BEDROCK_TEXTURE_LOCATION, "blocks/" + context.mod().id() + "/" + cleanPath).replace(".png", "");
                String id = key.namespace() + ":" + cleanPath;
                bedrockPack.addBlockTexture(id, outputLoc);

                // If the texture is animated, add it to the flipbook textures
                if (texture.hasMetadata()) {
                    AnimationMeta animationMeta = texture.meta().meta(AnimationMeta.class);
                    if (animationMeta != null) {
                        bedrockPack.addFlipbookTexture(id, outputLoc, animationMeta.frameTime());
                    }
                }
            }
        }
    }

    @Override
    public boolean test(@NotNull PackPostProcessContext<BlockPackModule> context) {
        return !context.registryValues(BuiltInRegistries.BLOCK).isEmpty();
    }

    private void onDefineCustomBlocks(PackEventContext<GeyserDefineCustomBlocksEvent, BlockPackModule> context) {
        GeyserDefineCustomBlocksEvent event = context.event();
        List<Block> blocks = context.registryValues(BuiltInRegistries.BLOCK);

        DefaultedRegistry<Block> registry = BuiltInRegistries.BLOCK;
        for (Block block : blocks) {
            Identifier blockLocation = registry.getKey(block);
            CustomBlockData.Builder builder = NonVanillaCustomBlockData.builder()
                    .name(blockLocation.getPath())
                    .namespace(blockLocation.getNamespace())
                    .includedInCreativeInventory(true);

            CreativeMappings.setupBlock(block, builder);

            // Bedrock uses the first value of each property as the default state (which is
            // also what the item icon renders), so put the Java default value first
            BlockState defaultState = block.defaultBlockState();
            for (Property<?> property : block.getStateDefinition().getProperties()) {
                if (property instanceof IntegerProperty intProperty) {
                    builder.intProperty(property.getName(), defaultFirst(intProperty, defaultState));
                } else if (property instanceof BooleanProperty) {
                    builder.booleanProperty(property.getName());
                } else if (property instanceof EnumProperty<?> enumProperty) {
                    builder.stringProperty(enumProperty.getName(), defaultFirst(enumProperty, defaultState).stream()
                            .map(StringRepresentable::getSerializedName)
                            .toList());
                } else {
                    throw new IllegalArgumentException("Unknown property type: " + property.getClass().getName());
                }
            }

            List<CustomBlockPermutation> permutations = new ArrayList<>();
            CustomBlockComponents.Builder baseComponentBuilder = CustomBlockComponents.builder();
            for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                ModelDefinition definition = getModel(context, blockLocation, state);
                if (definition == null) {
                    continue;
                }

                Model model = definition.model();
                Key key = model.key();

                // Uvlocked variants use geometry with the rotation already baked in, as rotating
                // with the transformation would also rotate the textures. See UvLockBaker
                Variant variant = definition.variant();
                Key bakedKey = UvLockBaker.bakedKey(key, variant.x(), variant.y());
                boolean baked = !isUnitCube(model.parent()) && bakedModels.contains(bakedKey.toString());
                Key geometryKey = baked ? bakedKey : key;
                int rotX = baked ? 0 : variant.x();
                int rotY = baked ? 0 : variant.y();

                CustomBlockComponents.Builder componentsBuilder = CustomBlockComponents.builder()
                        .transformation(new TransformationComponent(
                            (360 - rotX) % 360, // Rotation X
                            (360 - rotY) % 360, // Rotation Y
                            0, // Rotation Z
                            1, // Scale X
                            1, // Scale Y
                            1, // Scale Z
                            0, // Translation X
                            0, // Translation Y
                            0 // Translation Z
                        ));

                if (!isUnitCube(model.parent())) {
                    String namespace = geometryKey.namespace();
                    String value = geometryKey.value();

                    String geoKey = value.substring(value.lastIndexOf('/') + 1);
                    String geoName = "geometry." + (namespace.equals(Key.MINECRAFT_NAMESPACE) ? "" : namespace + ".") + geoKey;

                    if (emptyModels.contains(key.toString())) {
                        context.logger().warn("Missing block model for block {}", blockLocation);
                        geoName = "geometry." + Constants.MOD_ID + ".empty";
                    }

                    componentsBuilder.geometry(GeometryComponent.builder()
                            .identifier(geoName)
                            .build());

                    VoxelShape shape = state.getShape(new SingletonBlockGetter(state), BlockPos.ZERO);
                    VoxelShape collisionShape = state.getCollisionShape(new SingletonBlockGetter(state), BlockPos.ZERO);

                    // Bedrock rotates the boxes with the transformation, so convert them in model space
                    componentsBuilder.selectionBox(createBoxComponent(shape, rotX, rotY));
                    componentsBuilder.collisionBoxes(createBoxComponent(collisionShape, rotX, rotY));
                } else {
                    componentsBuilder.geometry(GeometryComponent.builder()
                            .identifier("minecraft:geometry.full_block")
                            .build());
                }

                // Tint colours are registered client side, so guess based on the block type
                String tintMethod = block instanceof LeavesBlock ? "default_foliage" : "grass";

                Materials materials = context.storage().materials();
                Materials.Material material = materials.material(key.toString());
                String renderMethod = getRenderMethod(block, state, model, material);

                if (material != null) {
                    // Add a default texture, can be replaced by the below (I think)
                    Map.Entry<String, String> firstEntry = material.textures().entrySet().iterator().next();

                    String name = PackUtil.getTextureName(firstEntry.getValue());

                    componentsBuilder.materialInstance("*", createMaterialInstance(name, renderMethod, model, material.tinted(firstEntry.getKey()) ? tintMethod : null));

                    Map<String, String> faceMapping = getFaceMapping(model.parent());
                    if (!faceMapping.isEmpty()) {
                        for (Map.Entry<String, String> face : faceMapping.entrySet()) {
                            if (!material.textures().containsKey(face.getValue())) continue;

                            String textureName = PackUtil.getTextureName(material.textures().get(face.getValue()));

                            componentsBuilder.materialInstance(face.getKey(), createMaterialInstance(textureName, renderMethod, model, material.tinted(face.getValue()) ? tintMethod : null));
                        }
                    } else {
                        for (Map.Entry<String, String> entry : material.textures().entrySet()) {
                            String materialKey = entry.getKey();

                            // Bedrock uses "*" for the particle texture
                            if ("particle".equals(materialKey)) {
                                materialKey = "*";
                            }

                            componentsBuilder.materialInstance(materialKey, createMaterialInstance(PackUtil.getTextureName(entry.getValue()), renderMethod, model, material.tinted(entry.getKey()) ? tintMethod : null));
                        }
                    }
                } else {
                    componentsBuilder.materialInstance("*", createMaterialInstance(PackUtil.getTextureName(key.toString()), renderMethod, model, null));
                    context.logger().warn("Could not find material for block {}", key);
                }

                // No properties exist on this state, so there's only one
                // blockstate that can exist. Update the base builder so that
                // the code that creates the component for the base block
                // persists everything we did above
                if (state.getProperties().isEmpty()) {
                    baseComponentBuilder = componentsBuilder;
                    continue;
                }

                List<String> conditions = new ArrayList<>();
                for (Property<?> property : state.getProperties()) {
                    String propValue = state.getValue(property).toString();
                    if (property instanceof EnumProperty<?>) {
                        propValue = "'" + propValue.toLowerCase() + "'";
                    }

                    conditions.add(String.format(STATE_CONDITION, property.getName(), propValue));
                }

                String condition = String.join(" && ", conditions);
                permutations.add(new CustomBlockPermutation(componentsBuilder.build(), condition));
            }

            builder.permutations(permutations);

            VoxelShape shape = defaultState.getShape(new SingletonBlockGetter(defaultState), BlockPos.ZERO);
            VoxelShape collisionShape = defaultState.getCollisionShape(new SingletonBlockGetter(defaultState), BlockPos.ZERO);

            CustomBlockComponents.Builder componentsBuilder = baseComponentBuilder
                    .displayName("%" + block.getDescriptionId())
                    .friction(Math.min(1 - block.getFriction(), 0.9f))
                    .destructibleByMining(block.defaultDestroyTime() < 0 ? UNBREAKABLE_DESTROY_TIME : block.defaultDestroyTime()) // TODO: Check
                    // .unitCube(true) // TODO: Geometry conversion
                    .selectionBox(createBoxComponent(shape))
                    .collisionBoxes(createBoxComponent(collisionShape));

            builder.components(componentsBuilder.build());

            CustomBlockData blockData = builder.build();
            try {
                event.register(blockData);
            } catch (IllegalArgumentException e) {
                context.logger().error("Failed to register block {}: {}", blockLocation, e.getMessage());
                continue;
            }

            int blockId = registry.getId(block);
            for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                CustomBlockState.Builder stateBuilder = blockData.blockStateBuilder();
                for (Property<?> property : state.getProperties()) {
                    if (property instanceof IntegerProperty intProperty) {
                        stateBuilder.intProperty(property.getName(), state.getValue(intProperty));
                    } else if (property instanceof BooleanProperty booleanProperty) {
                        stateBuilder.booleanProperty(property.getName(), state.getValue(booleanProperty));
                    } else if (property instanceof EnumProperty<?> enumProperty) {
                        stateBuilder.stringProperty(enumProperty.getName(), state.getValue(enumProperty).getSerializedName());
                    } else {
                        throw new IllegalArgumentException("Unknown property type: " + property.getClass().getName());
                    }
                }

                PistonBehavior pistonBehavior = switch (state.getPistonPushReaction()) {
                    case PUSH_PULL -> PistonBehavior.PUSH_PULL;
                    case PUSH -> PistonBehavior.PUSH;
                    case POPPED -> PistonBehavior.POPPED;
                    case IMMOVEABLE -> PistonBehavior.IMMOVEABLE;
                    case IGNORE_ENTITY -> PistonBehavior.IGNORE_ENTITY;
                };

                CustomBlockState customBlockState = stateBuilder.build();
                JavaBlockState.Builder javaBlockStateBuilder = JavaBlockState.builder()
                        .identifier(BlockStateParser.serialize(state))
                        .javaId(Block.getId(state))
                        .blockHardness(block.defaultDestroyTime()) // TODO: Check
                        .canBreakWithHand(!state.requiresCorrectToolForDrops())
                        .waterlogged(state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED))
                        .stateGroupId(blockId)
                        .pistonBehavior(pistonBehavior.name());

                /*
                List<AABB> aabbs = collisionShape.toAabbs();
                JavaBoundingBox[] bbs = new JavaBoundingBox[aabbs.size()];
                for (int i = 0; i < aabbs.size(); i++) {
                    AABB aabb = aabbs.get(i);
                    bbs[i] = new JavaBoundingBox(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
                }

                javaBlockStateBuilder.collision(bbs);
                 */
                javaBlockStateBuilder.collision(new JavaBoundingBox[0]); // TODO

                event.registerOverride(javaBlockStateBuilder.build(), customBlockState);
            }
        }
    }

    @Nullable
    private ModelDefinition getModel(@NotNull PackContext<?> context, @NotNull Identifier blockLocation, @NotNull BlockState state) {
        StateDefinition definition = this.blockStates.get(blockLocation.toString());
        if (definition == null) {
            context.logger().warn("Missing blockstate for block {}", blockLocation);
            return null;
        }

        team.unnamed.creative.blockstate.BlockState packState = definition.state();

        // Check if we have a variant match
        MultiVariant multiVariant = matchState(state, packState.variants());
        if (multiVariant == null || multiVariant.variants().isEmpty()) {
            // No variant, check if we have a default
            multiVariant = packState.variants().get("");
        }

        // Try and match the state
        // TODO Handle multiple variants since we only take the first match
        //      Will likely need to generate more geometry files and then alter bone visibility for each part
        if (multiVariant == null) {
            for (Selector selector : packState.multipart()) {
                // Ignore none conditions
                if (selector.condition() == Condition.NONE) {
                    continue;
                }

                List<Condition> conditions = new ArrayList<>();
                BiFunction<Boolean, Boolean, Boolean> comparator = (a, b) -> false;
                if (selector.condition() instanceof Condition.And andCondition) {
                    conditions.addAll(andCondition.conditions());
                    comparator = Boolean::logicalAnd;
                } else if (selector.condition() instanceof Condition.Or orCondition) {
                    conditions.addAll(orCondition.conditions());
                    comparator = Boolean::logicalOr;
                } else if (selector.condition() instanceof Condition.Match) {
                    conditions.add(selector.condition());
                }

                boolean first = true;
                boolean result = true;
                for (Condition condition : conditions) {
                    if (!(condition instanceof Condition.Match match)) {
                        context.logger().warn("Non match condition found in {}", blockLocation);
                        continue;
                    }

                    Property<?> foundProperty = null;
                    for (Property<?> property : state.getProperties()) {
                        if (property.getName().equals(match.key())) {
                            foundProperty = property;
                            break;
                        }
                    }

                    if (foundProperty == null) {
                        result = false;
                        continue;
                    }

                    boolean test = state.getValue(foundProperty).toString().equals(match.value().toString());
                    if (!first) {
                        result = comparator.apply(result, test);
                    } else {
                        result = test;
                        first = false;
                    }
                }

                if (result) {
                    multiVariant = selector.variant();
                    break;
                }
            }
        }

        // Get the default multipart variant if we have no match
        if (multiVariant == null) {
            Optional<Selector> selector = packState.multipart().stream().filter(multipart -> multipart.condition() == Condition.NONE).findFirst();
            if (selector.isPresent()) {
                multiVariant = selector.get().variant();
            }

            // LOGGER.warn("Missing multipart state conversion for block {} {}", blockLocation, state);
        }

        // We have a match! Now we need to find the model
        if (multiVariant != null && !multiVariant.variants().isEmpty()) {
            // TODO: Handle multiple variants?
            Variant variant = multiVariant.variants().get(0);
            Key modelKey = variant.model();

            Model model = definition.modelProvider().model(modelKey);
            if (model == null) {
                context.logger().warn("Missing model {} for block {}", modelKey, blockLocation);
            } else {
                return new ModelDefinition(model, variant);
            }
        }

        return null;
    }

    private static MultiVariant matchState(@NotNull BlockState state, @NotNull Map<String, MultiVariant> variants) {
        List<String> properties = new ArrayList<>();
        for (Property<?> property : state.getProperties()) {
            properties.add(property.getName() + "=" + state.getValue(property).toString().toLowerCase());
        }

        for (Map.Entry<String, MultiVariant> entry : variants.entrySet()) {
            String variant = entry.getKey();

            String[] property = variant.split(",");
            boolean match = true;
            for (String prop : property) {
                if (!properties.contains(prop)) {
                    match = false;
                    break;
                }
            }

            if (match) {
                return entry.getValue();
            }
        }

        return null;
    }

    /**
     * Gets the Bedrock render method for a block state from its most transparent texture.
     * @see <a href="https://wiki.bedrock.dev/blocks/block-visuals-intro#render-methods">Render methods</a>
     *
     * @param block the block
     * @param state the block state
     * @param model the model for the block state
     * @param material the material for the model, if any
     * @return the render method
     */
    private String getRenderMethod(@NotNull Block block, @NotNull BlockState state, @NotNull Model model, @Nullable Materials.Material material) {
        ImageUtil.Transparency transparency = ImageUtil.Transparency.OPAQUE;

        if (material != null) {
            transparency = material.textures().values().stream()
                .map(texture -> textureTransparency.getOrDefault(texture, ImageUtil.Transparency.OPAQUE))
                .max(Comparator.naturalOrder())
                .orElse(ImageUtil.Transparency.OPAQUE);
        }

        if (transparency == ImageUtil.Transparency.TRANSLUCENT) {
            return "blend";
        }

        if (block instanceof LeavesBlock) {
            return "alpha_test_to_opaque";
        }

        if (transparency == ImageUtil.Transparency.CUTOUT) {
            return state.canOcclude() ? "alpha_test_single_sided_to_opaque" : "alpha_test_single_sided"; // E.g. overlays, or plants and doors
        }

        String parent = model.parent() == null ? null : model.parent().value();
        boolean cross = "block/cross".equals(parent) || "block/tinted_cross".equals(parent);
        return cross ? "alpha_test_single_sided" : "opaque";
    }

    private static MaterialInstance createMaterialInstance(@NotNull String texture, @NotNull String renderMethod, @NotNull Model model, @Nullable String tintMethod) {
        return MaterialInstance.builder()
                .texture(texture)
                .renderMethod(renderMethod)
                .faceDimming(true)
                .ambientOcclusionExponent(model.ambientOcclusion() ? 1.0f : 0.0f)
                .tintMethod(tintMethod)
                .build();
    }

    private static void markTinted(@NotNull Map<String, ModelTexture> textures, @NotNull String key, @NotNull Set<String> tinted) {
        if (!tinted.add(key)) {
            return;
        }

        // Texture references the value of another texture
        ModelTexture value = textures.get(key);
        if (value != null && value.reference() != null) {
            markTinted(textures, value.reference(), tinted);
        }
    }

    @Nullable
    private static ModelTexture getModelTexture(@NotNull Map<String, ModelTexture> textures, @NotNull String key) {
        return getModelTexture(textures, key, new HashSet<>());
    }

    @Nullable
    private static ModelTexture getModelTexture(@NotNull Map<String, ModelTexture> textures, @NotNull String key, @NotNull Set<String> visited) {
        if (!visited.add(key)) {
            return null;
        }

        // Texture references the value of another texture
        ModelTexture value = textures.get(key);
        if (value != null && value.reference() != null) {
            return getModelTexture(textures, value.reference(), visited);
        }

        return value;
    }

    private static Map<String, ModelTexture> getTextures(@NotNull ModelTextures modelTextures) {
        Map<String, ModelTexture> textures = new HashMap<>(modelTextures.variables());
        textures.put("particle", modelTextures.particle());
        for (int i = 0; i < modelTextures.layers().size(); i++) {
            textures.put("layer" + i, modelTextures.layers().get(i));
        }

        return textures;
    }

    private boolean isUnitCube(Key parent) {
        if (parent == null) {
            return false;
        }
        return parent.namespace().equals("minecraft") && (parent.value().startsWith("block/cube") || parent.value().startsWith("block/orientable"));
    }

    /**
     * Get the face mapping for the given parent model.
     * This is due to some cube models having texture names bedrock doesn't understand.
     *
     * @param parent The parent model
     * @return The face mapping if any
     */
    private Map<String, String> getFaceMapping(Key parent) {
        // Destination <- Source
        Map<String, String> mapping = new HashMap<>();
//        {{
//            put("*", "particle");
//            put("up", "up");
//            put("down", "down");
//            put("north", "north");
//            put("south", "south");
//            put("west", "west");
//            put("east", "east");
//        }};

        // No parent, so return empty
        if (parent == null) {
            return mapping;
        }

        if ("block/cube_all".equals(parent.value())) {
            mapping.put("*", "all");
        } else if ("block/cube_bottom_top".equals(parent.value())) {
            mapping.put("*", "side");
            mapping.put("up", "top");
            mapping.put("down", "bottom");
            mapping.put("north", "side");
            mapping.put("south", "side");
            mapping.put("west", "side");
            mapping.put("east", "side");
        } else if ("block/cube_column".equals(parent.value())) {
            mapping.put("*", "side");
            mapping.put("up", "end");
            mapping.put("down", "end");
            mapping.put("north", "side");
            mapping.put("south", "side");
            mapping.put("west", "side");
            mapping.put("east", "side");
        }

        return mapping;
    }

    private static BoxComponent createBoxComponent(VoxelShape shape) {
        return createBoxComponent(shape, 0, 0);
    }

    /**
     * Creates a box component from a Java shape, undoing the given variant rotation
     * since Bedrock re-applies it through the transformation component.
     */
    private static BoxComponent createBoxComponent(VoxelShape shape, int rotX, int rotY) {
        if (shape.isEmpty()) {
            return BoxComponent.emptyBox();
        }

        float minX = 5;
        float minY = 5;
        float minZ = 5;
        float maxX = -5;
        float maxY = -5;
        float maxZ = -5;
        for (AABB worldBox : shape.toAabbs()) {
            AABB boundingBox = unrotate(worldBox, rotX, rotY);
            double offsetX = boundingBox.getXsize() * 0.5;
            double offsetY = boundingBox.getYsize() * 0.5;
            double offsetZ = boundingBox.getZsize() * 0.5;

            Vec3 center = boundingBox.getCenter();

            minX = Math.min(minX, (float) (center.x() - offsetX));
            minY = Math.min(minY, (float) (center.y() - offsetY));
            minZ = Math.min(minZ, (float) (center.z() - offsetZ));

            maxX = Math.max(maxX, (float) (center.x() + offsetX));
            maxY = Math.max(maxY, (float) (center.y() + offsetY));
            maxZ = Math.max(maxZ, (float) (center.z() + offsetZ));
        }
        minX = MathUtils.clamp(minX, 0, 1);
        minY = MathUtils.clamp(minY, 0, 1);
        minZ = MathUtils.clamp(minZ, 0, 1);
        maxX = MathUtils.clamp(maxX, 0, 1);
        maxY = MathUtils.clamp(maxY, 0, 1);
        maxZ = MathUtils.clamp(maxZ, 0, 1);

        return new BoxComponent(
                16 * (1 - maxX) - 8, // For some odd reason X is mirrored on Bedrock
                16 * minY,
                16 * minZ - 8,
                16 * (maxX - minX),
                16 * (maxY - minY),
                16 * (maxZ - minZ)
        );
    }

    private static <T extends Comparable<T>> List<T> defaultFirst(Property<T> property, BlockState state) {
        T defaultValue = state.getValue(property);
        List<T> values = new ArrayList<>(property.getPossibleValues());
        values.remove(defaultValue);
        values.addFirst(defaultValue);
        return values;
    }

    /**
     * Applies the inverse of a Java blockstate variant rotation to a box.
     * Java applies X then Y, so we undo Y first, then X.
     */
    private static AABB unrotate(AABB box, int rotX, int rotY) {
        int yTurns = Math.floorMod(rotY / 90, 4);
        for (int i = 0; i < yTurns; i++) {
            // Inverse of Java's Y+90 (x, z) -> (1 - z, x)
            box = new AABB(box.minZ, box.minY, 1 - box.maxX, box.maxZ, box.maxY, 1 - box.minX);
        }

        int xTurns = Math.floorMod(rotX / 90, 4);
        for (int i = 0; i < xTurns; i++) {
            // Inverse of Java's X+90 (y, z) -> (z, 1 - y)
            box = new AABB(box.minX, 1 - box.maxZ, box.minY, box.maxX, 1 - box.minZ, box.maxY);
        }

        return box;
    }
}
