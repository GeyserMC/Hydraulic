package org.geysermc.hydraulic.pack.bedrock.resource.sounds;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * Sound Definitions
 * <p>
 * The collection of sound definitions this resourcepack has defined.
 */
public class SoundDefinitions {
  @JsonProperty("format_version")
  public String formatVersion;

  @JsonProperty("sound_definitions")
  private Map<String, org.geysermc.hydraulic.pack.bedrock.resource.sounds.sounddefinitions.SoundDefinitions> soundDefinitions = new HashMap<>();

  @JsonProperty("__use_legacy_max_distance")
  public String useLegacyMaxDistance;

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
   * @return Sound Definitions
   */
  public Map<String, org.geysermc.hydraulic.pack.bedrock.resource.sounds.sounddefinitions.SoundDefinitions> getSoundDefinitions(
      ) {
    return this.soundDefinitions;
  }

  /**
   * @param soundDefinitions Sound Definitions
   */
  public void setSoundDefinitions(
      Map<String, org.geysermc.hydraulic.pack.bedrock.resource.sounds.sounddefinitions.SoundDefinitions> soundDefinitions) {
    this.soundDefinitions = soundDefinitions;
  }

  /**
   * @return Use Legacy Maximum Distance
   */
  public String getUseLegacyMaxDistance() {
    return this.useLegacyMaxDistance;
  }

  /**
   * @param useLegacyMaxDistance Use Legacy Maximum Distance
   */
  public void setUseLegacyMaxDistance(String useLegacyMaxDistance) {
    this.useLegacyMaxDistance = useLegacyMaxDistance;
  }
}
