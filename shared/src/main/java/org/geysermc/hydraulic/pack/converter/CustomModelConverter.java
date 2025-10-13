package org.geysermc.hydraulic.pack.converter;

import org.geysermc.pack.converter.pipeline.AssetExtractor;
import org.geysermc.pack.converter.pipeline.ExtractionContext;
import org.geysermc.pack.converter.type.model.ModelStitcher;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.model.Model;

import java.util.Collection;

public class CustomModelConverter implements AssetExtractor<Model> {
    private final ModelStitcher.Provider modelProvider;

    public CustomModelConverter(ModelStitcher.Provider modelProvider) {
        this.modelProvider = modelProvider;
    }

    @Override
    public Collection<Model> extract(ResourcePack pack, ExtractionContext context) {
        // TODO maybe parallel, if model stitching takes a lot of time
        return pack.models().stream()
                .map(model -> new ModelStitcher(this.modelProvider, model, context.logListener()).stitch())
                .toList();
    }
}
