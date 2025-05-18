package org.geysermc.hydraulic.pack.modules;

import com.google.auto.service.AutoService;
import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.hydraulic.util.GeoUtil;
import org.geysermc.pack.converter.converter.texture.TextureMappings;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@AutoService(PackModule.class)
public class HydraulicPackModule extends PackModule<HydraulicPackModule> {
    public HydraulicPackModule() {
        this.postProcess(context -> {
            // Map all block textures files as valid names
            for (Map.Entry<String, String> entry : ((Map<String, String>) TextureMappings.textureMappings().textures("block")).entrySet()) {
                context.bedrockResourcePack().addBlockTexture(Constants.MOD_ID + ":" + entry.getValue(), "textures/blocks/" + entry.getValue());
            }

            // Map all item textures files as valid names
            for (Map.Entry<String, String> entry : ((Map<String, String>) TextureMappings.textureMappings().textures("item")).entrySet()) {
                context.bedrockResourcePack().addItemTexture(Constants.MOD_ID + ":" + entry.getValue(), "textures/items/" + entry.getValue());
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
