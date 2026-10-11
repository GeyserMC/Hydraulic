package org.geysermc.hydraulic.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// TODO: Move into PackConverter
public final class BlockSounds {
    private static final String DEFAULT_GROUP = "stone";
    private static final String MINECRAFT = "minecraft";

    private static final Map<String, String> RENAMED = Map.ofEntries(
        Map.entry("wool", "cloth"),
        Map.entry("slime_block", "slime"),
        Map.entry("nether_bricks", "nether_brick"),
        Map.entry("netherite_block", "netherite"),
        Map.entry("moss", "moss_block"),
        Map.entry("amethyst", "amethyst_block"),
        Map.entry("coral_block", "coral"),
        Map.entry("vine", "vines"),
        Map.entry("weeping_vines", "vines"),
        Map.entry("twisting_vines", "vines"),
        Map.entry("frogspawn", "frog_spawn"),
        Map.entry("lily_pad", "grass"),
        Map.entry("wet_grass", "grass"),
        Map.entry("crop", "grass"),
        Map.entry("hard_crop", "grass"),
        Map.entry("leaf_litter", "grass"),
        Map.entry("cactus_flower", "grass"),
        Map.entry("rooted_dirt", "dirt"),
        Map.entry("small_dripleaf", "big_dripleaf"),
        Map.entry("nether_ore", "netherrack"),
        Map.entry("gilded_blackstone", "stone"),
        Map.entry("copper_golem_statue", "copper"),
        Map.entry("shelf", "wood"),
        Map.entry("dried_ghast", "stone"),
        Map.entry("empty", "normal"),
        Map.entry("cobweb", "normal")
    );

    private static final Map<SoundType, String> VANILLA = vanillaGroups();

    private final JsonObject blocks = new JsonObject();
    private final JsonObject blockSounds = new JsonObject();
    private final JsonObject interactiveSounds = new JsonObject();

    private BlockSounds() {
        JsonArray formatVersion = new JsonArray();
        formatVersion.add(1);
        formatVersion.add(1);
        formatVersion.add(0);
        this.blocks.add("format_version", formatVersion);
    }

    /**
     * Creates a {@link BlockSounds} for the list of blocks.
     *
     * @param blocks the blocks
     * @return the sounds, to be written to the resource pack
     */
    @NotNull
    public static BlockSounds of(@NotNull List<Block> blocks) {
        BlockSounds sounds = new BlockSounds();
        for (Block block : blocks) {
            Identifier id = BuiltInRegistries.BLOCK.getKey(block);
            JsonObject entry = new JsonObject();
            entry.addProperty("sound", sounds.group(block.defaultBlockState().getSoundType()));
            sounds.blocks.add(id.toString(), entry);
        }
        return sounds;
    }

    /**
     * Gets the contents of {@code blocks.json}.
     *
     * @return the block definitions, each giving its sound group
     */
    @NotNull
    public JsonObject blocks() {
        return this.blocks;
    }

    /**
     * Gets the contents of {@code sounds.json}, defining the sound groups made for sound types using a
     * mod's own sounds.
     *
     * @return the sound groups, or null if every block uses one of Bedrock's
     */
    @Nullable
    public JsonObject sounds() {
        if (this.blockSounds.isEmpty()) {
            return null;
        }

        JsonObject interactive = new JsonObject();
        interactive.add("block_sounds", this.interactiveSounds);

        JsonObject sounds = new JsonObject();
        sounds.add("block_sounds", this.blockSounds);
        sounds.add("interactive_sounds", interactive);
        return sounds;
    }

    @NotNull
    private String group(@NotNull SoundType type) {
        String vanilla = VANILLA.get(type);
        if (vanilla != null) {
            return vanilla;
        }

        // A sound type made from vanilla sounds plays like the vanilla type it borrows its break sound from.
        Identifier breakSound = type.getBreakSound().location();
        if (breakSound.getNamespace().equals(MINECRAFT)) {
            for (Map.Entry<SoundType, String> entry : VANILLA.entrySet()) {
                if (entry.getKey().getBreakSound().location().equals(breakSound)) {
                    return entry.getValue();
                }
            }
            return DEFAULT_GROUP;
        }

        String group = breakSound.getNamespace() + "." + breakSound.getPath().replace('/', '.');
        if (!this.blockSounds.has(group)) {
            this.define(group, type);
        }
        return group;
    }

    // Volumes and pitches as Java scales a sound type's for each event.
    private void define(@NotNull String group, @NotNull SoundType type) {
        float volume = type.getVolume();
        float pitch = type.getPitch();

        JsonObject events = new JsonObject();
        events.add("break", event(type.getBreakSound(), (volume + 1f) / 2f, pitch * 0.8f));
        events.add("place", event(type.getPlaceSound(), (volume + 1f) / 2f, pitch * 0.8f));
        events.add("hit", event(type.getHitSound(), (volume + 1f) / 8f, pitch * 0.5f));
        this.blockSounds.add(group, groupOf(events));

        JsonObject interactive = new JsonObject();
        interactive.add("step", event(type.getStepSound(), volume * 0.15f, pitch));
        interactive.add("fall", event(type.getFallSound(), volume * 0.5f, pitch * 0.75f));
        interactive.add("jump", event(type.getStepSound(), volume * 0.12f, pitch));
        interactive.add("land", event(type.getFallSound(), volume * 0.18f, pitch));
        this.interactiveSounds.add(group, groupOf(interactive));
    }

    @NotNull
    private static JsonObject groupOf(@NotNull JsonObject events) {
        JsonObject group = new JsonObject();
        group.addProperty("volume", 1f);
        group.addProperty("pitch", 1f);
        group.add("events", events);
        return group;
    }

    @NotNull
    private static JsonObject event(@NotNull SoundEvent sound, float volume, float pitch) {
        JsonObject event = new JsonObject();
        event.addProperty("sound", sound.location().toString());
        event.addProperty("volume", volume);
        event.addProperty("pitch", pitch);
        return event;
    }

    @NotNull
    private static Map<SoundType, String> vanillaGroups() {
        Map<SoundType, String> groups = new IdentityHashMap<>();
        for (Field field : SoundType.class.getFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || field.getType() != SoundType.class) {
                continue;
            }

            try {
                String name = field.getName().toLowerCase(Locale.ROOT);
                groups.putIfAbsent((SoundType) field.get(null), RENAMED.getOrDefault(name, name));
            } catch (IllegalAccessException ignored) {
            }
        }
        return groups;
    }
}
