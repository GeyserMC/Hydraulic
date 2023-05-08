package org.geysermc.hydraulic.pack.bedrock.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import org.geysermc.hydraulic.pack.bedrock.resource.sounds.BlockSounds;
import org.geysermc.hydraulic.pack.bedrock.resource.sounds.EntitySounds;
import org.geysermc.hydraulic.pack.bedrock.resource.sounds.IndividualEventSounds;
import org.geysermc.hydraulic.pack.bedrock.resource.sounds.InteractiveSounds;

/**
 * Sounds.json
 * <p>
 * Sound definitions.
 */
public class Sounds {
  @JsonProperty("block_sounds")
  private Map<String, BlockSounds> blockSounds = new HashMap<>();

  @JsonProperty("entity_sounds")
  public EntitySounds entitySounds;

  @JsonProperty("individual_event_sounds")
  public IndividualEventSounds individualEventSounds;

  @JsonProperty("interactive_sounds")
  public InteractiveSounds interactiveSounds;

  /**
   * Block sound definitions.
   *
   * @return Block Sounds
   */
  public Map<String, BlockSounds> getBlockSounds() {
    return this.blockSounds;
  }

  /**
   * Block sound definitions.
   *
   * @param blockSounds Block Sounds
   */
  public void setBlockSounds(Map<String, BlockSounds> blockSounds) {
    this.blockSounds = blockSounds;
  }

  /**
   * Entity sounds definitions.
   *
   * @return Entity Sounds
   */
  public EntitySounds getEntitySounds() {
    return this.entitySounds;
  }

  /**
   * Entity sounds definitions.
   *
   * @param entitySounds Entity Sounds
   */
  public void setEntitySounds(EntitySounds entitySounds) {
    this.entitySounds = entitySounds;
  }

  /**
   * Individual event sounds definitions.
   *
   * @return Individual Event Sounds
   */
  public IndividualEventSounds getIndividualEventSounds() {
    return this.individualEventSounds;
  }

  /**
   * Individual event sounds definitions.
   *
   * @param individualEventSounds Individual Event Sounds
   */
  public void setIndividualEventSounds(IndividualEventSounds individualEventSounds) {
    this.individualEventSounds = individualEventSounds;
  }

  /**
   * Interactive sounds definitions.
   *
   * @return Interactive Sounds
   */
  public InteractiveSounds getInteractiveSounds() {
    return this.interactiveSounds;
  }

  /**
   * Interactive sounds definitions.
   *
   * @param interactiveSounds Interactive Sounds
   */
  public void setInteractiveSounds(InteractiveSounds interactiveSounds) {
    this.interactiveSounds = interactiveSounds;
  }
}
