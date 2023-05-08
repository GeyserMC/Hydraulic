package org.geysermc.hydraulic.pack.bedrock.resource.sounds.interactivesounds;

import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import org.geysermc.hydraulic.pack.bedrock.resource.sounds.interactivesounds.entitysounds.Defaults;
import org.geysermc.hydraulic.pack.bedrock.resource.sounds.interactivesounds.entitysounds.Entities;

/**
 * Entity Sounds
 * <p>
 * Entity sound definitions.
 */
public class EntitySounds {
  private Map<String, Defaults> defaults = new HashMap<>();

  private Map<String, Entities> entities = new HashMap<>();

  /**
   * Default sound definitions.
   *
   * @return Defaults
   */
  public Map<String, Defaults> getDefaults() {
    return this.defaults;
  }

  /**
   * Default sound definitions.
   *
   * @param defaults Defaults
   */
  public void setDefaults(Map<String, Defaults> defaults) {
    this.defaults = defaults;
  }

  /**
   * Entities sound definitions.
   *
   * @return Entites Sounds
   */
  public Map<String, Entities> getEntities() {
    return this.entities;
  }

  /**
   * Entities sound definitions.
   *
   * @param entities Entites Sounds
   */
  public void setEntities(Map<String, Entities> entities) {
    this.entities = entities;
  }
}
