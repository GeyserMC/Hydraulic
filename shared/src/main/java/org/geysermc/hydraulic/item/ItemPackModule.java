package org.geysermc.hydraulic.item;

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
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.context.PackCreateContext;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@AutoService(PackModule.class)
public class ItemPackModule extends PackModule<ItemPackModule> {
    private static final String JAVA_ITEM_TEXTURE_LOCATION = "assets/%s/textures/item/%s.png";
    private static final String BEDROCK_ITEM_TEXTURE_LOCATION = "textures/items/%s/%s.png";

    public ItemPackModule() {
        this.listenOn(GeyserDefineCustomItemsEvent.class, ItemPackModule::onDefineCustomItems);
    }

    @Override
    public void create(@NotNull PackCreateContext<ItemPackModule> context) {
        List<Item> items = context.registryValues(Registries.ITEM);

        LOGGER.debug("Items to convert: " + items.size() + " in mod " + context.mod());
        Path jarPath = context.mod().modPath();

        for (Item item : items) {
            if (item instanceof BlockItem) {
                continue; // TODO: Special handling here?
            }

            ResourceLocation itemKey = BuiltInRegistries.ITEM.getKey(item);
            String outputLoc = String.format(BEDROCK_ITEM_TEXTURE_LOCATION, context.mod().id(), itemKey.getPath());
            context.pack().addItem(itemKey.toString(), outputLoc.replace(".png", ""));

            Path texturePath = jarPath.resolve(String.format(JAVA_ITEM_TEXTURE_LOCATION, context.mod().id(), itemKey.getPath()));
            if (Files.exists(texturePath)) {
                try {
                    Path outputPath = context.path().resolve(outputLoc);
                    if (Files.notExists(outputPath.getParent())) {
                        Files.createDirectories(outputPath.getParent());
                    }

                    Files.copy(texturePath, outputPath);
                    LOGGER.debug("Copied item texture {} for mod {}", texturePath, context.mod().id());
                } catch (IOException ex) {
                    LOGGER.error("Failed to copy item texture {} for mod {}", texturePath, context.mod().id(), ex);
                }
            } else {
                LOGGER.warn("Item texture {} not found for mod {}", texturePath, context.mod().id());
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
                    .icon(registry.getKey(item).getPath())
                    .javaId(registry.getId(item))
                    .stackSize(item.getMaxStackSize())
                    .maxDamage(item.getMaxDamage())
                    .allowOffhand(true)
                    .build());
        }
    }
}
