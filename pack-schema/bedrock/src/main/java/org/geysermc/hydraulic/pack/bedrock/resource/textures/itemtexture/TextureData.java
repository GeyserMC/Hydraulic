package org.geysermc.hydraulic.pack.bedrock.resource.textures.itemtexture;

import org.geysermc.hydraulic.pack.bedrock.resource.textures.itemtexture.texturedata.Textures;

/**
 * Texture Data
 */
public class TextureData {
  public Textures textures;

  /**
   * A collection of texture files.
   *
   * @return Texture
   */
  public Textures getTextures() {
    return this.textures;
  }

  /**
   * A collection of texture files.
   *
   * @param textures Texture
   */
  public void setTextures(Textures textures) {
    this.textures = textures;
  }
}
