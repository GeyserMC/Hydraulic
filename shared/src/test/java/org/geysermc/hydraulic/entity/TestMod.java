package org.geysermc.hydraulic.entity;

import org.geysermc.conduit.ConduitContext;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class TestMod {
    static final String CLIENT = "org/geysermc/hydraulic/fabric/test/client/";

    private static Path jar;

    private TestMod() {
    }

    static ConduitContext context() {
        return ConduitContext.load(jar());
    }

    static Map<String, CustomEntity> scan(ConduitContext context) {
        return new EntityScanner(context, LoggerFactory.getLogger("test"))
            .scan(TestRegistries.LAYERS, TestRegistries.RENDERERS)
            .stream().collect(Collectors.toMap(entity -> entity.id().getPath(), Function.identity()));
    }

    private static synchronized Path jar() {
        if (jar != null) {
            return jar;
        }

        String classes = System.getProperty("hydraulic.test.mod");
        if (classes == null) {
            throw new IllegalStateException("hydraulic.test.mod is not set to the test mod's classes");
        }

        try {
            Path root = Path.of(classes);
            jar = Files.createTempFile("hydraulic-test-mod", ".jar");
            jar.toFile().deleteOnExit();
            try (OutputStream out = Files.newOutputStream(jar); JarOutputStream jarOut = new JarOutputStream(out);
                 Stream<Path> files = Files.walk(root)) {
                for (Path file : files.filter(Files::isRegularFile).toList()) {
                    jarOut.putNextEntry(new JarEntry(root.relativize(file).toString().replace('\\', '/')));
                    Files.copy(file, jarOut);
                    jarOut.closeEntry();
                }
            }
            return jar;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
