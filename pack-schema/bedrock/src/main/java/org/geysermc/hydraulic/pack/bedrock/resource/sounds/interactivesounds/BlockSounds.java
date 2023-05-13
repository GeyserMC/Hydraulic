package org.geysermc.hydraulic.pack.bedrock.resource.sounds.interactivesounds;

import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * Block Sounds
 * <p>
 * Block sound definitions.
 */
public class BlockSounds {
  public float[] volume;

  public float[] pitch;

  private Map<String, String> events = new HashMap<>();

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
  public Map<String, String> getEvents() {
    return this.events;
  }

  /**
   * @param events Events
   */
  public void setEvents(Map<String, String> events) {
    this.events = events;
  }
}
