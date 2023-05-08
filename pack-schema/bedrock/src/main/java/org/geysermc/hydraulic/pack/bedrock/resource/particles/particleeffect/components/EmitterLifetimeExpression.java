package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Emitter Rate Manual Component 1.10.0
 */
public class EmitterLifetimeExpression {
  @JsonProperty("activation_expression")
  public String activationExpression;

  @JsonProperty("expiration_expression")
  public String expirationExpression;

  public String getActivationExpression() {
    return this.activationExpression;
  }

  public void setActivationExpression(String activationExpression) {
    this.activationExpression = activationExpression;
  }

  public String getExpirationExpression() {
    return this.expirationExpression;
  }

  public void setExpirationExpression(String expirationExpression) {
    this.expirationExpression = expirationExpression;
  }
}
