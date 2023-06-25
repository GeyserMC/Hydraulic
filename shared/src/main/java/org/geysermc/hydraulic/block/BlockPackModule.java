package org.geysermc.hydraulic.block;

import com.google.auto.service.AutoService;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import org.geysermc.geyser.api.block.custom.CustomBlockData;
import org.geysermc.geyser.api.block.custom.CustomBlockState;
import org.geysermc.geyser.api.block.custom.NonVanillaCustomBlockData;
import org.geysermc.geyser.api.block.custom.component.BoxComponent;
import org.geysermc.geyser.api.block.custom.component.CustomBlockComponents;
import org.geysermc.geyser.api.block.custom.component.MaterialInstance;
import org.geysermc.geyser.api.block.custom.nonvanilla.JavaBlockState;
import org.geysermc.geyser.api.block.custom.nonvanilla.JavaBoundingBox;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomBlocksEvent;
import org.geysermc.geyser.level.physics.PistonBehavior;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.TexturePackModule;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.geysermc.hydraulic.pack.context.PackProcessContext;
import org.geysermc.hydraulic.util.Constants;
import org.geysermc.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.pack.converter.PackConversionContext;
import org.geysermc.pack.converter.data.TextureConversionData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AutoService(PackModule.class)
public class BlockPackModule extends TexturePackModule<BlockPackModule> {

    public BlockPackModule() {
        this.listenOn(GeyserDefineCustomBlocksEvent.class, BlockPackModule::onDefineCustomBlocks);
    }

    @Override
    public void postConvert(PackConversionContext<TextureConversionData> packContext) {
        BedrockResourcePack bedrockPack = packContext.bedrockResourcePack();

        this.postProcess(context -> {
            List<Block> blocks = context.registryValues(Registries.BLOCK);

            LOGGER.info("Blocks to convert: " + blocks.size() + " in mod " + context.mod().id());
            for (Block block : blocks) {
                ResourceLocation blockLocation = BuiltInRegistries.BLOCK.getKey(block);
                String outputLoc = String.format(Constants.BEDROCK_TEXTURE_LOCATION, "blocks/" + context.mod().id() + "/" + blockLocation.getPath());
                bedrockPack.addBlockTexture(blockLocation.toString(), outputLoc.replace(".png", ""));
            }
        });
    }

    @Override
    public boolean test(@NotNull PackProcessContext<BlockPackModule> context) {
        return context.registryValues(Registries.BLOCK).size() > 0;
    }

    private static void onDefineCustomBlocks(PackEventContext<GeyserDefineCustomBlocksEvent, BlockPackModule> context) {
        GeyserDefineCustomBlocksEvent event = context.event();
        List<Block> blocks = context.registryValues(Registries.BLOCK);

        DefaultedRegistry<Block> registry = BuiltInRegistries.BLOCK;
        for (Block block : blocks) {
            ResourceLocation blockLocation = registry.getKey(block);
            CustomBlockData.Builder builder = NonVanillaCustomBlockData.builder()
                    .identifier(blockLocation.toString())
                    .name(blockLocation.getPath())
                    .includedInCreativeInventory(true)
                    .creativeGroup("itemGroup.name.items");

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

            // TODO: Permutations (requires model conversion)

            CustomBlockComponents.Builder componentsBuilder = CustomBlockComponents.builder()
                    .displayName("%" + block.getDescriptionId())
                    .friction(block.getFriction())
                    .destructibleByMining(block.defaultDestroyTime()) // TODO: Check
                    .unitCube(true) // TODO: Geometry conversion
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
}
