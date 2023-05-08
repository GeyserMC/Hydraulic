package org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry.bones;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ***EXPERIMENTAL*** A triangle or quad mesh object. Can be used in conjunction with cubes and texture geometry.
 */
public class PolyMesh {
  @JsonProperty("normalized_uvs")
  public boolean normalizedUvs;

  /**
   * If true, UVs are assumed to be [0-1]. If false, UVs are assumed to be [0-texture_width] and [0-texture_height] respectively.
   */
  public boolean getNormalizedUvs() {
    return this.normalizedUvs;
  }

  /**
   * If true, UVs are assumed to be [0-1]. If false, UVs are assumed to be [0-texture_width] and [0-texture_height] respectively.
   */
  public void setNormalizedUvs(boolean normalizedUvs) {
    this.normalizedUvs = normalizedUvs;
  }
}
