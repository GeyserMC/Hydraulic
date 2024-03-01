package org.geysermc.hydraulic.forge.platform;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import org.geysermc.hydraulic.platform.HydraulicBootstrap;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public class HydraulicForgeBootstrap implements HydraulicBootstrap {

    @Override
    public @NotNull Set<ModInfo> mods() {
        return ModList.get().getMods().stream().map(modInfo ->
                new ModInfo(
                        modInfo.getModId(),
                        modInfo.getVersion().toString(),
                        modInfo.getDisplayName(),
                        modInfo.getOwningFile().getFile().getFilePath(), // Confirm this will work with the .resolve calls used
                        modInfo.getOwningFile().getFile().getFilePath(),
                        modInfo.getLogoFile().orElse("")
                )
        ).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @Nullable ModInfo mod(@NotNull String modName) {
        return ModList.get().getModContainerById(modName).map(container ->
                new ModInfo(
                        container.getModId(),
                        container.getModInfo().getVersion().toString(),
                        container.getModInfo().getDisplayName(),
                        container.getModInfo().getOwningFile().getFile().getFilePath(), // Confirm this will work with the .resolve calls used
                        container.getModInfo().getOwningFile().getFile().getFilePath(),
                        container.getModInfo().getLogoFile().orElse("")
                )
        ).orElse(null);
    }

    @Override
    public @NotNull Path dataFolder(@NotNull String modId) {
        return FMLPaths.CONFIGDIR.get().resolve(modId);
    }
}
