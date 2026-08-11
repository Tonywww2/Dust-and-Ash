package com.tonywww.dustandash.data.datagen.recipe;

import com.google.gson.JsonObject;
import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

/** Minimal cooking recipe result with an explicit output count and no generated advancement. */
public final class SimpleFixedResult {
    private SimpleFixedResult() {
    }

    public static FinishedRecipe smelting(String id, Ingredient ingredient, Item result, int count,
                                           float experience, int cookingTime) {
        return new Result(id, RecipeSerializer.SMELTING_RECIPE, ingredient, result, count, experience, cookingTime);
    }

    public static FinishedRecipe blasting(String id, Ingredient ingredient, Item result, int count,
                                           float experience, int cookingTime) {
        return new Result(id, RecipeSerializer.BLASTING_RECIPE, ingredient, result, count, experience, cookingTime);
    }

    private record Result(String id, RecipeSerializer<?> serializer, Ingredient ingredient, Item result, int count,
                           float experience, int cookingTime) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("ingredient", ingredient.toJson());
            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", ForgeRegistries.ITEMS.getKey(result).toString());
            resultJson.addProperty("count", count);
            json.add("result", resultJson);
            json.addProperty("experience", experience);
            json.addProperty("cookingtime", cookingTime);
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation(DustAndAsh.MOD_ID, id.substring(id.indexOf(':') + 1));
        }

        @Override
        public RecipeSerializer<?> getType() {
            return serializer;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
