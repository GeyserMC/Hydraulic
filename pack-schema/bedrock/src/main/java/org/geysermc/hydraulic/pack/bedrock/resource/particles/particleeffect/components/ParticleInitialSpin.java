package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Particle Initial Spin Component For 1.10.0
 * <p>
 * Starts the particle with a specified orientation and rotation rate.
 */
public class ParticleInitialSpin {
  public String rotation;

  @JsonProperty("rotation_rate")
  public String rotationRate;

  public String getRotation() {
    return this.rotation;
  }

  public void setRotation(String rotation) {
    this.rotation = rotation;
  }

  public String getRotationRate() {
    return this.rotationRate;
  }

  public void setRotationRate(String rotationRate) {
    this.rotationRate = rotationRate;
  }
}
