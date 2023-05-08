package org.geysermc.hydraulic.pack.bedrock.resource.textures.terraintexture.texturedata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import org.geysermc.hydraulic.pack.bedrock.resource.textures.terraintexture.texturedata.textures.Variations;

/**
 * Texture
 * <p>
 * A collection of texture files.
 */
public class Textures {
  public String path;

  @JsonProperty("tint_color")
  public String tintColor;

  public List<Variations> variations = new ArrayList<>();

  /**
   * A texture file.
   *
   * @return Path
   */
  public String getPath() {
    return this.path;
  }

  /**
   * A texture file.
   *
   * @param path Path
   */
  public void setPath(String path) {
    this.path = path;
  }

  /**
   * The tint color to be applied to the texture.
   *
   * @return Tint Color
   */
  public String getTintColor() {
    return this.tintColor;
  }

  /**
   * The tint color to be applied to the texture.
   *
   * @param tintColor Tint Color
   */
  public void setTintColor(String tintColor) {
    this.tintColor = tintColor;
  }

  /**
   * The possible variations to use for this texture.
   *
   * @return Variantions
   */
  public List<Variations> getVariations() {
    return this.variations;
  }

  /**
   * The possible variations to use for this texture.
   *
   * @param variations Variantions
   */
  public void setVariations(List<Variations> variations) {
    this.variations = variations;
  }
}
