package org.geysermc.hydraulic.item;

/**
 * Represents a creative category.
 */
public enum CreativeCategory {
    COMMANDS("commands", 1),
    CONSTRUCTION("construction", 2),
    EQUIPMENT("equipment", 3),
    ITEMS("items", 4),
    NATURE("nature", 5),
    NONE("none", 6);

    private final String internalname;
    private final int id;

    CreativeCategory(String internalname, int id) {
        this.internalname = internalname;
        this.id = id;
    }

    /**
     * Get the internal name of the creative category.
     *
     * @return the internal name of the creative category
     */
    public String internalName() {
        return this.internalname;
    }

    /**
     * Get the ID of the creative category.
     *
     * @return the ID of the creative category
     */
    public int id() {
        return id;
    }
}
