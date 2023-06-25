package org.geysermc.hydraulic.item;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a creative mapping for the creative
 * group and {@link CreativeCategory category}.
 */
public record CreativeMapping(@NotNull String creativeGroup, @NotNull CreativeCategory creativeCategory) {
    public CreativeMapping(String creativeGroup) {
        this(creativeGroup, CreativeCategory.ITEMS);
    }

    public CreativeMapping(CreativeCategory creativeCategory) {
        this(switch (creativeCategory) {
            case COMMANDS -> "itemGroup.name.commands";
            case CONSTRUCTION -> "itemGroup.name.construction";
            case EQUIPMENT -> "itemGroup.name.equipment";
            case NATURE -> "itemGroup.name.nature";
            case ITEMS, NONE -> "itemGroup.name.items";
        }, creativeCategory);
    }
}
