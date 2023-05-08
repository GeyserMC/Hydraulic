package org.geysermc.hydraulic.pack.bedrock.resource.items.item;

import java.lang.String;

/**
 * Description
 * <p>
 * The description of an item.
 */
public class Description {
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
