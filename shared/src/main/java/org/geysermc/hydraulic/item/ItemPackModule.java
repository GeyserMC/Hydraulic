package org.geysermc.hydraulic.item;

import com.google.auto.service.AutoService;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;
import org.geysermc.geyser.api.item.custom.NonVanillaCustomItemData;
import org.geysermc.hydraulic.assets.Model;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.context.PackCreateContext;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.geysermc.hydraulic.util.Constants;
import org.geysermc.hydraulic.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@AutoService(PackModule.class)
public class ItemPackModule extends PackModule<ItemPackModule> {

    public ItemPackModule() {
        this.listenOn(GeyserDefineCustomItemsEvent.class, ItemPackModule::onDefineCustomItems);
    }

    @Override
    public void create(@NotNull PackCreateContext<ItemPackModule> context) {
        List<Item> items = context.registryValues(Registries.ITEM);

        LOGGER.info("Items to convert: " + items.size() + " in mod " + context.mod().id());
        Path jarPath = context.mod().modPath();

        for (Item item : items) {
            if (item instanceof BlockItem) {
                continue; // TODO: Special handling here?
            }
            ResourceLocation itemLocation = BuiltInRegistries.ITEM.getKey(item);

            try (InputStream itemModelStream = Files.newInputStream(jarPath.resolve(String.format(Constants.JAVA_ITEM_MODEL_LOCATION, itemLocation.getNamespace(), itemLocation.getPath())))) {
                Model model = Constants.MAPPER.readValue(itemModelStream, Model.class);

                // TODO
                ResourceLocation layer0 = model.textures() == null ? null : model.textures().get("layer0");
                if (layer0 == null) {
                    LOGGER.warn("Item {} has no layer0 texture, skipping", itemLocation);
                    continue;
                }

                ResourceLocation itemKey = BuiltInRegistries.ITEM.getKey(item);
                String outputLoc = String.format(Constants.BEDROCK_ITEM_TEXTURE_LOCATION, context.mod().id(), layer0.getPath().replace("item/", ""));
                context.pack().addItem(itemKey.toString(), outputLoc.replace(".png", ""));

                FileUtil.copyFileFromMod(context.mod(), String.format(Constants.JAVA_TEXTURE_LOCATION, layer0.getNamespace(), layer0.getPath()), context.path().resolve(outputLoc));
            } catch (Exception ex) {
                LOGGER.error("Failed to read item model {} for mod {}", itemLocation.toString(), context.mod().id(), ex);
            }
        }
    }

    @Override
    public boolean test(@NotNull PackCreateContext<ItemPackModule> context) {
        return context.registryValues(Registries.ITEM).size() > 0;
    }

    private static void onDefineCustomItems(PackEventContext<GeyserDefineCustomItemsEvent, ItemPackModule> context) {
        GeyserDefineCustomItemsEvent event = context.event();
        List<Item> items = context.registryValues(Registries.ITEM);

        // No custom items
        if (items.size() == 0) {
            return;
        }

        DefaultedRegistry<Item> registry = BuiltInRegistries.ITEM;
        for (Item item : items) {
            String name = Language.getInstance().getOrDefault(item.getDescriptionId());
            ResourceLocation itemLocation = registry.getKey(item);
            NonVanillaCustomItemData.Builder customItemBuilder = NonVanillaCustomItemData.builder()
                .name(itemLocation.getPath())
                .displayName(name)
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
