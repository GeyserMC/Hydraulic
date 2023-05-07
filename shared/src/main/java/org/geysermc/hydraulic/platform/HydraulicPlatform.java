package org.geysermc.hydraulic.platform;

public enum HydraulicPlatform {
    FABRIC("Fabric"),
    FORGE("Forge");

    private final String platformName;

    HydraulicPlatform(String platformName) {
        this.platformName = platformName;
    }

    public String platformName() {
        return this.platformName;
    }
}
