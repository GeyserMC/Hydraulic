package org.geysermc.hydraulic.pack.bedrock.resource.entity.cliententity.description;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Spawn Egg
 * <p>
 * The definition of how the spawn_egg icon looks like.
 */
public class SpawnEgg {
  @JsonProperty("base_color")
  public String baseColor;

  @JsonProperty("overlay_color")
  public String overlayColor;

  public String texture;

  @JsonProperty("texture_index")
  public int textureIndex;

  /**
   * The basic color of the egg.
   *
   * @return Base Color
   */
  public String getBaseColor() {
    return this.baseColor;
  }

  /**
   * The basic color of the egg.
   *
   * @param baseColor Base Color
   */
  public void setBaseColor(String baseColor) {
    this.baseColor = baseColor;
  }

  /**
   * The colors of the dots on the egg.
   *
   * @return Overlay Color
   */
  public String getOverlayColor() {
    return this.overlayColor;
  }

  /**
   * The colors of the dots on the egg.
   *
   * @param overlayColor Overlay Color
   */
  public void setOverlayColor(String overlayColor) {
    this.overlayColor = overlayColor;
  }

  /**
   * The texture reference in item_texture.json
   *
   * @return Texture
   */
  public String getTexture() {
    return this.texture;
  }

  /**
   * The texture reference in item_texture.json
   *
   * @param texture Texture
   */
  public void setTexture(String texture) {
    this.texture = texture;
  }

  /**
   * The index of the texture.
   *
   * @return Texture Index
   */
  public int getTextureIndex() {
    return this.textureIndex;
  }

  /**
   * The index of the texture.
   *
   * @param textureIndex Texture Index
   */
  public void setTextureIndex(int textureIndex) {
    this.textureIndex = textureIndex;
  }
}
