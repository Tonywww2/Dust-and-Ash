package com.tonywww.dustandash.block.entity;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public interface DroppableInventory {
    IItemHandler getInventory();

    default NonNullList<ItemStack> getDroppableInventory() {
        IItemHandler inventory = getInventory();
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            drops.add(inventory.getStackInSlot(slot));
        }
        return drops;
    }
}