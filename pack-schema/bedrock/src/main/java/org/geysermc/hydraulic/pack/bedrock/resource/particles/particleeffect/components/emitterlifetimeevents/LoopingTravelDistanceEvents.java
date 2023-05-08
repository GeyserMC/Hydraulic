package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.emitterlifetimeevents;

import java.lang.String;

/**
 * Distance Event
 */
public class LoopingTravelDistanceEvents {
  public float distance;

  public String effects;

  /**
   * @return Distance
   */
  public float getDistance() {
    return this.distance;
  }

  /**
   * @param distance Distance
   */
  public void setDistance(float distance) {
    this.distance = distance;
  }

  public String getEffects() {
    return this.effects;
  }

  public void setEffects(String effects) {
    this.effects = effects;
  }
}
