package org.geysermc.hydraulic.pack.bedrock.resource.attachables.attachable.description;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * Scripts
 */
public class Scripts {
  private Map<String, String> animate = new HashMap<>();

  @JsonProperty("parent_setup")
  public String parentSetup;

  public String scale;

  /**
   * @return Animate
   */
  public Map<String, String> getAnimate() {
    return this.animate;
  }

  /**
   * @param animate Animate
   */
  public void setAnimate(Map<String, String> animate) {
    this.animate = animate;
  }

  /**
   * @return Parent Setup
   */
  public String getParentSetup() {
    return this.parentSetup;
  }

  /**
   * @param parentSetup Parent Setup
   */
  public void setParentSetup(String parentSetup) {
    this.parentSetup = parentSetup;
  }

  /**
   * @return Scale
   */
  public String getScale() {
    return this.scale;
  }

  /**
   * @param scale Scale
   */
  public void setScale(String scale) {
    this.scale = scale;
  }
}
