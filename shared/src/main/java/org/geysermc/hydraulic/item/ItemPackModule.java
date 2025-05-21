package org.geysermc.hydraulic.item;

import com.google.auto.service.AutoService;
import net.kyori.adventure.key.Key;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;
import org.geysermc.geyser.api.exception.CustomItemDefinitionRegisterException;
import org.geysermc.geyser.api.item.custom.v2.CustomItemBedrockOptions;
import org.geysermc.geyser.api.item.custom.v2.NonVanillaCustomItemDefinition;
import org.geysermc.geyser.api.item.custom.v2.component.*;
import org.geysermc.geyser.api.util.CreativeCategory;
import org.geysermc.geyser.api.util.Identifier;
import org.geysermc.hydraulic.pack.PackLogListener;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.TexturePackModule;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.hydraulic.pack.context.PackPreProcessContext;
import org.geysermc.hydraulic.pack.converter.ComponentConverter;
import org.geysermc.hydraulic.util.HydraulicKey;
import org.geysermc.hydraulic.util.PackUtil;
import org.geysermc.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.pack.converter.converter.model.ModelStitcher;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.model.ModelTexture;

import java.util.*;

@AutoService(PackModule.class)
public class ItemPackModule extends TexturePackModule<ItemPackModule> {
    private final List<ResourceLocation> handheldItems = new ArrayList<>();
    private final Map<String, String> itemBuiltinTexture = new HashMap<>();

    public ItemPackModule() {
        this.listenOn(GeyserDefineCustomItemsEvent.class, this::onDefineCustomItems);

        this.preProcess(this::preProcess);
        this.postProcess(this::postProcess);
    }

    private void preProcess(@NotNull PackPreProcessContext<ItemPackModule> context) {
        for (Model model : context.assets(ResourcePack::models)) {
            // If the parent is item/generated, it's a 2D icon
            Key modelParent = model.parent();
            if (modelParent != null) {
                if (modelParent.value().equals("item/handheld")) {
                    HydraulicKey key = HydraulicKey.of(model.key());
                    key.path(key.path().replace("/item", ""));
                    handheldItems.add(key.location());
                }
            }
        }

        List<Item> items = context.registryValues(BuiltInRegistries.ITEM);
        PackLogListener packLogListener = new PackLogListener(context.logger());
        for (Item item : items) {
            ResourceLocation itemLocation = BuiltInRegistries.ITEM.getKey(item);

            Model baseModel = context.modelProvider().model(Key.key(itemLocation.getNamespace(), "item/" + itemLocation.getPath()));
            if (baseModel == null) {
                continue;
            }

            Model model = new ModelStitcher(context.modelProvider(), baseModel, packLogListener).stitch();
            if (model == null) {
                continue;
            }

            List<ModelTexture> layers = model.textures().layers();
            if (layers == null || layers.isEmpty()) {
                continue;
            }

            ModelTexture layer0 = layers.getFirst();

            if (layer0.key().namespace().equals(Key.MINECRAFT_NAMESPACE)) {
                itemBuiltinTexture.put(itemLocation.toString(), PackUtil.getTextureName(layer0.key().toString()));
            }
        }
    }

    private void postProcess(@NotNull PackPostProcessContext<ItemPackModule> context) {
        ResourcePack assets = context.javaResourcePack();
        BedrockResourcePack bedrockPack = context.bedrockResourcePack();

        List<Item> items = context.registryValues(BuiltInRegistries.ITEM);

        context.logger().info("Items to convert: {} in mod {}", items.size(), context.mod().id());

        PackLogListener packLogListener = new PackLogListener(context.logger());
        for (Item item : items) {
            ResourceLocation itemLocation = BuiltInRegistries.ITEM.getKey(item);

            Model baseModel = assets.model(Key.key(itemLocation.getNamespace(), "item/" + itemLocation.getPath()));
            if (baseModel == null) {
                context.logger().warn("Item {} has no item model, skipping", itemLocation);
                continue;
            }

            Model model = new ModelStitcher(context.modelProvider(), baseModel, packLogListener).stitch();

            List<ModelTexture> layers = model.textures().layers();
            if (layers == null || layers.isEmpty()) {
                // Don't warn if a block as they can use the block model
                if (!(item instanceof BlockItem)) {
                    context.logger().warn("Item {} has no layer0 texture, skipping", itemLocation);
                }

                continue;
            }

            ModelTexture layer0 = layers.getFirst();
            String outputLoc = getOutputFromModel(context, layer0.key());
            bedrockPack.addItemTexture(itemLocation.toString(), outputLoc.replace(".png", ""));
        }
    }

    @Override
    public boolean test(@NotNull PackPostProcessContext<ItemPackModule> context) {
        return !context.registryValues(BuiltInRegistries.ITEM).isEmpty();
    }

    private void onDefineCustomItems(PackEventContext<GeyserDefineCustomItemsEvent, ItemPackModule> context) {
        GeyserDefineCustomItemsEvent event = context.event();
        List<Item> items = context.registryValues(BuiltInRegistries.ITEM);

        DefaultedRegistry<Item> registry = BuiltInRegistries.ITEM;
        for (Item item : items) {
            ResourceLocation itemLocation = registry.getKey(item);

            NonVanillaCustomItemDefinition.Builder customItemDefinition = NonVanillaCustomItemDefinition.builder(
                    Identifier.of(itemLocation.toString()),
                    Identifier.of(itemLocation.toString()),
                    registry.getId(item)
            )
                    .displayName("%" + item.getDescriptionId())
                    .component(DataComponent.MAX_STACK_SIZE, item.getDefaultMaxStackSize());

            CustomItemBedrockOptions.Builder customItemOptions = CustomItemBedrockOptions.builder()
                    .allowOffhand(true)
                    .icon(itemLocation.toString());

            // Allow minecraft namespace texture to be used (remapped as hydraulic)
            if (itemBuiltinTexture.containsKey(itemLocation.toString())) {
                customItemOptions.icon(itemBuiltinTexture.get(itemLocation.toString()));
            }

            // Make it handheld if need be
            if (handheldItems.contains(itemLocation)) {
                customItemOptions.displayHandheld(true);
            }

            // Set the creative mappings
            CreativeMappings.setup(item, customItemOptions);

            // Set all bedrock components using what java components we have
            ComponentConverter.setGeyserComponents(
                    item.components(),
                    customItemDefinition,
                    customItemOptions
            );

            if (item instanceof BowItem) {
                customItemDefinition.component(GeyserDataComponent.CHARGEABLE, new Chargeable(
                        1f,
                        false
                ));
            }
            if (item instanceof CrossbowItem) {
                customItemDefinition.component(GeyserDataComponent.CHARGEABLE, new Chargeable(
                        0f,
                        true
                ));
            }

            if (item instanceof BlockItem blockItem) {
                // Set the block_placer component to the correct block
                // This fixes animations sometimes not showing
                Block block = blockItem.getBlock();

                customItemDefinition.component(GeyserDataComponent.BLOCK_PLACER, new BlockPlacer(Identifier.of(BuiltInRegistries.BLOCK.getKey(block).toString()), true));

                CreativeMappings.setupBlock(block, customItemOptions);
            }

            customItemDefinition.bedrockOptions(customItemOptions);

            try {
                event.register(customItemDefinition.build());
            } catch (CustomItemDefinitionRegisterException e) {
                // TODO: Handle this exception, perhaps make it for PackModule?
            }
        }
    }
}
