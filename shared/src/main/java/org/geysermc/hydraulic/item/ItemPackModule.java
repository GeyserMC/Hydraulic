package org.geysermc.hydraulic.item;

import com.google.auto.service.AutoService;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;
import org.geysermc.geyser.api.item.custom.NonVanillaCustomItemData;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.context.PackCreateContext;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AutoService(PackModule.class)
public class ItemPackModule extends PackModule<ItemPackModule> {

    public ItemPackModule() {
        this.listenOn(GeyserDefineCustomItemsEvent.class, ItemPackModule::onDefineCustomItems);
    }

    @Override
    public void create(@NotNull PackCreateContext<ItemPackModule> context) {
        List<Item> items = context.registryValues(Registries.ITEM);
        if (items.size() == 0) {
            return;
        }

        LOGGER.info("Items to convert: " + items.size() + " in mod " + context.mod());
        for (Item item : items) {
            ResourceLocation itemKey = BuiltInRegistries.ITEM.getKey(item);
            context.pack().addItem(itemKey.toString(), "textures/items/" + context.mod().id() + "/" + itemKey.getPath());
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
            String name = Language.getInstance().getOrDefault(item.getDescriptionId());

            event.register(NonVanillaCustomItemData.builder()
                    .name(name)
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
