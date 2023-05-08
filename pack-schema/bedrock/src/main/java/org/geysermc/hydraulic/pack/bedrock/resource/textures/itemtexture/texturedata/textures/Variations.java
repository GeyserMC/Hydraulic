package org.geysermc.hydraulic.pack.bedrock.resource.textures.itemtexture.texturedata.textures;

import java.lang.String;

/**
 * Variantion
 */
public class Variations {
  public String path;

  public int weight;

  /**
   * A texture file.
   *
   * @return Path
   */
  public String getPath() {
    return this.path;
  }

  /**
   * A texture file.
   *
   * @param path Path
   */
  public void setPath(String path) {
    this.path = path;
  }

  /**
   * @return Weight
   */
  public int getWeight() {
    return this.weight;
  }

  /**
   * @param weight Weight
   */
  public void setWeight(int weight) {
    this.weight = weight;
  }
}
