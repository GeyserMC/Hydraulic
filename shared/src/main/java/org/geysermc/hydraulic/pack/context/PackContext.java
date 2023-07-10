package org.geysermc.hydraulic.pack.context;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.hydraulic.storage.ModStorage;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Represents the context of a pack.
 *
 * @param <T> the module type
 */
public class PackContext<T extends PackModule<T>> {
    private final HydraulicImpl hydraulic;
    private final ModInfo mod;
    private final T module;

    public PackContext(@NotNull HydraulicImpl hydraulic, @NotNull ModInfo mod, @NotNull T module) {
        this.hydraulic = hydraulic;
        this.mod = mod;
        this.module = module;
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
     * Gets the values from the specified {@link Registry registry}
     * that are relevant for the {@link ModInfo mod} this pack is
     * part of.
     *
     * @param key the key of the registry to get the values from
     * @return the values from the specified registry that are relevant for this mod
     * @param <V> the type of the registry
     */
    @NotNull
    public <V> List<V> registryValues(@NotNull ResourceKey<Registry<V>> key) {
        Registry<V> registry = this.hydraulic.server().registryAccess().registryOrThrow(key);
        return registry.entrySet().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(this.mod.id()))
                .map(Map.Entry::getValue)
                .toList();
    }
}

