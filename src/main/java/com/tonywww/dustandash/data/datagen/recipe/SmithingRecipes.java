package com.tonywww.dustandash.data.datagen.recipe;

import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAAItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

import static com.tonywww.dustandash.data.datagen.recipe.RecipeCriteria.has;

/** Converts data/dustandash/recipes/smithing/*.json (all minecraft:smithing_transform). */
public final class SmithingRecipes {
    private SmithingRecipes() {
    }

    public static void buildRecipes(Consumer<FinishedRecipe> writer) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(DAAItems.EARTH_DUST.get()),
                        Ingredient.of(Items.BUCKET), Ingredient.of(DAABlocks.ENERGIZED_COBBLESTONE.get()),
                        RecipeCategory.MISC, DAAItems.ENERGIZED_COBBLESTONE_BUCKET.get())
                .unlocks("has_earth_dust", has(DAAItems.EARTH_DUST.get()))
                .save(writer, "dustandash:smithing/energized_cobblestone_bucket");

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(DAAItems.EARTH_DUST.get()),
                        Ingredient.of(Items.COBBLESTONE), Ingredient.of(DAAItems.EARTH_DUST.get()),
                        RecipeCategory.MISC, Items.GRAVEL)
                .unlocks("has_earth_dust", has(DAAItems.EARTH_DUST.get()))
                .save(writer, "dustandash:smithing/gravel");

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(DAAItems.EARTH_DUST.get()),
                        Ingredient.of(Items.GRAVEL), Ingredient.of(DAAItems.EARTH_DUST.get()),
                        RecipeCategory.MISC, Items.SAND)
                .unlocks("has_earth_dust", has(DAAItems.EARTH_DUST.get()))
                .save(writer, "dustandash:smithing/sand");

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(DAAItems.EARTH_DUST.get()),
                        Ingredient.of(Items.NETHERRACK), Ingredient.of(DAAItems.EXTINGUISH_DUST.get()),
                        RecipeCategory.MISC, Items.END_STONE)
                .unlocks("has_extinguish_dust", has(DAAItems.EXTINGUISH_DUST.get()))
                .save(writer, "dustandash:smithing/end_stone");
    }
}
