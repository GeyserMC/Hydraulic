package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Particle Initialization Component For 1.10.0
 */
public class ParticleInitialization {
  @JsonProperty("per_update_expression")
  public String perUpdateExpression;

  @JsonProperty("per_render_expression")
  public String perRenderExpression;

  public String getPerUpdateExpression() {
    return this.perUpdateExpression;
  }

  public void setPerUpdateExpression(String perUpdateExpression) {
    this.perUpdateExpression = perUpdateExpression;
  }

  public String getPerRenderExpression() {
    return this.perRenderExpression;
  }

  public void setPerRenderExpression(String perRenderExpression) {
    this.perRenderExpression = perRenderExpression;
  }
}
