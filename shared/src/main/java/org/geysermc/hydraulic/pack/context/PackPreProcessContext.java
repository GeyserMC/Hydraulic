package org.geysermc.hydraulic.pack.context;

import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.pack.converter.type.model.ModelStitcher;
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
    private final ResourcePack vanillaPack;

    public PackPreProcessContext(
        @NotNull HydraulicImpl hydraulic,
        @NotNull ModInfo mod,
        @NotNull T module,
        @NotNull Collection<ResourcePack> packs,
        @NotNull ModelStitcher.Provider modelProvider,
        @NotNull ResourcePack vanillaPack
    ) {
        super(hydraulic, mod, module);

        this.packs = packs;
        this.modelProvider = modelProvider;
        this.vanillaPack = vanillaPack;
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

    /**
     * Gets the stripped Vanilla resource pack, containing models, blockstates and a few textures.
     *
     * @return the Vanilla resource pack
     */
    @NotNull
    public ResourcePack vanillaPack() {
        return vanillaPack;
    }

    @NotNull
    public <A> Iterable<A> assets(Function<ResourcePack, Collection<A>> extractor) {
        return packs.stream().map(extractor).flatMap(Collection::stream)::iterator;
    }
}
