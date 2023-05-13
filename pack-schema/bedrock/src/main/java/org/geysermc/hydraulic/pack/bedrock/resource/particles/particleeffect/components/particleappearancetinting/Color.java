package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.particleappearancetinting;

import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * Interpolation based color.
 */
public class Color {
  private Map<String, String> gradient = new HashMap<>();

  public String interpolant;

  /**
   * An object of colors.
   */
  public Map<String, String> getGradient() {
    return this.gradient;
  }

  /**
   * An object of colors.
   */
  public void setGradient(Map<String, String> gradient) {
    this.gradient = gradient;
  }

  public String getInterpolant() {
    return this.interpolant;
  }

  public void setInterpolant(String interpolant) {
    this.interpolant = interpolant;
  }
}
