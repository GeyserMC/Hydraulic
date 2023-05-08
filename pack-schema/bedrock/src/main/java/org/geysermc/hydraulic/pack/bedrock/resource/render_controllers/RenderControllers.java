package org.geysermc.hydraulic.pack.bedrock.resource.render_controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * Render Controllers
 * <p>
 * A collection of render controllers to apply.
 */
public class RenderControllers {
  @JsonProperty("format_version")
  public String formatVersion;

  @JsonProperty("render_controllers")
  private Map<String, org.geysermc.hydraulic.pack.bedrock.resource.render_controllers.rendercontrollers.RenderControllers> renderControllers = new HashMap<>();

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
   * The collection of render controllers, each property is the identifier of a render controller.
   *
   * @return Render Controllers
   */
  public Map<String, org.geysermc.hydraulic.pack.bedrock.resource.render_controllers.rendercontrollers.RenderControllers> getRenderControllers(
      ) {
    return this.renderControllers;
  }

  /**
   * The collection of render controllers, each property is the identifier of a render controller.
   *
   * @param renderControllers Render Controllers
   */
  public void setRenderControllers(
      Map<String, org.geysermc.hydraulic.pack.bedrock.resource.render_controllers.rendercontrollers.RenderControllers> renderControllers) {
    this.renderControllers = renderControllers;
  }
}
