package org.geysermc.hydraulic.mixin.ext;

import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import net.kyori.adventure.key.Key;
import net.minecraft.SharedConstants;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import team.unnamed.creative.metadata.pack.FormatVersion;
import team.unnamed.creative.metadata.pack.PackFormat;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;
import team.unnamed.creative.serialize.minecraft.GsonUtil;
import team.unnamed.creative.serialize.minecraft.ResourceCategories;
import team.unnamed.creative.serialize.minecraft.ResourceCategory;
import team.unnamed.creative.serialize.minecraft.io.JsonResourceDeserializer;

import java.io.IOException;
import java.util.Map;

@Mixin(targets = "team.unnamed.creative.serialize.minecraft.MinecraftResourcePackReaderImpl", remap = false)
public abstract class MinecraftResourcePackReaderImplMixin {
    private static Logger LOGGER = LoggerFactory.getLogger("MinecraftResourcePackReaderImplMixin");

    private static PackFormat currentFormat;

    private static PackFormat currentFormat() {
        if (currentFormat == null) {
            net.minecraft.server.packs.metadata.pack.PackFormat format = SharedConstants.getCurrentVersion().packVersion(PackType.CLIENT_RESOURCES);
            currentFormat = PackFormat.format(FormatVersion.of(format.major(), format.minor()));
        }
        return currentFormat;
    }

    /**
     * Read every pack as the current version, like the game does for mod resources
     * Mods often ship an outdated (or no) pack.mcmeta, which would make creative look in old folders (e.g. models/equipment)
     */
    @Redirect(
        method = "read(Lteam/unnamed/creative/serialize/minecraft/fs/FileTreeReader;)Lteam/unnamed/creative/ResourcePack;",
        at = @At(
            value = "INVOKE",
            target = "Lteam/unnamed/creative/serialize/minecraft/ResourceCategories;buildCategoryMapByFolder(Lteam/unnamed/creative/metadata/pack/PackFormat;)Ljava/util/Map;"
        )
    )
    private Map<String, ResourceCategory<?>> buildCategoryMapByFolder(PackFormat packFormat) {
        return ResourceCategories.buildCategoryMapByFolder(currentFormat());
    }

    /**
     * Redirect the parseJson method to catch any exceptions that may occur
     * This means a single bad json file won't cause the entire resource pack to fail loading
     */
    @Redirect(
        method = "parseJson",
        at = @At(
            value = "INVOKE",
            target = "Lteam/unnamed/creative/serialize/minecraft/GsonUtil;parseReader(Lcom/google/gson/stream/JsonReader;)Lcom/google/gson/JsonElement;"
        )
    )
    private JsonElement parseJson(JsonReader reader) {
        try {
            return GsonUtil.parseReader(reader);
        } catch (Exception e) {
            LOGGER.error("Failed to parse JSON: " + e.getMessage());
        }

        return null;
    }

    /**
     * Redirect the deserializeFromJson to ignore any null JsonElements
     * Also catch any exceptions that may occur and log them
     */
    @Redirect(
        method = "read(Lteam/unnamed/creative/serialize/minecraft/fs/FileTreeReader;)Lteam/unnamed/creative/ResourcePack;",
        at = @At(
            value = "INVOKE",
            target = "Lteam/unnamed/creative/serialize/minecraft/io/JsonResourceDeserializer;deserializeFromJson(Lcom/google/gson/JsonElement;Lnet/kyori/adventure/key/Key;Lteam/unnamed/creative/metadata/pack/PackFormat;)Ljava/lang/Object;"
        )
    )
    private Object deserializeFromJson(JsonResourceDeserializer instance, JsonElement jsonElement, Key key, PackFormat packFormat) throws IOException {
        if (jsonElement == null) {
            return null;
        }

        try {
            return instance.deserializeFromJson(jsonElement, key, currentFormat());
        } catch (Exception e) {
            LOGGER.error("Failed to deserialize JSON (" + key + "): " + e.getMessage());
        }

        return null;
    }

    @Redirect(
            method = "read(Lteam/unnamed/creative/serialize/minecraft/fs/FileTreeReader;)Lteam/unnamed/creative/ResourcePack;",
            at = @At(
                    value = "INVOKE",
                    target = "Lteam/unnamed/creative/part/ResourcePackPart;addTo(Lteam/unnamed/creative/overlay/ResourceContainer;)V"
            )
    )
    private void addTo(ResourcePackPart instance, ResourceContainer resourceContainer) {
        if (instance != null) {
            instance.addTo(resourceContainer);
        }
    }

    //Key key = Key.key(namespace, keyValue);
    @ModifyArgs(
            method = "read(Lteam/unnamed/creative/serialize/minecraft/fs/FileTreeReader;)Lteam/unnamed/creative/ResourcePack;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/kyori/adventure/key/Key;key(Ljava/lang/String;Ljava/lang/String;)Lnet/kyori/adventure/key/Key;"
            )
    )
    private void injectKeyCreation(Args args) {
        args.set(1, ((String) args.get(1)).toLowerCase());
    }
}
