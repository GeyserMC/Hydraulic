package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Emitter Rate Instant Component For 1.10.0
 */
public class EmitterRateInstant {
  @JsonProperty("num_particles")
  public String numParticles;

  public String getNumParticles() {
    return this.numParticles;
  }

  public void setNumParticles(String numParticles) {
    this.numParticles = numParticles;
  }
}
