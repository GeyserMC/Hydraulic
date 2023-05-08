package org.geysermc.hydraulic.pack.bedrock.resource.animations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import org.geysermc.hydraulic.pack.bedrock.resource.animations.actoranimation.Animations;

/**
 * Actor Animation
 * <p>
 * The RP animation that changes an actors models, or molang data.
 */
public class ActorAnimation {
  @JsonProperty("format_version")
  public String formatVersion;

  private Map<String, Animations> animations = new HashMap<>();

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
   * The animation specification.
   *
   * @return Animations Schema
   */
  public Map<String, Animations> getAnimations() {
    return this.animations;
  }

  /**
   * The animation specification.
   *
   * @param animations Animations Schema
   */
  public void setAnimations(Map<String, Animations> animations) {
    this.animations = animations;
  }
}
