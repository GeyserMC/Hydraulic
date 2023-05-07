package org.geysermc.hydraulic.pack.bedrock.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Blocks
 * <p>
 * The minecraft block definition file.
 */
public class Blocks {
  @JsonProperty("brightness_gamma")
  public float brightnessGamma;

  public String sound;

  /**
   * Specifies the gamma brightness level to apply to the block texture.
   *
   * @return Brightness Gamma
   */
  public float getBrightnessGamma() {
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
}
