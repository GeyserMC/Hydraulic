package org.geysermc.hydraulic.pack.bedrock.resource.manifest;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Boolean;

/**
 * Capabilities
 * <p>
 * These are the different features that the pack makes use of that aren't necessarily enabled by default.
 */
public class Capabilities {
  @JsonProperty("experimental_custom_ui")
  public Boolean experimentalCustomUi;

  public Boolean chemistry;

  public Boolean raytraced;

  /**
   * Allows HTML files in the pack to be used for custom UI, and scripts in the pack to call and manipulate custom UI.
   *
   * @return Experimental Custom Ui
   */
  public Boolean getExperimentalCustomUi() {
    return this.experimentalCustomUi;
  }

  /**
   * Allows HTML files in the pack to be used for custom UI, and scripts in the pack to call and manipulate custom UI.
   *
   * @param experimentalCustomUi Experimental Custom Ui
   */
  public void setExperimentalCustomUi(Boolean experimentalCustomUi) {
    this.experimentalCustomUi = experimentalCustomUi;
  }

  /**
   * Allows the pack to add, change or replace Chemistry functionality.
   *
   * @return Chemistry
   */
  public Boolean getChemistry() {
    return this.chemistry;
  }

  /**
   * Allows the pack to add, change or replace Chemistry functionality.
   *
   * @param chemistry Chemistry
   */
  public void setChemistry(Boolean chemistry) {
    this.chemistry = chemistry;
  }

  /**
   * Indicates that this pack contains Raytracing Enhanced or Physical Based Materials for rendering.
   *
   * @return Raytraced
   */
  public Boolean getRaytraced() {
    return this.raytraced;
  }

  /**
   * Indicates that this pack contains Raytracing Enhanced or Physical Based Materials for rendering.
   *
   * @param raytraced Raytraced
   */
  public void setRaytraced(Boolean raytraced) {
    this.raytraced = raytraced;
  }
}
