package org.geysermc.hydraulic.neoforge.platform;

import com.google.common.base.Suppliers;
import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforgespi.language.IModInfo;
import org.geysermc.hydraulic.platform.HydraulicBootstrap;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HydraulicNeoForgeBootstrap implements HydraulicBootstrap {
    private final Supplier<Map<String, ModInfo>> modsList = Suppliers.memoize(() ->
        ModList.get()
            .getMods()
            .stream()
            .filter(Predicate.not(HydraulicNeoForgeBootstrap::ignoreMod))
            .map(this::modInfo)
            .collect(Collectors.toUnmodifiableMap(ModInfo::id, Function.identity()))
    );

    private ModInfo modInfo(IModInfo mod) {
        final List<Path> roots = mod.getOwningFile().getFile().getContents().getContentRoots()
                .stream()
                .map(HydraulicNeoForgeBootstrap::root)
                .toList();
        final Path icon = mod.getLogoFile()
                .flatMap(logo -> roots.stream().map(root -> root.resolve(logo)).filter(Files::isRegularFile).findFirst())
                .orElse(null);

        return new ModInfo(
                mod.getModId(),
                mod.getNamespace(),
                mod.getDisplayName(),
                mod.getVersion().toString(),
                icon,
                roots
        );
    }

    // We aren't modifying jars, therefore we don't need to close() them, as they'll simply get GCed.
    @SuppressWarnings("resource")
    private static Path root(Path contentRoot) {
        if (Files.isDirectory(contentRoot)) {
            return contentRoot;
        }

        try {
            return FileSystems.newFileSystem(contentRoot).getRootDirectories().iterator().next();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

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

    private static boolean ignoreMod(IModInfo mod) {
        return mod.getModId().equals("minecraft")
                || mod.getModId().equals("neoforge")
                || Boolean.TRUE.equals(mod.getModProperties().get("hydraulic:ignore"));
    }
}
