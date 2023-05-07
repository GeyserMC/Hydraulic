package org.geysermc.hydraulic.pack.bedrock.resource.animation_controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Float;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Animation Controller
 */
public class AnimationController {
  @JsonProperty("format_version")
  public String formatVersion;

  @JsonProperty("animation_controllers")
  public AnimationControllers animationControllers;

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
  public AnimationControllers getAnimationControllers() {
    return this.animationControllers;
  }

  /**
   * The animation controllers schema for.
   *
   * @param animationControllers Animation Controllers Schema
   */
  public void setAnimationControllers(AnimationControllers animationControllers) {
    this.animationControllers = animationControllers;
  }

  /**
   * Animation Controllers Schema
   * <p>
   * The animation controllers schema for.
   */
  public static class AnimationControllers {
    public States states;

    @JsonProperty("initial_state")
    public String initialState;

    /**
     * The states of this animation controller.
     *
     * @return States
     */
    public States getStates() {
      return this.states;
    }

    /**
     * The states of this animation controller.
     *
     * @param states States
     */
    public void setStates(States states) {
      this.states = states;
    }

    /**
     * The state to start with, if not specified state at position 0 in the array is used.
     *
     * @return Initial State
     */
    public String getInitialState() {
      return this.initialState;
    }

    /**
     * The state to start with, if not specified state at position 0 in the array is used.
     *
     * @param initialState Initial State
     */
    public void setInitialState(String initialState) {
      this.initialState = initialState;
    }

    /**
     * States
     * <p>
     * The states of this animation controller.
     */
    public static class States {
      @JsonProperty("blend_via_shortest_path")
      public boolean blendViaShortestPath;

      @JsonProperty("sound_effects")
      public List<SoundEffects> soundEffects = new ArrayList<>();

      public Variables variables;

      @JsonProperty("on_entry")
      public String[] onEntry;

      @JsonProperty("on_exit")
      public String[] onExit;

      /**
       * When blending a transition to another state, animate each euler axis through the shortest rotation, instead of by value.
       *
       * @return Blend Via Shortest Path
       */
      public boolean getBlendViaShortestPath() {
        return this.blendViaShortestPath;
      }

      /**
       * When blending a transition to another state, animate each euler axis through the shortest rotation, instead of by value.
       *
       * @param blendViaShortestPath Blend Via Shortest Path
       */
      public void setBlendViaShortestPath(boolean blendViaShortestPath) {
        this.blendViaShortestPath = blendViaShortestPath;
      }

      /**
       * Collection of sounds to trigger on entry to this animation state.
       */
      public List<SoundEffects> getSoundEffects() {
        return this.soundEffects;
      }

      /**
       * Collection of sounds to trigger on entry to this animation state.
       */
      public void setSoundEffects(List<SoundEffects> soundEffects) {
        this.soundEffects = soundEffects;
      }

      public Variables getVariables() {
        return this.variables;
      }

      public void setVariables(Variables variables) {
        this.variables = variables;
      }

      /**
       * Sets molang on data on entry.
       *
       * @return On Entry
       */
      public String[] getOnEntry() {
        return this.onEntry;
      }

      /**
       * Sets molang on data on entry.
       *
       * @param onEntry On Entry
       */
      public void setOnEntry(String[] onEntry) {
        this.onEntry = onEntry;
      }

      /**
       * Sets molang on data on exit.
       *
       * @return On Exit
       */
      public String[] getOnExit() {
        return this.onExit;
      }

      /**
       * Sets molang on data on exit.
       *
       * @param onExit On Exit
       */
      public void setOnExit(String[] onExit) {
        this.onExit = onExit;
      }

      public static class SoundEffects {
        public String effect;

        /**
         * Valid sound effect names should be listed in the entity's resource_definition json file.
         */
        public String getEffect() {
          return this.effect;
        }

        /**
         * Valid sound effect names should be listed in the entity's resource_definition json file.
         */
        public void setEffect(String effect) {
          this.effect = effect;
        }
      }

      public static class Variables {
        @JsonProperty("remap_curve")
        private Map<String, Float> remapCurve = new HashMap<>();

        /**
         * @return Remap Curve
         */
        public Map<String, Float> getRemapCurve() {
          return this.remapCurve;
        }

        /**
         * @param remapCurve Remap Curve
         */
        public void setRemapCurve(Map<String, Float> remapCurve) {
          this.remapCurve = remapCurve;
        }
      }
    }
  }
}
