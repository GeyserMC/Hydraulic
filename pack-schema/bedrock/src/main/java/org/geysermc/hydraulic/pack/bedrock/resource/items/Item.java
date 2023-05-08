package org.geysermc.hydraulic.pack.bedrock.resource.items;

import org.geysermc.hydraulic.pack.bedrock.resource.items.item.Components;
import org.geysermc.hydraulic.pack.bedrock.resource.items.item.Description;

/**
 * Item
 * <p>
 * A resource pack definition of an item.
 */
public class Item {
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
}
