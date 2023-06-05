package org.geysermc.hydraulic.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Constants {
    public static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static final String JAVA_TEXTURE_LOCATION = "assets/%s/textures/%s.png";
    public static final String JAVA_ITEM_MODEL_LOCATION = "assets/%s/models/item/%s.json";

    public static final String BEDROCK_TEXTURE_LOCATION = "textures/%s.png";
}
