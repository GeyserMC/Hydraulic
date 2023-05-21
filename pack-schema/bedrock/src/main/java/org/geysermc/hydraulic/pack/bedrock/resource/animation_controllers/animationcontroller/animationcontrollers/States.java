package org.geysermc.hydraulic.pack.bedrock.resource.animation_controllers.animationcontroller.animationcontrollers;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Boolean;
import java.lang.Float;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geysermc.hydraulic.pack.bedrock.resource.animation_controllers.animationcontroller.animationcontrollers.states.ParticleEffects;
import org.geysermc.hydraulic.pack.bedrock.resource.animation_controllers.animationcontroller.animationcontrollers.states.SoundEffects;
import org.geysermc.hydraulic.pack.bedrock.resource.animation_controllers.animationcontroller.animationcontrollers.states.Variables;

/**
 * States
 * <p>
 * The states of this animation controller.
 */
public class States {
  @JsonProperty("blend_transition")
  public Float blendTransition;

  @JsonProperty("blend_via_shortest_path")
  public Boolean blendViaShortestPath;

  @JsonProperty("particle_effects")
  public List<ParticleEffects> particleEffects = new ArrayList<>();

  @JsonProperty("sound_effects")
  public List<SoundEffects> soundEffects = new ArrayList<>();

  private Map<String, Variables> variables = new HashMap<>();

  @JsonProperty("on_entry")
  public String[] onEntry;

  @JsonProperty("on_exit")
  public String[] onExit;

  /**
   * A short-hand version of blend_out that simply sets the amount of time to fade out if the animation is interrupted.
   */
  public Float getBlendTransition() {
    return this.blendTransition;
  }

  /**
   * A short-hand version of blend_out that simply sets the amount of time to fade out if the animation is interrupted.
   */
  public void setBlendTransition(float blendTransition) {
    this.blendTransition = blendTransition;
  }

  /**
   * When blending a transition to another state, animate each euler axis through the shortest rotation, instead of by value.
   *
   * @return Blend Via Shortest Path
   */
  public Boolean getBlendViaShortestPath() {
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
   * The effects to be emitted.
   *
   * @return Particle Effects
   */
  public List<ParticleEffects> getParticleEffects() {
    return this.particleEffects;
  }

  /**
   * The effects to be emitted.
   *
   * @param particleEffects Particle Effects
   */
  public void setParticleEffects(List<ParticleEffects> particleEffects) {
    this.particleEffects = particleEffects;
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

  public Map<String, Variables> getVariables() {
    return this.variables;
  }

  public void setVariables(Map<String, Variables> variables) {
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
}
