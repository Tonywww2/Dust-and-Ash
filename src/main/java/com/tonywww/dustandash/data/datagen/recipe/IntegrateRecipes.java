package com.tonywww.dustandash.data.datagen.recipe;

import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAAItems;
import com.tonywww.dustandash.tag.ModTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

/** Converts data/dustandash/recipes/integrate/*.json (37 files) using {@link IntegrateRecipeBuilder}. */
public final class IntegrateRecipes {
    private IntegrateRecipes() {
    }

    public static void buildRecipes(Consumer<FinishedRecipe> writer) {
        IntegrateRecipeBuilder.integrate(DAAItems.ABSORB_DUST.get())
                .level(1)
                .ingredient(Items.BOWL)
                .ingredient(DAAItems.LIFE_DUST.get())
                .ingredient(DAAItems.LIFE_DUST.get())
                .ingredient(DAAItems.EARTH_DUST.get())
                .save(writer, "integrate/absorb_dust");

        IntegrateRecipeBuilder.integrate(DAAItems.ALUMINUM_INGOT.get())
                .level(3)
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.CRYSTALLIZE_DUST.get())
                .ingredient(DAAItems.LACERATE_DUST.get())
                .ingredient(DAAItems.INHERIT_DUST.get())
                .save(writer, "integrate/aluminum_ingot");

        IntegrateRecipeBuilder.integrate(Items.BLAZE_ROD)
                .level(2)
                .ingredient(Items.BLAZE_POWDER)
                .ingredient(Items.BLAZE_POWDER)
                .ingredient(Items.BLAZE_POWDER)
                .ingredient(Items.BLAZE_POWDER)
                .save(writer, "integrate/blaze_rod");

        IntegrateRecipeBuilder.integrate(DAAItems.BLOCKING_DUST.get())
                .level(1)
                .ingredient(Items.BOWL)
                .ingredient(DAAItems.EARTH_DUST.get())
                .ingredient(DAAItems.EARTH_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .save(writer, "integrate/blocking_dust");

        IntegrateRecipeBuilder.integrate(DAAItems.CARBON_FIBER_PLATE.get())
                .level(2)
                .ingredient(Items.PAPER)
                .ingredient(Items.SLIME_BALL)
                .ingredient(DAAItems.CARBON_FIBER.get())
                .ingredient(DAAItems.CARBON_FIBER.get())
                .ingredient(DAAItems.CARBON_FIBER.get())
                .ingredient(DAAItems.CARBON_FIBER.get())
                .ingredient(DAAItems.CARBON_FIBER.get())
                .ingredient(DAAItems.CARBON_FIBER.get())
                .save(writer, "integrate/carbon_fiber_plate");

        IntegrateRecipeBuilder.integrate(Blocks.CLAY)
                .level(0)
                .ingredient(DAAItems.EARTH_DUST.get())
                .ingredient(DAAItems.EARTH_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .save(writer, "integrate/clay");

        IntegrateRecipeBuilder.integrate(Items.COAL, 2)
                .level(1)
                .ingredient(DAAItems.CRYSTALLIZE_DUST.get())
                .ingredient(DAAItems.EARTH_DUST.get())
                .ingredient(Items.CHARCOAL)
                .ingredient(Items.CHARCOAL)
                .ingredient(Items.CHARCOAL)
                .save(writer, "integrate/coal");

        IntegrateRecipeBuilder.integrate(DAAItems.COPPER_INGOT.get())
                .level(1)
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.EARTH_DUST.get())
                .save(writer, "integrate/copper_ingot");

        IntegrateRecipeBuilder.integrate(DAAItems.CRYSTALLIZE_DUST.get())
                .level(1)
                .ingredient(Items.BOWL)
                .ingredient(DAAItems.EARTH_DUST.get())
                .ingredient(DAAItems.EARTH_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .save(writer, "integrate/crystallize_dust");

        IntegrateRecipeBuilder.integrate(Items.DIAMOND)
                .level(2)
                .ingredient(DAAItems.CARBON_DUST.get())
                .ingredient(DAAItems.CARBON_DUST.get())
                .ingredient(DAAItems.CARBON_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.CARBON_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.CARBON_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .save(writer, "integrate/diamond");

        IntegrateRecipeBuilder.integrate(Blocks.DIRT)
                .level(0)
                .ingredient(DAAItems.EARTH_DUST.get())
                .ingredient(DAAItems.EARTH_DUST.get())
                .ingredient(DAAItems.EARTH_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .save(writer, "integrate/dirt");

        IntegrateRecipeBuilder.integrate(DAABlocks.DUST.get(), 5)
                .level(2)
                .ingredient(DAAItems.BOWL_WITH_DUST.get())
                .ingredient(DAAItems.DUST_WITH_ENERGY.get())
                .ingredient(ModTags.Items.ASH)
                .ingredient(ModTags.Items.ASH)
                .ingredient(ModTags.Items.ASH)
                .ingredient(ModTags.Items.ASH)
                .ingredient(ModTags.Items.ASH)
                .ingredient(ModTags.Items.ASH)
                .save(writer, "integrate/dust_block");

        IntegrateRecipeBuilder.integrate(DAAItems.DUST_WITH_ENERGY.get(), 12)
                .level(3)
                .ingredient(DAAItems.EMPTY.get())
                .ingredient(DAAItems.EMPTY.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.FIRE_DUST.get())
                .ingredient(DAAItems.FIRE_DUST.get())
                .ingredient(DAAItems.FIRE_DUST.get())
                .save(writer, "integrate/dust_with_energy");

        IntegrateRecipeBuilder.integrate(DAAItems.EXTINGUISH_DUST.get())
                .level(1)
                .ingredient(Items.BOWL)
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.FIRE_DUST.get())
                .save(writer, "integrate/extinguish_dust");

        IntegrateRecipeBuilder.integrate(Items.GLOWSTONE_DUST)
                .level(2)
                .ingredient(Items.REDSTONE)
                .ingredient(Items.REDSTONE)
                .ingredient(DAAItems.METAMORPHOSE_DUST.get())
                .save(writer, "integrate/glowstone_dust");

        IntegrateRecipeBuilder.integrate(Items.GOLD_NUGGET, 8)
                .level(0)
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .save(writer, "integrate/gold");

        IntegrateRecipeBuilder.integrate(DAAItems.INHERIT_DUST.get())
                .level(1)
                .ingredient(Items.BOWL)
                .ingredient(DAAItems.LIFE_DUST.get())
                .ingredient(DAAItems.LIFE_DUST.get())
                .ingredient(DAAItems.FIRE_DUST.get())
                .save(writer, "integrate/inherit_dust");

        IntegrateRecipeBuilder.integrate(Items.IRON_INGOT)
                .level(0)
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .save(writer, "integrate/iron");

        IntegrateRecipeBuilder.integrate(DAAItems.LACERATE_DUST.get())
                .level(1)
                .ingredient(Items.BOWL)
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.LIFE_DUST.get())
                .save(writer, "integrate/lacerate_dust");

        IntegrateRecipeBuilder.integrate(DAAItems.LEAD_INGOT.get())
                .level(2)
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .save(writer, "integrate/lead_ingot");

        IntegrateRecipeBuilder.integrate(DAAItems.METAMORPHOSE_DUST.get())
                .level(1)
                .ingredient(Items.BOWL)
                .ingredient(DAAItems.FIRE_DUST.get())
                .ingredient(DAAItems.FIRE_DUST.get())
                .ingredient(DAAItems.EARTH_DUST.get())
                .save(writer, "integrate/metamorphose_dust");

        IntegrateRecipeBuilder.integrate(DAAItems.NICKEL_INGOT.get())
                .level(1)
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .save(writer, "integrate/nickel_ingot");

        IntegrateRecipeBuilder.integrate(DAAItems.OSMIUM_INGOT.get())
                .level(3)
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.CRYSTALLIZE_DUST.get())
                .ingredient(DAAItems.EXTINGUISH_DUST.get())
                .ingredient(DAAItems.BLOCKING_DUST.get())
                .save(writer, "integrate/osmium_ingot");

        IntegrateRecipeBuilder.integrate(DAAItems.PURE_ENERGY.get())
                .level(3)
                .ingredient(DAAItems.DUST_WITH_ENERGY.get())
                .ingredient(DAAItems.CARBON_DUST.get())
                .ingredient(DAAItems.ELECTRON.get())
                .ingredient(DAAItems.ELECTRON.get())
                .save(writer, "integrate/pure_energy_lv3");

        IntegrateRecipeBuilder.integrate(DAAItems.PURE_ENERGY.get())
                .level(1)
                .ingredient(DAAItems.DUST_WITH_ENERGY.get())
                .ingredient(DAAItems.ELECTRON.get())
                .ingredient(DAAItems.ELECTRON.get())
                .ingredient(DAAItems.ELECTRON.get())
                .ingredient(DAAItems.ELECTRON.get())
                .ingredient(DAAItems.ELECTRON.get())
                .ingredient(DAAItems.ELECTRON.get())
                .ingredient(DAAItems.ELECTRON.get())
                .save(writer, "integrate/pure_energy");

        IntegrateRecipeBuilder.integrate(DAAItems.RAW_ASH_STEEL.get(), 3)
                .level(1)
                .ingredient(Items.IRON_INGOT)
                .ingredient(Items.IRON_INGOT)
                .ingredient(Items.IRON_INGOT)
                .ingredient(Items.CHARCOAL)
                .ingredient(Items.IRON_INGOT)
                .ingredient(ModTags.Items.ASH)
                .ingredient(Items.IRON_INGOT)
                .ingredient(ModTags.Items.ASH)
                .save(writer, "integrate/raw_ash_steel");

        IntegrateRecipeBuilder.integrate(Items.REDSTONE, 3)
                .level(1)
                .ingredient(DAAItems.FIRE_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(ModTags.Items.ASH)
                .ingredient(DAAItems.DUST_WITH_ENERGY.get())
                .ingredient(ModTags.Items.ASH)
                .save(writer, "integrate/redstone");

        IntegrateRecipeBuilder.integrate(DAAItems.REPRODUCE_DUST.get())
                .level(1)
                .ingredient(Items.BOWL)
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.LIFE_DUST.get())
                .save(writer, "integrate/reproduce_dust");

        IntegrateRecipeBuilder.integrate(DAAItems.SEEP_DUST.get())
                .level(1)
                .ingredient(Items.BOWL)
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .save(writer, "integrate/seep_dust");

        IntegrateRecipeBuilder.integrate(Items.SLIME_BALL, 2)
                .level(1)
                .ingredient(Items.HONEY_BOTTLE)
                .ingredient(ModTags.Items.ASH)
                .ingredient(ModTags.Items.ASH)
                .ingredient(Items.LIME_DYE)
                .ingredient(DAAItems.ABSORB_DUST.get())
                .save(writer, "integrate/slime_ball");

        IntegrateRecipeBuilder.integrate(DAAItems.SILVER_INGOT.get())
                .level(2)
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.FIRE_DUST.get())
                .save(writer, "integrate/sliver_ingot");

        IntegrateRecipeBuilder.integrate(DAAItems.SMELT_DUST.get())
                .level(1)
                .ingredient(Items.BOWL)
                .ingredient(DAAItems.FIRE_DUST.get())
                .ingredient(DAAItems.FIRE_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .save(writer, "integrate/smelt_dust");

        IntegrateRecipeBuilder.integrate(DAAItems.TIN_INGOT.get())
                .level(1)
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .save(writer, "integrate/tin_ingot");

        IntegrateRecipeBuilder.integrate(DAABlocks.TITANIUM_SAND_BLOCK.get())
                .level(2)
                .ingredient(DAAItems.MANTLE_MIXTURE.get())
                .ingredient(Items.SAND)
                .ingredient(Items.GUNPOWDER)
                .ingredient(Items.QUARTZ)
                .save(writer, "integrate/titanium_sand_block");

        IntegrateRecipeBuilder.integrate(DAAItems.TITANIUM_SAND.get(), 2)
                .level(1)
                .ingredient(DAAItems.REDUCTANT.get())
                .ingredient(DAAItems.REDUCTANT.get())
                .ingredient(DAAItems.RAW_TITANIUM_SAND.get())
                .ingredient(DAAItems.CRYSTALLIZE_DUST.get())
                .ingredient(DAAItems.RAW_TITANIUM_SAND.get())
                .save(writer, "integrate/titanium_sand");

        IntegrateRecipeBuilder.integrate(DAAItems.URANIUM_INGOT.get())
                .level(3)
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.ORDER_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAL_DUST.get())
                .ingredient(DAAItems.METAMORPHOSE_DUST.get())
                .ingredient(DAAItems.EXTINGUISH_DUST.get())
                .ingredient(DAAItems.SMELT_DUST.get())
                .save(writer, "integrate/uranium_ingot");

        IntegrateRecipeBuilder.integrate(Items.WITHER_SKELETON_SKULL)
                .level(3)
                .ingredient(DAAItems.SMELT_DUST.get())
                .ingredient(DAAItems.EXTINGUISH_DUST.get())
                .ingredient(Items.SKELETON_SKULL)
                .ingredient(Items.NETHERITE_SCRAP)
                .ingredient(ItemTags.COALS)
                .save(writer, "integrate/wither_skeleton_skull");
    }
}
