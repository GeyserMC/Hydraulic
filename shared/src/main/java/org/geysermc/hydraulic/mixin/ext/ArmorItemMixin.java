package org.geysermc.hydraulic.mixin.ext;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import org.geysermc.hydraulic.ext.ArmorItemExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorItem.class)
public class ArmorItemMixin implements ArmorItemExt {
    @Unique
    private ArmorMaterial armorMaterial;

    @Unique
    private ArmorType armorType;

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void onInitFinish(ArmorMaterial armorMaterial, ArmorType armorType, Item.Properties properties, CallbackInfo ci) {
        this.armorMaterial = armorMaterial;
        this.armorType = armorType;
    }

    @Override
    public ArmorMaterial material() {
        return armorMaterial;
    }

    @Override
    public ArmorType type() {
        return armorType;
    }

    @Override
    public int protection() {
        return armorMaterial.defense().get(armorType);
    }
}
