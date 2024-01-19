package com.tonywww.dustandash.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class IItemStackHandler extends ItemStackHandler {

    public IItemStackHandler(int size) {
        super(size);
    }

    public IItemStackHandler(NonNullList<ItemStack> list){
        super(list.size());
        for (int i = 0; i < list.size(); i++) {
            super.stacks.set(i, list.get(i));

        }

    }

    public IItemStackHandler(NonNullList<ItemStack> list, int... index){
        super(index.length);
        int j = 0;
        for (int i : index) {
            super.stacks.set(j++, list.get(i));

        }

    }

    public IItemStackHandler(NonNullList<ItemStack> list1, NonNullList<ItemStack> list2, int... index){
        this(list1);
        int j = 0;
        for (int i : index) {
            super.stacks.set(i, list2.get(j));

        }

    }

    public NonNullList<ItemStack> getStacks(){
        return super.stacks;
    }

}
