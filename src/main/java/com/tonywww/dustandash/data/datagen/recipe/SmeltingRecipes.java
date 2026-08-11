package com.tonywww.dustandash.data.datagen.recipe;

import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAAItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

import static com.tonywww.dustandash.data.datagen.recipe.RecipeCriteria.has;

/** Converts data/dustandash/recipes/smelting/*.json. */
public final class SmeltingRecipes {
    private SmeltingRecipes() {
    }

    public static void buildRecipes(Consumer<FinishedRecipe> writer) {
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(DAAItems.RAW_ASH_STEEL.get()), RecipeCategory.MISC,
                        DAAItems.ASH_STEEL_INGOT.get(), 0.2f, 300)
                .unlockedBy("has_raw_ash_steel", has(DAAItems.RAW_ASH_STEEL.get()))
                .save(writer, "dustandash:smelting/ash_steel_ingot");

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(DAAItems.RAW_CARBON_FIBER.get()), RecipeCategory.MISC,
                        DAAItems.CARBON_FIBER.get(), 0.4f, 600)
                .unlockedBy("has_raw_carbon_fiber", has(DAAItems.RAW_CARBON_FIBER.get()))
                .save(writer, "dustandash:smelting/carbon_fiber");

        writer.accept(SimpleFixedResult.smelting("dustandash:smelting/charcoal",
                Ingredient.of(DAABlocks.LOG_PILE.get()), Items.CHARCOAL, 6, 0.2f, 1200));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(DAAItems.METAL_DUST.get()), RecipeCategory.MISC,
                        DAAItems.DUST_WITH_ENERGY.get(), 0.1f, 150)
                .unlockedBy("has_metal_dust", has(DAAItems.METAL_DUST.get()))
                .save(writer, "dustandash:smelting/dust_with_energy");

        writer.accept(SimpleFixedResult.smelting("dustandash:smelting/electron", Ingredient.of(DAAItems.MAGNET.get()),
                DAAItems.ELECTRON.get(), 2, 0.2f, 600));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(DAAItems.ENERGIZED_COBBLESTONE_BUCKET.get()), RecipeCategory.MISC,
                        Items.LAVA_BUCKET, 1.0f, 6000)
                .unlockedBy("has_energized_cobblestone_bucket", has(DAAItems.ENERGIZED_COBBLESTONE_BUCKET.get()))
                .save(writer, "dustandash:smelting/lava_bucket");

        writer.accept(SimpleFixedResult.blasting("dustandash:smelting/magma_block",
                Ingredient.of(DAABlocks.COOLED_MAGMA_BLOCK.get()), Items.MAGMA_BLOCK, 1, 0.2f, 400));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(DAAItems.TITANIUM_CHUNK.get()), RecipeCategory.MISC,
                        DAAItems.TITANIUM_INGOT.get(), 0.2f, 200)
                .unlockedBy("has_titanium_chunk", has(DAAItems.TITANIUM_CHUNK.get()))
                .save(writer, "dustandash:smelting/titanium_ingot");

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(DAAItems.TITANIUM_SAND.get()), RecipeCategory.MISC,
                        DAAItems.TITANIUM_SCRAP.get(), 0.4f, 6000)
                .unlockedBy("has_titanium_sand", has(DAAItems.TITANIUM_SAND.get()))
                .save(writer, "dustandash:smelting/titanium_scrap");

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(DAAItems.TUNGSTEN_DUST.get()), RecipeCategory.MISC,
                        DAAItems.TUNGSTEN_INGOT.get(), 0.2f, 1200)
                .unlockedBy("has_tungsten_dust", has(DAAItems.TUNGSTEN_DUST.get()))
                .save(writer, "dustandash:smelting/tungsten_ingot");
    }
}
