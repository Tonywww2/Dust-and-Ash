package com.tonywww.dustandash.block.entity;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.Objects;

public final class ItemHandlerContainerView implements Container {
    private final IItemHandler handler;

    public ItemHandlerContainerView(IItemHandler handler) {
        this.handler = Objects.requireNonNull(handler, "handler");
    }

    @Override
    public int getContainerSize() {
        return this.handler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int slot = 0; slot < this.handler.getSlots(); slot++) {
            if (!this.handler.getStackInSlot(slot).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.handler.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        throw new UnsupportedOperationException("Recipe views are read-only");
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        throw new UnsupportedOperationException("Recipe views are read-only");
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        throw new UnsupportedOperationException("Recipe views are read-only");
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        throw new UnsupportedOperationException("Recipe views are read-only");
    }
}