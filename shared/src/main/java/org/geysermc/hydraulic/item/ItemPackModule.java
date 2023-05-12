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
    private static final String BEDROCK_ITEM_TEXTURE_LOCATION = "textures/items/%s/%s.png";
    private static final String JAVA_ITEM_MODEL_LOCATION = "assets/%s/models/item/%s.json";

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

            try (InputStream itemModelStream = Files.newInputStream(jarPath.resolve(String.format(JAVA_ITEM_MODEL_LOCATION, itemLocation.getNamespace(), itemLocation.getPath())))) {
                Model model = Constants.MAPPER.readValue(itemModelStream, Model.class);

                ResourceLocation layer0 = model.textures().get("layer0");

                ResourceLocation itemKey = BuiltInRegistries.ITEM.getKey(item);
                String outputLoc = String.format(BEDROCK_ITEM_TEXTURE_LOCATION, context.mod().id(), layer0.getPath().replace("item/", ""));
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
            NonVanillaCustomItemData.Builder customItemBuilder = NonVanillaCustomItemData.builder()
                .name(registry.getKey(item).getPath())
                .displayName(name)
                .identifier(registry.getKey(item).toString())
                .icon(registry.getKey(item).toString())
                .javaId(registry.getId(item))
                .stackSize(item.getMaxStackSize())
                .maxDamage(item.getMaxDamage())
                .allowOffhand(true)
                .creativeCategory(4) // 4 - "Items" // 3 - "Equipment"
                .creativeGroup("itemGroup.name.items")
                .maxDamage(item.getMaxDamage());

            if (item instanceof ArmorItem armorItem) {
                customItemBuilder.creativeCategory(3);
                customItemBuilder.protectionValue(armorItem.getDefense());
                switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> customItemBuilder.armorType("helmet").creativeGroup("itemGroup.name.helmet");
                    case CHEST -> customItemBuilder.armorType("chestplate").creativeGroup("itemGroup.name.chestplate");
                    case LEGS -> customItemBuilder.armorType("leggings").creativeGroup("itemGroup.name.leggings");
                    case FEET -> customItemBuilder.armorType("boots").creativeGroup("itemGroup.name.boots");
                }
            } else if (item instanceof TieredItem tieredItem) {
                customItemBuilder.creativeCategory(3);

                // TODO Support custom tiers
                customItemBuilder.toolTier("DIAMOND");
                if (tieredItem.getTier() instanceof Tiers) {
                    customItemBuilder.toolTier(tieredItem.getTier().toString());
                }

                // TODO Work out a nicer way of assigning groups based on classes
                if (item instanceof PickaxeItem) {
                    customItemBuilder.toolType("pickaxe").creativeGroup("itemGroup.name.pickaxe");
                } else if (item instanceof HoeItem) {
                    customItemBuilder.toolType("hoe").creativeGroup("itemGroup.name.hoe");
                } else if (item instanceof AxeItem) {
                    customItemBuilder.toolType("axe").creativeGroup("itemGroup.name.axe");
                } else if (item instanceof ShovelItem) {
                    customItemBuilder.toolType("shovel").creativeGroup("itemGroup.name.shovel");
                } else if (item instanceof SwordItem) {
                    customItemBuilder.toolType("sword").creativeGroup("itemGroup.name.sword");
                }
            } else if (item instanceof ShearsItem) {
                customItemBuilder.creativeCategory(3);
                customItemBuilder.toolType("shears").creativeGroup("itemGroup.name.equipment");
            }

            event.register(customItemBuilder.build());
        }
    }
}
