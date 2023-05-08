package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.events.ParticleEffect;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.events.SoundEffect;

/**
 * Events
 */
public class Events {
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
}
