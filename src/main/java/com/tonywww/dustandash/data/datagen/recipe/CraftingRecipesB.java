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

/** Converts part 2/3 of data/dustandash/recipes/crafting/*.json (minecraft:crafting_shaped/crafting_shapeless). */
public final class CraftingRecipesB {
    private static final TagKey<Item> FORGE_INGOTS_IRON = ItemTags.create(new ResourceLocation("forge", "ingots/iron"));
    private static final TagKey<Item> FORGE_INGOTS_NICKEL = ItemTags.create(new ResourceLocation("forge", "ingots/nickel"));
    private static final TagKey<Item> FORGE_GLASS = ItemTags.create(new ResourceLocation("forge", "glass"));
    private static final TagKey<Item> FORGE_GEMS = ItemTags.create(new ResourceLocation("forge", "gems"));
    private static final TagKey<Item> FORGE_RODS = ItemTags.create(new ResourceLocation("forge", "rods"));
    private static final TagKey<Item> FORGE_RODS_WOODEN = ItemTags.create(new ResourceLocation("forge", "rods/wooden"));
    private static final TagKey<Item> FORGE_COBBLESTONE = ItemTags.create(new ResourceLocation("forge", "cobblestone"));
    private static final TagKey<Item> FORGE_MUSHROOMS = ItemTags.create(new ResourceLocation("forge", "mushrooms"));
    private static final TagKey<Item> FORGE_STONE = ItemTags.create(new ResourceLocation("forge", "stone"));

    private CraftingRecipesB() {
    }

    public static void buildRecipes(Consumer<FinishedRecipe> writer) {
        // horn_coral_block.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.HORN_CORAL_BLOCK)
                .pattern("#D#")
                .pattern("OYC")
                .pattern("#NQ")
                .define('#', DAAItems.ORDER_DUST.get())
                .define('D', DAAItems.ABSORB_DUST.get())
                .define('O', DAAItems.INHERIT_DUST.get())
                .define('Y', Items.YELLOW_DYE)
                .define('C', DAAItems.METAMORPHOSE_DUST.get())
                .define('N', DAAItems.REPRODUCE_DUST.get())
                .define('Q', DAAItems.LIFE_DUST.get())
                .unlockedBy("has_life_dust", has(DAAItems.LIFE_DUST.get()))
                .save(writer, "dustandash:crafting/horn_coral_block");

        // integrated_block.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.INTEGRATED_BLOCK.get())
                .pattern("ABA")
                .pattern("CDC")
                .pattern("CEC")
                .define('A', FORGE_INGOTS_IRON)
                .define('B', DAAItems.BOWL_WITH_DUST.get())
                .define('C', Items.STONE_BRICKS)
                .define('D', Items.LAVA_BUCKET)
                .define('E', FORGE_GLASS)
                .unlockedBy("has_bowl_with_dust", has(DAAItems.BOWL_WITH_DUST.get()))
                .save(writer, "dustandash:crafting/integrated_block");

        // integrated_frame_1.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.INTEGRATED_FRAME_1.get())
                .pattern("#B#")
                .pattern("#C#")
                .pattern("A#A")
                .define('#', Items.IRON_BARS)
                .define('A', Items.BRICKS)
                .define('B', Items.WATER_BUCKET)
                .define('C', Items.LAVA_BUCKET)
                .unlockedBy("has_iron_bars", has(Items.IRON_BARS))
                .save(writer, "dustandash:crafting/integrated_frame_1");

        // integrated_frame_2.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.INTEGRATED_FRAME_2.get())
                .pattern("EBE")
                .pattern("#C#")
                .pattern("ADA")
                .define('#', DAABlocks.INTEGRATED_FRAME_1.get())
                .define('A', Items.MAGMA_BLOCK)
                .define('B', Items.BOOK)
                .define('C', DAAItems.BASIC_MACHINE_FRAME.get())
                .define('D', Items.SOUL_SAND)
                .define('E', DAAItems.INHERIT_DUST.get())
                .unlockedBy("has_integrated_frame_1", has(DAABlocks.INTEGRATED_FRAME_1.get()))
                .save(writer, "dustandash:crafting/integrated_frame_2");

        // integrated_frame_3.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.INTEGRATED_FRAME_3.get(), 2)
                .pattern("#B#")
                .pattern("ACA")
                .pattern("#B#")
                .define('#', DAAItems.CARBON_FIBER.get())
                .define('A', DAABlocks.INTEGRATED_FRAME_2.get())
                .define('B', Items.NETHERITE_INGOT)
                .define('C', DAAItems.DARK_ENERGY_COLLAPSER.get())
                .unlockedBy("has_integrated_frame_2", has(DAABlocks.INTEGRATED_FRAME_2.get()))
                .save(writer, "dustandash:crafting/integrated_frame_3");

        // ionizer.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.IONIZER.get())
                .pattern(" CA")
                .pattern("ADA")
                .pattern("BEB")
                .define('A', DAAItems.ASH_STEEL_CYLINDER.get())
                .define('B', Items.CHAIN)
                .define('C', DAAItems.REDSTONE_VACUUM_TUBE.get())
                .define('D', DAAItems.DARK_ENERGY_COLLAPSER.get())
                .define('E', Items.SMOOTH_STONE_SLAB)
                .unlockedBy("has_dark_energy_collapser", has(DAAItems.DARK_ENERGY_COLLAPSER.get()))
                .save(writer, "dustandash:crafting/ionizer");

        // iron_nugget.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.IRON_NUGGET, 7)
                .requires(Items.BOWL)
                .requires(ItemTags.COALS)
                .requires(Items.FLINT)
                .requires(DAAItems.METAL_DUST.get())
                .requires(DAAItems.METAL_DUST.get())
                .requires(DAAItems.METAL_DUST.get())
                .requires(DAAItems.METAL_DUST.get())
                .requires(DAAItems.METAL_DUST.get())
                .requires(DAAItems.METAL_DUST.get())
                .unlockedBy("has_metal_dust", has(DAAItems.METAL_DUST.get()))
                .save(writer, "dustandash:crafting/iron_nugget");

        // iron_vacuum.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.IRON_VACUUM.get())
                .pattern("ABE")
                .pattern("ACD")
                .pattern("F G")
                .define('A', Items.CHAIN)
                .define('B', FORGE_RODS)
                .define('C', Items.IRON_BLOCK)
                .define('D', Items.GOLD_INGOT)
                .define('E', DAAItems.DUST_WITH_ENERGY.get())
                .define('F', Items.IRON_TRAPDOOR)
                .define('G', Items.GLASS_BOTTLE)
                .unlockedBy("has_dust_with_energy", has(DAAItems.DUST_WITH_ENERGY.get()))
                .save(writer, "dustandash:crafting/iron_vacuum");

        // item_sender.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.ITEM_SENDER.get(), 2)
                .pattern(" D ")
                .pattern("BCB")
                .pattern("AEA")
                .define('A', DAAItems.ASH_STEEL_CYLINDER.get())
                .define('B', DAAItems.GLASS_CONTAINER.get())
                .define('C', Items.ENDER_PEARL)
                .define('D', Items.LIGHTNING_ROD)
                .define('E', DAAItems.ASH_STEEL_GEAR.get())
                .unlockedBy("has_ash_steel_gear", has(DAAItems.ASH_STEEL_GEAR.get()))
                .save(writer, "dustandash:crafting/item_sender");

        // kelp.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.KELP)
                .requires(Items.SEAGRASS)
                .requires(DAAItems.METAMORPHOSE_DUST.get())
                .requires(DAAItems.REPRODUCE_DUST.get())
                .unlockedBy("has_metamorphose_dust", has(DAAItems.METAMORPHOSE_DUST.get()))
                .save(writer, "dustandash:crafting/kelp");

        // lapis_lazuli.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.LAPIS_LAZULI, 2)
                .requires(Items.BLUE_DYE)
                .requires(DAAItems.CRYSTALLIZE_DUST.get())
                .requires(DAAItems.EARTH_DUST.get())
                .unlockedBy("has_crystallize_dust", has(DAAItems.CRYSTALLIZE_DUST.get()))
                .save(writer, "dustandash:crafting/lapis_lazuli");

        // life_gem.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.LIFE_GEM.get())
                .requires(FORGE_GEMS)
                .requires(DAAItems.BOWL_WITH_DUST.get())
                .requires(DAAItems.LIFE_DUST.get())
                .requires(DAAItems.LIFE_DUST.get())
                .requires(DAAItems.LIFE_DUST.get())
                .requires(DAAItems.LIFE_DUST.get())
                .requires(DAAItems.LIFE_DUST.get())
                .requires(DAAItems.LIFE_DUST.get())
                .requires(DAAItems.LIFE_DUST.get())
                .unlockedBy("has_life_dust", has(DAAItems.LIFE_DUST.get()))
                .save(writer, "dustandash:crafting/life_gem");

        // log_pile.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.LOG_PILE.get(), 2)
                .pattern("ABA")
                .pattern("BAB")
                .pattern("ABA")
                .define('A', FORGE_RODS_WOODEN)
                .define('B', ItemTags.LOGS_THAT_BURN)
                .unlockedBy("has_logs_that_burn", has(ItemTags.LOGS_THAT_BURN))
                .save(writer, "dustandash:crafting/log_pile");

        // magnet.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.MAGNET.get())
                .pattern("AEA")
                .pattern("BEB")
                .pattern(" A ")
                .define('A', FORGE_INGOTS_NICKEL)
                .define('B', Items.IRON_INGOT)
                .define('E', DAAItems.DUST_WITH_ENERGY.get())
                .unlockedBy("has_dust_with_energy", has(DAAItems.DUST_WITH_ENERGY.get()))
                .save(writer, "dustandash:crafting/magnet");

        // mantle_mixture.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAAItems.MANTLE_MIXTURE.get(), 2)
                .pattern("ADC")
                .pattern("BEB")
                .pattern("CFA")
                .define('A', Items.DIORITE)
                .define('B', Items.ANDESITE)
                .define('C', Items.GRANITE)
                .define('D', DAAItems.CRYSTALLIZE_DUST.get())
                .define('E', DAAItems.BOWL_WITH_DUST.get())
                .define('F', Items.LAVA_BUCKET)
                .unlockedBy("has_bowl_with_dust", has(DAAItems.BOWL_WITH_DUST.get()))
                .save(writer, "dustandash:crafting/mantle_mixture");

        // melon.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.MELON)
                .pattern("#DW")
                .pattern("FGT")
                .pattern("#E#")
                .define('#', Items.SUGAR)
                .define('D', DAAItems.LIFE_DUST.get())
                .define('W', Items.WATER_BUCKET)
                .define('F', DAAItems.REPRODUCE_DUST.get())
                .define('G', Items.GREEN_WOOL)
                .define('T', DAAItems.METAMORPHOSE_DUST.get())
                .define('E', DAAItems.ABSORB_DUST.get())
                .unlockedBy("has_life_dust", has(DAAItems.LIFE_DUST.get()))
                .save(writer, "dustandash:crafting/melon");

        // metal_gem.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.METAL_GEM.get())
                .requires(FORGE_GEMS)
                .requires(DAAItems.BOWL_WITH_DUST.get())
                .requires(DAAItems.METAL_DUST.get())
                .requires(DAAItems.METAL_DUST.get())
                .requires(DAAItems.METAL_DUST.get())
                .requires(DAAItems.METAL_DUST.get())
                .requires(DAAItems.METAL_DUST.get())
                .requires(DAAItems.METAL_DUST.get())
                .requires(DAAItems.METAL_DUST.get())
                .unlockedBy("has_metal_dust", has(DAAItems.METAL_DUST.get()))
                .save(writer, "dustandash:crafting/metal_gem");

        // milling_machine.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DAABlocks.MILLING_MACHINE.get())
                .pattern(" #S")
                .pattern("BAP")
                .pattern("iCi")
                .define('#', Items.CHAIN)
                .define('S', Items.STONECUTTER)
                .define('B', Items.BLAST_FURNACE)
                .define('A', Items.ANVIL)
                .define('P', Items.SMITHING_TABLE)
                .define('i', Items.IRON_INGOT)
                .define('C', Items.CRAFTING_TABLE)
                .unlockedBy("has_anvil", has(Items.ANVIL))
                .save(writer, "dustandash:crafting/milling_machine");

        // moss_block.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.MOSS_BLOCK, 2)
                .requires(FORGE_COBBLESTONE)
                .requires(DAAItems.SHARPEN_FLINT.get())
                .requires(Items.BONE_MEAL)
                .requires(DAAItems.BLOODY_FLINT.get())
                .unlockedBy("has_sharpen_flint", has(DAAItems.SHARPEN_FLINT.get()))
                .save(writer, "dustandash:crafting/moss_block");

        // mycelium.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.MYCELIUM)
                .requires(Items.GRASS_BLOCK)
                .requires(FORGE_MUSHROOMS)
                .requires(DAAItems.METAMORPHOSE_DUST.get())
                .unlockedBy("has_metamorphose_dust", has(DAAItems.METAMORPHOSE_DUST.get()))
                .save(writer, "dustandash:crafting/mycelium");

        // netherrack.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.NETHERRACK, 2)
                .requires(FORGE_STONE)
                .requires(DAAItems.METAMORPHOSE_DUST.get())
                .requires(DAAItems.SMELT_DUST.get())
                .requires(FORGE_STONE)
                .unlockedBy("has_smelt_dust", has(DAAItems.SMELT_DUST.get()))
                .save(writer, "dustandash:crafting/netherrack");

        // nether_wart.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.NETHER_WART)
                .pattern("#D#")
                .pattern("XRX")
                .pattern("ISI")
                .define('#', DAAItems.REPRODUCE_DUST.get())
                .define('D', DAAItems.SMELT_DUST.get())
                .define('X', DAAItems.ABSORB_DUST.get())
                .define('R', Items.RED_MUSHROOM)
                .define('I', DAAItems.METAMORPHOSE_DUST.get())
                .define('S', Items.SOUL_SAND)
                .unlockedBy("has_smelt_dust", has(DAAItems.SMELT_DUST.get()))
                .save(writer, "dustandash:crafting/nether_wart");

        // netherite_mud.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAABlocks.NETHERITE_MUD.get(), 2)
                .requires(Items.DIAMOND)
                .requires(Items.BROWN_DYE)
                .requires(DAAItems.LACERATE_DUST.get())
                .requires(DAAItems.CRYSTALLIZE_DUST.get())
                .requires(DAAItems.TITANIUM_SCRAP.get())
                .requires(DAAItems.METAMORPHOSE_DUST.get())
                .requires(Items.OBSIDIAN)
                .requires(Items.FERMENTED_SPIDER_EYE)
                .requires(Items.PHANTOM_MEMBRANE)
                .unlockedBy("has_titanium_scrap", has(DAAItems.TITANIUM_SCRAP.get()))
                .save(writer, "dustandash:crafting/netherite_mud");

        // neutron_diamond_block.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.DIAMOND_BLOCK)
                .requires(RecipeIngredients.nbt(Items.COAL_BLOCK, "{neutron:1280, display:{Lore:['{\"text\":\"Neutron: 1280/1280\"}']}}"))
                .requires(DAAItems.ELECTRON.get())
                .unlockedBy("has_electron", has(DAAItems.ELECTRON.get()))
                .save(writer, "dustandash:crafting/neutron_diamond_block");

        // neutron_electron.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.ELECTRON.get(), 42)
                .requires(RecipeIngredients.nbt(DAAItems.URANIUM_INGOT.get(), "{neutron:1280, display:{Lore:['{\"text\":\"Neutron: 1280/1280\"}']}}"))
                .requires(DAAItems.ELECTRON.get())
                .unlockedBy("has_electron", has(DAAItems.ELECTRON.get()))
                .save(writer, "dustandash:crafting/neutron_electron");

        // neutron_gold_block.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GOLD_BLOCK)
                .requires(RecipeIngredients.nbt(Items.STONE, "{neutron:1280, display:{Lore:['{\"text\":\"Neutron: 1280/1280\"}']}}"))
                .requires(DAAItems.ELECTRON.get())
                .unlockedBy("has_electron", has(DAAItems.ELECTRON.get()))
                .save(writer, "dustandash:crafting/neutron_gold_block");

        // neutron_indestructible.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.INDESTRUCTIBLE.get())
                .requires(RecipeIngredients.nbt(DAAItems.ROCK_SOLID.get(), "{neutron:1280, display:{Lore:['{\"text\":\"Neutron: 1280/1280\"}']}}"))
                .requires(DAAItems.ELECTRON.get())
                .unlockedBy("has_electron", has(DAAItems.ELECTRON.get()))
                .save(writer, "dustandash:crafting/neutron_indestructible");

        // neutron_titanium.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.TITANIUM_INGOT.get(), 6)
                .requires(RecipeIngredients.nbt(Items.IRON_INGOT, "{neutron:1280, display:{Lore:['{\"text\":\"Neutron: 1280/1280\"}']}}"))
                .requires(RecipeIngredients.nbt(Items.IRON_INGOT, "{neutron:1280, display:{Lore:['{\"text\":\"Neutron: 1280/1280\"}']}}"))
                .requires(RecipeIngredients.nbt(Items.IRON_INGOT, "{neutron:1280, display:{Lore:['{\"text\":\"Neutron: 1280/1280\"}']}}"))
                .requires(RecipeIngredients.nbt(Items.IRON_INGOT, "{neutron:1280, display:{Lore:['{\"text\":\"Neutron: 1280/1280\"}']}}"))
                .requires(RecipeIngredients.nbt(Items.IRON_INGOT, "{neutron:1280, display:{Lore:['{\"text\":\"Neutron: 1280/1280\"}']}}"))
                .requires(DAAItems.ELECTRON.get())
                .requires(DAAItems.ELECTRON.get())
                .requires(DAAItems.ELECTRON.get())
                .unlockedBy("has_electron", has(DAAItems.ELECTRON.get()))
                .save(writer, "dustandash:crafting/neutron_titanium");

        // neutron_tungsten.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.TUNGSTEN_INGOT.get(), 4)
                .requires(RecipeIngredients.nbt(Items.IRON_INGOT, "{neutron:1280, display:{Lore:['{\"text\":\"Neutron: 1280/1280\"}']}}"))
                .requires(RecipeIngredients.nbt(Items.NETHERITE_INGOT, "{neutron:1280, display:{Lore:['{\"text\":\"Neutron: 1280/1280\"}']}}"))
                .requires(DAAItems.ELECTRON.get())
                .requires(DAAItems.ELECTRON.get())
                .unlockedBy("has_electron", has(DAAItems.ELECTRON.get()))
                .save(writer, "dustandash:crafting/neutron_tungsten");

        // oak_sapling.json
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.OAK_SAPLING)
                .pattern("#D#")
                .pattern("#/#")
                .pattern("JBJ")
                .define('#', DAAItems.LIFE_DUST.get())
                .define('D', DAAItems.REPRODUCE_DUST.get())
                .define('/', FORGE_RODS_WOODEN)
                .define('J', DAAItems.ABSORB_DUST.get())
                .define('B', Items.BAMBOO)
                .unlockedBy("has_life_dust", has(DAAItems.LIFE_DUST.get()))
                .save(writer, "dustandash:crafting/oak_sapling");

        // order_gem.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.ORDER_GEM.get())
                .requires(FORGE_GEMS)
                .requires(DAAItems.BOWL_WITH_DUST.get())
                .requires(DAAItems.ORDER_DUST.get())
                .requires(DAAItems.ORDER_DUST.get())
                .requires(DAAItems.ORDER_DUST.get())
                .requires(DAAItems.ORDER_DUST.get())
                .requires(DAAItems.ORDER_DUST.get())
                .requires(DAAItems.ORDER_DUST.get())
                .requires(DAAItems.ORDER_DUST.get())
                .unlockedBy("has_order_dust", has(DAAItems.ORDER_DUST.get()))
                .save(writer, "dustandash:crafting/order_gem");

        // placeholder.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.PLACEHOLDER.get(), 2)
                .requires(Items.IRON_NUGGET)
                .requires(DAAItems.ASH_STEEL_INGOT.get())
                .requires(Items.IRON_NUGGET)
                .requires(DAAItems.ASH_STEEL_INGOT.get())
                .unlockedBy("has_ash_steel_ingot", has(DAAItems.ASH_STEEL_INGOT.get()))
                .save(writer, "dustandash:crafting/placeholder");

        // position_selector.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DAAItems.POSITION_SELECTOR.get())
                .requires(DAAItems.PLACEHOLDER.get())
                .requires(DAAItems.REDSTONE_VACUUM_TUBE.get())
                .unlockedBy("has_placeholder", has(DAAItems.PLACEHOLDER.get()))
                .save(writer, "dustandash:crafting/position_selector");

        // potato.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.POTATO)
                .requires(Items.BEETROOT)
                .requires(DAAItems.METAMORPHOSE_DUST.get())
                .requires(Items.BROWN_DYE)
                .requires(DAAItems.REPRODUCE_DUST.get())
                .requires(DAAItems.REPRODUCE_DUST.get())
                .requires(Items.YELLOW_DYE)
                .unlockedBy("has_metamorphose_dust", has(DAAItems.METAMORPHOSE_DUST.get()))
                .save(writer, "dustandash:crafting/potato");

        // prismarine_crystals.json
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PRISMARINE_CRYSTALS)
                .requires(Items.PRISMARINE_SHARD)
                .requires(DAAItems.ABSORB_DUST.get())
                .requires(Items.GLOWSTONE_DUST)
                .unlockedBy("has_absorb_dust", has(DAAItems.ABSORB_DUST.get()))
                .save(writer, "dustandash:crafting/prismarine_crystals");
    }
}
