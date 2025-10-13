package org.geysermc.hydraulic.pack;

import net.kyori.adventure.key.Key;
import org.apache.commons.lang3.StringUtils;
import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.pack.context.PackContext;
import org.geysermc.pack.converter.type.texture.TextureConverter;
import org.jetbrains.annotations.NotNull;

public abstract class TexturePackModule<T extends PackModule<T>> extends PackModule<T> {
    /**
     * Gets the output location of the given key.
     *
     * @param packContext the pack context
     * @param key the key
     * @return the output location
     */
    protected static <T extends PackModule<T>> String getOutputFromModel(@NotNull PackContext<T> packContext, @NotNull Key key) {
        String directory = StringUtils.substringBefore(key.value(), "/");
        String remaining = StringUtils.substringAfter(key.value(), "/");
        String finalDir = TextureConverter.DIRECTORY_LOCATIONS.getOrDefault(directory, directory) + "/" + packContext.mod().id();

        return String.format(Constants.BEDROCK_TEXTURE_LOCATION, finalDir + "/" + remaining);
    }
}
