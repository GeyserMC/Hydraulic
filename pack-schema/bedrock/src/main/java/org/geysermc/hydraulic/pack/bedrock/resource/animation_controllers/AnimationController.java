package org.geysermc.hydraulic.pack.bedrock.resource.animation_controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import org.geysermc.hydraulic.pack.bedrock.resource.animation_controllers.animationcontroller.AnimationControllers;

/**
 * Animation Controller
 */
public class AnimationController {
  @JsonProperty("format_version")
  public String formatVersion;

  @JsonProperty("animation_controllers")
  private Map<String, AnimationControllers> animationControllers = new HashMap<>();

  /**
   * A version that tells minecraft what type of data format can be expected when reading this file.
   *
   * @return Format Version
   */
  public String getFormatVersion() {
    return this.formatVersion;
  }

  /**
   * A version that tells minecraft what type of data format can be expected when reading this file.
   *
   * @param formatVersion Format Version
   */
  public void setFormatVersion(String formatVersion) {
    this.formatVersion = formatVersion;
  }

  /**
   * The animation controllers schema for.
   *
   * @return Animation Controllers Schema
   */
  public Map<String, AnimationControllers> getAnimationControllers() {
    return this.animationControllers;
  }

  /**
   * The animation controllers schema for.
   *
   * @param animationControllers Animation Controllers Schema
   */
  public void setAnimationControllers(Map<String, AnimationControllers> animationControllers) {
    this.animationControllers = animationControllers;
  }
}
