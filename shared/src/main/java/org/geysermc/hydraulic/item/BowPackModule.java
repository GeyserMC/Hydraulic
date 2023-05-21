package org.geysermc.hydraulic.item;

import com.google.auto.service.AutoService;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BowItem;
import org.geysermc.hydraulic.assets.Model;
import org.geysermc.hydraulic.assets.ModelOverride;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.bedrock.resource.attachables.Attachable;
import org.geysermc.hydraulic.pack.bedrock.resource.attachables.Attachables;
import org.geysermc.hydraulic.pack.bedrock.resource.attachables.attachable.Description;
import org.geysermc.hydraulic.pack.bedrock.resource.attachables.attachable.description.Scripts;
import org.geysermc.hydraulic.pack.bedrock.resource.render_controllers.RenderControllers;
import org.geysermc.hydraulic.pack.bedrock.resource.render_controllers.rendercontrollers.Arrays;
import org.geysermc.hydraulic.pack.context.PackCreateContext;
import org.geysermc.hydraulic.util.Constants;
import org.geysermc.hydraulic.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoService(PackModule.class)
public class BowPackModule extends PackModule<BowPackModule> {
    private static final Map<String, String> ATTACHABLE_MATERIALS = new HashMap<>() {
        {
            put("default", "entity_alphatest");
            put("enchanted", "entity_alphatest_glint");
        }
    };
    private static final Map<String, String> ATTACHABLE_GEOMETRY = new HashMap<>() {
        {
            put("default", "geometry.bow_standby");
            put("bow_pulling_0", "geometry.bow_pulling_0");
            put("bow_pulling_1", "geometry.bow_pulling_1");
            put("bow_pulling_2", "geometry.bow_pulling_2");
        }
    };
    private static final Map<String, String> ATTACHABLE_ANIMATIONS = new HashMap<>() {
        {
            put("wield", "animation.bow.wield");
            put("wield_first_person_pull", "animation.bow.wield_first_person_pull");
        }
    };
    private static final Scripts ATTACHABLE_SCRIPTS = new Scripts();

    static {
        ATTACHABLE_SCRIPTS.setPreAnimation(new String[]{
            "v.charge_amount = math.clamp((q.main_hand_item_max_duration - (q.main_hand_item_use_duration - q.frame_alpha + 1.0)) / 10.0, 0.0, 1.0f);",
            "v.total_frames = 3;",
            "v.step = v.total_frames / 120;",
            "v.frame = query.is_using_item ? math.clamp((v.frame ?? 0) + v.step, 1, v.total_frames) : 0;"
        });
        ATTACHABLE_SCRIPTS.setAnimate(List.of(new HashMap<>() {
            {
                put("wield", "c.is_first_person");
            }
        }, new HashMap<>() {
            {
                put("wield_first_person_pull", "query.main_hand_item_use_duration > 0.0f && c.is_first_person");
            }
        }));
    }

    @Override
    public void create(@NotNull PackCreateContext<BowPackModule> context) {
        List<BowItem> bowItems = context.registryValues(Registries.ITEM).stream().filter(item -> item instanceof BowItem).map(item -> (BowItem)item).toList();

        LOGGER.info("Bows to convert: " + bowItems.size() + " in mod " + context.mod().id());
        Path jarPath = context.mod().modPath();

        for (BowItem bowItem : bowItems) {
            ResourceLocation bowLocation = BuiltInRegistries.ITEM.getKey(bowItem);
            Map<String, String> textures = new HashMap<>() {
                {
                    put("enchanted", "textures/misc/enchanted_item_glint");
                }
            };


            try (InputStream itemModelStream = Files.newInputStream(jarPath.resolve(String.format(Constants.JAVA_ITEM_MODEL_LOCATION, bowLocation.getNamespace(), bowLocation.getPath())))) {
                Model model = Constants.MAPPER.readValue(itemModelStream, Model.class);

                String defaultOutputLoc = String.format(Constants.BEDROCK_ITEM_TEXTURE_LOCATION, context.mod().id(), model.textures().get("layer0").getPath().replace("item/", ""));
                textures.put("default", defaultOutputLoc);

                for (ModelOverride override : model.overrides()) {
                    if (override.predicate().get("pulling") == 1) {
                        try (InputStream pullingModelStream = Files.newInputStream(jarPath.resolve(String.format(Constants.JAVA_ITEM_MODEL_LOCATION, override.model().getNamespace(), override.model().getPath().replace("item/", ""))))) {
                            Model pullingModel = Constants.MAPPER.readValue(pullingModelStream, Model.class);
                            ResourceLocation layer0 = pullingModel.textures().get("layer0");

                            String outputLoc = String.format(Constants.BEDROCK_ITEM_TEXTURE_LOCATION, context.mod().id(), layer0.getPath().replace("item/", ""));
                            FileUtil.copyFileFromMod(context.mod(), String.format(Constants.JAVA_TEXTURE_LOCATION, layer0.getNamespace(), layer0.getPath()), context.path().resolve(outputLoc));

                            if (!override.predicate().containsKey("pull") || override.predicate().get("pull") == 0f) {
                                textures.put("bow_pulling_0", outputLoc);
                            } else if (override.predicate().get("pull") == 0.65f) {
                                textures.put("bow_pulling_1", outputLoc);
                            } else if (override.predicate().get("pull") == 0.9f) {
                                textures.put("bow_pulling_2", outputLoc);
                            }
                        } catch (Exception ex) {
                            LOGGER.error("Failed to read bow item override model {} for mod {}", override.model().toString(), context.mod().id(), ex);
                        }
                    }
                }
            } catch (Exception ex) {
                LOGGER.error("Failed to read bow item model {} for mod {}", bowLocation.toString(), context.mod().id(), ex);
            }

//            String inputFileNamePrefix = "assets/minecraft/textures/models/armor/" + armorItem.getMaterial().getName();
//            String outputFileNamePrefix = "textures/models/armor/" + context.mod().id() + "/" + armorItem.getMaterial().getName();

//            FileUtil.copyFileFromMod(context.mod(), inputFileNamePrefix + "_layer_1.png", context.path().resolve(outputFileNamePrefix + "_layer_1.png"));
//            FileUtil.copyFileFromMod(context.mod(), inputFileNamePrefix + "_layer_2.png", context.path().resolve(outputFileNamePrefix + "_layer_2.png"));

            Attachables armorAttachable = new Attachables();
            armorAttachable.setFormatVersion("1.10.0");

            Description description = new Description();
            description.setIdentifier(bowLocation.toString());
            description.setMaterials(ATTACHABLE_MATERIALS);
            description.setGeometry(ATTACHABLE_GEOMETRY);
            description.setAnimations(ATTACHABLE_ANIMATIONS);
            description.setScripts(ATTACHABLE_SCRIPTS);
            description.setRenderControllers(new String[] {"controller.render.bow_custom"});

            description.setTextures(textures);

            Attachable attachable = new Attachable();
            attachable.setDescription(description);
            armorAttachable.setAttachable(attachable);

            context.pack().addAttachable(armorAttachable, "attachables/" + bowLocation.getPath() + ".json");
        }

        RenderControllers renderController = new RenderControllers();
        renderController.setFormatVersion("1.10.0");

        org.geysermc.hydraulic.pack.bedrock.resource.render_controllers.rendercontrollers.RenderControllers bowCustomRenderController = new org.geysermc.hydraulic.pack.bedrock.resource.render_controllers.rendercontrollers.RenderControllers();
        bowCustomRenderController.arrays = new Arrays();

        bowCustomRenderController.arrays.getTextures().put("array.bow_texture_frames", new String[] {
            "texture.default",
            "texture.bow_pulling_0",
            "texture.bow_pulling_1",
            "texture.bow_pulling_2"
        });

        bowCustomRenderController.arrays.getGeometries().put("array.bow_geo_frames", new String[] {
            "geometry.default",
            "geometry.bow_pulling_0",
            "geometry.bow_pulling_1",
            "geometry.bow_pulling_2"
        });

        bowCustomRenderController.geometry = "array.bow_geo_frames[math.floor(v.frame)]";
        bowCustomRenderController.getMaterials().add(Map.of("*", "variable.is_enchanted ? material.enchanted : material.default"));
        bowCustomRenderController.setTextures(new String[] {
            "array.bow_texture_frames[math.floor(v.frame)]",
            "texture.enchanted"
        });

        renderController.getRenderControllers().put("controller.render.bow_custom", bowCustomRenderController);
        context.pack().addRenderController(renderController, "render_controllers/bow_custom.render_controllers.json");
    }

    @Override
    public boolean test(@NotNull PackCreateContext<BowPackModule> context) {
        return context.registryValues(Registries.ITEM).stream().anyMatch(item -> item instanceof BowItem);
    }
}
