package org.geysermc.hydraulic.item;

import com.google.auto.service.AutoService;
import net.kyori.adventure.key.Key;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;
import org.geysermc.geyser.api.item.custom.NonVanillaCustomItemData;
import org.geysermc.geyser.api.util.CreativeCategory;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            if (model.parent() != null && model.parent().value().equals("item/generated")) {
                itemsWith2dIcon.add(model.key().toString().replace("item/", ""));
            }
        }

        List<Item> items = context.registryValues(Registries.ITEM);
        PackLogListener packLogListener = new PackLogListener(context.logger());
        for (Item item : items) {
            ResourceLocation itemLocation = BuiltInRegistries.ITEM.getKey(item);

            Model baseModel = context.modelProvider().model(Key.key(itemLocation.getNamespace(), "item/" + itemLocation.getPath()));
            if (baseModel == null) {
                continue;
            }

            Model model = new ModelStitcher(context.modelProvider(), baseModel, packLogListener).stitch();
            if (model == null || model.textures() == null) {
                continue;
            }

            List<ModelTexture> layers = model.textures().layers();
            if (layers == null || layers.isEmpty()) {
                continue;
            }

            ModelTexture layer0 = layers.get(0);

            if (layer0.key().namespace().equals(Key.MINECRAFT_NAMESPACE)) {
                itemBuiltinTexture.put(itemLocation.toString(), PackUtil.getTextureName(layer0.key().toString()));
            }
        }
    }

    private void postProcess(@NotNull PackPostProcessContext<ItemPackModule> context) {
        ResourcePack assets = context.javaResourcePack();
        BedrockResourcePack bedrockPack = context.bedrockResourcePack();

        List<Item> items = context.registryValues(Registries.ITEM);

        context.logger().info("Items to convert: " + items.size() + " in mod " + context.mod().id());

        PackLogListener packLogListener = new PackLogListener(context.logger());
        for (Item item : items) {
            ResourceLocation itemLocation = BuiltInRegistries.ITEM.getKey(item);

            Model baseModel = assets.model(Key.key(itemLocation.getNamespace(), "item/" + itemLocation.getPath()));
            if (baseModel == null) {
                context.logger().warn("Item {} has no item model, skipping", itemLocation);
                continue;
            }
            Model model = new ModelStitcher(context.modelProvider(), baseModel, packLogListener).stitch();
            if (model == null || model.textures() == null) {
                context.logger().warn("Item {} has no item model, skipping", itemLocation);
                continue;
            }

            List<ModelTexture> layers = model.textures().layers();
            if (layers == null || layers.isEmpty()) {
                // Don't warn if a block as they can use the block model
                if (!(item instanceof BlockItem)) {
                    context.logger().warn("Item {} has no layer0 texture, skipping", itemLocation);
                }

                continue;
            }

            ModelTexture layer0 = layers.get(0);
            String outputLoc = getOutputFromModel(context, layer0.key());
            bedrockPack.addItemTexture(itemLocation.toString(), outputLoc.replace(".png", ""));
        }
    }

    @Override
    public boolean test(@NotNull PackPostProcessContext<ItemPackModule> context) {
        return !context.registryValues(Registries.ITEM).isEmpty();
    }

    private void onDefineCustomItems(PackEventContext<GeyserDefineCustomItemsEvent, ItemPackModule> context) {
        GeyserDefineCustomItemsEvent event = context.event();
        List<Item> items = context.registryValues(Registries.ITEM);

        DefaultedRegistry<Item> registry = BuiltInRegistries.ITEM;
        for (Item item : items) {
            ResourceLocation itemLocation = registry.getKey(item);

            // Check if the item has a 2D icon
            boolean flatIcon = itemsWith2dIcon.contains(itemLocation.toString());
            NonVanillaCustomItemData.Builder customItemBuilder = NonVanillaCustomItemData.builder()
                .name(itemLocation.getPath())
                .displayName("%" + item.getDescriptionId())
                .identifier(itemLocation + (flatIcon ? "_item" : ""))
                .icon(itemLocation.toString())
                .javaId(registry.getId(item))
                .stackSize(item.getDefaultMaxStackSize())
                .maxDamage(item.getDefaultInstance().getMaxDamage())
                .allowOffhand(true);

            // Allow minecraft namespace texture to be used (remapped as hydraulic)
            if (itemBuiltinTexture.containsKey(itemLocation.toString())) {
                customItemBuilder.icon(itemBuiltinTexture.get(itemLocation.toString()));
            }

            // Enchantment glint by default
            if (item.isFoil(item.getDefaultInstance())) {
                customItemBuilder.foil(true);
            }

            if (item.components().has(DataComponents.FOOD)) {
                customItemBuilder
                        .creativeCategory(CreativeCategory.EQUIPMENT.id())
                        .creativeGroup("itemGroup.name.miscFood")
                        .edible(true);

                FoodProperties foodProperties = item.components().get(DataComponents.FOOD);
                if (foodProperties != null) {
                    customItemBuilder
                            .canAlwaysEat(foodProperties.canAlwaysEat());
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
                customItemBuilder.toolType("bow");
                customItemBuilder.chargeable(true);
            } else if (item instanceof BlockItem) {
                // Set the block_placer component to the correct block
                // This fixes animations sometimes not showing
                customItemBuilder.block(itemLocation.toString());

                Block block = BuiltInRegistries.BLOCK.get(itemLocation);
                CreativeMappings.setupBlock(block, customItemBuilder);
            }

            event.register(customItemBuilder.build());
        }
    }
}
