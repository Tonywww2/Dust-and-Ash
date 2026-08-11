package com.tonywww.dustandash.data.datagen.recipe;

import com.tonywww.dustandash.registry.DAAItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

import static com.tonywww.dustandash.data.datagen.recipe.RecipeCriteria.has;

/**
 * Converts data/dustandash/recipes/stonecutting/*.json. Despite the folder name, six of the
 * files are actually {@code minecraft:crafting_shaped} recipes (kept as-is from the source data).
 */
public final class StonecuttingRecipes {
    private StonecuttingRecipes() {
    }

    public static void buildRecipes(Consumer<FinishedRecipe> writer) {
        stonecutting(writer, "brain_coral_fan", Items.BRAIN_CORAL_BLOCK, Items.BRAIN_CORAL_FAN, 2);
        stonecutting(writer, "brain_coral", Items.BRAIN_CORAL_BLOCK, Items.BRAIN_CORAL, 2);
        stonecutting(writer, "bubble_coral", Items.BUBBLE_CORAL_BLOCK, Items.BUBBLE_CORAL, 2);
        stonecutting(writer, "bubble_coral_fan", Items.BUBBLE_CORAL_BLOCK, Items.BUBBLE_CORAL_FAN, 2);
        stonecutting(writer, "fire_coral", Items.FIRE_CORAL_BLOCK, Items.FIRE_CORAL, 2);
        stonecutting(writer, "fire_coral_fan", Items.FIRE_CORAL_BLOCK, Items.FIRE_CORAL_FAN, 2);
        stonecutting(writer, "horn_coral", Items.HORN_CORAL_BLOCK, Items.HORN_CORAL, 2);
        stonecutting(writer, "horn_coral_fan", Items.HORN_CORAL_BLOCK, Items.HORN_CORAL_FAN, 2);
        stonecutting(writer, "tube_coral", Items.TUBE_CORAL_BLOCK, Items.TUBE_CORAL, 2);
        stonecutting(writer, "tube_coral_fan", Items.TUBE_CORAL_BLOCK, Items.TUBE_CORAL_FAN, 2);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.BAMBOO)
                .pattern("#DG")
                .pattern("X/X")
                .pattern("XDX")
                .define('#', DAAItems.REPRODUCE_DUST.get())
                .define('D', DAAItems.LIFE_DUST.get())
                .define('G', DAAItems.ABSORB_DUST.get())
                .define('X', DAAItems.ORDER_DUST.get())
                .define('/', Items.STICK)
                .unlockedBy("has_order_dust", has(DAAItems.ORDER_DUST.get()))
                .save(writer, "dustandash:stonecutting/bamboo");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CACTUS)
                .pattern("#D#")
                .pattern("CSC")
                .pattern("JAU")
                .define('#', DAAItems.LIFE_DUST.get())
                .define('D', DAAItems.FIRE_DUST.get())
                .define('C', DAAItems.LACERATE_DUST.get())
                .define('S', Items.SUGAR_CANE)
                .define('J', DAAItems.REPRODUCE_DUST.get())
                .define('A', DAAItems.ABSORB_DUST.get())
                .define('U', DAAItems.SEEP_DUST.get())
                .unlockedBy("has_fire_dust", has(DAAItems.FIRE_DUST.get()))
                .save(writer, "dustandash:stonecutting/cactus");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.DEAD_BUSH)
                .pattern(" # ")
                .pattern("DOD")
                .pattern(" H ")
                .define('#', DAAItems.EXTINGUISH_DUST.get())
                .define('D', DAAItems.FIRE_DUST.get())
                .define('O', ItemTags.SAPLINGS)
                .define('H', DAAItems.EARTH_DUST.get())
                .unlockedBy("has_fire_dust", has(DAAItems.FIRE_DUST.get()))
                .save(writer, "dustandash:stonecutting/dead_bush");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.GRASS_BLOCK)
                .pattern("###")
                .pattern("DLN")
                .pattern(" M ")
                .define('#', DAAItems.LIFE_DUST.get())
                .define('D', DAAItems.REPRODUCE_DUST.get())
                .define('L', Items.DIRT)
                .define('N', DAAItems.SEEP_DUST.get())
                .define('M', DAAItems.BLOCKING_DUST.get())
                .unlockedBy("has_life_dust", has(DAAItems.LIFE_DUST.get()))
                .save(writer, "dustandash:stonecutting/grass_block");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.LILY_PAD)
                .pattern("#D#")
                .pattern("WGN")
                .pattern("#K#")
                .define('#', DAAItems.LIFE_DUST.get())
                .define('D', DAAItems.BLOCKING_DUST.get())
                .define('W', DAAItems.INHERIT_DUST.get())
                .define('G', Items.GREEN_CARPET)
                .define('N', DAAItems.REPRODUCE_DUST.get())
                .define('K', DAAItems.ABSORB_DUST.get())
                .unlockedBy("has_life_dust", has(DAAItems.LIFE_DUST.get()))
                .save(writer, "dustandash:stonecutting/lily_pad");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.SUGAR_CANE)
                .pattern("#DM")
                .pattern("D/D")
                .pattern("VDV")
                .define('#', DAAItems.REPRODUCE_DUST.get())
                .define('D', DAAItems.LIFE_DUST.get())
                .define('M', DAAItems.ABSORB_DUST.get())
                .define('/', Items.STICK)
                .define('V', DAAItems.ORDER_DUST.get())
                .unlockedBy("has_life_dust", has(DAAItems.LIFE_DUST.get()))
                .save(writer, "dustandash:stonecutting/suga_cane");
    }

    private static void stonecutting(Consumer<FinishedRecipe> writer, String name, net.minecraft.world.item.Item input,
                                      net.minecraft.world.item.Item result, int count) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), RecipeCategory.DECORATIONS, result, count)
                .unlockedBy("has_" + name, has(input))
                .save(writer, "dustandash:stonecutting/" + name);
    }
}
