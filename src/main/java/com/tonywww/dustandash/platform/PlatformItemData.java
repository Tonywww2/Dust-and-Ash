package com.tonywww.dustandash.platform;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
public final class PlatformItemData {
    private PlatformItemData() {}
    public static CompoundTag read(ItemStack stack) {
//? if forge {
return stack.getTag();
//?} else {
/*var data=stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA); return data == null ? null : data.copyTag();
*///?}
    }
    public static CompoundTag getOrCreate(ItemStack stack) { var tag=read(stack); return tag == null ? new CompoundTag() : tag; }
    public static void write(ItemStack stack, CompoundTag tag) {
//? if forge {
stack.setTag(tag);
//?} else {
/*stack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(tag));
*///?}
    }
    public static void makeUnbreakable(ItemStack stack) {
//? if forge {
var tag=getOrCreate(stack); tag.putBoolean("Unbreakable",true); write(stack,tag);
//?} else {
/*stack.set(net.minecraft.core.component.DataComponents.UNBREAKABLE,new net.minecraft.world.item.component.Unbreakable(true));
*///?}
    }
}
