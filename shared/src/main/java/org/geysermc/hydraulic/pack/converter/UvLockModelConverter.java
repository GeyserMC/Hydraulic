package org.geysermc.hydraulic.pack.converter;

import net.kyori.adventure.key.Key;
import org.geysermc.hydraulic.block.UvLockBaker;
import org.geysermc.pack.converter.pipeline.AssetExtractor;
import org.geysermc.pack.converter.pipeline.ExtractionContext;
import org.geysermc.pack.converter.type.model.ModelStitcher;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.blockstate.Selector;
import team.unnamed.creative.blockstate.Variant;
import team.unnamed.creative.model.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Extracts a baked model for every uvlocked, rotated blockstate variant, see {@link UvLockBaker}.
 */
public class UvLockModelConverter implements AssetExtractor<Model> {
    private final ModelStitcher.Provider modelProvider;

    public UvLockModelConverter(ModelStitcher.Provider modelProvider) {
        this.modelProvider = modelProvider;
    }

    @Override
    public Collection<Model> extract(ResourcePack pack, ExtractionContext context) {
        List<Variant> variants = pack.blockStates().stream()
                .flatMap(state -> Stream.concat(state.variants().values().stream(), state.multipart().stream().map(Selector::variant)))
                .flatMap(multiVariant -> multiVariant.variants().stream())
                .filter(UvLockBaker::needsBaking)
                .toList();

        List<Model> models = new ArrayList<>();
        Set<Key> baked = new HashSet<>();
        for (Variant variant : variants) {
            Model model = this.modelProvider.model(variant.model());
            if (model == null || !baked.add(UvLockBaker.bakedKey(variant.model(), variant.x(), variant.y()))) {
                continue;
            }

            Model stitched = new ModelStitcher(this.modelProvider, model, context.logListener()).stitch();
            Model bakedModel = stitched == null ? null : UvLockBaker.bake(stitched, variant.x(), variant.y());
            if (bakedModel != null) {
                models.add(bakedModel);
            }
        }

        return models;
    }
}
