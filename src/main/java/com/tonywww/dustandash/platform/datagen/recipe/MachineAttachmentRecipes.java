//? if forge {
package com.tonywww.dustandash.platform.datagen.recipe;

import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAAItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

import static com.tonywww.dustandash.platform.datagen.recipe.RecipeCriteria.has;

public final class MachineAttachmentRecipes {
    private MachineAttachmentRecipes() {}

    public static void buildRecipes(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.CENTRIFUGE_ACCELERATOR_1.get())
                .pattern("SRS").pattern("GFG").pattern("SRS")
                .define('S', DAAItems.ASH_STEEL_INGOT.get()).define('R', Items.REDSTONE)
                .define('G', DAAItems.IRON_GEAR.get()).define('F', DAAItems.BASIC_MACHINE_FRAME.get())
                .unlockedBy("has_ash_steel", has(DAAItems.ASH_STEEL_INGOT.get()))
                .save(writer, "dustandash:crafting/centrifuge_accelerator_1");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.CENTRIFUGE_ACCELERATOR_2.get())
                .pattern(" S ").pattern("GAG").pattern("R R")
                .define('S', DAAItems.ASH_STEEL_STRUCTURAL_COMPONENTS.get()).define('R', DAAItems.REDSTONE_GEAR.get())
                .define('G', DAAItems.ASH_STEEL_GEAR.get()).define('A', DAABlocks.CENTRIFUGE_ACCELERATOR_1.get())
                .unlockedBy("has_accelerator", has(DAABlocks.CENTRIFUGE_ACCELERATOR_1.get()))
                .save(writer, "dustandash:crafting/centrifuge_accelerator_2");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.CENTRIFUGE_ACCELERATOR_3.get())
                .pattern("TUT").pattern("GAG").pattern("TUT")
                .define('T', DAAItems.TUNGSTEN_INGOT.get()).define('U', DAAItems.URANIUM_INGOT.get())
                .define('G', DAAItems.ASH_STEEL_GEAR.get()).define('A', DAABlocks.CENTRIFUGE_ACCELERATOR_2.get())
                .unlockedBy("has_accelerator", has(DAABlocks.CENTRIFUGE_ACCELERATOR_2.get()))
                .save(writer, "dustandash:crafting/centrifuge_accelerator_3");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.CENTRIFUGE_ACCELERATOR_4.get())
                .pattern("TCT").pattern("GAG").pattern("TCT")
                .define('T', DAAItems.TITANIUM_INGOT.get()).define('C', DAAItems.CARBON_FIBER.get())
                .define('G', DAAItems.REDSTONE_GEAR.get()).define('A', DAABlocks.CENTRIFUGE_ACCELERATOR_3.get())
                .unlockedBy("has_accelerator", has(DAABlocks.CENTRIFUGE_ACCELERATOR_3.get()))
                .save(writer, "dustandash:crafting/centrifuge_accelerator_4");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.AUTOMATIC_MILLING_RECYCLER.get())
                .pattern("SRS").pattern("HFH").pattern("SCS")
                .define('S', DAAItems.ASH_STEEL_INGOT.get()).define('R', Items.COMPARATOR)
                .define('H', Items.HOPPER).define('F', DAAItems.BASIC_MACHINE_FRAME.get()).define('C', Items.CHEST)
                .unlockedBy("has_ash_steel", has(DAAItems.ASH_STEEL_INGOT.get()))
                .save(writer, "dustandash:crafting/automatic_milling_recycler");
    }
}
//?}
