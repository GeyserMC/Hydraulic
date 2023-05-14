package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.particleappearancebillboard.uv;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Boolean;
import java.lang.String;

/**
 * Flipbook
 */
public class Flipbook {
  @JsonProperty("frames_per_second")
  public String framesPerSecond;

  @JsonProperty("max_frame")
  public String maxFrame;

  @JsonProperty("stretch_to_lifetime")
  public Boolean stretchToLifetime;

  public Boolean loop;

  public String getFramesPerSecond() {
    return this.framesPerSecond;
  }

  public void setFramesPerSecond(String framesPerSecond) {
    this.framesPerSecond = framesPerSecond;
  }

  public String getMaxFrame() {
    return this.maxFrame;
  }

  public void setMaxFrame(String maxFrame) {
    this.maxFrame = maxFrame;
  }

  /**
   * @return Stretch To Lifetime
   */
  public Boolean getStretchToLifetime() {
    return this.stretchToLifetime;
  }

  /**
   * @param stretchToLifetime Stretch To Lifetime
   */
  public void setStretchToLifetime(Boolean stretchToLifetime) {
    this.stretchToLifetime = stretchToLifetime;
  }

  /**
   * @return Loop
   */
  public Boolean getLoop() {
    return this.loop;
  }

  /**
   * @param loop Loop
   */
  public void setLoop(Boolean loop) {
    this.loop = loop;
  }
}
