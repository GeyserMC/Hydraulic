package org.geysermc.hydraulic.pack.bedrock.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.geysermc.hydraulic.pack.bedrock.resource.manifest.Header;
import org.geysermc.hydraulic.pack.bedrock.resource.manifest.Modules;
import org.geysermc.hydraulic.pack.bedrock.resource.textures.ItemTexture;
import org.geysermc.hydraulic.pack.bedrock.resource.textures.itemtexture.TextureData;
import org.geysermc.hydraulic.pack.bedrock.resource.textures.itemtexture.texturedata.Textures;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.geysermc.hydraulic.util.FileUtil.exportJson;

/**
 * Represents a Bedrock resource pack.
 */
public class BedrockResourcePack {
    private static final int FORMAT_VERSION = 2;

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private final Manifest manifest;

    private ItemTexture itemTexture;

    public BedrockResourcePack(@NotNull Manifest manifest) {
        this(manifest, null);
    }

    public BedrockResourcePack(@NotNull Manifest manifest, @Nullable ItemTexture itemTexture) {
        this.manifest = manifest;
        this.itemTexture = itemTexture;
    }

    /**
     * Get the manifest of the resource pack.
     *
     * @return the manifest of the resource pack
     */
    @NotNull
    public Manifest manifest() {
        return this.manifest;
    }

    /**
     * Get the item texture of the resource pack.
     *
     * @return the item texture of the resource pack
     */
    @Nullable
    public ItemTexture itemTexture() {
        return this.itemTexture;
    }

    /**
     * Set the item texture of the resource pack.
     *
     * @param itemTexture the item texture of the resource pack
     */
    public void itemTexture(@Nullable ItemTexture itemTexture) {
        this.itemTexture = itemTexture;
    }

    /**
     * Add an item to the resource pack.
     *
     * @param id the id of the item
     * @param textureLocation the location of the texture
     */
    public void addItem(@NotNull String id, @NotNull String textureLocation) {
        if (this.itemTexture == null) {
            this.itemTexture = new ItemTexture();
            this.itemTexture.setResourcePackName(this.manifest.getHeader().getName());
            this.itemTexture.setTextureName("atlas.items");
        }

        TextureData data = new TextureData();
        data.setTextures(textureLocation);

        this.itemTexture.getTextureData().put(id, data);
    }

    /**
     * Exports the resource pack to the specified directory.
     *
     * @param directory the directory to export the resource pack to
     */
    public void export(@NotNull Path directory) throws IOException {
        exportJson(MAPPER, directory.resolve("manifest.json"), this.manifest);

        if (this.itemTexture != null) {
            exportJson(MAPPER, directory.resolve("textures/item_texture.json"), this.itemTexture);
        }
    }

    /**
     * Initializes a resource pack for the specified mod.
     *
     * @param mod the mod to initialize the resource pack for
     * @return the initialized resource pack
     */
    public static BedrockResourcePack initializeForMod(@NotNull ModInfo mod) {
        Manifest manifest = new Manifest();
        manifest.setFormatVersion(FORMAT_VERSION);

        Header header = new Header();
        header.setDescription("Resource pack for mod " + mod.name());
        header.setName(mod.name() + " Resource Pack");
        header.setVersion(new float[] { 1, 0, 0 });
        header.setMinEngineVersion(new float[] { 1, 16, 0 });
        header.setUuid(UUID.randomUUID().toString());

        manifest.setHeader(header);

        Modules module = new Modules();
        module.setDescription("Resource pack for mod " + mod.name());
        module.setType("resources");
        module.setUuid(UUID.randomUUID().toString());
        module.setVersion(new float[] { 1, 0, 0 });

        manifest.setModules(List.of(module));
        return new BedrockResourcePack(manifest);
    }
}
