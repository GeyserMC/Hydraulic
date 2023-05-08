package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Emitter Lifetime Once Component For 1.10.0
 */
public class EmitterLifetimeOnce {
  @JsonProperty("active_time")
  public String activeTime;

  public String getActiveTime() {
    return this.activeTime;
  }

  public void setActiveTime(String activeTime) {
    this.activeTime = activeTime;
  }
}
