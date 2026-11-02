package org.geysermc.hydraulic.block;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.geysermc.pack.bedrock.resource.Blocks;
import org.geysermc.pack.bedrock.resource.Sounds;
import org.geysermc.pack.bedrock.resource.sounds.InteractiveSounds;
import org.geysermc.pack.bedrock.resource.sounds.blocksounds.Events;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
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

    private final Map<String, Blocks> blocks = new LinkedHashMap<>();
    private final Map<String, org.geysermc.pack.bedrock.resource.sounds.BlockSounds> blockSounds = new LinkedHashMap<>();
    private final Map<String, org.geysermc.pack.bedrock.resource.sounds.interactivesounds.BlockSounds> interactiveSounds = new LinkedHashMap<>();

    private BlockSounds() {
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
            Blocks entry = new Blocks();
            entry.sound(sounds.group(block.defaultBlockState().getSoundType()));
            sounds.blocks.put(id.toString(), entry);
        }
        return sounds;
    }

    /**
     * Gets the contents of {@code blocks.json}.
     *
     * @return the block definitions by block id, each giving its sound group
     */
    @NotNull
    public Map<String, Blocks> blocks() {
        return this.blocks;
    }

    /**
     * Gets the contents of {@code sounds.json}, defining the sound groups made for sound types using a
     * mod's own sounds.
     *
     * @return the sound groups, or null if every block uses one of Bedrock's
     */
    @Nullable
    public Sounds sounds() {
        if (this.blockSounds.isEmpty()) {
            return null;
        }

        InteractiveSounds interactive = new InteractiveSounds();
        interactive.blockSounds(this.interactiveSounds);

        Sounds sounds = new Sounds();
        sounds.blockSounds(this.blockSounds);
        sounds.interactiveSounds(interactive);
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
        if (!this.blockSounds.containsKey(group)) {
            this.define(group, type);
        }
        return group;
    }

    // Volumes and pitches as Java scales a sound type's for each event.
    private void define(@NotNull String group, @NotNull SoundType type) {
        float volume = type.getVolume();
        float pitch = type.getPitch();

        Map<String, Events> events = new LinkedHashMap<>();
        events.put("break", event(type.getBreakSound(), (volume + 1f) / 2f, pitch * 0.8f));
        events.put("place", event(type.getPlaceSound(), (volume + 1f) / 2f, pitch * 0.8f));
        events.put("hit", event(type.getHitSound(), (volume + 1f) / 8f, pitch * 0.5f));
        org.geysermc.pack.bedrock.resource.sounds.BlockSounds blockSounds = new org.geysermc.pack.bedrock.resource.sounds.BlockSounds();
        blockSounds.events(events);
        this.blockSounds.put(group, blockSounds);

        Map<String, org.geysermc.pack.bedrock.resource.sounds.interactivesounds.blocksounds.Events> interactive = new LinkedHashMap<>();
        interactive.put("step", interactiveEvent(type.getStepSound(), volume * 0.15f, pitch));
        interactive.put("fall", interactiveEvent(type.getFallSound(), volume * 0.5f, pitch * 0.75f));
        interactive.put("jump", interactiveEvent(type.getStepSound(), volume * 0.12f, pitch));
        interactive.put("land", interactiveEvent(type.getFallSound(), volume * 0.18f, pitch));
        org.geysermc.pack.bedrock.resource.sounds.interactivesounds.BlockSounds interactiveSounds = new org.geysermc.pack.bedrock.resource.sounds.interactivesounds.BlockSounds();
        interactiveSounds.events(interactive);
        this.interactiveSounds.put(group, interactiveSounds);
    }

    @NotNull
    private static Events event(@NotNull SoundEvent sound, float volume, float pitch) {
        Events event = new Events();
        event.sound(sound.location().toString());
        event.volume(new float[] { volume, volume });
        event.pitch(new float[] { pitch, pitch });
        return event;
    }

    @NotNull
    private static org.geysermc.pack.bedrock.resource.sounds.interactivesounds.blocksounds.Events interactiveEvent(@NotNull SoundEvent sound, float volume, float pitch) {
        org.geysermc.pack.bedrock.resource.sounds.interactivesounds.blocksounds.Events event = new org.geysermc.pack.bedrock.resource.sounds.interactivesounds.blocksounds.Events();
        event.sound(sound.location().toString());
        event.volume(new float[] { volume, volume });
        event.pitch(new float[] { pitch, pitch });
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
