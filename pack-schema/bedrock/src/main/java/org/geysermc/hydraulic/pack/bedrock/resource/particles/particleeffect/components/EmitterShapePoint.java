package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import java.lang.String;

/**
 * Emitter Shape Point Component For 1.10.0
 */
public class EmitterShapePoint {
  public String[] direction;

  public String[] offset;

  /**
   * @return Direction
   */
  public String[] getDirection() {
    return this.direction;
  }

  /**
   * @param direction Direction
   */
  public void setDirection(String[] direction) {
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
}
