package org.geysermc.hydraulic.pack.modules;

import com.google.auto.service.AutoService;
import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.hydraulic.util.GeoUtil;
import org.geysermc.pack.converter.util.JsonMappings;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@AutoService(PackModule.class)
public class HydraulicPackModule extends PackModule<HydraulicPackModule> {
    public HydraulicPackModule() {
        this.postProcess(context -> {
            JsonMappings jsonMappings = JsonMappings.getMapping("textures");

            // Map all block and item textures files as valid names
            for (Map.Entry<String, List<String>> entry : jsonMappings.entrySet()) {
                if (entry.getKey().startsWith("block")) {
                    for (String str : entry.getValue()) {
                        context.bedrockResourcePack().addBlockTexture(Constants.MOD_ID + ":" + str, "textures/blocks/" + str.replace("block/", ""));
                    }
                } else if (entry.getKey().startsWith("item")) {
                    for (String str : entry.getValue()) {
                        context.bedrockResourcePack().addItemTexture(Constants.MOD_ID + ":" + str, "textures/items/" + str.replace("item/", ""));
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
