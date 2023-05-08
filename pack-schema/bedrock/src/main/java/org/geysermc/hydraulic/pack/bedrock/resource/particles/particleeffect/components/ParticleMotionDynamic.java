package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Particle Motion Dynamic Component For 1.10.0
 * <p>
 * This component specifies the dynamic properties of the particle, from a simulation standpoint what forces act upon the particle? These dynamics alter the velocity of the particle, which is a combination of the direction of the particle and the speed. Particle direction will always be in the direction of the velocity of the particle.
 */
public class ParticleMotionDynamic {
  @JsonProperty("linear_drag_coefficient")
  public String linearDragCoefficient;

  @JsonProperty("rotation_acceleration")
  public String rotationAcceleration;

  @JsonProperty("rotation_drag_coefficient")
  public String rotationDragCoefficient;

  public String getLinearDragCoefficient() {
    return this.linearDragCoefficient;
  }

  public void setLinearDragCoefficient(String linearDragCoefficient) {
    this.linearDragCoefficient = linearDragCoefficient;
  }

  public String getRotationAcceleration() {
    return this.rotationAcceleration;
  }

  public void setRotationAcceleration(String rotationAcceleration) {
    this.rotationAcceleration = rotationAcceleration;
  }

  public String getRotationDragCoefficient() {
    return this.rotationDragCoefficient;
  }

  public void setRotationDragCoefficient(String rotationDragCoefficient) {
    this.rotationDragCoefficient = rotationDragCoefficient;
  }
}
