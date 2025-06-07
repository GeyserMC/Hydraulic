package org.geysermc.hydraulic.fabric.test;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ModItems {
    public static final List<Item> ITEMS = new ArrayList<>();

    public static final ToolMaterial BARREL_TOOL_MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            9999,
            2.0F,
            5.0F,
            30,
            ConventionalItemTags.BARRELS
    );
    public static final Item BARREL_SWORD = register(
            "barrel_sword",
            Item::new,
            new Item.Properties()
                    .sword(BARREL_TOOL_MATERIAL, 12.0F, -2.4F)
                    .rarity(Rarity.EPIC)
    );
    public static final Item BARREL_PICKAXE = register(
            "barrel_pickaxe",
            Item::new,
            new Item.Properties()
                    .pickaxe(BARREL_TOOL_MATERIAL, 0.5F, -2.8F)
                    .rarity(Rarity.EPIC)
    );
    public static final Item BARREL_AXE = register(
            "barrel_axe",
            properties -> new AxeItem(BARREL_TOOL_MATERIAL, 16.0F, -3.2F, properties),
            new Item.Properties()
                    .rarity(Rarity.EPIC)
    );
    public static final Item BARREL_SHOVEL = register(
            "barrel_shovel",
            properties -> new ShovelItem(BARREL_TOOL_MATERIAL, 1.0F, -3.0F, properties),
            new Item.Properties()
                    .rarity(Rarity.EPIC)
    );
    public static final Item BARREL_HOE = register(
            "barrel_hoe",
            properties -> new AxeItem(BARREL_TOOL_MATERIAL, -5.0F, -3.0F, properties),
            new Item.Properties()
                    .rarity(Rarity.EPIC)
    );

    public static final ResourceKey<EquipmentAsset> BARREL_ARMOR_MATERIAL_KEY = ResourceKey.create(
            EquipmentAssets.ROOT_ID, ResourceLocation.fromNamespaceAndPath("htm", "barrel")
    );
    public static final ArmorMaterial BARREL_ARMOR_MATERIAL = new ArmorMaterial(
            15,
            Map.of(
                    ArmorType.HELMET, 3,
                    ArmorType.CHESTPLATE, 8,
                    ArmorType.LEGGINGS, 6,
                    ArmorType.BOOTS, 3,
                    ArmorType.BODY, 4
            ),
            5,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            0.0F,
            0.0F,
            ConventionalItemTags.BARRELS,
            BARREL_ARMOR_MATERIAL_KEY
    );
    public static final Item BARREL_HELMET = register(
            "barrel_helmet",
            Item::new,
            new Item.Properties()
                    .humanoidArmor(BARREL_ARMOR_MATERIAL, ArmorType.HELMET)
    );
    public static final Item BARREL_CHESTPLATE = register(
            "barrel_chestplate",
            Item::new,
            new Item.Properties()
                    .humanoidArmor(BARREL_ARMOR_MATERIAL, ArmorType.CHESTPLATE)
    );
    public static final Item BARREL_LEGGINGS = register(
            "barrel_leggings",
            Item::new,
            new Item.Properties()
                    .humanoidArmor(BARREL_ARMOR_MATERIAL, ArmorType.LEGGINGS)
    );
    public static final Item BARREL_BOOTS = register(
            "barrel_boots",
            Item::new,
            new Item.Properties()
                    .humanoidArmor(BARREL_ARMOR_MATERIAL, ArmorType.BOOTS)
    );
    public static final Item BARREL_HORSE_ARMOR = register(
            "barrel_horse_armor",
            Item::new,
            new Item.Properties()
                    .horseArmor(BARREL_ARMOR_MATERIAL)
    );

    public static final Item BARREL_STICK = register(
            "barrel_stick",
            Item::new,
            new Item.Properties()
                    .food(new FoodProperties(20, 20f, false))
    );

    public static final Item BARREL_PACK = register(
            "barrel_pack",
            Item::new,
            new Item.Properties()
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                    .component(DataComponents.MAX_STACK_SIZE, 16)
                    .component(DataComponents.USE_COOLDOWN, new UseCooldown(3))
    );

    public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties properties) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("hydraulic_test_mod", name));
        Item item = itemFactory.apply(properties.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        ITEMS.add(item);

        return item;
    }

    public static void init() {}
}
