package org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry.bones;

import org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry.bones.cubes.Uv;

/**
 * A single cube.
 */
public class Cubes {
  public float inflate;

  public boolean mirror;

  public float[] origin;

  public float[] pivot;

  public boolean reset;

  public float[] rotation;

  public float[] size;

  public Uv uv;

  /**
   * Grow this box by this additive amount in all directions (in model space units), this field overrides the bone's inflate field for this cube only.
   */
  public float getInflate() {
    return this.inflate;
  }

  /**
   * Grow this box by this additive amount in all directions (in model space units), this field overrides the bone's inflate field for this cube only.
   */
  public void setInflate(float inflate) {
    this.inflate = inflate;
  }

  /**
   * Mirrors this cube about the unrotated x axis (effectively flipping the east / west faces), overriding the bone's `mirror` setting for this cube.
   */
  public boolean getMirror() {
    return this.mirror;
  }

  /**
   * Mirrors this cube about the unrotated x axis (effectively flipping the east / west faces), overriding the bone's `mirror` setting for this cube.
   */
  public void setMirror(boolean mirror) {
    this.mirror = mirror;
  }

  public float[] getOrigin() {
    return this.origin;
  }

  public void setOrigin(float[] origin) {
    this.origin = origin;
  }

  /**
   * If this field is specified, rotation of this cube occurs around this point, otherwise its rotation is around the center of the box. Note that in 1.12 this is flipped upside-down, but is fixed in 1.14.
   *
   * @return Pivot
   */
  public float[] getPivot() {
    return this.pivot;
  }

  /**
   * If this field is specified, rotation of this cube occurs around this point, otherwise its rotation is around the center of the box. Note that in 1.12 this is flipped upside-down, but is fixed in 1.14.
   *
   * @param pivot Pivot
   */
  public void setPivot(float[] pivot) {
    this.pivot = pivot;
  }

  /**
   * @return Reset
   */
  public boolean getReset() {
    return this.reset;
  }

  /**
   * @param reset Reset
   */
  public void setReset(boolean reset) {
    this.reset = reset;
  }

  /**
   * @return Rotation
   */
  public float[] getRotation() {
    return this.rotation;
  }

  /**
   * @param rotation Rotation
   */
  public void setRotation(float[] rotation) {
    this.rotation = rotation;
  }

  /**
   * The cube extends this amount relative to its origin (in model space units).
   *
   * @return Size
   */
  public float[] getSize() {
    return this.size;
  }

  /**
   * The cube extends this amount relative to its origin (in model space units).
   *
   * @param size Size
   */
  public void setSize(float[] size) {
    this.size = size;
  }

  public Uv getUv() {
    return this.uv;
  }

  public void setUv(Uv uv) {
    this.uv = uv;
  }
}
