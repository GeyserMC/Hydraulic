package org.geysermc.hydraulic.pack.bedrock.resource.particles;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Object;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Particle
 * <p>
 * A particle definition file.
 */
public class Particles {
  @JsonProperty("format_version")
  public String formatVersion;

  @JsonProperty("particle_effect")
  public ParticleEffect particleEffect;

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
   * @return Particle Effect
   */
  public ParticleEffect getParticleEffect() {
    return this.particleEffect;
  }

  /**
   * @param particleEffect Particle Effect
   */
  public void setParticleEffect(ParticleEffect particleEffect) {
    this.particleEffect = particleEffect;
  }

  /**
   * Particle Effect
   */
  public static class ParticleEffect {
    public Description description;

    public Curves curves;

    public Components components;

    public Events events;

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
     * Curves are interpolation values, with inputs from 0 to 1, and outputs based on the curve. The result of the curve is a Molang variable of the same name that can be referenced in Molang in components. For each rendering frame for each particle, the curves are evaluated and the result is placed in a Molang variable of the name of the curve.
     *
     * @return Curves
     */
    public Curves getCurves() {
      return this.curves;
    }

    /**
     * Curves are interpolation values, with inputs from 0 to 1, and outputs based on the curve. The result of the curve is a Molang variable of the same name that can be referenced in Molang in components. For each rendering frame for each particle, the curves are evaluated and the result is placed in a Molang variable of the name of the curve.
     *
     * @param curves Curves
     */
    public void setCurves(Curves curves) {
      this.curves = curves;
    }

    /**
     * The particle components.
     *
     * @return Components
     */
    public Components getComponents() {
      return this.components;
    }

    /**
     * The particle components.
     *
     * @param components Components
     */
    public void setComponents(Components components) {
      this.components = components;
    }

    /**
     * @return Events
     */
    public Events getEvents() {
      return this.events;
    }

    /**
     * @param events Events
     */
    public void setEvents(Events events) {
      this.events = events;
    }

    /**
     * Description
     */
    public static class Description {
      public String identifier;

      @JsonProperty("basic_render_parameters")
      public BasicRenderParameters basicRenderParameters;

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
       * @return Basic Render Parameters
       */
      public BasicRenderParameters getBasicRenderParameters() {
        return this.basicRenderParameters;
      }

      /**
       * @param basicRenderParameters Basic Render Parameters
       */
      public void setBasicRenderParameters(BasicRenderParameters basicRenderParameters) {
        this.basicRenderParameters = basicRenderParameters;
      }

      /**
       * Basic Render Parameters
       */
      public static class BasicRenderParameters {
        public String material;

        public String texture;

        /**
         *  Minecraft material to use for emitter.
         *
         * @return Material
         */
        public String getMaterial() {
          return this.material;
        }

        /**
         *  Minecraft material to use for emitter.
         *
         * @param material Material
         */
        public void setMaterial(String material) {
          this.material = material;
        }

        /**
         * Minecraft texture to use for emitter.
         *
         * @return Texture
         */
        public String getTexture() {
          return this.texture;
        }

        /**
         * Minecraft texture to use for emitter.
         *
         * @param texture Texture
         */
        public void setTexture(String texture) {
          this.texture = texture;
        }
      }
    }

    /**
     * Curves
     * <p>
     * Curves are interpolation values, with inputs from 0 to 1, and outputs based on the curve. The result of the curve is a Molang variable of the same name that can be referenced in Molang in components. For each rendering frame for each particle, the curves are evaluated and the result is placed in a Molang variable of the name of the curve.
     */
    public static class Curves {
      public String type;

      /**
       * The type of curve.
       *
       * @return Type
       */
      public String getType() {
        return this.type;
      }

      /**
       * The type of curve.
       *
       * @param type Type
       */
      public void setType(String type) {
        this.type = type;
      }
    }

    /**
     * Components
     * <p>
     * The particle components.
     */
    public static class Components {
      @JsonProperty("minecraft:emitter_initialization")
      public EmitterInitialization emitterInitialization;

      @JsonProperty("minecraft:emitter_lifetime_events")
      public EmitterLifetimeEvents emitterLifetimeEvents;

      @JsonProperty("minecraft:emitter_lifetime_expression")
      public EmitterLifetimeExpression emitterLifetimeExpression;

      @JsonProperty("minecraft:emitter_lifetime_once")
      public EmitterLifetimeOnce emitterLifetimeOnce;

      @JsonProperty("minecraft:emitter_lifetime_looping")
      public EmitterLifetimeLooping emitterLifetimeLooping;

      @JsonProperty("minecraft:emitter_local_space")
      public EmitterLocalSpace emitterLocalSpace;

      @JsonProperty("minecraft:emitter_rate_instant")
      public EmitterRateInstant emitterRateInstant;

      @JsonProperty("minecraft:emitter_rate_manual")
      public EmitterRateManual emitterRateManual;

      @JsonProperty("minecraft:emitter_rate_steady")
      public EmitterRateSteady emitterRateSteady;

      @JsonProperty("minecraft:emitter_shape_box")
      public EmitterShapeBox emitterShapeBox;

      @JsonProperty("minecraft:emitter_shape_custom")
      public EmitterShapeCustom emitterShapeCustom;

      @JsonProperty("minecraft:emitter_shape_disc")
      public EmitterShapeDisc emitterShapeDisc;

      @JsonProperty("minecraft:emitter_shape_entity_aabb")
      public EmitterShapeEntityAabb emitterShapeEntityAabb;

      @JsonProperty("minecraft:emitter_shape_point")
      public EmitterShapePoint emitterShapePoint;

      @JsonProperty("minecraft:emitter_shape_sphere")
      public EmitterShapeSphere emitterShapeSphere;

      @JsonProperty("minecraft:particle_appearance_billboard")
      public ParticleAppearanceBillboard particleAppearanceBillboard;

      @JsonProperty("minecraft:particle_appearance_tinting")
      public ParticleAppearanceTinting particleAppearanceTinting;

      @JsonProperty("minecraft:particle_appearance_lighting")
      public ParticleAppearanceLighting particleAppearanceLighting;

      @JsonProperty("minecraft:particle_initialization")
      public ParticleInitialization particleInitialization;

      @JsonProperty("minecraft:particle_initial_spin")
      public ParticleInitialSpin particleInitialSpin;

      @JsonProperty("minecraft:particle_lifetime_expression")
      public ParticleLifetimeExpression particleLifetimeExpression;

      @JsonProperty("minecraft:particle_lifetime_events")
      public ParticleLifetimeEvents particleLifetimeEvents;

      @JsonProperty("minecraft:particle_motion_collision")
      public ParticleMotionCollision particleMotionCollision;

      @JsonProperty("minecraft:particle_motion_dynamic")
      public ParticleMotionDynamic particleMotionDynamic;

      @JsonProperty("minecraft:particle_motion_parametric")
      public ParticleMotionParametric particleMotionParametric;

      /**
       * This component allows the emitter to run some Molang at creation, primarily to populate any Molang variables that get used later.
       *
       * @return Emitter Initialization Component For 1.10.0
       */
      public EmitterInitialization getEmitterInitialization() {
        return this.emitterInitialization;
      }

      /**
       * This component allows the emitter to run some Molang at creation, primarily to populate any Molang variables that get used later.
       *
       * @param emitterInitialization Emitter Initialization Component For 1.10.0
       */
      public void setEmitterInitialization(EmitterInitialization emitterInitialization) {
        this.emitterInitialization = emitterInitialization;
      }

      /**
       * @return Emitter Lifetime Events Component For 1.10.0
       */
      public EmitterLifetimeEvents getEmitterLifetimeEvents() {
        return this.emitterLifetimeEvents;
      }

      /**
       * @param emitterLifetimeEvents Emitter Lifetime Events Component For 1.10.0
       */
      public void setEmitterLifetimeEvents(EmitterLifetimeEvents emitterLifetimeEvents) {
        this.emitterLifetimeEvents = emitterLifetimeEvents;
      }

      /**
       * @return Emitter Rate Manual Component 1.10.0
       */
      public EmitterLifetimeExpression getEmitterLifetimeExpression() {
        return this.emitterLifetimeExpression;
      }

      /**
       * @param emitterLifetimeExpression Emitter Rate Manual Component 1.10.0
       */
      public void setEmitterLifetimeExpression(
          EmitterLifetimeExpression emitterLifetimeExpression) {
        this.emitterLifetimeExpression = emitterLifetimeExpression;
      }

      /**
       * @return Emitter Lifetime Once Component For 1.10.0
       */
      public EmitterLifetimeOnce getEmitterLifetimeOnce() {
        return this.emitterLifetimeOnce;
      }

      /**
       * @param emitterLifetimeOnce Emitter Lifetime Once Component For 1.10.0
       */
      public void setEmitterLifetimeOnce(EmitterLifetimeOnce emitterLifetimeOnce) {
        this.emitterLifetimeOnce = emitterLifetimeOnce;
      }

      /**
       * @return Emitter Lifetime Looping Component For 1.10.0
       */
      public EmitterLifetimeLooping getEmitterLifetimeLooping() {
        return this.emitterLifetimeLooping;
      }

      /**
       * @param emitterLifetimeLooping Emitter Lifetime Looping Component For 1.10.0
       */
      public void setEmitterLifetimeLooping(EmitterLifetimeLooping emitterLifetimeLooping) {
        this.emitterLifetimeLooping = emitterLifetimeLooping;
      }

      /**
       * @return Emitter Local Space Component For 1.10.0
       */
      public EmitterLocalSpace getEmitterLocalSpace() {
        return this.emitterLocalSpace;
      }

      /**
       * @param emitterLocalSpace Emitter Local Space Component For 1.10.0
       */
      public void setEmitterLocalSpace(EmitterLocalSpace emitterLocalSpace) {
        this.emitterLocalSpace = emitterLocalSpace;
      }

      /**
       * @return Emitter Rate Instant Component For 1.10.0
       */
      public EmitterRateInstant getEmitterRateInstant() {
        return this.emitterRateInstant;
      }

      /**
       * @param emitterRateInstant Emitter Rate Instant Component For 1.10.0
       */
      public void setEmitterRateInstant(EmitterRateInstant emitterRateInstant) {
        this.emitterRateInstant = emitterRateInstant;
      }

      /**
       * @return Emitter Rate Manual Component For 1.10.0
       */
      public EmitterRateManual getEmitterRateManual() {
        return this.emitterRateManual;
      }

      /**
       * @param emitterRateManual Emitter Rate Manual Component For 1.10.0
       */
      public void setEmitterRateManual(EmitterRateManual emitterRateManual) {
        this.emitterRateManual = emitterRateManual;
      }

      /**
       * @return Emitter Rate Steady Component For 1.10.0
       */
      public EmitterRateSteady getEmitterRateSteady() {
        return this.emitterRateSteady;
      }

      /**
       * @param emitterRateSteady Emitter Rate Steady Component For 1.10.0
       */
      public void setEmitterRateSteady(EmitterRateSteady emitterRateSteady) {
        this.emitterRateSteady = emitterRateSteady;
      }

      /**
       * @return Emitter Shape Box Component For 1.10.0
       */
      public EmitterShapeBox getEmitterShapeBox() {
        return this.emitterShapeBox;
      }

      /**
       * @param emitterShapeBox Emitter Shape Box Component For 1.10.0
       */
      public void setEmitterShapeBox(EmitterShapeBox emitterShapeBox) {
        this.emitterShapeBox = emitterShapeBox;
      }

      /**
       * @return Emitter Shape Custom Component For 1.10.0
       */
      public EmitterShapeCustom getEmitterShapeCustom() {
        return this.emitterShapeCustom;
      }

      /**
       * @param emitterShapeCustom Emitter Shape Custom Component For 1.10.0
       */
      public void setEmitterShapeCustom(EmitterShapeCustom emitterShapeCustom) {
        this.emitterShapeCustom = emitterShapeCustom;
      }

      /**
       * @return Emitter Shape Disc Component For 1.10.0
       */
      public EmitterShapeDisc getEmitterShapeDisc() {
        return this.emitterShapeDisc;
      }

      /**
       * @param emitterShapeDisc Emitter Shape Disc Component For 1.10.0
       */
      public void setEmitterShapeDisc(EmitterShapeDisc emitterShapeDisc) {
        this.emitterShapeDisc = emitterShapeDisc;
      }

      /**
       * @return Emitter Shape Entity Aabb Component For 1.10.0
       */
      public EmitterShapeEntityAabb getEmitterShapeEntityAabb() {
        return this.emitterShapeEntityAabb;
      }

      /**
       * @param emitterShapeEntityAabb Emitter Shape Entity Aabb Component For 1.10.0
       */
      public void setEmitterShapeEntityAabb(EmitterShapeEntityAabb emitterShapeEntityAabb) {
        this.emitterShapeEntityAabb = emitterShapeEntityAabb;
      }

      /**
       * @return Emitter Shape Point Component For 1.10.0
       */
      public EmitterShapePoint getEmitterShapePoint() {
        return this.emitterShapePoint;
      }

      /**
       * @param emitterShapePoint Emitter Shape Point Component For 1.10.0
       */
      public void setEmitterShapePoint(EmitterShapePoint emitterShapePoint) {
        this.emitterShapePoint = emitterShapePoint;
      }

      /**
       * @return Emitter Shape Sphere Component For 1.10.0
       */
      public EmitterShapeSphere getEmitterShapeSphere() {
        return this.emitterShapeSphere;
      }

      /**
       * @param emitterShapeSphere Emitter Shape Sphere Component For 1.10.0
       */
      public void setEmitterShapeSphere(EmitterShapeSphere emitterShapeSphere) {
        this.emitterShapeSphere = emitterShapeSphere;
      }

      /**
       * @return Particle Appearance Billboard Component For 1.10.0
       */
      public ParticleAppearanceBillboard getParticleAppearanceBillboard() {
        return this.particleAppearanceBillboard;
      }

      /**
       * @param particleAppearanceBillboard Particle Appearance Billboard Component For 1.10.0
       */
      public void setParticleAppearanceBillboard(
          ParticleAppearanceBillboard particleAppearanceBillboard) {
        this.particleAppearanceBillboard = particleAppearanceBillboard;
      }

      /**
       * Color fields are special, they can be either an RGB, or a `#RRGGBB` field (or RGBA or `AARRGGBB`).  If RGB(A), the channels are from 0 to 1.  If the string `#AARRGGBB`, then the values are hex from 00 to ff.
       *
       * @return Particle Appearance Tinting Component For 1.10.0
       */
      public ParticleAppearanceTinting getParticleAppearanceTinting() {
        return this.particleAppearanceTinting;
      }

      /**
       * Color fields are special, they can be either an RGB, or a `#RRGGBB` field (or RGBA or `AARRGGBB`).  If RGB(A), the channels are from 0 to 1.  If the string `#AARRGGBB`, then the values are hex from 00 to ff.
       *
       * @param particleAppearanceTinting Particle Appearance Tinting Component For 1.10.0
       */
      public void setParticleAppearanceTinting(
          ParticleAppearanceTinting particleAppearanceTinting) {
        this.particleAppearanceTinting = particleAppearanceTinting;
      }

      /**
       * @return Particle Appearance Lighting Component For 1.10.0
       */
      public ParticleAppearanceLighting getParticleAppearanceLighting() {
        return this.particleAppearanceLighting;
      }

      /**
       * @param particleAppearanceLighting Particle Appearance Lighting Component For 1.10.0
       */
      public void setParticleAppearanceLighting(
          ParticleAppearanceLighting particleAppearanceLighting) {
        this.particleAppearanceLighting = particleAppearanceLighting;
      }

      /**
       * @return Particle Initialization Component For 1.10.0
       */
      public ParticleInitialization getParticleInitialization() {
        return this.particleInitialization;
      }

      /**
       * @param particleInitialization Particle Initialization Component For 1.10.0
       */
      public void setParticleInitialization(ParticleInitialization particleInitialization) {
        this.particleInitialization = particleInitialization;
      }

      /**
       * Starts the particle with a specified orientation and rotation rate.
       *
       * @return Particle Initial Spin Component For 1.10.0
       */
      public ParticleInitialSpin getParticleInitialSpin() {
        return this.particleInitialSpin;
      }

      /**
       * Starts the particle with a specified orientation and rotation rate.
       *
       * @param particleInitialSpin Particle Initial Spin Component For 1.10.0
       */
      public void setParticleInitialSpin(ParticleInitialSpin particleInitialSpin) {
        this.particleInitialSpin = particleInitialSpin;
      }

      /**
       * @return Particle Lifetime Expression Component For 1.10.0
       */
      public ParticleLifetimeExpression getParticleLifetimeExpression() {
        return this.particleLifetimeExpression;
      }

      /**
       * @param particleLifetimeExpression Particle Lifetime Expression Component For 1.10.0
       */
      public void setParticleLifetimeExpression(
          ParticleLifetimeExpression particleLifetimeExpression) {
        this.particleLifetimeExpression = particleLifetimeExpression;
      }

      /**
       * @return Particle Lifetime Events Component For 1.10.0
       */
      public ParticleLifetimeEvents getParticleLifetimeEvents() {
        return this.particleLifetimeEvents;
      }

      /**
       * @param particleLifetimeEvents Particle Lifetime Events Component For 1.10.0
       */
      public void setParticleLifetimeEvents(ParticleLifetimeEvents particleLifetimeEvents) {
        this.particleLifetimeEvents = particleLifetimeEvents;
      }

      /**
       * @return Particle Motion Collision Component For 1.10.0
       */
      public ParticleMotionCollision getParticleMotionCollision() {
        return this.particleMotionCollision;
      }

      /**
       * @param particleMotionCollision Particle Motion Collision Component For 1.10.0
       */
      public void setParticleMotionCollision(ParticleMotionCollision particleMotionCollision) {
        this.particleMotionCollision = particleMotionCollision;
      }

      /**
       * This component specifies the dynamic properties of the particle, from a simulation standpoint what forces act upon the particle? These dynamics alter the velocity of the particle, which is a combination of the direction of the particle and the speed. Particle direction will always be in the direction of the velocity of the particle.
       *
       * @return Particle Motion Dynamic Component For 1.10.0
       */
      public ParticleMotionDynamic getParticleMotionDynamic() {
        return this.particleMotionDynamic;
      }

      /**
       * This component specifies the dynamic properties of the particle, from a simulation standpoint what forces act upon the particle? These dynamics alter the velocity of the particle, which is a combination of the direction of the particle and the speed. Particle direction will always be in the direction of the velocity of the particle.
       *
       * @param particleMotionDynamic Particle Motion Dynamic Component For 1.10.0
       */
      public void setParticleMotionDynamic(ParticleMotionDynamic particleMotionDynamic) {
        this.particleMotionDynamic = particleMotionDynamic;
      }

      /**
       * @return Particle Motion Parametric Component For 1.10.0
       */
      public ParticleMotionParametric getParticleMotionParametric() {
        return this.particleMotionParametric;
      }

      /**
       * @param particleMotionParametric Particle Motion Parametric Component For 1.10.0
       */
      public void setParticleMotionParametric(ParticleMotionParametric particleMotionParametric) {
        this.particleMotionParametric = particleMotionParametric;
      }

      /**
       * Emitter Initialization Component For 1.10.0
       * <p>
       * This component allows the emitter to run some Molang at creation, primarily to populate any Molang variables that get used later.
       */
      public static class EmitterInitialization {
        @JsonProperty("creation_expression")
        public String creationExpression;

        @JsonProperty("per_update_expression")
        public String perUpdateExpression;

        /**
         * Molang definition.
         *
         * @return Molang
         */
        public String getCreationExpression() {
          return this.creationExpression;
        }

        /**
         * Molang definition.
         *
         * @param creationExpression Molang
         */
        public void setCreationExpression(String creationExpression) {
          this.creationExpression = creationExpression;
        }

        /**
         * Molang definition.
         *
         * @return Molang
         */
        public String getPerUpdateExpression() {
          return this.perUpdateExpression;
        }

        /**
         * Molang definition.
         *
         * @param perUpdateExpression Molang
         */
        public void setPerUpdateExpression(String perUpdateExpression) {
          this.perUpdateExpression = perUpdateExpression;
        }
      }

      /**
       * Emitter Lifetime Events Component For 1.10.0
       */
      public static class EmitterLifetimeEvents {
        private Map<String, Object> timeline = new HashMap<>();

        @JsonProperty("looping_travel_distance_events")
        public List<LoopingTravelDistanceEvents> loopingTravelDistanceEvents = new ArrayList<>();

        /**
         * A series of times, e.g. 0.0 or 1.0, that trigger the event, these get fired on every loop the emitter goes through, `time` is the time, e.g. one line might be: `0.4`: `event`
         *
         * @return Timeline
         */
        public Map<String, Object> getTimeline() {
          return this.timeline;
        }

        /**
         * A series of times, e.g. 0.0 or 1.0, that trigger the event, these get fired on every loop the emitter goes through, `time` is the time, e.g. one line might be: `0.4`: `event`
         *
         * @param timeline Timeline
         */
        public void setTimeline(Map<String, Object> timeline) {
          this.timeline = timeline;
        }

        /**
         * A series of events that occur at set intervals these get fired every time the emitter has moved the specified input distance from the last time it was fired.
         *
         * @return Looping Travel Distance Events
         */
        public List<LoopingTravelDistanceEvents> getLoopingTravelDistanceEvents() {
          return this.loopingTravelDistanceEvents;
        }

        /**
         * A series of events that occur at set intervals these get fired every time the emitter has moved the specified input distance from the last time it was fired.
         *
         * @param loopingTravelDistanceEvents Looping Travel Distance Events
         */
        public void setLoopingTravelDistanceEvents(
            List<LoopingTravelDistanceEvents> loopingTravelDistanceEvents) {
          this.loopingTravelDistanceEvents = loopingTravelDistanceEvents;
        }

        /**
         * Distance Event
         */
        public static class LoopingTravelDistanceEvents {
          public float distance;

          /**
           * @return Distance
           */
          public float getDistance() {
            return this.distance;
          }

          /**
           * @param distance Distance
           */
          public void setDistance(float distance) {
            this.distance = distance;
          }
        }
      }

      /**
       * Emitter Rate Manual Component 1.10.0
       */
      public static class EmitterLifetimeExpression {
      }

      /**
       * Emitter Lifetime Once Component For 1.10.0
       */
      public static class EmitterLifetimeOnce {
      }

      /**
       * Emitter Lifetime Looping Component For 1.10.0
       */
      public static class EmitterLifetimeLooping {
      }

      /**
       * Emitter Local Space Component For 1.10.0
       */
      public static class EmitterLocalSpace {
        public boolean position;

        public boolean rotation;

        public boolean velocity;

        /**
         * @return Position
         */
        public boolean getPosition() {
          return this.position;
        }

        /**
         * @param position Position
         */
        public void setPosition(boolean position) {
          this.position = position;
        }

        /**
         * @return Rotation
         */
        public boolean getRotation() {
          return this.rotation;
        }

        /**
         * @param rotation Rotation
         */
        public void setRotation(boolean rotation) {
          this.rotation = rotation;
        }

        /**
         * @return Rotation
         */
        public boolean getVelocity() {
          return this.velocity;
        }

        /**
         * @param velocity Rotation
         */
        public void setVelocity(boolean velocity) {
          this.velocity = velocity;
        }
      }

      /**
       * Emitter Rate Instant Component For 1.10.0
       */
      public static class EmitterRateInstant {
      }

      /**
       * Emitter Rate Manual Component For 1.10.0
       */
      public static class EmitterRateManual {
      }

      /**
       * Emitter Rate Steady Component For 1.10.0
       */
      public static class EmitterRateSteady {
      }

      /**
       * Emitter Shape Box Component For 1.10.0
       */
      public static class EmitterShapeBox {
        @JsonProperty("surface_only")
        public boolean surfaceOnly;

        /**
         * @return Surface Only
         */
        public boolean getSurfaceOnly() {
          return this.surfaceOnly;
        }

        /**
         * @param surfaceOnly Surface Only
         */
        public void setSurfaceOnly(boolean surfaceOnly) {
          this.surfaceOnly = surfaceOnly;
        }
      }

      /**
       * Emitter Shape Custom Component For 1.10.0
       */
      public static class EmitterShapeCustom {
      }

      /**
       * Emitter Shape Disc Component For 1.10.0
       */
      public static class EmitterShapeDisc {
        @JsonProperty("surface_only")
        public boolean surfaceOnly;

        /**
         * @return Surface Only
         */
        public boolean getSurfaceOnly() {
          return this.surfaceOnly;
        }

        /**
         * @param surfaceOnly Surface Only
         */
        public void setSurfaceOnly(boolean surfaceOnly) {
          this.surfaceOnly = surfaceOnly;
        }
      }

      /**
       * Emitter Shape Entity Aabb Component For 1.10.0
       */
      public static class EmitterShapeEntityAabb {
      }

      /**
       * Emitter Shape Point Component For 1.10.0
       */
      public static class EmitterShapePoint {
      }

      /**
       * Emitter Shape Sphere Component For 1.10.0
       */
      public static class EmitterShapeSphere {
        @JsonProperty("surface_only")
        public boolean surfaceOnly;

        /**
         * @return Surface Only
         */
        public boolean getSurfaceOnly() {
          return this.surfaceOnly;
        }

        /**
         * @param surfaceOnly Surface Only
         */
        public void setSurfaceOnly(boolean surfaceOnly) {
          this.surfaceOnly = surfaceOnly;
        }
      }

      /**
       * Particle Appearance Billboard Component For 1.10.0
       */
      public static class ParticleAppearanceBillboard {
        @JsonProperty("facing_camera_mode")
        public String facingCameraMode;

        public Direction direction;

        public Uv uv;

        /**
         * Used to orient the billboard.
         *
         * @return Facing Camera Mode
         */
        public String getFacingCameraMode() {
          return this.facingCameraMode;
        }

        /**
         * Used to orient the billboard.
         *
         * @param facingCameraMode Facing Camera Mode
         */
        public void setFacingCameraMode(String facingCameraMode) {
          this.facingCameraMode = facingCameraMode;
        }

        public Direction getDirection() {
          return this.direction;
        }

        public void setDirection(Direction direction) {
          this.direction = direction;
        }

        /**
         * @return Uv
         */
        public Uv getUv() {
          return this.uv;
        }

        /**
         * @param uv Uv
         */
        public void setUv(Uv uv) {
          this.uv = uv;
        }

        public static class Direction {
          public String mode;

          @JsonProperty("min_speed_threshold")
          public float minSpeedThreshold;

          /**
           * Specified how to calculate the billboard direction of a particle.
           */
          public String getMode() {
            return this.mode;
          }

          /**
           * Specified how to calculate the billboard direction of a particle.
           */
          public void setMode(String mode) {
            this.mode = mode;
          }

          /**
           * The direction is set if the speed of the particle is above the threshold.
           */
          public float getMinSpeedThreshold() {
            return this.minSpeedThreshold;
          }

          /**
           * The direction is set if the speed of the particle is above the threshold.
           */
          public void setMinSpeedThreshold(float minSpeedThreshold) {
            this.minSpeedThreshold = minSpeedThreshold;
          }
        }

        /**
         * Uv
         */
        public static class Uv {
          @JsonProperty("texture_width")
          public int textureWidth;

          @JsonProperty("texture_height")
          public int textureHeight;

          public Flipbook flipbook;

          /**
           * @return Texture Width
           */
          public int getTextureWidth() {
            return this.textureWidth;
          }

          /**
           * @param textureWidth Texture Width
           */
          public void setTextureWidth(int textureWidth) {
            this.textureWidth = textureWidth;
          }

          /**
           * @return Texture Height
           */
          public int getTextureHeight() {
            return this.textureHeight;
          }

          /**
           * @param textureHeight Texture Height
           */
          public void setTextureHeight(int textureHeight) {
            this.textureHeight = textureHeight;
          }

          /**
           * @return Flipbook
           */
          public Flipbook getFlipbook() {
            return this.flipbook;
          }

          /**
           * @param flipbook Flipbook
           */
          public void setFlipbook(Flipbook flipbook) {
            this.flipbook = flipbook;
          }

          /**
           * Flipbook
           */
          public static class Flipbook {
            @JsonProperty("stretch_to_lifetime")
            public boolean stretchToLifetime;

            public boolean loop;

            /**
             * @return Stretch To Lifetime
             */
            public boolean getStretchToLifetime() {
              return this.stretchToLifetime;
            }

            /**
             * @param stretchToLifetime Stretch To Lifetime
             */
            public void setStretchToLifetime(boolean stretchToLifetime) {
              this.stretchToLifetime = stretchToLifetime;
            }

            /**
             * @return Loop
             */
            public boolean getLoop() {
              return this.loop;
            }

            /**
             * @param loop Loop
             */
            public void setLoop(boolean loop) {
              this.loop = loop;
            }
          }
        }
      }

      /**
       * Particle Appearance Tinting Component For 1.10.0
       * <p>
       * Color fields are special, they can be either an RGB, or a `#RRGGBB` field (or RGBA or `AARRGGBB`).  If RGB(A), the channels are from 0 to 1.  If the string `#AARRGGBB`, then the values are hex from 00 to ff.
       */
      public static class ParticleAppearanceTinting {
      }

      /**
       * Particle Appearance Lighting Component For 1.10.0
       */
      public static class ParticleAppearanceLighting {
      }

      /**
       * Particle Initialization Component For 1.10.0
       */
      public static class ParticleInitialization {
      }

      /**
       * Particle Initial Spin Component For 1.10.0
       * <p>
       * Starts the particle with a specified orientation and rotation rate.
       */
      public static class ParticleInitialSpin {
      }

      /**
       * Particle Lifetime Expression Component For 1.10.0
       */
      public static class ParticleLifetimeExpression {
      }

      /**
       * Particle Lifetime Events Component For 1.10.0
       */
      public static class ParticleLifetimeEvents {
      }

      /**
       * Particle Motion Collision Component For 1.10.0
       */
      public static class ParticleMotionCollision {
        @JsonProperty("collision_drag")
        public float collisionDrag;

        @JsonProperty("coefficient_of_restitution")
        public float coefficientOfRestitution;

        @JsonProperty("collision_radius")
        public float collisionRadius;

        @JsonProperty("expire_on_contact")
        public boolean expireOnContact;

        /**
         * @return Collision Drag
         */
        public float getCollisionDrag() {
          return this.collisionDrag;
        }

        /**
         * @param collisionDrag Collision Drag
         */
        public void setCollisionDrag(float collisionDrag) {
          this.collisionDrag = collisionDrag;
        }

        /**
         * @return Coefficient Of Restitution
         */
        public float getCoefficientOfRestitution() {
          return this.coefficientOfRestitution;
        }

        /**
         * @param coefficientOfRestitution Coefficient Of Restitution
         */
        public void setCoefficientOfRestitution(float coefficientOfRestitution) {
          this.coefficientOfRestitution = coefficientOfRestitution;
        }

        /**
         * @return Collision Radius
         */
        public float getCollisionRadius() {
          return this.collisionRadius;
        }

        /**
         * @param collisionRadius Collision Radius
         */
        public void setCollisionRadius(float collisionRadius) {
          this.collisionRadius = collisionRadius;
        }

        /**
         * @return Expire On Contact
         */
        public boolean getExpireOnContact() {
          return this.expireOnContact;
        }

        /**
         * @param expireOnContact Expire On Contact
         */
        public void setExpireOnContact(boolean expireOnContact) {
          this.expireOnContact = expireOnContact;
        }
      }

      /**
       * Particle Motion Dynamic Component For 1.10.0
       * <p>
       * This component specifies the dynamic properties of the particle, from a simulation standpoint what forces act upon the particle? These dynamics alter the velocity of the particle, which is a combination of the direction of the particle and the speed. Particle direction will always be in the direction of the velocity of the particle.
       */
      public static class ParticleMotionDynamic {
      }

      /**
       * Particle Motion Parametric Component For 1.10.0
       */
      public static class ParticleMotionParametric {
      }
    }

    /**
     * Events
     */
    public static class Events {
      @JsonProperty("particle_effect")
      public ParticleEffect particleEffect;

      @JsonProperty("sound_effect")
      public SoundEffect soundEffect;

      /**
       * @return Particle Effect
       */
      public ParticleEffect getParticleEffect() {
        return this.particleEffect;
      }

      /**
       * @param particleEffect Particle Effect
       */
      public void setParticleEffect(ParticleEffect particleEffect) {
        this.particleEffect = particleEffect;
      }

      /**
       * @return Sound Effect
       */
      public SoundEffect getSoundEffect() {
        return this.soundEffect;
      }

      /**
       * @param soundEffect Sound Effect
       */
      public void setSoundEffect(SoundEffect soundEffect) {
        this.soundEffect = soundEffect;
      }

      /**
       * Particle Effect
       */
      public static class ParticleEffect {
        public String effect;

        public String type;

        /**
         * @return Effect
         */
        public String getEffect() {
          return this.effect;
        }

        /**
         * @param effect Effect
         */
        public void setEffect(String effect) {
          this.effect = effect;
        }

        /**
         * @return Type
         */
        public String getType() {
          return this.type;
        }

        /**
         * @param type Type
         */
        public void setType(String type) {
          this.type = type;
        }
      }

      /**
       * Sound Effect
       */
      public static class SoundEffect {
        @JsonProperty("event_name")
        public String eventName;

        /**
         * @return Event Name
         */
        public String getEventName() {
          return this.eventName;
        }

        /**
         * @param eventName Event Name
         */
        public void setEventName(String eventName) {
          this.eventName = eventName;
        }
      }
    }
  }
}
