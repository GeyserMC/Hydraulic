package org.geysermc.hydraulic.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Constants {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static final String JAVA_TEXTURE_LOCATION = "assets/%s/textures/%s.png";
    public static final String JAVA_ITEM_MODEL_LOCATION = "assets/%s/models/item/%s.json";

    public static final String BEDROCK_ITEM_TEXTURE_LOCATION = "textures/items/%s/%s.png";
}
