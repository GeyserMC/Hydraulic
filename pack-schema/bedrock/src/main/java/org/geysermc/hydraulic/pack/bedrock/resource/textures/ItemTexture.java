package org.geysermc.hydraulic.pack.bedrock.resource.textures;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import org.geysermc.hydraulic.pack.bedrock.resource.textures.itemtexture.TextureData;

/**
 * Item Texture File
 */
public class ItemTexture {
  @JsonProperty("resource_pack_name")
  public String resourcePackName;

  @JsonProperty("texture_data")
  private Map<String, TextureData> textureData = new HashMap<>();

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
  public Map<String, TextureData> getTextureData() {
    return this.textureData;
  }

  /**
   * @param textureData Texture Data
   */
  public void setTextureData(Map<String, TextureData> textureData) {
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
}
