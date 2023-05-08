package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Emitter Lifetime Looping Component For 1.10.0
 */
public class EmitterLifetimeLooping {
  @JsonProperty("active_time")
  public String activeTime;

  @JsonProperty("sleep_time")
  public String sleepTime;

  public String getActiveTime() {
    return this.activeTime;
  }

  public void setActiveTime(String activeTime) {
    this.activeTime = activeTime;
  }

  public String getSleepTime() {
    return this.sleepTime;
  }

  public void setSleepTime(String sleepTime) {
    this.sleepTime = sleepTime;
  }
}
