package org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry.bones;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Boolean;

public class Locators {
  public float[] offset;

  public float[] rotation;

  @JsonProperty("ignore_inherited_scale")
  public Boolean ignoreInheritedScale;

  /**
   * Position of the locator in model space.
   */
  public float[] getOffset() {
    return this.offset;
  }

  /**
   * Position of the locator in model space.
   */
  public void setOffset(float[] offset) {
    this.offset = offset;
  }

  /**
   * Rotation of the locator in model space.
   */
  public float[] getRotation() {
    return this.rotation;
  }

  /**
   * Rotation of the locator in model space.
   */
  public void setRotation(float[] rotation) {
    this.rotation = rotation;
  }

  /**
   * Discard scale inherited from parent bone.
   */
  public Boolean getIgnoreInheritedScale() {
    return this.ignoreInheritedScale;
  }

  /**
   * Discard scale inherited from parent bone.
   */
  public void setIgnoreInheritedScale(boolean ignoreInheritedScale) {
    this.ignoreInheritedScale = ignoreInheritedScale;
  }
}
