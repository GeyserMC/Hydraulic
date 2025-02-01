package org.geysermc.hydraulic.ext;

import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

public interface ArmorItemExt {
    ArmorMaterial material();

    ArmorType type();

    int protection();
}
