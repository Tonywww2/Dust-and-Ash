package com.tonywww.dustandash.data.datagen.recipe;

import com.tonywww.dustandash.registry.DAAItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

/** Converts the first half of data/dustandash/recipes/milling/*.json using {@link MillingRecipeBuilder}. */
public final class MillingRecipesA {
    private MillingRecipesA() {
    }

    public static void buildRecipes(Consumer<FinishedRecipe> writer) {
        MillingRecipeBuilder.milling(DAAItems.APOTHEOSIS_CRYSTAL.get())
                .pattern("ACBDA")
                .pattern("EKHIF")
                .pattern("BHJHB")
                .pattern("FIHGE")
                .pattern("ADBCA")
                .define('A', Items.PRISMARINE_SHARD)
                .define('B', Items.EXPERIENCE_BOTTLE)
                .define('C', DAAItems.EXTINGUISH_DUST.get())
                .define('D', DAAItems.ABSORB_DUST.get())
                .define('E', DAAItems.LACERATE_DUST.get())
                .define('F', DAAItems.BLOCKING_DUST.get())
                .define('G', DAAItems.SOUL_OF_LIGHT.get())
                .define('H', DAAItems.TITANIUM_PLATE_SCRAP.get())
                .define('I', DAAItems.GRAPHITE_ELECTRODE.get())
                .define('J', Items.NETHER_STAR)
                .define('K', DAAItems.PURE_DARKNESS.get())
                .catalyst(DAAItems.SEEP_DUST.get())
                .save(writer, "milling/apotheosis_crystal");

        MillingRecipeBuilder.milling(DAAItems.ASH_STEEL_AXE.get())
                .pattern("AA##B")
                .pattern("A###B")
                .pattern("#####")
                .pattern("####B")
                .pattern("CD#CC")
                .define('#', DAAItems.ASH_STEEL_SCRAP.get())
                .define('A', DAAItems.LACERATE_DUST.get())
                .define('B', DAAItems.CRYSTALLIZE_DUST.get())
                .define('C', DAAItems.SMELT_DUST.get())
                .define('D', DAAItems.ABSORB_DUST.get())
                .catalyst(DAAItems.WOODEN_TOOL_HANDLE.get())
                .save(writer, "milling/ash_steel_axe");

        MillingRecipeBuilder.milling(DAAItems.ASH_STEEL_BOOTS.get())
                .pattern("AC BA")
                .pattern("## ##")
                .pattern("## ##")
                .pattern("## ##")
                .pattern("## ##")
                .define('#', DAAItems.ASH_STEEL_SCRAP.get())
                .define('A', Items.STRING)
                .define('B', DAAItems.CRYSTALLIZE_DUST.get())
                .define('C', DAAItems.BLOCKING_DUST.get())
                .catalyst(DAAItems.LACERATE_DUST.get())
                .save(writer, "milling/ash_steel_boots");

        MillingRecipeBuilder.milling(DAAItems.ASH_STEEL_CHESTPLATE.get())
                .pattern("## ##")
                .pattern("#####")
                .pattern("#####")
                .pattern("#####")
                .pattern("C###B")
                .define('#', DAAItems.ASH_STEEL_SCRAP.get())
                .define('A', Items.STRING)
                .define('B', DAAItems.CRYSTALLIZE_DUST.get())
                .define('C', DAAItems.BLOCKING_DUST.get())
                .catalyst(DAAItems.LACERATE_DUST.get())
                .save(writer, "milling/ash_steel_chestplate");

        MillingRecipeBuilder.milling(DAAItems.ASH_STEEL_CYLINDER.get(), 3)
                .pattern("##A##")
                .pattern("# # #")
                .pattern("#   #")
                .pattern("# # #")
                .pattern("#####")
                .define('#', DAAItems.ASH_STEEL_SCRAP.get())
                .define('A', Items.TRIPWIRE_HOOK)
                .catalyst(DAAItems.BLOCKING_DUST.get())
                .save(writer, "milling/ash_steel_cylinder");

        MillingRecipeBuilder.milling(DAAItems.ASH_STEEL_GEAR.get())
                .pattern("# # #")
                .pattern(" ### ")
                .pattern("## ##")
                .pattern(" ### ")
                .pattern("# # #")
                .define('#', DAAItems.ASH_STEEL_SCRAP.get())
                .catalyst(DAAItems.LACERATE_DUST.get())
                .save(writer, "milling/ash_steel_gear");

        MillingRecipeBuilder.milling(DAAItems.ASH_STEEL_HELMET.get())
                .pattern("#####")
                .pattern("#####")
                .pattern("##B##")
                .pattern("##A##")
                .pattern("CCACC")
                .define('#', DAAItems.ASH_STEEL_SCRAP.get())
                .define('A', Items.STRING)
                .define('B', DAAItems.CRYSTALLIZE_DUST.get())
                .define('C', DAAItems.BLOCKING_DUST.get())
                .catalyst(DAAItems.LACERATE_DUST.get())
                .save(writer, "milling/ash_steel_helmet");

        MillingRecipeBuilder.milling(DAAItems.ASH_STEEL_HOE.get())
                .pattern("###CC")
                .pattern("####C")
                .pattern("AE###")
                .pattern(" ED##")
                .pattern("  BBB")
                .define('#', DAAItems.ASH_STEEL_SCRAP.get())
                .define('A', DAAItems.LACERATE_DUST.get())
                .define('B', DAAItems.CRYSTALLIZE_DUST.get())
                .define('C', DAAItems.SMELT_DUST.get())
                .define('D', DAAItems.ABSORB_DUST.get())
                .define('E', DAAItems.ORDER_DUST.get())
                .catalyst(DAAItems.WOODEN_TOOL_HANDLE.get())
                .save(writer, "milling/ash_steel_hoe");

        MillingRecipeBuilder.milling(DAAItems.ASH_STEEL_LEGGING.get())
                .pattern("#####")
                .pattern("#####")
                .pattern("##A##")
                .pattern("##C##")
                .pattern("##B##")
                .define('#', DAAItems.ASH_STEEL_SCRAP.get())
                .define('A', Items.STRING)
                .define('B', DAAItems.CRYSTALLIZE_DUST.get())
                .define('C', DAAItems.BLOCKING_DUST.get())
                .catalyst(DAAItems.LACERATE_DUST.get())
                .save(writer, "milling/ash_steel_leggings");

        MillingRecipeBuilder.milling(DAAItems.ASH_STEEL_LEVER.get(), 4)
                .pattern("     ")
                .pattern("#####")
                .pattern("A   A")
                .pattern("#####")
                .pattern("     ")
                .define('#', DAAItems.ASH_STEEL_SCRAP.get())
                .define('A', DAAItems.ABSORB_DUST.get())
                .catalyst(DAAItems.INHERIT_DUST.get())
                .save(writer, "milling/ash_steel_lever");

        MillingRecipeBuilder.milling(DAAItems.ASH_STEEL_PICKAXE.get())
                .pattern("###CC")
                .pattern("####C")
                .pattern("AB###")
                .pattern(" DB##")
                .pattern("B A##")
                .define('#', DAAItems.ASH_STEEL_SCRAP.get())
                .define('A', DAAItems.LACERATE_DUST.get())
                .define('B', DAAItems.CRYSTALLIZE_DUST.get())
                .define('C', DAAItems.SMELT_DUST.get())
                .define('D', DAAItems.ABSORB_DUST.get())
                .catalyst(DAAItems.WOODEN_TOOL_HANDLE.get())
                .save(writer, "milling/ash_steel_pickaxe");

        MillingRecipeBuilder.milling(DAAItems.ASH_STEEL_SHOVEL.get())
                .pattern("CC#EA")
                .pattern("C###E")
                .pattern("#####")
                .pattern("####B")
                .pattern("D##BB")
                .define('#', DAAItems.ASH_STEEL_SCRAP.get())
                .define('A', DAAItems.LACERATE_DUST.get())
                .define('B', DAAItems.CRYSTALLIZE_DUST.get())
                .define('C', DAAItems.SMELT_DUST.get())
                .define('D', DAAItems.ABSORB_DUST.get())
                .define('E', DAAItems.ORDER_DUST.get())
                .catalyst(DAAItems.WOODEN_TOOL_HANDLE.get())
                .save(writer, "milling/ash_steel_shovel");

        MillingRecipeBuilder.milling(DAAItems.ASH_STEEL_STRUCTURAL_COMPONENTS.get())
                .pattern("#####")
                .pattern("#BCB#")
                .pattern("ACDCA")
                .pattern("#BCB#")
                .pattern("##A##")
                .define('#', DAAItems.ASH_STEEL_SCRAP.get())
                .define('A', Items.IRON_BARS)
                .define('B', Items.CHAIN)
                .define('C', DAAItems.DUST_WITH_ENERGY.get())
                .define('D', DAAItems.ASH_STEEL_GEAR.get())
                .catalyst(DAAItems.INHERIT_DUST.get())
                .save(writer, "milling/ash_steel_structural_components");

        MillingRecipeBuilder.milling(DAAItems.ASH_STEEL_SWORD.get())
                .pattern("CEC##")
                .pattern("EA###")
                .pattern("C###B")
                .pattern("###AE")
                .pattern("D#BEB")
                .define('#', DAAItems.ASH_STEEL_SCRAP.get())
                .define('A', DAAItems.LACERATE_DUST.get())
                .define('B', DAAItems.CRYSTALLIZE_DUST.get())
                .define('C', DAAItems.SMELT_DUST.get())
                .define('D', DAAItems.ABSORB_DUST.get())
                .define('E', DAAItems.ORDER_DUST.get())
                .catalyst(DAAItems.WOODEN_TOOL_HANDLE.get())
                .save(writer, "milling/ash_steel_sword");

        MillingRecipeBuilder.milling(DAAItems.ASH_STEEL_SCRAP.get())
                .step1(DAAItems.ASH_STEEL_HEAVY_PLATE.get())
                .save(writer, "milling/base_ash_steel");

        MillingRecipeBuilder.milling(DAAItems.CARBON_FIBER_SCRAP.get())
                .step1(DAAItems.CARBON_FIBER_PLATE.get())
                .save(writer, "milling/base_carbon_fiber");

        MillingRecipeBuilder.milling(DAAItems.IRON_SCRAP.get())
                .step1(Items.IRON_BLOCK)
                .save(writer, "milling/base_iron");

        MillingRecipeBuilder.milling(DAAItems.REDSTONE_SCRAP.get())
                .step1(Items.REDSTONE_BLOCK)
                .save(writer, "milling/base_redstone");
    }
}
