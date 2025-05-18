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
    private final List<String> itemsWith2dIcon = new ArrayList<>();
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
            if (modelParent != null && modelParent.value().equals("item/generated")) {
                itemsWith2dIcon.add(model.key().toString().replace("item/", ""));
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
            DataComponentMap components = item.components();

            // Check if the item has a 2D icon
            //boolean flatIcon = itemsWith2dIcon.contains(itemLocation.toString());
            NonVanillaCustomItemDefinition.Builder customItemDefinition = NonVanillaCustomItemDefinition.builder(
                    Identifier.of(itemLocation.toString()),
                    Identifier.of(itemLocation.toString()/* + (flatIcon ? "_item" : "")*/), // Lets do a little test... Doesn't seem needed
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

            // Enchantment glint by default
            if (item.isFoil(item.getDefaultInstance())) {
                customItemDefinition.component(DataComponent.ENCHANTMENT_GLINT_OVERRIDE, true);
            }

            net.minecraft.world.food.FoodProperties foodProperties = components.get(DataComponents.FOOD);
            if (foodProperties != null) {
                customItemOptions
                        .creativeCategory(CreativeCategory.EQUIPMENT)
                        .creativeGroup("itemGroup.name.miscFood");

                net.minecraft.world.item.component.Consumable consumable = components.get(DataComponents.CONSUMABLE);
                if (consumable != null) {
                    Consumable.Animation animation;
                    try { // The names mostly line up, but bedrock is sadly missing 2, so we need to fallback
                        animation = Consumable.Animation.valueOf(consumable.animation().getSerializedName().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        animation = Consumable.Animation.EAT;
                    }

                    customItemDefinition.component(
                            DataComponent.CONSUMABLE,
                            new Consumable(consumable.consumeSeconds(), animation)
                    );
                } else {
                    customItemDefinition.component(
                            DataComponent.CONSUMABLE,
                            new Consumable(1.61f, Consumable.Animation.EAT) // Default for food, needed so bedrock will actually play animation
                    );
                }

                customItemDefinition.component(
                        DataComponent.FOOD,
                        new FoodProperties(foodProperties.nutrition(), foodProperties.saturation(), foodProperties.canAlwaysEat())
                );
            }

            CreativeMappings.setup(item, customItemOptions);

            net.minecraft.world.item.equipment.Equippable equippable = components.get(DataComponents.EQUIPPABLE);
            if (equippable != null) {
                ItemAttributeModifiers itemAttributeModifiers = components.get(DataComponents.ATTRIBUTE_MODIFIERS);
                if (itemAttributeModifiers != null) {
                    customItemOptions.protectionValue((int) itemAttributeModifiers.compute(0, equippable.slot()));
                }

                customItemOptions.tag(Identifier.of("minecraft", "is_armor"));

//                switch (item.getDefaultInstance().get(DataComponents.EQUIPPABLE).slot()) {
//                    case HEAD -> customItemOptions.armorType("helmet").creativeGroup("itemGroup.name.helmet");
//                    case CHEST -> customItemOptions.armorType("chestplate").creativeGroup("itemGroup.name.chestplate");
//                    case LEGS -> customItemOptions.armorType("leggings").creativeGroup("itemGroup.name.leggings");
//                    case FEET -> customItemOptions.armorType("boots").creativeGroup("itemGroup.name.boots");
//                }
                switch (equippable.slot()) {
                    case HEAD -> {
                        customItemDefinition.component(DataComponent.EQUIPPABLE, new Equippable(Equippable.EquipmentSlot.HEAD));
                        customItemOptions.creativeGroup("itemGroup.name.helmet");
                    }
                    case CHEST -> {
                        customItemDefinition.component(DataComponent.EQUIPPABLE, new Equippable(Equippable.EquipmentSlot.CHEST));
                        customItemOptions.creativeGroup("itemGroup.name.chestplate");
                    }
                    case LEGS -> {
                        customItemDefinition.component(DataComponent.EQUIPPABLE, new Equippable(Equippable.EquipmentSlot.LEGS));
                        customItemOptions.creativeGroup("itemGroup.name.leggings");
                    }
                    case FEET -> {
                        customItemDefinition.component(DataComponent.EQUIPPABLE, new Equippable(Equippable.EquipmentSlot.FEET));
                        customItemOptions.creativeGroup("itemGroup.name.boots");
                    }
                    case BODY -> {
                        customItemDefinition.component(DataComponent.EQUIPPABLE, new Equippable(Equippable.EquipmentSlot.BODY));
                        //customItemOptions.creativeGroup("itemGroup.name.helmet"); // TODO: Find this creative group
                    }
                    case SADDLE -> {
                        customItemDefinition.component(DataComponent.EQUIPPABLE, new Equippable(Equippable.EquipmentSlot.SADDLE));
                        //customItemOptions.creativeGroup("itemGroup.name.helmet"); // TODO: Find this creative group
                    }
                }
            }

            Tool tool = components.get(DataComponents.TOOL);
            if (tool != null) {
                customItemOptions.displayHandheld(true); // So we hold the tool right

                customItemDefinition.component(DataComponent.TOOL, new ToolProperties(tool.canDestroyBlocksInCreative()));
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
