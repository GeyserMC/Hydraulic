package org.geysermc.hydraulic.pack.bedrock.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Object;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * Sounds.json
 * <p>
 * Sound definitions.
 */
public class Sounds {
  @JsonProperty("block_sounds")
  public BlockSounds blockSounds;

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
  public BlockSounds getBlockSounds() {
    return this.blockSounds;
  }

  /**
   * Block sound definitions.
   *
   * @param blockSounds Block Sounds
   */
  public void setBlockSounds(BlockSounds blockSounds) {
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

  /**
   * Block Sounds
   * <p>
   * Block sound definitions.
   */
  public static class BlockSounds {
    private Map<String, Object> events = new HashMap<>();

    /**
     * @return Events
     */
    public Map<String, Object> getEvents() {
      return this.events;
    }

    /**
     * @param events Events
     */
    public void setEvents(Map<String, Object> events) {
      this.events = events;
    }
  }

  /**
   * Entity Sounds
   * <p>
   * Entity sounds definitions.
   */
  public static class EntitySounds {
    public Defaults defaults;

    private Map<String, Object> entities = new HashMap<>();

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
    public Map<String, Object> getEntities() {
      return this.entities;
    }

    /**
     * Entities definitions.
     *
     * @param entities Entities
     */
    public void setEntities(Map<String, Object> entities) {
      this.entities = entities;
    }

    /**
     * Entity Sound
     * <p>
     * Entity sound definitions.
     */
    public static class Defaults {
      private Map<String, Object> events = new HashMap<>();

      /**
       * @return Events
       */
      public Map<String, Object> getEvents() {
        return this.events;
      }

      /**
       * @param events Events
       */
      public void setEvents(Map<String, Object> events) {
        this.events = events;
      }
    }
  }

  /**
   * Individual Event Sounds
   * <p>
   * Individual event sounds definitions.
   */
  public static class IndividualEventSounds {
    private Map<String, Object> events = new HashMap<>();

    /**
     * Events.
     *
     * @return Events
     */
    public Map<String, Object> getEvents() {
      return this.events;
    }

    /**
     * Events.
     *
     * @param events Events
     */
    public void setEvents(Map<String, Object> events) {
      this.events = events;
    }
  }

  /**
   * Interactive Sounds
   * <p>
   * Interactive sounds definitions.
   */
  public static class InteractiveSounds {
    @JsonProperty("block_sounds")
    public BlockSounds blockSounds;

    @JsonProperty("entity_sounds")
    public EntitySounds entitySounds;

    /**
     * Block sound definitions.
     *
     * @return Block Sounds
     */
    public BlockSounds getBlockSounds() {
      return this.blockSounds;
    }

    /**
     * Block sound definitions.
     *
     * @param blockSounds Block Sounds
     */
    public void setBlockSounds(BlockSounds blockSounds) {
      this.blockSounds = blockSounds;
    }

    /**
     * Entity sound definitions.
     *
     * @return Entity Sounds
     */
    public EntitySounds getEntitySounds() {
      return this.entitySounds;
    }

    /**
     * Entity sound definitions.
     *
     * @param entitySounds Entity Sounds
     */
    public void setEntitySounds(EntitySounds entitySounds) {
      this.entitySounds = entitySounds;
    }

    /**
     * Block Sounds
     * <p>
     * Block sound definitions.
     */
    public static class BlockSounds {
      private Map<String, Object> events = new HashMap<>();

      /**
       * @return Events
       */
      public Map<String, Object> getEvents() {
        return this.events;
      }

      /**
       * @param events Events
       */
      public void setEvents(Map<String, Object> events) {
        this.events = events;
      }
    }

    /**
     * Entity Sounds
     * <p>
     * Entity sound definitions.
     */
    public static class EntitySounds {
      public Defaults defaults;

      public Entities entities;

      /**
       * Default sound definitions.
       *
       * @return Defaults
       */
      public Defaults getDefaults() {
        return this.defaults;
      }

      /**
       * Default sound definitions.
       *
       * @param defaults Defaults
       */
      public void setDefaults(Defaults defaults) {
        this.defaults = defaults;
      }

      /**
       * Entities sound definitions.
       *
       * @return Entites Sounds
       */
      public Entities getEntities() {
        return this.entities;
      }

      /**
       * Entities sound definitions.
       *
       * @param entities Entites Sounds
       */
      public void setEntities(Entities entities) {
        this.entities = entities;
      }

      /**
       * Defaults
       * <p>
       * Default sound definitions.
       */
      public static class Defaults {
        private Map<String, Object> events = new HashMap<>();

        /**
         * @return Entity Events
         */
        public Map<String, Object> getEvents() {
          return this.events;
        }

        /**
         * @param events Entity Events
         */
        public void setEvents(Map<String, Object> events) {
          this.events = events;
        }
      }

      /**
       * Entites Sounds
       * <p>
       * Entities sound definitions.
       */
      public static class Entities {
        private Map<String, Object> events = new HashMap<>();

        /**
         * @return Entity Events
         */
        public Map<String, Object> getEvents() {
          return this.events;
        }

        /**
         * @param events Entity Events
         */
        public void setEvents(Map<String, Object> events) {
          this.events = events;
        }
      }
    }
  }
}
