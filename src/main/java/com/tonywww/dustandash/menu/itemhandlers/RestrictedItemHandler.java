package com.tonywww.dustandash.menu.itemhandlers;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.IntPredicate;

public final class RestrictedItemHandler implements IItemHandler {
    private final IItemHandler delegate;
    private final IntPredicate canInsert;
    private final IntPredicate canExtract;

    public RestrictedItemHandler(IItemHandler delegate, IntPredicate canInsert, IntPredicate canExtract) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
        this.canInsert = Objects.requireNonNull(canInsert, "canInsert");
        this.canExtract = Objects.requireNonNull(canExtract, "canExtract");
    }

    @Override
    public int getSlots() {
        return this.delegate.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.delegate.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (!this.canInsert.test(slot) || !this.delegate.isItemValid(slot, stack)) {
            return stack;
        }
        return this.delegate.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (!this.canExtract.test(slot)) {
            return ItemStack.EMPTY;
        }
        return this.delegate.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.delegate.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return this.canInsert.test(slot) && this.delegate.isItemValid(slot, stack);
    }
}