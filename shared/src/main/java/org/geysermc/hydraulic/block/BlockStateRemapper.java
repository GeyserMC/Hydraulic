package org.geysermc.hydraulic.block;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.world.level.block.Block;
import org.geysermc.geyser.registry.BlockRegistries;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.BitStorage;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.DataPalette;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.GlobalPalette;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.ListPalette;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.MapPalette;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.Palette;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.PaletteType;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.SingletonPalette;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Translates the server's block state ids into the ids Geyser knows them by, for mods that add properties
 * to vanilla blocks and so renumber every state after them.
 */
public final class BlockStateRemapper {
    private static final int AIR = org.geysermc.geyser.level.block.type.Block.JAVA_AIR_ID;

    private BlockStateRemapper() {
    }

    /**
     * Translates a server block state id into the id Geyser knows the state by.
     *
     * @param state the server's id for the state
     * @return Geyser's id for the state
     */
    public static int remapState(int state) {
        int[] table = Table.STATES;
        return table == null || state < 0 || state >= table.length ? state : table[state];
    }

    /**
     * Translates an entire {@link DataPalette} of server block states into the ids Geyser knows them by
     * for mods that add custom properties.
     *
     * @param data the server's block state palette
     * @param type the type of palette, which must be {@link PaletteType#BLOCK_STATE} for remapping to occur
     * @param registrySize the size of the registry, which is used to determine the size of the storage
     * @return a new {@link DataPalette} with the remapped block state ids, or the original palette if no remapping is needed
     */
    public static DataPalette remapPalette(DataPalette data, PaletteType type, int registrySize) {
        if (type != PaletteType.BLOCK_STATE || !BlockStateRemapper.isRemapping()) {
            return data;
        }

        Palette palette = data.getPalette();
        if (palette instanceof SingletonPalette) {
            return DataPalette.create(new SingletonPalette(remapState(palette.idToState(0))), null, type, registrySize);
        }

        BitStorage storage = data.getStorage();
        if (palette instanceof GlobalPalette) {
            for (int i = 0; i < type.getStorageSize(); i++) {
                storage.set(i, remapState(storage.get(i)));
            }
            return data;
        }

        Palette remapped = palette instanceof ListPalette ? new ListPalette(storage.getBitsPerEntry()) : new MapPalette(storage.getBitsPerEntry());
        int[] moved = new int[palette.size()];
        boolean shifted = false;
        for (int i = 0; i < palette.size(); i++) {
            moved[i] = remapped.stateToId(BlockStateRemapper.remapState(palette.idToState(i)));
            shifted |= moved[i] != i;
        }

        if (shifted) {
            for (int i = 0; i < type.getStorageSize(); i++) {
                storage.set(i, moved[storage.get(i)]);
            }
        }

        return DataPalette.create(remapped, storage, type, registrySize);
    }

    /**
     * Checks whether any server block state is known to Geyser by a different id.
     * <p>
     * If this returns true, {@link #remapState(int)} and {@link #remapPalette(DataPalette, PaletteType, int)} should be used
     * to translate server block state ids into Geyser's ids, but also serves as a quick check to avoid unnecessary remapping
     * when no mods that require remapping are present.
     *
     * @return whether block states are remapped
     */
    public static boolean isRemapping() {
        return Table.STATES != null;
    }

    /**
     * Matches each server block state with the Geyser state of the same block and properties, dropping
     * properties Geyser's block doesn't have. A state no Geyser state matches becomes air.
     *
     * @param server each server block state, as {@code namespace:block[property=value,...]}, by id
     * @param geyser the id of each block state Geyser knows
     * @return Geyser's id for each server state, or null if every state already has the same id
     */
    @VisibleForTesting
    static int[] buildTable(@NotNull List<String> server, @NotNull Object2IntMap<String> geyser) {
        Map<String, GeyserBlock> blocks = new HashMap<>();
        for (Object2IntMap.Entry<String> entry : geyser.object2IntEntrySet()) {
            ParsedState parsed = ParsedState.of(entry.getKey());
            GeyserBlock block = blocks.computeIfAbsent(parsed.block(), _ -> new GeyserBlock(new HashMap<>()));
            block.states().put(parsed.properties(), entry.getIntValue());
        }

        int[] table = new int[server.size()];
        boolean identity = true;
        for (int id = 0; id < table.length; id++) {
            ParsedState state = ParsedState.of(server.get(id));
            GeyserBlock block = blocks.get(state.block());
            table[id] = block == null ? AIR : block.match(state.properties());
            identity &= table[id] == id;
        }

        return identity ? null : table;
    }

    private record GeyserBlock(Map<Map<String, String>, Integer> states) {

        int match(@NotNull Map<String, String> properties) {
            Map<String, String> kept = new TreeMap<>(properties);
            kept.keySet().retainAll(this.propertyNames());
            Integer exact = this.states.get(kept);
            if (exact != null) {
                return exact;
            }

            int closest = AIR;
            int closestShared = -1;
            for (Map.Entry<Map<String, String>, Integer> candidate : this.states.entrySet()) {
                int shared = 0;
                for (Map.Entry<String, String> property : candidate.getKey().entrySet()) {
                    if (property.getValue().equals(kept.get(property.getKey()))) {
                        shared++;
                    }
                }

                if (shared > closestShared || shared == closestShared && candidate.getValue() < closest) {
                    closest = candidate.getValue();
                    closestShared = shared;
                }
            }

            return closest;
        }

        private Set<String> propertyNames() {
            return this.states.keySet().iterator().next().keySet();
        }
    }

    private record ParsedState(String block, Map<String, String> properties) {

        static ParsedState of(@NotNull String state) {
            int open = state.indexOf('[');
            if (open < 0) {
                return new ParsedState(state, Map.of());
            }

            Map<String, String> properties = new TreeMap<>();
            for (String property : state.substring(open + 1, state.length() - 1).split(",")) {
                int equals = property.indexOf('=');
                if (equals > 0) {
                    properties.put(property.substring(0, equals), property.substring(equals + 1));
                }
            }

            return new ParsedState(state.substring(0, open), properties);
        }
    }

    private static final class Table {
        static final int[] STATES = build();

        private static int[] build() {
            List<String> server = new ArrayList<>(Block.BLOCK_STATE_REGISTRY.size());
            for (int id = 0; id < Block.BLOCK_STATE_REGISTRY.size(); id++) {
                server.add(BlockStateParser.serialize(Block.stateById(id)));
            }

            return BlockStateRemapper.buildTable(server, BlockRegistries.JAVA_BLOCK_STATE_IDENTIFIER_TO_ID.get());
        }
    }
}
