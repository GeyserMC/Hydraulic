package org.geysermc.hydraulic.item;

public enum CreativeCategory {
    CONSTRUCTION(2),
    EQUIPMENT(3),
    ITEMS(4),
    NATURE(5);

    private final int id;

    CreativeCategory(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }
}
