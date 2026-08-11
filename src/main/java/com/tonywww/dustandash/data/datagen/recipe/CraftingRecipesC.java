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

/** Converts part 3/3 of data/dustandash/recipes/crafting/*.json (minecraft:crafting_shaped/crafting_shapeless). */
public final class CraftingRecipesC {
    private static final TagKey<Item> FORGE_SAND = ItemTags.create(new ResourceLocation("forge", "sand"));
    private static final TagKey<Item> FORGE_INGOTS_LEAD = ItemTags.create(new ResourceLocation("forge", "ingots/lead"));
    private static final TagKey<Item> FORGE_INGOTS_TITANIUM = ItemTags.create(new ResourceLocation("forge", "ingots/titanium"));
    private static final TagKey<Item> FORGE_RODS_WOODEN = ItemTags.create(new ResourceLocation("forge", "rods/wooden"));

    private CraftingRecipesC() {
    }

    public static void buildRecipes(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.PUMPKIN)
                .pattern(" # ")
                .pattern("DGO")
                .pattern("SKX")
                .define('#', DAAItems.LIFE_DUST.get())
                .define('D', DAAItems.REPRODUCE_DUST.get())
                .define('G', Items.ORANGE_WOOL)
                .define('O', DAAItems.METAMORPHOSE_DUST.get())
                .define('S', Items.SUGAR)
                .define('K', DAAItems.ABSORB_DUST.get())
                .define('X', DAAItems.EARTH_DUST.get())
                .unlockedBy("has_life_dust", has(DAAItems.LIFE_DUST.get()))
                .save(writer, "dustandash:crafting/pumpkin");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.RAIN_CRYSTAL.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', DAAItems.ORDER_DUST.get())
                .define('B', Items.SNOWBALL)
                .unlockedBy("has_order_dust", has(DAAItems.ORDER_DUST.get()))
                .save(writer, "dustandash:crafting/rain_crystal");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.RAW_CARBON_FIBER.get(), 2)
                .requires(Items.STRING)
                .requires(DAAItems.CARBON_DUST.get())
                .requires(DAAItems.CARBON_DUST.get())
                .requires(Items.STRING)
                .unlockedBy("has_carbon_dust", has(DAAItems.CARBON_DUST.get()))
                .save(writer, "dustandash:crafting/raw_carbon_fiber");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.RAW_ASH_STEEL.get())
                .requires(DAAItems.ASH_STEEL_SCRAP.get())
                .requires(DAAItems.ASH_STEEL_SCRAP.get())
                .requires(DAAItems.ASH_STEEL_SCRAP.get())
                .requires(DAAItems.ASH_STEEL_SCRAP.get())
                .requires(DAAItems.ASH_STEEL_SCRAP.get())
                .requires(DAAItems.ASH_STEEL_SCRAP.get())
                .requires(DAAItems.ASH_STEEL_SCRAP.get())
                .requires(DAAItems.ASH_STEEL_SCRAP.get())
                .requires(DAAItems.ASH_STEEL_SCRAP.get())
                .unlockedBy("has_ash_steel_scrap", has(DAAItems.ASH_STEEL_SCRAP.get()))
                .save(writer, "dustandash:crafting/recycle_ash_steel_scrap");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COAL)
                .requires(DAAItems.CARBON_FIBER_SCRAP.get())
                .requires(DAAItems.CARBON_FIBER_SCRAP.get())
                .requires(DAAItems.CARBON_FIBER_SCRAP.get())
                .requires(DAAItems.CARBON_FIBER_SCRAP.get())
                .unlockedBy("has_carbon_fiber_scrap", has(DAAItems.CARBON_FIBER_SCRAP.get()))
                .save(writer, "dustandash:crafting/recycle_carbon_fiber_scrap");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.RAW_IRON)
                .requires(DAAItems.IRON_SCRAP.get())
                .requires(DAAItems.IRON_SCRAP.get())
                .requires(DAAItems.IRON_SCRAP.get())
                .requires(DAAItems.IRON_SCRAP.get())
                .requires(DAAItems.IRON_SCRAP.get())
                .requires(DAAItems.IRON_SCRAP.get())
                .requires(DAAItems.IRON_SCRAP.get())
                .requires(DAAItems.IRON_SCRAP.get())
                .unlockedBy("has_iron_scrap", has(DAAItems.IRON_SCRAP.get()))
                .save(writer, "dustandash:crafting/recycle_iron_scrap");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.REDSTONE)
                .requires(DAAItems.REDSTONE_SCRAP.get())
                .requires(DAAItems.REDSTONE_SCRAP.get())
                .requires(DAAItems.REDSTONE_SCRAP.get())
                .requires(DAAItems.REDSTONE_SCRAP.get())
                .requires(DAAItems.REDSTONE_SCRAP.get())
                .requires(DAAItems.REDSTONE_SCRAP.get())
                .requires(DAAItems.REDSTONE_SCRAP.get())
                .requires(DAAItems.REDSTONE_SCRAP.get())
                .unlockedBy("has_redstone_scrap", has(DAAItems.REDSTONE_SCRAP.get()))
                .save(writer, "dustandash:crafting/recycle_redstone_scrap");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.TITANIUM_INGOT.get())
                .requires(DAAItems.TITANIUM_PLATE_SCRAP.get())
                .requires(DAAItems.TITANIUM_PLATE_SCRAP.get())
                .requires(DAAItems.TITANIUM_PLATE_SCRAP.get())
                .requires(DAAItems.TITANIUM_PLATE_SCRAP.get())
                .requires(DAAItems.TITANIUM_PLATE_SCRAP.get())
                .requires(DAAItems.TITANIUM_PLATE_SCRAP.get())
                .requires(DAAItems.TITANIUM_PLATE_SCRAP.get())
                .requires(DAAItems.TITANIUM_PLATE_SCRAP.get())
                .requires(DAAItems.CRYSTALLIZE_DUST.get())
                .unlockedBy("has_titanium_plate_scrap", has(DAAItems.TITANIUM_PLATE_SCRAP.get()))
                .save(writer, "dustandash:crafting/recycle_titanium_plate_scrap");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.RED_MUSHROOM)
                .pattern("#R#")
                .pattern("DGD")
                .pattern("IPI")
                .define('#', DAAItems.METAMORPHOSE_DUST.get())
                .define('R', Items.RED_DYE)
                .define('D', DAAItems.REPRODUCE_DUST.get())
                .define('G', Items.GRASS)
                .define('I', DAAItems.EARTH_DUST.get())
                .define('P', DAAItems.ABSORB_DUST.get())
                .unlockedBy("has_metamorphose_dust", has(DAAItems.METAMORPHOSE_DUST.get()))
                .save(writer, "dustandash:crafting/red_mushroom");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.REDSTONE_VACUUM_TUBE.get())
                .pattern("C")
                .pattern("B")
                .pattern("A")
                .define('A', DAAItems.ASH_STEEL_INGOT.get())
                .define('B', Items.REDSTONE_TORCH)
                .define('C', DAAItems.GLASS_CONTAINER.get())
                .unlockedBy("has_ash_steel_ingot", has(DAAItems.ASH_STEEL_INGOT.get()))
                .save(writer, "dustandash:crafting/redstone_vacuum_tube");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.REDUCTANT.get(), 2)
                .requires(DAAItems.WOOD_ASH.get())
                .requires(DAAItems.SODIUM_DUST.get())
                .requires(DAAItems.SODIUM_DUST.get())
                .requires(Items.FERMENTED_SPIDER_EYE)
                .unlockedBy("has_wood_ash", has(DAAItems.WOOD_ASH.get()))
                .save(writer, "dustandash:crafting/reductant");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.ROCK_SOLID.get())
                .pattern(" AE")
                .pattern("ADA")
                .pattern("BAC")
                .define('A', Items.CUT_COPPER)
                .define('B', DAAItems.EARTH_GEM.get())
                .define('C', DAAItems.METAL_GEM.get())
                .define('D', Items.GOLDEN_APPLE)
                .define('E', Items.STRING)
                .unlockedBy("has_earth_gem", has(DAAItems.EARTH_GEM.get()))
                .save(writer, "dustandash:crafting/rock_solid");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.SEA_PICKLE)
                .pattern("#T#")
                .pattern("DSA")
                .pattern("KMK")
                .define('#', DAAItems.LIFE_DUST.get())
                .define('T', Items.TORCH)
                .define('D', DAAItems.REPRODUCE_DUST.get())
                .define('S', Items.SUGAR_CANE)
                .define('A', DAAItems.ABSORB_DUST.get())
                .define('K', Items.KELP)
                .define('M', DAAItems.INHERIT_DUST.get())
                .unlockedBy("has_life_dust", has(DAAItems.LIFE_DUST.get()))
                .save(writer, "dustandash:crafting/sea_pickle");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.SHARPEN_FLINT.get(), 2)
                .requires(Items.GRAVEL)
                .requires(Items.FLINT)
                .unlockedBy("has_flint", has(Items.FLINT))
                .save(writer, "dustandash:crafting/sharpen_flint");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.SMOOTH_COBBLESTONE.get(), 4)
                .pattern("AB")
                .pattern("BA")
                .define('A', Items.STONE)
                .define('B', Items.COBBLESTONE)
                .unlockedBy("has_cobblestone", has(Items.COBBLESTONE))
                .save(writer, "dustandash:crafting/smooth_cobblestone");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SOUL_SAND, 3)
                .requires(FORGE_SAND)
                .requires(DAAItems.METAMORPHOSE_DUST.get())
                .requires(DAAItems.SMELT_DUST.get())
                .requires(FORGE_SAND)
                .unlockedBy("has_metamorphose_dust", has(DAAItems.METAMORPHOSE_DUST.get()))
                .save(writer, "dustandash:crafting/soul_sand");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.SPRUCE_SAPLING)
                .pattern(" # ")
                .pattern("#O#")
                .pattern(" # ")
                .define('#', DAAItems.METAMORPHOSE_DUST.get())
                .define('O', Items.OAK_SAPLING)
                .unlockedBy("has_metamorphose_dust", has(DAAItems.METAMORPHOSE_DUST.get()))
                .save(writer, "dustandash:crafting/sp1");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.BIRCH_SAPLING)
                .pattern(" # ")
                .pattern("#S#")
                .pattern(" # ")
                .define('#', DAAItems.METAMORPHOSE_DUST.get())
                .define('S', Items.SPRUCE_SAPLING)
                .unlockedBy("has_metamorphose_dust", has(DAAItems.METAMORPHOSE_DUST.get()))
                .save(writer, "dustandash:crafting/sp2");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.JUNGLE_SAPLING)
                .pattern(" # ")
                .pattern("#B#")
                .pattern(" # ")
                .define('#', DAAItems.METAMORPHOSE_DUST.get())
                .define('B', Items.BIRCH_SAPLING)
                .unlockedBy("has_metamorphose_dust", has(DAAItems.METAMORPHOSE_DUST.get()))
                .save(writer, "dustandash:crafting/sp3");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.ACACIA_SAPLING)
                .pattern(" # ")
                .pattern("#J#")
                .pattern(" # ")
                .define('#', DAAItems.METAMORPHOSE_DUST.get())
                .define('J', Items.JUNGLE_SAPLING)
                .unlockedBy("has_metamorphose_dust", has(DAAItems.METAMORPHOSE_DUST.get()))
                .save(writer, "dustandash:crafting/sp4");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.DARK_OAK_SAPLING)
                .pattern(" # ")
                .pattern("#A#")
                .pattern(" # ")
                .define('#', DAAItems.METAMORPHOSE_DUST.get())
                .define('A', Items.ACACIA_SAPLING)
                .unlockedBy("has_metamorphose_dust", has(DAAItems.METAMORPHOSE_DUST.get()))
                .save(writer, "dustandash:crafting/sp5");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.OAK_SAPLING)
                .pattern(" # ")
                .pattern("#D#")
                .pattern(" # ")
                .define('#', DAAItems.METAMORPHOSE_DUST.get())
                .define('D', Items.DARK_OAK_SAPLING)
                .unlockedBy("has_metamorphose_dust", has(DAAItems.METAMORPHOSE_DUST.get()))
                .save(writer, "dustandash:crafting/sp6");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.STRENGTHENED_CEMENT.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', FORGE_INGOTS_LEAD)
                .define('B', Items.GRAY_CONCRETE_POWDER)
                .define('C', DAAItems.ASH_STEEL_HEAVY_PLATE.get())
                .unlockedBy("has_ash_steel_heavy_plate", has(DAAItems.ASH_STEEL_HEAVY_PLATE.get()))
                .save(writer, "dustandash:crafting/strengthened_cement");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.SUN_CRYSTAL.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', DAAItems.ORDER_DUST.get())
                .define('B', Items.COAL)
                .unlockedBy("has_order_dust", has(DAAItems.ORDER_DUST.get()))
                .save(writer, "dustandash:crafting/sun_crystal");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.SWEET_BERRIES)
                .pattern("#R#")
                .pattern("SDS")
                .pattern("UWU")
                .define('#', DAAItems.REPRODUCE_DUST.get())
                .define('R', Items.RED_DYE)
                .define('S', Items.SUGAR)
                .define('D', DAAItems.METAMORPHOSE_DUST.get())
                .define('U', DAAItems.ABSORB_DUST.get())
                .define('W', Items.SPRUCE_LEAVES)
                .unlockedBy("has_reproduce_dust", has(DAAItems.REPRODUCE_DUST.get()))
                .save(writer, "dustandash:crafting/sweet_berries");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.TITANIUM_ALLOY_PICKAXE.get())
                .pattern("ABC")
                .pattern(" D ")
                .pattern(" E ")
                .define('A', DAAItems.TITANIUM_TUNGSTEN_ALLOY.get())
                .define('B', DAAItems.TITANIUM_ALUMINUM_ALLOY.get())
                .define('C', FORGE_INGOTS_TITANIUM)
                .define('D', DAAItems.CARBON_FIBER.get())
                .define('E', DAAItems.GRAPHITE_ELECTRODE.get())
                .unlockedBy("has_titanium_tungsten_alloy", has(DAAItems.TITANIUM_TUNGSTEN_ALLOY.get()))
                .save(writer, "dustandash:crafting/titanium_alloy_pickaxe");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.TITANIUM_CHUNK.get())
                .requires(DAAItems.TITANIUM_SCRAP.get())
                .requires(DAAItems.TITANIUM_SCRAP.get())
                .requires(DAAItems.TITANIUM_SCRAP.get())
                .unlockedBy("has_titanium_scrap", has(DAAItems.TITANIUM_SCRAP.get()))
                .save(writer, "dustandash:crafting/titanium_chunk");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.TITANIUM_HEAVY_PLATE.get())
                .pattern("#A#")
                .pattern("A#A")
                .pattern("#A#")
                .define('#', DAAItems.CRYSTALLIZE_DUST.get())
                .define('A', FORGE_INGOTS_TITANIUM)
                .unlockedBy("has_crystallize_dust", has(DAAItems.CRYSTALLIZE_DUST.get()))
                .save(writer, "dustandash:crafting/titanium_heavy_plate");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.TUBE_CORAL_BLOCK)
                .pattern("#D#")
                .pattern("UBG")
                .pattern("#JH")
                .define('#', DAAItems.ORDER_DUST.get())
                .define('D', DAAItems.ABSORB_DUST.get())
                .define('U', DAAItems.INHERIT_DUST.get())
                .define('B', Items.BLUE_DYE)
                .define('G', DAAItems.METAMORPHOSE_DUST.get())
                .define('J', DAAItems.REPRODUCE_DUST.get())
                .define('H', DAAItems.LIFE_DUST.get())
                .unlockedBy("has_order_dust", has(DAAItems.ORDER_DUST.get()))
                .save(writer, "dustandash:crafting/tube_coral_block");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.TWISTING_VINES)
                .pattern("#D#")
                .pattern("JVJ")
                .pattern(" C ")
                .define('#', DAAItems.REPRODUCE_DUST.get())
                .define('D', DAAItems.SEEP_DUST.get())
                .define('J', DAAItems.METAMORPHOSE_DUST.get())
                .define('V', Items.VINE)
                .define('C', DAAItems.ABSORB_DUST.get())
                .unlockedBy("has_reproduce_dust", has(DAAItems.REPRODUCE_DUST.get()))
                .save(writer, "dustandash:crafting/twistping_vine");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.U235_FUEL.get())
                .pattern("ABA")
                .pattern("ADA")
                .pattern("CBC")
                .define('A', DAAItems.REFINED_URANIUM.get())
                .define('B', DAAItems.METAMORPHOSE_DUST.get())
                .define('C', DAAItems.ELECTRON.get())
                .define('D', DAAItems.EMPTY_FUEL_CONTAINER.get())
                .unlockedBy("has_refined_uranium", has(DAAItems.REFINED_URANIUM.get()))
                .save(writer, "dustandash:crafting/u235_fuel");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.WATER_BUCKET)
                .requires(Items.BUCKET)
                .requires(DAAItems.RAIN_CRYSTAL.get())
                .unlockedBy("has_rain_crystal", has(DAAItems.RAIN_CRYSTAL.get()))
                .save(writer, "dustandash:crafting/water_bucket");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.WATER_MISCIBLE_SOLVENTS.get(), 2)
                .requires(DAAItems.GLASS_CONTAINER.get())
                .requires(DAAItems.WOOD_ASH.get())
                .requires(Items.GUNPOWDER)
                .requires(RecipeIngredients.nbt(Items.POTION, "{Potion:'minecraft:weakness'}"))
                .unlockedBy("has_glass_container", has(DAAItems.GLASS_CONTAINER.get()))
                .save(writer, "dustandash:crafting/water_miscible_solvents");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.WEEPING_VINES)
                .pattern(" # ")
                .pattern("DVD")
                .pattern("OIO")
                .define('#', DAAItems.ABSORB_DUST.get())
                .define('D', DAAItems.METAMORPHOSE_DUST.get())
                .define('V', Items.VINE)
                .define('O', DAAItems.REPRODUCE_DUST.get())
                .define('I', DAAItems.SEEP_DUST.get())
                .unlockedBy("has_absorb_dust", has(DAAItems.ABSORB_DUST.get()))
                .save(writer, "dustandash:crafting/weeping_vine");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.WOODEN_TOOL_HANDLE.get(), 2)
                .pattern("#/D")
                .pattern("/H/")
                .pattern("F/#")
                .define('#', Items.STRING)
                .define('/', FORGE_RODS_WOODEN)
                .define('D', DAAItems.ABSORB_DUST.get())
                .define('H', DAAItems.ASH_STEEL_INGOT.get())
                .define('F', DAAItems.PURE_ENERGY.get())
                .unlockedBy("has_ash_steel_ingot", has(DAAItems.ASH_STEEL_INGOT.get()))
                .save(writer, "dustandash:crafting/wooden_tool_handle");
    }
}
