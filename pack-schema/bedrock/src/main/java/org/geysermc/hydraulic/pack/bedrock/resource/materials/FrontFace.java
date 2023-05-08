package org.geysermc.hydraulic.pack.bedrock.resource.materials;

import java.lang.String;

/**
 * Face
 */
public class FrontFace {
  public String stencilDepthFailOp;

  public String stencilFailOp;

  public String stencilFunc;

  public String stencilPass;

  public String stencilPassOp;

  /**
   * @return Stencil Depth Fail Operation
   */
  public String getStencilDepthFailOp() {
    return this.stencilDepthFailOp;
  }

  /**
   * @param stencilDepthFailOp Stencil Depth Fail Operation
   */
  public void setStencilDepthFailOp(String stencilDepthFailOp) {
    this.stencilDepthFailOp = stencilDepthFailOp;
  }

  /**
   * @return Stencil Fail Operation
   */
  public String getStencilFailOp() {
    return this.stencilFailOp;
  }

  /**
   * @param stencilFailOp Stencil Fail Operation
   */
  public void setStencilFailOp(String stencilFailOp) {
    this.stencilFailOp = stencilFailOp;
  }

  /**
   * @return Stencil Function
   */
  public String getStencilFunc() {
    return this.stencilFunc;
  }

  /**
   * @param stencilFunc Stencil Function
   */
  public void setStencilFunc(String stencilFunc) {
    this.stencilFunc = stencilFunc;
  }

  /**
   * @return Stencil Pass
   */
  public String getStencilPass() {
    return this.stencilPass;
  }

  /**
   * @param stencilPass Stencil Pass
   */
  public void setStencilPass(String stencilPass) {
    this.stencilPass = stencilPass;
  }

  /**
   * @return Stencil Depth Fail Operation
   */
  public String getStencilPassOp() {
    return this.stencilPassOp;
  }

  /**
   * @param stencilPassOp Stencil Depth Fail Operation
   */
  public void setStencilPassOp(String stencilPassOp) {
    this.stencilPassOp = stencilPassOp;
  }
}
