package org.geysermc.hydraulic.pack.bedrock.resource.sounds.sounddefinitions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Boolean;
import java.lang.Float;
import java.lang.Integer;
import java.lang.String;

/**
 * Sounds
 * <p>
 * A collection of sounds to choice from.
 */
public class Sounds {
  public Boolean is3D;

  public Float pitch;

  public Float volume;

  @JsonProperty("load_on_low_memory")
  public Boolean loadOnLowMemory;

  public Boolean stream;

  public String name;

  public Integer weight;

  /**
   * @return Is 3D
   */
  public Boolean getIs3D() {
    return this.is3D;
  }

  /**
   * @param is3D Is 3D
   */
  public void setIs3D(Boolean is3D) {
    this.is3D = is3D;
  }

  /**
   * The pitch of the audio, 1 is nomial.
   *
   * @return Pitch
   */
  public Float getPitch() {
    return this.pitch;
  }

  /**
   * The pitch of the audio, 1 is nomial.
   *
   * @param pitch Pitch
   */
  public void setPitch(float pitch) {
    this.pitch = pitch;
  }

  /**
   * The pitch of the audio, 1 is nomial.
   *
   * @param pitch Pitch
   */
  public void setPitch(Float pitch) {
    this.pitch = pitch;
  }

  /**
   * The volume of the audio, 1 is nomial.
   *
   * @return Volume
   */
  public Float getVolume() {
    return this.volume;
  }

  /**
   * The volume of the audio, 1 is nomial.
   *
   * @param volume Volume
   */
  public void setVolume(float volume) {
    this.volume = volume;
  }

  /**
   * The volume of the audio, 1 is nomial.
   *
   * @param volume Volume
   */
  public void setVolume(Float volume) {
    this.volume = volume;
  }

  /**
   * Marks if this audio should be loaded or not on low memory.
   *
   * @return Load On Low Memory
   */
  public Boolean getLoadOnLowMemory() {
    return this.loadOnLowMemory;
  }

  /**
   * Marks if this audio should be loaded or not on low memory.
   *
   * @param loadOnLowMemory Load On Low Memory
   */
  public void setLoadOnLowMemory(Boolean loadOnLowMemory) {
    this.loadOnLowMemory = loadOnLowMemory;
  }

  /**
   * If marked true then minecraft will stream the audio.
   *
   * @return Stream
   */
  public Boolean getStream() {
    return this.stream;
  }

  /**
   * If marked true then minecraft will stream the audio.
   *
   * @param stream Stream
   */
  public void setStream(Boolean stream) {
    this.stream = stream;
  }

  /**
   * @return Name
   */
  public String getName() {
    return this.name;
  }

  /**
   * @param name Name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return Weight
   */
  public Integer getWeight() {
    return this.weight;
  }

  /**
   * @param weight Weight
   */
  public void setWeight(Integer weight) {
    this.weight = weight;
  }
}
