package org.geysermc.hydraulic.pack.bedrock.resource.animations.actoranimation.animations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Object;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
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

  private Map<String, Object> scale = new HashMap<>();

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

  public Map<String, Object> getScale() {
    return this.scale;
  }

  public void setScale(Map<String, Object> scale) {
    this.scale = scale;
  }
}
