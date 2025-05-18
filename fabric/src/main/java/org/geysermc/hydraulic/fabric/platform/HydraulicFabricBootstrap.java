package org.geysermc.hydraulic.fabric.platform;

import com.google.common.base.Suppliers;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.ModOrigin;
import net.minecraft.server.MinecraftServer;
import org.geysermc.hydraulic.fabric.FabricUtil;
import org.geysermc.hydraulic.platform.HydraulicBootstrap;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HydraulicFabricBootstrap implements HydraulicBootstrap {
    private final Supplier<Map<String, ModInfo>> modsList = Suppliers.memoize(() ->
        FabricLoader.getInstance()
            .getAllMods()
            .stream()
            .filter(Predicate.not(this::ignoreMod))
            .filter(mod -> mod.getMetadata().getEnvironment().matches(EnvType.CLIENT))
            .filter(mod -> mod.getOrigin().getKind() != ModOrigin.Kind.NESTED) // Nested jars are handled in resolveJiJ below
            .<ModInfo>mapMulti((mod, output) -> FabricUtil.resolveJiJ(mod.getRootPaths(), output))
            .collect(Collectors.toUnmodifiableMap(ModInfo::id, Function.identity(), this::maxByVersion))
    );

    /**
     * Ignore mods that are not built in or sub-mods.
     *
     * @param container the mod container
     * @return whether to ignore the mod
     */
    private boolean ignoreMod(ModContainer container) {
        return (container.getMetadata().getId().startsWith("fabric") && container.getMetadata().containsCustomValue("fabric-api:module-lifecycle"))
            || container.getMetadata().getId().equals("fabricloader")
            || container.getMetadata().getId().equals("fabric-api");
    }

    private ModInfo maxByVersion(ModInfo a, ModInfo b) {
        try {
            return Version.parse(b.version()).compareTo(Version.parse(a.version())) > 0 ? b : a;
        } catch (VersionParsingException e) {
            throw new RuntimeException(e);
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
        return FabricLoader.getInstance().getConfigDir().resolve(modId);
    }

    @Override
    public boolean isDev() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public void registerServerStop(Consumer<MinecraftServer> listenerAction) {
        ServerLifecycleEvents.SERVER_STOPPING.register(minecraftServer -> listenerAction.accept(minecraftServer));
    }
}
