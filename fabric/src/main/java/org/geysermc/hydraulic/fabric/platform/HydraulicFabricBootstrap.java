package org.geysermc.hydraulic.fabric.platform;

import net.fabricmc.loader.api.FabricLoader;
import org.geysermc.hydraulic.platform.HydraulicBootstrap;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.Collectors;

public class HydraulicFabricBootstrap implements HydraulicBootstrap {

    @Override
    public @NotNull Set<ModInfo> mods() {
        return FabricLoader.getInstance().getAllMods().stream().map(container ->
            new ModInfo(
                    container.getMetadata().getId(),
                    container.getMetadata().getVersion().getFriendlyString(),
                    container.getMetadata().getName(),
                    container.getRootPaths().get(0) // TODO: Multi-path support
            )
        ).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @Nullable ModInfo mod(@NotNull String modName) {
        return FabricLoader.getInstance().getModContainer(modName).map(container ->
                new ModInfo(
                        container.getMetadata().getId(),
                        container.getMetadata().getVersion().getFriendlyString(),
                        container.getMetadata().getName(),
                        container.getRootPaths().get(0) // TODO: Multi-path support
                )
        ).orElse(null);
    }
}
