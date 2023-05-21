package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.EmitterInitialization;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.EmitterLifetimeEvents;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.EmitterLifetimeExpression;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.EmitterLifetimeLooping;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.EmitterLifetimeOnce;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.EmitterLocalSpace;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.EmitterRateInstant;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.EmitterRateManual;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.EmitterRateSteady;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.EmitterShapeBox;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.EmitterShapeCustom;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.EmitterShapeDisc;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.EmitterShapeEntityAabb;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.EmitterShapePoint;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.EmitterShapeSphere;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.ParticleAppearanceBillboard;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.ParticleAppearanceLighting;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.ParticleAppearanceTinting;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.ParticleInitialSpin;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.ParticleInitialization;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.ParticleLifetimeEvents;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.ParticleLifetimeExpression;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.ParticleMotionCollision;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.ParticleMotionDynamic;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.ParticleMotionParametric;

/**
 * Components
 * <p>
 * The particle components.
 */
public class Components {
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

  @JsonProperty("minecraft:particle_expire_if_not_in_blocks")
  public String[] particleExpireIfNotInBlocks;

  @JsonProperty("minecraft:particle_expire_if_in_blocks")
  public String[] particleExpireIfInBlocks;

  @JsonProperty("minecraft:particle_initialization")
  public ParticleInitialization particleInitialization;

  @JsonProperty("minecraft:particle_initial_speed")
  public String particleInitialSpeed;

  @JsonProperty("minecraft:particle_initial_spin")
  public ParticleInitialSpin particleInitialSpin;

  @JsonProperty("minecraft:particle_lifetime_expression")
  public ParticleLifetimeExpression particleLifetimeExpression;

  @JsonProperty("minecraft:particle_lifetime_events")
  public ParticleLifetimeEvents particleLifetimeEvents;

  @JsonProperty("minecraft:particle_kill_plane")
  public String[] particleKillPlane;

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
  public void setEmitterLifetimeExpression(EmitterLifetimeExpression emitterLifetimeExpression) {
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
  public void setParticleAppearanceTinting(ParticleAppearanceTinting particleAppearanceTinting) {
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
  public void setParticleAppearanceLighting(ParticleAppearanceLighting particleAppearanceLighting) {
    this.particleAppearanceLighting = particleAppearanceLighting;
  }

  /**
   * @return Particle Expire If Not In Blocks Component For 1.10.0
   */
  public String[] getParticleExpireIfNotInBlocks() {
    return this.particleExpireIfNotInBlocks;
  }

  /**
   * @param particleExpireIfNotInBlocks Particle Expire If Not In Blocks Component For 1.10.0
   */
  public void setParticleExpireIfNotInBlocks(String[] particleExpireIfNotInBlocks) {
    this.particleExpireIfNotInBlocks = particleExpireIfNotInBlocks;
  }

  /**
   * @return Particle Expire If Not In Blocks Component For 1.10.0
   */
  public String[] getParticleExpireIfInBlocks() {
    return this.particleExpireIfInBlocks;
  }

  /**
   * @param particleExpireIfInBlocks Particle Expire If Not In Blocks Component For 1.10.0
   */
  public void setParticleExpireIfInBlocks(String[] particleExpireIfInBlocks) {
    this.particleExpireIfInBlocks = particleExpireIfInBlocks;
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

  public String getParticleInitialSpeed() {
    return this.particleInitialSpeed;
  }

  public void setParticleInitialSpeed(String particleInitialSpeed) {
    this.particleInitialSpeed = particleInitialSpeed;
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
  public void setParticleLifetimeExpression(ParticleLifetimeExpression particleLifetimeExpression) {
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
   * A*x + B*y + C*z + D = 0
   * with the parameters being [ A, B, C, D ].
   *
   * @return Particle Kill Plane Component For 1.10.0
   */
  public String[] getParticleKillPlane() {
    return this.particleKillPlane;
  }

  /**
   * A*x + B*y + C*z + D = 0
   * with the parameters being [ A, B, C, D ].
   *
   * @param particleKillPlane Particle Kill Plane Component For 1.10.0
   */
  public void setParticleKillPlane(String[] particleKillPlane) {
    this.particleKillPlane = particleKillPlane;
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
}
