package org.geysermc.hydraulic.item;

import com.google.auto.service.AutoService;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
                .filter(item -> item.getDefaultInstance().has(DataComponents.EQUIPPABLE))
                .toList();

        context.logger().info("Armor to convert: " + armorItems.size() + " in mod " + context.mod().id());

        // enchanted_actor_glint.png is from https://github.com/Mojang/bedrock-samples/blob/main/resource_pack/textures/misc/enchanted_actor_glint.png
        try (InputStream stream = ArmorPackModule.class.getClassLoader().getResourceAsStream("textures/enchanted_actor_glint.png")) {
            context.bedrockResourcePack().addExtraFile(stream.readAllBytes(), "textures/misc/enchanted_actor_glint.png");
        } catch (IOException e) {
            context.logger().warn("Failed to load enchanted_actor_glint.png, enchanted armor will not have a glint effect");
        }

        for (Item armorItem : armorItems) {
            Equippable equippable = armorItem.components().get(DataComponents.EQUIPPABLE);

            ResourceLocation armorItemLocation = BuiltInRegistries.ITEM.getKey(armorItem);

            Optional<ResourceLocation> optionalResourceLocation = equippable.assetId().map(ResourceKey::location);

            if (optionalResourceLocation.isEmpty()) {
                continue; // TODO: Figure out what to do here, no texture means what??
            }

            ResourceLocation armorTextureLocation = optionalResourceLocation.get();

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

            description.textures(new HashMap<>() {
                {
                    put("default", String.format(BEDROCK_ARMOR_TEXTURE_LOCATION, context.mod().id(), (equippable.slot().equals(EquipmentSlot.LEGS) ? "humanoid_leggings" : "humanoid"), armorTextureLocation.getPath()));
                    put("enchanted", "textures/misc/enchanted_actor_glint");
                }
            });

            String geometryType = "";
            switch (equippable.slot()) {
                case EquipmentSlot.HEAD -> geometryType = "helmet";
                case EquipmentSlot.CHEST -> geometryType = "chestplate";
                case EquipmentSlot.LEGS -> geometryType = "leggings";
                case EquipmentSlot.FEET -> geometryType = "boots";
            }

            final String finalGeometryType = geometryType;
            description.geometry(Map.of("default", "geometry.player.armor." + finalGeometryType));

            Attachable attachable = new Attachable();
            attachable.description(description);
            armorAttachable.attachable(attachable);

            context.bedrockResourcePack().addAttachable(armorAttachable, "attachables/" + armorItemLocation.getPath() + ".json");
        }
    }

    @Override
    public boolean test(@NotNull PackPostProcessContext<ArmorPackModule> context) {
        return context.registryValues(BuiltInRegistries.ITEM).stream().anyMatch(item -> item.getDefaultInstance().has(DataComponents.EQUIPPABLE));
    }
}
