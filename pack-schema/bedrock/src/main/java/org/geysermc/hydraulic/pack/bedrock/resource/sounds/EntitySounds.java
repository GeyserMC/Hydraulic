package org.geysermc.hydraulic.pack.bedrock.resource.sounds;

import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import org.geysermc.hydraulic.pack.bedrock.resource.sounds.entitysounds.Defaults;
import org.geysermc.hydraulic.pack.bedrock.resource.sounds.entitysounds.Entities;

/**
 * Entity Sounds
 * <p>
 * Entity sounds definitions.
 */
public class EntitySounds {
  public Defaults defaults;

  private Map<String, Entities> entities = new HashMap<>();

  /**
   * Entity sound definitions.
   *
   * @return Entity Sound
   */
  public Defaults getDefaults() {
    return this.defaults;
  }

  /**
   * Entity sound definitions.
   *
   * @param defaults Entity Sound
   */
  public void setDefaults(Defaults defaults) {
    this.defaults = defaults;
  }

  /**
   * Entities definitions.
   *
   * @return Entities
   */
  public Map<String, Entities> getEntities() {
    return this.entities;
  }

  /**
   * Entities definitions.
   *
   * @param entities Entities
   */
  public void setEntities(Map<String, Entities> entities) {
    this.entities = entities;
  }
}
