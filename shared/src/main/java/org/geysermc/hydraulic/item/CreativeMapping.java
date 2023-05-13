package org.geysermc.hydraulic.item;

public record CreativeMapping(String creativeGroup, CreativeCategory creativeCategory) {
    public CreativeMapping(String creativeGroup) {
        this(creativeGroup, CreativeCategory.ITEMS);
    }

    public CreativeMapping(CreativeCategory creativeCategory) {
        this(switch (creativeCategory) {
            case CONSTRUCTION -> "itemGroup.name.construction";
            case EQUIPMENT -> "itemGroup.name.equipment";
            case ITEMS -> "itemGroup.name.items";
            case NATURE -> "itemGroup.name.nature";
        }, creativeCategory);
    }
}
