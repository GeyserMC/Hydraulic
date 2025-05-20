package org.geysermc.hydraulic.util;

import net.kyori.adventure.key.Key;
import net.minecraft.resources.ResourceLocation;
import org.geysermc.geyser.api.util.Identifier;

public class KeyUtil {
    public static Identifier toGeyserIdentifier(ResourceLocation location) {
        if (location == null) return null;
        return Identifier.of(location.getNamespace(), location.getPath());
    }

    public static Identifier toGeyserIdentifier(Key key) {
        if (key == null) return null;
        return Identifier.of(key.namespace(), key.value());
    }
}
