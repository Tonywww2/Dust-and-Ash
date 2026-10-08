package com.tonywww.dustandash.platform;

import net.minecraft.nbt.CompoundTag;
//? if forge {
public class ItemStackHandler extends net.minecraftforge.items.ItemStackHandler implements IItemHandler {
//?} else {
/*public class ItemStackHandler extends net.neoforged.neoforge.items.ItemStackHandler implements IItemHandler {
*///?}

    public ItemStackHandler(int slots) { super(slots); }
//? if forge {

//?} else {
/*public CompoundTag serializeNBT() { return super.serializeNBT(PlatformHooks.registries()); }
    public void deserializeNBT(CompoundTag tag) { super.deserializeNBT(PlatformHooks.registries(), tag); }
*///?}
}
