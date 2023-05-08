package org.geysermc.hydraulic.pack.bedrock.resource.animations.actoranimation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Object;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import org.geysermc.hydraulic.pack.bedrock.resource.animations.actoranimation.animations.Bones;

/**
 * Animations Schema
 * <p>
 * The animation specification.
 */
public class Animations {
  @JsonProperty("anim_time_update")
  public String animTimeUpdate;

  @JsonProperty("animation_length")
  public float animationLength;

  @JsonProperty("blend_weight")
  public String blendWeight;

  private Map<String, Bones> bones = new HashMap<>();

  @JsonProperty("loop_delay")
  public String loopDelay;

  @JsonProperty("override_previous_animation")
  public boolean overridePreviousAnimation;

  @JsonProperty("particle_effects")
  private Map<String, Object> particleEffects = new HashMap<>();

  @JsonProperty("start_delay")
  public String startDelay;

  @JsonProperty("sound_effects")
  private Map<String, Object> soundEffects = new HashMap<>();

  private Map<String, Object> timeline = new HashMap<>();

  public String getAnimTimeUpdate() {
    return this.animTimeUpdate;
  }

  public void setAnimTimeUpdate(String animTimeUpdate) {
    this.animTimeUpdate = animTimeUpdate;
  }

  /**
   * Override calculated value (set as the last keyframe time) and set animation length in seconds.
   *
   * @return Animation Length
   */
  public float getAnimationLength() {
    return this.animationLength;
  }

  /**
   * Override calculated value (set as the last keyframe time) and set animation length in seconds.
   *
   * @param animationLength Animation Length
   */
  public void setAnimationLength(float animationLength) {
    this.animationLength = animationLength;
  }

  public String getBlendWeight() {
    return this.blendWeight;
  }

  public void setBlendWeight(String blendWeight) {
    this.blendWeight = blendWeight;
  }

  /**
   * Defines how the bones in an animation move or transform.
   *
   * @return Bones
   */
  public Map<String, Bones> getBones() {
    return this.bones;
  }

  /**
   * Defines how the bones in an animation move or transform.
   *
   * @param bones Bones
   */
  public void setBones(Map<String, Bones> bones) {
    this.bones = bones;
  }

  public String getLoopDelay() {
    return this.loopDelay;
  }

  public void setLoopDelay(String loopDelay) {
    this.loopDelay = loopDelay;
  }

  /**
   * Reset bones in this animation to the default pose before applying this animation.
   *
   * @return Override Previous Animation
   */
  public boolean getOverridePreviousAnimation() {
    return this.overridePreviousAnimation;
  }

  /**
   * Reset bones in this animation to the default pose before applying this animation.
   *
   * @param overridePreviousAnimation Override Previous Animation
   */
  public void setOverridePreviousAnimation(boolean overridePreviousAnimation) {
    this.overridePreviousAnimation = overridePreviousAnimation;
  }

  /**
   * @return Particle Effects
   */
  public Map<String, Object> getParticleEffects() {
    return this.particleEffects;
  }

  /**
   * @param particleEffects Particle Effects
   */
  public void setParticleEffects(Map<String, Object> particleEffects) {
    this.particleEffects = particleEffects;
  }

  public String getStartDelay() {
    return this.startDelay;
  }

  public void setStartDelay(String startDelay) {
    this.startDelay = startDelay;
  }

  /**
   * @return Sound Effect
   */
  public Map<String, Object> getSoundEffects() {
    return this.soundEffects;
  }

  /**
   * @param soundEffects Sound Effect
   */
  public void setSoundEffects(Map<String, Object> soundEffects) {
    this.soundEffects = soundEffects;
  }

  /**
   * The time line.
   *
   * @return Timeline
   */
  public Map<String, Object> getTimeline() {
    return this.timeline;
  }

  /**
   * The time line.
   *
   * @param timeline Timeline
   */
  public void setTimeline(Map<String, Object> timeline) {
    this.timeline = timeline;
  }
}
