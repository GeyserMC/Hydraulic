package org.geysermc.hydraulic.pack.bedrock.resource.animations.actoranimation.animations.bones;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

public class Position {
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
   * An array of 3 items that describe the bones position.
   *
   * @return Position Array
   */
  public String[] getPre() {
    return this.pre;
  }

  /**
   * An array of 3 items that describe the bones position.
   *
   * @param pre Position Array
   */
  public void setPre(String[] pre) {
    this.pre = pre;
  }

  /**
   * An array of 3 items that describe the bones position.
   *
   * @return Position Array
   */
  public String[] getPost() {
    return this.post;
  }

  /**
   * An array of 3 items that describe the bones position.
   *
   * @param post Position Array
   */
  public void setPost(String[] post) {
    this.post = post;
  }
}
