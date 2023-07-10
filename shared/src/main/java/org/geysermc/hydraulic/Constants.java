package org.geysermc.hydraulic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Constants {
    public static final String MOD_ID = "hydraulic";
    public static final String MOD_NAME = "Hydraulic";
    public static final String VERSION = "1.0.0-SNAPSHOT";

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
}
