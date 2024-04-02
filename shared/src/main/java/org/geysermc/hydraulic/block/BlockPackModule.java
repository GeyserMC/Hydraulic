package org.geysermc.hydraulic.block;

import com.google.auto.service.AutoService;
import net.kyori.adventure.key.Key;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
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
import org.geysermc.geyser.api.util.CreativeCategory;
import org.geysermc.geyser.level.physics.PistonBehavior;
import org.geysermc.geyser.util.MathUtils;
import org.geysermc.hydraulic.item.CreativeMappings;
import org.geysermc.hydraulic.pack.ConvertablePackModule;
import org.geysermc.hydraulic.pack.PackLogListener;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.context.PackContext;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.hydraulic.pack.context.PackPreProcessContext;
import org.geysermc.hydraulic.storage.ModStorage;
import org.geysermc.hydraulic.util.Constants;
import org.geysermc.hydraulic.util.PackUtil;
import org.geysermc.hydraulic.util.SingletonBlockGetter;
import org.geysermc.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.pack.converter.converter.model.ModelStitcher;
import org.geysermc.pack.converter.data.ModelConversionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.blockstate.Condition;
import team.unnamed.creative.blockstate.MultiVariant;
import team.unnamed.creative.blockstate.Selector;
import team.unnamed.creative.blockstate.Variant;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.model.ModelTexture;
import team.unnamed.creative.model.ModelTextures;
import team.unnamed.creative.texture.Texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AutoService(PackModule.class)
public class BlockPackModule extends ConvertablePackModule<BlockPackModule, ModelConversionData> {
    private static final String STATE_CONDITION = "query.block_property('%s') == %s";

    private final Map<String, StateDefinition> blockStates = new HashMap<>();

    public BlockPackModule() {
        super(ModelConversionData.class);

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
                bedrockPack.addBlockTexture(key.namespace() + ":" + cleanPath, outputLoc);
            }
        }
    }

    @Override
    public boolean test(@NotNull PackPostProcessContext<BlockPackModule> context) {
        return !context.registryValues(Registries.BLOCK).isEmpty();
    }

    private void onDefineCustomBlocks(PackEventContext<GeyserDefineCustomBlocksEvent, BlockPackModule> context) {
        GeyserDefineCustomBlocksEvent event = context.event();
        List<Block> blocks = context.registryValues(Registries.BLOCK);

        DefaultedRegistry<Block> registry = BuiltInRegistries.BLOCK;
        for (Block block : blocks) {
            ResourceLocation blockLocation = registry.getKey(block);
            CustomBlockData.Builder builder = NonVanillaCustomBlockData.builder()
                    .name(blockLocation.getPath())
                    .namespace(blockLocation.getNamespace())
                    .includedInCreativeInventory(true)
                    .creativeGroup("itemGroup.name.items")
                    .creativeCategory(CreativeCategory.ITEMS);

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
                                definition.variant().x(), // Rotation X
                                definition.variant().y(), // Rotation Y
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

                    String fileName = value.substring(value.lastIndexOf('/') + 1);
                    String geoName = (namespace.equals(Key.MINECRAFT_NAMESPACE) ? "" : namespace + ".") + fileName;

                    componentsBuilder.geometry(GeometryComponent.builder()
                            .identifier("geometry." + geoName)
                            .build());

                    // TODO: This is not fully correct. On Bedrock, the shape rotates with
                    //       the block, so the collision box will need to be rotated back here
                    VoxelShape shape = state.getShape(new SingletonBlockGetter(state), BlockPos.ZERO);
                    VoxelShape collisionShape = state.getCollisionShape(new SingletonBlockGetter(state), BlockPos.ZERO);

                    componentsBuilder.selectionBox(createBoxComponent(shape));
                    componentsBuilder.collisionBox(createBoxComponent(collisionShape));
                } else {
                    componentsBuilder.geometry(GeometryComponent.builder()
                            .identifier("minecraft:geometry.full_block")
                            .build());
                }

                // TODO: Work this out based on block state/texture? as this isn't perfect
                // https://wiki.bedrock.dev/blocks/block-components.html#render-methods
                String renderMethod = state.canOcclude() ? "opaque" : "blend";

                Materials materials = context.storage().materials();
                Materials.Material material = materials.material(key.toString());
                if (material != null) {
                    // Add a default texture, can be replaced by the below (I think)
                    Map.Entry<String, String> firstEntry = material.textures().entrySet().iterator().next();
                    componentsBuilder.materialInstance("*", MaterialInstance.builder()
                            .texture(PackUtil.getTextureName(firstEntry.getValue()))
                            .renderMethod(renderMethod)
                            .faceDimming(true)
                            .ambientOcclusion(model.ambientOcclusion())
                            .build());

                    Map<String, String> faceMapping = getFaceMapping(model.parent());
                    if (!faceMapping.isEmpty()) {
                        for (Map.Entry<String, String> face : faceMapping.entrySet()) {
                            if (!material.textures().containsKey(face.getValue())) continue;

                            String textureName = material.textures().get(face.getValue());

                            componentsBuilder.materialInstance(face.getKey(), MaterialInstance.builder()
                                    .texture(PackUtil.getTextureName(textureName))
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
                                    .texture(PackUtil.getTextureName(entry.getValue()))
                                    .renderMethod(renderMethod)
                                    .faceDimming(true)
                                    .ambientOcclusion(model.ambientOcclusion())
                                    .build());
                        }
                    }
                } else {
                    componentsBuilder.materialInstance("*", MaterialInstance.builder()
                            .texture(PackUtil.getTextureName(key.toString()))
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
                    .destructibleByMining(block.defaultDestroyTime()) // TODO: Check
                    // .unitCube(true) // TODO: Geometry conversion
                    .selectionBox(createBoxComponent(shape))
                    .collisionBox(createBoxComponent(collisionShape));

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
                        .blockHardness(block.defaultDestroyTime()) // TODO: Check
                        .hasBlockEntity(state.hasBlockEntity())
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
    private ModelDefinition getModel(@NotNull PackContext<?> context, @NotNull ResourceLocation blockLocation, @NotNull BlockState state) {
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

        // Get the default multipart variant if we have no match
        // TODO: Multipart states, this is temporary as we dont parse the condition yet
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

    @Nullable
    private static ModelTexture getModelTexture(@NotNull Map<String, ModelTexture> textures, @NotNull String key) {
        // Texture references the value of another texture
        ModelTexture value = textures.get(key);
        if (value != null && value.reference() != null) {
            return getModelTexture(textures, value.reference());
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
        return parent.namespace().equals("minecraft") && parent.value().startsWith("block/cube");
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
