package org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.volumetric;

import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.volumetric.mediacoefficients.Air;
import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.volumetric.mediacoefficients.Cloud;
import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.volumetric.mediacoefficients.Water;

/**
 * Media Coefficients
 * <p>
 * The coefficient settings for the volumetric fog in different blocks.
 */
public class MediaCoefficients {
  public Air air;

  public Water water;

  public Cloud cloud;

  public Air getAir() {
    return this.air;
  }

  public void setAir(Air air) {
    this.air = air;
  }

  public Water getWater() {
    return this.water;
  }

  public void setWater(Water water) {
    this.water = water;
  }

  public Cloud getCloud() {
    return this.cloud;
  }

  public void setCloud(Cloud cloud) {
    this.cloud = cloud;
  }
}
