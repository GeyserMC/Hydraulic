package org.geysermc.hydraulic.pack.bedrock.resource.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Object;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * Actor Entity 1.10.0
 * <p>
 * A client side entity definition.
 */
public class Entity {
  @JsonProperty("format_version")
  public String formatVersion;

  @JsonProperty("minecraft:client_entity")
  public ClientEntity clientEntity;

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
   * The entity description for clientside rendering, animations and models.
   *
   * @return Client Entity
   */
  public ClientEntity getClientEntity() {
    return this.clientEntity;
  }

  /**
   * The entity description for clientside rendering, animations and models.
   *
   * @param clientEntity Client Entity
   */
  public void setClientEntity(ClientEntity clientEntity) {
    this.clientEntity = clientEntity;
  }

  /**
   * Client Entity
   * <p>
   * The entity description for clientside rendering, animations and models.
   */
  public static class ClientEntity {
    public Description description;

    /**
     * The entity description for clientside rendering, animations and models.
     *
     * @return Description
     */
    public Description getDescription() {
      return this.description;
    }

    /**
     * The entity description for clientside rendering, animations and models.
     *
     * @param description Description
     */
    public void setDescription(Description description) {
      this.description = description;
    }

    /**
     * Description
     * <p>
     * The entity description for clientside rendering, animations and models.
     */
    public static class Description {
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

      /**
       * Scripts
       * <p>
       * The place where variables, and animations / controller to be run is specified.
       */
      public static class Scripts {
        private Map<String, Object> animate = new HashMap<>();

        @JsonProperty("parent_setup")
        public String parentSetup;

        public String scale;

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

      /**
       * Spawn Egg
       * <p>
       * The definition of how the spawn_egg icon looks like.
       */
      public static class SpawnEgg {
        @JsonProperty("base_color")
        public String baseColor;

        @JsonProperty("overlay_color")
        public String overlayColor;

        public String texture;

        @JsonProperty("texture_index")
        public int textureIndex;

        /**
         * The basic color of the egg.
         *
         * @return Base Color
         */
        public String getBaseColor() {
          return this.baseColor;
        }

        /**
         * The basic color of the egg.
         *
         * @param baseColor Base Color
         */
        public void setBaseColor(String baseColor) {
          this.baseColor = baseColor;
        }

        /**
         * The colors of the dots on the egg.
         *
         * @return Overlay Color
         */
        public String getOverlayColor() {
          return this.overlayColor;
        }

        /**
         * The colors of the dots on the egg.
         *
         * @param overlayColor Overlay Color
         */
        public void setOverlayColor(String overlayColor) {
          this.overlayColor = overlayColor;
        }

        /**
         * The texture reference in item_texture.json
         *
         * @return Texture
         */
        public String getTexture() {
          return this.texture;
        }

        /**
         * The texture reference in item_texture.json
         *
         * @param texture Texture
         */
        public void setTexture(String texture) {
          this.texture = texture;
        }

        /**
         * The index of the texture.
         *
         * @return Texture Index
         */
        public int getTextureIndex() {
          return this.textureIndex;
        }

        /**
         * The index of the texture.
         *
         * @param textureIndex Texture Index
         */
        public void setTextureIndex(int textureIndex) {
          this.textureIndex = textureIndex;
        }
      }
    }
  }
}
