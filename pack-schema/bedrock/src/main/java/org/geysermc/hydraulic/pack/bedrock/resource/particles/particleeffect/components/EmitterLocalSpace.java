package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

/**
 * Emitter Local Space Component For 1.10.0
 */
public class EmitterLocalSpace {
  public boolean position;

  public boolean rotation;

  public boolean velocity;

  /**
   * @return Position
   */
  public boolean getPosition() {
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
  public boolean getRotation() {
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
  public boolean getVelocity() {
    return this.velocity;
  }

  /**
   * @param velocity Rotation
   */
  public void setVelocity(boolean velocity) {
    this.velocity = velocity;
  }
}
