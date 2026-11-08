package org.geysermc.hydraulic.fabric.platform;

import com.google.common.base.Suppliers;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.ModOrigin;
import net.minecraft.server.MinecraftServer;
import org.geysermc.hydraulic.entity.EntityRendererRegistryInfo;
import org.geysermc.hydraulic.entity.ModelLayerRegistryInfo;
import org.geysermc.hydraulic.fabric.FabricUtil;
import org.geysermc.hydraulic.fabric.entity.EntityRendererRegistry;
import org.geysermc.hydraulic.fabric.entity.ModelLayerRegistry;
import org.geysermc.hydraulic.platform.HydraulicBootstrap;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HydraulicFabricBootstrap implements HydraulicBootstrap {
    // Minecraft's common classes would otherwise be followed into, such as Mth.cos into its lookup table.
    private static final Set<String> NOT_MOD_JARS = Set.of("minecraft", "java", "fabricloader");

    private final Supplier<Map<String, ModInfo>> modsList = Suppliers.memoize(() ->
        FabricLoader.getInstance()
            .getAllMods()
            .stream()
            .filter(Predicate.not(this::ignoreMod))
            .filter(mod -> mod.getMetadata().getEnvironment().matches(EnvType.CLIENT))
            .filter(mod -> mod.getOrigin().getKind() != ModOrigin.Kind.NESTED) // Nested jars are handled in resolveJiJ below
            .<ModInfo>mapMulti((mod, output) -> FabricUtil.resolveJiJ(mod.getRootPaths(), originOf(mod), output))
            .collect(Collectors.toUnmodifiableMap(ModInfo::id, Function.identity(), this::maxByVersion))
    );

    /**
     * The jar(s) on disk a mod was loaded from. {@link ModOrigin#getPaths()} throws for anything
     * that isn't {@link ModOrigin.Kind#PATH}, so mods without a file of their own report nothing.
     *
     * @param container the mod container
     * @return the mod's on-disk paths, or empty if it has none
     */
    private Collection<Path> originOf(ModContainer container) {
        return container.getOrigin().getKind() == ModOrigin.Kind.PATH
            ? container.getOrigin().getPaths()
            : List.of();
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
    public @NotNull Collection<Path> modJars() {
        return FabricLoader.getInstance()
            .getAllMods()
            .stream()
            .filter(mod -> !NOT_MOD_JARS.contains(mod.getMetadata().getId()))
            .flatMap(mod -> this.originOf(mod).stream())
            .filter(Files::isRegularFile)
            .distinct()
            .toList();
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
        ServerLifecycleEvents.SERVER_STOPPING.register(listenerAction::accept);
    }

    @Override
    public void registerServerTick(Consumer<MinecraftServer> listenerAction) {
        ServerTickEvents.END_SERVER_TICK.register(listenerAction::accept);
    }

    @Override
    public ModelLayerRegistryInfo modelLayerInfo() {
        return new ModelLayerRegistryInfo(ModelLayerRegistry.class, () -> ModelLayerRegistry.CAPTURE);
    }

    @Override
    public EntityRendererRegistryInfo entityRendererInfo() {
        return new EntityRendererRegistryInfo(EntityRendererRegistry.class, () -> EntityRendererRegistry.CAPTURE);
    }
}
