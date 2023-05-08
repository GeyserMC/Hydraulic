package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.particleappearancebillboard.Direction;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.particleappearancebillboard.Uv;

/**
 * Particle Appearance Billboard Component For 1.10.0
 */
public class ParticleAppearanceBillboard {
  @JsonProperty("facing_camera_mode")
  public String facingCameraMode;

  public Direction direction;

  public Uv uv;

  /**
   * Used to orient the billboard.
   *
   * @return Facing Camera Mode
   */
  public String getFacingCameraMode() {
    return this.facingCameraMode;
  }

  /**
   * Used to orient the billboard.
   *
   * @param facingCameraMode Facing Camera Mode
   */
  public void setFacingCameraMode(String facingCameraMode) {
    this.facingCameraMode = facingCameraMode;
  }

  public Direction getDirection() {
    return this.direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  /**
   * @return Uv
   */
  public Uv getUv() {
    return this.uv;
  }

  /**
   * @param uv Uv
   */
  public void setUv(Uv uv) {
    this.uv = uv;
  }
}
