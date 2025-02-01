package org.geysermc.hydraulic.mixin.ext;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.util.InventoryUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = InventoryUtils.class, remap = false)
public class InventoryUtilsMixin {
//
//    /**
//     * Find or create the item with the 2d item identifier
//     * Only for creative inventory items
//     */
//    @Inject(
//        method = "findOrCreateItem(Lorg/geysermc/geyser/session/GeyserSession;Ljava/lang/String;)V",
//        at = @At(
//            value = "INVOKE",
//            target = "Lorg/geysermc/geyser/GeyserLogger;debug(Ljava/lang/String;)V"
//        ),
//        cancellable = true
//    )
//    private static void findOrCreateItemCreative(GeyserSession session, String itemName, CallbackInfo ci, @Share("creative") LocalBooleanRef creative) {
//        // If we are already looking for the `item.`, don't do anything
//        if (itemName.endsWith("_item")) {
//            return;
//        }
//
//        // Flag this call as creative
//        creative.set(true);
//
//        InventoryUtils.findOrCreateItem(session, itemName + "_item");
//
//        ci.cancel();
//    }
//
//    /**
//     * Find or create the item with the 2d item identifier
//     * Only for non-creative inventory items
//     */
//    @Inject(
//        method = "findOrCreateItem(Lorg/geysermc/geyser/session/GeyserSession;Ljava/lang/String;)V",
//        at = @At("TAIL")
//    )
//    private static void findOrCreateItemSurvival(GeyserSession session, String itemName, CallbackInfo ci, @Share("creative") LocalBooleanRef creative) {
//        // Creative is handled in the previous mixin
//        if (creative.get()) {
//            return;
//        }
//
//        // If we are already looking for the `_item`, don't do anything
//        if (itemName.endsWith("_item")) {
//            return;
//        }
//
//        InventoryUtils.findOrCreateItem(session, itemName + "_item");
//    }
}
