package com.tonywww.dustandash.platform;

//? if forge {
public interface IItemHandler extends net.minecraftforge.items.IItemHandler {
//?} else {
/*public interface IItemHandler extends net.neoforged.neoforge.items.IItemHandler {
*///?}

    static IItemHandler wrap(
//? if forge {
net.minecraftforge.items.IItemHandler nativeHandler
//?} else {
/*net.neoforged.neoforge.items.IItemHandler nativeHandler
*///?}
) {
        if (nativeHandler instanceof IItemHandler handler) return handler;
        return new IItemHandler() {
            public int getSlots() { return nativeHandler.getSlots(); }
            public net.minecraft.world.item.ItemStack getStackInSlot(int slot) { return nativeHandler.getStackInSlot(slot); }
            public net.minecraft.world.item.ItemStack insertItem(int slot, net.minecraft.world.item.ItemStack stack, boolean simulate) { return nativeHandler.insertItem(slot, stack, simulate); }
            public net.minecraft.world.item.ItemStack extractItem(int slot, int count, boolean simulate) { return nativeHandler.extractItem(slot, count, simulate); }
            public int getSlotLimit(int slot) { return nativeHandler.getSlotLimit(slot); }
            public boolean isItemValid(int slot, net.minecraft.world.item.ItemStack stack) { return nativeHandler.isItemValid(slot, stack); }
        };
    }
}
