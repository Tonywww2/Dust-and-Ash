package com.tonywww.dustandash.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public final class VoidRing extends Item implements ICurioItem {
    public VoidRing(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return "ring".equals(slotContext.identifier());
    }
}