package org.geysermc.hydraulic.pack.bedrock.resource.manifest;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

/**
 * Header
 * <p>
 * Section containing information regarding the name of the pack, description, and other features that are public facing.
 */
public class Header {
  @JsonProperty("base_game_version")
  public float[] baseGameVersion;

  public String description;

  @JsonProperty("lock_template_options")
  public boolean lockTemplateOptions;

  @JsonProperty("min_engine_version")
  public float[] minEngineVersion;

  public String name;

  public String uuid;

  public float[] version;

  /**
   * A version made of 3 numbers.
   *
   * @return Version Numbering
   */
  public float[] getBaseGameVersion() {
    return this.baseGameVersion;
  }

  /**
   * A version made of 3 numbers.
   *
   * @param baseGameVersion Version Numbering
   */
  public void setBaseGameVersion(float[] baseGameVersion) {
    this.baseGameVersion = baseGameVersion;
  }

  /**
   * This is a short description of the pack. It will appear in the game below the name of the pack. We recommend keeping it to 1-2 lines.
   *
   * @return Description
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * This is a short description of the pack. It will appear in the game below the name of the pack. We recommend keeping it to 1-2 lines.
   *
   * @param description Description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * This option is required for any world templates. This will lock the player from modifying the options of the world.
   *
   * @return Lock Template Options
   */
  public boolean getLockTemplateOptions() {
    return this.lockTemplateOptions;
  }

  /**
   * This option is required for any world templates. This will lock the player from modifying the options of the world.
   *
   * @param lockTemplateOptions Lock Template Options
   */
  public void setLockTemplateOptions(boolean lockTemplateOptions) {
    this.lockTemplateOptions = lockTemplateOptions;
  }

  /**
   * A version made of 3 numbers.
   *
   * @return Version Numbering
   */
  public float[] getMinEngineVersion() {
    return this.minEngineVersion;
  }

  /**
   * A version made of 3 numbers.
   *
   * @param minEngineVersion Version Numbering
   */
  public void setMinEngineVersion(float[] minEngineVersion) {
    this.minEngineVersion = minEngineVersion;
  }

  /**
   * This is the name of the pack as it appears within Minecraft. This is a required field.
   *
   * @return Name
   */
  public String getName() {
    return this.name;
  }

  /**
   * This is the name of the pack as it appears within Minecraft. This is a required field.
   *
   * @param name Name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * A valid UUID v4.
   *
   * @return An UUID V4
   */
  public String getUuid() {
    return this.uuid;
  }

  /**
   * A valid UUID v4.
   *
   * @param uuid An UUID V4
   */
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  /**
   * A version made of 3 numbers.
   *
   * @return Version Numbering
   */
  public float[] getVersion() {
    return this.version;
  }

  /**
   * A version made of 3 numbers.
   *
   * @param version Version Numbering
   */
  public void setVersion(float[] version) {
    this.version = version;
  }
}
