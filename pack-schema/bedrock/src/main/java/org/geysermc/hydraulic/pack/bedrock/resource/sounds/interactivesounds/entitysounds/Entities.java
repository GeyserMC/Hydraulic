package org.geysermc.hydraulic.pack.bedrock.resource.sounds.interactivesounds.entitysounds;

import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import org.geysermc.hydraulic.pack.bedrock.resource.sounds.interactivesounds.entitysounds.entities.Events;

/**
 * Entites Sounds
 * <p>
 * Entities sound definitions.
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
   * @return Entity Events
   */
  public Map<String, Events> getEvents() {
    return this.events;
  }

  /**
   * @param events Entity Events
   */
  public void setEvents(Map<String, Events> events) {
    this.events = events;
  }
}
