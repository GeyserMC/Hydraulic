package org.geysermc.hydraulic.pack.converter;

import org.geysermc.pack.converter.converter.Converter;
import org.geysermc.pack.converter.converter.model.ModelConverter;
import org.geysermc.pack.converter.converter.model.ModelStitcher;
import org.geysermc.pack.converter.data.ModelConversionData;
import org.jetbrains.annotations.NotNull;

public class CustomModelConverter extends ModelConverter {
    private final ModelStitcher.Provider modelProvider;

    public CustomModelConverter(ModelStitcher.Provider modelProvider) {
        this.modelProvider = modelProvider;
    }

    @Override
    public ModelConversionData createConversionData(@NotNull Converter.ConversionDataCreationContext context) {
        return new ModelConversionData(context.inputDirectory(), context.outputDirectory(), modelProvider);
    }
}
