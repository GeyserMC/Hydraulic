package org.geysermc.hydraulic.pack.bedrock.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Float;
import java.util.ArrayList;
import java.util.List;
import org.geysermc.hydraulic.pack.bedrock.resource.manifest.Capabilities;
import org.geysermc.hydraulic.pack.bedrock.resource.manifest.Dependencies;
import org.geysermc.hydraulic.pack.bedrock.resource.manifest.Header;
import org.geysermc.hydraulic.pack.bedrock.resource.manifest.Metadata;
import org.geysermc.hydraulic.pack.bedrock.resource.manifest.Modules;
import org.geysermc.hydraulic.pack.bedrock.resource.manifest.Subpacks;

/**
 * Manifest V2 Schema
 * <p>
 * The manifest file contains all the basic information about the pack that Minecraft needs to identify it. The tables below contain all the components of the manifest, their individual properties, and what they mean.
 */
public class Manifest {
  @JsonProperty("format_version")
  public Float formatVersion;

  public Capabilities capabilities;

  public List<Dependencies> dependencies = new ArrayList<>();

  public Header header;

  public List<Modules> modules = new ArrayList<>();

  public Metadata metadata;

  public List<Subpacks> subpacks = new ArrayList<>();

  /**
   * This defines the current version of the manifest. Don't change this unless you have a good reason to
   *
   * @return Format Version
   */
  public Float getFormatVersion() {
    return this.formatVersion;
  }

  /**
   * This defines the current version of the manifest. Don't change this unless you have a good reason to
   *
   * @param formatVersion Format Version
   */
  public void setFormatVersion(float formatVersion) {
    this.formatVersion = formatVersion;
  }

  /**
   * These are the different features that the pack makes use of that aren't necessarily enabled by default.
   *
   * @return Capabilities
   */
  public Capabilities getCapabilities() {
    return this.capabilities;
  }

  /**
   * These are the different features that the pack makes use of that aren't necessarily enabled by default.
   *
   * @param capabilities Capabilities
   */
  public void setCapabilities(Capabilities capabilities) {
    this.capabilities = capabilities;
  }

  /**
   * Section containing definitions for any other packs or modules that are required in order for this manifest.json file to work.
   *
   * @return Dependencies
   */
  public List<Dependencies> getDependencies() {
    return this.dependencies;
  }

  /**
   * Section containing definitions for any other packs or modules that are required in order for this manifest.json file to work.
   *
   * @param dependencies Dependencies
   */
  public void setDependencies(List<Dependencies> dependencies) {
    this.dependencies = dependencies;
  }

  /**
   * Section containing information regarding the name of the pack, description, and other features that are public facing.
   *
   * @return Header
   */
  public Header getHeader() {
    return this.header;
  }

  /**
   * Section containing information regarding the name of the pack, description, and other features that are public facing.
   *
   * @param header Header
   */
  public void setHeader(Header header) {
    this.header = header;
  }

  /**
   * Section containing information regarding the type of content that is being brought in.
   *
   * @return Modules
   */
  public List<Modules> getModules() {
    return this.modules;
  }

  /**
   * Section containing information regarding the type of content that is being brought in.
   *
   * @param modules Modules
   */
  public void setModules(List<Modules> modules) {
    this.modules = modules;
  }

  /**
   * Section containing the metadata about the file such as authors and licensing information.
   *
   * @return Metadata
   */
  public Metadata getMetadata() {
    return this.metadata;
  }

  /**
   * Section containing the metadata about the file such as authors and licensing information.
   *
   * @param metadata Metadata
   */
  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }

  /**
   * A list of subpacks that are applied per memory tier.
   *
   * @return Subpacks
   */
  public List<Subpacks> getSubpacks() {
    return this.subpacks;
  }

  /**
   * A list of subpacks that are applied per memory tier.
   *
   * @param subpacks Subpacks
   */
  public void setSubpacks(List<Subpacks> subpacks) {
    this.subpacks = subpacks;
  }
}
