package org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.distance.water;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.distance.water.transitionfog.InitFog;

/**
 * Transition Fog
 * <p>
 * Additional fog data which will slowly transition to the distance fog of current biome.
 */
public class TransitionFog {
  @JsonProperty("init_fog")
  public InitFog initFog;

  @JsonProperty("min_percent")
  public float minPercent;

  @JsonProperty("mid_seconds")
  public float midSeconds;

  @JsonProperty("mid_percent")
  public float midPercent;

  @JsonProperty("max_seconds")
  public float maxSeconds;

  /**
   * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
   *
   * @return Initial Fog
   */
  public InitFog getInitFog() {
    return this.initFog;
  }

  /**
   * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
   *
   * @param initFog Initial Fog
   */
  public void setInitFog(InitFog initFog) {
    this.initFog = initFog;
  }

  /**
   * The minimum progress of fog transition.
   *
   * @return Minimum Percent
   */
  public float getMinPercent() {
    return this.minPercent;
  }

  /**
   * The minimum progress of fog transition.
   *
   * @param minPercent Minimum Percent
   */
  public void setMinPercent(float minPercent) {
    this.minPercent = minPercent;
  }

  /**
   * The time takes to reach certain progress('mid_percent') of fog transition.
   *
   * @return Midpoint Seconds
   */
  public float getMidSeconds() {
    return this.midSeconds;
  }

  /**
   * The time takes to reach certain progress('mid_percent') of fog transition.
   *
   * @param midSeconds Midpoint Seconds
   */
  public void setMidSeconds(float midSeconds) {
    this.midSeconds = midSeconds;
  }

  /**
   * The progress of fog transition after 'mid_seconds' seconds.
   *
   * @return Midpoint Percent
   */
  public float getMidPercent() {
    return this.midPercent;
  }

  /**
   * The progress of fog transition after 'mid_seconds' seconds.
   *
   * @param midPercent Midpoint Percent
   */
  public void setMidPercent(float midPercent) {
    this.midPercent = midPercent;
  }

  /**
   * Total amount of time takes to complete fog transition.
   *
   * @return Maximum Seconds
   */
  public float getMaxSeconds() {
    return this.maxSeconds;
  }

  /**
   * Total amount of time takes to complete fog transition.
   *
   * @param maxSeconds Maximum Seconds
   */
  public void setMaxSeconds(float maxSeconds) {
    this.maxSeconds = maxSeconds;
  }
}
