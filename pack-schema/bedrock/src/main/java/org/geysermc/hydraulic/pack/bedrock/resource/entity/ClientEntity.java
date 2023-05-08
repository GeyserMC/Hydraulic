package org.geysermc.hydraulic.pack.bedrock.resource.entity;

import org.geysermc.hydraulic.pack.bedrock.resource.entity.cliententity.Description;

/**
 * Client Entity
 * <p>
 * The entity description for clientside rendering, animations and models.
 */
public class ClientEntity {
  public Description description;

  /**
   * The entity description for clientside rendering, animations and models.
   *
   * @return Description
   */
  public Description getDescription() {
    return this.description;
  }

  /**
   * The entity description for clientside rendering, animations and models.
   *
   * @param description Description
   */
  public void setDescription(Description description) {
    this.description = description;
  }
}
