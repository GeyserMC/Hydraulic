package org.geysermc.hydraulic.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.List;

@ConfigSerializable
public interface HydraulicConfig {
    @Comment("Mods that should be ignored")
    default List<String> ignoredMods() {
        return List.of("this-example-mod-id-should-be-ignored",
            "this-other-example-mod-id-should-also-be-ignored");
    }

    @Comment("Do not change!")
    @SuppressWarnings("unused")
    default int configVersion() {
        return 1;
    }
}
