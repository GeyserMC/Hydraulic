package org.geysermc.hydraulic.schema;

import org.geysermc.hydraulic.schema.converter.ConverterOptions;
import org.geysermc.hydraulic.schema.converter.JsonTemplateToClassConverter;
import org.geysermc.hydraulic.schema.converter.MultiConvertType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class PackSchemaGenerator {
    private static final String PACKAGE_NAME = "org.geysermc.hydraulic.pack.bedrock.resource";

    private static final String GENERAL_SCHEMA_PATH = "/schema/source/general";
    private static final String RESOURCE_SCHEMA_PATH = "/schema/source/resource";

    private static final Pattern VERSION_PATTERN = Pattern.compile("(?!\\.)(\\d+(\\.\\d+)+)");

    public static void main(String[] args) throws Exception {
        generateSchema(GENERAL_SCHEMA_PATH);
        generateSchema(RESOURCE_SCHEMA_PATH);
    }

    private static void generateSchema(@NotNull String schemaPath) throws Exception {
        URI uri = PackSchemaGenerator.class.getResource(schemaPath).toURI();
        Path myPath;
        if (uri.getScheme().equals("jar")) {
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
            myPath = fileSystem.getPath(schemaPath);
        } else {
            myPath = Paths.get(uri);
        }

        Path output = Paths.get("pack-schema/bedrock/src/main/java");
        try (Stream<Path> paths = Files.walk(myPath)) {
            paths.forEach(path -> {
                if (Files.isDirectory(path) || !path.toString().endsWith(".json")) {
                    return;
                }

                try {
                    Path relativePath = myPath.relativize(path);
                    String packageName = PACKAGE_NAME;
                    if (relativePath.getParent() != null) {
                        String pathName = relativePath.getParent().toString();
                        Matcher matcher = VERSION_PATTERN.matcher(pathName);
                        if (matcher.find()) {
                            pathName = matcher.replaceAll(result -> "v" + result.group(1).replace(".", "_"));
                        }

                        packageName += "." + pathName.replace(File.separator, ".");
                    }

                    JsonTemplateToClassConverter.convert(
                            schemaPath + "/" +
                                    relativePath,
                            packageName,
                            output,
                            ConverterOptions.builder()
                                    .multiConvertType(MultiConvertType.COMPLEX)
                                    .collisionPrefix("Minecraft")
                                    .schemaConfig("schema-config.json")
                                    .build()
                    );
                } catch (Exception ex) {
                    System.err.println("An error occurred converting " + path);
                    ex.printStackTrace();
                }
            });
        }
    }
}
