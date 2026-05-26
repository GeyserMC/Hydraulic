package org.geysermc.hydraulic.util;

import org.geysermc.hydraulic.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Validates the Java2D/ImageIO operations used by pack texture conversion before
 * worker threads start doing the same work concurrently.
 */
public final class ImagePipelineDiagnostics {
    private static final Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_NAME + "/ImagePipelineDiagnostics");

    private ImagePipelineDiagnostics() {
    }

    public static boolean validate(Path diagnosticsDirectory) {
        LOGGER.info("Validating Java image pipeline before pack conversion");
        logEnvironment();

        AtomicBoolean success = new AtomicBoolean(true);
        BufferedImage source = check("Java2D ARGB surface and text rendering", success, ImagePipelineDiagnostics::createRenderedImage);
        if (source != null) {
            check("Java2D scaled draw", success, () -> scaledDraw(source));
            check("Java2D affine transform", success, () -> affineScale(source));
            byte[] pngBytes = check("ImageIO PNG memory round-trip", success, () -> imageIoMemoryRoundTrip(source));
            if (pngBytes != null) {
                check("ImageIO PNG file round-trip", success, () -> imageIoFileRoundTrip(diagnosticsDirectory, pngBytes));
            }
        }

        check("Font discovery", success, ImagePipelineDiagnostics::discoverFonts);

        if (success.get()) {
            LOGGER.info("Java image pipeline validation completed successfully");
        } else {
            LOGGER.error("Java image pipeline validation failed; Hydraulic pack conversion will be skipped to avoid worker thread crashes");
            LOGGER.error("For minimal Linux containers, verify that the runtime includes the java.desktop module, writable temp/cache directories, fontconfig, and at least one system font package");
        }

        return success.get();
    }

    private static void logEnvironment() {
        LOGGER.info(
                "Image runtime: java={} vendor={} os={} {} headless={} DISPLAY={} java.io.tmpdir={}",
                System.getProperty("java.version"),
                System.getProperty("java.vendor"),
                System.getProperty("os.name"),
                System.getProperty("os.arch"),
                safeHeadlessValue(),
                System.getenv("DISPLAY"),
                System.getProperty("java.io.tmpdir")
        );

        try {
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            LOGGER.info(
                    "Graphics environment: {} headlessInstance={}",
                    environment.getClass().getName(),
                    environment.isHeadlessInstance()
            );
        } catch (Throwable throwable) {
            LOGGER.warn("Unable to initialize GraphicsEnvironment for diagnostics", throwable);
        }

        LOGGER.info("ImageIO cache: useCache={} cacheDirectory={}", ImageIO.getUseCache(), ImageIO.getCacheDirectory());
        LOGGER.info("ImageIO PNG support: reader={} writer={}", hasFormat(ImageIO.getReaderFormatNames(), "png"), hasFormat(ImageIO.getWriterFormatNames(), "png"));
    }

    private static String safeHeadlessValue() {
        try {
            return Boolean.toString(GraphicsEnvironment.isHeadless());
        } catch (Throwable throwable) {
            return "unavailable: " + throwable.getClass().getName();
        }
    }

    private static BufferedImage createRenderedImage() {
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        try {
            graphics.setComposite(AlphaComposite.Src);
            graphics.setColor(new Color(0, 0, 0, 0));
            graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
            graphics.setComposite(AlphaComposite.SrcOver);
            graphics.setColor(new Color(255, 0, 64, 192));
            graphics.fillRect(1, 1, 14, 14);
            graphics.setColor(new Color(0, 255, 128, 224));
            graphics.drawLine(0, 15, 15, 0);
            graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));
            graphics.drawString("OK", 2, 10);
        } finally {
            graphics.dispose();
        }
        return image;
    }

    private static BufferedImage scaledDraw(BufferedImage source) {
        BufferedImage scaled = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = scaled.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.drawImage(source, 0, 0, scaled.getWidth(), scaled.getHeight(), null);
        } finally {
            graphics.dispose();
        }
        return scaled;
    }

    private static BufferedImage affineScale(BufferedImage source) {
        BufferedImage scaled = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
        AffineTransform transform = AffineTransform.getScaleInstance(0.5d, 0.5d);
        AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        return operation.filter(source, scaled);
    }

    private static byte[] imageIoMemoryRoundTrip(BufferedImage source) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (!ImageIO.write(source, "png", outputStream)) {
            throw new IOException("No ImageIO writer accepted PNG output");
        }

        byte[] bytes = outputStream.toByteArray();
        BufferedImage read = ImageIO.read(new ByteArrayInputStream(bytes));
        if (read == null) {
            throw new IOException("No ImageIO reader accepted generated PNG input");
        }

        return bytes;
    }

    private static BufferedImage imageIoFileRoundTrip(Path diagnosticsDirectory, byte[] pngBytes) throws IOException {
        Files.createDirectories(diagnosticsDirectory);
        Path file = diagnosticsDirectory.resolve("imageio-preflight.png");
        try {
            Files.write(file, pngBytes);
            BufferedImage read = ImageIO.read(file.toFile());
            if (read == null) {
                throw new IOException("No ImageIO reader accepted generated PNG file");
            }
            return read;
        } finally {
            try {
                Files.deleteIfExists(file);
            } catch (IOException exception) {
                LOGGER.debug("Failed to delete image diagnostics file {}", file, exception);
            }
        }
    }

    private static String[] discoverFonts() {
        String[] families = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(Locale.ROOT);
        if (families.length == 0) {
            throw new IllegalStateException("No font families were discovered");
        }

        LOGGER.info("Discovered {} font families; first entries={}", families.length, Arrays.toString(Arrays.copyOf(families, Math.min(5, families.length))));
        return families;
    }

    private static boolean hasFormat(String[] formats, String expected) {
        for (String format : formats) {
            if (expected.equalsIgnoreCase(format)) {
                return true;
            }
        }
        return false;
    }

    private static <T> T check(String name, AtomicBoolean success, DiagnosticStep<T> step) {
        try {
            T result = step.run();
            LOGGER.info("{}: ok", name);
            return result;
        } catch (Throwable throwable) {
            success.set(false);
            LOGGER.error("{}: failed", name, throwable);
            return null;
        }
    }

    @FunctionalInterface
    private interface DiagnosticStep<T> {
        T run() throws Exception;
    }
}
