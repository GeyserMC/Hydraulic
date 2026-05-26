package org.geysermc.hydraulic.mixin.ext;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.unnamed.creative.metadata.pack.PackMeta;

@Mixin(targets = "team.unnamed.creative.serialize.minecraft.metadata.PackMetaCodec", remap = false)
public class PackMetaCodecMixin {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Inject(
        method = "read(Lcom/google/gson/JsonObject;)Lteam/unnamed/creative/metadata/pack/PackMeta;",
        at = @At("HEAD")
    )
    private void hydraulic$sanitizePackFormatRange(JsonObject object, CallbackInfoReturnable<PackMeta> cir) {
        sanitizeFormatPair(object, "min_format", "max_format");

        JsonElement supportedFormats = object.get("supported_formats");
        if (supportedFormats != null && supportedFormats.isJsonObject()) {
            sanitizeFormatPair(supportedFormats.getAsJsonObject(), "min_inclusive", "max_inclusive");
        }
    }

    private static void sanitizeFormatPair(JsonObject object, String minKey, String maxKey) {
        if (!object.has(minKey) || !object.has(maxKey)) {
            return;
        }

        int[] min = readFormat(object.get(minKey));
        int[] max = readFormat(object.get(maxKey));
        if (min == null || max == null || compare(min, max) <= 0) {
            return;
        }

        LOGGER.warn(
                "[Hydraulic] Correcting invalid pack metadata range: {}={} is greater than {}={}; using {} for both bounds",
                minKey,
                formatToString(min),
                maxKey,
                formatToString(max),
                formatToString(max)
        );
        object.add(minKey, writeFormat(max));
        object.add(maxKey, writeFormat(max));
    }

    private static int[] readFormat(JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return null;
        }

        if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            if (array.isEmpty()) {
                return null;
            }
            return new int[] { array.get(0).getAsInt(), array.size() > 1 ? array.get(1).getAsInt() : 0 };
        }

        if (element.isJsonPrimitive()) {
            String value = element.getAsString();
            int separator = value.indexOf('.');
            if (separator == -1) {
                return new int[] { element.getAsInt(), 0 };
            }

            return new int[] {
                    Integer.parseInt(value.substring(0, separator)),
                    Integer.parseInt(value.substring(separator + 1))
            };
        }

        return null;
    }

    private static JsonElement writeFormat(int[] format) {
        if (format[1] == 0) {
            return new com.google.gson.JsonPrimitive(format[0]);
        }

        JsonArray array = new JsonArray(2);
        array.add(format[0]);
        array.add(format[1]);
        return array;
    }

    private static int compare(int[] first, int[] second) {
        int major = Integer.compare(first[0], second[0]);
        return major != 0 ? major : Integer.compare(first[1], second[1]);
    }

    private static String formatToString(int[] format) {
        return format[1] == 0 ? Integer.toString(format[0]) : format[0] + "." + format[1];
    }
}
