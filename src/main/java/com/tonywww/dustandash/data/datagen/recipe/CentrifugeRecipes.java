package com.tonywww.dustandash.data.datagen.recipe;

import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAAItems;
import com.tonywww.dustandash.tag.ModTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

/** Converts data/dustandash/recipes/centrifuge/*.json (25 files) using {@link CentrifugeRecipeBuilder}. */
public final class CentrifugeRecipes {
    private CentrifugeRecipes() {
    }

    public static void buildRecipes(Consumer<FinishedRecipe> writer) {
        CentrifugeRecipeBuilder.centrifuge(200)
                .input(DAAItems.CRUSHED_SEEDS.get())
                .input(DAAItems.GLASS_CONTAINER.get())
                .empty().empty().empty().empty()
                .output(DAAItems.BASE_OIL.get())
                .empty().empty().empty()
                .save(writer, "centrifuge/base_oil");

        CentrifugeRecipeBuilder.centrifuge(200)
                .input(Items.MAGMA_CREAM)
                .input(DAAItems.EMPTY.get())
                .empty()
                .output(Items.BLAZE_POWDER)
                .empty().empty()
                .output(DAAItems.FIRE_DUST.get())
                .empty().empty().empty()
                .save(writer, "centrifuge/blaze_powder");

        CentrifugeRecipeBuilder.centrifuge(400)
                .input(ItemTags.create(new ResourceLocation("forge", "storage_blocks/coal")))
                .input(DAAItems.EMPTY.get())
                .output(DAAItems.EARTH_DUST.get())
                .empty()
                .output(DAAItems.CARBON_DUST.get())
                .output(DAAItems.CARBON_DUST.get())
                .output(DAAItems.CARBON_DUST.get())
                .output(DAAItems.CARBON_DUST.get())
                .empty()
                .output(DAAItems.CARBON_DUST.get())
                .save(writer, "centrifuge/carbon_dust");

        CentrifugeRecipeBuilder.centrifuge(200)
                .input(DAABlocks.DUST.get())
                .input(DAAItems.WATER_MISCIBLE_SOLVENTS.get())
                .output(DAAItems.LIFE_DUST.get())
                .output(DAAItems.LIFE_DUST.get())
                .output(DAAItems.METAL_DUST.get())
                .output(DAAItems.METAL_DUST.get())
                .empty()
                .output(DAAItems.ORDER_DUST.get())
                .output(DAAItems.EARTH_DUST.get())
                .output(DAAItems.FIRE_DUST.get())
                .save(writer, "centrifuge/dust");

        CentrifugeRecipeBuilder.centrifuge(400)
                .input(DAAItems.MAGNET.get())
                .input(DAAItems.EMPTY.get())
                .output(DAAItems.ELECTRON.get())
                .empty()
                .output(DAAItems.ELECTRON.get())
                .output(Items.IRON_NUGGET)
                .output(Items.IRON_NUGGET)
                .output(DAAItems.ELECTRON.get())
                .output(Items.IRON_NUGGET)
                .output(DAAItems.ELECTRON.get())
                .save(writer, "centrifuge/electron");

        CentrifugeRecipeBuilder.centrifuge(200)
                .input(ItemTags.FLOWERS)
                .input(DAAItems.EMPTY.get())
                .empty()
                .output(DAAItems.LIFE_DUST.get())
                .empty().empty()
                .output(DAAItems.LIFE_DUST.get())
                .output(DAAItems.ORDER_DUST.get())
                .empty().empty()
                .save(writer, "centrifuge/flowers");

        CentrifugeRecipeBuilder.centrifuge(200)
                .input(Items.GRAVEL)
                .input(DAAItems.EMPTY.get())
                .empty().empty().empty()
                .output(DAAItems.METAL_DUST.get())
                .empty().empty()
                .output(DAAItems.EARTH_DUST.get())
                .output(DAAItems.EARTH_DUST.get())
                .save(writer, "centrifuge/gravel");

        CentrifugeRecipeBuilder.centrifuge(600)
                .input(Items.BEACON)
                .input(DAAItems.GRIND_SOLVENTS.get())
                .empty()
                .output(Items.SAND)
                .output(Items.HEART_OF_THE_SEA)
                .output(Items.OBSIDIAN)
                .empty()
                .output(DAAItems.DUST_WITH_ENERGY.get())
                .empty()
                .output(Items.SAND)
                .save(writer, "centrifuge/heart_of_the_sea");

        CentrifugeRecipeBuilder.centrifuge(200)
                .input(Items.HONEY_BOTTLE)
                .input(DAAItems.WATER_MISCIBLE_SOLVENTS.get())
                .output(Items.HONEY_BOTTLE)
                .empty().empty()
                .output(Items.HONEY_BOTTLE)
                .output(DAAItems.BASE_OIL.get())
                .output(Items.HONEY_BOTTLE)
                .empty().empty()
                .save(writer, "centrifuge/honey_copy");

        CentrifugeRecipeBuilder.centrifuge(6000)
                .input(DAAItems.PURE_ENERGY.get())
                .input(RecipeIngredients.nbt(Items.POTION, "{Potion:'minecraft:water'}"))
                .output(DAAItems.SOUL_OF_LIGHT.get())
                .empty().empty().empty()
                .empty().empty().empty()
                .output(DAAItems.PURE_DARKNESS.get())
                .save(writer, "centrifuge/light_and_darkness");

        CentrifugeRecipeBuilder.centrifuge(200)
                .input(Items.MAGMA_BLOCK)
                .input(DAAItems.EMPTY.get())
                .empty().empty()
                .output(Items.MAGMA_CREAM)
                .empty()
                .output(DAAItems.FIRE_DUST.get())
                .output(Items.MAGMA_CREAM)
                .empty().empty()
                .save(writer, "centrifuge/magma_cream");

        CentrifugeRecipeBuilder.centrifuge(400)
                .input(Items.BRAIN_CORAL_BLOCK)
                .input(DAAItems.BASE_OIL.get())
                .empty()
                .output(Items.BONE_MEAL)
                .empty()
                .output(Items.NAUTILUS_SHELL)
                .output(Items.BONE_MEAL)
                .output(Items.BONE_MEAL)
                .output(Items.PRISMARINE_SHARD)
                .empty()
                .save(writer, "centrifuge/nautilus_shell");

        CentrifugeRecipeBuilder.centrifuge(1200)
                .input(DAABlocks.NETHERITE_MUD.get())
                .input(DAAItems.WATER_MISCIBLE_SOLVENTS.get())
                .empty()
                .output(Items.GOLD_NUGGET)
                .empty()
                .output(Items.NETHERITE_SCRAP)
                .output(Items.BONE_MEAL)
                .empty()
                .output(Items.COAL)
                .empty()
                .save(writer, "centrifuge/netherite_scrap");

        CentrifugeRecipeBuilder.centrifuge(200)
                .input(Items.TUBE_CORAL_BLOCK)
                .input(DAAItems.BASE_OIL.get())
                .output(Items.STICK)
                .empty().empty()
                .output(Items.PRISMARINE_SHARD)
                .empty()
                .output(DAAItems.ORDER_DUST.get())
                .output(Items.PRISMARINE_SHARD)
                .empty()
                .save(writer, "centrifuge/prismarine_shard");

        CentrifugeRecipeBuilder.centrifuge(200)
                .input(Items.SOUL_SAND)
                .input(DAAItems.BASE_OIL.get())
                .output(DAAItems.EARTH_DUST.get())
                .empty().empty()
                .output(Items.SAND)
                .empty()
                .output(DAAItems.FIRE_DUST.get())
                .empty()
                .output(Items.QUARTZ)
                .save(writer, "centrifuge/quartz");

        CentrifugeRecipeBuilder.centrifuge(600)
                .input(DAABlocks.TITANIUM_SAND_BLOCK.get())
                .input(DAAItems.GRIND_SOLVENTS.get())
                .output(DAAItems.EARTH_DUST.get())
                .empty()
                .output(DAAItems.RAW_TITANIUM_SAND.get())
                .empty()
                .output(Items.SAND)
                .empty().empty()
                .output(DAAItems.RAW_TITANIUM_SAND.get())
                .save(writer, "centrifuge/raw_titanium_sand");

        CentrifugeRecipeBuilder.centrifuge(160)
                .input(Items.TORCH)
                .input(DAAItems.EMPTY.get())
                .output(DAAItems.FIRE_DUST.get())
                .empty().empty().empty()
                .empty().empty().empty().empty()
                .save(writer, "centrifuge/torch");

        CentrifugeRecipeBuilder.centrifuge(6000)
                .input(Items.NETHERITE_INGOT)
                .input(DAAItems.GRIND_SOLVENTS.get())
                .output(DAAItems.FIRE_DUST.get())
                .output(Items.GOLD_NUGGET)
                .output(DAAItems.METAL_DUST.get())
                .output(DAAItems.TUNGSTEN_DUST.get())
                .output(Items.GOLD_NUGGET)
                .output(DAAItems.FIRE_DUST.get())
                .output(DAAItems.EARTH_DUST.get())
                .output(DAAItems.METAL_DUST.get())
                .save(writer, "centrifuge/tungsten_dust");

        CentrifugeRecipeBuilder.centrifuge(400)
                .input(RecipeIngredients.nbt(DAAItems.U235_FUEL.get(), "{Damage:0}"))
                .input(DAAItems.EARTH_GEM.get())
                .output(DAAItems.U235_FUEL_EARTH.get())
                .output(DAAItems.EARTH_DUST.get())
                .empty().empty()
                .empty().empty().empty().empty()
                .save(writer, "centrifuge/u235_earth");

        CentrifugeRecipeBuilder.centrifuge(400)
                .input(RecipeIngredients.nbt(DAAItems.U235_FUEL.get(), "{Damage:0}"))
                .input(DAAItems.FIRE_GEM.get())
                .output(DAAItems.U235_FUEL_FIRE.get())
                .output(DAAItems.FIRE_DUST.get())
                .empty().empty()
                .empty().empty().empty().empty()
                .save(writer, "centrifuge/u235_fire");

        CentrifugeRecipeBuilder.centrifuge(400)
                .input(RecipeIngredients.nbt(DAAItems.U235_FUEL.get(), "{Damage:0}"))
                .input(DAAItems.LIFE_GEM.get())
                .output(DAAItems.U235_FUEL_LIFE.get())
                .output(DAAItems.LIFE_DUST.get())
                .empty().empty()
                .empty().empty().empty().empty()
                .save(writer, "centrifuge/u235_life");

        CentrifugeRecipeBuilder.centrifuge(400)
                .input(RecipeIngredients.nbt(DAAItems.U235_FUEL.get(), "{Damage:0}"))
                .input(DAAItems.METAL_GEM.get())
                .output(DAAItems.U235_FUEL_METAL.get())
                .output(DAAItems.METAL_DUST.get())
                .empty().empty()
                .empty().empty().empty().empty()
                .save(writer, "centrifuge/u235_metal");

        CentrifugeRecipeBuilder.centrifuge(400)
                .input(RecipeIngredients.nbt(DAAItems.U235_FUEL.get(), "{Damage:0}"))
                .input(DAAItems.ORDER_GEM.get())
                .output(DAAItems.U235_FUEL_ORDER.get())
                .output(DAAItems.ORDER_DUST.get())
                .empty().empty()
                .empty().empty().empty().empty()
                .save(writer, "centrifuge/u235_order");

        CentrifugeRecipeBuilder.centrifuge(160)
                .input(ItemTags.create(new ResourceLocation("forge", "ingots/uranium")))
                .input(DAAItems.GLASS_CONTAINER.get())
                .output(DAAItems.METAL_DUST.get())
                .output(DAAItems.REFINED_URANIUM.get())
                .empty().empty().empty()
                .output(DAAItems.METAL_DUST.get())
                .empty().empty()
                .save(writer, "centrifuge/uranium");

        CentrifugeRecipeBuilder.centrifuge(100)
                .input(ModTags.Items.ASH)
                .input(DAAItems.GLASS_CONTAINER.get())
                .empty().empty()
                .output(Items.LIGHT_GRAY_DYE)
                .output(DAAItems.WOOD_ASH.get())
                .empty().empty().empty().empty()
                .save(writer, "centrifuge/wood_ash");
    }
}
