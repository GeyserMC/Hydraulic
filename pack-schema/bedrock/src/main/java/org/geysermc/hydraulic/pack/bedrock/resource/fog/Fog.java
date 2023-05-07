package org.geysermc.hydraulic.pack.bedrock.resource.fog;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Fog
 */
public class Fog {
  @JsonProperty("format_version")
  public String formatVersion;

  @JsonProperty("minecraft:fog_settings")
  public FogSettings fogSettings;

  /**
   * A version that tells minecraft what type of data format can be expected when reading this file.
   *
   * @return Format Version
   */
  public String getFormatVersion() {
    return this.formatVersion;
  }

  /**
   * A version that tells minecraft what type of data format can be expected when reading this file.
   *
   * @param formatVersion Format Version
   */
  public void setFormatVersion(String formatVersion) {
    this.formatVersion = formatVersion;
  }

  /**
   * The definition of a single fog.
   *
   * @return Fog Settings
   */
  public FogSettings getFogSettings() {
    return this.fogSettings;
  }

  /**
   * The definition of a single fog.
   *
   * @param fogSettings Fog Settings
   */
  public void setFogSettings(FogSettings fogSettings) {
    this.fogSettings = fogSettings;
  }

  /**
   * Fog Settings
   * <p>
   * The definition of a single fog.
   */
  public static class FogSettings {
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

    /**
     * Description
     * <p>
     * The identifying description of this fog settings.
     */
    public static class Description {
      public String identifier;

      /**
       * The identifier for these fog settings. The identifier must include a namespace.
       *
       * @return Identifier
       */
      public String getIdentifier() {
        return this.identifier;
      }

      /**
       * The identifier for these fog settings. The identifier must include a namespace.
       *
       * @param identifier Identifier
       */
      public void setIdentifier(String identifier) {
        this.identifier = identifier;
      }
    }

    /**
     * Distance
     * <p>
     * The distance fog settings for different camera locations.
     */
    public static class Distance {
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

      public static class Air {
        @JsonProperty("fog_start")
        public float fogStart;

        @JsonProperty("fog_end")
        public float fogEnd;

        @JsonProperty("fog_color")
        public String fogColor;

        @JsonProperty("render_distance_type")
        public String renderDistanceType;

        @JsonProperty("transition_fog")
        public TransitionFog transitionFog;

        /**
         * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
         *
         * @return Fog Start
         */
        public float getFogStart() {
          return this.fogStart;
        }

        /**
         * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
         *
         * @param fogStart Fog Start
         */
        public void setFogStart(float fogStart) {
          this.fogStart = fogStart;
        }

        /**
         * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
         *
         * @return Fog End
         */
        public float getFogEnd() {
          return this.fogEnd;
        }

        /**
         * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
         *
         * @param fogEnd Fog End
         */
        public void setFogEnd(float fogEnd) {
          this.fogEnd = fogEnd;
        }

        /**
         * The color that the fog will take on.
         *
         * @return Fog Color
         */
        public String getFogColor() {
          return this.fogColor;
        }

        /**
         * The color that the fog will take on.
         *
         * @param fogColor Fog Color
         */
        public void setFogColor(String fogColor) {
          this.fogColor = fogColor;
        }

        /**
         * Determines how distance value is used. Fixed distance is measured in blocks. Dynamic distance is multiplied by the current render distance.
         *
         * @return Render Distance Type
         */
        public String getRenderDistanceType() {
          return this.renderDistanceType;
        }

        /**
         * Determines how distance value is used. Fixed distance is measured in blocks. Dynamic distance is multiplied by the current render distance.
         *
         * @param renderDistanceType Render Distance Type
         */
        public void setRenderDistanceType(String renderDistanceType) {
          this.renderDistanceType = renderDistanceType;
        }

        /**
         * Additional fog data which will slowly transition to the distance fog of current biome.
         *
         * @return Transition Fog
         */
        public TransitionFog getTransitionFog() {
          return this.transitionFog;
        }

        /**
         * Additional fog data which will slowly transition to the distance fog of current biome.
         *
         * @param transitionFog Transition Fog
         */
        public void setTransitionFog(TransitionFog transitionFog) {
          this.transitionFog = transitionFog;
        }

        /**
         * Transition Fog
         * <p>
         * Additional fog data which will slowly transition to the distance fog of current biome.
         */
        public static class TransitionFog {
          @JsonProperty("init_fog")
          public InitFog initFog;

          @JsonProperty("min_percent")
          public float minPercent;

          @JsonProperty("mid_seconds")
          public float midSeconds;

          @JsonProperty("mid_percent")
          public float midPercent;

          @JsonProperty("max_seconds")
          public float maxSeconds;

          /**
           * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
           *
           * @return Initial Fog
           */
          public InitFog getInitFog() {
            return this.initFog;
          }

          /**
           * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
           *
           * @param initFog Initial Fog
           */
          public void setInitFog(InitFog initFog) {
            this.initFog = initFog;
          }

          /**
           * The minimum progress of fog transition.
           *
           * @return Minimum Percent
           */
          public float getMinPercent() {
            return this.minPercent;
          }

          /**
           * The minimum progress of fog transition.
           *
           * @param minPercent Minimum Percent
           */
          public void setMinPercent(float minPercent) {
            this.minPercent = minPercent;
          }

          /**
           * The time takes to reach certain progress('mid_percent') of fog transition.
           *
           * @return Midpoint Seconds
           */
          public float getMidSeconds() {
            return this.midSeconds;
          }

          /**
           * The time takes to reach certain progress('mid_percent') of fog transition.
           *
           * @param midSeconds Midpoint Seconds
           */
          public void setMidSeconds(float midSeconds) {
            this.midSeconds = midSeconds;
          }

          /**
           * The progress of fog transition after 'mid_seconds' seconds.
           *
           * @return Midpoint Percent
           */
          public float getMidPercent() {
            return this.midPercent;
          }

          /**
           * The progress of fog transition after 'mid_seconds' seconds.
           *
           * @param midPercent Midpoint Percent
           */
          public void setMidPercent(float midPercent) {
            this.midPercent = midPercent;
          }

          /**
           * Total amount of time takes to complete fog transition.
           *
           * @return Maximum Seconds
           */
          public float getMaxSeconds() {
            return this.maxSeconds;
          }

          /**
           * Total amount of time takes to complete fog transition.
           *
           * @param maxSeconds Maximum Seconds
           */
          public void setMaxSeconds(float maxSeconds) {
            this.maxSeconds = maxSeconds;
          }

          /**
           * Initial Fog
           * <p>
           * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
           */
          public static class InitFog {
            @JsonProperty("fog_color")
            public String fogColor;

            @JsonProperty("fog_start")
            public float fogStart;

            @JsonProperty("fog_end")
            public float fogEnd;

            /**
             * The color that the fog will take on.
             *
             * @return Fog Color
             */
            public String getFogColor() {
              return this.fogColor;
            }

            /**
             * The color that the fog will take on.
             *
             * @param fogColor Fog Color
             */
            public void setFogColor(String fogColor) {
              this.fogColor = fogColor;
            }

            /**
             * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
             *
             * @return Fog Start
             */
            public float getFogStart() {
              return this.fogStart;
            }

            /**
             * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
             *
             * @param fogStart Fog Start
             */
            public void setFogStart(float fogStart) {
              this.fogStart = fogStart;
            }

            /**
             * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
             *
             * @return Fog End
             */
            public float getFogEnd() {
              return this.fogEnd;
            }

            /**
             * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
             *
             * @param fogEnd Fog End
             */
            public void setFogEnd(float fogEnd) {
              this.fogEnd = fogEnd;
            }
          }
        }
      }

      public static class Weather {
        @JsonProperty("fog_start")
        public float fogStart;

        @JsonProperty("fog_end")
        public float fogEnd;

        @JsonProperty("fog_color")
        public String fogColor;

        @JsonProperty("render_distance_type")
        public String renderDistanceType;

        @JsonProperty("transition_fog")
        public TransitionFog transitionFog;

        /**
         * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
         *
         * @return Fog Start
         */
        public float getFogStart() {
          return this.fogStart;
        }

        /**
         * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
         *
         * @param fogStart Fog Start
         */
        public void setFogStart(float fogStart) {
          this.fogStart = fogStart;
        }

        /**
         * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
         *
         * @return Fog End
         */
        public float getFogEnd() {
          return this.fogEnd;
        }

        /**
         * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
         *
         * @param fogEnd Fog End
         */
        public void setFogEnd(float fogEnd) {
          this.fogEnd = fogEnd;
        }

        /**
         * The color that the fog will take on.
         *
         * @return Fog Color
         */
        public String getFogColor() {
          return this.fogColor;
        }

        /**
         * The color that the fog will take on.
         *
         * @param fogColor Fog Color
         */
        public void setFogColor(String fogColor) {
          this.fogColor = fogColor;
        }

        /**
         * Determines how distance value is used. Fixed distance is measured in blocks. Dynamic distance is multiplied by the current render distance.
         *
         * @return Render Distance Type
         */
        public String getRenderDistanceType() {
          return this.renderDistanceType;
        }

        /**
         * Determines how distance value is used. Fixed distance is measured in blocks. Dynamic distance is multiplied by the current render distance.
         *
         * @param renderDistanceType Render Distance Type
         */
        public void setRenderDistanceType(String renderDistanceType) {
          this.renderDistanceType = renderDistanceType;
        }

        /**
         * Additional fog data which will slowly transition to the distance fog of current biome.
         *
         * @return Transition Fog
         */
        public TransitionFog getTransitionFog() {
          return this.transitionFog;
        }

        /**
         * Additional fog data which will slowly transition to the distance fog of current biome.
         *
         * @param transitionFog Transition Fog
         */
        public void setTransitionFog(TransitionFog transitionFog) {
          this.transitionFog = transitionFog;
        }

        /**
         * Transition Fog
         * <p>
         * Additional fog data which will slowly transition to the distance fog of current biome.
         */
        public static class TransitionFog {
          @JsonProperty("init_fog")
          public InitFog initFog;

          @JsonProperty("min_percent")
          public float minPercent;

          @JsonProperty("mid_seconds")
          public float midSeconds;

          @JsonProperty("mid_percent")
          public float midPercent;

          @JsonProperty("max_seconds")
          public float maxSeconds;

          /**
           * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
           *
           * @return Initial Fog
           */
          public InitFog getInitFog() {
            return this.initFog;
          }

          /**
           * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
           *
           * @param initFog Initial Fog
           */
          public void setInitFog(InitFog initFog) {
            this.initFog = initFog;
          }

          /**
           * The minimum progress of fog transition.
           *
           * @return Minimum Percent
           */
          public float getMinPercent() {
            return this.minPercent;
          }

          /**
           * The minimum progress of fog transition.
           *
           * @param minPercent Minimum Percent
           */
          public void setMinPercent(float minPercent) {
            this.minPercent = minPercent;
          }

          /**
           * The time takes to reach certain progress('mid_percent') of fog transition.
           *
           * @return Midpoint Seconds
           */
          public float getMidSeconds() {
            return this.midSeconds;
          }

          /**
           * The time takes to reach certain progress('mid_percent') of fog transition.
           *
           * @param midSeconds Midpoint Seconds
           */
          public void setMidSeconds(float midSeconds) {
            this.midSeconds = midSeconds;
          }

          /**
           * The progress of fog transition after 'mid_seconds' seconds.
           *
           * @return Midpoint Percent
           */
          public float getMidPercent() {
            return this.midPercent;
          }

          /**
           * The progress of fog transition after 'mid_seconds' seconds.
           *
           * @param midPercent Midpoint Percent
           */
          public void setMidPercent(float midPercent) {
            this.midPercent = midPercent;
          }

          /**
           * Total amount of time takes to complete fog transition.
           *
           * @return Maximum Seconds
           */
          public float getMaxSeconds() {
            return this.maxSeconds;
          }

          /**
           * Total amount of time takes to complete fog transition.
           *
           * @param maxSeconds Maximum Seconds
           */
          public void setMaxSeconds(float maxSeconds) {
            this.maxSeconds = maxSeconds;
          }

          /**
           * Initial Fog
           * <p>
           * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
           */
          public static class InitFog {
            @JsonProperty("fog_color")
            public String fogColor;

            @JsonProperty("fog_start")
            public float fogStart;

            @JsonProperty("fog_end")
            public float fogEnd;

            /**
             * The color that the fog will take on.
             *
             * @return Fog Color
             */
            public String getFogColor() {
              return this.fogColor;
            }

            /**
             * The color that the fog will take on.
             *
             * @param fogColor Fog Color
             */
            public void setFogColor(String fogColor) {
              this.fogColor = fogColor;
            }

            /**
             * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
             *
             * @return Fog Start
             */
            public float getFogStart() {
              return this.fogStart;
            }

            /**
             * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
             *
             * @param fogStart Fog Start
             */
            public void setFogStart(float fogStart) {
              this.fogStart = fogStart;
            }

            /**
             * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
             *
             * @return Fog End
             */
            public float getFogEnd() {
              return this.fogEnd;
            }

            /**
             * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
             *
             * @param fogEnd Fog End
             */
            public void setFogEnd(float fogEnd) {
              this.fogEnd = fogEnd;
            }
          }
        }
      }

      public static class Water {
        @JsonProperty("fog_start")
        public float fogStart;

        @JsonProperty("fog_end")
        public float fogEnd;

        @JsonProperty("fog_color")
        public String fogColor;

        @JsonProperty("render_distance_type")
        public String renderDistanceType;

        @JsonProperty("transition_fog")
        public TransitionFog transitionFog;

        /**
         * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
         *
         * @return Fog Start
         */
        public float getFogStart() {
          return this.fogStart;
        }

        /**
         * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
         *
         * @param fogStart Fog Start
         */
        public void setFogStart(float fogStart) {
          this.fogStart = fogStart;
        }

        /**
         * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
         *
         * @return Fog End
         */
        public float getFogEnd() {
          return this.fogEnd;
        }

        /**
         * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
         *
         * @param fogEnd Fog End
         */
        public void setFogEnd(float fogEnd) {
          this.fogEnd = fogEnd;
        }

        /**
         * The color that the fog will take on.
         *
         * @return Fog Color
         */
        public String getFogColor() {
          return this.fogColor;
        }

        /**
         * The color that the fog will take on.
         *
         * @param fogColor Fog Color
         */
        public void setFogColor(String fogColor) {
          this.fogColor = fogColor;
        }

        /**
         * Determines how distance value is used. Fixed distance is measured in blocks. Dynamic distance is multiplied by the current render distance.
         *
         * @return Render Distance Type
         */
        public String getRenderDistanceType() {
          return this.renderDistanceType;
        }

        /**
         * Determines how distance value is used. Fixed distance is measured in blocks. Dynamic distance is multiplied by the current render distance.
         *
         * @param renderDistanceType Render Distance Type
         */
        public void setRenderDistanceType(String renderDistanceType) {
          this.renderDistanceType = renderDistanceType;
        }

        /**
         * Additional fog data which will slowly transition to the distance fog of current biome.
         *
         * @return Transition Fog
         */
        public TransitionFog getTransitionFog() {
          return this.transitionFog;
        }

        /**
         * Additional fog data which will slowly transition to the distance fog of current biome.
         *
         * @param transitionFog Transition Fog
         */
        public void setTransitionFog(TransitionFog transitionFog) {
          this.transitionFog = transitionFog;
        }

        /**
         * Transition Fog
         * <p>
         * Additional fog data which will slowly transition to the distance fog of current biome.
         */
        public static class TransitionFog {
          @JsonProperty("init_fog")
          public InitFog initFog;

          @JsonProperty("min_percent")
          public float minPercent;

          @JsonProperty("mid_seconds")
          public float midSeconds;

          @JsonProperty("mid_percent")
          public float midPercent;

          @JsonProperty("max_seconds")
          public float maxSeconds;

          /**
           * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
           *
           * @return Initial Fog
           */
          public InitFog getInitFog() {
            return this.initFog;
          }

          /**
           * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
           *
           * @param initFog Initial Fog
           */
          public void setInitFog(InitFog initFog) {
            this.initFog = initFog;
          }

          /**
           * The minimum progress of fog transition.
           *
           * @return Minimum Percent
           */
          public float getMinPercent() {
            return this.minPercent;
          }

          /**
           * The minimum progress of fog transition.
           *
           * @param minPercent Minimum Percent
           */
          public void setMinPercent(float minPercent) {
            this.minPercent = minPercent;
          }

          /**
           * The time takes to reach certain progress('mid_percent') of fog transition.
           *
           * @return Midpoint Seconds
           */
          public float getMidSeconds() {
            return this.midSeconds;
          }

          /**
           * The time takes to reach certain progress('mid_percent') of fog transition.
           *
           * @param midSeconds Midpoint Seconds
           */
          public void setMidSeconds(float midSeconds) {
            this.midSeconds = midSeconds;
          }

          /**
           * The progress of fog transition after 'mid_seconds' seconds.
           *
           * @return Midpoint Percent
           */
          public float getMidPercent() {
            return this.midPercent;
          }

          /**
           * The progress of fog transition after 'mid_seconds' seconds.
           *
           * @param midPercent Midpoint Percent
           */
          public void setMidPercent(float midPercent) {
            this.midPercent = midPercent;
          }

          /**
           * Total amount of time takes to complete fog transition.
           *
           * @return Maximum Seconds
           */
          public float getMaxSeconds() {
            return this.maxSeconds;
          }

          /**
           * Total amount of time takes to complete fog transition.
           *
           * @param maxSeconds Maximum Seconds
           */
          public void setMaxSeconds(float maxSeconds) {
            this.maxSeconds = maxSeconds;
          }

          /**
           * Initial Fog
           * <p>
           * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
           */
          public static class InitFog {
            @JsonProperty("fog_color")
            public String fogColor;

            @JsonProperty("fog_start")
            public float fogStart;

            @JsonProperty("fog_end")
            public float fogEnd;

            /**
             * The color that the fog will take on.
             *
             * @return Fog Color
             */
            public String getFogColor() {
              return this.fogColor;
            }

            /**
             * The color that the fog will take on.
             *
             * @param fogColor Fog Color
             */
            public void setFogColor(String fogColor) {
              this.fogColor = fogColor;
            }

            /**
             * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
             *
             * @return Fog Start
             */
            public float getFogStart() {
              return this.fogStart;
            }

            /**
             * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
             *
             * @param fogStart Fog Start
             */
            public void setFogStart(float fogStart) {
              this.fogStart = fogStart;
            }

            /**
             * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
             *
             * @return Fog End
             */
            public float getFogEnd() {
              return this.fogEnd;
            }

            /**
             * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
             *
             * @param fogEnd Fog End
             */
            public void setFogEnd(float fogEnd) {
              this.fogEnd = fogEnd;
            }
          }
        }
      }

      public static class Lava {
        @JsonProperty("fog_start")
        public float fogStart;

        @JsonProperty("fog_end")
        public float fogEnd;

        @JsonProperty("fog_color")
        public String fogColor;

        @JsonProperty("render_distance_type")
        public String renderDistanceType;

        @JsonProperty("transition_fog")
        public TransitionFog transitionFog;

        /**
         * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
         *
         * @return Fog Start
         */
        public float getFogStart() {
          return this.fogStart;
        }

        /**
         * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
         *
         * @param fogStart Fog Start
         */
        public void setFogStart(float fogStart) {
          this.fogStart = fogStart;
        }

        /**
         * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
         *
         * @return Fog End
         */
        public float getFogEnd() {
          return this.fogEnd;
        }

        /**
         * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
         *
         * @param fogEnd Fog End
         */
        public void setFogEnd(float fogEnd) {
          this.fogEnd = fogEnd;
        }

        /**
         * The color that the fog will take on.
         *
         * @return Fog Color
         */
        public String getFogColor() {
          return this.fogColor;
        }

        /**
         * The color that the fog will take on.
         *
         * @param fogColor Fog Color
         */
        public void setFogColor(String fogColor) {
          this.fogColor = fogColor;
        }

        /**
         * Determines how distance value is used. Fixed distance is measured in blocks. Dynamic distance is multiplied by the current render distance.
         *
         * @return Render Distance Type
         */
        public String getRenderDistanceType() {
          return this.renderDistanceType;
        }

        /**
         * Determines how distance value is used. Fixed distance is measured in blocks. Dynamic distance is multiplied by the current render distance.
         *
         * @param renderDistanceType Render Distance Type
         */
        public void setRenderDistanceType(String renderDistanceType) {
          this.renderDistanceType = renderDistanceType;
        }

        /**
         * Additional fog data which will slowly transition to the distance fog of current biome.
         *
         * @return Transition Fog
         */
        public TransitionFog getTransitionFog() {
          return this.transitionFog;
        }

        /**
         * Additional fog data which will slowly transition to the distance fog of current biome.
         *
         * @param transitionFog Transition Fog
         */
        public void setTransitionFog(TransitionFog transitionFog) {
          this.transitionFog = transitionFog;
        }

        /**
         * Transition Fog
         * <p>
         * Additional fog data which will slowly transition to the distance fog of current biome.
         */
        public static class TransitionFog {
          @JsonProperty("init_fog")
          public InitFog initFog;

          @JsonProperty("min_percent")
          public float minPercent;

          @JsonProperty("mid_seconds")
          public float midSeconds;

          @JsonProperty("mid_percent")
          public float midPercent;

          @JsonProperty("max_seconds")
          public float maxSeconds;

          /**
           * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
           *
           * @return Initial Fog
           */
          public InitFog getInitFog() {
            return this.initFog;
          }

          /**
           * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
           *
           * @param initFog Initial Fog
           */
          public void setInitFog(InitFog initFog) {
            this.initFog = initFog;
          }

          /**
           * The minimum progress of fog transition.
           *
           * @return Minimum Percent
           */
          public float getMinPercent() {
            return this.minPercent;
          }

          /**
           * The minimum progress of fog transition.
           *
           * @param minPercent Minimum Percent
           */
          public void setMinPercent(float minPercent) {
            this.minPercent = minPercent;
          }

          /**
           * The time takes to reach certain progress('mid_percent') of fog transition.
           *
           * @return Midpoint Seconds
           */
          public float getMidSeconds() {
            return this.midSeconds;
          }

          /**
           * The time takes to reach certain progress('mid_percent') of fog transition.
           *
           * @param midSeconds Midpoint Seconds
           */
          public void setMidSeconds(float midSeconds) {
            this.midSeconds = midSeconds;
          }

          /**
           * The progress of fog transition after 'mid_seconds' seconds.
           *
           * @return Midpoint Percent
           */
          public float getMidPercent() {
            return this.midPercent;
          }

          /**
           * The progress of fog transition after 'mid_seconds' seconds.
           *
           * @param midPercent Midpoint Percent
           */
          public void setMidPercent(float midPercent) {
            this.midPercent = midPercent;
          }

          /**
           * Total amount of time takes to complete fog transition.
           *
           * @return Maximum Seconds
           */
          public float getMaxSeconds() {
            return this.maxSeconds;
          }

          /**
           * Total amount of time takes to complete fog transition.
           *
           * @param maxSeconds Maximum Seconds
           */
          public void setMaxSeconds(float maxSeconds) {
            this.maxSeconds = maxSeconds;
          }

          /**
           * Initial Fog
           * <p>
           * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
           */
          public static class InitFog {
            @JsonProperty("fog_color")
            public String fogColor;

            @JsonProperty("fog_start")
            public float fogStart;

            @JsonProperty("fog_end")
            public float fogEnd;

            /**
             * The color that the fog will take on.
             *
             * @return Fog Color
             */
            public String getFogColor() {
              return this.fogColor;
            }

            /**
             * The color that the fog will take on.
             *
             * @param fogColor Fog Color
             */
            public void setFogColor(String fogColor) {
              this.fogColor = fogColor;
            }

            /**
             * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
             *
             * @return Fog Start
             */
            public float getFogStart() {
              return this.fogStart;
            }

            /**
             * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
             *
             * @param fogStart Fog Start
             */
            public void setFogStart(float fogStart) {
              this.fogStart = fogStart;
            }

            /**
             * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
             *
             * @return Fog End
             */
            public float getFogEnd() {
              return this.fogEnd;
            }

            /**
             * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
             *
             * @param fogEnd Fog End
             */
            public void setFogEnd(float fogEnd) {
              this.fogEnd = fogEnd;
            }
          }
        }
      }

      public static class LavaResistance {
        @JsonProperty("fog_start")
        public float fogStart;

        @JsonProperty("fog_end")
        public float fogEnd;

        @JsonProperty("fog_color")
        public String fogColor;

        @JsonProperty("render_distance_type")
        public String renderDistanceType;

        @JsonProperty("transition_fog")
        public TransitionFog transitionFog;

        /**
         * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
         *
         * @return Fog Start
         */
        public float getFogStart() {
          return this.fogStart;
        }

        /**
         * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
         *
         * @param fogStart Fog Start
         */
        public void setFogStart(float fogStart) {
          this.fogStart = fogStart;
        }

        /**
         * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
         *
         * @return Fog End
         */
        public float getFogEnd() {
          return this.fogEnd;
        }

        /**
         * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
         *
         * @param fogEnd Fog End
         */
        public void setFogEnd(float fogEnd) {
          this.fogEnd = fogEnd;
        }

        /**
         * The color that the fog will take on.
         *
         * @return Fog Color
         */
        public String getFogColor() {
          return this.fogColor;
        }

        /**
         * The color that the fog will take on.
         *
         * @param fogColor Fog Color
         */
        public void setFogColor(String fogColor) {
          this.fogColor = fogColor;
        }

        /**
         * Determines how distance value is used. Fixed distance is measured in blocks. Dynamic distance is multiplied by the current render distance.
         *
         * @return Render Distance Type
         */
        public String getRenderDistanceType() {
          return this.renderDistanceType;
        }

        /**
         * Determines how distance value is used. Fixed distance is measured in blocks. Dynamic distance is multiplied by the current render distance.
         *
         * @param renderDistanceType Render Distance Type
         */
        public void setRenderDistanceType(String renderDistanceType) {
          this.renderDistanceType = renderDistanceType;
        }

        /**
         * Additional fog data which will slowly transition to the distance fog of current biome.
         *
         * @return Transition Fog
         */
        public TransitionFog getTransitionFog() {
          return this.transitionFog;
        }

        /**
         * Additional fog data which will slowly transition to the distance fog of current biome.
         *
         * @param transitionFog Transition Fog
         */
        public void setTransitionFog(TransitionFog transitionFog) {
          this.transitionFog = transitionFog;
        }

        /**
         * Transition Fog
         * <p>
         * Additional fog data which will slowly transition to the distance fog of current biome.
         */
        public static class TransitionFog {
          @JsonProperty("init_fog")
          public InitFog initFog;

          @JsonProperty("min_percent")
          public float minPercent;

          @JsonProperty("mid_seconds")
          public float midSeconds;

          @JsonProperty("mid_percent")
          public float midPercent;

          @JsonProperty("max_seconds")
          public float maxSeconds;

          /**
           * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
           *
           * @return Initial Fog
           */
          public InitFog getInitFog() {
            return this.initFog;
          }

          /**
           * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
           *
           * @param initFog Initial Fog
           */
          public void setInitFog(InitFog initFog) {
            this.initFog = initFog;
          }

          /**
           * The minimum progress of fog transition.
           *
           * @return Minimum Percent
           */
          public float getMinPercent() {
            return this.minPercent;
          }

          /**
           * The minimum progress of fog transition.
           *
           * @param minPercent Minimum Percent
           */
          public void setMinPercent(float minPercent) {
            this.minPercent = minPercent;
          }

          /**
           * The time takes to reach certain progress('mid_percent') of fog transition.
           *
           * @return Midpoint Seconds
           */
          public float getMidSeconds() {
            return this.midSeconds;
          }

          /**
           * The time takes to reach certain progress('mid_percent') of fog transition.
           *
           * @param midSeconds Midpoint Seconds
           */
          public void setMidSeconds(float midSeconds) {
            this.midSeconds = midSeconds;
          }

          /**
           * The progress of fog transition after 'mid_seconds' seconds.
           *
           * @return Midpoint Percent
           */
          public float getMidPercent() {
            return this.midPercent;
          }

          /**
           * The progress of fog transition after 'mid_seconds' seconds.
           *
           * @param midPercent Midpoint Percent
           */
          public void setMidPercent(float midPercent) {
            this.midPercent = midPercent;
          }

          /**
           * Total amount of time takes to complete fog transition.
           *
           * @return Maximum Seconds
           */
          public float getMaxSeconds() {
            return this.maxSeconds;
          }

          /**
           * Total amount of time takes to complete fog transition.
           *
           * @param maxSeconds Maximum Seconds
           */
          public void setMaxSeconds(float maxSeconds) {
            this.maxSeconds = maxSeconds;
          }

          /**
           * Initial Fog
           * <p>
           * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
           */
          public static class InitFog {
            @JsonProperty("fog_color")
            public String fogColor;

            @JsonProperty("fog_start")
            public float fogStart;

            @JsonProperty("fog_end")
            public float fogEnd;

            /**
             * The color that the fog will take on.
             *
             * @return Fog Color
             */
            public String getFogColor() {
              return this.fogColor;
            }

            /**
             * The color that the fog will take on.
             *
             * @param fogColor Fog Color
             */
            public void setFogColor(String fogColor) {
              this.fogColor = fogColor;
            }

            /**
             * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
             *
             * @return Fog Start
             */
            public float getFogStart() {
              return this.fogStart;
            }

            /**
             * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
             *
             * @param fogStart Fog Start
             */
            public void setFogStart(float fogStart) {
              this.fogStart = fogStart;
            }

            /**
             * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
             *
             * @return Fog End
             */
            public float getFogEnd() {
              return this.fogEnd;
            }

            /**
             * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
             *
             * @param fogEnd Fog End
             */
            public void setFogEnd(float fogEnd) {
              this.fogEnd = fogEnd;
            }
          }
        }
      }

      public static class PowderSnow {
        @JsonProperty("fog_start")
        public float fogStart;

        @JsonProperty("fog_end")
        public float fogEnd;

        @JsonProperty("fog_color")
        public String fogColor;

        @JsonProperty("render_distance_type")
        public String renderDistanceType;

        @JsonProperty("transition_fog")
        public TransitionFog transitionFog;

        /**
         * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
         *
         * @return Fog Start
         */
        public float getFogStart() {
          return this.fogStart;
        }

        /**
         * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
         *
         * @param fogStart Fog Start
         */
        public void setFogStart(float fogStart) {
          this.fogStart = fogStart;
        }

        /**
         * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
         *
         * @return Fog End
         */
        public float getFogEnd() {
          return this.fogEnd;
        }

        /**
         * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
         *
         * @param fogEnd Fog End
         */
        public void setFogEnd(float fogEnd) {
          this.fogEnd = fogEnd;
        }

        /**
         * The color that the fog will take on.
         *
         * @return Fog Color
         */
        public String getFogColor() {
          return this.fogColor;
        }

        /**
         * The color that the fog will take on.
         *
         * @param fogColor Fog Color
         */
        public void setFogColor(String fogColor) {
          this.fogColor = fogColor;
        }

        /**
         * Determines how distance value is used. Fixed distance is measured in blocks. Dynamic distance is multiplied by the current render distance.
         *
         * @return Render Distance Type
         */
        public String getRenderDistanceType() {
          return this.renderDistanceType;
        }

        /**
         * Determines how distance value is used. Fixed distance is measured in blocks. Dynamic distance is multiplied by the current render distance.
         *
         * @param renderDistanceType Render Distance Type
         */
        public void setRenderDistanceType(String renderDistanceType) {
          this.renderDistanceType = renderDistanceType;
        }

        /**
         * Additional fog data which will slowly transition to the distance fog of current biome.
         *
         * @return Transition Fog
         */
        public TransitionFog getTransitionFog() {
          return this.transitionFog;
        }

        /**
         * Additional fog data which will slowly transition to the distance fog of current biome.
         *
         * @param transitionFog Transition Fog
         */
        public void setTransitionFog(TransitionFog transitionFog) {
          this.transitionFog = transitionFog;
        }

        /**
         * Transition Fog
         * <p>
         * Additional fog data which will slowly transition to the distance fog of current biome.
         */
        public static class TransitionFog {
          @JsonProperty("init_fog")
          public InitFog initFog;

          @JsonProperty("min_percent")
          public float minPercent;

          @JsonProperty("mid_seconds")
          public float midSeconds;

          @JsonProperty("mid_percent")
          public float midPercent;

          @JsonProperty("max_seconds")
          public float maxSeconds;

          /**
           * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
           *
           * @return Initial Fog
           */
          public InitFog getInitFog() {
            return this.initFog;
          }

          /**
           * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
           *
           * @param initFog Initial Fog
           */
          public void setInitFog(InitFog initFog) {
            this.initFog = initFog;
          }

          /**
           * The minimum progress of fog transition.
           *
           * @return Minimum Percent
           */
          public float getMinPercent() {
            return this.minPercent;
          }

          /**
           * The minimum progress of fog transition.
           *
           * @param minPercent Minimum Percent
           */
          public void setMinPercent(float minPercent) {
            this.minPercent = minPercent;
          }

          /**
           * The time takes to reach certain progress('mid_percent') of fog transition.
           *
           * @return Midpoint Seconds
           */
          public float getMidSeconds() {
            return this.midSeconds;
          }

          /**
           * The time takes to reach certain progress('mid_percent') of fog transition.
           *
           * @param midSeconds Midpoint Seconds
           */
          public void setMidSeconds(float midSeconds) {
            this.midSeconds = midSeconds;
          }

          /**
           * The progress of fog transition after 'mid_seconds' seconds.
           *
           * @return Midpoint Percent
           */
          public float getMidPercent() {
            return this.midPercent;
          }

          /**
           * The progress of fog transition after 'mid_seconds' seconds.
           *
           * @param midPercent Midpoint Percent
           */
          public void setMidPercent(float midPercent) {
            this.midPercent = midPercent;
          }

          /**
           * Total amount of time takes to complete fog transition.
           *
           * @return Maximum Seconds
           */
          public float getMaxSeconds() {
            return this.maxSeconds;
          }

          /**
           * Total amount of time takes to complete fog transition.
           *
           * @param maxSeconds Maximum Seconds
           */
          public void setMaxSeconds(float maxSeconds) {
            this.maxSeconds = maxSeconds;
          }

          /**
           * Initial Fog
           * <p>
           * Initial fog that will slowly transition into water distance fog of the biome when player goes into water.
           */
          public static class InitFog {
            @JsonProperty("fog_color")
            public String fogColor;

            @JsonProperty("fog_start")
            public float fogStart;

            @JsonProperty("fog_end")
            public float fogEnd;

            /**
             * The color that the fog will take on.
             *
             * @return Fog Color
             */
            public String getFogColor() {
              return this.fogColor;
            }

            /**
             * The color that the fog will take on.
             *
             * @param fogColor Fog Color
             */
            public void setFogColor(String fogColor) {
              this.fogColor = fogColor;
            }

            /**
             * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
             *
             * @return Fog Start
             */
            public float getFogStart() {
              return this.fogStart;
            }

            /**
             * The distance from the player that the fog will begin to appear. 'fog_start' must be less than or equal to 'fog_end'.
             *
             * @param fogStart Fog Start
             */
            public void setFogStart(float fogStart) {
              this.fogStart = fogStart;
            }

            /**
             * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
             *
             * @return Fog End
             */
            public float getFogEnd() {
              return this.fogEnd;
            }

            /**
             * The distance from the player that the fog will become fully opaque. 'fog_end' must be greater than or equal to 'fog_start'.
             *
             * @param fogEnd Fog End
             */
            public void setFogEnd(float fogEnd) {
              this.fogEnd = fogEnd;
            }
          }
        }
      }
    }

    /**
     * Volumetric
     * <p>
     * The volumetric fog settings.
     */
    public static class Volumetric {
      public Density density;

      @JsonProperty("media_coefficients")
      public MediaCoefficients mediaCoefficients;

      /**
       * The density settings for different camera locations.
       *
       * @return Density
       */
      public Density getDensity() {
        return this.density;
      }

      /**
       * The density settings for different camera locations.
       *
       * @param density Density
       */
      public void setDensity(Density density) {
        this.density = density;
      }

      /**
       * The coefficient settings for the volumetric fog in different blocks.
       *
       * @return Media Coefficients
       */
      public MediaCoefficients getMediaCoefficients() {
        return this.mediaCoefficients;
      }

      /**
       * The coefficient settings for the volumetric fog in different blocks.
       *
       * @param mediaCoefficients Media Coefficients
       */
      public void setMediaCoefficients(MediaCoefficients mediaCoefficients) {
        this.mediaCoefficients = mediaCoefficients;
      }

      /**
       * Density
       * <p>
       * The density settings for different camera locations.
       */
      public static class Density {
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

        public static class Air {
          @JsonProperty("max_density")
          public float maxDensity;

          @JsonProperty("max_density_height")
          public float maxDensityHeight;

          @JsonProperty("zero_density_height")
          public float zeroDensityHeight;

          public boolean uniform;

          /**
           * The maximum amount of opaqueness that the ground fog will take on. A value from [0.0, 1.0].
           *
           * @return Maximum Density
           */
          public float getMaxDensity() {
            return this.maxDensity;
          }

          /**
           * The maximum amount of opaqueness that the ground fog will take on. A value from [0.0, 1.0].
           *
           * @param maxDensity Maximum Density
           */
          public void setMaxDensity(float maxDensity) {
            this.maxDensity = maxDensity;
          }

          /**
           * The height in blocks that the ground fog will become it's maximum density.
           *
           * @return Maximum Density Height
           */
          public float getMaxDensityHeight() {
            return this.maxDensityHeight;
          }

          /**
           * The height in blocks that the ground fog will become it's maximum density.
           *
           * @param maxDensityHeight Maximum Density Height
           */
          public void setMaxDensityHeight(float maxDensityHeight) {
            this.maxDensityHeight = maxDensityHeight;
          }

          /**
           * The height in blocks that the ground fog will be completely transparent and begin to appear. This value needs to be at least 1 higher than `max_density_height`.
           *
           * @return Zero Density Height
           */
          public float getZeroDensityHeight() {
            return this.zeroDensityHeight;
          }

          /**
           * The height in blocks that the ground fog will be completely transparent and begin to appear. This value needs to be at least 1 higher than `max_density_height`.
           *
           * @param zeroDensityHeight Zero Density Height
           */
          public void setZeroDensityHeight(float zeroDensityHeight) {
            this.zeroDensityHeight = zeroDensityHeight;
          }

          /**
           * When set to true, the density will be uniform across all heights.
           *
           * @return Uniform
           */
          public boolean getUniform() {
            return this.uniform;
          }

          /**
           * When set to true, the density will be uniform across all heights.
           *
           * @param uniform Uniform
           */
          public void setUniform(boolean uniform) {
            this.uniform = uniform;
          }
        }

        public static class Water {
          @JsonProperty("max_density")
          public float maxDensity;

          @JsonProperty("max_density_height")
          public float maxDensityHeight;

          @JsonProperty("zero_density_height")
          public float zeroDensityHeight;

          public boolean uniform;

          /**
           * The maximum amount of opaqueness that the ground fog will take on. A value from [0.0, 1.0].
           *
           * @return Maximum Density
           */
          public float getMaxDensity() {
            return this.maxDensity;
          }

          /**
           * The maximum amount of opaqueness that the ground fog will take on. A value from [0.0, 1.0].
           *
           * @param maxDensity Maximum Density
           */
          public void setMaxDensity(float maxDensity) {
            this.maxDensity = maxDensity;
          }

          /**
           * The height in blocks that the ground fog will become it's maximum density.
           *
           * @return Maximum Density Height
           */
          public float getMaxDensityHeight() {
            return this.maxDensityHeight;
          }

          /**
           * The height in blocks that the ground fog will become it's maximum density.
           *
           * @param maxDensityHeight Maximum Density Height
           */
          public void setMaxDensityHeight(float maxDensityHeight) {
            this.maxDensityHeight = maxDensityHeight;
          }

          /**
           * The height in blocks that the ground fog will be completely transparent and begin to appear. This value needs to be at least 1 higher than `max_density_height`.
           *
           * @return Zero Density Height
           */
          public float getZeroDensityHeight() {
            return this.zeroDensityHeight;
          }

          /**
           * The height in blocks that the ground fog will be completely transparent and begin to appear. This value needs to be at least 1 higher than `max_density_height`.
           *
           * @param zeroDensityHeight Zero Density Height
           */
          public void setZeroDensityHeight(float zeroDensityHeight) {
            this.zeroDensityHeight = zeroDensityHeight;
          }

          /**
           * When set to true, the density will be uniform across all heights.
           *
           * @return Uniform
           */
          public boolean getUniform() {
            return this.uniform;
          }

          /**
           * When set to true, the density will be uniform across all heights.
           *
           * @param uniform Uniform
           */
          public void setUniform(boolean uniform) {
            this.uniform = uniform;
          }
        }

        public static class Lava {
          @JsonProperty("max_density")
          public float maxDensity;

          @JsonProperty("max_density_height")
          public float maxDensityHeight;

          @JsonProperty("zero_density_height")
          public float zeroDensityHeight;

          public boolean uniform;

          /**
           * The maximum amount of opaqueness that the ground fog will take on. A value from [0.0, 1.0].
           *
           * @return Maximum Density
           */
          public float getMaxDensity() {
            return this.maxDensity;
          }

          /**
           * The maximum amount of opaqueness that the ground fog will take on. A value from [0.0, 1.0].
           *
           * @param maxDensity Maximum Density
           */
          public void setMaxDensity(float maxDensity) {
            this.maxDensity = maxDensity;
          }

          /**
           * The height in blocks that the ground fog will become it's maximum density.
           *
           * @return Maximum Density Height
           */
          public float getMaxDensityHeight() {
            return this.maxDensityHeight;
          }

          /**
           * The height in blocks that the ground fog will become it's maximum density.
           *
           * @param maxDensityHeight Maximum Density Height
           */
          public void setMaxDensityHeight(float maxDensityHeight) {
            this.maxDensityHeight = maxDensityHeight;
          }

          /**
           * The height in blocks that the ground fog will be completely transparent and begin to appear. This value needs to be at least 1 higher than `max_density_height`.
           *
           * @return Zero Density Height
           */
          public float getZeroDensityHeight() {
            return this.zeroDensityHeight;
          }

          /**
           * The height in blocks that the ground fog will be completely transparent and begin to appear. This value needs to be at least 1 higher than `max_density_height`.
           *
           * @param zeroDensityHeight Zero Density Height
           */
          public void setZeroDensityHeight(float zeroDensityHeight) {
            this.zeroDensityHeight = zeroDensityHeight;
          }

          /**
           * When set to true, the density will be uniform across all heights.
           *
           * @return Uniform
           */
          public boolean getUniform() {
            return this.uniform;
          }

          /**
           * When set to true, the density will be uniform across all heights.
           *
           * @param uniform Uniform
           */
          public void setUniform(boolean uniform) {
            this.uniform = uniform;
          }
        }

        public static class LavaResistance {
          @JsonProperty("max_density")
          public float maxDensity;

          @JsonProperty("max_density_height")
          public float maxDensityHeight;

          @JsonProperty("zero_density_height")
          public float zeroDensityHeight;

          public boolean uniform;

          /**
           * The maximum amount of opaqueness that the ground fog will take on. A value from [0.0, 1.0].
           *
           * @return Maximum Density
           */
          public float getMaxDensity() {
            return this.maxDensity;
          }

          /**
           * The maximum amount of opaqueness that the ground fog will take on. A value from [0.0, 1.0].
           *
           * @param maxDensity Maximum Density
           */
          public void setMaxDensity(float maxDensity) {
            this.maxDensity = maxDensity;
          }

          /**
           * The height in blocks that the ground fog will become it's maximum density.
           *
           * @return Maximum Density Height
           */
          public float getMaxDensityHeight() {
            return this.maxDensityHeight;
          }

          /**
           * The height in blocks that the ground fog will become it's maximum density.
           *
           * @param maxDensityHeight Maximum Density Height
           */
          public void setMaxDensityHeight(float maxDensityHeight) {
            this.maxDensityHeight = maxDensityHeight;
          }

          /**
           * The height in blocks that the ground fog will be completely transparent and begin to appear. This value needs to be at least 1 higher than `max_density_height`.
           *
           * @return Zero Density Height
           */
          public float getZeroDensityHeight() {
            return this.zeroDensityHeight;
          }

          /**
           * The height in blocks that the ground fog will be completely transparent and begin to appear. This value needs to be at least 1 higher than `max_density_height`.
           *
           * @param zeroDensityHeight Zero Density Height
           */
          public void setZeroDensityHeight(float zeroDensityHeight) {
            this.zeroDensityHeight = zeroDensityHeight;
          }

          /**
           * When set to true, the density will be uniform across all heights.
           *
           * @return Uniform
           */
          public boolean getUniform() {
            return this.uniform;
          }

          /**
           * When set to true, the density will be uniform across all heights.
           *
           * @param uniform Uniform
           */
          public void setUniform(boolean uniform) {
            this.uniform = uniform;
          }
        }
      }

      /**
       * Media Coefficients
       * <p>
       * The coefficient settings for the volumetric fog in different blocks.
       */
      public static class MediaCoefficients {
        public Air air;

        public Water water;

        public Cloud cloud;

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

        public Cloud getCloud() {
          return this.cloud;
        }

        public void setCloud(Cloud cloud) {
          this.cloud = cloud;
        }

        public static class Air {
        }

        public static class Water {
        }

        public static class Cloud {
        }
      }
    }
  }
}
