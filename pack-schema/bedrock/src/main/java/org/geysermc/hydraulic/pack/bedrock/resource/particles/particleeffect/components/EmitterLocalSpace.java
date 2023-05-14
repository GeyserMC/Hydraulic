package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import java.lang.Boolean;

/**
 * Emitter Local Space Component For 1.10.0
 */
public class EmitterLocalSpace {
  public Boolean position;

  public Boolean rotation;

  public Boolean velocity;

  /**
   * @return Position
   */
  public Boolean getPosition() {
    return this.position;
  }

  /**
   * @param position Position
   */
  public void setPosition(boolean position) {
    this.position = position;
  }

  /**
   * @return Rotation
   */
  public Boolean getRotation() {
    return this.rotation;
  }

  /**
   * @param rotation Rotation
   */
  public void setRotation(boolean rotation) {
    this.rotation = rotation;
  }

  /**
   * @return Rotation
   */
  public Boolean getVelocity() {
    return this.velocity;
  }

  /**
   * @param velocity Rotation
   */
  public void setVelocity(boolean velocity) {
    this.velocity = velocity;
  }
}
