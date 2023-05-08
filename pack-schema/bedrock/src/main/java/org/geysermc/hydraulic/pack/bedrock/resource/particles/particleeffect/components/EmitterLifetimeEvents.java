package org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Object;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.components.emitterlifetimeevents.LoopingTravelDistanceEvents;

/**
 * Emitter Lifetime Events Component For 1.10.0
 */
public class EmitterLifetimeEvents {
  @JsonProperty("creation_event")
  public String creationEvent;

  @JsonProperty("expiration_event")
  public String expirationEvent;

  private Map<String, Object> timeline = new HashMap<>();

  @JsonProperty("looping_travel_distance_events")
  public List<LoopingTravelDistanceEvents> loopingTravelDistanceEvents = new ArrayList<>();

  public String getCreationEvent() {
    return this.creationEvent;
  }

  public void setCreationEvent(String creationEvent) {
    this.creationEvent = creationEvent;
  }

  public String getExpirationEvent() {
    return this.expirationEvent;
  }

  public void setExpirationEvent(String expirationEvent) {
    this.expirationEvent = expirationEvent;
  }

  /**
   * A series of times, e.g. 0.0 or 1.0, that trigger the event, these get fired on every loop the emitter goes through, `time` is the time, e.g. one line might be: `0.4`: `event`
   *
   * @return Timeline
   */
  public Map<String, Object> getTimeline() {
    return this.timeline;
  }

  /**
   * A series of times, e.g. 0.0 or 1.0, that trigger the event, these get fired on every loop the emitter goes through, `time` is the time, e.g. one line might be: `0.4`: `event`
   *
   * @param timeline Timeline
   */
  public void setTimeline(Map<String, Object> timeline) {
    this.timeline = timeline;
  }

  /**
   * A series of events that occur at set intervals these get fired every time the emitter has moved the specified input distance from the last time it was fired.
   *
   * @return Looping Travel Distance Events
   */
  public List<LoopingTravelDistanceEvents> getLoopingTravelDistanceEvents() {
    return this.loopingTravelDistanceEvents;
  }

  /**
   * A series of events that occur at set intervals these get fired every time the emitter has moved the specified input distance from the last time it was fired.
   *
   * @param loopingTravelDistanceEvents Looping Travel Distance Events
   */
  public void setLoopingTravelDistanceEvents(
      List<LoopingTravelDistanceEvents> loopingTravelDistanceEvents) {
    this.loopingTravelDistanceEvents = loopingTravelDistanceEvents;
  }
}
