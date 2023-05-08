package org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.volumetric.Density;
import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.volumetric.MediaCoefficients;

/**
 * Volumetric
 * <p>
 * The volumetric fog settings.
 */
public class Volumetric {
  public Density density;

  @JsonProperty("media_coefficients")
  public MediaCoefficients mediaCoefficients;

  /**
   * The density settings for different camera locations.
   *
   * @return Density
   */
  public Density getDensity() {
    return this.density;
  }

  /**
   * The density settings for different camera locations.
   *
   * @param density Density
   */
  public void setDensity(Density density) {
    this.density = density;
  }

  /**
   * The coefficient settings for the volumetric fog in different blocks.
   *
   * @return Media Coefficients
   */
  public MediaCoefficients getMediaCoefficients() {
    return this.mediaCoefficients;
  }

  /**
   * The coefficient settings for the volumetric fog in different blocks.
   *
   * @param mediaCoefficients Media Coefficients
   */
  public void setMediaCoefficients(MediaCoefficients mediaCoefficients) {
    this.mediaCoefficients = mediaCoefficients;
  }
}
