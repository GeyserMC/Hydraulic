package org.geysermc.hydraulic.item;

import com.google.auto.service.AutoService;
import com.nimbusds.jose.util.Resource;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.bedrock.resource.attachables.Attachable;
import org.geysermc.hydraulic.pack.bedrock.resource.attachables.Attachables;
import org.geysermc.hydraulic.pack.bedrock.resource.attachables.attachable.Description;
import org.geysermc.hydraulic.pack.bedrock.resource.attachables.attachable.description.Scripts;
import org.geysermc.hydraulic.pack.context.PackCreateContext;
import org.geysermc.hydraulic.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoService(PackModule.class)
public class ArmorPackModule extends PackModule<ArmorPackModule> {
    private static final String JAVA_ARMOR_TEXTURE_LOCATION = "assets/%s/textures/models/armor/%s_layer_%s.png";
    private static final String BEDROCK_ARMOR_TEXTURE_LOCATION = "textures/models/armor/%s/%s_layer_%s.png";

    private static final Map<String, String> ATTACHABLE_MATERIALS = new HashMap<>() {
        {
            put("default", "armor");
            put("enchanted", "armor_enchanted");
        }
    };
    private static final Scripts ATTACHABLE_SCRIPTS = new Scripts();

    static {
        ATTACHABLE_SCRIPTS.setParentSetup("variable.chest_layer_visible = 0.0;");
    }

    @Override
    public void create(@NotNull PackCreateContext<ArmorPackModule> context) {
        List<ArmorItem> armorItems = context.registryValues(Registries.ITEM).stream().filter(item -> item instanceof ArmorItem).map(item -> (ArmorItem)item).toList();

        LOGGER.info("Armor to convert: " + armorItems.size() + " in mod " + context.mod().id());

        for (ArmorItem armorItem : armorItems) {
            ResourceLocation armorItemLocation = BuiltInRegistries.ITEM.getKey(armorItem);

            ResourceLocation armorTextureLocation = new ResourceLocation(armorItem.getMaterial().getName());

            String inputFileNamePrefix = "assets/%s/textures/models/armor/%s_layer_%s.png" + armorItem.getMaterial().getName();
            String outputFileNamePrefix = "textures/models/armor/" + context.mod().id() + "/" + armorItem.getMaterial().getName();

            FileUtil.copyFileFromMod(context.mod(), String.format(JAVA_ARMOR_TEXTURE_LOCATION, armorTextureLocation.getNamespace(), armorTextureLocation.getPath(), 1), context.path().resolve(String.format(BEDROCK_ARMOR_TEXTURE_LOCATION, context.mod().id(), armorTextureLocation.getPath(), 1)));
            FileUtil.copyFileFromMod(context.mod(), String.format(JAVA_ARMOR_TEXTURE_LOCATION, armorTextureLocation.getNamespace(), armorTextureLocation.getPath(), 2), context.path().resolve(String.format(BEDROCK_ARMOR_TEXTURE_LOCATION, context.mod().id(), armorTextureLocation.getPath(), 2)));

            Attachables armorAttachable = new Attachables();
            armorAttachable.setFormatVersion("1.8.0");

            Description description = new Description();
            description.setIdentifier(armorItemLocation.toString());
            description.setMaterials(ATTACHABLE_MATERIALS);
            description.setScripts(ATTACHABLE_SCRIPTS);
            description.setRenderControllers(new String[] {"controller.render.armor"});

            description.setTextures(new HashMap<>() {
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
            description.setGeometry(Map.of("default", "geometry.player.armor." + finalGeometryType));

            Attachable attachable = new Attachable();
            attachable.setDescription(description);
            armorAttachable.setAttachable(attachable);

            context.pack().addAttachable(armorAttachable, "attachables/" + armorItemLocation.getPath() + ".json");
        }
    }

    @Override
    public boolean test(@NotNull PackCreateContext<ArmorPackModule> context) {
        return context.registryValues(Registries.ITEM).stream().anyMatch(item -> item instanceof ArmorItem);
    }
}
