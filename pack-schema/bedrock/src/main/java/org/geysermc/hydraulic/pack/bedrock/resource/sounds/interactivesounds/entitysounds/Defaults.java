package org.geysermc.hydraulic.pack.bedrock.resource.sounds.interactivesounds.entitysounds;

import java.lang.Object;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * Defaults
 * <p>
 * Default sound definitions.
 */
public class Defaults {
  public float[] volume;

  public float[] pitch;

  private Map<String, Object> events = new HashMap<>();

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
  public Map<String, Object> getEvents() {
    return this.events;
  }

  /**
   * @param events Entity Events
   */
  public void setEvents(Map<String, Object> events) {
    this.events = events;
  }
}
