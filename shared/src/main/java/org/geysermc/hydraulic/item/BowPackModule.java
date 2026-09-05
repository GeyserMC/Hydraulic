package org.geysermc.hydraulic.item;

import com.google.auto.service.AutoService;
import net.kyori.adventure.key.Key;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BowItem;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.TexturePackModule;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.pack.bedrock.resource.attachables.Attachable;
import org.geysermc.pack.bedrock.resource.attachables.Attachables;
import org.geysermc.pack.bedrock.resource.attachables.attachable.Description;
import org.geysermc.pack.bedrock.resource.attachables.attachable.description.Scripts;
import org.geysermc.pack.bedrock.resource.render_controllers.RenderControllers;
import org.geysermc.pack.bedrock.resource.render_controllers.rendercontrollers.Arrays;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.model.ItemOverride;
import team.unnamed.creative.model.ItemPredicate;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.model.ModelTexture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoService(PackModule.class)
public class BowPackModule extends TexturePackModule<BowPackModule> {
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
        ATTACHABLE_SCRIPTS.preAnimation(new String[] {
            "v.charge_amount = math.clamp((q.main_hand_item_max_duration - (q.main_hand_item_use_duration - q.frame_alpha + 1.0)) / 10.0, 0.0, 1.0f);",
            "v.total_frames = 3;",
            "v.step = v.total_frames / 120;",
            "v.frame = query.is_using_item ? math.clamp((v.frame ?? 0) + v.step, 1, v.total_frames) : 0;"
        });
        ATTACHABLE_SCRIPTS.animate(List.of(
            Map.of("wield", "c.is_first_person"),
            Map.of("wield_first_person_pull", "query.main_hand_item_use_duration > 0.0f && c.is_first_person")
        ));
    }

    public BowPackModule() {
        this.postProcess(this::postProcess);
    }

    private void postProcess(@NotNull PackPostProcessContext<BowPackModule> context) {
        ResourcePack assets = context.javaResourcePack();
        BedrockResourcePack bedrockPack = context.bedrockResourcePack();

        List<BowItem> bowItems = context.registryValues(BuiltInRegistries.ITEM).stream()
                .filter(item -> item instanceof BowItem)
                .map(item -> (BowItem) item)
                .toList();

        context.logger().info("Bows to convert: " + bowItems.size() + " in mod " + context.mod().id());

        for (BowItem bowItem : bowItems) {
            Identifier bowLocation = BuiltInRegistries.ITEM.getKey(bowItem);
            Map<String, String> textures = new HashMap<>() {
                {
                    put("enchanted", "textures/misc/enchanted_item_glint");
                }
            };

            Model model = assets.model(Key.key(bowLocation.getNamespace(), "item/" + bowLocation.getPath()));
            if (model == null) {
                context.logger().warn("Bow {} has no model, skipping", bowLocation);
                continue;
            }

            List<ModelTexture> layers = model.textures().layers();
            if (layers == null || layers.isEmpty()) {
                context.logger().warn("Bow {} has no layer0 texture, skipping", bowLocation);
                continue;
            }

            ModelTexture layer0 = layers.getFirst();
            String defaultOutputLoc = getOutputFromModel(context, layer0.key()).replace(".png", "");

            textures.put("default", defaultOutputLoc);

            for (ItemOverride override : model.overrides()) {
                Model pullingModel = assets.model(override.model());
                if (pullingModel == null) {
                    context.logger().warn("Bow pulling model {} has no model, skipping", override.model());
                    continue;
                }

                List<ModelTexture> pullingLayers = pullingModel.textures().layers();
                if (pullingLayers == null || pullingLayers.isEmpty()) {
                    context.logger().warn("Bow pulling model {} has no layer0 texture, skipping", override.model());
                    continue;
                }

                ModelTexture pullingLayer0 = pullingLayers.getFirst();
                String outputLoc = getOutputFromModel(context, pullingLayer0.key()).replace(".png", "");

                Map<String, Float> predicate = new HashMap<>();
                for (ItemPredicate itemPredicate : override.predicate()) {
                    if (itemPredicate.value() instanceof Number number) {
                        predicate.put(itemPredicate.name(), number.floatValue());
                    }
                }

                if (!predicate.containsKey("pull") || predicate.get("pull") == 0f) {
                    textures.put("bow_pulling_0", outputLoc);
                } else if (predicate.get("pull") == 0.65f) {
                    textures.put("bow_pulling_1", outputLoc);
                } else if (predicate.get("pull") == 0.9f) {
                    textures.put("bow_pulling_2", outputLoc);
                }
            }

            Attachables armorAttachable = new Attachables();
            armorAttachable.formatVersion("1.10.0");

            Description description = new Description();
            description.identifier(bowLocation.toString());
            description.materials(ATTACHABLE_MATERIALS);
            description.geometry(ATTACHABLE_GEOMETRY);
            description.animations(ATTACHABLE_ANIMATIONS);
            description.scripts(ATTACHABLE_SCRIPTS);
            description.renderControllers(new String[] {"controller.render.bow_custom"});

            description.textures(textures);

            Attachable attachable = new Attachable();
            attachable.description(description);
            armorAttachable.attachable(attachable);

            bedrockPack.addAttachable(armorAttachable, "attachables/" + bowLocation.getPath() + ".json");
        }

        RenderControllers renderController = new RenderControllers();
        renderController.formatVersion("1.10.0");

        org.geysermc.pack.bedrock.resource.render_controllers.rendercontrollers.RenderControllers bowCustomRenderController = new org.geysermc.pack.bedrock.resource.render_controllers.rendercontrollers.RenderControllers();
        bowCustomRenderController.arrays(new Arrays());

        bowCustomRenderController.arrays().textures().put("array.bow_texture_frames", new String[] {
                "texture.default",
                "texture.bow_pulling_0",
                "texture.bow_pulling_1",
                "texture.bow_pulling_2"
        });

        bowCustomRenderController.arrays().geometries().put("array.bow_geo_frames", new String[] {
                "geometry.default",
                "geometry.bow_pulling_0",
                "geometry.bow_pulling_1",
                "geometry.bow_pulling_2"
        });

        bowCustomRenderController.geometry("array.bow_geo_frames[math.floor(v.frame)]");
        bowCustomRenderController.materials().add(Map.of("*", "variable.is_enchanted ? material.enchanted : material.default"));
        bowCustomRenderController.textures(new String[] {
                "array.bow_texture_frames[math.floor(v.frame)]",
                "texture.enchanted"
        });

        renderController.renderControllers().put("controller.render.bow_custom", bowCustomRenderController);
        bedrockPack.addRenderController(renderController, "render_controllers/bow_custom.render_controllers.json");
    }

    @Override
    public boolean test(@NotNull PackPostProcessContext<BowPackModule> context) {
        return context.registryValues(BuiltInRegistries.ITEM).stream().anyMatch(item -> item instanceof BowItem);
    }
}
