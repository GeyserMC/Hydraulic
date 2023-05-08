package org.geysermc.hydraulic.pack.bedrock.resource.materials;

import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * Material
 * <p>
 * A collection of material specifications for the render engine of minecraft.
 */
public class Materials {
  private Map<String, Materials> materials = new HashMap<>();

  /**
   * The collection of materials, each property key is the identification key of the material, and what it implements if : are used.
   *
   * @return Materials
   */
  public Map<String, Materials> getMaterials() {
    return this.materials;
  }

  /**
   * The collection of materials, each property key is the identification key of the material, and what it implements if : are used.
   *
   * @param materials Materials
   */
  public void setMaterials(Map<String, Materials> materials) {
    this.materials = materials;
  }
}
