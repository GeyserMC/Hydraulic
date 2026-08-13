package org.geysermc.hydraulic.mixin.ext;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.geysermc.geyser.registry.type.ItemMapping;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.item.ItemTranslator;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponentTypes;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds a lore line with the owning mod's name to non-vanilla items so Bedrock players can tell
 * where an item comes from. Replaces the pre-1.20.5 translateDisplayProperties approach.
 */
@Mixin(value = ItemTranslator.class, remap = false)
public class ItemTranslatorMixin {

    @WrapOperation(
        method = "translateToBedrock(Lorg/geysermc/geyser/session/GeyserSession;Lorg/geysermc/mcprotocollib/protocol/data/game/item/ItemStack;)Lorg/geysermc/mcprotocollib/protocol/data/game/item/ItemData;",
        at = @At(
            value = "INVOKE",
            target = "translateToBedrock(Lorg/geysermc/geyser/session/GeyserSession;Lnet/minecraft/world/item/Item;Lorg/geysermc/geyser/registry/type/ItemMapping;ILorg/geysermc/mcprotocollib/protocol/data/game/item/component/DataComponents;)Lorg/geysermc/mcprotocollib/protocol/data/game/item/ItemData$Builder;"
        )
    )
    private static ItemData.Builder wrapTranslateToBedrock(Operation<ItemData.Builder> original, GeyserSession session, Item javaItem, ItemMapping bedrockItem, int count, DataComponents components) {
        if (components != null) {
            try {
                Identifier identifier = BuiltInRegistries.ITEM.getKey(javaItem);
                if (!identifier.getNamespace().equals("minecraft")) {
                    List<ModInfo> mods = HydraulicImpl.instance().getPackManager().getNamespacesToMods().get(identifier.getNamespace());
                    String modName = mods.isEmpty() ? identifier.getNamespace() : mods.get(0).name();

                    List<Component> lore = new ArrayList<>(components.getOrDefault(DataComponentTypes.LORE, List.of()));
                    lore.add(Component.text(modName)
                            .color(NamedTextColor.BLUE)
                            .decoration(TextDecoration.ITALIC, TextDecoration.State.TRUE));
                    components.put(DataComponentTypes.LORE, lore);
                }
            } catch (Exception ignored) {
                // Never break item translation because of the lore addition
            }
        }

        return original.call(session, javaItem, bedrockItem, count, components);
    }
}
