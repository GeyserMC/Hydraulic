package org.geysermc.hydraulic.pack.bedrock.resource.animations.actoranimation.animations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import org.geysermc.hydraulic.pack.bedrock.resource.animations.actoranimation.animations.bones.RelativeTo;

/**
 * Bones
 * <p>
 * Defines how the bones in an animation move or transform.
 */
public class Bones {
  public String[] position;

  public String[] rotation;

  @JsonProperty("relative_to")
  public RelativeTo relativeTo;

  public float scale;

  public String[] getPosition() {
    return this.position;
  }

  public void setPosition(String[] position) {
    this.position = position;
  }

  public String[] getRotation() {
    return this.rotation;
  }

  public void setRotation(String[] rotation) {
    this.rotation = rotation;
  }

  /**
   * If set, makes the bone rotation relative to the entity instead of the bone's parent.
   *
   * @return Relative To
   */
  public RelativeTo getRelativeTo() {
    return this.relativeTo;
  }

  /**
   * If set, makes the bone rotation relative to the entity instead of the bone's parent.
   *
   * @param relativeTo Relative To
   */
  public void setRelativeTo(RelativeTo relativeTo) {
    this.relativeTo = relativeTo;
  }

  public float getScale() {
    return this.scale;
  }

  public void setScale(float scale) {
    this.scale = scale;
  }
}
