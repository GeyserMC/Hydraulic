package org.geysermc.hydraulic.pack.bedrock.resource.fog;

import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.Description;
import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.Distance;
import org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.Volumetric;

/**
 * Fog Settings
 * <p>
 * The definition of a single fog.
 */
public class FogSettings {
  public Description description;

  public Distance distance;

  public Volumetric volumetric;

  /**
   * The identifying description of this fog settings.
   *
   * @return Description
   */
  public Description getDescription() {
    return this.description;
  }

  /**
   * The identifying description of this fog settings.
   *
   * @param description Description
   */
  public void setDescription(Description description) {
    this.description = description;
  }

  /**
   * The distance fog settings for different camera locations.
   *
   * @return Distance
   */
  public Distance getDistance() {
    return this.distance;
  }

  /**
   * The distance fog settings for different camera locations.
   *
   * @param distance Distance
   */
  public void setDistance(Distance distance) {
    this.distance = distance;
  }

  /**
   * The volumetric fog settings.
   *
   * @return Volumetric
   */
  public Volumetric getVolumetric() {
    return this.volumetric;
  }

  /**
   * The volumetric fog settings.
   *
   * @param volumetric Volumetric
   */
  public void setVolumetric(Volumetric volumetric) {
    this.volumetric = volumetric;
  }
}
