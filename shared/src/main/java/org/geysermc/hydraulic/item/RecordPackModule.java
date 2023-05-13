package org.geysermc.hydraulic.item;

import com.google.auto.service.AutoService;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.bedrock.resource.attachables.Attachable;
import org.geysermc.hydraulic.pack.bedrock.resource.attachables.Attachables;
import org.geysermc.hydraulic.pack.bedrock.resource.attachables.attachable.Description;
import org.geysermc.hydraulic.pack.context.PackCreateContext;
import org.geysermc.hydraulic.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoService(PackModule.class)
public class RecordPackModule extends PackModule<RecordPackModule> {
    private static final String JAVA_RECORD_SOUND_LOCATION = "assets/%s/sounds/records/%s_sound.ogg";
    private static final String BEDROCK_RECORD_SOUND_LOCATION = "sounds/records/%s/%s.ogg";

    @Override
    public void create(@NotNull PackCreateContext<RecordPackModule> context) {
        List<RecordItem> recordItems = context.registryValues(Registries.ITEM).stream().filter(item -> item instanceof RecordItem).map(item -> (RecordItem)item).toList();

        LOGGER.info("Records to convert: " + recordItems.size() + " in mod " + context.mod().id());

        for (RecordItem recordItem : recordItems) {
            ResourceLocation recordLocation = BuiltInRegistries.ITEM.getKey(recordItem);

            // TODO Use the resource and parse like we do for block and item textures
            // Probably best to replace with the pack converter/doing all sounds
            //recordItem.getSound().getLocation();

            // TODO Work out how to work these on geysers end
            // They need to be run like /playsound

            String outputLoc = String.format(BEDROCK_RECORD_SOUND_LOCATION, context.mod().id(), recordLocation.getPath());
            FileUtil.copyFileFromMod(context.mod(), String.format(JAVA_RECORD_SOUND_LOCATION, context.mod().id(), recordLocation.getPath()), context.path().resolve(outputLoc));

            context.pack().addSound(recordItem.getSound().getLocation().toString(), outputLoc.replace(".ogg", ""));
        }
    }

    @Override
    public boolean test(@NotNull PackCreateContext<RecordPackModule> context) {
        return context.registryValues(Registries.ITEM).stream().anyMatch(item -> item instanceof RecordItem);
    }
}
