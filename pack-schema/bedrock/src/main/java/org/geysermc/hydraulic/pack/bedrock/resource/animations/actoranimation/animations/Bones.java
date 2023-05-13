package org.geysermc.hydraulic.pack.bedrock.resource.animations.actoranimation.animations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import org.geysermc.hydraulic.pack.bedrock.resource.animations.actoranimation.animations.bones.Position;
import org.geysermc.hydraulic.pack.bedrock.resource.animations.actoranimation.animations.bones.RelativeTo;
import org.geysermc.hydraulic.pack.bedrock.resource.animations.actoranimation.animations.bones.Rotation;
import org.geysermc.hydraulic.pack.bedrock.resource.animations.actoranimation.animations.bones.Scale;

/**
 * Bones
 * <p>
 * Defines how the bones in an animation move or transform.
 */
public class Bones {
  private Map<String, Position> position = new HashMap<>();

  private Map<String, Rotation> rotation = new HashMap<>();

  @JsonProperty("relative_to")
  public RelativeTo relativeTo;

  private Map<String, Scale> scale = new HashMap<>();

  public Map<String, Position> getPosition() {
    return this.position;
  }

  public void setPosition(Map<String, Position> position) {
    this.position = position;
  }

  public Map<String, Rotation> getRotation() {
    return this.rotation;
  }

  public void setRotation(Map<String, Rotation> rotation) {
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

  public Map<String, Scale> getScale() {
    return this.scale;
  }

  public void setScale(Map<String, Scale> scale) {
    this.scale = scale;
  }
}
