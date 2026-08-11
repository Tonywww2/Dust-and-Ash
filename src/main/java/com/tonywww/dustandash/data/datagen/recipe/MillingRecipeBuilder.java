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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Datagen builder for {@code dustandash:milling} recipes. Two modes mirror the hand-written json:
 * {@link #step1(ItemLike)} for the single-ingredient "step1" recipes, and {@link #pattern} +
 * {@link #define} + {@link #catalyst} for the 5x5 grid recipes.
 */
public final class MillingRecipeBuilder {
    private final ItemStack output;
    private final Map<Character, Ingredient> key = new LinkedHashMap<>();
    private final List<String> pattern = new java.util.ArrayList<>();
    private Ingredient catalyst;
    private boolean step1;
    private Ingredient step1Ingredient;

    private MillingRecipeBuilder(ItemStack output) {
        this.output = output;
    }

    public static MillingRecipeBuilder milling(ItemLike result) {
        return new MillingRecipeBuilder(new ItemStack(result));
    }

    public static MillingRecipeBuilder milling(ItemLike result, int count) {
        return new MillingRecipeBuilder(new ItemStack(result, count));
    }

    public MillingRecipeBuilder step1(ItemLike input) {
        this.step1 = true;
        this.step1Ingredient = Ingredient.of(input);
        return this;
    }

    public MillingRecipeBuilder step1(TagKey<Item> tag) {
        this.step1 = true;
        this.step1Ingredient = Ingredient.of(tag);
        return this;
    }

    public MillingRecipeBuilder pattern(String row) {
        pattern.add(row);
        return this;
    }

    public MillingRecipeBuilder define(char symbol, ItemLike item) {
        key.put(symbol, Ingredient.of(item));
        return this;
    }

    public MillingRecipeBuilder define(char symbol, TagKey<Item> tag) {
        key.put(symbol, Ingredient.of(tag));
        return this;
    }

    public MillingRecipeBuilder catalyst(ItemLike item) {
        this.catalyst = Ingredient.of(item);
        return this;
    }

    public MillingRecipeBuilder catalyst(TagKey<Item> tag) {
        this.catalyst = Ingredient.of(tag);
        return this;
    }

    public void save(Consumer<FinishedRecipe> writer, String id) {
        if (step1) {
            if (step1Ingredient == null) {
                throw new IllegalStateException("Milling recipe " + id + " is missing its step1 ingredient");
            }
        } else {
            if (pattern.size() != 5) {
                throw new IllegalStateException("Milling recipe " + id + " requires exactly 5 pattern rows, found " + pattern.size());
            }
            for (String row : pattern) {
                if (row.length() != 5) {
                    throw new IllegalStateException("Milling recipe " + id + " pattern row \"" + row + "\" must contain exactly 5 symbols");
                }
                for (char symbol : row.toCharArray()) {
                    if (symbol != ' ' && !key.containsKey(symbol)) {
                        throw new IllegalStateException("Milling recipe " + id + " pattern references undefined symbol '" + symbol + "'");
                    }
                }
            }
            if (catalyst == null) {
                throw new IllegalStateException("Milling recipe " + id + " is missing its catalyst");
            }
        }
        writer.accept(new Result(new ResourceLocation(DustAndAsh.MOD_ID, id), output, step1, step1Ingredient,
                List.copyOf(pattern), Map.copyOf(key), catalyst));
    }

    private record Result(ResourceLocation id, ItemStack output, boolean step1, Ingredient step1Ingredient,
                           List<String> pattern, Map<Character, Ingredient> key,
                           Ingredient catalyst) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("step1", step1);

            if (step1) {
                JsonArray ingredientsArray = new JsonArray();
                ingredientsArray.add(step1Ingredient.toJson());
                json.add("ingredients", ingredientsArray);
            } else {
                JsonArray patternArray = new JsonArray();
                pattern.forEach(patternArray::add);
                json.add("pattern", patternArray);

                JsonObject keyObject = new JsonObject();
                keyObject.add("catalyst", catalyst.toJson());
                key.forEach((symbol, ingredient) -> keyObject.add(String.valueOf(symbol), ingredient.toJson()));
                json.add("key", keyObject);
            }

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
            return com.tonywww.dustandash.registry.DAARecipe.MILLING_SERIALIZER.get();
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
