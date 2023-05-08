package org.geysermc.hydraulic.pack.bedrock.resource.particles;

import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.Components;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.Curves;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.Description;
import org.geysermc.hydraulic.pack.bedrock.resource.particles.particleeffect.Events;

/**
 * Particle Effect
 */
public class ParticleEffect {
  public Description description;

  private Map<String, Curves> curves = new HashMap<>();

  public Components components;

  private Map<String, Events> events = new HashMap<>();

  /**
   * @return Description
   */
  public Description getDescription() {
    return this.description;
  }

  /**
   * @param description Description
   */
  public void setDescription(Description description) {
    this.description = description;
  }

  /**
   * Curves are interpolation values, with inputs from 0 to 1, and outputs based on the curve. The result of the curve is a Molang variable of the same name that can be referenced in Molang in components. For each rendering frame for each particle, the curves are evaluated and the result is placed in a Molang variable of the name of the curve.
   *
   * @return Curves
   */
  public Map<String, Curves> getCurves() {
    return this.curves;
  }

  /**
   * Curves are interpolation values, with inputs from 0 to 1, and outputs based on the curve. The result of the curve is a Molang variable of the same name that can be referenced in Molang in components. For each rendering frame for each particle, the curves are evaluated and the result is placed in a Molang variable of the name of the curve.
   *
   * @param curves Curves
   */
  public void setCurves(Map<String, Curves> curves) {
    this.curves = curves;
  }

  /**
   * The particle components.
   *
   * @return Components
   */
  public Components getComponents() {
    return this.components;
  }

  /**
   * The particle components.
   *
   * @param components Components
   */
  public void setComponents(Components components) {
    this.components = components;
  }

  /**
   * @return Events
   */
  public Map<String, Events> getEvents() {
    return this.events;
  }

  /**
   * @param events Events
   */
  public void setEvents(Map<String, Events> events) {
    this.events = events;
  }
}
