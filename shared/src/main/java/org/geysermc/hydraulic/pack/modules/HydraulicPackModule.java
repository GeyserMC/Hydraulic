package org.geysermc.hydraulic.pack.modules;

import com.google.auto.service.AutoService;
import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.hydraulic.util.GeoUtil;
import org.geysermc.pack.converter.util.JsonMappings;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@AutoService(PackModule.class)
public class HydraulicPackModule extends PackModule<HydraulicPackModule> {
    private static final String POLYMER_BLOCK_PLACEHOLDER_TEXTURE_ID = "hydraulic:polymer_placeholder_block";
    private static final String POLYMER_BLOCK_PLACEHOLDER_TEXTURE_PATH = "textures/blocks/hydraulic/polymer_placeholder_block";
    private static final byte[] POLYMER_BLOCK_PLACEHOLDER_TEXTURE = Base64.getDecoder().decode(
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+/p9sAAAAASUVORK5CYII="
    );

    public HydraulicPackModule() {
        this.postProcess(context -> {
            Map<String, List<String>> mappings;

            JsonMappings jsonMappings = JsonMappings.getMapping("textures");
            if (jsonMappings != null) {
                try {
                    Field mappingsField = JsonMappings.class.getDeclaredField("mappings");
                    mappingsField.setAccessible(true);

                    mappings = (Map<String, List<String>>) mappingsField.get(jsonMappings);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else {
                mappings = Map.of();
            }

            // Map all block and item textures files as valid names
            for (Map.Entry<String, List<String>> entry : mappings.entrySet()) {
                if (entry.getKey().startsWith("block")) {
                    for (String str : entry.getValue()) {
                        context.bedrockResourcePack().addBlockTexture(Constants.MOD_ID + ":" + str, "textures/blocks/" + str);
                    }
                } else if (entry.getKey().startsWith("item")) {
                    for (String str : entry.getValue()) {
                        context.bedrockResourcePack().addItemTexture(Constants.MOD_ID + ":" + str, "textures/items/" + str);
                    }
                }
            }

            // Add the empty geometry
            context.bedrockResourcePack().addBlockModel(GeoUtil.empty("geometry." + Constants.MOD_ID + ".empty"), "empty.json");

            // Add a visible last-resort Polymer block texture so fallback custom blocks are not invisible.
            context.bedrockResourcePack().addExtraFile(POLYMER_BLOCK_PLACEHOLDER_TEXTURE, POLYMER_BLOCK_PLACEHOLDER_TEXTURE_PATH + ".png");
            context.bedrockResourcePack().addBlockTexture(POLYMER_BLOCK_PLACEHOLDER_TEXTURE_ID, POLYMER_BLOCK_PLACEHOLDER_TEXTURE_PATH);
        });
    }

    @Override
    public boolean test(@NotNull PackPostProcessContext<HydraulicPackModule> context) {
        return context.mod().id().equals(Constants.MOD_ID);
    }
}
