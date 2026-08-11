package com.tonywww.dustandash.data.datagen.recipe;

import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAAItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

import static com.tonywww.dustandash.data.datagen.recipe.RecipeCriteria.has;

/** Converts part 1/3 of data/dustandash/recipes/crafting/*.json (minecraft:crafting_shaped/crafting_shapeless). */
public final class CraftingRecipesA {
    private CraftingRecipesA() {
    }

    private static TagKey<Item> forgeTag(String path) {
        return ItemTags.create(new ResourceLocation("forge", path));
    }

    private static TagKey<Item> modTag(String path) {
        return ItemTags.create(new ResourceLocation("dustandash", path));
    }

    private static TagKey<Item> vanillaTag(String path) {
        return ItemTags.create(new ResourceLocation("minecraft", path));
    }

    public static void buildRecipes(Consumer<FinishedRecipe> writer) {
        // ash_collector.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.ASH_COLLECTOR.get())
                .pattern("###")
                .pattern("SHS")
                .pattern("S_S")
                .define('#', Items.IRON_BARS)
                .define('S', Items.STONE_BRICKS)
                .define('H', Items.HOPPER)
                .define('_', Items.SMOOTH_STONE_SLAB)
                .unlockedBy("has_iron_bars", has(Items.IRON_BARS))
                .save(writer, "dustandash:crafting/ash_collector");

        // ash_steel_from_block.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.ASH_STEEL_INGOT.get(), 9)
                .requires(DAABlocks.BLOCK_OF_ASH_STEEL.get())
                .unlockedBy("has_block_of_ash_steel", has(DAABlocks.BLOCK_OF_ASH_STEEL.get()))
                .save(writer, "dustandash:crafting/ash_steel_from_block");

        // ash_steel_heavy_plate.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.ASH_STEEL_HEAVY_PLATE.get())
                .pattern("#A#")
                .pattern("A#A")
                .pattern("#A#")
                .define('#', DAAItems.CRYSTALLIZE_DUST.get())
                .define('A', DAAItems.ASH_STEEL_INGOT.get())
                .unlockedBy("has_ash_steel_ingot", has(DAAItems.ASH_STEEL_INGOT.get()))
                .save(writer, "dustandash:crafting/ash_steel_heavy_plate");

        // basic_cooling.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.BASIC_COOLING.get())
                .requires(DAAItems.EMPTY_FUEL_CONTAINER.get())
                .requires(Items.ICE)
                .requires(Items.WATER_BUCKET)
                .unlockedBy("has_empty_fuel_container", has(DAAItems.EMPTY_FUEL_CONTAINER.get()))
                .save(writer, "dustandash:crafting/basic_cooling");

        // beetroot_seeds.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.BEETROOT_SEEDS)
                .pattern("#R#")
                .pattern("SWS")
                .pattern(" D ")
                .define('#', DAAItems.REPRODUCE_DUST.get())
                .define('R', Items.RED_DYE)
                .define('S', Items.SUGAR)
                .define('W', Items.WHEAT_SEEDS)
                .define('D', DAAItems.ABSORB_DUST.get())
                .unlockedBy("has_wheat_seeds", has(Items.WHEAT_SEEDS))
                .save(writer, "dustandash:crafting/beetroot_seeds");

        // block_of_ash_steel.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAABlocks.BLOCK_OF_ASH_STEEL.get())
                .requires(DAAItems.ASH_STEEL_INGOT.get())
                .requires(DAAItems.ASH_STEEL_INGOT.get())
                .requires(DAAItems.ASH_STEEL_INGOT.get())
                .requires(DAAItems.ASH_STEEL_INGOT.get())
                .requires(DAAItems.ASH_STEEL_INGOT.get())
                .requires(DAAItems.ASH_STEEL_INGOT.get())
                .requires(DAAItems.ASH_STEEL_INGOT.get())
                .requires(DAAItems.ASH_STEEL_INGOT.get())
                .requires(DAAItems.ASH_STEEL_INGOT.get())
                .unlockedBy("has_ash_steel_ingot", has(DAAItems.ASH_STEEL_INGOT.get()))
                .save(writer, "dustandash:crafting/block_of_ash_steel");

        // bloody_flint_stick.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.BLOODY_FLINT_STICK.get(), 3)
                .pattern("#")
                .pattern("#")
                .define('#', DAAItems.BLOODY_FLINT.get())
                .unlockedBy("has_bloody_flint", has(DAAItems.BLOODY_FLINT.get()))
                .save(writer, "dustandash:crafting/bloody_flint_stick");

        // bowl_with_dust.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.BOWL_WITH_DUST.get())
                .requires(Items.BOWL)
                .requires(modTag("element_dust"))
                .requires(modTag("element_dust"))
                .requires(modTag("element_dust"))
                .requires(modTag("element_dust"))
                .requires(modTag("element_dust"))
                .requires(modTag("element_dust"))
                .requires(modTag("element_dust"))
                .unlockedBy("has_bowl", has(Items.BOWL))
                .save(writer, "dustandash:crafting/bowl_with_dust");

        // brain_coral_block.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.BRAIN_CORAL_BLOCK)
                .pattern("#D#")
                .pattern("YPQ")
                .pattern("#TC")
                .define('#', DAAItems.ORDER_DUST.get())
                .define('D', DAAItems.ABSORB_DUST.get())
                .define('Y', DAAItems.INHERIT_DUST.get())
                .define('P', Items.PINK_DYE)
                .define('Q', DAAItems.METAMORPHOSE_DUST.get())
                .define('T', DAAItems.REPRODUCE_DUST.get())
                .define('C', DAAItems.LIFE_DUST.get())
                .unlockedBy("has_order_dust", has(DAAItems.ORDER_DUST.get()))
                .save(writer, "dustandash:crafting/brain_coral_block");

        // brown_mushroom.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.BROWN_MUSHROOM)
                .pattern("#L#")
                .pattern("DGD")
                .pattern("YTY")
                .define('#', DAAItems.METAMORPHOSE_DUST.get())
                .define('L', Items.LIGHT_GRAY_DYE)
                .define('D', DAAItems.REPRODUCE_DUST.get())
                .define('G', Items.GRASS)
                .define('Y', DAAItems.EARTH_DUST.get())
                .define('T', DAAItems.ABSORB_DUST.get())
                .unlockedBy("has_metamorphose_dust", has(DAAItems.METAMORPHOSE_DUST.get()))
                .save(writer, "dustandash:crafting/brown_mushroom");

        // bubble_coral_block.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.BUBBLE_CORAL_BLOCK)
                .pattern("#D#")
                .pattern("OPA")
                .pattern("#IW")
                .define('#', DAAItems.ORDER_DUST.get())
                .define('D', DAAItems.ABSORB_DUST.get())
                .define('O', DAAItems.INHERIT_DUST.get())
                .define('P', Items.PURPLE_DYE)
                .define('A', DAAItems.METAMORPHOSE_DUST.get())
                .define('I', DAAItems.REPRODUCE_DUST.get())
                .define('W', DAAItems.LIFE_DUST.get())
                .unlockedBy("has_order_dust", has(DAAItems.ORDER_DUST.get()))
                .save(writer, "dustandash:crafting/bubble_coral_block");

        // carrot.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.CARROT)
                .requires(Items.BEETROOT)
                .requires(DAAItems.METAMORPHOSE_DUST.get())
                .requires(Items.ORANGE_DYE)
                .requires(DAAItems.REPRODUCE_DUST.get())
                .unlockedBy("has_beetroot", has(Items.BEETROOT))
                .save(writer, "dustandash:crafting/carrot");

        // centrifuge.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.CENTRIFUGE.get())
                .pattern("CBC")
                .pattern("GEG")
                .pattern("CTC")
                .define('B', Items.PISTON)
                .define('G', Items.GLASS)
                .define('C', DAAItems.ASH_STEEL_CYLINDER.get())
                .define('T', DAAItems.IRON_STRUCTURAL_COMPONENTS.get())
                .define('E', DAAItems.REACTION_CHAMBER.get())
                .unlockedBy("has_ash_steel_cylinder", has(DAAItems.ASH_STEEL_CYLINDER.get()))
                .save(writer, "dustandash:crafting/centrifuge");

        // cobblestone_with_moss.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAABlocks.COBBLESTONE_WITH_MOSS.get(), 3)
                .requires(forgeTag("cobblestone"))
                .requires(forgeTag("cobblestone"))
                .requires(Items.MOSS_BLOCK)
                .unlockedBy("has_moss_block", has(Items.MOSS_BLOCK))
                .save(writer, "dustandash:crafting/cobblestone_with_moss");

        // cocoa_beans.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.COCOA_BEANS)
                .pattern("#SD")
                .pattern("JEB")
                .pattern("#SD")
                .define('#', DAAItems.ABSORB_DUST.get())
                .define('S', Items.SUGAR)
                .define('D', DAAItems.REPRODUCE_DUST.get())
                .define('J', Items.JUNGLE_LEAVES)
                .define('E', DAAItems.METAMORPHOSE_DUST.get())
                .define('B', Items.BLACK_DYE)
                .unlockedBy("has_jungle_leaves", has(Items.JUNGLE_LEAVES))
                .save(writer, "dustandash:crafting/cocoa_beans");

        // cooled_magma_block.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAABlocks.COOLED_MAGMA_BLOCK.get())
                .requires(Items.NETHERRACK)
                .requires(Items.SLIME_BALL)
                .requires(DAAItems.FIRE_DUST.get())
                .requires(Items.COBBLESTONE)
                .unlockedBy("has_netherrack", has(Items.NETHERRACK))
                .save(writer, "dustandash:crafting/cooled_magma_block");

        // cooling_tier_1.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.COOLING_TIER_1.get())
                .requires(DAAItems.BASIC_COOLING.get())
                .requires(Items.ICE)
                .requires(Items.ICE)
                .unlockedBy("has_basic_cooling", has(DAAItems.BASIC_COOLING.get()))
                .save(writer, "dustandash:crafting/cooling_tier_1");

        // cooling_tier_2.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.COOLING_TIER_2.get())
                .pattern("C C")
                .pattern("BAB")
                .pattern("C C")
                .define('A', DAAItems.COOLING_TIER_1.get())
                .define('B', Items.PACKED_ICE)
                .define('C', DAAItems.ABSORB_DUST.get())
                .unlockedBy("has_cooling_tier_1", has(DAAItems.COOLING_TIER_1.get()))
                .save(writer, "dustandash:crafting/cooling_tier_2");

        // cooling_tier_3.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.COOLING_TIER_3.get())
                .pattern("CDC")
                .pattern("BAB")
                .pattern("C C")
                .define('A', DAAItems.COOLING_TIER_2.get())
                .define('B', DAAItems.BOWL_WITH_DUST.get())
                .define('C', Items.BLUE_ICE)
                .define('D', DAAItems.ELECTRON.get())
                .unlockedBy("has_cooling_tier_2", has(DAAItems.COOLING_TIER_2.get()))
                .save(writer, "dustandash:crafting/cooling_tier_3");

        // copper_block.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COPPER_BLOCK)
                .requires(forgeTag("ingots/copper"))
                .requires(forgeTag("ingots/copper"))
                .requires(forgeTag("ingots/copper"))
                .requires(forgeTag("ingots/copper"))
                .requires(forgeTag("ingots/copper"))
                .requires(forgeTag("ingots/copper"))
                .requires(forgeTag("ingots/copper"))
                .requires(forgeTag("ingots/copper"))
                .requires(forgeTag("ingots/copper"))
                .unlockedBy("has_copper_ingot", has(forgeTag("ingots/copper")))
                .save(writer, "dustandash:crafting/copper_block");

        // crushed_seeds.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.CRUSHED_SEEDS.get())
                .requires(forgeTag("seeds"))
                .requires(forgeTag("seeds"))
                .requires(forgeTag("seeds"))
                .requires(DAAItems.SHARPEN_FLINT.get())
                .unlockedBy("has_sharpen_flint", has(DAAItems.SHARPEN_FLINT.get()))
                .save(writer, "dustandash:crafting/crushed_seeds");

        // dust_source.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.DUST_SOURCE.get())
                .pattern("###")
                .pattern("#A#")
                .pattern("###")
                .define('#', Items.GRAVEL)
                .define('A', DAAItems.BLOODY_FLINT.get())
                .unlockedBy("has_bloody_flint", has(DAAItems.BLOODY_FLINT.get()))
                .save(writer, "dustandash:crafting/dust_source");

        // earth_gem.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.EARTH_GEM.get())
                .requires(forgeTag("gems"))
                .requires(DAAItems.BOWL_WITH_DUST.get())
                .requires(DAAItems.EARTH_DUST.get())
                .requires(DAAItems.EARTH_DUST.get())
                .requires(DAAItems.EARTH_DUST.get())
                .requires(DAAItems.EARTH_DUST.get())
                .requires(DAAItems.EARTH_DUST.get())
                .requires(DAAItems.EARTH_DUST.get())
                .requires(DAAItems.EARTH_DUST.get())
                .unlockedBy("has_earth_dust", has(DAAItems.EARTH_DUST.get()))
                .save(writer, "dustandash:crafting/earth_gem");

        // echo_activator.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.ECHO_ACTIVATOR.get())
                .pattern("AB ")
                .pattern("CDE")
                .pattern(" FG")
                .define('A', DAAItems.BLOODY_FLINT.get())
                .define('B', DAAItems.ABSORB_DUST.get())
                .define('C', DAAItems.METAMORPHOSE_DUST.get())
                .define('D', Items.BLUE_CANDLE)
                .define('E', DAAItems.SEEP_DUST.get())
                .define('F', DAAItems.EXTINGUISH_DUST.get())
                .define('G', DAAItems.BLOODY_FLINT_STICK.get())
                .unlockedBy("has_bloody_flint", has(DAAItems.BLOODY_FLINT.get()))
                .save(writer, "dustandash:crafting/echo_activator");

        // empty_fuel_container.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.EMPTY_FUEL_CONTAINER.get())
                .pattern("ACA")
                .pattern("BBB")
                .pattern("ABA")
                .define('A', DAAItems.ASH_STEEL_CYLINDER.get())
                .define('B', Items.IRON_BARS)
                .define('C', forgeTag("glass"))
                .unlockedBy("has_ash_steel_cylinder", has(DAAItems.ASH_STEEL_CYLINDER.get()))
                .save(writer, "dustandash:crafting/empty_fuel_container");

        // energized_cobblestone.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.ENERGIZED_COBBLESTONE.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', DAAItems.DUST_WITH_ENERGY.get())
                .define('B', forgeTag("cobblestone"))
                .define('C', DAAItems.METAL_DUST.get())
                .unlockedBy("has_dust_with_energy", has(DAAItems.DUST_WITH_ENERGY.get()))
                .save(writer, "dustandash:crafting/energized_cobblestone");

        // fire_coral_block.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.FIRE_CORAL_BLOCK)
                .pattern("#D#")
                .pattern("URG")
                .pattern("#XE")
                .define('#', DAAItems.ORDER_DUST.get())
                .define('D', DAAItems.ABSORB_DUST.get())
                .define('U', DAAItems.INHERIT_DUST.get())
                .define('R', Items.RED_DYE)
                .define('G', DAAItems.METAMORPHOSE_DUST.get())
                .define('X', DAAItems.REPRODUCE_DUST.get())
                .define('E', DAAItems.LIFE_DUST.get())
                .unlockedBy("has_order_dust", has(DAAItems.ORDER_DUST.get()))
                .save(writer, "dustandash:crafting/fire_coral_block");

        // fire_gem.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.FIRE_GEM.get())
                .requires(forgeTag("gems"))
                .requires(DAAItems.BOWL_WITH_DUST.get())
                .requires(DAAItems.FIRE_DUST.get())
                .requires(DAAItems.FIRE_DUST.get())
                .requires(DAAItems.FIRE_DUST.get())
                .requires(DAAItems.FIRE_DUST.get())
                .requires(DAAItems.FIRE_DUST.get())
                .requires(DAAItems.FIRE_DUST.get())
                .requires(DAAItems.FIRE_DUST.get())
                .unlockedBy("has_fire_dust", has(DAAItems.FIRE_DUST.get()))
                .save(writer, "dustandash:crafting/fire_gem");

        // fission_reactor_casing.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.FISSION_REACTOR_CASING.get(), 2)
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', DAAItems.ASH_STEEL_INGOT.get())
                .define('B', DAABlocks.STRENGTHENED_CEMENT.get())
                .define('C', DAAItems.DUST_WITH_ENERGY.get())
                .unlockedBy("has_ash_steel_ingot", has(DAAItems.ASH_STEEL_INGOT.get()))
                .save(writer, "dustandash:crafting/fission_reactor_casing");

        // fission_reactor_cooling_cell.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.FISSION_REACTOR_COOLING_CELL.get(), 2)
                .pattern("ADA")
                .pattern("BCB")
                .pattern("ADA")
                .define('A', DAAItems.ASH_STEEL_INGOT.get())
                .define('B', DAAItems.CARBON_FIBER_PLATE.get())
                .define('C', DAAItems.REACTION_CHAMBER.get())
                .define('D', DAABlocks.FISSION_REACTOR_CASING.get())
                .unlockedBy("has_fission_reactor_casing", has(DAABlocks.FISSION_REACTOR_CASING.get()))
                .save(writer, "dustandash:crafting/fission_reactor_cooling_cell");

        // fission_reactor_fuel_cell.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.FISSION_REACTOR_FUEL_CELL.get(), 2)
                .pattern("ADA")
                .pattern("BCB")
                .pattern("ADA")
                .define('A', DAAItems.ASH_STEEL_INGOT.get())
                .define('B', DAAItems.CARBON_FIBER_PLATE.get())
                .define('C', DAAItems.EMPTY_FUEL_CONTAINER.get())
                .define('D', DAABlocks.FISSION_REACTOR_CASING.get())
                .unlockedBy("has_fission_reactor_casing", has(DAABlocks.FISSION_REACTOR_CASING.get()))
                .save(writer, "dustandash:crafting/fission_reactor_fuel_cell");

        // fission_reactor_interface.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.FISSION_REACTOR_INTERFACE.get())
                .pattern("AAB")
                .pattern("CDE")
                .pattern("AAB")
                .define('A', DAAItems.GLASS_CONTAINER.get())
                .define('B', DAAItems.ASH_STEEL_LEVER.get())
                .define('C', Items.HOPPER)
                .define('D', DAAItems.PLACEHOLDER.get())
                .define('E', DAABlocks.FISSION_REACTOR_CASING.get())
                .unlockedBy("has_fission_reactor_casing", has(DAABlocks.FISSION_REACTOR_CASING.get()))
                .save(writer, "dustandash:crafting/fission_reactor_interface");

        // flint_pickaxe.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.FLINT_PICKAXE.get())
                .pattern("##")
                .pattern(" /")
                .define('#', DAAItems.BLOODY_FLINT.get())
                .define('/', forgeTag("rods/wooden"))
                .unlockedBy("has_bloody_flint", has(DAAItems.BLOODY_FLINT.get()))
                .save(writer, "dustandash:crafting/flint_pickaxe");

        // gravel.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GRAVEL, 3)
                .requires(forgeTag("cobblestone"))
                .requires(forgeTag("cobblestone"))
                .requires(forgeTag("cobblestone"))
                .requires(Items.MOSS_CARPET)
                .unlockedBy("has_moss_carpet", has(Items.MOSS_CARPET))
                .save(writer, "dustandash:crafting/gravel");

        // grind_solvents.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.GRIND_SOLVENTS.get(), 2)
                .requires(DAAItems.CHLORINE.get())
                .requires(DAAItems.WOOD_ASH.get())
                .requires(DAAItems.SHARPEN_FLINT.get())
                .requires(RecipeIngredients.nbt(Items.POTION, "{Potion:'minecraft:strength'}"))
                .unlockedBy("has_chlorine", has(DAAItems.CHLORINE.get()))
                .save(writer, "dustandash:crafting/grind_solvents");

        // hand_vacuum.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.HAND_VACUUM.get())
                .pattern("AA ")
                .pattern("FCD")
                .pattern(" FE")
                .define('A', vanillaTag("wooden_slabs"))
                .define('C', forgeTag("fences/wooden"))
                .define('D', Items.BOWL)
                .define('E', Items.STICK)
                .define('F', DAAItems.BLOODY_FLINT.get())
                .unlockedBy("has_bloody_flint", has(DAAItems.BLOODY_FLINT.get()))
                .save(writer, "dustandash:crafting/hand_vacuum");
    }
}
