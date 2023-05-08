package org.geysermc.hydraulic.pack.bedrock.resource;

import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.Biomes;

/**
 * Biomes Client
 * <p>
 * The minecraft biomes definition file.
 */
public class BiomesClient {
  public Biomes biomes;

  /**
   * A collection of predefined biomes.
   *
   * @return Biomes
   */
  public Biomes getBiomes() {
    return this.biomes;
  }

  /**
   * A collection of predefined biomes.
   *
   * @param biomes Biomes
   */
  public void setBiomes(Biomes biomes) {
    this.biomes = biomes;
  }
}
