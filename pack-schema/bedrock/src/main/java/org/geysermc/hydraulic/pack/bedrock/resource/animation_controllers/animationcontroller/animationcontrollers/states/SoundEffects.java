package org.geysermc.hydraulic.pack.bedrock.resource.animation_controllers.animationcontroller.animationcontrollers.states;

import java.lang.String;

public class SoundEffects {
  public String effect;

  /**
   * Valid sound effect names should be listed in the entity's resource_definition json file.
   */
  public String getEffect() {
    return this.effect;
  }

  /**
   * Valid sound effect names should be listed in the entity's resource_definition json file.
   */
  public void setEffect(String effect) {
    this.effect = effect;
  }
}
