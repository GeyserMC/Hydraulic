package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.description.BasicRenderParameters;

/**
 * Description
 */
public class Description {
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
}
