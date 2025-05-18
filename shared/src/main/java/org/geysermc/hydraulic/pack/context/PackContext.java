package org.geysermc.hydraulic.pack.context;

import com.google.common.collect.Lists;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.hydraulic.storage.ModStorage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;

/**
 * Represents the context of a pack.
 *
 * @param <T> the module type
 */
public class PackContext<T extends PackModule<T>> {
    private final HydraulicImpl hydraulic;
    private final ModInfo mod;
    private final T module;
    private final Logger logger;

    public PackContext(@NotNull HydraulicImpl hydraulic, @NotNull ModInfo mod, @NotNull T module) {
        this.hydraulic = hydraulic;
        this.mod = mod;
        this.module = module;
        this.logger = module.logger(mod);
    }

    /**
     * Gets the mod that owns this pack.
     *
     * @return the mod that owns this pack
     */
    @NotNull
    public ModInfo mod() {
        return this.mod;
    }

    /**
     * Gets the storage for the mod that owns this pack.
     *
     * @return the storage for the mod that owns this pack
     */
    @NotNull
    public ModStorage storage() {
        return this.hydraulic.modStorage(this.mod);
    }

    /**
     * Gets the module that this context is part of.
     *
     * @return the module that this context is part of
     */
    @NotNull
    public T module() {
        return this.module;
    }

    /**
     * Gets the logger for this pack.
     *
     * @return the logger for this pack
     */
    @NotNull
    public Logger logger() {
        return this.logger;
    }

    /**
     * Gets the values from the specified {@link Registry registry}
     * that are relevant for the {@link ModInfo mod} this pack is
     * part of.
     *
     * @param registry the registry to get the values from
     * @return the values from the specified registry that are relevant for this mod
     * @param <V> the type of the registry
     */
    @NotNull
    @SuppressWarnings("RedundantCast")
    public <V> List<V> registryValues(@NotNull DefaultedRegistry<? extends V> registry) {
        final List<ResourceLocation> locations;
        if (registry == BuiltInRegistries.BLOCK) {
            locations = hydraulic.getPackManager()
                .getModsToBlocks()
                .get(mod.id());
        } else if (registry == BuiltInRegistries.ITEM) {
            locations = hydraulic.getPackManager()
                .getModsToItems()
                .get(mod.id());
        } else {
            locations = List.of();
        }
        return Lists.transform(locations, (k) -> {
            return registry.get(k).get().value();
        });
    }
}

