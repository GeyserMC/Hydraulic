package org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.distance.Air;
import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.distance.Lava;
import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.distance.LavaResistance;
import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.distance.PowderSnow;
import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.distance.Water;
import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.distance.Weather;

/**
 * Distance
 * <p>
 * The distance fog settings for different camera locations.
 */
public class Distance {
  public Air air;

  public Weather weather;

  public Water water;

  public Lava lava;

  @JsonProperty("lava_resistance")
  public LavaResistance lavaResistance;

  @JsonProperty("powder_snow")
  public PowderSnow powderSnow;

  public Air getAir() {
    return this.air;
  }

  public void setAir(Air air) {
    this.air = air;
  }

  public Weather getWeather() {
    return this.weather;
  }

  public void setWeather(Weather weather) {
    this.weather = weather;
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

  public PowderSnow getPowderSnow() {
    return this.powderSnow;
  }

  public void setPowderSnow(PowderSnow powderSnow) {
    this.powderSnow = powderSnow;
  }
}
