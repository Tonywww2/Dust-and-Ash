package com.tonywww.dustandash.item;

import com.tonywww.dustandash.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {

    public static final ItemGroup DUST_TAB = new ItemGroup("dust_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.HAND_VACUUM.get());
        }
    };

    public static final ItemGroup ASH_TAB = new ItemGroup("ash_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.ASH.get());
        }
    };

    public static final ItemGroup BLOCKS_TAB = new ItemGroup("blocks_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.INTEGRATED_BLOCK.get());
        }
    };

}
