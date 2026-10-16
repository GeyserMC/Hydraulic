package org.geysermc.hydraulic.platform.mod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

/**
 * A mod installed on the server.
 *
 * @param roots the mod's content roots. For a jar mod these live in a zip filesystem, so they are
 *              readable but are not files on disk.
 * @param origin the jar(s) on disk the mod was loaded from, empty when it has none. A JiJ'd mod
 *               reports the outer jar it is nested inside, since that is where it physically lives.
 */
public record ModInfo(
    @NotNull String id,
    @NotNull String namespace,
    @NotNull String name,
    @NotNull String version,
    @Nullable Path iconPath,
    @NotNull Collection<Path> roots,
    @NotNull Collection<Path> origin
) {
    @Nullable
    public Path resolveFile(String file) {
        for (final Path path : roots) {
            final Path resolved = path.resolve(file.replace("/", path.getFileSystem().getSeparator()));
            if (Files.isRegularFile(resolved)) {
                return resolved;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ModInfo other && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
