package org.geysermc.hydraulic.pack.converter;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.world.entity.EquipmentSlot;
import org.geysermc.geyser.api.item.custom.v2.CustomItemBedrockOptions;
import org.geysermc.geyser.api.item.custom.v2.CustomItemDefinition;
import org.geysermc.geyser.api.item.custom.v2.component.*;
import org.geysermc.geyser.api.util.CreativeCategory;
import org.geysermc.geyser.api.util.Identifier;
import org.geysermc.hydraulic.util.HydraulicKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ComponentConverter {
    private static final Map<DataComponentType<?>, Converter<Object>> COMPONENT_CONVERT_MAP = new HashMap<>();

    private static <T> void addComponentConversion(DataComponentType<T> dataComponent, Converter<T> conversion) {
        COMPONENT_CONVERT_MAP.put(dataComponent, (Converter<Object>) conversion);
    }

    public static void setGeyserComponents(DataComponentMap componentMap, CustomItemDefinition.Builder definition, CustomItemBedrockOptions.Builder options) {
        for (TypedDataComponent<?> dataComponent : componentMap) {
            DataComponentType<?> dataComponentType = dataComponent.type();
            Converter<Object> triConsumer = COMPONENT_CONVERT_MAP.get(dataComponentType);
            if (triConsumer != null) {
                triConsumer.convert(dataComponent.value(), componentMap, definition, options);
            }
        }
    }

    static {
        addComponentConversion(DataComponents.MAX_STACK_SIZE, (component, map, definition, options) -> {
            definition.component(DataComponent.MAX_STACK_SIZE, component);
        });
        addComponentConversion(DataComponents.MAX_DAMAGE, (component, map, definition, options) -> {
            definition.component(DataComponent.MAX_DAMAGE, component);
        });
        addComponentConversion(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, (component, map, definition, options) -> {
            definition.component(DataComponent.ENCHANTMENT_GLINT_OVERRIDE, component);
        });
        addComponentConversion(DataComponents.FOOD, (component, map, definition, options) -> {
            definition.component(DataComponent.FOOD, new FoodProperties(component.nutrition(), component.saturation(), component.canAlwaysEat()));
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
            definition.component(DataComponent.CONSUMABLE, new Consumable(component.consumeSeconds(), animation));
        });
        addComponentConversion(DataComponents.USE_COOLDOWN, (component, map, definition, options) -> {
            Identifier location = component.cooldownGroup().map(HydraulicKey::of).orElse(null);
            definition.component(DataComponent.USE_COOLDOWN, new UseCooldown(component.seconds(), location));
        });
        addComponentConversion(DataComponents.TOOL, (component, map, definition, options) -> {
            definition.component(DataComponent.TOOL, new ToolProperties(component.canDestroyBlocksInCreative()));
        });
        addComponentConversion(DataComponents.ENCHANTABLE, (component, map, definition, options) -> {
            definition.component(DataComponent.ENCHANTABLE, component.value());
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
                definition.component(DataComponent.EQUIPPABLE, new Equippable(slot));
        });
        addComponentConversion(DataComponents.REPAIRABLE, (component, map, definition, options) -> {
            definition.component(DataComponent.REPAIRABLE, new Repairable(
                    component.items().stream()
                            .map(Holder::unwrapKey)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .map(HydraulicKey::of)
                            .toList()
                            .toArray(new Identifier[]{})
            ));
        });
        addComponentConversion(DataComponents.ATTRIBUTE_MODIFIERS, (component, map, definition, options) -> {
            net.minecraft.world.item.equipment.Equippable equippable = map.get(DataComponents.EQUIPPABLE); // only one stinky inline import. thanks "protectionValue" :pensive:
            if (equippable != null)
                options.protectionValue((int) component.compute(0, equippable.slot()));

            definition.component(GeyserDataComponent.ATTACK_DAMAGE, (int) component.compute(0, EquipmentSlot.MAINHAND));
        });
    }

    @FunctionalInterface
    private interface Converter<T> {
        void convert(T value, DataComponentMap componentMap, CustomItemDefinition.Builder definition, CustomItemBedrockOptions.Builder options);
    }
}
