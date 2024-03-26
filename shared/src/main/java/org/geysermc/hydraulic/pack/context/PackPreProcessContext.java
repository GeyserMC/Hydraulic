package org.geysermc.hydraulic.pack.context;

import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.pack.converter.converter.model.ModelStitcher;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.ResourcePack;

import java.util.Collection;
import java.util.function.Function;

/**
 * Represents the context of a pack before it has
 * been created.
 *
 * @param <T> the module type
 */
public class PackPreProcessContext<T extends PackModule<T>> extends PackContext<T> {
    private final Collection<ResourcePack> packs;
    private final ModelStitcher.Provider modelProvider;

    public PackPreProcessContext(
        @NotNull HydraulicImpl hydraulic,
        @NotNull ModInfo mod,
        @NotNull T module,
        @NotNull Collection<ResourcePack> packs,
        @NotNull ModelStitcher.Provider modelProvider
    ) {
        super(hydraulic, mod, module);

        this.packs = packs;
        this.modelProvider = modelProvider;
    }

    /**
     * Gets the pack.
     *
     * @return the pack
     */
    @NotNull
    public Collection<ResourcePack> packs() {
        return this.packs;
    }

    @NotNull
    public ModelStitcher.Provider modelProvider() {
        return modelProvider;
    }

    @NotNull
    public <A> Iterable<A> assets(Function<ResourcePack, Collection<A>> extractor) {
        return packs.stream().map(extractor).flatMap(Collection::stream)::iterator;
    }
}
