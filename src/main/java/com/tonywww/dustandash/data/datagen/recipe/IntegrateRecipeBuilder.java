package com.tonywww.dustandash.data.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Datagen builder for {@code dustandash:integrate} recipes (up to 8 inputs, single output).
 */
public final class IntegrateRecipeBuilder {
    private final ItemStack output;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private int level;

    private IntegrateRecipeBuilder(ItemStack output) {
        this.output = output;
    }

    public static IntegrateRecipeBuilder integrate(ItemLike result) {
        return new IntegrateRecipeBuilder(new ItemStack(result));
    }

    public static IntegrateRecipeBuilder integrate(ItemLike result, int count) {
        return new IntegrateRecipeBuilder(new ItemStack(result, count));
    }

    public IntegrateRecipeBuilder level(int level) {
        this.level = level;
        return this;
    }

    public IntegrateRecipeBuilder ingredient(ItemLike item) {
        ingredients.add(Ingredient.of(item));
        return this;
    }

    public IntegrateRecipeBuilder ingredient(TagKey<Item> tag) {
        ingredients.add(Ingredient.of(tag));
        return this;
    }

    public IntegrateRecipeBuilder ingredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        return this;
    }

    public void save(Consumer<FinishedRecipe> writer, String id) {
        if (ingredients.isEmpty() || ingredients.size() > 8) {
            throw new IllegalStateException("Integrate recipe " + id + " requires between 1 and 8 ingredients, found " + ingredients.size());
        }
        writer.accept(new Result(new ResourceLocation(DustAndAsh.MOD_ID, id), level, List.copyOf(ingredients), output));
    }

    private record Result(ResourceLocation id, int level, List<Ingredient> ingredients,
                           ItemStack output) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("level", level);

            JsonArray ingredientsArray = new JsonArray();
            ingredients.forEach(ingredient -> ingredientsArray.add(ingredient.toJson()));
            json.add("ingredients", ingredientsArray);

            JsonObject outputJson = new JsonObject();
            outputJson.addProperty("item", ForgeRegistries.ITEMS.getKey(output.getItem()).toString());
            outputJson.addProperty("count", output.getCount());
            json.add("output", outputJson);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public net.minecraft.world.item.crafting.RecipeSerializer<?> getType() {
            return com.tonywww.dustandash.registry.DAARecipe.INTEGRATE_SERIALIZER.get();
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
