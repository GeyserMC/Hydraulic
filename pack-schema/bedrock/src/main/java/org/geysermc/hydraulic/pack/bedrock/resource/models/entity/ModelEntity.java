package org.geysermc.hydraulic.pack.bedrock.resource.models.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Object;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Geometry 1.16.0
 * <p>
 * The minecraft resourcepack model schema for 1.16.0
 */
public class ModelEntity {
  public boolean debug;

  @JsonProperty("format_version")
  public String formatVersion;

  @JsonProperty("minecraft:geometry")
  public List<Geometry> geometry = new ArrayList<>();

  /**
   * @return Debug
   */
  public boolean getDebug() {
    return this.debug;
  }

  /**
   * @param debug Debug
   */
  public void setDebug(boolean debug) {
    this.debug = debug;
  }

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
   * The collection of geometries.
   *
   * @return Geometry
   */
  public List<Geometry> getGeometry() {
    return this.geometry;
  }

  /**
   * The collection of geometries.
   *
   * @param geometry Geometry
   */
  public void setGeometry(List<Geometry> geometry) {
    this.geometry = geometry;
  }

  /**
   * Model
   * <p>
   * Model specification.
   */
  public static class Geometry {
    public Description description;

    public List<Bones> bones = new ArrayList<>();

    public String cape;

    /**
     * The descriptions of the geometry.
     *
     * @return Description
     */
    public Description getDescription() {
      return this.description;
    }

    /**
     * The descriptions of the geometry.
     *
     * @param description Description
     */
    public void setDescription(Description description) {
      this.description = description;
    }

    /**
     * Bones define the `skeleton` of the mob: the parts that can be animated, and to which geometry and other bones are attached.
     *
     * @return Bones
     */
    public List<Bones> getBones() {
      return this.bones;
    }

    /**
     * Bones define the `skeleton` of the mob: the parts that can be animated, and to which geometry and other bones are attached.
     *
     * @param bones Bones
     */
    public void setBones(List<Bones> bones) {
      this.bones = bones;
    }

    /**
     * @return Cape
     */
    public String getCape() {
      return this.cape;
    }

    /**
     * @param cape Cape
     */
    public void setCape(String cape) {
      this.cape = cape;
    }

    /**
     * Description
     * <p>
     * The descriptions of the geometry.
     */
    public static class Description {
      public String identifier;

      @JsonProperty("texture_width")
      public float textureWidth;

      @JsonProperty("texture_height")
      public float textureHeight;

      @JsonProperty("visible_bounds_offset")
      public float[] visibleBoundsOffset;

      @JsonProperty("visible_bounds_width")
      public float visibleBoundsWidth;

      @JsonProperty("visible_bounds_height")
      public float visibleBoundsHeight;

      /**
       * Entity definition and Client Block definition files refer to this geometry via this identifier.
       *
       * @return Identifier
       */
      public String getIdentifier() {
        return this.identifier;
      }

      /**
       * Entity definition and Client Block definition files refer to this geometry via this identifier.
       *
       * @param identifier Identifier
       */
      public void setIdentifier(String identifier) {
        this.identifier = identifier;
      }

      /**
       * Assumed width in texels of the texture that will be bound to this geometry.
       *
       * @return Texture Width
       */
      public float getTextureWidth() {
        return this.textureWidth;
      }

      /**
       * Assumed width in texels of the texture that will be bound to this geometry.
       *
       * @param textureWidth Texture Width
       */
      public void setTextureWidth(float textureWidth) {
        this.textureWidth = textureWidth;
      }

      /**
       * Assumed height in texels of the texture that will be bound to this geometry.
       *
       * @return Texture Height
       */
      public float getTextureHeight() {
        return this.textureHeight;
      }

      /**
       * Assumed height in texels of the texture that will be bound to this geometry.
       *
       * @param textureHeight Texture Height
       */
      public void setTextureHeight(float textureHeight) {
        this.textureHeight = textureHeight;
      }

      /**
       * Offset of the visibility bounding box from the entity location point (in model space units).
       *
       * @return Visible Bounds Offset
       */
      public float[] getVisibleBoundsOffset() {
        return this.visibleBoundsOffset;
      }

      /**
       * Offset of the visibility bounding box from the entity location point (in model space units).
       *
       * @param visibleBoundsOffset Visible Bounds Offset
       */
      public void setVisibleBoundsOffset(float[] visibleBoundsOffset) {
        this.visibleBoundsOffset = visibleBoundsOffset;
      }

      /**
       * Width of the visibility bounding box (in model space units).
       *
       * @return Visible Bounds Width
       */
      public float getVisibleBoundsWidth() {
        return this.visibleBoundsWidth;
      }

      /**
       * Width of the visibility bounding box (in model space units).
       *
       * @param visibleBoundsWidth Visible Bounds Width
       */
      public void setVisibleBoundsWidth(float visibleBoundsWidth) {
        this.visibleBoundsWidth = visibleBoundsWidth;
      }

      /**
       * Height of the visible bounding box (in model space units).
       *
       * @return Visible Bounds Height
       */
      public float getVisibleBoundsHeight() {
        return this.visibleBoundsHeight;
      }

      /**
       * Height of the visible bounding box (in model space units).
       *
       * @param visibleBoundsHeight Visible Bounds Height
       */
      public void setVisibleBoundsHeight(float visibleBoundsHeight) {
        this.visibleBoundsHeight = visibleBoundsHeight;
      }
    }

    /**
     * A bones specification.
     */
    public static class Bones {
      public String binding;

      public List<Cubes> cubes = new ArrayList<>();

      public boolean debug;

      public float inflate;

      private Map<String, Object> locators = new HashMap<>();

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
      public Map<String, Object> getLocators() {
        return this.locators;
      }

      /**
       * This is a list of locators associated with this bone. A locator is a point in model space that tracks a particular bone as the bone animates (by maintaining it's relationship to the bone through the animation).
       */
      public void setLocators(Map<String, Object> locators) {
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

      /**
       * A single cube.
       */
      public static class Cubes {
        public float inflate;

        public boolean mirror;

        public float[] origin;

        public float[] pivot;

        public boolean reset;

        public float[] rotation;

        public float[] size;

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
      }

      /**
       * ***EXPERIMENTAL*** A triangle or quad mesh object. Can be used in conjunction with cubes and texture geometry.
       */
      public static class PolyMesh {
        @JsonProperty("normalized_uvs")
        public boolean normalizedUvs;

        /**
         * If true, UVs are assumed to be [0-1]. If false, UVs are assumed to be [0-texture_width] and [0-texture_height] respectively.
         */
        public boolean getNormalizedUvs() {
          return this.normalizedUvs;
        }

        /**
         * If true, UVs are assumed to be [0-1]. If false, UVs are assumed to be [0-texture_width] and [0-texture_height] respectively.
         */
        public void setNormalizedUvs(boolean normalizedUvs) {
          this.normalizedUvs = normalizedUvs;
        }
      }

      public static class TextureMeshes {
        @JsonProperty("local_pivot")
        public float[] localPivot;

        public float[] position;

        public float[] rotation;

        public float[] scale;

        public String texture;

        /**
         * The pivot point on the texture (in *texture space* not entity or bone space) of the texture geometry.
         */
        public float[] getLocalPivot() {
          return this.localPivot;
        }

        /**
         * The pivot point on the texture (in *texture space* not entity or bone space) of the texture geometry.
         */
        public void setLocalPivot(float[] localPivot) {
          this.localPivot = localPivot;
        }

        /**
         * The position of the pivot point after rotation (in *entity space* not texture or bone space) of the texture geometry.
         */
        public float[] getPosition() {
          return this.position;
        }

        /**
         * The position of the pivot point after rotation (in *entity space* not texture or bone space) of the texture geometry.
         */
        public void setPosition(float[] position) {
          this.position = position;
        }

        /**
         * The rotation (in degrees) of the texture geometry relative to the offset.
         */
        public float[] getRotation() {
          return this.rotation;
        }

        /**
         * The rotation (in degrees) of the texture geometry relative to the offset.
         */
        public void setRotation(float[] rotation) {
          this.rotation = rotation;
        }

        /**
         * The scale (in degrees) of the texture geometry relative to the offset.
         */
        public float[] getScale() {
          return this.scale;
        }

        /**
         * The scale (in degrees) of the texture geometry relative to the offset.
         */
        public void setScale(float[] scale) {
          this.scale = scale;
        }

        /**
         * The friendly-named texture to use.
         */
        public String getTexture() {
          return this.texture;
        }

        /**
         * The friendly-named texture to use.
         */
        public void setTexture(String texture) {
          this.texture = texture;
        }
      }
    }
  }
}
