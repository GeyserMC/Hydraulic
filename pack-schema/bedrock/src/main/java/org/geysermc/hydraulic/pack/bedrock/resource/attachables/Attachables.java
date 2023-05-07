package org.geysermc.hydraulic.pack.bedrock.resource.attachables;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * Actor Animation 1.10.0
 */
public class Attachables {
  @JsonProperty("format_version")
  public String formatVersion;

  @JsonProperty("minecraft:attachable")
  public Attachable attachable;

  /**
   * A version that tells minecraft what type of data format can be expected when reading this file.
   *
   * @return 1.10.0 Format Version
   */
  public String getFormatVersion() {
    return this.formatVersion;
  }

  /**
   * A version that tells minecraft what type of data format can be expected when reading this file.
   *
   * @param formatVersion 1.10.0 Format Version
   */
  public void setFormatVersion(String formatVersion) {
    this.formatVersion = formatVersion;
  }

  /**
   * The attachables definition.
   *
   * @return Attachables
   */
  public Attachable getAttachable() {
    return this.attachable;
  }

  /**
   * The attachables definition.
   *
   * @param attachable Attachables
   */
  public void setAttachable(Attachable attachable) {
    this.attachable = attachable;
  }

  /**
   * Attachables
   * <p>
   * The attachables definition.
   */
  public static class Attachable {
    public Description description;

    /**
     * @return Description
     */
    public Description getDescription() {
      return this.description;
    }

    /**
     * @param description Description
     */
    public void setDescription(Description description) {
      this.description = description;
    }

    /**
     * Description
     */
    public static class Description {
      private Map<String, String> animations = new HashMap<>();

      @JsonProperty("animation_controllers")
      public String[] animationControllers;

      @JsonProperty("enable_attachables")
      public boolean enableAttachables;

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
      public boolean getEnableAttachables() {
        return this.enableAttachables;
      }

      /**
       * @param enableAttachables Enable Attachables
       */
      public void setEnableAttachables(boolean enableAttachables) {
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

      /**
       * Scripts
       */
      public static class Scripts {
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

      /**
       * Spawn Egg
       */
      public static class SpawnEgg {
        @JsonProperty("base_colour")
        public String baseColour;

        @JsonProperty("overlay_color")
        public String overlayColor;

        public String texture;

        @JsonProperty("texture_index")
        public int textureIndex;

        /**
         * @return Base Colour
         */
        public String getBaseColour() {
          return this.baseColour;
        }

        /**
         * @param baseColour Base Colour
         */
        public void setBaseColour(String baseColour) {
          this.baseColour = baseColour;
        }

        /**
         * @return Overlay Color
         */
        public String getOverlayColor() {
          return this.overlayColor;
        }

        /**
         * @param overlayColor Overlay Color
         */
        public void setOverlayColor(String overlayColor) {
          this.overlayColor = overlayColor;
        }

        /**
         * @return Texture
         */
        public String getTexture() {
          return this.texture;
        }

        /**
         * @param texture Texture
         */
        public void setTexture(String texture) {
          this.texture = texture;
        }

        /**
         * @return Texture Index
         */
        public int getTextureIndex() {
          return this.textureIndex;
        }

        /**
         * @param textureIndex Texture Index
         */
        public void setTextureIndex(int textureIndex) {
          this.textureIndex = textureIndex;
        }
      }
    }
  }
}
