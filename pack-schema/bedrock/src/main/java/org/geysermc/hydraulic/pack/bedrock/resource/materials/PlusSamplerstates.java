package org.geysermc.hydraulic.pack.bedrock.resource.materials;

import java.lang.String;

/**
 * Sample State
 */
public class PlusSamplerstates {
  public int samplerIndex;

  public String textureFilter;

  public String textureWrap;

  /**
   * @return Sample State
   */
  public int getSamplerIndex() {
    return this.samplerIndex;
  }

  /**
   * @param samplerIndex Sample State
   */
  public void setSamplerIndex(int samplerIndex) {
    this.samplerIndex = samplerIndex;
  }

  /**
   * @return Texture Filter
   */
  public String getTextureFilter() {
    return this.textureFilter;
  }

  /**
   * @param textureFilter Texture Filter
   */
  public void setTextureFilter(String textureFilter) {
    this.textureFilter = textureFilter;
  }

  /**
   * @return Texture Wrap
   */
  public String getTextureWrap() {
    return this.textureWrap;
  }

  /**
   * @param textureWrap Texture Wrap
   */
  public void setTextureWrap(String textureWrap) {
    this.textureWrap = textureWrap;
  }
}
