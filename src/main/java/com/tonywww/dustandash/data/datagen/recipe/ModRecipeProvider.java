package com.tonywww.dustandash.data.datagen.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
        Consumer<FinishedRecipe> recipesOnly = recipe -> writer.accept(new RecipeWithoutAdvancement(recipe));

        SmeltingRecipes.buildRecipes(recipesOnly);
        SmithingRecipes.buildRecipes(recipesOnly);
        StonecuttingRecipes.buildRecipes(recipesOnly);
        CompactRecipes.buildRecipes(recipesOnly);
        MiscRecipes.buildRecipes(recipesOnly);
        CentrifugeRecipes.buildRecipes(recipesOnly);
        IonizerRecipes.buildRecipes(recipesOnly);
        IntegrateRecipes.buildRecipes(recipesOnly);
        MillingRecipesA.buildRecipes(recipesOnly);
        MillingRecipesB.buildRecipes(recipesOnly);
        CraftingRecipesA.buildRecipes(recipesOnly);
        CraftingRecipesB.buildRecipes(recipesOnly);
        CraftingRecipesC.buildRecipes(recipesOnly);
    }

    private record RecipeWithoutAdvancement(FinishedRecipe delegate) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            delegate.serializeRecipeData(json);
        }

        @Override
        public ResourceLocation getId() {
            return delegate.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return delegate.getType();
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
