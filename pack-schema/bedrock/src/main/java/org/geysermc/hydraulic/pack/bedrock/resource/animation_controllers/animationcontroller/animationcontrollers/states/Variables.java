package org.geysermc.hydraulic.pack.bedrock.resource.animation_controllers.animationcontroller.animationcontrollers.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Float;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

public class Variables {
  public String input;

  @JsonProperty("remap_curve")
  private Map<String, Float> remapCurve = new HashMap<>();

  public String getInput() {
    return this.input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  /**
   * @return Remap Curve
   */
  public Map<String, Float> getRemapCurve() {
    return this.remapCurve;
  }

  /**
   * @param remapCurve Remap Curve
   */
  public void setRemapCurve(Map<String, Float> remapCurve) {
    this.remapCurve = remapCurve;
  }
}
