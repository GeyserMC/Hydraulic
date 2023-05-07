package org.geysermc.hydraulic.pack.bedrock.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

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

  /**
   * Biomes
   * <p>
   * A collection of predefined biomes.
   */
  public static class Biomes {
    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class BambooJungleHills {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class BambooJungle {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class BasaltDeltas {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class Beach {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class BirchForestHills {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class BirchForest {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class ColdBeach {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class ColdOcean {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class ColdTaigaHills {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class ColdTaigaMutated {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class ColdTaiga {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class CrimsonForest {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class DeepColdOcean {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class DeepFrozenOcean {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class DeepLukewarmOcean {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class DeepOcean {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class DeepWarmOcean {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class Default {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class DesertHills {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class Desert {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class ExtremeHillsEdge {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class ExtremeHillsMutated {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class ExtremeHillsPlusTreesMutated {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class ExtremeHillsPlusTrees {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class ExtremeHills {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class FlowerForest {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class ForestHills {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class Forest {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class FrozenOcean {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class FrozenRiver {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class Hell {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class IceMountains {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class IcePlainsSpikes {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class IcePlains {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class JungleEdge {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class JungleHills {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class JungleMutated {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class Jungle {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class LukewarmOcean {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class MangroveSwamp {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class MegaSpruceTaigaMutated {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class MegaSpruceTaiga {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class MegaTaigaHills {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class MegaTaigaMutated {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class MegaTaiga {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class MesaBryce {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class MesaMutated {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class MesaPlateauStone {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class MesaPlateau {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class Mesa {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class MushroomIslandShore {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class MushroomIsland {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class Ocean {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class Plains {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class River {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class RoofedForest {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class SavannaMutated {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class SavannaPlateau {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class Savanna {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class SoulsandValley {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class StoneBeach {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class SunflowerPlains {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class SwamplandMutated {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class Swampland {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class TaigaHills {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class TaigaMutated {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class Taiga {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class TheEnd {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class WarmOcean {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }

    /**
     * Biome
     * <p>
     * The specification of colors in a given biome.
     */
    public static class WarpedForest {
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

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class FogColor {
      }

      /**
       * Fog Identifier
       * <p>
       * A minecraft fog identifier.
       */
      public static class FogIdentifier {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterFogColor {
      }

      /**
       * Colorhex
       * <p>
       * The colouration of this object.
       */
      public static class WaterSurfaceColor {
      }
    }
  }
}
