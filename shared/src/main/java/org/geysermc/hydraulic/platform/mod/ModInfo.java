package org.geysermc.hydraulic.platform.mod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

public record ModInfo(
    @NotNull String id,
    @NotNull String namespace,
    @NotNull String name,
    @NotNull String version,
    @Nullable Path iconPath,
    @NotNull Collection<Path> roots
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
