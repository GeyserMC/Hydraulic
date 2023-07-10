package org.geysermc.hydraulic.item;

import com.google.auto.service.AutoService;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.pack.bedrock.resource.attachables.Attachable;
import org.geysermc.pack.bedrock.resource.attachables.Attachables;
import org.geysermc.pack.bedrock.resource.attachables.attachable.Description;
import org.geysermc.pack.bedrock.resource.attachables.attachable.description.Scripts;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoService(PackModule.class)
public class ArmorPackModule extends PackModule<ArmorPackModule> {
    private static final String BEDROCK_ARMOR_TEXTURE_LOCATION = "textures/models/%s/armor/%s_layer_%s";

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

    public void postProcess(@NotNull PackPostProcessContext<ArmorPackModule> context) {
        List<ArmorItem> armorItems = context.registryValues(Registries.ITEM).stream()
                .filter(item -> item instanceof ArmorItem)
                .map(item -> (ArmorItem) item)
                .toList();

        LOGGER.info("Armor to convert: " + armorItems.size() + " in mod " + context.mod().id());

        for (ArmorItem armorItem : armorItems) {
            ResourceLocation armorItemLocation = BuiltInRegistries.ITEM.getKey(armorItem);

            ResourceLocation armorTextureLocation = new ResourceLocation(armorItem.getMaterial().getName());

            Attachables armorAttachable = new Attachables();
            armorAttachable.formatVersion("1.10.0");

            Description description = new Description();
            description.identifier(armorItemLocation.toString());
            description.materials(ATTACHABLE_MATERIALS);
            description.scripts(ATTACHABLE_SCRIPTS);
            description.renderControllers(new String[] { "controller.render.armor" });

            description.textures(new HashMap<>() {
                {
                    put("default", String.format(BEDROCK_ARMOR_TEXTURE_LOCATION, context.mod().id(), armorTextureLocation.getPath(), (armorItem.getEquipmentSlot() == EquipmentSlot.LEGS ? 2 : 1)));
                    put("enchanted", "textures/misc/enchanted_item_glint");
                }
            });

            String geometryType = "";
            switch (armorItem.getEquipmentSlot()) {
                case HEAD -> geometryType = "helmet";
                case CHEST -> geometryType = "chestplate";
                case LEGS -> geometryType = "leggings";
                case FEET -> geometryType = "boots";
            }

            final String finalGeometryType = geometryType;
            description.geometry(Map.of("default", "geometry.player.armor." + finalGeometryType));

            Attachable attachable = new Attachable();
            attachable.description(description);
            armorAttachable.attachable(attachable);

            context.pack().addAttachable(armorAttachable, "attachables/" + armorItemLocation.getPath() + ".json");
        }
    }

    @Override
    public boolean test(@NotNull PackPostProcessContext<ArmorPackModule> context) {
        return context.registryValues(Registries.ITEM).stream().anyMatch(item -> item instanceof ArmorItem);
    }
}
