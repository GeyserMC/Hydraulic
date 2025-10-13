package org.geysermc.hydraulic.pack.modules;

import com.google.auto.service.AutoService;
import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.hydraulic.util.GeoUtil;
import org.geysermc.pack.converter.type.texture.TextureMappings;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@AutoService(PackModule.class)
public class HydraulicPackModule extends PackModule<HydraulicPackModule> {
    public HydraulicPackModule() {
        this.postProcess(context -> {
            // Map all block textures files as valid names
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) TextureMappings.textureMappings().textures("block")).entrySet()) {
                if (entry.getValue() instanceof String str) {
                    context.bedrockResourcePack().addBlockTexture(Constants.MOD_ID + ":" + str, "textures/blocks/" + str);
                } else if (entry.getValue() instanceof List<?> list) {
                    for (String str : (List<String>) list) {
                        context.bedrockResourcePack().addBlockTexture(Constants.MOD_ID + ":" + str, "textures/blocks/" + str);
                    }
                }
            }

            // Map all item textures files as valid names
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) TextureMappings.textureMappings().textures("item")).entrySet()) {
                if (entry.getValue() instanceof String str) {
                    context.bedrockResourcePack().addItemTexture(Constants.MOD_ID + ":" + str, "textures/items/" + str);
                } else if (entry.getValue() instanceof List<?> list) {
                    for (String str : (List<String>) list) {
                        context.bedrockResourcePack().addItemTexture(Constants.MOD_ID + ":" + str, "textures/items/" + str);
                    }
                }
            }

            // Add the empty geometry
            context.bedrockResourcePack().addBlockModel(GeoUtil.empty("geometry." + Constants.MOD_ID + ".empty"), "empty.json");
        });
    }

    @Override
    public boolean test(@NotNull PackPostProcessContext<HydraulicPackModule> context) {
        return context.mod().id().equals(Constants.MOD_ID);
    }
}
