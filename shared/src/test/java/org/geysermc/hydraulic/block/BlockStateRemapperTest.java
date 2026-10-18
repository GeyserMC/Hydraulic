package org.geysermc.hydraulic.block;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BlockStateRemapperTest {
    private static final Object2IntMap<String> GEYSER = geyser(
        "minecraft:air",
        "minecraft:oak_slab[type=bottom,waterlogged=false]",
        "minecraft:oak_slab[type=bottom,waterlogged=true]",
        "minecraft:stone",
        "minecraft:cake[bites=0]",
        "minecraft:cake[bites=1]",
        "mod:crystal[lit=false]"
    );

    @Test
    void matchingNumberingNeedsNoTable() {
        assertNull(BlockStateRemapper.buildTable(List.of(
            "minecraft:air",
            "minecraft:oak_slab[type=bottom,waterlogged=false]",
            "minecraft:oak_slab[type=bottom,waterlogged=true]",
            "minecraft:stone",
            "minecraft:cake[bites=0]",
            "minecraft:cake[bites=1]",
            "mod:crystal[lit=false]"
        ), GEYSER));
    }

    @Test
    void addedPropertiesAreDroppedAndLaterStatesShiftBack() {
        int[] table = BlockStateRemapper.buildTable(List.of(
            "minecraft:air",
            "minecraft:oak_slab[ichorlogged=false,type=bottom,waterlogged=false]",
            "minecraft:oak_slab[ichorlogged=true,type=bottom,waterlogged=false]",
            "minecraft:oak_slab[ichorlogged=false,type=bottom,waterlogged=true]",
            "minecraft:oak_slab[ichorlogged=true,type=bottom,waterlogged=true]",
            "minecraft:stone",
            "minecraft:cake[bites=0]",
            "minecraft:cake[bites=1]",
            "mod:fluid[level=0]",
            "mod:crystal[lit=false]"
        ), GEYSER);

        assertArrayEquals(new int[] { 0, 1, 1, 2, 2, 3, 4, 5, 0, 6 }, table);
    }

    @Test
    void valuesGeyserDoesNotKnowFallBackToTheClosestState() {
        int[] table = BlockStateRemapper.buildTable(List.of(
            "minecraft:air",
            "minecraft:oak_slab[waterlogged=false,type=bottom]",
            "minecraft:oak_slab[type=bottom,waterlogged=true]",
            "minecraft:stone",
            "minecraft:cake[bites=1]",
            "minecraft:cake[bites=7]",
            "mod:crystal[lit=false]"
        ), GEYSER);

        assertArrayEquals(new int[] { 0, 1, 2, 3, 5, 4, 6 }, table);
    }

    private static Object2IntMap<String> geyser(String... states) {
        Object2IntMap<String> ids = new Object2IntOpenHashMap<>();
        for (int id = 0; id < states.length; id++) {
            ids.put(states[id], id);
        }
        return ids;
    }
}
