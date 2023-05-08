package org.geysermc.hydraulic.pack.bedrock.resource.attachables;

import org.geysermc.hydraulic.pack.bedrock.resource.attachables.attachable.Description;

/**
 * Attachables
 * <p>
 * The attachables definition.
 */
public class Attachable {
  public Description description;

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
}
