package org.geysermc.hydraulic.item;

import com.google.auto.service.AutoService;
import net.kyori.adventure.key.Key;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;
import org.geysermc.geyser.api.item.custom.NonVanillaCustomItemData;
import org.geysermc.geyser.api.util.CreativeCategory;
import org.geysermc.hydraulic.pack.PackLogListener;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.TexturePackModule;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.pack.converter.converter.model.ModelStitcher;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.model.ModelTexture;

import java.util.List;

@AutoService(PackModule.class)
public class ItemPackModule extends TexturePackModule<ItemPackModule> {

    public ItemPackModule() {
        this.listenOn(GeyserDefineCustomItemsEvent.class, ItemPackModule::onDefineCustomItems);

        this.postProcess(this::postProcess);
    }

    private void postProcess(@NotNull PackPostProcessContext<ItemPackModule> context) {
        ResourcePack assets = context.javaResourcePack();
        BedrockResourcePack bedrockPack = context.bedrockResourcePack();

        List<Item> items = context.registryValues(Registries.ITEM);

        LOGGER.info("Items to convert: " + items.size() + " in mod " + context.mod().id());

        ModelStitcher.Provider provider = ModelStitcher.vanillaProvider(assets, new PackLogListener(LOGGER));
        for (Item item : items) {
            ResourceLocation itemLocation = BuiltInRegistries.ITEM.getKey(item);

            Model baseModel = assets.model(Key.key(itemLocation.getNamespace(), "item/" + itemLocation.getPath()));
            Model model = new ModelStitcher(provider, baseModel).stitch();
            if (model == null || model.textures() == null) {
                LOGGER.warn("Item {} has no item model, skipping", itemLocation);
                continue;
            }

            List<ModelTexture> layers = model.textures().layers();
            if (layers == null || layers.isEmpty()) {
                LOGGER.warn("Item {} has no layer0 texture, skipping", itemLocation);
                continue;
            }

            ModelTexture layer0 = layers.get(0);
            String outputLoc = getOutputFromModel(context, layer0.key());
            bedrockPack.addItemTexture(itemLocation.toString(), outputLoc.replace(".png", ""));
        }
    }

    @Override
    public boolean test(@NotNull PackPostProcessContext<ItemPackModule> context) {
        return context.registryValues(Registries.ITEM).size() > 0;
    }

    private static void onDefineCustomItems(PackEventContext<GeyserDefineCustomItemsEvent, ItemPackModule> context) {
        GeyserDefineCustomItemsEvent event = context.event();
        List<Item> items = context.registryValues(Registries.ITEM);

        DefaultedRegistry<Item> registry = BuiltInRegistries.ITEM;
        for (Item item : items) {
            ResourceLocation itemLocation = registry.getKey(item);
            NonVanillaCustomItemData.Builder customItemBuilder = NonVanillaCustomItemData.builder()
                .name(itemLocation.getPath())
                .displayName("%" + item.getDescriptionId())
                .identifier(itemLocation.toString())
                .icon(itemLocation.toString())
                .javaId(registry.getId(item))
                .stackSize(item.getMaxStackSize())
                .maxDamage(item.getMaxDamage())
                .allowOffhand(true)
                .creativeCategory(CreativeCategory.ITEMS.id())
                .creativeGroup("itemGroup.name.items")
                .maxDamage(item.getMaxDamage())
                .stackSize(item.getMaxStackSize());

            // Enchantment glint by default
            if (item.isFoil(item.getDefaultInstance())) {
                customItemBuilder.foil(true);
            }

            if (item.isEdible()) {
                customItemBuilder
                        .creativeCategory(CreativeCategory.EQUIPMENT.id())
                        .creativeGroup("itemGroup.name.miscFood")
                        .edible(true);

                if (item.getFoodProperties() != null) {
                    customItemBuilder
                            .canAlwaysEat(item.getFoodProperties().canAlwaysEat());
                }
            }

            CreativeMappings.setup(item, customItemBuilder);

            if (item instanceof ArmorItem armorItem) {
                customItemBuilder.protectionValue(armorItem.getDefense());
                switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> customItemBuilder.armorType("helmet").creativeGroup("itemGroup.name.helmet");
                    case CHEST -> customItemBuilder.armorType("chestplate").creativeGroup("itemGroup.name.chestplate");
                    case LEGS -> customItemBuilder.armorType("leggings").creativeGroup("itemGroup.name.leggings");
                    case FEET -> customItemBuilder.armorType("boots").creativeGroup("itemGroup.name.boots");
                }
            } else if (item instanceof TieredItem tieredItem) {
                customItemBuilder.displayHandheld(true); // So we hold the tool right

                // TODO Support custom tiers
                customItemBuilder.toolTier("DIAMOND");
                if (tieredItem.getTier() instanceof Tiers) {
                    customItemBuilder.toolTier(tieredItem.getTier().toString());
                }

                if (item instanceof PickaxeItem) {
                    customItemBuilder.toolType("pickaxe");
                } else if (item instanceof HoeItem) {
                    customItemBuilder.toolType("hoe");
                } else if (item instanceof AxeItem) {
                    customItemBuilder.toolType("axe");
                } else if (item instanceof ShovelItem) {
                    customItemBuilder.toolType("shovel");
                } else if (item instanceof SwordItem) {
                    customItemBuilder.toolType("sword");
                }
            } else if (item instanceof ShearsItem) {
                customItemBuilder.toolType("shears");
            } else if (item instanceof BowItem) {
                customItemBuilder.chargeable(true);
            }

            event.register(customItemBuilder.build());
        }
    }
}
