package org.geysermc.hydraulic.pack.bedrock.resource.animations.actoranimation.animations.bones;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

public class Scale {
  @JsonProperty("lerp_mode")
  public String lerpMode;

  public String[] pre;

  public String[] post;

  /**
   * @return Lerp Mode
   */
  public String getLerpMode() {
    return this.lerpMode;
  }

  /**
   * @param lerpMode Lerp Mode
   */
  public void setLerpMode(String lerpMode) {
    this.lerpMode = lerpMode;
  }

  /**
   * An array of 3 items that describe the bones scale.
   *
   * @return Scale Array
   */
  public String[] getPre() {
    return this.pre;
  }

  /**
   * An array of 3 items that describe the bones scale.
   *
   * @param pre Scale Array
   */
  public void setPre(String[] pre) {
    this.pre = pre;
  }

  /**
   * An array of 3 items that describe the bones scale.
   *
   * @return Scale Array
   */
  public String[] getPost() {
    return this.post;
  }

  /**
   * An array of 3 items that describe the bones scale.
   *
   * @param post Scale Array
   */
  public void setPost(String[] post) {
    this.post = post;
  }
}
