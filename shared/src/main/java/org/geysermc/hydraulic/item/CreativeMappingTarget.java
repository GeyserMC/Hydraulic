package org.geysermc.hydraulic.item;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;

public record CreativeMappingTarget(List<Class<? extends Item>> itemClasses, List<TagKey<Item>> itemTags, List<Class<? extends Block>> blockClasses, List<TagKey<Block>> blockTags) {
}
