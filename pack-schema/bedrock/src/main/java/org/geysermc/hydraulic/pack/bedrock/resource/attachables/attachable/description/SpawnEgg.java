package org.geysermc.hydraulic.pack.bedrock.resource.attachables.attachable.description;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Spawn Egg
 */
public class SpawnEgg {
  @JsonProperty("base_colour")
  public String baseColour;

  @JsonProperty("overlay_color")
  public String overlayColor;

  public String texture;

  @JsonProperty("texture_index")
  public int textureIndex;

  /**
   * @return Base Colour
   */
  public String getBaseColour() {
    return this.baseColour;
  }

  /**
   * @param baseColour Base Colour
   */
  public void setBaseColour(String baseColour) {
    this.baseColour = baseColour;
  }

  /**
   * @return Overlay Color
   */
  public String getOverlayColor() {
    return this.overlayColor;
  }

  /**
   * @param overlayColor Overlay Color
   */
  public void setOverlayColor(String overlayColor) {
    this.overlayColor = overlayColor;
  }

  /**
   * @return Texture
   */
  public String getTexture() {
    return this.texture;
  }

  /**
   * @param texture Texture
   */
  public void setTexture(String texture) {
    this.texture = texture;
  }

  /**
   * @return Texture Index
   */
  public int getTextureIndex() {
    return this.textureIndex;
  }

  /**
   * @param textureIndex Texture Index
   */
  public void setTextureIndex(int textureIndex) {
    this.textureIndex = textureIndex;
  }
}
