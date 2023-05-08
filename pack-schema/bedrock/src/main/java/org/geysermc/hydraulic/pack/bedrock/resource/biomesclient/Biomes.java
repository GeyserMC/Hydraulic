package org.geysermc.hydraulic.pack.bedrock.resource.biomesclient;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.BambooJungle;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.BambooJungleHills;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.BasaltDeltas;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.Beach;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.BirchForest;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.BirchForestHills;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.ColdBeach;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.ColdOcean;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.ColdTaiga;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.ColdTaigaHills;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.ColdTaigaMutated;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.CrimsonForest;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.DeepColdOcean;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.DeepFrozenOcean;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.DeepLukewarmOcean;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.DeepOcean;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.DeepWarmOcean;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.DefaultValue;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.Desert;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.DesertHills;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.ExtremeHills;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.ExtremeHillsEdge;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.ExtremeHillsMutated;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.ExtremeHillsPlusTrees;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.ExtremeHillsPlusTreesMutated;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.FlowerForest;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.Forest;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.ForestHills;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.FrozenOcean;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.FrozenRiver;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.Hell;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.IceMountains;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.IcePlains;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.IcePlainsSpikes;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.Jungle;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.JungleEdge;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.JungleHills;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.JungleMutated;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.LukewarmOcean;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.MangroveSwamp;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.MegaSpruceTaiga;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.MegaSpruceTaigaMutated;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.MegaTaiga;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.MegaTaigaHills;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.MegaTaigaMutated;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.Mesa;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.MesaBryce;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.MesaMutated;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.MesaPlateau;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.MesaPlateauStone;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.MushroomIsland;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.MushroomIslandShore;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.Ocean;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.Plains;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.River;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.RoofedForest;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.Savanna;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.SavannaMutated;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.SavannaPlateau;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.SoulsandValley;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.StoneBeach;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.SunflowerPlains;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.Swampland;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.SwamplandMutated;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.Taiga;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.TaigaHills;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.TaigaMutated;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.TheEnd;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.WarmOcean;
import org.geysermc.hydraulic.pack.bedrock.resource.biomesclient.biomes.WarpedForest;

/**
 * Biomes
 * <p>
 * A collection of predefined biomes.
 */
public class Biomes {
  @JsonProperty("bamboo_jungle_hills")
  public BambooJungleHills bambooJungleHills;

  @JsonProperty("bamboo_jungle")
  public BambooJungle bambooJungle;

  @JsonProperty("basalt_deltas")
  public BasaltDeltas basaltDeltas;

  public Beach beach;

  @JsonProperty("birch_forest_hills")
  public BirchForestHills birchForestHills;

  @JsonProperty("birch_forest")
  public BirchForest birchForest;

  @JsonProperty("cold_beach")
  public ColdBeach coldBeach;

  @JsonProperty("cold_ocean")
  public ColdOcean coldOcean;

  @JsonProperty("cold_taiga_hills")
  public ColdTaigaHills coldTaigaHills;

  @JsonProperty("cold_taiga_mutated")
  public ColdTaigaMutated coldTaigaMutated;

  @JsonProperty("cold_taiga")
  public ColdTaiga coldTaiga;

  @JsonProperty("crimson_forest")
  public CrimsonForest crimsonForest;

  @JsonProperty("deep_cold_ocean")
  public DeepColdOcean deepColdOcean;

  @JsonProperty("deep_frozen_ocean")
  public DeepFrozenOcean deepFrozenOcean;

  @JsonProperty("deep_lukewarm_ocean")
  public DeepLukewarmOcean deepLukewarmOcean;

  @JsonProperty("deep_ocean")
  public DeepOcean deepOcean;

  @JsonProperty("deep_warm_ocean")
  public DeepWarmOcean deepWarmOcean;

  @JsonProperty("default")
  public DefaultValue defaultValue;

  @JsonProperty("desert_hills")
  public DesertHills desertHills;

  public Desert desert;

  @JsonProperty("extreme_hills_edge")
  public ExtremeHillsEdge extremeHillsEdge;

  @JsonProperty("extreme_hills_mutated")
  public ExtremeHillsMutated extremeHillsMutated;

  @JsonProperty("extreme_hills_plus_trees_mutated")
  public ExtremeHillsPlusTreesMutated extremeHillsPlusTreesMutated;

  @JsonProperty("extreme_hills_plus_trees")
  public ExtremeHillsPlusTrees extremeHillsPlusTrees;

  @JsonProperty("extreme_hills")
  public ExtremeHills extremeHills;

  @JsonProperty("flower_forest")
  public FlowerForest flowerForest;

  @JsonProperty("forest_hills")
  public ForestHills forestHills;

  public Forest forest;

  @JsonProperty("frozen_ocean")
  public FrozenOcean frozenOcean;

  @JsonProperty("frozen_river")
  public FrozenRiver frozenRiver;

  public Hell hell;

  @JsonProperty("ice_mountains")
  public IceMountains iceMountains;

  @JsonProperty("ice_plains_spikes")
  public IcePlainsSpikes icePlainsSpikes;

  @JsonProperty("ice_plains")
  public IcePlains icePlains;

  @JsonProperty("jungle_edge")
  public JungleEdge jungleEdge;

  @JsonProperty("jungle_hills")
  public JungleHills jungleHills;

  @JsonProperty("jungle_mutated")
  public JungleMutated jungleMutated;

  public Jungle jungle;

  @JsonProperty("lukewarm_ocean")
  public LukewarmOcean lukewarmOcean;

  @JsonProperty("mangrove_swamp")
  public MangroveSwamp mangroveSwamp;

  @JsonProperty("mega_spruce_taiga_mutated")
  public MegaSpruceTaigaMutated megaSpruceTaigaMutated;

  @JsonProperty("mega_spruce_taiga")
  public MegaSpruceTaiga megaSpruceTaiga;

  @JsonProperty("mega_taiga_hills")
  public MegaTaigaHills megaTaigaHills;

  @JsonProperty("mega_taiga_mutated")
  public MegaTaigaMutated megaTaigaMutated;

  @JsonProperty("mega_taiga")
  public MegaTaiga megaTaiga;

  @JsonProperty("mesa_bryce")
  public MesaBryce mesaBryce;

  @JsonProperty("mesa_mutated")
  public MesaMutated mesaMutated;

  @JsonProperty("mesa_plateau_stone")
  public MesaPlateauStone mesaPlateauStone;

  @JsonProperty("mesa_plateau")
  public MesaPlateau mesaPlateau;

  public Mesa mesa;

  @JsonProperty("mushroom_island_shore")
  public MushroomIslandShore mushroomIslandShore;

  @JsonProperty("mushroom_island")
  public MushroomIsland mushroomIsland;

  public Ocean ocean;

  public Plains plains;

  public River river;

  @JsonProperty("roofed_forest")
  public RoofedForest roofedForest;

  @JsonProperty("savanna_mutated")
  public SavannaMutated savannaMutated;

  @JsonProperty("savanna_plateau")
  public SavannaPlateau savannaPlateau;

  public Savanna savanna;

  @JsonProperty("soulsand_valley")
  public SoulsandValley soulsandValley;

  @JsonProperty("stone_beach")
  public StoneBeach stoneBeach;

  @JsonProperty("sunflower_plains")
  public SunflowerPlains sunflowerPlains;

  @JsonProperty("swampland_mutated")
  public SwamplandMutated swamplandMutated;

  public Swampland swampland;

  @JsonProperty("taiga_hills")
  public TaigaHills taigaHills;

  @JsonProperty("taiga_mutated")
  public TaigaMutated taigaMutated;

  public Taiga taiga;

  @JsonProperty("the_end")
  public TheEnd theEnd;

  @JsonProperty("warm_ocean")
  public WarmOcean warmOcean;

  @JsonProperty("warped_forest")
  public WarpedForest warpedForest;

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public BambooJungleHills getBambooJungleHills() {
    return this.bambooJungleHills;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param bambooJungleHills Biome
   */
  public void setBambooJungleHills(BambooJungleHills bambooJungleHills) {
    this.bambooJungleHills = bambooJungleHills;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public BambooJungle getBambooJungle() {
    return this.bambooJungle;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param bambooJungle Biome
   */
  public void setBambooJungle(BambooJungle bambooJungle) {
    this.bambooJungle = bambooJungle;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public BasaltDeltas getBasaltDeltas() {
    return this.basaltDeltas;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param basaltDeltas Biome
   */
  public void setBasaltDeltas(BasaltDeltas basaltDeltas) {
    this.basaltDeltas = basaltDeltas;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public Beach getBeach() {
    return this.beach;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param beach Biome
   */
  public void setBeach(Beach beach) {
    this.beach = beach;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public BirchForestHills getBirchForestHills() {
    return this.birchForestHills;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param birchForestHills Biome
   */
  public void setBirchForestHills(BirchForestHills birchForestHills) {
    this.birchForestHills = birchForestHills;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public BirchForest getBirchForest() {
    return this.birchForest;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param birchForest Biome
   */
  public void setBirchForest(BirchForest birchForest) {
    this.birchForest = birchForest;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public ColdBeach getColdBeach() {
    return this.coldBeach;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param coldBeach Biome
   */
  public void setColdBeach(ColdBeach coldBeach) {
    this.coldBeach = coldBeach;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public ColdOcean getColdOcean() {
    return this.coldOcean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param coldOcean Biome
   */
  public void setColdOcean(ColdOcean coldOcean) {
    this.coldOcean = coldOcean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public ColdTaigaHills getColdTaigaHills() {
    return this.coldTaigaHills;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param coldTaigaHills Biome
   */
  public void setColdTaigaHills(ColdTaigaHills coldTaigaHills) {
    this.coldTaigaHills = coldTaigaHills;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public ColdTaigaMutated getColdTaigaMutated() {
    return this.coldTaigaMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param coldTaigaMutated Biome
   */
  public void setColdTaigaMutated(ColdTaigaMutated coldTaigaMutated) {
    this.coldTaigaMutated = coldTaigaMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public ColdTaiga getColdTaiga() {
    return this.coldTaiga;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param coldTaiga Biome
   */
  public void setColdTaiga(ColdTaiga coldTaiga) {
    this.coldTaiga = coldTaiga;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public CrimsonForest getCrimsonForest() {
    return this.crimsonForest;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param crimsonForest Biome
   */
  public void setCrimsonForest(CrimsonForest crimsonForest) {
    this.crimsonForest = crimsonForest;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public DeepColdOcean getDeepColdOcean() {
    return this.deepColdOcean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param deepColdOcean Biome
   */
  public void setDeepColdOcean(DeepColdOcean deepColdOcean) {
    this.deepColdOcean = deepColdOcean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public DeepFrozenOcean getDeepFrozenOcean() {
    return this.deepFrozenOcean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param deepFrozenOcean Biome
   */
  public void setDeepFrozenOcean(DeepFrozenOcean deepFrozenOcean) {
    this.deepFrozenOcean = deepFrozenOcean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public DeepLukewarmOcean getDeepLukewarmOcean() {
    return this.deepLukewarmOcean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param deepLukewarmOcean Biome
   */
  public void setDeepLukewarmOcean(DeepLukewarmOcean deepLukewarmOcean) {
    this.deepLukewarmOcean = deepLukewarmOcean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public DeepOcean getDeepOcean() {
    return this.deepOcean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param deepOcean Biome
   */
  public void setDeepOcean(DeepOcean deepOcean) {
    this.deepOcean = deepOcean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public DeepWarmOcean getDeepWarmOcean() {
    return this.deepWarmOcean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param deepWarmOcean Biome
   */
  public void setDeepWarmOcean(DeepWarmOcean deepWarmOcean) {
    this.deepWarmOcean = deepWarmOcean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public DefaultValue getDefaultValue() {
    return this.defaultValue;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param defaultValue Biome
   */
  public void setDefaultValue(DefaultValue defaultValue) {
    this.defaultValue = defaultValue;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public DesertHills getDesertHills() {
    return this.desertHills;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param desertHills Biome
   */
  public void setDesertHills(DesertHills desertHills) {
    this.desertHills = desertHills;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public Desert getDesert() {
    return this.desert;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param desert Biome
   */
  public void setDesert(Desert desert) {
    this.desert = desert;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public ExtremeHillsEdge getExtremeHillsEdge() {
    return this.extremeHillsEdge;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param extremeHillsEdge Biome
   */
  public void setExtremeHillsEdge(ExtremeHillsEdge extremeHillsEdge) {
    this.extremeHillsEdge = extremeHillsEdge;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public ExtremeHillsMutated getExtremeHillsMutated() {
    return this.extremeHillsMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param extremeHillsMutated Biome
   */
  public void setExtremeHillsMutated(ExtremeHillsMutated extremeHillsMutated) {
    this.extremeHillsMutated = extremeHillsMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public ExtremeHillsPlusTreesMutated getExtremeHillsPlusTreesMutated() {
    return this.extremeHillsPlusTreesMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param extremeHillsPlusTreesMutated Biome
   */
  public void setExtremeHillsPlusTreesMutated(
      ExtremeHillsPlusTreesMutated extremeHillsPlusTreesMutated) {
    this.extremeHillsPlusTreesMutated = extremeHillsPlusTreesMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public ExtremeHillsPlusTrees getExtremeHillsPlusTrees() {
    return this.extremeHillsPlusTrees;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param extremeHillsPlusTrees Biome
   */
  public void setExtremeHillsPlusTrees(ExtremeHillsPlusTrees extremeHillsPlusTrees) {
    this.extremeHillsPlusTrees = extremeHillsPlusTrees;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public ExtremeHills getExtremeHills() {
    return this.extremeHills;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param extremeHills Biome
   */
  public void setExtremeHills(ExtremeHills extremeHills) {
    this.extremeHills = extremeHills;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public FlowerForest getFlowerForest() {
    return this.flowerForest;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param flowerForest Biome
   */
  public void setFlowerForest(FlowerForest flowerForest) {
    this.flowerForest = flowerForest;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public ForestHills getForestHills() {
    return this.forestHills;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param forestHills Biome
   */
  public void setForestHills(ForestHills forestHills) {
    this.forestHills = forestHills;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public Forest getForest() {
    return this.forest;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param forest Biome
   */
  public void setForest(Forest forest) {
    this.forest = forest;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public FrozenOcean getFrozenOcean() {
    return this.frozenOcean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param frozenOcean Biome
   */
  public void setFrozenOcean(FrozenOcean frozenOcean) {
    this.frozenOcean = frozenOcean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public FrozenRiver getFrozenRiver() {
    return this.frozenRiver;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param frozenRiver Biome
   */
  public void setFrozenRiver(FrozenRiver frozenRiver) {
    this.frozenRiver = frozenRiver;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public Hell getHell() {
    return this.hell;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param hell Biome
   */
  public void setHell(Hell hell) {
    this.hell = hell;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public IceMountains getIceMountains() {
    return this.iceMountains;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param iceMountains Biome
   */
  public void setIceMountains(IceMountains iceMountains) {
    this.iceMountains = iceMountains;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public IcePlainsSpikes getIcePlainsSpikes() {
    return this.icePlainsSpikes;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param icePlainsSpikes Biome
   */
  public void setIcePlainsSpikes(IcePlainsSpikes icePlainsSpikes) {
    this.icePlainsSpikes = icePlainsSpikes;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public IcePlains getIcePlains() {
    return this.icePlains;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param icePlains Biome
   */
  public void setIcePlains(IcePlains icePlains) {
    this.icePlains = icePlains;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public JungleEdge getJungleEdge() {
    return this.jungleEdge;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param jungleEdge Biome
   */
  public void setJungleEdge(JungleEdge jungleEdge) {
    this.jungleEdge = jungleEdge;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public JungleHills getJungleHills() {
    return this.jungleHills;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param jungleHills Biome
   */
  public void setJungleHills(JungleHills jungleHills) {
    this.jungleHills = jungleHills;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public JungleMutated getJungleMutated() {
    return this.jungleMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param jungleMutated Biome
   */
  public void setJungleMutated(JungleMutated jungleMutated) {
    this.jungleMutated = jungleMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public Jungle getJungle() {
    return this.jungle;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param jungle Biome
   */
  public void setJungle(Jungle jungle) {
    this.jungle = jungle;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public LukewarmOcean getLukewarmOcean() {
    return this.lukewarmOcean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param lukewarmOcean Biome
   */
  public void setLukewarmOcean(LukewarmOcean lukewarmOcean) {
    this.lukewarmOcean = lukewarmOcean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public MangroveSwamp getMangroveSwamp() {
    return this.mangroveSwamp;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param mangroveSwamp Biome
   */
  public void setMangroveSwamp(MangroveSwamp mangroveSwamp) {
    this.mangroveSwamp = mangroveSwamp;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public MegaSpruceTaigaMutated getMegaSpruceTaigaMutated() {
    return this.megaSpruceTaigaMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param megaSpruceTaigaMutated Biome
   */
  public void setMegaSpruceTaigaMutated(MegaSpruceTaigaMutated megaSpruceTaigaMutated) {
    this.megaSpruceTaigaMutated = megaSpruceTaigaMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public MegaSpruceTaiga getMegaSpruceTaiga() {
    return this.megaSpruceTaiga;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param megaSpruceTaiga Biome
   */
  public void setMegaSpruceTaiga(MegaSpruceTaiga megaSpruceTaiga) {
    this.megaSpruceTaiga = megaSpruceTaiga;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public MegaTaigaHills getMegaTaigaHills() {
    return this.megaTaigaHills;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param megaTaigaHills Biome
   */
  public void setMegaTaigaHills(MegaTaigaHills megaTaigaHills) {
    this.megaTaigaHills = megaTaigaHills;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public MegaTaigaMutated getMegaTaigaMutated() {
    return this.megaTaigaMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param megaTaigaMutated Biome
   */
  public void setMegaTaigaMutated(MegaTaigaMutated megaTaigaMutated) {
    this.megaTaigaMutated = megaTaigaMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public MegaTaiga getMegaTaiga() {
    return this.megaTaiga;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param megaTaiga Biome
   */
  public void setMegaTaiga(MegaTaiga megaTaiga) {
    this.megaTaiga = megaTaiga;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public MesaBryce getMesaBryce() {
    return this.mesaBryce;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param mesaBryce Biome
   */
  public void setMesaBryce(MesaBryce mesaBryce) {
    this.mesaBryce = mesaBryce;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public MesaMutated getMesaMutated() {
    return this.mesaMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param mesaMutated Biome
   */
  public void setMesaMutated(MesaMutated mesaMutated) {
    this.mesaMutated = mesaMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public MesaPlateauStone getMesaPlateauStone() {
    return this.mesaPlateauStone;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param mesaPlateauStone Biome
   */
  public void setMesaPlateauStone(MesaPlateauStone mesaPlateauStone) {
    this.mesaPlateauStone = mesaPlateauStone;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public MesaPlateau getMesaPlateau() {
    return this.mesaPlateau;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param mesaPlateau Biome
   */
  public void setMesaPlateau(MesaPlateau mesaPlateau) {
    this.mesaPlateau = mesaPlateau;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public Mesa getMesa() {
    return this.mesa;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param mesa Biome
   */
  public void setMesa(Mesa mesa) {
    this.mesa = mesa;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public MushroomIslandShore getMushroomIslandShore() {
    return this.mushroomIslandShore;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param mushroomIslandShore Biome
   */
  public void setMushroomIslandShore(MushroomIslandShore mushroomIslandShore) {
    this.mushroomIslandShore = mushroomIslandShore;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public MushroomIsland getMushroomIsland() {
    return this.mushroomIsland;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param mushroomIsland Biome
   */
  public void setMushroomIsland(MushroomIsland mushroomIsland) {
    this.mushroomIsland = mushroomIsland;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public Ocean getOcean() {
    return this.ocean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param ocean Biome
   */
  public void setOcean(Ocean ocean) {
    this.ocean = ocean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public Plains getPlains() {
    return this.plains;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param plains Biome
   */
  public void setPlains(Plains plains) {
    this.plains = plains;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public River getRiver() {
    return this.river;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param river Biome
   */
  public void setRiver(River river) {
    this.river = river;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public RoofedForest getRoofedForest() {
    return this.roofedForest;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param roofedForest Biome
   */
  public void setRoofedForest(RoofedForest roofedForest) {
    this.roofedForest = roofedForest;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public SavannaMutated getSavannaMutated() {
    return this.savannaMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param savannaMutated Biome
   */
  public void setSavannaMutated(SavannaMutated savannaMutated) {
    this.savannaMutated = savannaMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public SavannaPlateau getSavannaPlateau() {
    return this.savannaPlateau;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param savannaPlateau Biome
   */
  public void setSavannaPlateau(SavannaPlateau savannaPlateau) {
    this.savannaPlateau = savannaPlateau;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public Savanna getSavanna() {
    return this.savanna;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param savanna Biome
   */
  public void setSavanna(Savanna savanna) {
    this.savanna = savanna;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public SoulsandValley getSoulsandValley() {
    return this.soulsandValley;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param soulsandValley Biome
   */
  public void setSoulsandValley(SoulsandValley soulsandValley) {
    this.soulsandValley = soulsandValley;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public StoneBeach getStoneBeach() {
    return this.stoneBeach;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param stoneBeach Biome
   */
  public void setStoneBeach(StoneBeach stoneBeach) {
    this.stoneBeach = stoneBeach;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public SunflowerPlains getSunflowerPlains() {
    return this.sunflowerPlains;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param sunflowerPlains Biome
   */
  public void setSunflowerPlains(SunflowerPlains sunflowerPlains) {
    this.sunflowerPlains = sunflowerPlains;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public SwamplandMutated getSwamplandMutated() {
    return this.swamplandMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param swamplandMutated Biome
   */
  public void setSwamplandMutated(SwamplandMutated swamplandMutated) {
    this.swamplandMutated = swamplandMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public Swampland getSwampland() {
    return this.swampland;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param swampland Biome
   */
  public void setSwampland(Swampland swampland) {
    this.swampland = swampland;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public TaigaHills getTaigaHills() {
    return this.taigaHills;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param taigaHills Biome
   */
  public void setTaigaHills(TaigaHills taigaHills) {
    this.taigaHills = taigaHills;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public TaigaMutated getTaigaMutated() {
    return this.taigaMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param taigaMutated Biome
   */
  public void setTaigaMutated(TaigaMutated taigaMutated) {
    this.taigaMutated = taigaMutated;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public Taiga getTaiga() {
    return this.taiga;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param taiga Biome
   */
  public void setTaiga(Taiga taiga) {
    this.taiga = taiga;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public TheEnd getTheEnd() {
    return this.theEnd;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param theEnd Biome
   */
  public void setTheEnd(TheEnd theEnd) {
    this.theEnd = theEnd;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public WarmOcean getWarmOcean() {
    return this.warmOcean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param warmOcean Biome
   */
  public void setWarmOcean(WarmOcean warmOcean) {
    this.warmOcean = warmOcean;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @return Biome
   */
  public WarpedForest getWarpedForest() {
    return this.warpedForest;
  }

  /**
   * The specification of colors in a given biome.
   *
   * @param warpedForest Biome
   */
  public void setWarpedForest(WarpedForest warpedForest) {
    this.warpedForest = warpedForest;
  }
}
