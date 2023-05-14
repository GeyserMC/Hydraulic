package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.particleappearancebillboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Integer;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.particleappearancebillboard.uv.Flipbook;

/**
 * Uv
 */
public class Uv {
  @JsonProperty("texture_width")
  public Integer textureWidth;

  @JsonProperty("texture_height")
  public Integer textureHeight;

  public Flipbook flipbook;

  /**
   * @return Texture Width
   */
  public Integer getTextureWidth() {
    return this.textureWidth;
  }

  /**
   * @param textureWidth Texture Width
   */
  public void setTextureWidth(int textureWidth) {
    this.textureWidth = textureWidth;
  }

  /**
   * @return Texture Height
   */
  public Integer getTextureHeight() {
    return this.textureHeight;
  }

  /**
   * @param textureHeight Texture Height
   */
  public void setTextureHeight(int textureHeight) {
    this.textureHeight = textureHeight;
  }

  /**
   * @return Flipbook
   */
  public Flipbook getFlipbook() {
    return this.flipbook;
  }

  /**
   * @param flipbook Flipbook
   */
  public void setFlipbook(Flipbook flipbook) {
    this.flipbook = flipbook;
  }
}
