package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.particleappearancebillboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Float;
import java.lang.String;

public class Direction {
  public String mode;

  @JsonProperty("min_speed_threshold")
  public Float minSpeedThreshold;

  /**
   * Specified how to calculate the billboard direction of a particle.
   */
  public String getMode() {
    return this.mode;
  }

  /**
   * Specified how to calculate the billboard direction of a particle.
   */
  public void setMode(String mode) {
    this.mode = mode;
  }

  /**
   * The direction is set if the speed of the particle is above the threshold.
   */
  public Float getMinSpeedThreshold() {
    return this.minSpeedThreshold;
  }

  /**
   * The direction is set if the speed of the particle is above the threshold.
   */
  public void setMinSpeedThreshold(float minSpeedThreshold) {
    this.minSpeedThreshold = minSpeedThreshold;
  }
}
