package org.geysermc.hydraulic.pack.bedrock.resource.models.entity.modelentity.geometry.bones.cubes.uv;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;

public class Down {
  public float[] uv;

  @JsonProperty("uv_size")
  public float[] uvSize;

  @JsonProperty("material_instance")
  public String materialInstance;

  public float[] getUv() {
    return this.uv;
  }

  public void setUv(float[] uv) {
    this.uv = uv;
  }

  public float[] getUvSize() {
    return this.uvSize;
  }

  public void setUvSize(float[] uvSize) {
    this.uvSize = uvSize;
  }

  /**
   * Specifies the UV's for the face that stretches.
   *
   * @return Material Instance
   */
  public String getMaterialInstance() {
    return this.materialInstance;
  }

  /**
   * Specifies the UV's for the face that stretches.
   *
   * @param materialInstance Material Instance
   */
  public void setMaterialInstance(String materialInstance) {
    this.materialInstance = materialInstance;
  }
}
