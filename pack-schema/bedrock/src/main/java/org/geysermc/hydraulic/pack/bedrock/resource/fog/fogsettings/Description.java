package org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings;

import java.lang.String;

/**
 * Description
 * <p>
 * The identifying description of this fog settings.
 */
public class Description {
  public String identifier;

  /**
   * The identifier for these fog settings. The identifier must include a namespace.
   *
   * @return Identifier
   */
  public String getIdentifier() {
    return this.identifier;
  }

  /**
   * The identifier for these fog settings. The identifier must include a namespace.
   *
   * @param identifier Identifier
   */
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }
}
