package org.geysermc.hydraulic.pack.bedrock.resource.materials;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import org.geysermc.hydraulic.pack.bedrock.resource.materials.plusvariants.VertexFields;

/**
 * Variant Item
 */
public class PlusVariants {
  @JsonProperty("+defines")
  public String[] plusDefines;

  public List<VertexFields> vertexFields = new ArrayList<>();

  public String[] states;

  @JsonProperty("+states")
  public String[] plusStates;

  @JsonProperty("-states")
  public String[] minusStates;

  /**
   * @return Defines
   */
  public String[] getPlusDefines() {
    return this.plusDefines;
  }

  /**
   * @param plusDefines Defines
   */
  public void setPlusDefines(String[] plusDefines) {
    this.plusDefines = plusDefines;
  }

  /**
   * @return Vertex Fields
   */
  public List<VertexFields> getVertexFields() {
    return this.vertexFields;
  }

  /**
   * @param vertexFields Vertex Fields
   */
  public void setVertexFields(List<VertexFields> vertexFields) {
    this.vertexFields = vertexFields;
  }

  /**
   * @return States
   */
  public String[] getStates() {
    return this.states;
  }

  /**
   * @param states States
   */
  public void setStates(String[] states) {
    this.states = states;
  }

  /**
   * @return States
   */
  public String[] getPlusStates() {
    return this.plusStates;
  }

  /**
   * @param plusStates States
   */
  public void setPlusStates(String[] plusStates) {
    this.plusStates = plusStates;
  }

  /**
   * @return States
   */
  public String[] getMinusStates() {
    return this.minusStates;
  }

  /**
   * @param minusStates States
   */
  public void setMinusStates(String[] minusStates) {
    this.minusStates = minusStates;
  }
}
