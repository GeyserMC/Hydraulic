package org.geysermc.hydraulic.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.geysermc.geyser.api.block.custom.CustomBlockData;
import org.geysermc.geyser.api.item.custom.NonVanillaCustomItemData;
import org.geysermc.geyser.api.item.custom.v2.CustomItemBedrockOptions;
import org.geysermc.geyser.api.item.custom.v2.NonVanillaCustomItemDefinition;
import org.geysermc.geyser.api.util.CreativeCategory;
import org.geysermc.hydraulic.util.ItemGroup;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains mappings for items to their creative groups and categories.
 */
public class CreativeMappings {
    private static final Map<CreativeMappingTarget, CreativeMapping> CREATIVE_MAPPINGS = new LinkedHashMap<>() {
        {
            // region --- Construction ---
            put(new CreativeMappingTarget(
                List.of(),
                List.of(ItemTags.DOORS),
                List.of(DoorBlock.class),
                List.of(BlockTags.DOORS)
            ), new CreativeMapping(ItemGroup.DOORS, CreativeCategory.CONSTRUCTION));

            put(new CreativeMappingTarget(
                List.of(),
                List.of(ItemTags.FENCES),
                List.of(FenceBlock.class),
                List.of(BlockTags.FENCES)
            ), new CreativeMapping(ItemGroup.FENCES, CreativeCategory.CONSTRUCTION));

            put(new CreativeMappingTarget(
                List.of(),
                List.of(ItemTags.FENCE_GATES),
                List.of(FenceGateBlock.class),
                List.of(BlockTags.FENCE_GATES)
            ), new CreativeMapping(ItemGroup.FENCE_GATES, CreativeCategory.CONSTRUCTION));

            put(new CreativeMappingTarget(
                List.of(),
                List.of(ItemTags.PLANKS),
                List.of(),
                List.of(BlockTags.PLANKS)
            ), new CreativeMapping(ItemGroup.PLANKS, CreativeCategory.CONSTRUCTION));

            put(new CreativeMappingTarget(
                List.of(),
                List.of(ItemTags.SLABS),
                List.of(SlabBlock.class),
                List.of(BlockTags.SLABS)
            ), new CreativeMapping(ItemGroup.SLABS, CreativeCategory.CONSTRUCTION));

            put(new CreativeMappingTarget(
                List.of(),
                List.of(ItemTags.STAIRS),
                List.of(StairBlock.class),
                List.of(BlockTags.STAIRS)
            ), new CreativeMapping(ItemGroup.STAIRS, CreativeCategory.CONSTRUCTION));

            put(new CreativeMappingTarget(
                List.of(),
                List.of(ItemTags.TRAPDOORS),
                List.of(TrapDoorBlock.class),
                List.of(BlockTags.TRAPDOORS)
            ), new CreativeMapping(ItemGroup.TRAPDOORS, CreativeCategory.CONSTRUCTION));

            put(new CreativeMappingTarget(
                List.of(),
                List.of(ItemTags.WALLS),
                List.of(WallBlock.class),
                List.of(BlockTags.WALLS)
            ), new CreativeMapping(ItemGroup.WALLS, CreativeCategory.CONSTRUCTION));
            // endregion

            // region --- Nature ---
            put(new CreativeMappingTarget(
                List.of(),
                List.of(),
                List.of(CropBlock.class),
                List.of()
            ), new CreativeMapping(ItemGroup.CROPS, CreativeCategory.NATURE));

            put(new CreativeMappingTarget(
                List.of(),
                List.of(ItemTags.FLOWERS),
                List.of(FlowerBlock.class, TallFlowerBlock.class),
                List.of(BlockTags.FLOWERS, BlockTags.SMALL_FLOWERS)
            ), new CreativeMapping(ItemGroup.FLOWERS, CreativeCategory.NATURE));

            put(new CreativeMappingTarget(
                List.of(),
                List.of(ItemTags.LEAVES),
                List.of(LeavesBlock.class),
                List.of(BlockTags.LEAVES)
            ), new CreativeMapping(ItemGroup.LEAVES, CreativeCategory.NATURE));

            put(new CreativeMappingTarget(
                List.of(),
                List.of(ItemTags.LOGS),
                List.of(),
                List.of(BlockTags.LOGS)
            ), new CreativeMapping(ItemGroup.LOGS, CreativeCategory.NATURE));

            // TODO Remove, these are added differently to bedrock https://wiki.bedrock.dev/visuals/retexturing-spawn-eggs.html
            put(new CreativeMappingTarget(
                List.of(SpawnEggItem.class),
                List.of(),
                List.of(),
                List.of()
            ), new CreativeMapping(ItemGroup.MOB_EGGS, CreativeCategory.NATURE));

            put(new CreativeMappingTarget(
                List.of(),
                List.of(),
                List.of(HugeMushroomBlock.class, MushroomBlock.class),
                List.of()
            ), new CreativeMapping(ItemGroup.MUSHROOMS, CreativeCategory.NATURE));

            put(new CreativeMappingTarget(
                List.of(),
                List.of(ItemTags.SAPLINGS),
                List.of(SaplingBlock.class),
                List.of(BlockTags.SAPLINGS)
            ), new CreativeMapping(ItemGroup.SAPLINGS, CreativeCategory.NATURE));
            // endregion

            // region --- Equipment ---
            put(new CreativeMappingTarget(
                List.of(ArrowItem.class),
                List.of(ItemTags.ARROWS),
                List.of(),
                List.of()
            ), new CreativeMapping(ItemGroup.ARROWS, CreativeCategory.EQUIPMENT));

            put(new CreativeMappingTarget(
                List.of(AxeItem.class),
                List.of(ItemTags.AXES),
                List.of(),
                List.of()
            ), new CreativeMapping(ItemGroup.AXES, CreativeCategory.EQUIPMENT));

            put(new CreativeMappingTarget(
                List.of(HoeItem.class),
                List.of(ItemTags.HOES),
                List.of(),
                List.of()
            ), new CreativeMapping(ItemGroup.HOES, CreativeCategory.EQUIPMENT));

            put(new CreativeMappingTarget(
                List.of(), // No item class present, currently depends on the tag
                List.of(ItemTags.PICKAXES),
                List.of(),
                List.of()
            ), new CreativeMapping(ItemGroup.PICKAXES, CreativeCategory.EQUIPMENT));

            put(new CreativeMappingTarget(
                List.of(ShovelItem.class),
                List.of(ItemTags.SHOVELS),
                List.of(),
                List.of()
            ), new CreativeMapping(ItemGroup.SHOVELS, CreativeCategory.EQUIPMENT));

            put(new CreativeMappingTarget(
                List.of(), // No item class present, currently depends on the tag
                List.of(ItemTags.SWORDS),
                List.of(),
                List.of()
            ), new CreativeMapping(ItemGroup.SWORDS, CreativeCategory.EQUIPMENT));
            // endregion

            // region --- Items ---
            put(new CreativeMappingTarget(
                List.of(),
                List.of(ItemTags.ANVIL),
                List.of(AnvilBlock.class),
                List.of(BlockTags.ANVIL)
            ), new CreativeMapping(ItemGroup.ANVILS, CreativeCategory.ITEMS));

            put(new CreativeMappingTarget(
                List.of(),
                List.of(ItemTags.CHEST_BOATS),
                List.of(),
                List.of()
            ), new CreativeMapping(ItemGroup.BOATS_WITH_CHEST, CreativeCategory.ITEMS));

            put(new CreativeMappingTarget(
                List.of(),
                List.of(ItemTags.BOATS),
                List.of(),
                List.of()
            ), new CreativeMapping(ItemGroup.BOATS, CreativeCategory.ITEMS));

            put(new CreativeMappingTarget(
                List.of(),
                List.of(ItemTags.BUTTONS),
                List.of(ButtonBlock.class),
                List.of(BlockTags.BUTTONS)
            ), new CreativeMapping(ItemGroup.BUTTONS, CreativeCategory.ITEMS));

            put(new CreativeMappingTarget(
                List.of(HangingSignItem.class),
                List.of(ItemTags.HANGING_SIGNS),
                List.of(CeilingHangingSignBlock.class, WallHangingSignBlock.class),
                List.of(BlockTags.CEILING_HANGING_SIGNS, BlockTags.WALL_HANGING_SIGNS)
            ), new CreativeMapping(ItemGroup.HANGING_SIGNS, CreativeCategory.ITEMS));

            put(new CreativeMappingTarget(
                List.of(MinecartItem.class),
                List.of(),
                List.of(),
                List.of()
            ), new CreativeMapping(ItemGroup.MINECARTS, CreativeCategory.ITEMS));

            put(new CreativeMappingTarget(
                List.of(),
                List.of(),
                List.of(PressurePlateBlock.class),
                List.of(BlockTags.PRESSURE_PLATES)
            ), new CreativeMapping(ItemGroup.PRESSURE_PLATES, CreativeCategory.ITEMS));

            put(new CreativeMappingTarget(
                List.of(SignItem.class),
                List.of(ItemTags.SIGNS),
                List.of(SignBlock.class),
                List.of(BlockTags.WALL_SIGNS, BlockTags.STANDING_SIGNS)
            ), new CreativeMapping(ItemGroup.SIGNS, CreativeCategory.ITEMS));

            put(new CreativeMappingTarget(
                List.of(SmithingTemplateItem.class),
                List.of(ItemTags.TRIM_MATERIALS),
                List.of(),
                List.of()
            ), new CreativeMapping(ItemGroup.SMITHING_TEMPLATES, CreativeCategory.ITEMS));
            // endregion

            // region --- Fallbacks ---
            put(new CreativeMappingTarget(
                List.of(),
                List.of(),
                List.of(GrowingPlantBlock.class, VineBlock.class, BushBlock.class),
                List.of()
            ), new CreativeMapping(CreativeCategory.NATURE));

            put(new CreativeMappingTarget(
                List.of(BowItem.class, ShearsItem.class), // TODO: Armor item class is now missing, would require components to check or a tag
                List.of(),
                List.of(),
                List.of()
            ), new CreativeMapping(CreativeCategory.EQUIPMENT));

            put(new CreativeMappingTarget(
                List.of(Item.class),
                List.of(),
                List.of(Block.class),
                List.of()
            ), new CreativeMapping(CreativeCategory.ITEMS));
            // endregion
        }
    };

    public static void setup(Item item, CustomItemBedrockOptions.Builder customItemOptions) {
        CreativeMapping mapping = getMapping(item);

        if (mapping == null) {
            return;
        }

        customItemOptions.creativeGroup(mapping.creativeGroup().group()).creativeCategory(mapping.creativeCategory());
    }

    public static void setupBlock(Block block, CustomItemBedrockOptions.Builder customItemOptions) {
        CreativeMapping mapping = getMapping(block);

        if (mapping == null) {
            return;
        }

        customItemOptions.creativeGroup(mapping.creativeGroup().group()).creativeCategory(mapping.creativeCategory());
    }

    public static void setupBlock(Block block, CustomBlockData.Builder customItemBuilder) {
        CreativeMapping mapping = getMapping(block);

        if (mapping == null) {
            return;
        }

        customItemBuilder.creativeGroup(mapping.creativeGroup().group()).creativeCategory(mapping.creativeCategory());
    }

    private static CreativeMapping getMapping(Item item) {
        Class<? extends Item> itemClass = item.getClass();
        ItemStack itemStack = item.getDefaultInstance();

        for (Map.Entry<CreativeMappingTarget, CreativeMapping> entry : CREATIVE_MAPPINGS.entrySet()) {
            CreativeMappingTarget target = entry.getKey();

            // Check item classes
            if (!target.itemClasses().isEmpty()) {
                if (target.itemClasses().contains(itemClass)) {
                    return entry.getValue();
                }
                for (Class<? extends Item> targetItemClass : target.itemClasses()) {
                    if (targetItemClass.isAssignableFrom(itemClass)) {
                        return entry.getValue();
                    }
                }
            }

            // Check item tags
            if (!target.itemTags().isEmpty()) {
                for (TagKey<Item> tag : target.itemTags()) {
                    if (itemStack.is(tag)) {
                        return entry.getValue();
                    }
                }
            }
        }

        return null;
    }

    private static CreativeMapping getMapping(Block block) {
        Class<? extends Block> blockClass = block.getClass();
        BlockState blockState = block.defaultBlockState();

        for (Map.Entry<CreativeMappingTarget, CreativeMapping> entry : CREATIVE_MAPPINGS.entrySet()) {
            CreativeMappingTarget target = entry.getKey();

            // Check block classes
            if (!target.blockClasses().isEmpty()) {
                if (target.blockClasses().contains(blockClass)) {
                    return entry.getValue();
                }
                for (Class<? extends Block> targetBlockClass : target.blockClasses()) {
                    if (targetBlockClass.isAssignableFrom(blockClass)) {
                        return entry.getValue();
                    }
                }
            }

            // Check block tags
            if (!target.blockTags().isEmpty()) {
                for (TagKey<Block> tag : target.blockTags()) {
                    if (blockState.is(tag)) {
                        return entry.getValue();
                    }
                }
            }
        }

        return null;
    }
}
