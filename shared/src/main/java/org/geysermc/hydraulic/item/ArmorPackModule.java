package org.geysermc.hydraulic.item;

import com.google.auto.service.AutoService;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.Equippable;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.pack.bedrock.resource.attachables.Attachable;
import org.geysermc.pack.bedrock.resource.attachables.Attachables;
import org.geysermc.pack.bedrock.resource.attachables.attachable.Description;
import org.geysermc.pack.bedrock.resource.attachables.attachable.description.Scripts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.equipment.Equipment;
import team.unnamed.creative.equipment.EquipmentLayer;
import team.unnamed.creative.equipment.EquipmentLayerType;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AutoService(PackModule.class)
public class ArmorPackModule extends PackModule<ArmorPackModule> {
    private static final String BEDROCK_ARMOR_TEXTURE_LOCATION = "textures/entity/%s/equipment/%s/%s";

    private static final Map<String, String> ATTACHABLE_MATERIALS = new HashMap<>() {
        {
            put("default", "armor");
            put("enchanted", "armor_enchanted");
        }
    };
    private static final Scripts ATTACHABLE_SCRIPTS = new Scripts();

    static {
        ATTACHABLE_SCRIPTS.parentSetup("variable.chest_layer_visible = 0.0;");
    }

    public ArmorPackModule() {
        this.postProcess(this::postProcess);
    }

    private void postProcess(@NotNull PackPostProcessContext<ArmorPackModule> context) {
        List<Item> armorItems = context.registryValues(BuiltInRegistries.ITEM).stream()
                .filter(item -> item.components().has(DataComponents.EQUIPPABLE) && item.components().get(DataComponents.EQUIPPABLE).assetId().isPresent())
                .toList();

        context.logger().info("Armor to convert: {} in mod {}", armorItems.size(), context.mod().id());

        for (Item armorItem : armorItems) {
            Equippable equippable = armorItem.components().get(DataComponents.EQUIPPABLE);

            EquipmentLayerType layerType = getEquipmentLayer(equippable.slot());
            if (layerType == null) {
                // This might be something else... lets just check
                Optional<HolderSet<EntityType<?>>> optionalEntityType = equippable.allowedEntities();
                if (optionalEntityType.isPresent()) {
                    for (Holder<EntityType<?>> holderEntityType : optionalEntityType.get()) {
                        if (holderEntityType.is(EntityTypeTags.CAN_WEAR_HORSE_ARMOR)) {
                            layerType = EquipmentLayerType.HORSE_BODY;
                        } else if (holderEntityType.is(EntityType.getKey(EntityType.WOLF))) {
                            layerType = EquipmentLayerType.WOLF_BODY;
                        } else if (holderEntityType.is(EntityType.getKey(EntityType.LLAMA)) || holderEntityType.is(EntityType.getKey(EntityType.TRADER_LLAMA))) {
                            layerType = EquipmentLayerType.LLAMA_BODY;
                        }
                    }
                }

                if (layerType == null) { // We recheck as above can change how things go
                    continue; // There is no layer we can give the bedrock currently, so we can skip this
                }
            }

            ResourceLocation armorItemLocation = BuiltInRegistries.ITEM.getKey(armorItem);

            ResourceLocation armorTextureLocation = equippable.assetId().map(ResourceKey::location).orElseThrow(); // Checked above to ensure all armor processed has an asset id, so this shouldn't throw (This instead of get to prevent yellow lines)

            Equipment equipment = context.javaResourcePack().equipment(Key.key(armorTextureLocation.toString()));
            if (equipment == null) {
                continue;
            }
            List<EquipmentLayer> layers = equipment.layers().get(layerType);
            if (layers == null || layers.isEmpty()) {
                continue; // We have no layers that we can convert, so we can just skip this one
            }
            Key layerTexture = layers.getFirst().texture();

            Attachables armorAttachable = new Attachables();
            armorAttachable.formatVersion("1.10.0");

            Description description = new Description();
            description.identifier(armorItemLocation.toString());
            description.materials(ATTACHABLE_MATERIALS);
            description.scripts(ATTACHABLE_SCRIPTS);
            description.renderControllers(new String[] { "controller.render.armor" });

            // Change the query to match the item
            // This should always work as armour should have 2d item models
            // If its 3d this will break as the item won't have the `item.` prefix
            // TODO Register another attachable for 3d items? Or just work out which is correct from here
            Map<String, String> items = new HashMap<>() {{
                put(armorItemLocation + "_item", "query.owner_identifier == 'minecraft:player'");
            }};
            description.item(items);

            EquipmentLayerType finalLayerType = layerType;
            description.textures(new HashMap<>() {
                {
                    put("default", String.format(BEDROCK_ARMOR_TEXTURE_LOCATION, layerTexture.namespace(), finalLayerType.name().toLowerCase(), layerTexture.value()));
                    put("enchanted", "textures/misc/enchanted_actor_glint");
                }
            });

            String geometryType = "";
            switch (layerType) {
                case EquipmentLayerType.HUMANOID -> {
                    switch (equippable.slot()) {
                        case EquipmentSlot.HEAD -> geometryType = "geometry.player.armor.helmet";
                        case EquipmentSlot.CHEST -> geometryType = "geometry.player.armor.chestplate";
                        case EquipmentSlot.FEET -> geometryType = "geometry.player.armor.boots";
                    }
                }
                case EquipmentLayerType.HUMANOID_LEGGINGS -> geometryType = "geometry.player.armor.leggings";
                case EquipmentLayerType.HORSE_BODY -> {} // TODO: Handle adding horse armor, might need to PR geyser for the slot
            }

            description.geometry(Map.of("default", geometryType));

            Attachable attachable = new Attachable();
            attachable.description(description);
            armorAttachable.attachable(attachable);

            context.bedrockResourcePack().addAttachable(armorAttachable, "attachables/" + armorItemLocation.getPath() + ".json");
        }
    }

    @Override
    public boolean test(@NotNull PackPostProcessContext<ArmorPackModule> context) {
        return context.registryValues(BuiltInRegistries.ITEM).stream().anyMatch(item -> item.components().has(DataComponents.EQUIPPABLE) && item.components().get(DataComponents.EQUIPPABLE).assetId().isPresent());
    }

    private static @Nullable EquipmentLayerType getEquipmentLayer(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD, CHEST, FEET -> EquipmentLayerType.HUMANOID;
            case LEGS -> EquipmentLayerType.HUMANOID_LEGGINGS;
            default -> null;
        };
    }
}
