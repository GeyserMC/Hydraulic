package org.geysermc.hydraulic.pack.bedrock.resource.materials;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Object;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Material
 * <p>
 * A collection of material specifications for the render engine of minecraft.
 */
public class Materials {
  public Materials materials;

  /**
   * The collection of materials, each property key is the identification key of the material, and what it implements if : are used.
   *
   * @return Materials
   */
  public Materials getMaterials() {
    return this.materials;
  }

  /**
   * The collection of materials, each property key is the identification key of the material, and what it implements if : are used.
   *
   * @param materials Materials
   */
  public void setMaterials(Materials materials) {
    this.materials = materials;
  }

  /**
   * Materials
   * <p>
   * The collection of materials, each property key is the identification key of the material, and what it implements if : are used.
   */
  public static class MinecraftMaterials {
    public String version;

    @JsonProperty("alphaDst")
    public String alphadst;

    @JsonProperty("backFace")
    public Backface backface;

    @JsonProperty("blendDst")
    public String blenddst;

    @JsonProperty("blendSrc")
    public String blendsrc;

    public String[] defines;

    @JsonProperty("+defines")
    public String[] Plusdefines;

    @JsonProperty("-defines")
    public String[] _defines;

    @JsonProperty("depthBias")
    public float depthbias;

    @JsonProperty("depthBiasOGL")
    public float depthbiasogl;

    @JsonProperty("depthFunc")
    public String depthfunc;

    @JsonProperty("fragmentShader")
    public String fragmentshader;

    @JsonProperty("frontFace")
    public Frontface frontface;

    @JsonProperty("isAnimatedTexture")
    public int isanimatedtexture;

    @JsonProperty("msaaSupport")
    public String msaasupport;

    @JsonProperty("primitiveMode")
    public String primitivemode;

    @JsonProperty("samplerStates")
    public List<Samplerstates> samplerstates = new ArrayList<>();

    @JsonProperty("+samplerStates")
    public List<Plussamplerstates> Plussamplerstates = new ArrayList<>();

    @JsonProperty("slopeScaledDepthBias")
    public float slopescaleddepthbias;

    @JsonProperty("slopeScaledDepthBiasOGL")
    public float slopescaleddepthbiasogl;

    public String[] states;

    @JsonProperty("+states")
    public String[] Plusstates;

    @JsonProperty("-states")
    public String[] _states;

    @JsonProperty("stencilRef")
    public int stencilref;

    @JsonProperty("stencilRefOverride")
    public int stencilrefoverride;

    @JsonProperty("stencilReadMask")
    public int stencilreadmask;

    @JsonProperty("stencilWriteMask")
    public int stencilwritemask;

    private Map<String, Object> variants = new HashMap<>();

    @JsonProperty("+variants")
    private Map<String, Object> Plusvariants = new HashMap<>();

    @JsonProperty("vertexFields")
    public List<Vertexfields> vertexfields = new ArrayList<>();

    @JsonProperty("vertexShader")
    public String vertexshader;

    @JsonProperty("vrGeometryShader")
    public String vrgeometryshader;

    /**
     * @return Version
     */
    public String getVersion() {
      return this.version;
    }

    /**
     * @param version Version
     */
    public void setVersion(String version) {
      this.version = version;
    }

    /**
     * @return Alpha Distance
     */
    public String getAlphadst() {
      return this.alphadst;
    }

    /**
     * @param alphadst Alpha Distance
     */
    public void setAlphadst(String alphadst) {
      this.alphadst = alphadst;
    }

    /**
     * @return Face
     */
    public Backface getBackface() {
      return this.backface;
    }

    /**
     * @param backface Face
     */
    public void setBackface(Backface backface) {
      this.backface = backface;
    }

    /**
     * @return Blend Distance
     */
    public String getBlenddst() {
      return this.blenddst;
    }

    /**
     * @param blenddst Blend Distance
     */
    public void setBlenddst(String blenddst) {
      this.blenddst = blenddst;
    }

    /**
     * @return Blend Source
     */
    public String getBlendsrc() {
      return this.blendsrc;
    }

    /**
     * @param blendsrc Blend Source
     */
    public void setBlendsrc(String blendsrc) {
      this.blendsrc = blendsrc;
    }

    /**
     * @return Defines
     */
    public String[] getDefines() {
      return this.defines;
    }

    /**
     * @param defines Defines
     */
    public void setDefines(String[] defines) {
      this.defines = defines;
    }

    /**
     * @return Defines
     */
    public String[] getPlusdefines() {
      return this.Plusdefines;
    }

    /**
     * @param Plusdefines Defines
     */
    public void setPlusdefines(String[] Plusdefines) {
      this.Plusdefines = Plusdefines;
    }

    /**
     * @return Defines
     */
    public String[] get_defines() {
      return this._defines;
    }

    /**
     * @param _defines Defines
     */
    public void set_defines(String[] _defines) {
      this._defines = _defines;
    }

    /**
     * @return Depth Bias
     */
    public float getDepthbias() {
      return this.depthbias;
    }

    /**
     * @param depthbias Depth Bias
     */
    public void setDepthbias(float depthbias) {
      this.depthbias = depthbias;
    }

    /**
     * @return Depth Bias OGL
     */
    public float getDepthbiasogl() {
      return this.depthbiasogl;
    }

    /**
     * @param depthbiasogl Depth Bias OGL
     */
    public void setDepthbiasogl(float depthbiasogl) {
      this.depthbiasogl = depthbiasogl;
    }

    /**
     * @return Depth Function
     */
    public String getDepthfunc() {
      return this.depthfunc;
    }

    /**
     * @param depthfunc Depth Function
     */
    public void setDepthfunc(String depthfunc) {
      this.depthfunc = depthfunc;
    }

    /**
     * @return Fragment Shader
     */
    public String getFragmentshader() {
      return this.fragmentshader;
    }

    /**
     * @param fragmentshader Fragment Shader
     */
    public void setFragmentshader(String fragmentshader) {
      this.fragmentshader = fragmentshader;
    }

    /**
     * @return Face
     */
    public Frontface getFrontface() {
      return this.frontface;
    }

    /**
     * @param frontface Face
     */
    public void setFrontface(Frontface frontface) {
      this.frontface = frontface;
    }

    /**
     * @return Is Animated Texture
     */
    public int getIsanimatedtexture() {
      return this.isanimatedtexture;
    }

    /**
     * @param isanimatedtexture Is Animated Texture
     */
    public void setIsanimatedtexture(int isanimatedtexture) {
      this.isanimatedtexture = isanimatedtexture;
    }

    /**
     * @return MSAA Support
     */
    public String getMsaasupport() {
      return this.msaasupport;
    }

    /**
     * @param msaasupport MSAA Support
     */
    public void setMsaasupport(String msaasupport) {
      this.msaasupport = msaasupport;
    }

    /**
     * @return Primitive Mode
     */
    public String getPrimitivemode() {
      return this.primitivemode;
    }

    /**
     * @param primitivemode Primitive Mode
     */
    public void setPrimitivemode(String primitivemode) {
      this.primitivemode = primitivemode;
    }

    /**
     * @return Sampler States
     */
    public List<Samplerstates> getSamplerstates() {
      return this.samplerstates;
    }

    /**
     * @param samplerstates Sampler States
     */
    public void setSamplerstates(List<Samplerstates> samplerstates) {
      this.samplerstates = samplerstates;
    }

    /**
     * @return Sampler States
     */
    public List<Plussamplerstates> getPlussamplerstates() {
      return this.Plussamplerstates;
    }

    /**
     * @param Plussamplerstates Sampler States
     */
    public void setPlussamplerstates(List<Plussamplerstates> Plussamplerstates) {
      this.Plussamplerstates = Plussamplerstates;
    }

    /**
     * @return Slope Scaled Depth Bias
     */
    public float getSlopescaleddepthbias() {
      return this.slopescaleddepthbias;
    }

    /**
     * @param slopescaleddepthbias Slope Scaled Depth Bias
     */
    public void setSlopescaleddepthbias(float slopescaleddepthbias) {
      this.slopescaleddepthbias = slopescaleddepthbias;
    }

    /**
     * @return Slope Scaled Depth Bias OGL
     */
    public float getSlopescaleddepthbiasogl() {
      return this.slopescaleddepthbiasogl;
    }

    /**
     * @param slopescaleddepthbiasogl Slope Scaled Depth Bias OGL
     */
    public void setSlopescaleddepthbiasogl(float slopescaleddepthbiasogl) {
      this.slopescaleddepthbiasogl = slopescaleddepthbiasogl;
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
    public String[] getPlusstates() {
      return this.Plusstates;
    }

    /**
     * @param Plusstates States
     */
    public void setPlusstates(String[] Plusstates) {
      this.Plusstates = Plusstates;
    }

    /**
     * @return States
     */
    public String[] get_states() {
      return this._states;
    }

    /**
     * @param _states States
     */
    public void set_states(String[] _states) {
      this._states = _states;
    }

    /**
     * @return Stencil Ref
     */
    public int getStencilref() {
      return this.stencilref;
    }

    /**
     * @param stencilref Stencil Ref
     */
    public void setStencilref(int stencilref) {
      this.stencilref = stencilref;
    }

    /**
     * @return Stencil Ref Override
     */
    public int getStencilrefoverride() {
      return this.stencilrefoverride;
    }

    /**
     * @param stencilrefoverride Stencil Ref Override
     */
    public void setStencilrefoverride(int stencilrefoverride) {
      this.stencilrefoverride = stencilrefoverride;
    }

    /**
     * @return Stencil Read Mask
     */
    public int getStencilreadmask() {
      return this.stencilreadmask;
    }

    /**
     * @param stencilreadmask Stencil Read Mask
     */
    public void setStencilreadmask(int stencilreadmask) {
      this.stencilreadmask = stencilreadmask;
    }

    /**
     * @return Stencil Write Mask
     */
    public int getStencilwritemask() {
      return this.stencilwritemask;
    }

    /**
     * @param stencilwritemask Stencil Write Mask
     */
    public void setStencilwritemask(int stencilwritemask) {
      this.stencilwritemask = stencilwritemask;
    }

    /**
     * @return Variants
     */
    public Map<String, Object> getVariants() {
      return this.variants;
    }

    /**
     * @param variants Variants
     */
    public void setVariants(Map<String, Object> variants) {
      this.variants = variants;
    }

    /**
     * @return Variants
     */
    public Map<String, Object> getPlusvariants() {
      return this.Plusvariants;
    }

    /**
     * @param Plusvariants Variants
     */
    public void setPlusvariants(Map<String, Object> Plusvariants) {
      this.Plusvariants = Plusvariants;
    }

    /**
     * @return Vertex Fields
     */
    public List<Vertexfields> getVertexfields() {
      return this.vertexfields;
    }

    /**
     * @param vertexfields Vertex Fields
     */
    public void setVertexfields(List<Vertexfields> vertexfields) {
      this.vertexfields = vertexfields;
    }

    /**
     * @return Vertex Shader
     */
    public String getVertexshader() {
      return this.vertexshader;
    }

    /**
     * @param vertexshader Vertex Shader
     */
    public void setVertexshader(String vertexshader) {
      this.vertexshader = vertexshader;
    }

    /**
     * @return VR Geometry Shader
     */
    public String getVrgeometryshader() {
      return this.vrgeometryshader;
    }

    /**
     * @param vrgeometryshader VR Geometry Shader
     */
    public void setVrgeometryshader(String vrgeometryshader) {
      this.vrgeometryshader = vrgeometryshader;
    }

    /**
     * Face
     */
    public static class Backface {
      @JsonProperty("stencilDepthFailOp")
      public String stencildepthfailop;

      @JsonProperty("stencilFailOp")
      public String stencilfailop;

      @JsonProperty("stencilFunc")
      public String stencilfunc;

      @JsonProperty("stencilPass")
      public String stencilpass;

      @JsonProperty("stencilPassOp")
      public String stencilpassop;

      /**
       * @return Stencil Depth Fail Operation
       */
      public String getStencildepthfailop() {
        return this.stencildepthfailop;
      }

      /**
       * @param stencildepthfailop Stencil Depth Fail Operation
       */
      public void setStencildepthfailop(String stencildepthfailop) {
        this.stencildepthfailop = stencildepthfailop;
      }

      /**
       * @return Stencil Fail Operation
       */
      public String getStencilfailop() {
        return this.stencilfailop;
      }

      /**
       * @param stencilfailop Stencil Fail Operation
       */
      public void setStencilfailop(String stencilfailop) {
        this.stencilfailop = stencilfailop;
      }

      /**
       * @return Stencil Function
       */
      public String getStencilfunc() {
        return this.stencilfunc;
      }

      /**
       * @param stencilfunc Stencil Function
       */
      public void setStencilfunc(String stencilfunc) {
        this.stencilfunc = stencilfunc;
      }

      /**
       * @return Stencil Pass
       */
      public String getStencilpass() {
        return this.stencilpass;
      }

      /**
       * @param stencilpass Stencil Pass
       */
      public void setStencilpass(String stencilpass) {
        this.stencilpass = stencilpass;
      }

      /**
       * @return Stencil Depth Fail Operation
       */
      public String getStencilpassop() {
        return this.stencilpassop;
      }

      /**
       * @param stencilpassop Stencil Depth Fail Operation
       */
      public void setStencilpassop(String stencilpassop) {
        this.stencilpassop = stencilpassop;
      }
    }

    /**
     * Face
     */
    public static class Frontface {
      @JsonProperty("stencilDepthFailOp")
      public String stencildepthfailop;

      @JsonProperty("stencilFailOp")
      public String stencilfailop;

      @JsonProperty("stencilFunc")
      public String stencilfunc;

      @JsonProperty("stencilPass")
      public String stencilpass;

      @JsonProperty("stencilPassOp")
      public String stencilpassop;

      /**
       * @return Stencil Depth Fail Operation
       */
      public String getStencildepthfailop() {
        return this.stencildepthfailop;
      }

      /**
       * @param stencildepthfailop Stencil Depth Fail Operation
       */
      public void setStencildepthfailop(String stencildepthfailop) {
        this.stencildepthfailop = stencildepthfailop;
      }

      /**
       * @return Stencil Fail Operation
       */
      public String getStencilfailop() {
        return this.stencilfailop;
      }

      /**
       * @param stencilfailop Stencil Fail Operation
       */
      public void setStencilfailop(String stencilfailop) {
        this.stencilfailop = stencilfailop;
      }

      /**
       * @return Stencil Function
       */
      public String getStencilfunc() {
        return this.stencilfunc;
      }

      /**
       * @param stencilfunc Stencil Function
       */
      public void setStencilfunc(String stencilfunc) {
        this.stencilfunc = stencilfunc;
      }

      /**
       * @return Stencil Pass
       */
      public String getStencilpass() {
        return this.stencilpass;
      }

      /**
       * @param stencilpass Stencil Pass
       */
      public void setStencilpass(String stencilpass) {
        this.stencilpass = stencilpass;
      }

      /**
       * @return Stencil Depth Fail Operation
       */
      public String getStencilpassop() {
        return this.stencilpassop;
      }

      /**
       * @param stencilpassop Stencil Depth Fail Operation
       */
      public void setStencilpassop(String stencilpassop) {
        this.stencilpassop = stencilpassop;
      }
    }

    /**
     * Sample State
     */
    public static class Samplerstates {
      @JsonProperty("samplerIndex")
      public int samplerindex;

      @JsonProperty("textureFilter")
      public String texturefilter;

      @JsonProperty("textureWrap")
      public String texturewrap;

      /**
       * @return Sample State
       */
      public int getSamplerindex() {
        return this.samplerindex;
      }

      /**
       * @param samplerindex Sample State
       */
      public void setSamplerindex(int samplerindex) {
        this.samplerindex = samplerindex;
      }

      /**
       * @return Texture Filter
       */
      public String getTexturefilter() {
        return this.texturefilter;
      }

      /**
       * @param texturefilter Texture Filter
       */
      public void setTexturefilter(String texturefilter) {
        this.texturefilter = texturefilter;
      }

      /**
       * @return Texture Wrap
       */
      public String getTexturewrap() {
        return this.texturewrap;
      }

      /**
       * @param texturewrap Texture Wrap
       */
      public void setTexturewrap(String texturewrap) {
        this.texturewrap = texturewrap;
      }
    }

    /**
     * Sample State
     */
    public static class Plussamplerstates {
      @JsonProperty("samplerIndex")
      public int samplerindex;

      @JsonProperty("textureFilter")
      public String texturefilter;

      @JsonProperty("textureWrap")
      public String texturewrap;

      /**
       * @return Sample State
       */
      public int getSamplerindex() {
        return this.samplerindex;
      }

      /**
       * @param samplerindex Sample State
       */
      public void setSamplerindex(int samplerindex) {
        this.samplerindex = samplerindex;
      }

      /**
       * @return Texture Filter
       */
      public String getTexturefilter() {
        return this.texturefilter;
      }

      /**
       * @param texturefilter Texture Filter
       */
      public void setTexturefilter(String texturefilter) {
        this.texturefilter = texturefilter;
      }

      /**
       * @return Texture Wrap
       */
      public String getTexturewrap() {
        return this.texturewrap;
      }

      /**
       * @param texturewrap Texture Wrap
       */
      public void setTexturewrap(String texturewrap) {
        this.texturewrap = texturewrap;
      }
    }

    /**
     * Vertex Field
     */
    public static class Vertexfields {
      public String field;

      /**
       * @return Vertex Field
       */
      public String getField() {
        return this.field;
      }

      /**
       * @param field Vertex Field
       */
      public void setField(String field) {
        this.field = field;
      }
    }
  }
}
