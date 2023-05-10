package org.geysermc.hydraulic.block;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.service.AutoService;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.geysermc.hydraulic.assets.Model;
import org.geysermc.hydraulic.assets.BlockState;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.bedrock.resource.textures.TerrainTexture;
import org.geysermc.hydraulic.pack.context.PackCreateContext;
import org.geysermc.hydraulic.util.Constants;
import org.geysermc.hydraulic.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@AutoService(PackModule.class)
public class BlockPackModule extends PackModule<BlockPackModule> {
    private static final String JAVA_BLOCK_STATE_LOCATION = "assets/%s/blockstates/%s.json";
    private static final String JAVA_BLOCK_MODEL_LOCATION = "assets/%s/models/%s.json";
    private static final String BEDROCK_BLOCK_TEXTURE_LOCATION = "textures/blocks/%s/%s.png";

    @Override
    public void create(@NotNull PackCreateContext<BlockPackModule> context) {
        List<Block> blocks = context.registryValues(Registries.BLOCK);

        LOGGER.info("Blocks to convert: " + blocks.size() + " in mod " + context.mod().id());
        Path jarPath = context.mod().modPath();

        TerrainTexture terrainTexture = new TerrainTexture();

        for (Block block : blocks) {
            ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(block);
            Path blockStatePath = jarPath.resolve(String.format(JAVA_BLOCK_STATE_LOCATION, blockKey.getNamespace(), blockKey.getPath()));
            try (InputStream blockStateStream = Files.newInputStream(blockStatePath)) {
                BlockState state = Constants.MAPPER.readValue(blockStateStream, BlockState.class);
                ResourceLocation modelPath = state.variants().get("").model();
                try (InputStream modelStream = Files.newInputStream(jarPath.resolve(String.format(JAVA_BLOCK_MODEL_LOCATION, modelPath.getNamespace(), modelPath.getPath())))) {
                    Model model = Constants.MAPPER.readValue(modelStream, Model.class);

                    if (!model.parent().getNamespace().equals("minecraft")) {
                        // TODO Parse inherited models?
                        return;
                    }

                    for (Map.Entry<String, ResourceLocation> texture : model.textures().entrySet()) {
                        if (texture.getValue().getNamespace().equals("minecraft")) {
                            // TODO Map these
                            continue;
                        }

                        String rawPath = texture.getValue().getPath().replace("block/", "");
                        String texturePath = String.format(BEDROCK_BLOCK_TEXTURE_LOCATION, texture.getValue().getNamespace(), rawPath);

                        context.pack().addBlockTexture(texture.getValue().toString(), texturePath);

                        FileUtil.copyFileFromMod(context.mod(), String.format(Constants.JAVA_TEXTURE_LOCATION, texture.getValue().getNamespace(), texture.getValue().getPath()), context.path().resolve(texturePath));
                    }
                }
            } catch (Exception ex) {
                LOGGER.error("Failed to read block state {} for mod {}", blockStatePath, context.mod().id(), ex);
            }
        }
    }

    @Override
    public boolean test(@NotNull PackCreateContext<BlockPackModule> context) {
        return context.registryValues(Registries.BLOCK).size() > 0;
    }
}
