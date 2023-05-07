package org.geysermc.hydraulic.pack.bedrock.resource.textures;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Item Texture File
 */
public class ItemTexture {
  @JsonProperty("resource_pack_name")
  public String resourcePackName;

  @JsonProperty("texture_data")
  public TextureData textureData;

  @JsonProperty("texture_name")
  public String textureName;

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
