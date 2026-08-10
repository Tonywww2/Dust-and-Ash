package com.tonywww.dustandash.block.entity.FissionReactor;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public final class ReactorConsumableService {
    private ReactorConsumableService() {
    }

    public static void damageAndRecycle(ItemStack consumable, int damage, RandomSource random,
                                        ItemStackHandler inventory, int outputSlot, Item emptyContainer) {
        if (consumable.isEmpty() || !consumable.hurt(damage, random, null)) {
            return;
        }

        consumable.shrink(1);
        ItemStack output = inventory.getStackInSlot(outputSlot);
        if (output.is(emptyContainer)) {
            output.grow(1);
        } else {
            inventory.setStackInSlot(outputSlot, new ItemStack(emptyContainer));
        }
    }
}