package com.tonywww.dustandash.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.tonywww.dustandash.registry.DAAItems;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

public final class RecipeIo {
    private RecipeIo() {
    }

    public static NonNullList<Ingredient> readIngredients(JsonObject json, ResourceLocation recipeId,
                                                           String field, int capacity, boolean exactSize) {
        JsonArray array = GsonHelper.getAsJsonArray(json, field);
        validateJsonSize(recipeId, field, array.size(), capacity, exactSize);

        NonNullList<Ingredient> ingredients = NonNullList.withSize(capacity, Ingredient.EMPTY);
        for (int index = 0; index < array.size(); index++) {
            Ingredient ingredient = Ingredient.fromJson(array.get(index));
            JsonObject ingredientJson = GsonHelper.convertToJsonObject(array.get(index), field + "[" + index + "]");
            if (ingredientJson.has("item")) {
                Item item = ShapedRecipe.itemFromJson(ingredientJson);
                if (item == DAAItems.EMPTY.get()) {
                    continue;
                }
            }
            if (!ingredient.isEmpty()) {
                ingredients.set(index, ingredient);
            }
        }
        return ingredients;
    }

    public static NonNullList<ItemStack> readOutputs(JsonObject json, ResourceLocation recipeId,
                                                      String field, int capacity, boolean exactSize) {
        JsonArray array = GsonHelper.getAsJsonArray(json, field);
        validateJsonSize(recipeId, field, array.size(), capacity, exactSize);

        NonNullList<ItemStack> outputs = NonNullList.withSize(capacity, ItemStack.EMPTY);
        for (int index = 0; index < array.size(); index++) {
            ItemStack[] matchingStacks = Ingredient.fromJson(array.get(index)).getItems();
            if (matchingStacks.length == 0) {
                throw new JsonSyntaxException("Recipe " + recipeId + " " + field + "[" + index
                        + "] has no matching items");
            }
            if (!matchingStacks[0].is(DAAItems.EMPTY.get())) {
                outputs.set(index, matchingStacks[0]);
            }
        }
        return outputs;
    }

    public static NonNullList<Ingredient> readIngredients(FriendlyByteBuf buffer, ResourceLocation recipeId,
                                                           String field, int expectedSize) {
        int size = buffer.readVarInt();
        validateNetworkSize(recipeId, field, size, expectedSize);

        NonNullList<Ingredient> ingredients = NonNullList.withSize(expectedSize, Ingredient.EMPTY);
        for (int index = 0; index < size; index++) {
            ingredients.set(index, Ingredient.fromNetwork(buffer));
        }
        return ingredients;
    }

    public static NonNullList<ItemStack> readOutputs(FriendlyByteBuf buffer, ResourceLocation recipeId,
                                                      String field, int expectedSize) {
        int size = buffer.readVarInt();
        validateNetworkSize(recipeId, field, size, expectedSize);

        NonNullList<ItemStack> outputs = NonNullList.withSize(expectedSize, ItemStack.EMPTY);
        for (int index = 0; index < size; index++) {
            outputs.set(index, buffer.readItem());
        }
        return outputs;
    }

    public static void writeIngredients(FriendlyByteBuf buffer, NonNullList<Ingredient> ingredients) {
        buffer.writeVarInt(ingredients.size());
        ingredients.forEach(ingredient -> ingredient.toNetwork(buffer));
    }

    public static void writeOutputs(FriendlyByteBuf buffer, NonNullList<ItemStack> outputs) {
        buffer.writeVarInt(outputs.size());
        outputs.forEach(output -> buffer.writeItemStack(output, false));
    }

    private static void validateJsonSize(ResourceLocation recipeId, String field, int actual,
                                         int capacity, boolean exactSize) {
        boolean invalid = exactSize ? actual != capacity : actual > capacity;
        if (invalid) {
            String expected = exactSize ? "exactly " + capacity : "at most " + capacity;
            throw new JsonSyntaxException("Recipe " + recipeId + " requires " + expected + " " + field
                    + ", found " + actual);
        }
    }

    private static void validateNetworkSize(ResourceLocation recipeId, String field, int actual, int expected) {
        if (actual != expected) {
            throw new IllegalArgumentException("Recipe " + recipeId + " network payload requires exactly "
                    + expected + " " + field + ", found " + actual);
        }
    }
}