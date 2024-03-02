package org.geysermc.hydraulic.pack;

import com.mojang.logging.LogUtils;
import org.geysermc.event.Event;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.geysermc.hydraulic.pack.context.PackPreProcessContext;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.pack.converter.PackConverter;
import org.geysermc.pack.converter.converter.ActionListener;
import org.geysermc.pack.converter.converter.Converters;
import org.geysermc.pack.converter.data.ConversionData;
import org.geysermc.pack.converter.util.NioDirectoryFileTreeReader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackReader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Manages packs within Hydraulic. Most of the pack conversion
 * management is done within this class, and it is also responsible
 * for loading the packs onto the server.
 */
public class PackManager {
    private static final Logger LOGGER = LogUtils.getLogger();

    static final Set<String> IGNORED_MODS = Set.of(
            "minecraft",
            "java",
            "geyser-fabric",
            "geyser-forge",
            "floodgate",
            "vanilla",
            "mixinextras"
    );

    private final HydraulicImpl hydraulic;
    private final List<PackModule<?>> modules = new ArrayList<>();

    public PackManager(HydraulicImpl hydraulic) {
        this.hydraulic = hydraulic;
    }

    /**
     * Initializes the pack manager.
     */
    public void initialize() {
        for (PackModule<?> module : ServiceLoader.load(PackModule.class)) {
            this.modules.add(module);

            GeyserApi.api().eventBus().register(this.hydraulic, module);
            module.eventListeners().forEach((eventClass, listeners) -> {
                GeyserApi.api().eventBus().subscribe(this.hydraulic, eventClass, this::callEvents);
            });

            for (ModInfo mod : this.hydraulic.mods()) {
                if (IGNORED_MODS.contains(mod.id())) {
                    continue;
                }

                if (module.hasPreProcessors()) {
                    try {
                        ResourcePack pack = MinecraftResourcePackReader.minecraft().read(NioDirectoryFileTreeReader.read(mod.modPath()));

                        PackPreProcessContext context = new PackPreProcessContext(this.hydraulic, mod, module, pack);
                        module.preProcess0(context);
                    } catch (Throwable t) {
                        LOGGER.error("Failed to pre-process mod {} for module {}", mod.id(), module.getClass().getSimpleName(), t);
                    }
                }
            }
        }

        GeyserApi.api().eventBus().register(this.hydraulic, new PackListener(this.hydraulic, this));

    }

    /**
     * Creates the pack for the given mod.
     *
     * @param mod the mod to create the pack for
     * @param packPath the path to the pack
     * @return {@code true} if the pack was created, {@code false} otherwise
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    boolean createPack(@NotNull ModInfo mod, @NotNull Path packPath) {
        PackConverter converter = new PackConverter()
                .logListener(new PackLogListener(LOGGER))
                .converters(Converters.defaultConverters())
                .input(mod.modPath(), false)
                .output(packPath)
                .textureSubdirectory(mod.id());

        Map<Class<ConversionData>, List<ActionListener<ConversionData>>> actionListeners = new IdentityHashMap<>();
        for (PackModule<?> module : this.modules) {
            if (module instanceof ConvertablePackModule<?, ?> convertableModule) {
                actionListeners.computeIfAbsent((Class<ConversionData>) convertableModule.conversionType(),
                        e -> new ArrayList<>()).add((ConvertablePackModule<?, ConversionData>) convertableModule);
            }
        }

        converter.actionListeners(actionListeners);
        converter.postProcessor(pack -> {
            for (PackModule<?> module : this.modules) {
                PackPostProcessContext context = new PackPostProcessContext(this.hydraulic, mod, module, converter, pack, packPath);
                if (!module.test(context)) {
                    continue;
                }

                module.postProcess0(context);
            }
        });

        try {
            converter.convert();
        } catch (IOException ex) {
            LOGGER.error("Failed to convert mod {} to pack", mod.id(), ex);
            return false;
        }

        // TODO Ignore packs if they only have a manifest

        // Now export the pack
        try {
            converter.pack();
        } catch (IOException ex) {
            LOGGER.error("Failed to export pack for mod {}", mod.id(), ex);
        }

        return true;
    }

    private void callEvents(@NotNull Event event) {
        for (ModInfo mod : this.hydraulic.mods()) {
            if (IGNORED_MODS.contains(mod.id())) {
                continue;
            }

            this.callEvent(mod, event);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void callEvent(@NotNull ModInfo mod, @NotNull Event event) {
        for (PackModule<?> module : this.modules) {
            module.call(event.getClass(), new PackEventContext(this.hydraulic, mod, module, event));
        }
    }
}
