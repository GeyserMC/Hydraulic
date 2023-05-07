package org.geysermc.hydraulic.pack.bedrock.resource.items;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Item
 * <p>
 * Minecraft items 1.10.0
 */
public class Items {
  @JsonProperty("format_version")
  public String formatVersion;

  @JsonProperty("minecraft:item")
  public Item item;

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
   * A resource pack definition of an item.
   *
   * @return Item
   */
  public Item getItem() {
    return this.item;
  }

  /**
   * A resource pack definition of an item.
   *
   * @param item Item
   */
  public void setItem(Item item) {
    this.item = item;
  }

  /**
   * Item
   * <p>
   * A resource pack definition of an item.
   */
  public static class Item {
    public Description description;

    public Components components;

    /**
     * The description of an item.
     *
     * @return Description
     */
    public Description getDescription() {
      return this.description;
    }

    /**
     * The description of an item.
     *
     * @param description Description
     */
    public void setDescription(Description description) {
      this.description = description;
    }

    /**
     * The components that describe this item.
     *
     * @return Components
     */
    public Components getComponents() {
      return this.components;
    }

    /**
     * The components that describe this item.
     *
     * @param components Components
     */
    public void setComponents(Components components) {
      this.components = components;
    }

    /**
     * Description
     * <p>
     * The description of an item.
     */
    public static class Description {
      public String identifier;

      public String category;

      /**
       * A minecraft item identifier.
       *
       * @return Item Identifier
       */
      public String getIdentifier() {
        return this.identifier;
      }

      /**
       * A minecraft item identifier.
       *
       * @param identifier Item Identifier
       */
      public void setIdentifier(String identifier) {
        this.identifier = identifier;
      }

      /**
       * The category this item belongs in.
       *
       * @return Category
       */
      public String getCategory() {
        return this.category;
      }

      /**
       * The category this item belongs in.
       *
       * @param category Category
       */
      public void setCategory(String category) {
        this.category = category;
      }
    }

    /**
     * Components
     * <p>
     * The components that describe this item.
     */
    public static class Components {
      @JsonProperty("minecraft:icon")
      public String icon;

      @JsonProperty("minecraft:render_offsets")
      public String renderOffsets;

      /**
       * The texture defined in `textures/item_texture.json`
       *
       * @return Icon
       */
      public String getIcon() {
        return this.icon;
      }

      /**
       * The texture defined in `textures/item_texture.json`
       *
       * @param icon Icon
       */
      public void setIcon(String icon) {
        this.icon = icon;
      }

      /**
       * The render offset used for the item.
       *
       * @return Render Offsets
       */
      public String getRenderOffsets() {
        return this.renderOffsets;
      }

      /**
       * The render offset used for the item.
       *
       * @param renderOffsets Render Offsets
       */
      public void setRenderOffsets(String renderOffsets) {
        this.renderOffsets = renderOffsets;
      }
    }
  }
}
