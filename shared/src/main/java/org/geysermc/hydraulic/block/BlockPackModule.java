package org.geysermc.hydraulic.block;

import com.google.auto.service.AutoService;
import net.kyori.adventure.key.Key;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
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
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.item.CreativeMappings;
import org.geysermc.hydraulic.pack.PackLogListener;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.context.PackContext;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.hydraulic.pack.context.PackPreProcessContext;
import org.geysermc.hydraulic.storage.ModStorage;
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

    private final Map<String, StateDefinition> blockStates = new HashMap<>();
    private final Set<String> emptyModels = new HashSet<>();

    public BlockPackModule() {
        this.listenOn(GeyserDefineCustomBlocksEvent.class, this::onDefineCustomBlocks);

        this.preProcess(this::preProcess);
        this.postProcess(this::postProcess);
    }

    private void preProcess(@NotNull PackPreProcessContext<BlockPackModule> context) {
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

                Materials.Material material = new Materials.Material(textures);
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

                // Check if the model is empty
                Model stitchedModel = new ModelStitcher(context.modelProvider(), model, new PackLogListener(context.logger())).stitch();
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

                String outputLoc = PackUtil.limitPathLength(String.format(Constants.BEDROCK_TEXTURE_LOCATION, "blocks/" + context.mod().id() + "/" + cleanPath).replace(".png", ""), 75);
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

            for (Property<?> property : block.getStateDefinition().getProperties()) {
                if (property instanceof IntegerProperty intProperty) {
                    builder.intProperty(property.getName(), List.copyOf(intProperty.getPossibleValues()));
                } else if (property instanceof BooleanProperty) {
                    builder.booleanProperty(property.getName());
                } else if (property instanceof EnumProperty<?> enumProperty) {
                    builder.stringProperty(enumProperty.getName(), enumProperty.getPossibleValues().stream().map(StringRepresentable::getSerializedName).toList());
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

                CustomBlockComponents.Builder componentsBuilder = CustomBlockComponents.builder()
                        .transformation(new TransformationComponent(
                            (360 - definition.variant().x()) % 360, // Rotation X
                            (360 - definition.variant().y()) % 360, // Rotation Y
                            0, // Rotation Z
                            1, // Scale X
                            1, // Scale Y
                            1, // Scale Z
                            0, // Translation X
                            0, // Translation Y
                            0 // Translation Z
                        ));

                if (!isUnitCube(model.parent())) {
                    String namespace = key.namespace();
                    String value = key.value();

                    String geoKey = value.substring(value.lastIndexOf('/') + 1);
                    String geoName = "geometry." + (namespace.equals(Key.MINECRAFT_NAMESPACE) ? "" : namespace + ".") + geoKey;

                    if (emptyModels.contains(key.toString())) {
                        context.logger().warn("Missing block model for block {}", blockLocation);
                        geoName = "geometry." + Constants.MOD_ID + ".empty";
                    }

                    componentsBuilder.geometry(GeometryComponent.builder()
                            .identifier(geoName)
                            .build());

                    // Bedrock rotates the rendered model by the transformation component above,
                    // and takes the collision/selection box with it. Rotate the Java shape back
                    // so the box matches the rotated model.
                    VoxelShape shape = state.getShape(new SingletonBlockGetter(state), BlockPos.ZERO);
                    VoxelShape collisionShape = state.getCollisionShape(new SingletonBlockGetter(state), BlockPos.ZERO);
                    int rx = definition.variant().x();
                    int ry = definition.variant().y();
                    if (rx != 0 || ry != 0) {
                        shape = rotateShape(shape, rx, ry);
                        collisionShape = rotateShape(collisionShape, rx, ry);
                    }

                    componentsBuilder.selectionBox(createBoxComponent(shape));
                    componentsBuilder.collisionBox(createBoxComponent(collisionShape));
                } else {
                    componentsBuilder.geometry(GeometryComponent.builder()
                            .identifier("minecraft:geometry.full_block")
                            .build());
                }

                // Work out the render method from the model parent. Cross models (flowers etc.)
                // and cutout-style models need alpha testing, everything else follows occlusion.
                // https://wiki.bedrock.dev/blocks/block-components.html#render-methods
                String renderMethod = state.canOcclude() ? "opaque" : "blend";
                if (model.parent() != null) {
                    String parent = model.parent().value();
                    if (parent.equals("block/cross") || parent.equals("block/tinted_cross")
                            || parent.equals("block/crop") || parent.equals("block/template_orientable_trapdoor")
                            || parent.endsWith("_cross") || parent.contains("/cross")) {
                        renderMethod = "alpha_test_single_sided";
                    } else if (parent.equals("block/template_glazed_terracotta") || parent.contains("glass")
                            || parent.contains("ice") || parent.contains("leaves") || parent.contains("carpet")) {
                        // Semi-transparent blocks render best with blend
                        renderMethod = "blend";
                    }
                }

                Materials materials = context.storage().materials();
                Materials.Material material = materials.material(key.toString());
                if (material != null) {
                    // Add a default texture, can be replaced by the below (I think)
                    Map.Entry<String, String> firstEntry = material.textures().entrySet().iterator().next();

                    String name = PackUtil.getTextureName(firstEntry.getValue());

                    componentsBuilder.materialInstance("*", MaterialInstance.builder()
                            .texture(name)
                            .renderMethod(renderMethod)
                            .faceDimming(true)
                            .ambientOcclusion(model.ambientOcclusion())
                            .build());

                    Map<String, String> faceMapping = getFaceMapping(model.parent());
                    if (!faceMapping.isEmpty()) {
                        for (Map.Entry<String, String> face : faceMapping.entrySet()) {
                            if (!material.textures().containsKey(face.getValue())) continue;

                            String textureName = PackUtil.getTextureName(material.textures().get(face.getValue()));

                            componentsBuilder.materialInstance(face.getKey(), MaterialInstance.builder()
                                    .texture(textureName)
                                    .renderMethod(renderMethod)
                                    .faceDimming(true)
                                    .ambientOcclusion(model.ambientOcclusion())
                                    .build());
                        }
                    } else {
                        for (Map.Entry<String, String> entry : material.textures().entrySet()) {
                            String materialKey = entry.getKey();

                            // Bedrock uses "*" for the particle texture
                            if ("particle".equals(materialKey)) {
                                materialKey = "*";
                            }

                            componentsBuilder.materialInstance(materialKey, MaterialInstance.builder()
                                    .texture(resolveTextureName(context, entry.getValue()))
                                    .renderMethod(renderMethod)
                                    .faceDimming(true)
                                    .ambientOcclusion(model.ambientOcclusion())
                                    .build());
                        }
                    }
                } else {
                    componentsBuilder.materialInstance("*", MaterialInstance.builder()
                            .texture(resolveTextureName(context, key.toString()))
                            .renderMethod(renderMethod)
                            .faceDimming(true)
                            .ambientOcclusion(model.ambientOcclusion())
                            .build());
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

            BlockState defaultState = block.defaultBlockState();
            VoxelShape shape = defaultState.getShape(new SingletonBlockGetter(defaultState), BlockPos.ZERO);
            VoxelShape collisionShape = defaultState.getCollisionShape(new SingletonBlockGetter(defaultState), BlockPos.ZERO);

            CustomBlockComponents.Builder componentsBuilder = baseComponentBuilder
                    .displayName("%" + block.getDescriptionId())
                    .friction(Math.min(1 - block.getFriction(), 0.9f))
                    .destructibleByMining(Math.max(0, block.defaultDestroyTime())) // Bedrock requires non-negative; bedrock-like blocks report -1
                    // Unit cube models (full blocks) render with Bedrock's built-in cube, skipping
                    // geometry conversion; per-face textures are applied via material_instances above.
                    .selectionBox(createBoxComponent(shape))
                    .collisionBox(createBoxComponent(collisionShape));

            // Full blocks don't need a converted geometry; use Bedrock's built-in cube rendering.
            ModelDefinition defaultDefinition = getModel(context, blockLocation, defaultState);
            if (defaultDefinition != null && isUnitCube(defaultDefinition.model().parent())) {
                componentsBuilder.unitCube(true);
            }

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
                    case BLOCK -> PistonBehavior.BLOCK;
                    case DESTROY -> PistonBehavior.DESTROY;
                    case PUSH_ONLY -> PistonBehavior.PUSH_ONLY;
                    default -> PistonBehavior.NORMAL;
                };

                CustomBlockState customBlockState = stateBuilder.build();
                JavaBlockState.Builder javaBlockStateBuilder = JavaBlockState.builder()
                        .identifier(BlockStateParser.serialize(state))
                        .javaId(Block.getId(state))
                        .blockHardness(Math.max(0, block.defaultDestroyTime())) // Bedrock requires non-negative; bedrock-like blocks report -1
                        .canBreakWithHand(!state.requiresCorrectToolForDrops())
                        .waterlogged(state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED))
                        .stateGroupId(blockId)
                        .pistonBehavior(pistonBehavior.name());

                // Bedrock block items are registered as "<block>_item" by Geyser; the Java block id
                // used as pick item is resolved to the bedrock item id by Geyser's item registry.
                try {
                    ItemStack pickItem = state.getCloneItemStack(HydraulicImpl.instance().server().overworld(), BlockPos.ZERO, false);
                    String itemId = BuiltInRegistries.ITEM.getKey(pickItem.getItem()).toString();

                    // If the method is annotated with `@Environment(EnvType.CLIENT)` then we get air back, so lets ignore that
                    if (!itemId.equals("minecraft:air")) {
                        javaBlockStateBuilder.pickItem(itemId);
                    }
                } catch (Exception e) {
                    context.logger().warn("Failed to get pick item for block {}: {}", blockLocation, e.getMessage());
                }

                // Send the actual collision shape so open doors/trapdoors don't stay solid on Bedrock (#70)
                VoxelShape stateCollisionShape = state.getCollisionShape(new SingletonBlockGetter(state), BlockPos.ZERO);
                List<AABB> aabbs = stateCollisionShape.toAabbs();
                JavaBoundingBox[] bbs = new JavaBoundingBox[aabbs.size()];
                for (int i = 0; i < aabbs.size(); i++) {
                    AABB aabb = aabbs.get(i);
                    bbs[i] = new JavaBoundingBox(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
                }

                javaBlockStateBuilder.collision(bbs);

                event.registerOverride(javaBlockStateBuilder.build(), customBlockState);
            }
        }
    }

    /**
     * Resolves a texture name for Bedrock. If a mod references a "minecraft:" texture that it
     * actually ships itself (bad model references), use the mod's own texture instead.
     */
    private static String resolveTextureName(@NotNull PackContext<?> context, @NotNull String modelName) {
        if (modelName.startsWith(Key.MINECRAFT_NAMESPACE)) {
            String value = modelName.substring(modelName.indexOf(':') + 1);
            // The mod's textures live under assets/<mod>/textures/<value>.png; a mod may reference
            // a "minecraft:" texture it actually ships itself (bad model references)
            if (context.mod().resolveFile("assets/" + context.mod().namespace() + "/textures/" + value + ".png") != null) {
                return value;
            }
        }
        return PackUtil.getTextureName(modelName);
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
        // Multiple variants are resolved below by highest weight; full support (multiple geometry
        // files with bone visibility switching) is a large feature with limited Bedrock benefit
        // since Java selects random variants by weight, which Bedrock custom blocks cannot do.
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
            // Bedrock custom blocks can't do Java's weight-based random model selection, so pick
            // the variant with the highest weight as the closest deterministic match. Variants
            // with equal weights keep the first entry's model (Java picks one at random, any is fine).
            Variant variant = multiVariant.variants().stream()
                    .max(Comparator.comparingInt(Variant::weight))
                    .orElse(multiVariant.variants().get(0));
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

    /**
     * Rotates a voxel shape around the block center (0.5, 0.5, 0.5), Y axis first then X axis,
     * by the given angles in degrees. Used to counteract Bedrock rotating the collision box
     * along with the block model.
     */
    private static VoxelShape rotateShape(VoxelShape shape, int rx, int ry) {
        if (rx == 0 && ry == 0) {
            return shape;
        }

        double cosY = Math.cos(Math.toRadians(ry));
        double sinY = Math.sin(Math.toRadians(ry));
        double cosX = Math.cos(Math.toRadians(rx));
        double sinX = Math.sin(Math.toRadians(rx));

        VoxelShape result = Shapes.empty();
        for (AABB box : shape.toAabbs()) {
            // Rotate around Y axis
            double minX = 0.5 + (box.minX - 0.5) * cosY - (box.minZ - 0.5) * sinY;
            double minZ = 0.5 + (box.minX - 0.5) * sinY + (box.minZ - 0.5) * cosY;
            double maxX = 0.5 + (box.maxX - 0.5) * cosY - (box.maxZ - 0.5) * sinY;
            double maxZ = 0.5 + (box.maxX - 0.5) * sinY + (box.maxZ - 0.5) * cosY;

            // Rotate around X axis
            double minY = 0.5 + (box.minY - 0.5) * cosX - (minZ - 0.5) * sinX;
            double rotZ1 = 0.5 + (box.minY - 0.5) * sinX + (minZ - 0.5) * cosX;
            double maxY = 0.5 + (box.maxY - 0.5) * cosX - (maxZ - 0.5) * sinX;
            double rotZ2 = 0.5 + (box.maxY - 0.5) * sinX + (maxZ - 0.5) * cosX;

            result = Shapes.or(result, Shapes.create(minX, minY, Math.min(rotZ1, rotZ2), maxX, maxY, Math.max(rotZ1, rotZ2)));
        }
        return result;
    }

    private static BoxComponent createBoxComponent(VoxelShape shape) {
        if (shape.isEmpty()) {
            return BoxComponent.emptyBox();
        }

        float minX = 5;
        float minY = 5;
        float minZ = 5;
        float maxX = -5;
        float maxY = -5;
        float maxZ = -5;
        for (AABB boundingBox : shape.toAabbs()) {
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
}
