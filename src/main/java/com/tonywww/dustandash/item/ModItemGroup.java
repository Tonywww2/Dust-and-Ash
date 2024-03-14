package com.tonywww.dustandash.item;

import com.tonywww.dustandash.block.ModBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroup {

    public static final CreativeModeTab DUST_TAB = new CreativeModeTab("dust_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.HAND_VACUUM.get());
        }
    };

    public static final CreativeModeTab ASH_TAB = new CreativeModeTab("ash_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.ASH.get());
        }
    };

    public static final CreativeModeTab BLOCKS_TAB = new CreativeModeTab("blocks_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.INTEGRATED_BLOCK.get());
        }
    };

}
