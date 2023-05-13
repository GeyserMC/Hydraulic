package org.geysermc.hydraulic.item;

import net.minecraft.world.item.*;
import org.geysermc.geyser.api.item.custom.NonVanillaCustomItemData;

import java.util.HashMap;
import java.util.Map;

public class CreativeMappings {
    private static Map<Class<? extends Item>, CreativeMapping> CREATIVE_MAPPINGS = new HashMap<>() {
        {
            put(ArrowItem.class, new CreativeMapping("itemGroup.name.arrow", CreativeCategory.EQUIPMENT));
            put(AxeItem.class, new CreativeMapping("itemGroup.name.axe", CreativeCategory.EQUIPMENT));
            put(BannerPatternItem.class, new CreativeMapping("itemGroup.name.banner_pattern"));
            put(HoeItem.class, new CreativeMapping("itemGroup.name.hoe", CreativeCategory.EQUIPMENT));
            put(HorseArmorItem.class, new CreativeMapping("itemGroup.name.horseArmor", CreativeCategory.EQUIPMENT));
            put(MinecartItem.class, new CreativeMapping("itemGroup.name.minecart"));
            put(PickaxeItem.class, new CreativeMapping("itemGroup.name.pickaxe", CreativeCategory.EQUIPMENT));
            put(RecordItem.class, new CreativeMapping("itemGroup.name.record"));
            put(ShearsItem.class, new CreativeMapping(CreativeCategory.EQUIPMENT));
            put(ShovelItem.class, new CreativeMapping("itemGroup.name.shovel", CreativeCategory.EQUIPMENT));
            put(SpawnEggItem.class, new CreativeMapping("itemGroup.name.mobEgg", CreativeCategory.NATURE));
            put(SwordItem.class, new CreativeMapping("itemGroup.name.sword", CreativeCategory.EQUIPMENT));
        }
    };

    public static void setup(Item item, NonVanillaCustomItemData.Builder customItemBuilder) {
        if (!CREATIVE_MAPPINGS.containsKey(item.getClass())) {
            return;
        }
        CreativeMapping mapping = CREATIVE_MAPPINGS.get(item.getClass());
        customItemBuilder.creativeGroup(mapping.creativeGroup()).creativeCategory(mapping.creativeCategory().id());
    }
}
