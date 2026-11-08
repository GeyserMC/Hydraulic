package org.geysermc.hydraulic.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.base.Writable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Utility class for images.
 */
public class ImageUtil {
    /**
     * Reads an image from the given data.
     *
     * @param data the image data, EG a texture
     * @return the image, or null if it couldn't be read
     */
    @Nullable
    public static BufferedImage read(@NotNull Writable data) {
        try {
            return ImageIO.read(new ByteArrayInputStream(data.toByteArray()));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Checks if the given image data has any pixels that aren't fully opaque.
     *
     * @param data the image data, EG a texture
     * @return true if the image has transparent pixels
     */
    public static boolean hasTransparency(@NotNull Writable data) {
        BufferedImage image = read(data);
        if (image == null || !image.getColorModel().hasAlpha()) {
            return false;
        }

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if ((image.getRGB(x, y) >>> 24) < 255) {
                    return true;
                }
            }
        }

        return false;
    }
}
