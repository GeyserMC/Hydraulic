package org.geysermc.hydraulic.item;

import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import org.geysermc.geyser.api.block.custom.CustomBlockData;
import org.geysermc.geyser.api.item.custom.NonVanillaCustomItemData;
import org.geysermc.geyser.api.util.CreativeCategory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Contains mappings for items to their creative groups and categories.
 */
public class CreativeMappings {
    // https://wiki.bedrock.dev/documentation/creative-categories.html
    private static final Map<Class<? extends Item>, CreativeMapping> CREATIVE_ITEM_MAPPINGS = new LinkedHashMap<>() {
        {
            put(ArmorItem.class, new CreativeMapping(CreativeCategory.EQUIPMENT));
            put(ArrowItem.class, new CreativeMapping("itemGroup.name.arrow", CreativeCategory.EQUIPMENT));
            put(AxeItem.class, new CreativeMapping("itemGroup.name.axe", CreativeCategory.EQUIPMENT));
            put(BannerPatternItem.class, new CreativeMapping("itemGroup.name.banner_pattern"));
            put(BowItem.class, new CreativeMapping(CreativeCategory.EQUIPMENT));
            put(HoeItem.class, new CreativeMapping("itemGroup.name.hoe", CreativeCategory.EQUIPMENT));
            put(HorseArmorItem.class, new CreativeMapping("itemGroup.name.horseArmor", CreativeCategory.EQUIPMENT));
            put(MinecartItem.class, new CreativeMapping("itemGroup.name.minecart"));
            put(PickaxeItem.class, new CreativeMapping("itemGroup.name.pickaxe", CreativeCategory.EQUIPMENT));
            put(RecordItem.class, new CreativeMapping("itemGroup.name.record"));
            put(ShearsItem.class, new CreativeMapping(CreativeCategory.EQUIPMENT));
            put(ShovelItem.class, new CreativeMapping("itemGroup.name.shovel", CreativeCategory.EQUIPMENT));
            put(SpawnEggItem.class, new CreativeMapping("itemGroup.name.mobEgg", CreativeCategory.NATURE)); // Added differently to bedrock https://wiki.bedrock.dev/visuals/retexturing-spawn-eggs.html
            put(SwordItem.class, new CreativeMapping("itemGroup.name.sword", CreativeCategory.EQUIPMENT));
            put(HangingSignItem.class , new CreativeMapping("itemGroup.name.hanging_sign"));
            put(SignItem.class , new CreativeMapping("itemGroup.name.sign"));
            put(SmithingTemplateItem.class , new CreativeMapping("itemGroup.name.smithing_templates"));

            // Fallbacks for more base level classes
            put(TieredItem.class, new CreativeMapping(CreativeCategory.EQUIPMENT));
        }
    };

    private static final Map<Class<? extends Block>, CreativeMapping> CREATIVE_BLOCK_MAPPINGS = new LinkedHashMap<>() {
        {
            put(AnvilBlock.class, new CreativeMapping("itemGroup.name.anvil"));

            put(LeavesBlock.class, new CreativeMapping("itemGroup.name.leaves", CreativeCategory.NATURE));
            put(SaplingBlock.class, new CreativeMapping("itemGroup.name.sapling", CreativeCategory.NATURE));
            put(TallFlowerBlock.class, new CreativeMapping("itemGroup.name.flower", CreativeCategory.NATURE));
            put(FlowerBlock.class, new CreativeMapping("itemGroup.name.flower", CreativeCategory.NATURE));
            put(PinkPetalsBlock.class, new CreativeMapping("itemGroup.name.flower", CreativeCategory.NATURE));
            put(CropBlock.class, new CreativeMapping("itemGroup.name.crop", CreativeCategory.NATURE));
            put(HugeMushroomBlock.class, new CreativeMapping("itemGroup.name.mushroom", CreativeCategory.NATURE));
            put(MushroomBlock.class, new CreativeMapping("itemGroup.name.mushroom", CreativeCategory.NATURE));

            put(SlabBlock.class, new CreativeMapping("itemGroup.name.slab", CreativeCategory.CONSTRUCTION));
            put(FenceGateBlock.class, new CreativeMapping("itemGroup.name.fenceGate", CreativeCategory.CONSTRUCTION));
            put(FenceBlock.class, new CreativeMapping("itemGroup.name.fence", CreativeCategory.CONSTRUCTION));
            put(DoorBlock.class, new CreativeMapping("itemGroup.name.door", CreativeCategory.CONSTRUCTION));
            put(TrapDoorBlock.class, new CreativeMapping("itemGroup.name.trapdoor", CreativeCategory.CONSTRUCTION));
            put(StairBlock.class, new CreativeMapping("itemGroup.name.stairs", CreativeCategory.CONSTRUCTION));
            put(WallBlock.class, new CreativeMapping("itemGroup.name.walls", CreativeCategory.CONSTRUCTION));

            put(CeilingHangingSignBlock.class, new CreativeMapping("itemGroup.name.hanging_sign"));
            put(WallHangingSignBlock.class, new CreativeMapping("itemGroup.name.hanging_sign"));
            put(SignBlock.class, new CreativeMapping("itemGroup.name.sign"));

            put(ButtonBlock.class, new CreativeMapping("itemGroup.name.buttons"));
            put(PressurePlateBlock.class, new CreativeMapping("itemGroup.name.pressurePlate"));

            // Fallbacks for more base level classes
            put(GrowingPlantBlock.class, new CreativeMapping(CreativeCategory.NATURE));
            put(VineBlock.class, new CreativeMapping(CreativeCategory.NATURE));
            put(BushBlock.class, new CreativeMapping(CreativeCategory.NATURE));
        }
    };

    static void setup(Item item, NonVanillaCustomItemData.Builder customItemBuilder) {
        CreativeMapping mapping = getMapping(item.getClass(), CREATIVE_ITEM_MAPPINGS);

        if (mapping == null) {
            return;
        }

        customItemBuilder.creativeGroup(mapping.creativeGroup()).creativeCategory(mapping.creativeCategory().id());
    }

    public static void setupBlock(Block block, NonVanillaCustomItemData.Builder customItemBuilder) {
        CreativeMapping mapping = getMapping(block.getClass(), CREATIVE_BLOCK_MAPPINGS);

        if (mapping == null) {
            return;
        }

        customItemBuilder.creativeGroup(mapping.creativeGroup()).creativeCategory(mapping.creativeCategory().id());
    }

    public static void setupBlock(Block block, CustomBlockData.Builder customItemBuilder) {
        CreativeMapping mapping = getMapping(block.getClass(), CREATIVE_BLOCK_MAPPINGS);

        if (mapping == null) {
            return;
        }

        customItemBuilder.creativeGroup(mapping.creativeGroup()).creativeCategory(mapping.creativeCategory());
    }

    private static CreativeMapping getMapping(Class inputClass, Map<?, CreativeMapping> mappings) {
        CreativeMapping mapping = mappings.get(inputClass);

        // Try to find any inheritance mappings
        if (mapping == null) {
            for (Map.Entry<?, CreativeMapping> entry : mappings.entrySet()) {
                if (((Class)entry.getKey()).isAssignableFrom(inputClass)) {
                    mapping = entry.getValue();
                    break;
                }
            }
        }

        return mapping;
    }
}
