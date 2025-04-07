package org.geysermc.hydraulic.pack;

import org.geysermc.pack.converter.converter.ActionListener;
import org.geysermc.pack.converter.data.ConversionData;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Represents a pack module which listens for more complex
 * behavior from the pack converter.
 *
 * @param <T> the pack module
 * @param <C> the conversion data
 */
public abstract class ConvertablePackModule<T extends PackModule<T>, C extends ConversionData> extends PackModule<T> implements ActionListener<C> {
    protected static final Map<String, String> DIRECTORY_LOCATIONS = Map.of(
            "block", "blocks",
            "item", "items"
    );

    private final Class<C> conversionType;

    public ConvertablePackModule(@NotNull Class<C> conversionType) {
        this.conversionType = conversionType;
    }

    /**
     * Gets the conversion type of this pack module.
     *
     * @return the conversion type
     */
    public final Class<C> conversionType() {
        return this.conversionType;
    }
}
