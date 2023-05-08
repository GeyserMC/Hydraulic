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
}
