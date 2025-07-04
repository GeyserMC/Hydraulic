package org.geysermc.hydraulic.component;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import org.geysermc.geyser.api.item.custom.v2.CustomItemBedrockOptions;
import org.geysermc.geyser.api.item.custom.v2.CustomItemDefinition;
import org.geysermc.geyser.api.item.custom.v2.component.*;
import org.geysermc.geyser.api.item.custom.v2.component.geyser.GeyserDataComponent;
import org.geysermc.geyser.api.item.custom.v2.component.java.*;
import org.geysermc.geyser.api.util.CreativeCategory;
import org.geysermc.geyser.api.util.Holders;
import org.geysermc.geyser.api.util.Identifier;
import org.geysermc.hydraulic.util.HydraulicKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ComponentConverter {
    private static final Map<DataComponentType<?>, Converter<Object>> COMPONENT_CONVERT_MAP = new HashMap<>();

    private static <T> void addComponentConversion(DataComponentType<T> dataComponent, Converter<T> conversion) {
        // Yellow lines make me sad, this is safe, so no worries
        //noinspection unchecked
        COMPONENT_CONVERT_MAP.put(dataComponent, (Converter<Object>) conversion);
    }

    private static <T> void addSimpleConversion(DataComponentType<T> javaDataComponent, DataComponent<T> bedrock) {
        addComponentConversion(javaDataComponent, (component, map, definition, options) -> {
            definition.component(bedrock, component);
        });
    }

    public static void setGeyserComponents(DataComponentMap componentMap, CustomItemDefinition.Builder definition, CustomItemBedrockOptions.Builder options) {
        for (TypedDataComponent<?> dataComponent : componentMap) {
            DataComponentType<?> dataComponentType = dataComponent.type();
            Optional<Converter<Object>> potentialConverter = Optional.ofNullable(COMPONENT_CONVERT_MAP.get(dataComponentType));
            potentialConverter.ifPresent(
                    converter ->
                            converter.convert(
                                    dataComponent.value(),
                                    componentMap,
                                    definition,
                                    options
                            )
            );
        }
    }

    static {
        // Same types, so we can just passthrough these values
        addSimpleConversion(DataComponents.MAX_STACK_SIZE, ItemDataComponents.MAX_STACK_SIZE);
        addSimpleConversion(DataComponents.MAX_DAMAGE, ItemDataComponents.MAX_DAMAGE);
        addSimpleConversion(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, ItemDataComponents.ENCHANTMENT_GLINT_OVERRIDE);

        // These components are a little different or too complex, so we need a more powerful conversion
        addComponentConversion(DataComponents.FOOD, (component, map, definition, options) -> {
            definition.component(
                    ItemDataComponents.FOOD,
                    FoodProperties.of(component.nutrition(), component.saturation(), component.canAlwaysEat())
            );
        });
        addComponentConversion(DataComponents.CONSUMABLE, (component, map, definition, options) -> {
            Consumable.Animation animation = switch (component.animation()) {
                case NONE -> Consumable.Animation.NONE;
                case DRINK -> Consumable.Animation.DRINK;
                case BLOCK -> Consumable.Animation.BLOCK;
                case BOW -> Consumable.Animation.BOW;
                case SPEAR -> Consumable.Animation.SPEAR;
                case SPYGLASS -> Consumable.Animation.SPYGLASS;
                case BRUSH -> Consumable.Animation.BRUSH;
                default -> Consumable.Animation.EAT;
            };
            definition.component(
                    ItemDataComponents.CONSUMABLE,
                    Consumable.of(component.consumeSeconds(), animation)
            );
        });
        addComponentConversion(DataComponents.USE_COOLDOWN, (component, map, definition, options) -> {
            Identifier location = component.cooldownGroup().map(HydraulicKey::of).orElse(null);
            definition.component(
                    ItemDataComponents.USE_COOLDOWN,
                    UseCooldown.builder()
                            .seconds(component.seconds())
                            .cooldownGroup(location)
            );
        });
        addComponentConversion(DataComponents.TOOL, (component, map, definition, options) -> {
            ToolProperties.Builder toolProperties = ToolProperties.builder()
                    .canDestroyBlocksInCreative(component.canDestroyBlocksInCreative())
                    .defaultMiningSpeed(component.defaultMiningSpeed());

            for (Tool.Rule toolRule : component.rules()) {
                if (toolRule.speed().isEmpty()) continue;

                toolProperties.rule(
                        ToolProperties.Rule.of(toHolders(toolRule.blocks()), toolRule.speed().get())
                );
            }

            definition.component(
                    ItemDataComponents.TOOL,
                    toolProperties
            );
        });
        addComponentConversion(DataComponents.ENCHANTABLE, (component, map, definition, options) -> {
            definition.component(ItemDataComponents.ENCHANTABLE, component.value());
        });
        addComponentConversion(DataComponents.EQUIPPABLE, (component, map, definition, options) -> {
            options.tag(Identifier.of("minecraft", "is_armor"));
            options.creativeCategory(CreativeCategory.EQUIPMENT);

            Equippable.EquipmentSlot slot = switch (component.slot()) {
                case FEET -> {
                    options.creativeGroup("itemGroup.name.boots");
                    yield Equippable.EquipmentSlot.FEET;
                }
                case LEGS -> {
                    options.creativeGroup("itemGroup.name.leggings");
                    yield Equippable.EquipmentSlot.LEGS;
                }
                case CHEST -> {
                    options.creativeGroup("itemGroup.name.chestplate");
                    yield Equippable.EquipmentSlot.CHEST;
                }
                case HEAD -> {
                    options.creativeGroup("itemGroup.name.helmet");
                    yield Equippable.EquipmentSlot.HEAD;
                }
                case BODY -> Equippable.EquipmentSlot.BODY;
                case SADDLE -> Equippable.EquipmentSlot.SADDLE;
                default -> null;
            };
            if (slot != null)
                definition.component(
                        ItemDataComponents.EQUIPPABLE,
                        Equippable.of(slot)
                );
        });
        addComponentConversion(DataComponents.REPAIRABLE, (component, map, definition, options) -> {
            Repairable.Builder repairableComponent = Repairable.builder();

            repairableComponent.items(toHolders(component.items()));

            definition.component(
                    ItemDataComponents.REPAIRABLE,
                    repairableComponent
            );
        });
        addComponentConversion(DataComponents.ATTRIBUTE_MODIFIERS, (component, map, definition, options) -> {
            net.minecraft.world.item.equipment.Equippable equippable = map.get(DataComponents.EQUIPPABLE); // only one stinky inline import. thanks "protectionValue" :pensive:
            if (equippable != null)
                options.protectionValue((int) component.compute(0, equippable.slot()));

            definition.component(GeyserDataComponent.ATTACK_DAMAGE, Math.max(0, (int) component.compute(0, EquipmentSlot.MAINHAND)));
        });
    }

    private static Holders toHolders(HolderSet<?> holderSet) {
        return holderSet.unwrap()
                .map(
                        tag -> Holders.ofTag(HydraulicKey.of(tag.location())),
                        holders -> Holders.of(
                                holders.stream()
                                        .map(holder -> (Identifier) HydraulicKey.of(
                                                holder.unwrapKey().map(ResourceKey::location)
                                                        .orElseThrow()
                                        ))
                                        .toList()
                        )
                );
    }

    @FunctionalInterface
    private interface Converter<T> {
        void convert(T value, DataComponentMap componentMap, CustomItemDefinition.Builder definition, CustomItemBedrockOptions.Builder options);
    }
}
