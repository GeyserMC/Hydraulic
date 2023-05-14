package org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry.bones;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Boolean;

/**
 * ***EXPERIMENTAL*** A triangle or quad mesh object. Can be used in conjunction with cubes and texture geometry.
 */
public class PolyMesh {
  @JsonProperty("normalized_uvs")
  public Boolean normalizedUvs;

  public float[][] normals;

  public float[][][] polys;

  public float[][] positions;

  public float[][] uvs;

  /**
   * If true, UVs are assumed to be [0-1]. If false, UVs are assumed to be [0-texture_width] and [0-texture_height] respectively.
   */
  public Boolean getNormalizedUvs() {
    return this.normalizedUvs;
  }

  /**
   * If true, UVs are assumed to be [0-1]. If false, UVs are assumed to be [0-texture_width] and [0-texture_height] respectively.
   */
  public void setNormalizedUvs(Boolean normalizedUvs) {
    this.normalizedUvs = normalizedUvs;
  }

  /**
   * Vertex normals. Can be either indexed via the `polys` section, or be a quad-list if mapped 1-to-1 to the positions and UVs sections.
   */
  public float[][] getNormals() {
    return this.normals;
  }

  /**
   * Vertex normals. Can be either indexed via the `polys` section, or be a quad-list if mapped 1-to-1 to the positions and UVs sections.
   */
  public void setNormals(float[][] normals) {
    this.normals = normals;
  }

  /**
   * Poly element indices, as an array of polygons, each an array of either three or four vertices, each an array of indices into positions, normals, and UVs (in that order).
   */
  public float[][][] getPolys() {
    return this.polys;
  }

  /**
   * Poly element indices, as an array of polygons, each an array of either three or four vertices, each an array of indices into positions, normals, and UVs (in that order).
   */
  public void setPolys(float[][][] polys) {
    this.polys = polys;
  }

  public float[][] getPositions() {
    return this.positions;
  }

  public void setPositions(float[][] positions) {
    this.positions = positions;
  }

  /**
   * Vertex UVs. Can be either indexed via the `polys` section, or be a quad-list if mapped 1-to-1 to the positions and normals sections.
   */
  public float[][] getUvs() {
    return this.uvs;
  }

  /**
   * Vertex UVs. Can be either indexed via the `polys` section, or be a quad-list if mapped 1-to-1 to the positions and normals sections.
   */
  public void setUvs(float[][] uvs) {
    this.uvs = uvs;
  }
}
