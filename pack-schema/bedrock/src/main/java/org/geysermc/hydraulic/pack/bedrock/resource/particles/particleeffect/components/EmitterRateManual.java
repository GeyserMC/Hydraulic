package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Emitter Rate Manual Component For 1.10.0
 */
public class EmitterRateManual {
  @JsonProperty("max_particles")
  public String maxParticles;

  public String getMaxParticles() {
    return this.maxParticles;
  }

  public void setMaxParticles(String maxParticles) {
    this.maxParticles = maxParticles;
  }
}
