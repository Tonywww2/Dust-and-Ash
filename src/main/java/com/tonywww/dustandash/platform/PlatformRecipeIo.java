package com.tonywww.dustandash.platform;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
public final class PlatformRecipeIo {
    private PlatformRecipeIo() {}
    private static final ThreadLocal<com.mojang.serialization.DynamicOps<JsonElement>> OPS = new ThreadLocal<>();
    public static <T> T withOps(com.mojang.serialization.DynamicOps<JsonElement> ops, java.util.function.Supplier<T> action) {
        var previous = OPS.get();
        OPS.set(ops);
        try { return action.get(); }
        finally { if (previous == null) OPS.remove(); else OPS.set(previous); }
    }
    public static Ingredient ingredient(JsonElement json) {
//? if forge {
return Ingredient.fromJson(json);
//?} else {
/*var ops = OPS.get();
return Ingredient.CODEC.parse(ops == null ? net.minecraft.resources.RegistryOps.create(com.mojang.serialization.JsonOps.INSTANCE, PlatformHooks.registries()) : ops, json).getOrThrow(com.google.gson.JsonSyntaxException::new);
*///?}
    }
    public static Item item(JsonObject json) {
        String id=net.minecraft.util.GsonHelper.getAsString(json,"item");
        Item item=PlatformRegistries.ITEMS.getValue(PlatformIdentifier.parse(id));
        if (item == null) throw new com.google.gson.JsonSyntaxException("Unknown item "+id);
        return item;
    }
    public static ItemStack stack(JsonObject json) {
        ItemStack result=new ItemStack(item(json),net.minecraft.util.GsonHelper.getAsInt(json,"count",1));
        if (result.getCount() < 1 || result.getCount() > result.getMaxStackSize()) throw new com.google.gson.JsonSyntaxException("Invalid item count");
        return result;
    }
    public static Ingredient ingredient(FriendlyByteBuf buffer) {
//? if forge {
return Ingredient.fromNetwork(buffer);
//?} else {
/*return Ingredient.CONTENTS_STREAM_CODEC.decode((net.minecraft.network.RegistryFriendlyByteBuf)buffer);
*///?}
    }
    public static void ingredient(FriendlyByteBuf buffer, Ingredient ingredient) {
//? if forge {
ingredient.toNetwork(buffer);
//?} else {
/*Ingredient.CONTENTS_STREAM_CODEC.encode((net.minecraft.network.RegistryFriendlyByteBuf)buffer,ingredient);
*///?}
    }
    public static ItemStack item(FriendlyByteBuf buffer) {
//? if forge {
return buffer.readItem();
//?} else {
/*return ItemStack.OPTIONAL_STREAM_CODEC.decode((net.minecraft.network.RegistryFriendlyByteBuf)buffer);
*///?}
    }
    public static void item(FriendlyByteBuf buffer, ItemStack stack) {
//? if forge {
buffer.writeItemStack(stack,false);
//?} else {
/*ItemStack.OPTIONAL_STREAM_CODEC.encode((net.minecraft.network.RegistryFriendlyByteBuf)buffer,stack);
*///?}
    }
}
