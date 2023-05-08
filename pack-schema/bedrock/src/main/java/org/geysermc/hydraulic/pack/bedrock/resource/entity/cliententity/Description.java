package org.geysermc.hydraulic.pack.bedrock.resource.entity.cliententity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import org.geysermc.hydraulic.pack.bedrock.resource.entity.cliententity.description.Scripts;
import org.geysermc.hydraulic.pack.bedrock.resource.entity.cliententity.description.SpawnEgg;

/**
 * Description
 * <p>
 * The entity description for clientside rendering, animations and models.
 */
public class Description {
  private Map<String, String> animations = new HashMap<>();

  @JsonProperty("enable_attachables")
  public boolean enableAttachables;

  private Map<String, String> geometry = new HashMap<>();

  @JsonProperty("queryable_geometry")
  public String queryableGeometry;

  @JsonProperty("hide_armor")
  public boolean hideArmor;

  @JsonProperty("held_item_ignores_lighting")
  public boolean heldItemIgnoresLighting;

  public String identifier;

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
  private Map<String, String> soundEffects = new HashMap<>();

  @JsonProperty("spawn_egg")
  public SpawnEgg spawnEgg;

  private Map<String, String> textures = new HashMap<>();

  /**
   * These names are used by the animation controller JSON. Players can reference animations from the vanilla Minecraft Resource Pack or create their own. Custom animations should be in the animation folder at the root of the Resource Pack.
   *
   * @return Animations
   */
  public Map<String, String> getAnimations() {
    return this.animations;
  }

  /**
   * These names are used by the animation controller JSON. Players can reference animations from the vanilla Minecraft Resource Pack or create their own. Custom animations should be in the animation folder at the root of the Resource Pack.
   *
   * @param animations Animations
   */
  public void setAnimations(Map<String, String> animations) {
    this.animations = animations;
  }

  /**
   * Whether or not attachables are enaboled.
   *
   * @return Enable Attachables
   */
  public boolean getEnableAttachables() {
    return this.enableAttachables;
  }

  /**
   * Whether or not attachables are enaboled.
   *
   * @param enableAttachables Enable Attachables
   */
  public void setEnableAttachables(boolean enableAttachables) {
    this.enableAttachables = enableAttachables;
  }

  /**
   * The reference to defined geometries in `<resource pack>/models/'.
   *
   * @return Geometry
   */
  public Map<String, String> getGeometry() {
    return this.geometry;
  }

  /**
   * The reference to defined geometries in `<resource pack>/models/'.
   *
   * @param geometry Geometry
   */
  public void setGeometry(Map<String, String> geometry) {
    this.geometry = geometry;
  }

  /**
   * @return Queryable Geometry
   */
  public String getQueryableGeometry() {
    return this.queryableGeometry;
  }

  /**
   * @param queryableGeometry Queryable Geometry
   */
  public void setQueryableGeometry(String queryableGeometry) {
    this.queryableGeometry = queryableGeometry;
  }

  /**
   * Hides or shows the possible armor.
   *
   * @return Hide Armor
   */
  public boolean getHideArmor() {
    return this.hideArmor;
  }

  /**
   * Hides or shows the possible armor.
   *
   * @param hideArmor Hide Armor
   */
  public void setHideArmor(boolean hideArmor) {
    this.hideArmor = hideArmor;
  }

  /**
   * This determines if the item held by an entity should render fully lit up (if true), or depending on surrounding lighting.
   *
   * @return Held Item Ignores Lighting
   */
  public boolean getHeldItemIgnoresLighting() {
    return this.heldItemIgnoresLighting;
  }

  /**
   * This determines if the item held by an entity should render fully lit up (if true), or depending on surrounding lighting.
   *
   * @param heldItemIgnoresLighting Held Item Ignores Lighting
   */
  public void setHeldItemIgnoresLighting(boolean heldItemIgnoresLighting) {
    this.heldItemIgnoresLighting = heldItemIgnoresLighting;
  }

  /**
   * The entity indentifier.
   *
   * @return Identifier
   */
  public String getIdentifier() {
    return this.identifier;
  }

  /**
   * The entity indentifier.
   *
   * @param identifier Identifier
   */
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  /**
   * A collection of material definitions.
   *
   * @return Materials
   */
  public Map<String, String> getMaterials() {
    return this.materials;
  }

  /**
   * A collection of material definitions.
   *
   * @param materials Materials
   */
  public void setMaterials(Map<String, String> materials) {
    this.materials = materials;
  }

  /**
   * The minimum engine version to be used.
   *
   * @return Minimum Engine Version
   */
  public String getMinEngineVersion() {
    return this.minEngineVersion;
  }

  /**
   * The minimum engine version to be used.
   *
   * @param minEngineVersion Minimum Engine Version
   */
  public void setMinEngineVersion(String minEngineVersion) {
    this.minEngineVersion = minEngineVersion;
  }

  /**
   * A collection of particle definitions.
   *
   * @return Particle Effects
   */
  public Map<String, String> getParticleEffects() {
    return this.particleEffects;
  }

  /**
   * A collection of particle definitions.
   *
   * @param particleEffects Particle Effects
   */
  public void setParticleEffects(Map<String, String> particleEffects) {
    this.particleEffects = particleEffects;
  }

  /**
   * A collection of particle emitters definitions.
   *
   * @return Particle Emitters
   */
  public Map<String, String> getParticleEmitters() {
    return this.particleEmitters;
  }

  /**
   * A collection of particle emitters definitions.
   *
   * @param particleEmitters Particle Emitters
   */
  public void setParticleEmitters(Map<String, String> particleEmitters) {
    this.particleEmitters = particleEmitters;
  }

  /**
   * A collection of Render controller definitions.
   *
   * @return Render Controllers
   */
  public String[] getRenderControllers() {
    return this.renderControllers;
  }

  /**
   * A collection of Render controller definitions.
   *
   * @param renderControllers Render Controllers
   */
  public void setRenderControllers(String[] renderControllers) {
    this.renderControllers = renderControllers;
  }

  /**
   * The place where variables, and animations / controller to be run is specified.
   *
   * @return Scripts
   */
  public Scripts getScripts() {
    return this.scripts;
  }

  /**
   * The place where variables, and animations / controller to be run is specified.
   *
   * @param scripts Scripts
   */
  public void setScripts(Scripts scripts) {
    this.scripts = scripts;
  }

  /**
   * A collection of sound effect definition.
   *
   * @return Sound Effects
   */
  public Map<String, String> getSoundEffects() {
    return this.soundEffects;
  }

  /**
   * A collection of sound effect definition.
   *
   * @param soundEffects Sound Effects
   */
  public void setSoundEffects(Map<String, String> soundEffects) {
    this.soundEffects = soundEffects;
  }

  /**
   * The definition of how the spawn_egg icon looks like.
   *
   * @return Spawn Egg
   */
  public SpawnEgg getSpawnEgg() {
    return this.spawnEgg;
  }

  /**
   * The definition of how the spawn_egg icon looks like.
   *
   * @param spawnEgg Spawn Egg
   */
  public void setSpawnEgg(SpawnEgg spawnEgg) {
    this.spawnEgg = spawnEgg;
  }

  /**
   * A collection of references to textures in the resourcepack.
   *
   * @return Textures
   */
  public Map<String, String> getTextures() {
    return this.textures;
  }

  /**
   * A collection of references to textures in the resourcepack.
   *
   * @param textures Textures
   */
  public void setTextures(Map<String, String> textures) {
    this.textures = textures;
  }
}
