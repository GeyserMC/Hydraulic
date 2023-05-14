package org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.volumetric.density;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Boolean;
import java.lang.Float;

public class LavaResistance {
  @JsonProperty("max_density")
  public Float maxDensity;

  @JsonProperty("max_density_height")
  public Float maxDensityHeight;

  @JsonProperty("zero_density_height")
  public Float zeroDensityHeight;

  public Boolean uniform;

  /**
   * The maximum amount of opaqueness that the ground fog will take on. A value from [0.0, 1.0].
   *
   * @return Maximum Density
   */
  public Float getMaxDensity() {
    return this.maxDensity;
  }

  /**
   * The maximum amount of opaqueness that the ground fog will take on. A value from [0.0, 1.0].
   *
   * @param maxDensity Maximum Density
   */
  public void setMaxDensity(float maxDensity) {
    this.maxDensity = maxDensity;
  }

  /**
   * The maximum amount of opaqueness that the ground fog will take on. A value from [0.0, 1.0].
   *
   * @param maxDensity Maximum Density
   */
  public void setMaxDensity(Float maxDensity) {
    this.maxDensity = maxDensity;
  }

  /**
   * The height in blocks that the ground fog will become it's maximum density.
   *
   * @return Maximum Density Height
   */
  public Float getMaxDensityHeight() {
    return this.maxDensityHeight;
  }

  /**
   * The height in blocks that the ground fog will become it's maximum density.
   *
   * @param maxDensityHeight Maximum Density Height
   */
  public void setMaxDensityHeight(float maxDensityHeight) {
    this.maxDensityHeight = maxDensityHeight;
  }

  /**
   * The height in blocks that the ground fog will become it's maximum density.
   *
   * @param maxDensityHeight Maximum Density Height
   */
  public void setMaxDensityHeight(Float maxDensityHeight) {
    this.maxDensityHeight = maxDensityHeight;
  }

  /**
   * The height in blocks that the ground fog will be completely transparent and begin to appear. This value needs to be at least 1 higher than `max_density_height`.
   *
   * @return Zero Density Height
   */
  public Float getZeroDensityHeight() {
    return this.zeroDensityHeight;
  }

  /**
   * The height in blocks that the ground fog will be completely transparent and begin to appear. This value needs to be at least 1 higher than `max_density_height`.
   *
   * @param zeroDensityHeight Zero Density Height
   */
  public void setZeroDensityHeight(float zeroDensityHeight) {
    this.zeroDensityHeight = zeroDensityHeight;
  }

  /**
   * The height in blocks that the ground fog will be completely transparent and begin to appear. This value needs to be at least 1 higher than `max_density_height`.
   *
   * @param zeroDensityHeight Zero Density Height
   */
  public void setZeroDensityHeight(Float zeroDensityHeight) {
    this.zeroDensityHeight = zeroDensityHeight;
  }

  /**
   * When set to true, the density will be uniform across all heights.
   *
   * @return Uniform
   */
  public Boolean getUniform() {
    return this.uniform;
  }

  /**
   * When set to true, the density will be uniform across all heights.
   *
   * @param uniform Uniform
   */
  public void setUniform(Boolean uniform) {
    this.uniform = uniform;
  }
}
