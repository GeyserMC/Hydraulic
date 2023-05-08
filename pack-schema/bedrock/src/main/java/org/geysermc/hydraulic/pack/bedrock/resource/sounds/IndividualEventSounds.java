package org.geysermc.hydraulic.pack.bedrock.resource.sounds;

import java.lang.Object;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * Individual Event Sounds
 * <p>
 * Individual event sounds definitions.
 */
public class IndividualEventSounds {
  private Map<String, Object> events = new HashMap<>();

  /**
   * Events.
   *
   * @return Events
   */
  public Map<String, Object> getEvents() {
    return this.events;
  }

  /**
   * Events.
   *
   * @param events Events
   */
  public void setEvents(Map<String, Object> events) {
    this.events = events;
  }
}
