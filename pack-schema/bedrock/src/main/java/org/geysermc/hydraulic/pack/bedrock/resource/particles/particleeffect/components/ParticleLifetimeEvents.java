package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Particle Lifetime Events Component For 1.10.0
 */
public class ParticleLifetimeEvents {
  @JsonProperty("creation_event")
  public String[] creationEvent;

  @JsonProperty("expiration_event")
  public String[] expirationEvent;

  public String[] getCreationEvent() {
    return this.creationEvent;
  }

  public void setCreationEvent(String[] creationEvent) {
    this.creationEvent = creationEvent;
  }

  public String[] getExpirationEvent() {
    return this.expirationEvent;
  }

  public void setExpirationEvent(String[] expirationEvent) {
    this.expirationEvent = expirationEvent;
  }
}
