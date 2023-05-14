package org.geysermc.hydraulic.pack.bedrock.resource.sounds;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Integer;
import java.lang.String;

/**
 * Music File
 * <p>
 * The definition file of music of the resourcepack.
 */
public class MusicDefinitions {
  @JsonProperty("event_name")
  public String eventName;

  @JsonProperty("min_delay")
  public Integer minDelay;

  @JsonProperty("max_delay")
  public Integer maxDelay;

  /**
   * The name of the minecraft music event.
   *
   * @return Event Name
   */
  public String getEventName() {
    return this.eventName;
  }

  /**
   * The name of the minecraft music event.
   *
   * @param eventName Event Name
   */
  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  /**
   * @return Minimum Delay
   */
  public Integer getMinDelay() {
    return this.minDelay;
  }

  /**
   * @param minDelay Minimum Delay
   */
  public void setMinDelay(int minDelay) {
    this.minDelay = minDelay;
  }

  /**
   * @return Maximum Delay
   */
  public Integer getMaxDelay() {
    return this.maxDelay;
  }

  /**
   * @param maxDelay Maximum Delay
   */
  public void setMaxDelay(int maxDelay) {
    this.maxDelay = maxDelay;
  }
}
