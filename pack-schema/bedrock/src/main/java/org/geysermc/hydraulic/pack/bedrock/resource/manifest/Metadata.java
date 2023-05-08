package org.geysermc.hydraulic.pack.bedrock.resource.manifest;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * Metadata
 * <p>
 * Section containing the metadata about the file such as authors and licensing information.
 */
public class Metadata {
  public String[] authors;

  public String license;

  public String url;

  @JsonProperty("generated_with")
  private Map<String, String[]> generatedWith = new HashMap<>();

  /**
   * Name of the author(s) of the pack.
   *
   * @return Authors
   */
  public String[] getAuthors() {
    return this.authors;
  }

  /**
   * Name of the author(s) of the pack.
   *
   * @param authors Authors
   */
  public void setAuthors(String[] authors) {
    this.authors = authors;
  }

  /**
   * The license of the pack.
   *
   * @return License
   */
  public String getLicense() {
    return this.license;
  }

  /**
   * The license of the pack.
   *
   * @param license License
   */
  public void setLicense(String license) {
    this.license = license;
  }

  /**
   * The home website of your pack.
   *
   * @return Url
   */
  public String getUrl() {
    return this.url;
  }

  /**
   * The home website of your pack.
   *
   * @param url Url
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * A list of tools and their version that have modified this pack.
   *
   * @return Generated With
   */
  public Map<String, String[]> getGeneratedWith() {
    return this.generatedWith;
  }

  /**
   * A list of tools and their version that have modified this pack.
   *
   * @param generatedWith Generated With
   */
  public void setGeneratedWith(Map<String, String[]> generatedWith) {
    this.generatedWith = generatedWith;
  }
}
