package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Boolean;
import java.lang.String;

/**
 * Emitter Shape Sphere Component For 1.10.0
 */
public class EmitterShapeSphere {
  public String direction;

  public String[] offset;

  public String radius;

  @JsonProperty("surface_only")
  public Boolean surfaceOnly;

  public String getDirection() {
    return this.direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  /**
   * @return Offset
   */
  public String[] getOffset() {
    return this.offset;
  }

  /**
   * @param offset Offset
   */
  public void setOffset(String[] offset) {
    this.offset = offset;
  }

  public String getRadius() {
    return this.radius;
  }

  public void setRadius(String radius) {
    this.radius = radius;
  }

  /**
   * @return Surface Only
   */
  public Boolean getSurfaceOnly() {
    return this.surfaceOnly;
  }

  /**
   * @param surfaceOnly Surface Only
   */
  public void setSurfaceOnly(Boolean surfaceOnly) {
    this.surfaceOnly = surfaceOnly;
  }
}
