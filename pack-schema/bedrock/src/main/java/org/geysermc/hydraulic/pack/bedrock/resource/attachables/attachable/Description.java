package org.geysermc.hydraulic.pack.bedrock.resource.attachables.attachable;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Boolean;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import org.geysermc.hydraulic.pack.bedrock.resource.attachables.attachable.description.Scripts;
import org.geysermc.hydraulic.pack.bedrock.resource.attachables.attachable.description.SpawnEgg;

/**
 * Description
 */
public class Description {
  private Map<String, String> animations = new HashMap<>();

  @JsonProperty("animation_controllers")
  public String[] animationControllers;

  @JsonProperty("enable_attachables")
  public Boolean enableAttachables;

  private Map<String, String> geometry = new HashMap<>();

  public String identifier;

  private Map<String, String> item = new HashMap<>();

  private Map<String, String> materials = new HashMap<>();

  @JsonProperty("min_engine_version")
  public String minEngineVersion;

  @JsonProperty("particle_effects")
  private Map<String, String> particleEffects = new HashMap<>();

  @JsonProperty("particle_emitters")
  private Map<String, String> particleEmitters = new HashMap<>();

  @JsonProperty("render_controllers")
  public String[] renderControllers;

  public Scripts scripts;

  @JsonProperty("sound_effects")
  public String[] soundEffects;

  @JsonProperty("spawn_egg")
  public SpawnEgg spawnEgg;

  private Map<String, String> textures = new HashMap<>();

  /**
   * The collection of animations references.
   *
   * @return Animations
   */
  public Map<String, String> getAnimations() {
    return this.animations;
  }

  /**
   * The collection of animations references.
   *
   * @param animations Animations
   */
  public void setAnimations(Map<String, String> animations) {
    this.animations = animations;
  }

  /**
   * The specification of animation controllers.
   *
   * @return Animation Controllers
   */
  public String[] getAnimationControllers() {
    return this.animationControllers;
  }

  /**
   * The specification of animation controllers.
   *
   * @param animationControllers Animation Controllers
   */
  public void setAnimationControllers(String[] animationControllers) {
    this.animationControllers = animationControllers;
  }

  /**
   * @return Enable Attachables
   */
  public Boolean getEnableAttachables() {
    return this.enableAttachables;
  }

  /**
   * @param enableAttachables Enable Attachables
   */
  public void setEnableAttachables(Boolean enableAttachables) {
    this.enableAttachables = enableAttachables;
  }

  /**
   * The geometry specification.
   *
   * @return Geometry
   */
  public Map<String, String> getGeometry() {
    return this.geometry;
  }

  /**
   * The geometry specification.
   *
   * @param geometry Geometry
   */
  public void setGeometry(Map<String, String> geometry) {
    this.geometry = geometry;
  }

  /**
   * @return Identifier
   */
  public String getIdentifier() {
    return this.identifier;
  }

  /**
   * @param identifier Identifier
   */
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  /**
   * @return Item
   */
  public Map<String, String> getItem() {
    return this.item;
  }

  /**
   * @param item Item
   */
  public void setItem(Map<String, String> item) {
    this.item = item;
  }

  /**
   * A collection of material references.
   *
   * @return Materials
   */
  public Map<String, String> getMaterials() {
    return this.materials;
  }

  /**
   * A collection of material references.
   *
   * @param materials Materials
   */
  public void setMaterials(Map<String, String> materials) {
    this.materials = materials;
  }

  /**
   * The minimum engine needed to use this.
   *
   * @return Minimum Engine Version
   */
  public String getMinEngineVersion() {
    return this.minEngineVersion;
  }

  /**
   * The minimum engine needed to use this.
   *
   * @param minEngineVersion Minimum Engine Version
   */
  public void setMinEngineVersion(String minEngineVersion) {
    this.minEngineVersion = minEngineVersion;
  }

  /**
   * A collection of particle effect references.
   *
   * @return Particle Effects
   */
  public Map<String, String> getParticleEffects() {
    return this.particleEffects;
  }

  /**
   * A collection of particle effect references.
   *
   * @param particleEffects Particle Effects
   */
  public void setParticleEffects(Map<String, String> particleEffects) {
    this.particleEffects = particleEffects;
  }

  /**
   * @return Particle Emitters
   */
  public Map<String, String> getParticleEmitters() {
    return this.particleEmitters;
  }

  /**
   * @param particleEmitters Particle Emitters
   */
  public void setParticleEmitters(Map<String, String> particleEmitters) {
    this.particleEmitters = particleEmitters;
  }

  /**
   * @return Render Controllers
   */
  public String[] getRenderControllers() {
    return this.renderControllers;
  }

  /**
   * @param renderControllers Render Controllers
   */
  public void setRenderControllers(String[] renderControllers) {
    this.renderControllers = renderControllers;
  }

  /**
   * @return Scripts
   */
  public Scripts getScripts() {
    return this.scripts;
  }

  /**
   * @param scripts Scripts
   */
  public void setScripts(Scripts scripts) {
    this.scripts = scripts;
  }

  /**
   * @return Sound Effects
   */
  public String[] getSoundEffects() {
    return this.soundEffects;
  }

  /**
   * @param soundEffects Sound Effects
   */
  public void setSoundEffects(String[] soundEffects) {
    this.soundEffects = soundEffects;
  }

  /**
   * @return Spawn Egg
   */
  public SpawnEgg getSpawnEgg() {
    return this.spawnEgg;
  }

  /**
   * @param spawnEgg Spawn Egg
   */
  public void setSpawnEgg(SpawnEgg spawnEgg) {
    this.spawnEgg = spawnEgg;
  }

  /**
   * @return Textures
   */
  public Map<String, String> getTextures() {
    return this.textures;
  }

  /**
   * @param textures Textures
   */
  public void setTextures(Map<String, String> textures) {
    this.textures = textures;
  }
}
