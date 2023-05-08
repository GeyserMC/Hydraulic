package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.events;

import java.lang.String;

/**
 * Particle Effect
 */
public class ParticleEffect {
  public String effect;

  public String type;

  /**
   * @return Effect
   */
  public String getEffect() {
    return this.effect;
  }

  /**
   * @param effect Effect
   */
  public void setEffect(String effect) {
    this.effect = effect;
  }

  /**
   * @return Type
   */
  public String getType() {
    return this.type;
  }

  /**
   * @param type Type
   */
  public void setType(String type) {
    this.type = type;
  }
}
