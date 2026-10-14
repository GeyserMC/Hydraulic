package org.geysermc.hydraulic.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.List;

@ConfigSerializable
public class HydraulicConfig {
    @Comment("Mods that should be ignored")
    private List<String> ignoredMods = List.of("this-example-mod-id-should-be-ignored",
        "this-other-example-mod-id-should-also-be-ignored");

    @Comment("Do not change!")
    @SuppressWarnings({"unused", "FieldMayBeFinal"})
    private int configVersion = 1;

    public List<String> ignoredMods() {
        return this.ignoredMods;
    }
}
