package com.tonywww.dustandash.data.datagen;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.registry.DAAItems;
import com.tonywww.dustandash.tag.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
                                ExistingFileHelper existingFileHelper) {

        super(output, lookupProvider, blockTags, DustAndAsh.MOD_ID, existingFileHelper);
    }

    private static TagKey<Item> forgeTag(String path) {

        return ItemTags.create(new ResourceLocation("forge", path));
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.CRAFT_MATERIAL)
                .addTag(forgeTag("storage_blocks"));

        tag(ModTags.Items.MILLING_BLACKLIST)
                .add(DAAItems.IRON_SCRAP.get())
                .add(DAAItems.REDSTONE_SCRAP.get())
                .add(DAAItems.ASH_STEEL_SCRAP.get())
                .add(DAAItems.CARBON_FIBER_SCRAP.get())
                .add(DAAItems.TITANIUM_PLATE_SCRAP.get());

        tag(ModTags.Items.CENTRIFUGE_CATALYST)
                .add(DAAItems.WATER_MISCIBLE_SOLVENTS.get())
                .add(DAAItems.GRIND_SOLVENTS.get())
                .add(DAAItems.BASE_OIL.get())
                .add(DAAItems.GLASS_CONTAINER.get())
                .add(DAAItems.METAL_GEM.get())
                .add(DAAItems.LIFE_GEM.get())
                .add(DAAItems.ORDER_GEM.get())
                .add(DAAItems.FIRE_GEM.get())
                .add(DAAItems.EARTH_GEM.get())
                .add(net.minecraft.world.item.Items.POTION);

        tag(ModTags.Items.NANO_BLACKLIST)
                .addTag(elementDust())
                .add(net.minecraft.world.item.Items.BOWL)
                .add(net.minecraft.world.item.Items.HONEY_BOTTLE)
                .add(net.minecraft.world.item.Items.CHARCOAL)
                .add(net.minecraft.world.item.Items.REDSTONE)
                .add(net.minecraft.world.item.Items.SLIME_BALL)
                .add(net.minecraft.world.item.Items.PAPER)
                .add(net.minecraft.world.item.Items.SAND)
                .add(net.minecraft.world.item.Items.GUNPOWDER)
                .add(net.minecraft.world.item.Items.QUARTZ)
                .add(DAAItems.BOWL_WITH_DUST.get())
                .add(DAAItems.DUST_WITH_ENERGY.get())
                .add(DAAItems.ASH.get())
                .add(DAAItems.CARBON_DUST.get())
                .add(DAAItems.CARBON_FIBER.get())
                .add(DAAItems.MANTLE_MIXTURE.get());

        tag(elementCrystals())
                .add(DAAItems.WIND_CRYSTAL.get())
                .add(DAAItems.FIRE_CRYSTAL.get())
                .add(DAAItems.SILVER_CRYSTAL.get())
                .add(DAAItems.PARASITISM_CRYSTAL.get())
                .add(DAAItems.DEPRAVITY_CRYSTAL.get())
                .add(DAAItems.APOTHEOSIS_CRYSTAL.get())
                .add(DAAItems.ETERNAL_LIGHT_CRYSTAL.get())
                .add(DAAItems.FOREVER_DARK_CRYSTAL.get())
                .add(DAAItems.VOID_CRYSTAL.get());

        tag(elementDust())
                .addTag(lv1Dust())
                .addTag(lv2Dust())
                .addTag(lv3Dust());

        tag(lv1Dust())
                .add(DAAItems.METAL_DUST.get())
                .add(DAAItems.ORDER_DUST.get())
                .add(DAAItems.EARTH_DUST.get());

        tag(lv2Dust())
                .add(DAAItems.LIFE_DUST.get())
                .add(DAAItems.FIRE_DUST.get());

        tag(lv3Dust())
                .add(DAAItems.REPRODUCE_DUST.get())
                .add(DAAItems.INHERIT_DUST.get())
                .add(DAAItems.METAMORPHOSE_DUST.get())
                .add(DAAItems.CRYSTALLIZE_DUST.get())
                .add(DAAItems.SEEP_DUST.get())
                .add(DAAItems.EXTINGUISH_DUST.get())
                .add(DAAItems.ABSORB_DUST.get())
                .add(DAAItems.SMELT_DUST.get())
                .add(DAAItems.BLOCKING_DUST.get())
                .add(DAAItems.LACERATE_DUST.get());

        tag(ModTags.Items.NEUTRON_CONTAINER)
                .add(Items.COAL_BLOCK)
                .add(Items.STONE)
                .add(Items.IRON_INGOT)
                .add(Items.NETHERITE_INGOT)
                .add(DAAItems.URANIUM_INGOT.get())
                .add(DAAItems.ROCK_SOLID.get())
                .add(DAAItems.NANO_MACHINE.get())
                .add(DAAItems.U235_FUEL.get())
                .add(DAAItems.U235_FUEL_METAL.get())
                .add(DAAItems.U235_FUEL_LIFE.get())
                .add(DAAItems.U235_FUEL_ORDER.get())
                .add(DAAItems.U235_FUEL_FIRE.get())
                .add(DAAItems.U235_FUEL_EARTH.get())
                .add(DAAItems.BASIC_COOLING.get())
                .add(DAAItems.COOLING_TIER_1.get())
                .add(DAAItems.COOLING_TIER_2.get())
                .add(DAAItems.COOLING_TIER_3.get());

        tag(ModTags.Items.ASH)
                .add(DAAItems.ASH.get())
                .addOptional(new ResourceLocation("embers", "ash"))
                .addOptional(new ResourceLocation("forestry", "ash"));

        tag(forgeTag("gears/iron")).add(DAAItems.IRON_GEAR.get());
        tag(forgeTag("gears/redstone")).add(DAAItems.REDSTONE_GEAR.get());
        tag(forgeTag("gears/steel")).add(DAAItems.ASH_STEEL_GEAR.get());
        tag(forgeTag("gears"))
                .addTag(forgeTag("gears/iron"))
                .addTag(forgeTag("gears/redstone"))
                .addTag(forgeTag("gears/steel"));

        tag(forgeTag("ingots/aluminum")).add(DAAItems.ALUMINUM_INGOT.get());
        tag(forgeTag("ingots/ash_steel")).add(DAAItems.ASH_STEEL_INGOT.get());
        tag(forgeTag("ingots/copper")).add(DAAItems.COPPER_INGOT.get());
        tag(forgeTag("ingots/lead")).add(DAAItems.LEAD_INGOT.get());
        tag(forgeTag("ingots/nickel")).add(DAAItems.NICKEL_INGOT.get());
        tag(forgeTag("ingots/osmium")).add(DAAItems.OSMIUM_INGOT.get());
        tag(forgeTag("ingots/silver")).add(DAAItems.SILVER_INGOT.get());
        tag(forgeTag("ingots/tin")).add(DAAItems.TIN_INGOT.get());
        tag(forgeTag("ingots/titanium")).add(DAAItems.TITANIUM_INGOT.get());
        tag(forgeTag("ingots/tungsten")).add(DAAItems.TUNGSTEN_INGOT.get());
        tag(forgeTag("ingots/uranium")).add(DAAItems.URANIUM_INGOT.get());
        tag(forgeTag("ingots"))
                .addTag(forgeTag("ingots/copper"))
                .addTag(forgeTag("ingots/lead"))
                .addTag(forgeTag("ingots/nickel"))
                .addTag(forgeTag("ingots/silver"))
                .addTag(forgeTag("ingots/aluminum"))
                .addTag(forgeTag("ingots/osmium"))
                .addTag(forgeTag("ingots/uranium"))
                .addTag(forgeTag("ingots/tungsten"))
                .addTag(forgeTag("ingots/titanium"));

        tag(forgeTag("rods/wooden")).add(DAAItems.BLOODY_FLINT_STICK.get());

        tag(ItemTags.create(new ResourceLocation("minecraft", "swords")))
                .add(DAAItems.ASH_STEEL_SWORD.get())
                .add(DAAItems.TITANIUM_ALLOY_SWORD.get())
                .add(DAAItems.TITANIUM_ALLOY_GREAT_SWORD.get())
                .add(DAAItems.GALE_OTAIJUTSU.get())
                .add(DAAItems.SUNBURN_MEGA_SWORD.get())
                .add(DAAItems.WHITE_LIGHTNING.get())
                .add(DAAItems.LORD_OF_BLOOD.get())
                .add(DAAItems.ROTTEN_BLADE.get());
    }

    private static TagKey<Item> elementCrystals() {

        return ItemTags.create(new ResourceLocation(DustAndAsh.MOD_ID, "element_crystals"));
    }

    private static TagKey<Item> elementDust() {

        return ItemTags.create(new ResourceLocation(DustAndAsh.MOD_ID, "element_dust"));
    }

    private static TagKey<Item> lv1Dust() {

        return ItemTags.create(new ResourceLocation(DustAndAsh.MOD_ID, "lv1_dust"));
    }

    private static TagKey<Item> lv2Dust() {

        return ItemTags.create(new ResourceLocation(DustAndAsh.MOD_ID, "lv2_dust"));
    }

    private static TagKey<Item> lv3Dust() {

        return ItemTags.create(new ResourceLocation(DustAndAsh.MOD_ID, "lv3_dust"));
    }
}
