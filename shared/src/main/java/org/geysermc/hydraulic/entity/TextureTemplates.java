package org.geysermc.hydraulic.entity;

import org.geysermc.conduit.Value;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Finds the textures a texture location built at runtime can resolve to, such as
 * {@code "textures/entity/moobloom/moobloom_" + variant + ".png"}, by matching it against the
 * textures mods ship.
 */
final class TextureTemplates {
    private static final String ASSETS = "assets/";
    private static final String UNKNOWN = "[^/]*";
    private static final int LIMIT = 64;
    private static final Set<String> FACTORIES = Set.of("fromNamespaceAndPath", "tryBuild", "<init>");
    private static final Set<String> PARSERS = Set.of("parse", "tryParse", "withDefaultNamespace");

    private TextureTemplates() {
    }

    /**
     * Lists the textures a texture location can resolve to.
     *
     * @param texture the texture location, as summarized
     * @param assets the assets to match against, from {@link #assets(Collection)}
     * @return every matching texture, as {@code namespace:path}, sorted
     */
    @NotNull
    static List<String> expand(@NotNull Value texture, @NotNull List<String> assets) {
        List<Pattern> patterns = new ArrayList<>();
        for (String template : identifiers(texture)) {
            if (template.contains(UNKNOWN)) {
                patterns.add(Pattern.compile(template));
            }
        }

        if (patterns.isEmpty()) {
            return List.of();
        }

        Set<String> found = new TreeSet<>();
        for (String asset : assets) {
            for (Pattern pattern : patterns) {
                if (pattern.matcher(asset).matches()) {
                    found.add(asset);
                }
            }
        }

        return List.copyOf(found);
    }

    /**
     * Lists the assets in some jars and folders.
     *
     * @param sources the jars and folders
     * @return every asset, as {@code namespace:path}
     */
    @NotNull
    static List<String> assets(@NotNull Collection<Path> sources) {
        List<String> assets = new ArrayList<>();
        for (Path source : sources) {
            try {
                if (Files.isRegularFile(source)) {
                    try (JarFile jar = new JarFile(source.toFile())) {
                        jar.stream().map(JarEntry::getName).forEach(name -> asset(name, assets));
                    }
                } else if (Files.isDirectory(source)) {
                    try (Stream<Path> files = Files.walk(source)) {
                        files.filter(Files::isRegularFile).forEach(file -> asset(source.relativize(file).toString().replace('\\', '/'), assets));
                    }
                }
            } catch (IOException ignored) {
            }
        }

        return assets;
    }

    @NotNull
    private static List<String> identifiers(@NotNull Value value) {
        return switch (value) {
            case Value.Conditional(Value _, Value then, Value otherwise) -> union(identifiers(then), identifiers(otherwise));
            case Value.Multiple(List<Value> values) -> values.stream().flatMap(each -> identifiers(each).stream()).toList();
            case Value.StaticCall(String _, String name, List<Value> args) -> identifier(name, args);
            case Value.Constructed(String _, List<Value> args) -> identifier("<init>", args);
            default -> List.of();
        };
    }

    @NotNull
    private static List<String> identifier(@NotNull String factory, @NotNull List<Value> args) {
        if (FACTORIES.contains(factory) && args.size() == 2) {
            List<String> out = new ArrayList<>();
            for (String namespace : strings(args.get(0))) {
                for (String path : strings(args.get(1))) {
                    out.add(namespace + ":" + path);
                }
            }

            return out;
        }
        if (PARSERS.contains(factory) && args.size() == 1) {
            return strings(args.getFirst()).stream()
                .map(template -> template.contains(":") ? template : "minecraft:" + template)
                .toList();
        }

        return List.of();
    }

    // Templates are regular expressions: literal text quoted, anything unknown a single path segment.
    @NotNull
    private static List<String> strings(@NotNull Value value) {
        return switch (value) {
            case Value.Literal(String text) -> List.of(Pattern.quote(text));
            case Value.Conditional(Value _, Value then, Value otherwise) -> union(strings(then), strings(otherwise));
            case Value.Operation(Value.Operator operator, List<Value> parts) when operator == Value.Operator.CONCAT -> {
                List<String> joined = List.of("");
                for (Value part : parts) {
                    List<String> next = new ArrayList<>();
                    for (String head : joined) {
                        for (String tail : strings(part)) {
                            if (next.size() < LIMIT) {
                                next.add(head + tail);
                            }
                        }
                    }
                    joined = next;
                }

                yield joined;
            }

            default -> List.of(UNKNOWN);
        };
    }

    @NotNull
    private static List<String> union(@NotNull List<String> a, @NotNull List<String> b) {
        List<String> out = new ArrayList<>(a);
        b.stream().filter(each -> !out.contains(each)).forEach(out::add);
        return out;
    }

    private static void asset(@NotNull String name, @NotNull List<String> out) {
        if (!name.startsWith(ASSETS)) {
            return;
        }

        int slash = name.indexOf('/', ASSETS.length());
        if (slash > 0) {
            out.add(name.substring(ASSETS.length(), slash) + ":" + name.substring(slash + 1));
        }
    }
}
