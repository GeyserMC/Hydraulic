package org.geysermc.hydraulic.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import org.geysermc.hydraulic.pack.TexturePackModule;
import org.geysermc.hydraulic.pack.context.PackProcessContext;
import org.geysermc.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.pack.converter.PackConversionContext;
import org.geysermc.pack.converter.data.TextureConversionData;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.ResourcePack;

import java.util.List;

// TODO: Awaiting support in Geyser - needs to be converted to new system
// @AutoService(PackModule.class)
public class BlockPackModule extends TexturePackModule<BlockPackModule> {
    private static final String JAVA_BLOCK_STATE_LOCATION = "assets/%s/blockstates/%s.json";
    private static final String JAVA_BLOCK_MODEL_LOCATION = "assets/%s/models/%s.json";
    private static final String BEDROCK_BLOCK_TEXTURE_LOCATION = "textures/blocks/%s/%s.png";

    @Override
    public void postConvert(PackConversionContext<TextureConversionData> packContext) {
        ResourcePack assets = packContext.javaResourcePack();
        BedrockResourcePack bedrockPack = packContext.bedrockResourcePack();

        this.postProcess(context -> {
            List<Block> blocks = context.registryValues(Registries.BLOCK);

            LOGGER.info("Blocks to convert: " + blocks.size() + " in mod " + context.mod().id());
            /*
            Path jarPath = context.mod().modPath();

            for (Block block : blocks) {
                ResourceLocation blockPath = BuiltInRegistries.BLOCK.getKey(block);
                Path blockStatePath = jarPath.resolve(String.format(JAVA_BLOCK_STATE_LOCATION, blockPath.getNamespace(), blockPath.getPath()));
                try (InputStream blockStateStream = Files.newInputStream(blockStatePath)) {
                    BlockState state = Constants.MAPPER.readValue(blockStateStream, BlockState.class);
                    Variant rootVariant = state.variants().get("");
                    if (rootVariant == null) {
                        // TODO: Handle multi-face block variants
                        continue;
                    }

                    ResourceLocation modelPath = rootVariant.model();
                    try (InputStream modelStream = Files.newInputStream(jarPath.resolve(String.format(JAVA_BLOCK_MODEL_LOCATION, modelPath.getNamespace(), modelPath.getPath())))) {
                        Model model = Constants.MAPPER.readValue(modelStream, Model.class);

                        if (model.parent() == null || !model.parent().getNamespace().equals("minecraft")) {
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

             */
        });
    }

    @Override
    public boolean test(@NotNull PackProcessContext<BlockPackModule> context) {
        return context.registryValues(Registries.BLOCK).size() > 0;
    }
}
