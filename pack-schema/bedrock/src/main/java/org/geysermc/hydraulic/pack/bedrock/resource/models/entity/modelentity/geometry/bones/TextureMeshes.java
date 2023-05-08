package org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry.bones;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

public class TextureMeshes {
  @JsonProperty("local_pivot")
  public float[] localPivot;

  public float[] position;

  public float[] rotation;

  public float[] scale;

  public String texture;

  /**
   * The pivot point on the texture (in *texture space* not entity or bone space) of the texture geometry.
   */
  public float[] getLocalPivot() {
    return this.localPivot;
  }

  /**
   * The pivot point on the texture (in *texture space* not entity or bone space) of the texture geometry.
   */
  public void setLocalPivot(float[] localPivot) {
    this.localPivot = localPivot;
  }

  /**
   * The position of the pivot point after rotation (in *entity space* not texture or bone space) of the texture geometry.
   */
  public float[] getPosition() {
    return this.position;
  }

  /**
   * The position of the pivot point after rotation (in *entity space* not texture or bone space) of the texture geometry.
   */
  public void setPosition(float[] position) {
    this.position = position;
  }

  /**
   * The rotation (in degrees) of the texture geometry relative to the offset.
   */
  public float[] getRotation() {
    return this.rotation;
  }

  /**
   * The rotation (in degrees) of the texture geometry relative to the offset.
   */
  public void setRotation(float[] rotation) {
    this.rotation = rotation;
  }

  /**
   * The scale (in degrees) of the texture geometry relative to the offset.
   */
  public float[] getScale() {
    return this.scale;
  }

  /**
   * The scale (in degrees) of the texture geometry relative to the offset.
   */
  public void setScale(float[] scale) {
    this.scale = scale;
  }

  /**
   * The friendly-named texture to use.
   */
  public String getTexture() {
    return this.texture;
  }

  /**
   * The friendly-named texture to use.
   */
  public void setTexture(String texture) {
    this.texture = texture;
  }
}
