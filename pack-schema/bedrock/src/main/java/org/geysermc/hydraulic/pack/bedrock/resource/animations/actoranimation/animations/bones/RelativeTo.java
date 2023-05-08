package org.geysermc.hydraulic.pack.bedrock.resource.animations.actoranimation.animations.bones;

import java.lang.String;

/**
 * Relative To
 * <p>
 * If set, makes the bone rotation relative to the entity instead of the bone's parent.
 */
public class RelativeTo {
  public String rotation;

  /**
   * If set, makes the bone rotation relative to the entity instead of the bone's parent.
   *
   * @return Rotation
   */
  public String getRotation() {
    return this.rotation;
  }

  /**
   * If set, makes the bone rotation relative to the entity instead of the bone's parent.
   *
   * @param rotation Rotation
   */
  public void setRotation(String rotation) {
    this.rotation = rotation;
  }
}
