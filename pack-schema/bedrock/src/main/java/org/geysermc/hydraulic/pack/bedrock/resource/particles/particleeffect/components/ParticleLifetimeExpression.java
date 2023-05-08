package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Particle Lifetime Expression Component For 1.10.0
 */
public class ParticleLifetimeExpression {
  @JsonProperty("expiration_expression")
  public String expirationExpression;

  @JsonProperty("max_lifetime")
  public String maxLifetime;

  public String getExpirationExpression() {
    return this.expirationExpression;
  }

  public void setExpirationExpression(String expirationExpression) {
    this.expirationExpression = expirationExpression;
  }

  public String getMaxLifetime() {
    return this.maxLifetime;
  }

  public void setMaxLifetime(String maxLifetime) {
    this.maxLifetime = maxLifetime;
  }
}
