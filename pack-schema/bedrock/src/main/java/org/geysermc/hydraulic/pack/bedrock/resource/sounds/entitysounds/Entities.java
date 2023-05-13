package org.geysermc.hydraulic.pack.bedrock.resource.sounds.entitysounds;

import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import org.geysermc.hydraulic.pack.bedrock.resource.sounds.entitysounds.entities.Events;

/**
 * Entity Sound
 * <p>
 * Entity sound definitions.
 */
public class Entities {
  public float[] volume;

  public float[] pitch;

  private Map<String, Events> events = new HashMap<>();

  /**
   * A random selection between a minimum and maximum.
   */
  public float[] getVolume() {
    return this.volume;
  }

  /**
   * A random selection between a minimum and maximum.
   */
  public void setVolume(float[] volume) {
    this.volume = volume;
  }

  /**
   * A random selection between a minimum and maximum.
   */
  public float[] getPitch() {
    return this.pitch;
  }

  /**
   * A random selection between a minimum and maximum.
   */
  public void setPitch(float[] pitch) {
    this.pitch = pitch;
  }

  /**
   * @return Events
   */
  public Map<String, Events> getEvents() {
    return this.events;
  }

  /**
   * @param events Events
   */
  public void setEvents(Map<String, Events> events) {
    this.events = events;
  }
}
