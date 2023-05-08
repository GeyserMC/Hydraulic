package org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Biome
 * <p>
 * The specification of colors in a given biome.
 */
public class JungleEdge {
  @JsonProperty("fog_identifier")
  public String fogIdentifier;

  @JsonProperty("fog_ids_to_merge")
  public String[] fogIdsToMerge;

  @JsonProperty("inherit_from_prior_fog")
  public boolean inheritFromPriorFog;

  @JsonProperty("remove_all_prior_fog")
  public boolean removeAllPriorFog;

  @JsonProperty("water_fog_distance")
  public int waterFogDistance;

  @JsonProperty("water_surface_transparency")
  public float waterSurfaceTransparency;

  /**
   * A minecraft fog identifier.
   *
   * @return Fog Identifier
   */
  public String getFogIdentifier() {
    return this.fogIdentifier;
  }

  /**
   * A minecraft fog identifier.
   *
   * @param fogIdentifier Fog Identifier
   */
  public void setFogIdentifier(String fogIdentifier) {
    this.fogIdentifier = fogIdentifier;
  }

  /**
   * @return Fog Ids To Merge
   */
  public String[] getFogIdsToMerge() {
    return this.fogIdsToMerge;
  }

  /**
   * @param fogIdsToMerge Fog Ids To Merge
   */
  public void setFogIdsToMerge(String[] fogIdsToMerge) {
    this.fogIdsToMerge = fogIdsToMerge;
  }

  /**
   * @return Inherit From Prior Fog
   */
  public boolean getInheritFromPriorFog() {
    return this.inheritFromPriorFog;
  }

  /**
   * @param inheritFromPriorFog Inherit From Prior Fog
   */
  public void setInheritFromPriorFog(boolean inheritFromPriorFog) {
    this.inheritFromPriorFog = inheritFromPriorFog;
  }

  /**
   * @return Remove All Prior Fog
   */
  public boolean getRemoveAllPriorFog() {
    return this.removeAllPriorFog;
  }

  /**
   * @param removeAllPriorFog Remove All Prior Fog
   */
  public void setRemoveAllPriorFog(boolean removeAllPriorFog) {
    this.removeAllPriorFog = removeAllPriorFog;
  }

  /**
   * The distance the water fog start at.
   *
   * @return Water Fog Distance
   */
  public int getWaterFogDistance() {
    return this.waterFogDistance;
  }

  /**
   * The distance the water fog start at.
   *
   * @param waterFogDistance Water Fog Distance
   */
  public void setWaterFogDistance(int waterFogDistance) {
    this.waterFogDistance = waterFogDistance;
  }

  /**
   * The amount of transpareny the surface of the water has.
   *
   * @return Water Surface Transparency
   */
  public float getWaterSurfaceTransparency() {
    return this.waterSurfaceTransparency;
  }

  /**
   * The amount of transpareny the surface of the water has.
   *
   * @param waterSurfaceTransparency Water Surface Transparency
   */
  public void setWaterSurfaceTransparency(float waterSurfaceTransparency) {
    this.waterSurfaceTransparency = waterSurfaceTransparency;
  }
}
