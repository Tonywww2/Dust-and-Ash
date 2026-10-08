package com.tonywww.dustandash.platform;

import net.minecraft.world.item.Tier;
public interface PlatformTier extends Tier {
    int getLevel();
//? if forge {

//?} else {
/*@Override default net.minecraft.tags.TagKey<net.minecraft.world.level.block.Block> getIncorrectBlocksForDrops() {
    return getLevel() == 0 ? net.minecraft.tags.BlockTags.INCORRECT_FOR_WOODEN_TOOL : net.minecraft.tags.BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
}
*///?}
}
