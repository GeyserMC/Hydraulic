package org.geysermc.hydraulic.pack.bedrock.resource.animations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Object;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * Actor Animation
 * <p>
 * The RP animation that changes an actors models, or molang data.
 */
public class ActorAnimation {
  @JsonProperty("format_version")
  public String formatVersion;

  public Animations animations;

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
  public Animations getAnimations() {
    return this.animations;
  }

  /**
   * The animation specification.
   *
   * @param animations Animations Schema
   */
  public void setAnimations(Animations animations) {
    this.animations = animations;
  }

  /**
   * Animations Schema
   * <p>
   * The animation specification.
   */
  public static class Animations {
    @JsonProperty("animation_length")
    public float animationLength;

    public Bones bones;

    @JsonProperty("override_previous_animation")
    public boolean overridePreviousAnimation;

    @JsonProperty("particle_effects")
    private Map<String, Object> particleEffects = new HashMap<>();

    @JsonProperty("sound_effects")
    private Map<String, Object> soundEffects = new HashMap<>();

    private Map<String, Object> timeline = new HashMap<>();

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

    /**
     * Defines how the bones in an animation move or transform.
     *
     * @return Bones
     */
    public Bones getBones() {
      return this.bones;
    }

    /**
     * Defines how the bones in an animation move or transform.
     *
     * @param bones Bones
     */
    public void setBones(Bones bones) {
      this.bones = bones;
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

    /**
     * Bones
     * <p>
     * Defines how the bones in an animation move or transform.
     */
    public static class Bones {
      @JsonProperty("relative_to")
      public RelativeTo relativeTo;

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

      /**
       * Relative To
       * <p>
       * If set, makes the bone rotation relative to the entity instead of the bone's parent.
       */
      public static class RelativeTo {
        public String rotation;

        /**
         * If set, makes the bone rotation relative to the entity instead of the bone's parent.
         *
         * @return Rotation
         */
        public String getRotation() {
          return this.rotation;
        }

        /**
         * If set, makes the bone rotation relative to the entity instead of the bone's parent.
         *
         * @param rotation Rotation
         */
        public void setRotation(String rotation) {
          this.rotation = rotation;
        }
      }
    }
  }
}
