package org.geysermc.hydraulic.block;

import com.google.auto.service.AutoService;
import net.kyori.adventure.key.Key;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
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
import org.geysermc.geyser.api.block.custom.CustomBlockData;
import org.geysermc.geyser.api.block.custom.CustomBlockPermutation;
import org.geysermc.geyser.api.block.custom.CustomBlockState;
import org.geysermc.geyser.api.block.custom.NonVanillaCustomBlockData;
import org.geysermc.geyser.api.block.custom.component.BoxComponent;
import org.geysermc.geyser.api.block.custom.component.CustomBlockComponents;
import org.geysermc.geyser.api.block.custom.component.MaterialInstance;
import org.geysermc.geyser.api.block.custom.component.TransformationComponent;
import org.geysermc.geyser.api.block.custom.nonvanilla.JavaBlockState;
import org.geysermc.geyser.api.block.custom.nonvanilla.JavaBoundingBox;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomBlocksEvent;
import org.geysermc.geyser.level.physics.PistonBehavior;
import org.geysermc.hydraulic.item.CreativeCategory;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.TexturePackModule;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.hydraulic.util.Constants;
import org.geysermc.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.pack.converter.PackConversionContext;
import org.geysermc.pack.converter.data.TextureConversionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.blockstate.MultiVariant;
import team.unnamed.creative.blockstate.Variant;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.texture.Texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoService(PackModule.class)
public class BlockPackModule extends TexturePackModule<BlockPackModule> {
    private static final Key UNIT_CUBE_KEY = Key.key("block/cube_all");
    private static final String STATE_CONDITION = "query.block_property('%s') == %s";

    private final Map<String, StateDefinition> blockStates = new HashMap<>();

    public BlockPackModule() {
        this.listenOn(GeyserDefineCustomBlocksEvent.class, this::onDefineCustomBlocks);

        this.preProcess(context -> {
            ResourcePack pack = context.pack();
            for (team.unnamed.creative.blockstate.BlockState blockState : pack.blockStates()) {
                this.blockStates.put(blockState.key().toString(), new StateDefinition(blockState, context.pack()));
            }
        });
    }

    @Override
    public void postConvert(PackConversionContext<TextureConversionData> packContext) {
        ResourcePack assets = packContext.javaResourcePack();
        BedrockResourcePack bedrockPack = packContext.bedrockResourcePack();

        this.postProcess(context -> {
            for (Texture texture : assets.textures()) {
                Key key = texture.key();
                String value = key.value();

                if (value.startsWith("block/")) {
                    String cleanPath = value.replace("block/", "").replace(".png", "");

                    String outputLoc = String.format(Constants.BEDROCK_TEXTURE_LOCATION, "blocks/" + context.mod().id() + "/" + cleanPath).replace(".png", "");
                    bedrockPack.addBlockTexture(key.namespace() + ":" + cleanPath, outputLoc);
                }
            }
        });
    }

    @Override
    public boolean test(@NotNull PackPostProcessContext<BlockPackModule> context) {
        return context.registryValues(Registries.BLOCK).size() > 0;
    }

    private void onDefineCustomBlocks(PackEventContext<GeyserDefineCustomBlocksEvent, BlockPackModule> context) {
        GeyserDefineCustomBlocksEvent event = context.event();
        List<Block> blocks = context.registryValues(Registries.BLOCK);

        DefaultedRegistry<Block> registry = BuiltInRegistries.BLOCK;
        for (Block block : blocks) {
            ResourceLocation blockLocation = registry.getKey(block);
            CustomBlockData.Builder builder = NonVanillaCustomBlockData.builder()
                    .identifier(blockLocation.toString())
                    .name(blockLocation.getPath())
                    .includedInCreativeInventory(true)
                    .creativeGroup("itemGroup.name.items")
                    .creativeCategory(CreativeCategory.ITEMS.internalName());

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
                ModelDefinition definition = getModel(blockLocation, state);
                if (definition != null) {
                    Key key = definition.model().key();

                    CustomBlockComponents.Builder componentsBuilder = CustomBlockComponents.builder()
                            .materialInstance("*", new MaterialInstance(getTextureFromModule(context, key), "alpha_test", true, definition.model().ambientOcclusion()))
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

                    if (!UNIT_CUBE_KEY.equals(definition.model().parent())) {
                        String namespace = key.namespace();
                        String value = key.value();

                        String fileName = value.substring(value.lastIndexOf('/') + 1);
                        String geoName = (namespace.equals(Key.MINECRAFT_NAMESPACE) ? "" : namespace + ".") + fileName;

                        componentsBuilder.geometry("geometry." + geoName);
                    } else {
                        componentsBuilder.unitCube(true);
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
                            propValue = "'" + propValue + "'";
                        }

                        conditions.add(String.format(STATE_CONDITION, property.getName(), propValue));
                    }

                    String condition = String.join(" && ", conditions);
                    permutations.add(new CustomBlockPermutation(componentsBuilder.build(), condition));
                }
            }

            builder.permutations(permutations);

            CustomBlockComponents.Builder componentsBuilder = baseComponentBuilder
                    .displayName("%" + block.getDescriptionId())
                    .friction(block.getFriction())
                    .destructibleByMining(block.defaultDestroyTime()) // TODO: Check
                    // .unitCube(true) // TODO: Geometry conversion
                    .selectionBox(BoxComponent.fullBox()) // TODO: Shapes
                    .collisionBox(BoxComponent.fullBox()) // TODO: Shapes
                    .materialInstance("*", new MaterialInstance(blockLocation.toString(), "alpha_test", true, true));

            builder.components(componentsBuilder.build());

            CustomBlockData blockData = builder.build();
            event.register(blockData);

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

                event.registerOverride(JavaBlockState.builder()
                                .identifier(BlockStateParser.serialize(state))
                                .javaId(Block.getId(state))
                                .blockHardness(block.defaultDestroyTime()) // TODO: Check
                                .hasBlockEntity(state.hasBlockEntity())
                                .waterlogged(state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED))
                                .collision(new JavaBoundingBox[0]) // TODO: VoxelShape -> BoundingBox
                                .stateGroupId(blockId)
                                .pistonBehavior(pistonBehavior.name())
                                .build(),
                        stateBuilder.build()
                );
            }
        }
    }

    @Nullable
    private ModelDefinition getModel(@NotNull ResourceLocation blockLocation, @NotNull BlockState state) {
        StateDefinition definition = this.blockStates.get(blockLocation.toString());
        if (definition == null) {
            LOGGER.warn("Missing blockstate for block {}", blockLocation);
            return null;
        }

        team.unnamed.creative.blockstate.BlockState packState = definition.state();

        // Check if we have a variant match
        MultiVariant multiVariant = matchState(state, packState.variants());
        if (multiVariant == null || multiVariant.variants().isEmpty()) {
            // No variant, check if we have a default
            multiVariant = packState.variants().get("");
        }

        // We have a match! Now we need to find the model
        if (multiVariant != null && !multiVariant.variants().isEmpty()) {
            // TODO: Handle multiple variants?
            Variant variant = multiVariant.variants().get(0);
            Key modelKey = variant.model();

            Model model = definition.pack().model(modelKey);
            if (model == null) {
                LOGGER.warn("Missing model {} for block {}", modelKey, blockLocation);
            } else {
                return new ModelDefinition(model, variant);
            }
        }

        // TODO: Multipart states

        return null;
    }

    private static String toPropertyString(@NotNull BlockState state) {
        StringBuilder builder = new StringBuilder();
        for (Property<?> property : state.getProperties()) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(property.getName()).append("=").append(state.getValue(property));
        }

        return builder.toString();
    }

    private static MultiVariant matchState(@NotNull BlockState state, @NotNull Map<String, MultiVariant> variants) {
        List<String> properties = new ArrayList<>();
        for (Property<?> property : state.getProperties()) {
            properties.add(property.getName() + "=" + state.getValue(property));
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
}
