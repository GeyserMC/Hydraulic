package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.particleappearancetinting.Color;

/**
 * Particle Appearance Tinting Component For 1.10.0
 * <p>
 * Color fields are special, they can be either an RGB, or a `#RRGGBB` field (or RGBA or `AARRGGBB`).  If RGB(A), the channels are from 0 to 1.  If the string `#AARRGGBB`, then the values are hex from 00 to ff.
 */
public class ParticleAppearanceTinting {
  public Color color;

  /**
   * Interpolation based color.
   */
  public Color getColor() {
    return this.color;
  }

  /**
   * Interpolation based color.
   */
  public void setColor(Color color) {
    this.color = color;
  }
}
