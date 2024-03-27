package org.geysermc.hydraulic.mixin.ext;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import team.unnamed.creative.serialize.minecraft.blockstate.BlockStateSerializer;

import java.util.Map;

@Mixin(value = BlockStateSerializer.class, remap = false)

public class BlockStateSerializerMixin {
    private static Logger LOGGER = LoggerFactory.getLogger("BlockStateSerializerMixin");

    /**
     * This mixin attempts to convert forge_marker blockstates with variants to multipart blockstates
     */
    @Redirect(
        method = "deserializeFromJson(Lcom/google/gson/JsonElement;Lnet/kyori/adventure/key/Key;)Lteam/unnamed/creative/blockstate/BlockState;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/gson/JsonElement;getAsJsonObject()Lcom/google/gson/JsonObject;"
        )
    )
    private JsonObject deserializeFromJson(JsonElement node) {
        JsonObject objectNode = node.getAsJsonObject();

        // Only handle forge_marker blockstates with variants
        if (!objectNode.has("forge_marker") || !objectNode.has("variants")) {
            return objectNode;
        }

        // Check if multipart exists and get it else create a new one
        JsonArray multiPartNode = new JsonArray();
        if (objectNode.has("multipart")) {
            multiPartNode = objectNode.get("multipart").getAsJsonArray();
        }

        // Won't work for all cases, but should work for most
        // TODO better edge case handling
        JsonObject variantsNode = objectNode.getAsJsonObject("variants");
        for (Map.Entry<String, JsonElement> variantEntry : variantsNode.entrySet()) {
            JsonArray variantEntryArr = variantEntry.getValue().getAsJsonArray();

            if (variantEntryArr.size() > 1) {
                LOGGER.warn("More than 1 variant entry found!"); // TODO Add more info to this warning
            }

            // Only handle the first variant entry
            JsonObject variantEntryObj = variantEntryArr.get(0).getAsJsonObject();
            for (Map.Entry<String, JsonElement> variantDataEntry : variantEntryObj.entrySet()) {
                JsonObject multipartApply;
                JsonObject multipartWhen = new JsonObject();

                // When condition
                multipartWhen.addProperty(variantEntry.getKey(), variantDataEntry.getKey());

                JsonObject variantDataEntryObj = variantDataEntry.getValue().getAsJsonObject();
                if (!variantDataEntryObj.has("submodel")) {
                    LOGGER.warn("No submodel found!"); // TODO Add more info to this warning
                    continue;
                }

                // TODO Handle multiple submodels?
                JsonObject submodelObj = variantDataEntryObj.get("submodel").getAsJsonObject();
                multipartApply = submodelObj.get(submodelObj.keySet().toArray(new String[0])[0]).getAsJsonObject();

                JsonObject multipartEntry = new JsonObject();
                multipartEntry.add("apply", multipartApply);
                multipartEntry.add("when", multipartWhen);
                multiPartNode.add(multipartEntry);
            }
        }

        // Remove the now-parsed variants
        objectNode.remove("variants");

        // Update multipart with the new entries
        objectNode.add("multipart", multiPartNode);

        return objectNode;
    }
}
