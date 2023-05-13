package org.geysermc.hydraulic.pack.bedrock.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.geysermc.hydraulic.pack.bedrock.resource.attachables.Attachables;
import org.geysermc.hydraulic.pack.bedrock.resource.manifest.Header;
import org.geysermc.hydraulic.pack.bedrock.resource.manifest.Modules;
import org.geysermc.hydraulic.pack.bedrock.resource.sounds.SoundDefinitions;
import org.geysermc.hydraulic.pack.bedrock.resource.sounds.sounddefinitions.Sounds;
import org.geysermc.hydraulic.pack.bedrock.resource.textures.ItemTexture;
import org.geysermc.hydraulic.pack.bedrock.resource.textures.TerrainTexture;
import org.geysermc.hydraulic.pack.bedrock.resource.textures.itemtexture.TextureData;
import org.geysermc.hydraulic.pack.bedrock.resource.textures.terraintexture.texturedata.Textures;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.geysermc.hydraulic.util.FileUtil.exportJson;

/**
 * Represents a Bedrock resource pack.
 */
public class BedrockResourcePack {
    private static final int FORMAT_VERSION = 2;

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

    private final Manifest manifest;

    private ItemTexture itemTexture;
    private TerrainTexture terrainTexture;
    private Map<String, Attachables> attachables;
    private SoundDefinitions soundDefinitions;

    public BedrockResourcePack(@NotNull Manifest manifest) {
        this(manifest, null, null);
    }

    public BedrockResourcePack(@NotNull Manifest manifest, @Nullable ItemTexture itemTexture, @Nullable TerrainTexture terrainTexture) {
        this.manifest = manifest;
        this.itemTexture = itemTexture;
        this.terrainTexture = terrainTexture;
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
     * Get the terrain texture of the resource pack.
     *
     * @return the terrain texture of the resource pack
     */
    @Nullable
    public TerrainTexture terrainTexture() {
        return this.terrainTexture;
    }

    /**
     * Set the terrain texture of the resource pack.
     *
     * @param terrainTexture the terrain texture of the resource pack
     */
    public void terrainTexture(@Nullable TerrainTexture terrainTexture) {
        this.terrainTexture = terrainTexture;
    }

    /**
     * Get the attachables of the resource pack.
     *
     * @return the attachables of the resource pack
     */
    @Nullable
    public Map<String, Attachables> attachables() {
        return this.attachables;
    }

    /**
     * Set the attachables of the resource pack.
     *
     * @param attachables the attachables of the resource pack
     */
    public void attachables(@Nullable Map<String, Attachables> attachables) {
        this.attachables = attachables;
    }

    /**
     * Get the sound definitions of the resource pack.
     *
     * @return the sound definitions of the resource pack
     */
    @Nullable
    public SoundDefinitions soundDefinitions() {
        return this.soundDefinitions;
    }

    /**
     * Set the sound definitions of the resource pack.
     *
     * @param soundDefinitions the sound definitions of the resource pack
     */
    public void soundDefinitions(@Nullable SoundDefinitions soundDefinitions) {
        this.soundDefinitions = soundDefinitions;
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
     * Add a block texture to the resource pack.
     *
     * @param id the id of the block texture
     * @param textureLocation the location of the texture
     */
    public void addBlockTexture(@NotNull String id, @NotNull String textureLocation) {
        if (this.terrainTexture == null) {
            this.terrainTexture = new TerrainTexture();
            this.terrainTexture.setResourcePackName(this.manifest.getHeader().getName());
            this.terrainTexture.setTextureName("atlas.terrain");
            this.terrainTexture.setPadding(8);
            this.terrainTexture.setNumMipLevels(4);
        }

        org.geysermc.hydraulic.pack.bedrock.resource.textures.terraintexture.TextureData data = new org.geysermc.hydraulic.pack.bedrock.resource.textures.terraintexture.TextureData();
        Textures textures = new Textures();
        textures.setPath(textureLocation);
        data.setTextures(textures);

        this.terrainTexture.getTextureData().put(id, data);
    }


    /**
     * Add an attachable to the resource pack.
     *
     * @param armorAttachable the data of the attachable
     * @param location the location of the final json
     */
    public void addAttachable(Attachables armorAttachable, String location) {
        if (this.attachables == null) {
            this.attachables = new HashMap<>();
        }

        this.attachables.put(location, armorAttachable);
    }

    /**
     * Add a sound to the resource pack.
     *
     * @param id the id of the sound
     * @param soundLocation the location of the sound
     */
    public void addSound(@NotNull String id, @NotNull String soundLocation) {
        if (this.soundDefinitions == null) {
            this.soundDefinitions = new SoundDefinitions();
            this.soundDefinitions.setFormatVersion("1.14.0");
        }

        Sounds sounds = new Sounds();
        sounds.setName(soundLocation);
        sounds.setLoadOnLowMemory(true);
        sounds.setStream(true);
        sounds.setVolume(1);
        sounds.setIs3D(true);
        sounds.setPitch(1);

        org.geysermc.hydraulic.pack.bedrock.resource.sounds.sounddefinitions.SoundDefinitions data = new org.geysermc.hydraulic.pack.bedrock.resource.sounds.sounddefinitions.SoundDefinitions();
        data.getSounds().add(sounds);
        data.setMaxDistance(64);

        this.soundDefinitions.getSoundDefinitions().put(id, data);
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

        if (this.terrainTexture != null) {
            exportJson(MAPPER, directory.resolve("textures/terrain_texture.json"), this.terrainTexture);
        }

        if (this.attachables != null) {
            for (Map.Entry<String, Attachables> attachable : this.attachables.entrySet()) {
                exportJson(MAPPER, directory.resolve(attachable.getKey()), attachable.getValue());
            }
        }

        if (this.soundDefinitions != null) {
            exportJson(MAPPER, directory.resolve("sounds/sound_definitions.json"), this.soundDefinitions);
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
