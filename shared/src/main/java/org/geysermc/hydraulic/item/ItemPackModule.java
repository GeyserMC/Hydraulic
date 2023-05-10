package org.geysermc.hydraulic.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.service.AutoService;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;
import org.geysermc.geyser.api.item.custom.NonVanillaCustomItemData;
import org.geysermc.hydraulic.assets.Model;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.context.PackCreateContext;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.geysermc.hydraulic.util.Constants;
import org.geysermc.hydraulic.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@AutoService(PackModule.class)
public class ItemPackModule extends PackModule<ItemPackModule> {
    private static final String JAVA_ITEM_TEXTURE_LOCATION = "assets/%s/textures/item/%s.png";
    private static final String BEDROCK_ITEM_TEXTURE_LOCATION = "textures/items/%s/%s.png";
    private static final String JAVA_ITEM_MODEL_LOCATION = "assets/%s/models/item/%s.json";

    public ItemPackModule() {
        this.listenOn(GeyserDefineCustomItemsEvent.class, ItemPackModule::onDefineCustomItems);
    }

    @Override
    public void create(@NotNull PackCreateContext<ItemPackModule> context) {
        List<Item> items = context.registryValues(Registries.ITEM);

        LOGGER.info("Items to convert: " + items.size() + " in mod " + context.mod());
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

            event.register(NonVanillaCustomItemData.builder()
                    .name(registry.getKey(item).getPath())
                    .displayName(name)
                    .identifier(registry.getKey(item).toString())
                    .icon(registry.getKey(item).toString())
                    .javaId(registry.getId(item))
                    .stackSize(item.getMaxStackSize())
                    .maxDamage(item.getMaxDamage())
                    .allowOffhand(true)
                    .creativeCategory(4) // 4 - "Items"
                    .creativeGroup("")
                    .build());
        }
    }
}
