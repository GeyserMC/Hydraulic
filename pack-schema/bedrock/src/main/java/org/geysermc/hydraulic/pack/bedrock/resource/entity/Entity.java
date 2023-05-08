package org.geysermc.hydraulic.pack.bedrock.resource.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Actor Entity 1.10.0
 * <p>
 * A client side entity definition.
 */
public class Entity {
  @JsonProperty("format_version")
  public String formatVersion;

  @JsonProperty("minecraft:client_entity")
  public ClientEntity clientEntity;

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
   * The entity description for clientside rendering, animations and models.
   *
   * @return Client Entity
   */
  public ClientEntity getClientEntity() {
    return this.clientEntity;
  }

  /**
   * The entity description for clientside rendering, animations and models.
   *
   * @param clientEntity Client Entity
   */
  public void setClientEntity(ClientEntity clientEntity) {
    this.clientEntity = clientEntity;
  }
}
