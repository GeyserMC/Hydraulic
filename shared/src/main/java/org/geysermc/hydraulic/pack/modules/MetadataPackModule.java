package org.geysermc.hydraulic.pack.modules;

import com.google.auto.service.AutoService;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@AutoService(PackModule.class)
public class MetadataPackModule extends PackModule<MetadataPackModule> {
    public MetadataPackModule() {
        this.postProcess(context -> {
            // Set the pack name and description
            context.pack().manifest().header().name(context.mod().name().trim() + " Resource Pack");
            context.pack().manifest().header().description("Resource pack for mod " + context.mod().name().trim());

            // Generate the pack uuid from the mod file
            try {
                context.pack().manifest().header().uuid(UUID.nameUUIDFromBytes(Files.readAllBytes(context.mod().modFile())).toString());
            } catch (IOException ignored) { }

            // Copy the icon if it exists
            // TODO Add a default icon, without there is just a purple and black square
            if (!context.mod().iconPath().isEmpty()) {
                try {
                    context.pack().icon(Files.readAllBytes(context.mod().modPath().resolve(context.mod().iconPath())));
                } catch (IOException ignored) { }
            }
        });
    }

    @Override
    public boolean test(@NotNull PackPostProcessContext<MetadataPackModule> context) {
        return true;
    }
}
