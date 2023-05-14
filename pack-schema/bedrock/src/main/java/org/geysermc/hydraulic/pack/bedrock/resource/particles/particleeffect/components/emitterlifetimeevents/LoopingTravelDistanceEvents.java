package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.emitterlifetimeevents;

import java.lang.Float;
import java.lang.String;

/**
 * Distance Event
 */
public class LoopingTravelDistanceEvents {
  public Float distance;

  public String[] effects;

  /**
   * @return Distance
   */
  public Float getDistance() {
    return this.distance;
  }

  /**
   * @param distance Distance
   */
  public void setDistance(float distance) {
    this.distance = distance;
  }

  public String[] getEffects() {
    return this.effects;
  }

  public void setEffects(String[] effects) {
    this.effects = effects;
  }
}
