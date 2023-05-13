package org.geysermc.hydraulic.pack.bedrock.resource.render_controllers.rendercontrollers;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * Render Controllers
 * <p>
 * The collection of render controllers, each property is the identifier of a render controller.
 */
public class RenderControllers {
  public Arrays arrays;

  public Color color;

  @JsonProperty("filter_lighting")
  public boolean filterLighting;

  public String geometry;

  @JsonProperty("ignore_lighting")
  public boolean ignoreLighting;

  @JsonProperty("is_hurt_color")
  public IsHurtColor isHurtColor;

  @JsonProperty("light_color_multiplier")
  public String lightColorMultiplier;

  private Map<String, String> materials = new HashMap<>();

  @JsonProperty("on_fire_color")
  public OnFireColor onFireColor;

  @JsonProperty("overlay_color")
  public OverlayColor overlayColor;

  @JsonProperty("part_visibility")
  private Map<String, String> partVisibility = new HashMap<>();

  @JsonProperty("uv_anim")
  public UvAnim uvAnim;

  /**
   * A collection of definition of arrays.
   *
   * @return Arrays
   */
  public Arrays getArrays() {
    return this.arrays;
  }

  /**
   * A collection of definition of arrays.
   *
   * @param arrays Arrays
   */
  public void setArrays(Arrays arrays) {
    this.arrays = arrays;
  }

  /**
   * The color to apply.
   *
   * @return Color
   */
  public Color getColor() {
    return this.color;
  }

  /**
   * The color to apply.
   *
   * @param color Color
   */
  public void setColor(Color color) {
    this.color = color;
  }

  /**
   * Whenever or not to apply enviroment lighting to this object.
   *
   * @return Filter Lighting
   */
  public boolean getFilterLighting() {
    return this.filterLighting;
  }

  /**
   * Whenever or not to apply enviroment lighting to this object.
   *
   * @param filterLighting Filter Lighting
   */
  public void setFilterLighting(boolean filterLighting) {
    this.filterLighting = filterLighting;
  }

  /**
   * Molang definition.
   *
   * @return Molang
   */
  public String getGeometry() {
    return this.geometry;
  }

  /**
   * Molang definition.
   *
   * @param geometry Molang
   */
  public void setGeometry(String geometry) {
    this.geometry = geometry;
  }

  /**
   * Whenever or not to apply enviroment lighting to this object.
   *
   * @return Ignore Lighting
   */
  public boolean getIgnoreLighting() {
    return this.ignoreLighting;
  }

  /**
   * Whenever or not to apply enviroment lighting to this object.
   *
   * @param ignoreLighting Ignore Lighting
   */
  public void setIgnoreLighting(boolean ignoreLighting) {
    this.ignoreLighting = ignoreLighting;
  }

  /**
   * The color to overlay on the entity when hurt.
   *
   * @return Is Hurt Color
   */
  public IsHurtColor getIsHurtColor() {
    return this.isHurtColor;
  }

  /**
   * The color to overlay on the entity when hurt.
   *
   * @param isHurtColor Is Hurt Color
   */
  public void setIsHurtColor(IsHurtColor isHurtColor) {
    this.isHurtColor = isHurtColor;
  }

  public String getLightColorMultiplier() {
    return this.lightColorMultiplier;
  }

  public void setLightColorMultiplier(String lightColorMultiplier) {
    this.lightColorMultiplier = lightColorMultiplier;
  }

  /**
   * The specification where to apply materials to.
   *
   * @return Materials
   */
  public Map<String, String> getMaterials() {
    return this.materials;
  }

  /**
   * The specification where to apply materials to.
   *
   * @param materials Materials
   */
  public void setMaterials(Map<String, String> materials) {
    this.materials = materials;
  }

  /**
   * The color that will be overlayed when the object is on fire.
   *
   * @return On Fire Color
   */
  public OnFireColor getOnFireColor() {
    return this.onFireColor;
  }

  /**
   * The color that will be overlayed when the object is on fire.
   *
   * @param onFireColor On Fire Color
   */
  public void setOnFireColor(OnFireColor onFireColor) {
    this.onFireColor = onFireColor;
  }

  /**
   * The color to put over the object.
   *
   * @return Overlay Color
   */
  public OverlayColor getOverlayColor() {
    return this.overlayColor;
  }

  /**
   * The color to put over the object.
   *
   * @param overlayColor Overlay Color
   */
  public void setOverlayColor(OverlayColor overlayColor) {
    this.overlayColor = overlayColor;
  }

  /**
   * Determines what part of the object to show or hide.
   *
   * @return Part Visibility
   */
  public Map<String, String> getPartVisibility() {
    return this.partVisibility;
  }

  /**
   * Determines what part of the object to show or hide.
   *
   * @param partVisibility Part Visibility
   */
  public void setPartVisibility(Map<String, String> partVisibility) {
    this.partVisibility = partVisibility;
  }

  /**
   * The UV animation to apply to the render texture.
   *
   * @return Uv Anim
   */
  public UvAnim getUvAnim() {
    return this.uvAnim;
  }

  /**
   * The UV animation to apply to the render texture.
   *
   * @param uvAnim Uv Anim
   */
  public void setUvAnim(UvAnim uvAnim) {
    this.uvAnim = uvAnim;
  }
}
