package com.tonywww.dustandash.platform;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
public final class PlatformBlocks {
    private PlatformBlocks() {}
    public static BlockBehaviour.Properties copy(Block block) {
//? if forge {
return BlockBehaviour.Properties.copy(block);
//?} else {
/*return BlockBehaviour.Properties.ofFullCopy(block);
*///?}
    }
}
