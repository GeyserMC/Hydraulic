package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import java.lang.String;

/**
 * Particle Appearance Tinting Component For 1.10.0
 * <p>
 * Color fields are special, they can be either an RGB, or a `#RRGGBB` field (or RGBA or `AARRGGBB`).  If RGB(A), the channels are from 0 to 1.  If the string `#AARRGGBB`, then the values are hex from 00 to ff.
 */
public class ParticleAppearanceTinting {
  public String color;

  /**
   * Direct color field.
   */
  public String getColor() {
    return this.color;
  }

  /**
   * Direct color field.
   */
  public void setColor(String color) {
    this.color = color;
  }
}
