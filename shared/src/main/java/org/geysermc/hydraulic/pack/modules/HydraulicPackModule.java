package org.geysermc.hydraulic.pack.modules;

import com.google.auto.service.AutoService;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.pack.converter.converter.texture.TextureMappings;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@AutoService(PackModule.class)
public class HydraulicPackModule extends PackModule<HydraulicPackModule> {
    public HydraulicPackModule() {
        this.postProcess(context -> {
            // Map all textures files as valid names
            for (Map.Entry<String, String> entry : TextureMappings.textureMappings().textures("block").entrySet()) {
                context.bedrockResourcePack().addBlockTexture("hydraulic:" + entry.getValue(), "textures/blocks/" + entry.getValue());
            }
        });
    }

    @Override
    public boolean test(@NotNull PackPostProcessContext<HydraulicPackModule> context) {
        return context.mod().id().equals("hydraulic");
    }
}
