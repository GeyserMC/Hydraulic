package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Particle Motion Collision Component For 1.10.0
 */
public class ParticleMotionCollision {
  @JsonProperty("collision_drag")
  public float collisionDrag;

  @JsonProperty("coefficient_of_restitution")
  public float coefficientOfRestitution;

  @JsonProperty("collision_radius")
  public float collisionRadius;

  public String enabled;

  @JsonProperty("expire_on_contact")
  public boolean expireOnContact;

  /**
   * @return Collision Drag
   */
  public float getCollisionDrag() {
    return this.collisionDrag;
  }

  /**
   * @param collisionDrag Collision Drag
   */
  public void setCollisionDrag(float collisionDrag) {
    this.collisionDrag = collisionDrag;
  }

  /**
   * @return Coefficient Of Restitution
   */
  public float getCoefficientOfRestitution() {
    return this.coefficientOfRestitution;
  }

  /**
   * @param coefficientOfRestitution Coefficient Of Restitution
   */
  public void setCoefficientOfRestitution(float coefficientOfRestitution) {
    this.coefficientOfRestitution = coefficientOfRestitution;
  }

  /**
   * @return Collision Radius
   */
  public float getCollisionRadius() {
    return this.collisionRadius;
  }

  /**
   * @param collisionRadius Collision Radius
   */
  public void setCollisionRadius(float collisionRadius) {
    this.collisionRadius = collisionRadius;
  }

  public String getEnabled() {
    return this.enabled;
  }

  public void setEnabled(String enabled) {
    this.enabled = enabled;
  }

  /**
   * @return Expire On Contact
   */
  public boolean getExpireOnContact() {
    return this.expireOnContact;
  }

  /**
   * @param expireOnContact Expire On Contact
   */
  public void setExpireOnContact(boolean expireOnContact) {
    this.expireOnContact = expireOnContact;
  }
}
