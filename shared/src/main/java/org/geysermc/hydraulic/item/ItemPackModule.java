package org.geysermc.hydraulic.item;

import com.google.auto.service.AutoService;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;
import org.geysermc.geyser.api.item.custom.NonVanillaCustomItemData;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.context.PackCreateContext;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.jetbrains.annotations.NotNull;

import javax.tools.Tool;
import java.util.List;

@AutoService(PackModule.class)
public class ItemPackModule extends PackModule<ItemPackModule> {

    public ItemPackModule() {
        this.listenOn(GeyserDefineCustomItemsEvent.class, ItemPackModule::onDefineCustomItems);
    }

    @Override
    public void create(@NotNull PackCreateContext<ItemPackModule> context) {
        List<Item> items = context.registryValues(Registries.ITEM);
        if (items.size() > 1) {
            LOGGER.info("Items to convert: " + items.size() + " in mod " + context.mod());
            for (Item item : items) {
                LOGGER.info("Item: " + item.getDescriptionId());
            }
        }
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
            event.register(NonVanillaCustomItemData.builder()
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
