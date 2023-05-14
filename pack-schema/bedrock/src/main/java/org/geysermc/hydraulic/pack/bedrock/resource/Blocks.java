package org.geysermc.hydraulic.pack.bedrock.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Float;
import java.lang.String;
import org.geysermc.hydraulic.pack.bedrock.resource.blocks.CarriedTextures;
import org.geysermc.hydraulic.pack.bedrock.resource.blocks.Isotropic;
import org.geysermc.hydraulic.pack.bedrock.resource.blocks.Textures;

/**
 * Blocks
 * <p>
 * The minecraft block definition file.
 */
public class Blocks {
  @JsonProperty("format_version")
  public String formatVersion;

  @JsonProperty("brightness_gamma")
  public Float brightnessGamma;

  @JsonProperty("carried_textures")
  public CarriedTextures carriedTextures;

  public Isotropic isotropic;

  public String sound;

  public Textures textures;

  /**
   * A version that tells minecraft what type of data format can be expected when reading this file.
   *
   * @return Format Version
   */
  public String getFormatVersion() {
    return this.formatVersion;
  }

  /**
   * A version that tells minecraft what type of data format can be expected when reading this file.
   *
   * @param formatVersion Format Version
   */
  public void setFormatVersion(String formatVersion) {
    this.formatVersion = formatVersion;
  }

  /**
   * Specifies the gamma brightness level to apply to the block texture.
   *
   * @return Brightness Gamma
   */
  public Float getBrightnessGamma() {
    return this.brightnessGamma;
  }

  /**
   * Specifies the gamma brightness level to apply to the block texture.
   *
   * @param brightnessGamma Brightness Gamma
   */
  public void setBrightnessGamma(float brightnessGamma) {
    this.brightnessGamma = brightnessGamma;
  }

  public CarriedTextures getCarriedTextures() {
    return this.carriedTextures;
  }

  public void setCarriedTextures(CarriedTextures carriedTextures) {
    this.carriedTextures = carriedTextures;
  }

  public Isotropic getIsotropic() {
    return this.isotropic;
  }

  public void setIsotropic(Isotropic isotropic) {
    this.isotropic = isotropic;
  }

  /**
   * The sound definition of this block.
   *
   * @return Sound
   */
  public String getSound() {
    return this.sound;
  }

  /**
   * The sound definition of this block.
   *
   * @param sound Sound
   */
  public void setSound(String sound) {
    this.sound = sound;
  }

  public Textures getTextures() {
    return this.textures;
  }

  public void setTextures(Textures textures) {
    this.textures = textures;
  }
}
