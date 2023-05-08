package org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.volumetric;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.volumetric.density.Air;
import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.volumetric.density.Lava;
import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.volumetric.density.LavaResistance;
import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.volumetric.density.Water;

/**
 * Density
 * <p>
 * The density settings for different camera locations.
 */
public class Density {
  public Air air;

  public Water water;

  public Lava lava;

  @JsonProperty("lava_resistance")
  public LavaResistance lavaResistance;

  public Air getAir() {
    return this.air;
  }

  public void setAir(Air air) {
    this.air = air;
  }

  public Water getWater() {
    return this.water;
  }

  public void setWater(Water water) {
    this.water = water;
  }

  public Lava getLava() {
    return this.lava;
  }

  public void setLava(Lava lava) {
    this.lava = lava;
  }

  public LavaResistance getLavaResistance() {
    return this.lavaResistance;
  }

  public void setLavaResistance(LavaResistance lavaResistance) {
    this.lavaResistance = lavaResistance;
  }
}
