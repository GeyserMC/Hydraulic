package org.geysermc.hydraulic.pack.bedrock.resource.manifest;

import java.lang.String;

/**
 * Module
 * <p>
 * Section containing information regarding the type of content that is being brought in.
 */
public class Modules {
  public String description;

  public String type;

  public String language;

  public String uuid;

  public float[] version;

  public String entry;

  /**
   * This is a short description of the module. This is not user-facing at the moment but is a good place to remind yourself why the module is defined
   *
   * @return Description
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * This is a short description of the module. This is not user-facing at the moment but is a good place to remind yourself why the module is defined
   *
   * @param description Description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * This is the type of the module.
   *
   * @return Type
   */
  public String getType() {
    return this.type;
  }

  /**
   * This is the type of the module.
   *
   * @param type Type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * The programming language to use.
   *
   * @return Language
   */
  public String getLanguage() {
    return this.language;
  }

  /**
   * The programming language to use.
   *
   * @param language Language
   */
  public void setLanguage(String language) {
    this.language = language;
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

  /**
   * The javascript entry point for tests, only works if types has been set to `javascript`.
   *
   * @return Entry
   */
  public String getEntry() {
    return this.entry;
  }

  /**
   * The javascript entry point for tests, only works if types has been set to `javascript`.
   *
   * @param entry Entry
   */
  public void setEntry(String entry) {
    this.entry = entry;
  }
}
