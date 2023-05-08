package org.geysermc.hydraulic.pack.bedrock.resource.entity.cliententity.description;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Object;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * Scripts
 * <p>
 * The place where variables, and animations / controller to be run is specified.
 */
public class Scripts {
  private Map<String, Object> animate = new HashMap<>();

  @JsonProperty("parent_setup")
  public String parentSetup;

  public String scale;

  public String scalex;

  public String scaley;

  public String scalez;

  @JsonProperty("should_update_bones_and_effects_offscreen")
  public String shouldUpdateBonesAndEffectsOffscreen;

  @JsonProperty("should_update_effects_offscreen")
  public String shouldUpdateEffectsOffscreen;

  private Map<String, String> variables = new HashMap<>();

  /**
   * The array of items to animate.
   *
   * @return Animate
   */
  public Map<String, Object> getAnimate() {
    return this.animate;
  }

  /**
   * The array of items to animate.
   *
   * @param animate Animate
   */
  public void setAnimate(Map<String, Object> animate) {
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
   * Scale sets the scale of the mob's geometry.
   *
   * @return Scale
   */
  public String getScale() {
    return this.scale;
  }

  /**
   * Scale sets the scale of the mob's geometry.
   *
   * @param scale Scale
   */
  public void setScale(String scale) {
    this.scale = scale;
  }

  public String getScalex() {
    return this.scalex;
  }

  public void setScalex(String scalex) {
    this.scalex = scalex;
  }

  public String getScaley() {
    return this.scaley;
  }

  public void setScaley(String scaley) {
    this.scaley = scaley;
  }

  public String getScalez() {
    return this.scalez;
  }

  public void setScalez(String scalez) {
    this.scalez = scalez;
  }

  public String getShouldUpdateBonesAndEffectsOffscreen() {
    return this.shouldUpdateBonesAndEffectsOffscreen;
  }

  public void setShouldUpdateBonesAndEffectsOffscreen(String shouldUpdateBonesAndEffectsOffscreen) {
    this.shouldUpdateBonesAndEffectsOffscreen = shouldUpdateBonesAndEffectsOffscreen;
  }

  public String getShouldUpdateEffectsOffscreen() {
    return this.shouldUpdateEffectsOffscreen;
  }

  public void setShouldUpdateEffectsOffscreen(String shouldUpdateEffectsOffscreen) {
    this.shouldUpdateEffectsOffscreen = shouldUpdateEffectsOffscreen;
  }

  /**
   *  A list of variables that need certain settings applied to them. Currently, for the client, only `public` is supported.
   *
   * @return Variables
   */
  public Map<String, String> getVariables() {
    return this.variables;
  }

  /**
   *  A list of variables that need certain settings applied to them. Currently, for the client, only `public` is supported.
   *
   * @param variables Variables
   */
  public void setVariables(Map<String, String> variables) {
    this.variables = variables;
  }
}
