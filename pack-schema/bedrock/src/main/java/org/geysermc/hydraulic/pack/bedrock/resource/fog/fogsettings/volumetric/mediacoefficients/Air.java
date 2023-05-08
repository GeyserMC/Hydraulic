package org.geysermc.hydraulic.pack.bedrock.resource.fog.fogsettings.volumetric.mediacoefficients;

public class Air {
  public float[] absorption;

  public float[] scattering;

  public float[] getAbsorption() {
    return this.absorption;
  }

  public void setAbsorption(float[] absorption) {
    this.absorption = absorption;
  }

  public float[] getScattering() {
    return this.scattering;
  }

  public void setScattering(float[] scattering) {
    this.scattering = scattering;
  }
}
