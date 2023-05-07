package org.geysermc.hydraulic.pack.bedrock.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manifest V2 Schema
 * <p>
 * The manifest file contains all the basic information about the pack that Minecraft needs to identify it. The tables below contain all the components of the manifest, their individual properties, and what they mean.
 */
public class Manifest {
  @JsonProperty("format_version")
  public float formatVersion;

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
  public float getFormatVersion() {
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

  /**
   * Capabilities
   * <p>
   * These are the different features that the pack makes use of that aren't necessarily enabled by default.
   */
  public static class Capabilities {
    @JsonProperty("experimental_custom_ui")
    public boolean experimentalCustomUi;

    public boolean chemistry;

    public boolean raytraced;

    /**
     * Allows HTML files in the pack to be used for custom UI, and scripts in the pack to call and manipulate custom UI.
     *
     * @return Experimental Custom Ui
     */
    public boolean getExperimentalCustomUi() {
      return this.experimentalCustomUi;
    }

    /**
     * Allows HTML files in the pack to be used for custom UI, and scripts in the pack to call and manipulate custom UI.
     *
     * @param experimentalCustomUi Experimental Custom Ui
     */
    public void setExperimentalCustomUi(boolean experimentalCustomUi) {
      this.experimentalCustomUi = experimentalCustomUi;
    }

    /**
     * Allows the pack to add, change or replace Chemistry functionality.
     *
     * @return Chemistry
     */
    public boolean getChemistry() {
      return this.chemistry;
    }

    /**
     * Allows the pack to add, change or replace Chemistry functionality.
     *
     * @param chemistry Chemistry
     */
    public void setChemistry(boolean chemistry) {
      this.chemistry = chemistry;
    }

    /**
     * Indicates that this pack contains Raytracing Enhanced or Physical Based Materials for rendering.
     *
     * @return Raytraced
     */
    public boolean getRaytraced() {
      return this.raytraced;
    }

    /**
     * Indicates that this pack contains Raytracing Enhanced or Physical Based Materials for rendering.
     *
     * @param raytraced Raytraced
     */
    public void setRaytraced(boolean raytraced) {
      this.raytraced = raytraced;
    }
  }

  /**
   * Dependency
   * <p>
   * Section containing definitions for any other packs that are required in order for this manifest.json file to work.
   */
  public static class Dependencies {
    public String uuid;

    public float[] version;

    /**
     * This is the unique identifier of the pack that this pack depends on. It needs to be the exact same UUID that the pack has defined in the header section of it's manifest file
     *
     * @return Uuid
     */
    public String getUuid() {
      return this.uuid;
    }

    /**
     * This is the unique identifier of the pack that this pack depends on. It needs to be the exact same UUID that the pack has defined in the header section of it's manifest file
     *
     * @param uuid Uuid
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

  /**
   * Header
   * <p>
   * Section containing information regarding the name of the pack, description, and other features that are public facing.
   */
  public static class Header {
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

  /**
   * Module
   * <p>
   * Section containing information regarding the type of content that is being brought in.
   */
  public static class Modules {
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

  /**
   * Metadata
   * <p>
   * Section containing the metadata about the file such as authors and licensing information.
   */
  public static class Metadata {
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

  /**
   * Subpacks
   * <p>
   * A single definition of a subpack.
   */
  public static class Subpacks {
    @JsonProperty("folder_name")
    public String folderName;

    public String name;

    @JsonProperty("memory_tier")
    public float memoryTier;

    /**
     * This represents the folder name located in "subpacks" folder. When user select this resolution Minecraft loads the content inside the folder.
     *
     * @return Folder Name
     */
    public String getFolderName() {
      return this.folderName;
    }

    /**
     * This represents the folder name located in "subpacks" folder. When user select this resolution Minecraft loads the content inside the folder.
     *
     * @param folderName Folder Name
     */
    public void setFolderName(String folderName) {
      this.folderName = folderName;
    }

    /**
     * This is the name of the pack resolution. This lets user know what resolution they are choosing.
     *
     * @return Name
     */
    public String getName() {
      return this.name;
    }

    /**
     * This is the name of the pack resolution. This lets user know what resolution they are choosing.
     *
     * @param name Name
     */
    public void setName(String name) {
      this.name = name;
    }

    /**
     * This creates a requirement on the capacity of memory needed to select the resolution. Each tier increases memory requirement by 256 MB.
     *
     * @return Memory Tier
     */
    public float getMemoryTier() {
      return this.memoryTier;
    }

    /**
     * This creates a requirement on the capacity of memory needed to select the resolution. Each tier increases memory requirement by 256 MB.
     *
     * @param memoryTier Memory Tier
     */
    public void setMemoryTier(float memoryTier) {
      this.memoryTier = memoryTier;
    }
  }
}
