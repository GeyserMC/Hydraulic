package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Emitter Initialization Component For 1.10.0
 * <p>
 * This component allows the emitter to run some Molang at creation, primarily to populate any Molang variables that get used later.
 */
public class EmitterInitialization {
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
