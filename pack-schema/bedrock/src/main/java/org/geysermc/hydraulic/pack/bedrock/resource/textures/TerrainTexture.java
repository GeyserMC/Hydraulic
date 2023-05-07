package org.geysermc.hydraulic.pack.bedrock.resource.textures;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Terrain Texture File
 * <p>
 * An collection of texture definitions.
 */
public class TerrainTexture {
  @JsonProperty("num_mip_levels")
  public int numMipLevels;

  public int padding;

  @JsonProperty("resource_pack_name")
  public String resourcePackName;

  @JsonProperty("texture_data")
  public TextureData textureData;

  @JsonProperty("texture_name")
  public String textureName;

  /**
   * @return Num Mip Levels
   */
  public int getNumMipLevels() {
    return this.numMipLevels;
  }

  /**
   * @param numMipLevels Num Mip Levels
   */
  public void setNumMipLevels(int numMipLevels) {
    this.numMipLevels = numMipLevels;
  }

  /**
   * @return Padding
   */
  public int getPadding() {
    return this.padding;
  }

  /**
   * @param padding Padding
   */
  public void setPadding(int padding) {
    this.padding = padding;
  }

  /**
   * @return Resource Pack Name
   */
  public String getResourcePackName() {
    return this.resourcePackName;
  }

  /**
   * @param resourcePackName Resource Pack Name
   */
  public void setResourcePackName(String resourcePackName) {
    this.resourcePackName = resourcePackName;
  }

  /**
   * @return Texture Data
   */
  public TextureData getTextureData() {
    return this.textureData;
  }

  /**
   * @param textureData Texture Data
   */
  public void setTextureData(TextureData textureData) {
    this.textureData = textureData;
  }

  /**
   * @return Texture Name
   */
  public String getTextureName() {
    return this.textureName;
  }

  /**
   * @param textureName Texture Name
   */
  public void setTextureName(String textureName) {
    this.textureName = textureName;
  }

  /**
   * Texture Data
   */
  public static class TextureData {
  }
}
