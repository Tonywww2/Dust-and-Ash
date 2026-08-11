package com.tonywww.dustandash.data.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Datagen builder for {@code dustandash:ionizer} recipes (exactly 5 inputs, exactly 4 outputs).
 */
public final class IonizerRecipeBuilder {
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final List<Ingredient> outputs = new ArrayList<>();
    private Block inputBlock;
    private Block outputBlock;
    private int cost;
    private int tick;
    private boolean costElectrodes;

    private IonizerRecipeBuilder() {
    }

    public static IonizerRecipeBuilder ionizer() {
        return new IonizerRecipeBuilder();
    }

    public IonizerRecipeBuilder input(ItemLike item) {
        ingredients.add(Ingredient.of(item));
        return this;
    }

    public IonizerRecipeBuilder input(TagKey<Item> tag) {
        ingredients.add(Ingredient.of(tag));
        return this;
    }

    public IonizerRecipeBuilder input(Ingredient ingredient) {
        ingredients.add(ingredient);
        return this;
    }

    public IonizerRecipeBuilder output(ItemLike item) {
        outputs.add(Ingredient.of(item));
        return this;
    }

    public IonizerRecipeBuilder output(TagKey<Item> tag) {
        outputs.add(Ingredient.of(tag));
        return this;
    }

    public IonizerRecipeBuilder inputBlock(Block block) {
        this.inputBlock = block;
        return this;
    }

    public IonizerRecipeBuilder outputBlock(Block block) {
        this.outputBlock = block;
        return this;
    }

    public IonizerRecipeBuilder cost(int cost) {
        this.cost = cost;
        return this;
    }

    public IonizerRecipeBuilder tick(int tick) {
        this.tick = tick;
        return this;
    }

    public IonizerRecipeBuilder costElectrodes(boolean costElectrodes) {
        this.costElectrodes = costElectrodes;
        return this;
    }

    public void save(Consumer<FinishedRecipe> writer, String id) {
        if (ingredients.size() != 5) {
            throw new IllegalStateException("Ionizer recipe " + id + " requires exactly 5 ingredients, found " + ingredients.size());
        }
        if (outputs.size() != 4) {
            throw new IllegalStateException("Ionizer recipe " + id + " requires exactly 4 outputs, found " + outputs.size());
        }
        if (inputBlock == null || outputBlock == null) {
            throw new IllegalStateException("Ionizer recipe " + id + " requires both inputBlock and outputBlock");
        }
        writer.accept(new Result(new ResourceLocation(DustAndAsh.MOD_ID, id), List.copyOf(ingredients), inputBlock,
                List.copyOf(outputs), cost, tick, costElectrodes, outputBlock));
    }

    private record Result(ResourceLocation id, List<Ingredient> ingredients, Block inputBlock,
                           List<Ingredient> outputs, int cost, int tick, boolean costElectrodes,
                           Block outputBlock) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonArray ingredientsArray = new JsonArray();
            ingredients.forEach(ingredient -> ingredientsArray.add(ingredient.toJson()));
            json.add("ingredients", ingredientsArray);

            json.addProperty("inputBlock", ForgeRegistries.BLOCKS.getKey(inputBlock).toString());

            JsonArray outputsArray = new JsonArray();
            outputs.forEach(output -> outputsArray.add(output.toJson()));
            json.add("outputs", outputsArray);

            json.addProperty("cost", cost);
            json.addProperty("tick", tick);
            json.addProperty("costElectrodes", costElectrodes);
            json.addProperty("outputBlock", ForgeRegistries.BLOCKS.getKey(outputBlock).toString());
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public net.minecraft.world.item.crafting.RecipeSerializer<?> getType() {
            return com.tonywww.dustandash.registry.DAARecipe.IONIZER_SERIALIZER.get();
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
