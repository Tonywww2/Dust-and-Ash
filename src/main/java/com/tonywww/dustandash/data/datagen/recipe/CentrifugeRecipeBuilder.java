package com.tonywww.dustandash.data.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.registry.DAAItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Datagen builder for {@code dustandash:centrifuge} recipes (exactly 2 inputs, exactly 8 outputs).
 */
public final class CentrifugeRecipeBuilder {
    private final int tick;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final List<Ingredient> outputs = new ArrayList<>();

    private CentrifugeRecipeBuilder(int tick) {
        this.tick = tick;
    }

    public static CentrifugeRecipeBuilder centrifuge(int tick) {
        return new CentrifugeRecipeBuilder(tick);
    }

    public CentrifugeRecipeBuilder input(ItemLike item) {
        ingredients.add(Ingredient.of(item));
        return this;
    }

    public CentrifugeRecipeBuilder input(TagKey<Item> tag) {
        ingredients.add(Ingredient.of(tag));
        return this;
    }

    public CentrifugeRecipeBuilder input(Ingredient ingredient) {
        ingredients.add(ingredient);
        return this;
    }

    public CentrifugeRecipeBuilder output(ItemLike item) {
        outputs.add(Ingredient.of(item));
        return this;
    }

    public CentrifugeRecipeBuilder output(TagKey<Item> tag) {
        outputs.add(Ingredient.of(tag));
        return this;
    }

    public CentrifugeRecipeBuilder empty() {
        outputs.add(Ingredient.of(DAAItems.EMPTY.get()));
        return this;
    }

    public void save(Consumer<FinishedRecipe> writer, String id) {
        if (ingredients.size() != 2) {
            throw new IllegalStateException("Centrifuge recipe " + id + " requires exactly 2 ingredients, found " + ingredients.size());
        }
        if (outputs.size() != 8) {
            throw new IllegalStateException("Centrifuge recipe " + id + " requires exactly 8 outputs, found " + outputs.size());
        }
        writer.accept(new Result(new ResourceLocation(DustAndAsh.MOD_ID, id), tick, List.copyOf(ingredients), List.copyOf(outputs)));
    }

    private record Result(ResourceLocation id, int tick, List<Ingredient> ingredients,
                           List<Ingredient> outputs) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("tick", tick);

            JsonArray ingredientsArray = new JsonArray();
            ingredients.forEach(ingredient -> ingredientsArray.add(ingredient.toJson()));
            json.add("ingredients", ingredientsArray);

            JsonArray outputsArray = new JsonArray();
            outputs.forEach(output -> outputsArray.add(output.toJson()));
            json.add("outputs", outputsArray);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public net.minecraft.world.item.crafting.RecipeSerializer<?> getType() {
            return com.tonywww.dustandash.registry.DAARecipe.CENTRIFUGE_SERIALIZER.get();
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
