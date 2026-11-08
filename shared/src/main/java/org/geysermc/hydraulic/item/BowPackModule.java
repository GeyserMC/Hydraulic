package org.geysermc.hydraulic.item;

import com.google.auto.service.AutoService;
import net.kyori.adventure.key.Key;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BowItem;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.TexturePackModule;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.hydraulic.util.HydraulicKey;
import org.geysermc.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.pack.bedrock.resource.attachables.Attachable;
import org.geysermc.pack.bedrock.resource.attachables.Attachables;
import org.geysermc.pack.bedrock.resource.attachables.attachable.Description;
import org.geysermc.pack.bedrock.resource.attachables.attachable.description.Scripts;
import org.geysermc.pack.bedrock.resource.render_controllers.RenderControllers;
import org.geysermc.pack.bedrock.resource.render_controllers.rendercontrollers.Arrays;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.item.ConditionItemModel;
import team.unnamed.creative.item.Item;
import team.unnamed.creative.item.ItemModel;
import team.unnamed.creative.item.RangeDispatchItemModel;
import team.unnamed.creative.item.ReferenceItemModel;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.model.ModelTexture;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoService(PackModule.class)
public class BowPackModule extends TexturePackModule<BowPackModule> {
    private static final Map<String, String> ATTACHABLE_MATERIALS = Map.of(
            "default", "entity_alphatest",
            "enchanted", "entity_alphatest_glint"
    );
    private static final Map<String, String> ATTACHABLE_GEOMETRY = Map.of(
            "default", "geometry.bow_standby",
            "bow_pulling_0", "geometry.bow_pulling_0",
            "bow_pulling_1", "geometry.bow_pulling_1",
            "bow_pulling_2", "geometry.bow_pulling_2"
    );
    private static final Map<String, String> ATTACHABLE_ANIMATIONS = Map.of(
            "wield", "animation.bow.wield",
            "wield_first_person_pull", "animation.bow.wield_first_person_pull"
    );
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
                .filter(BowItem.class::isInstance)
                .map(BowItem.class::cast)
                .toList();

        context.logger().info("Bows to convert: " + bowItems.size() + " in mod " + context.mod().id());

        for (BowItem bowItem : bowItems) {
            Identifier bowLocation = BuiltInRegistries.ITEM.getKey(bowItem);

            // Bow item definitions are a condition on using_item, with the pulling
            // frames as a range dispatch on use_duration when the bow is in use
            Item item = assets.item(HydraulicKey.of(bowLocation));
            if (item == null
                    || !(item.model() instanceof ConditionItemModel conditionModel)
                    || !(conditionModel.onFalse() instanceof ReferenceItemModel standbyModel)
                    || !(conditionModel.onTrue() instanceof RangeDispatchItemModel pullingModel)) {
                context.logger().warn("Bow {} has an unsupported item definition, skipping", bowLocation);
                continue;
            }

            String defaultTexture = getTextureFromModel(context, assets, standbyModel.model());
            if (defaultTexture == null) {
                context.logger().warn("Bow {} has no layer0 texture, skipping", bowLocation);
                continue;
            }

            Map<String, String> textures = new HashMap<>();
            textures.put("enchanted", "textures/misc/enchanted_item_glint");
            textures.put("default", defaultTexture);

            List<ItemModel> pullingFrames = new ArrayList<>();
            pullingFrames.add(pullingModel.fallback());
            pullingModel.entries().stream()
                    .sorted(Comparator.comparingDouble(RangeDispatchItemModel.Entry::threshold))
                    .map(RangeDispatchItemModel.Entry::model)
                    .forEach(pullingFrames::add);

            // The attachable only has 3 pulling frames, any extra frames are ignored
            // TODO Support more than 3? Not aware of any bows that have more than 3 pulling frames
            for (int i = 0; i < Math.min(pullingFrames.size(), 3); i++) {
                if (!(pullingFrames.get(i) instanceof ReferenceItemModel frameModel)) {
                    continue;
                }

                String frameTexture = getTextureFromModel(context, assets, frameModel.model());
                if (frameTexture == null) {
                    context.logger().warn("Bow pulling model {} has no layer0 texture, skipping", frameModel.model());
                    continue;
                }

                textures.put("bow_pulling_" + i, frameTexture);
            }

            Attachables bowAttachable = new Attachables();
            bowAttachable.formatVersion("1.10.0");

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
            bowAttachable.attachable(attachable);

            bedrockPack.addAttachable(bowAttachable, "attachables/" + bowLocation.getPath() + ".json");
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

    @Nullable
    private String getTextureFromModel(@NotNull PackPostProcessContext<BowPackModule> context, @NotNull ResourcePack assets, @NotNull Key modelKey) {
        Model model = assets.model(modelKey);
        if (model == null) {
            return null;
        }

        List<ModelTexture> layers = model.textures().layers();
        if (layers.isEmpty()) {
            return null;
        }

        return getOutputFromModel(context, layers.getFirst().key()).replace(".png", "");
    }

    @Override
    public boolean test(@NotNull PackPostProcessContext<BowPackModule> context) {
        return context.registryValues(BuiltInRegistries.ITEM).stream().anyMatch(item -> item instanceof BowItem);
    }
}
