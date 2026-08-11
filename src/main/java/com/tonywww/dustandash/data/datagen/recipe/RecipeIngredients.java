package com.tonywww.dustandash.data.datagen.recipe;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Reproduces the hand-written "forge:nbt" ingredient entries used by a handful of source recipes.
 * Built by hand-assembling the same json Forge's NBT ingredient serializer reads, then parsing it
 * through {@link Ingredient#fromJson(com.google.gson.JsonElement)} so the exact ingredient class
 * (which varies across Forge versions) never needs to be referenced directly.
 */
public final class RecipeIngredients {
    private RecipeIngredients() {
    }

    public static Ingredient nbt(Item item, String nbt) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);
        if (itemId == null) {
            throw new IllegalArgumentException("Unregistered item passed to RecipeIngredients.nbt");
        }
        JsonObject json = new JsonObject();
        json.addProperty("type", "forge:nbt");
        json.addProperty("item", itemId.toString());
        json.addProperty("nbt", nbt);
        return Ingredient.fromJson(json);
    }

    public static Ingredient nbt(ResourceLocation itemId, String nbt) {
        Item item = ForgeRegistries.ITEMS.getValue(itemId);
        if (item == null) {
            throw new IllegalArgumentException("Unknown item " + itemId);
        }
        return nbt(item, nbt);
    }
}
