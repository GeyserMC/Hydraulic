package org.geysermc.hydraulic.fabric.platform;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.geysermc.hydraulic.platform.HydraulicBootstrap;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public class HydraulicFabricBootstrap implements HydraulicBootstrap {

    @Override
    public @NotNull Set<ModInfo> mods() {
        return FabricLoader.getInstance().getAllMods().stream().filter(container -> !ignoreMod(container)).map(container ->
            new ModInfo(
                    container.getMetadata().getId(),
                    container.getMetadata().getVersion().getFriendlyString(),
                    container.getMetadata().getName(),
                    container.getRootPaths().get(0), // TODO: Multi-path support
                    container.getMetadata().getIconPath(256).orElse("")
            )
        ).collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Ignore mods that are not built in or sub-mods.
     *
     * @param container the mod container
     * @return whether to ignore the mod
     */
    private boolean ignoreMod(ModContainer container) {
        return (container.getMetadata().getId().startsWith("fabric") && container.getMetadata().containsCustomValue("fabric-api:module-lifecycle"))
            || container.getMetadata().getId().equals("fabricloader")
            || container.getMetadata().getId().equals("fabric-api")
            || !container.getContainingMod().isEmpty();
    }

    @Override
    public @Nullable ModInfo mod(@NotNull String modName) {
        return FabricLoader.getInstance().getModContainer(modName).map(container ->
                new ModInfo(
                        container.getMetadata().getId(),
                        container.getMetadata().getVersion().getFriendlyString(),
                        container.getMetadata().getName(),
                        container.getRootPaths().get(0), // TODO: Multi-path support
                        container.getMetadata().getIconPath(256).orElse("")
                )
        ).orElse(null);
    }

    @Override
    public @NotNull Path dataFolder(@NotNull String modId) {
        return FabricLoader.getInstance().getConfigDir().resolve(modId);
    }
}
