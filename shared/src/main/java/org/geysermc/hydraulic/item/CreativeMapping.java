package org.geysermc.hydraulic.item;

import org.geysermc.geyser.api.util.CreativeCategory;
import org.geysermc.hydraulic.util.ItemGroup;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a creative mapping for the creative
 * group and {@link CreativeCategory category}.
 */
public record CreativeMapping(@NotNull ItemGroup creativeGroup, @NotNull CreativeCategory creativeCategory) {
    public CreativeMapping(CreativeCategory creativeCategory) {
        this(ItemGroup.NONE, creativeCategory);
    }
}
