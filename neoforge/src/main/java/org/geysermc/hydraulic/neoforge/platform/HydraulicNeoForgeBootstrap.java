package org.geysermc.hydraulic.neoforge.platform;

import com.google.common.base.Suppliers;
import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.geysermc.hydraulic.platform.HydraulicBootstrap;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HydraulicNeoForgeBootstrap implements HydraulicBootstrap {
    private static final List<String> USER_BLOCKED_MODS = Arrays.stream(
            System.getProperty("hydraulic.ignored_mods", "").split(",")
    ).toList();

    private final Supplier<Map<String, ModInfo>> modsList = Suppliers.memoize(() ->
        ModList.get()
            .getMods()
            .stream()
            .map(mod -> {
                final Path modPath = mod.getOwningFile().getFile().getFilePath();
                return new ModInfo(
                    mod.getModId(),
                    mod.getNamespace(),
                    mod.getDisplayName(),
                    mod.getVersion().toString(),
                    mod.getLogoFile().map(modPath::resolve).filter(Files::isRegularFile).orElse(null),
                    List.of(modPath)
                );
            })
            .filter(modInfo -> !USER_BLOCKED_MODS.contains(modInfo.id()))
            .collect(Collectors.toUnmodifiableMap(ModInfo::id, Function.identity()))
    );

    @Override
    public @Nullable ModInfo mod(@NotNull String modName) {
        return modsList.get().get(modName);
    }

    @Override
    public @NotNull Collection<ModInfo> mods() {
        return modsList.get().values();
    }

    @Override
    public @NotNull Path dataFolder(@NotNull String modId) {
        return FMLPaths.CONFIGDIR.get().resolve(modId);
    }

    @Override
    public boolean isDev() {
        return !FMLLoader.getCurrent().isProduction();
    }

    @Override
    public void registerServerStop(Consumer<MinecraftServer> listenerAction) {
        NeoForge.EVENT_BUS.addListener(ServerStoppingEvent.class, event -> {
            listenerAction.accept(event.getServer());
        });
    }
}
