package org.geysermc.hydraulic.item;

import com.google.auto.service.AutoService;
import net.kyori.adventure.key.Key;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
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
                    HolderSet<EntityType<?>> entityTypeHolderSet = optionalEntityType.get();

                    if (entityTypeHolderSet.contains(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityTypes.HORSE))) {
                        layerType = EquipmentLayerType.HORSE_BODY;
                    } else if (entityTypeHolderSet.contains(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityTypes.WOLF))) {
                        layerType = EquipmentLayerType.WOLF_BODY;
                    } else if (entityTypeHolderSet.contains(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityTypes.LLAMA))) {
                        layerType = EquipmentLayerType.LLAMA_BODY;
                    }
                }

                if (layerType == null) { // We recheck as above can change how things go
                    continue; // There is no layer we can give the bedrock currently, so we can skip this
                }
            }

            Identifier armorItemLocation = BuiltInRegistries.ITEM.getKey(armorItem);

            Identifier armorTextureLocation = equippable.assetId().map(ResourceKey::identifier).orElseThrow(); // Checked above to ensure all armor processed has an asset id, so this shouldn't throw (This instead of get to prevent yellow lines)

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

            // Change the query to match the item. Both the raw item id and the "<id>_item" form are
            // registered: Geyser's custom block items use the _item suffix, while plain custom items
            // (2D icons and 3D models alike) keep their bedrock identifier as-is.
            Map<String, String> items = new HashMap<>() {{
                put(armorItemLocation + "_item", "query.owner_identifier == 'minecraft:player'");
                put(armorItemLocation.toString(), "query.owner_identifier == 'minecraft:player'");
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
                case EquipmentLayerType.HORSE_BODY -> {
                    // Bedrock renders horse armor from the item id with the vanilla horse armor
                    // geometry; the texture layer above already points at the converted texture.
                    // End-to-end still needs Geyser to translate the horse equipment slot.
                    geometryType = "geometry.horse.armor";
                }
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
