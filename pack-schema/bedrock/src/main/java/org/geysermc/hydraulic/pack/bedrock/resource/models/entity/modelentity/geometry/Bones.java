package org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry.bones.Cubes;
import org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry.bones.Locators;
import org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry.bones.PolyMesh;
import org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry.bones.TextureMeshes;

/**
 * A bones specification.
 */
public class Bones {
  public String binding;

  public List<Cubes> cubes = new ArrayList<>();

  public boolean debug;

  public float inflate;

  private Map<String, Locators> locators = new HashMap<>();

  public boolean mirror;

  public String name;

  public String parent;

  public float[] pivot;

  @JsonProperty("poly_mesh")
  public PolyMesh polyMesh;

  @JsonProperty("render_group_id")
  public int renderGroupId;

  public float[] rotation;

  @JsonProperty("texture_meshes")
  public List<TextureMeshes> textureMeshes = new ArrayList<>();

  /**
   * Molang definition.
   *
   * @return Molang
   */
  public String getBinding() {
    return this.binding;
  }

  /**
   * Molang definition.
   *
   * @param binding Molang
   */
  public void setBinding(String binding) {
    this.binding = binding;
  }

  /**
   * This is the list of cubes associated with this bone.
   *
   * @return Cubes
   */
  public List<Cubes> getCubes() {
    return this.cubes;
  }

  /**
   * This is the list of cubes associated with this bone.
   *
   * @param cubes Cubes
   */
  public void setCubes(List<Cubes> cubes) {
    this.cubes = cubes;
  }

  public boolean getDebug() {
    return this.debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  /**
   * Grow this box by this additive amount in all directions (in model space units).
   */
  public float getInflate() {
    return this.inflate;
  }

  /**
   * Grow this box by this additive amount in all directions (in model space units).
   */
  public void setInflate(float inflate) {
    this.inflate = inflate;
  }

  /**
   * This is a list of locators associated with this bone. A locator is a point in model space that tracks a particular bone as the bone animates (by maintaining it's relationship to the bone through the animation).
   */
  public Map<String, Locators> getLocators() {
    return this.locators;
  }

  /**
   * This is a list of locators associated with this bone. A locator is a point in model space that tracks a particular bone as the bone animates (by maintaining it's relationship to the bone through the animation).
   */
  public void setLocators(Map<String, Locators> locators) {
    this.locators = locators;
  }

  /**
   * Mirrors the UV's of the unrotated cubes along the x axis, also causes the east/west faces to get flipped.
   *
   * @return Mirror
   */
  public boolean getMirror() {
    return this.mirror;
  }

  /**
   * Mirrors the UV's of the unrotated cubes along the x axis, also causes the east/west faces to get flipped.
   *
   * @param mirror Mirror
   */
  public void setMirror(boolean mirror) {
    this.mirror = mirror;
  }

  /**
   * Animation files refer to this bone via this identifier.
   *
   * @return Name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Animation files refer to this bone via this identifier.
   *
   * @param name Name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Bone that this bone is relative to. If the parent bone moves, this bone will move along with it.
   *
   * @return Parent
   */
  public String getParent() {
    return this.parent;
  }

  /**
   * Bone that this bone is relative to. If the parent bone moves, this bone will move along with it.
   *
   * @param parent Parent
   */
  public void setParent(String parent) {
    this.parent = parent;
  }

  /**
   * The bone pivots around this point (in model space units).
   *
   * @return Pivot
   */
  public float[] getPivot() {
    return this.pivot;
  }

  /**
   * The bone pivots around this point (in model space units).
   *
   * @param pivot Pivot
   */
  public void setPivot(float[] pivot) {
    this.pivot = pivot;
  }

  /**
   * ***EXPERIMENTAL*** A triangle or quad mesh object. Can be used in conjunction with cubes and texture geometry.
   */
  public PolyMesh getPolyMesh() {
    return this.polyMesh;
  }

  /**
   * ***EXPERIMENTAL*** A triangle or quad mesh object. Can be used in conjunction with cubes and texture geometry.
   */
  public void setPolyMesh(PolyMesh polyMesh) {
    this.polyMesh = polyMesh;
  }

  public int getRenderGroupId() {
    return this.renderGroupId;
  }

  public void setRenderGroupId(int renderGroupId) {
    this.renderGroupId = renderGroupId;
  }

  /**
   * This is the initial rotation of the bone around the pivot, pre-animation (in degrees, x-then-y-then-z order).
   *
   * @return Rotation
   */
  public float[] getRotation() {
    return this.rotation;
  }

  /**
   * This is the initial rotation of the bone around the pivot, pre-animation (in degrees, x-then-y-then-z order).
   *
   * @param rotation Rotation
   */
  public void setRotation(float[] rotation) {
    this.rotation = rotation;
  }

  /**
   * ***EXPERIMENTAL*** Adds a mesh to the bone's geometry by converting texels in a texture into boxes.
   *
   * @return Texture Meshes
   */
  public List<TextureMeshes> getTextureMeshes() {
    return this.textureMeshes;
  }

  /**
   * ***EXPERIMENTAL*** Adds a mesh to the bone's geometry by converting texels in a texture into boxes.
   *
   * @param textureMeshes Texture Meshes
   */
  public void setTextureMeshes(List<TextureMeshes> textureMeshes) {
    this.textureMeshes = textureMeshes;
  }
}
