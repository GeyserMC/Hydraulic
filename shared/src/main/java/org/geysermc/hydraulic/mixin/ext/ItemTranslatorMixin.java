package org.geysermc.hydraulic.mixin.ext;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.ListTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.geysermc.geyser.item.type.Item;
import org.geysermc.geyser.registry.type.ItemMapping;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.inventory.item.ItemTranslator;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = ItemTranslator.class, remap = false)
public class ItemTranslatorMixin {

    @Inject(
            method = "translateDisplayProperties(Lorg/geysermc/geyser/session/GeyserSession;Lcom/github/steveice10/opennbt/tag/builtin/CompoundTag;Lorg/geysermc/geyser/registry/type/ItemMapping;C)Lcom/github/steveice10/opennbt/tag/builtin/CompoundTag;",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void translateDisplayProperties(GeyserSession session, CompoundTag tag, ItemMapping mapping, char translationColor, CallbackInfoReturnable<CompoundTag> ci) {
        CompoundTag newNbt = tag;
        if (newNbt == null) {
            newNbt = new CompoundTag("nbt");
            CompoundTag display = new CompoundTag("display");
            display.put(new ListTag("Lore"));
            newNbt.put(display);
        }

        CompoundTag compoundTag = newNbt.get("display");
        if (compoundTag == null) {
            compoundTag = new CompoundTag("display");
        }
        ListTag listTag = compoundTag.get("Lore");

        if (listTag == null) {
            listTag = new ListTag("Lore");
        }

        String identifier = mapping.getJavaItem().javaIdentifier();

        // Get the mod name from the identifier
        String modId = identifier.substring(0, identifier.indexOf(":"));
        ModInfo mod = HydraulicImpl.instance().mod(modId);
        String modName = "Minecraft";
        if (mod != null) {
            modName = mod.name();
        }

        listTag.add(new StringTag("", "§r§9§o" + modName));
        compoundTag.put(listTag);
        newNbt.put(compoundTag);

        ci.setReturnValue(newNbt);
    }
}
