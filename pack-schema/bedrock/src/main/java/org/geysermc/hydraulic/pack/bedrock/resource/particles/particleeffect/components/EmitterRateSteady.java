package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Emitter Rate Steady Component For 1.10.0
 */
public class EmitterRateSteady {
  @JsonProperty("max_particles")
  public String maxParticles;

  @JsonProperty("spawn_rate")
  public String spawnRate;

  public String getMaxParticles() {
    return this.maxParticles;
  }

  public void setMaxParticles(String maxParticles) {
    this.maxParticles = maxParticles;
  }

  public String getSpawnRate() {
    return this.spawnRate;
  }

  public void setSpawnRate(String spawnRate) {
    this.spawnRate = spawnRate;
  }
}
