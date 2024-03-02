package com.tonywww.dustandash.container;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IonizerItemHandler implements IItemHandler {

    private final IItemHandler itemHandler;
    private final Direction side;

    public IonizerItemHandler(IItemHandler itemHandler, @Nullable Direction side) {
        this.itemHandler = itemHandler;
        this.side = side;

    }

    @Override
    public int getSlots() {
        return itemHandler.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

        if (side == null ||
                (side == Direction.UP && slot >= 4) ||
                (side == Direction.NORTH && slot < 4)) {
            if (isItemValid(slot, stack)) {
                return itemHandler.insertItem(slot, stack, simulate);
            }

        }

        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (side == null) {
            return itemHandler.extractItem(slot, amount, simulate);

        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return itemHandler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return itemHandler.isItemValid(slot, stack);
    }
}
