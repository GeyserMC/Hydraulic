package org.geysermc.hydraulic.pack.bedrock.resource.render_controllers.rendercontrollers;

import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * Arrays
 * <p>
 * A collection of definition of arrays.
 */
public class Arrays {
  private Map<String, String[]> geometries = new HashMap<>();

  private Map<String, String[]> materials = new HashMap<>();

  private Map<String, String[]> textures = new HashMap<>();

  /**
   * A collection of Geometry array.
   *
   * @return Geometries
   */
  public Map<String, String[]> getGeometries() {
    return this.geometries;
  }

  /**
   * A collection of Geometry array.
   *
   * @param geometries Geometries
   */
  public void setGeometries(Map<String, String[]> geometries) {
    this.geometries = geometries;
  }

  /**
   * A collection of materials array.
   *
   * @return Materials
   */
  public Map<String, String[]> getMaterials() {
    return this.materials;
  }

  /**
   * A collection of materials array.
   *
   * @param materials Materials
   */
  public void setMaterials(Map<String, String[]> materials) {
    this.materials = materials;
  }

  /**
   * A collection of texture array.
   *
   * @return Textures
   */
  public Map<String, String[]> getTextures() {
    return this.textures;
  }

  /**
   * A collection of texture array.
   *
   * @param textures Textures
   */
  public void setTextures(Map<String, String[]> textures) {
    this.textures = textures;
  }
}
