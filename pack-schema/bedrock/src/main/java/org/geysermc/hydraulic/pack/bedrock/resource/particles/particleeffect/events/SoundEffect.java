package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Sound Effect
 */
public class SoundEffect {
  @JsonProperty("event_name")
  public String eventName;

  /**
   * @return Event Name
   */
  public String getEventName() {
    return this.eventName;
  }

  /**
   * @param eventName Event Name
   */
  public void setEventName(String eventName) {
    this.eventName = eventName;
  }
}
