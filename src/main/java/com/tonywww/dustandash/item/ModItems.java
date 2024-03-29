package com.tonywww.dustandash.item;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.item.custom.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DustAndAsh.MOD_ID);

    public static final RegistryObject<Item> EMPTY = ITEMS.register("empty",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    // Items
    // tools
    public static final RegistryObject<Item> HAND_VACUUM = ITEMS.register("hand_vacuum",
            () -> new HandVacuum(new Item.Properties()
                    .stacksTo(1)
                    .durability(38)
            ));

    public static final RegistryObject<Item> IRON_VACUUM = ITEMS.register("iron_vacuum",
            () -> new IronVacuum(new Item.Properties()
                    .stacksTo(1)
                    .durability(540)
            ));

    public static final RegistryObject<Item> SHARPEN_FLINT = ITEMS.register("sharpen_flint",
            () -> new SharpenFlint(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> FLINT_PICKAXE = ITEMS.register("flint_pickaxe",
            () -> new PickaxeItem(ModItemTier.FLINT, 4, -2.4F,
                    (new Item.Properties())
            ));

    public static final RegistryObject<Item> ASH_STEEL_BOOTS = ITEMS.register("ash_steel_boots",
            () -> new ArmorItem(ModArmorMaterial.ASH_STEEL, ArmorItem.Type.BOOTS,
                    new Item.Properties()
            ));

    public static final RegistryObject<Item> ASH_STEEL_CHESTPLATE = ITEMS.register("ash_steel_chestplate",
            () -> new ArmorItem(ModArmorMaterial.ASH_STEEL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties()
            ));

    public static final RegistryObject<Item> ASH_STEEL_LEGGING = ITEMS.register("ash_steel_leggings",
            () -> new ArmorItem(ModArmorMaterial.ASH_STEEL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties()
            ));

    public static final RegistryObject<Item> ASH_STEEL_HELMET = ITEMS.register("ash_steel_helmet",
            () -> new ArmorItem(ModArmorMaterial.ASH_STEEL, ArmorItem.Type.HELMET,
                    new Item.Properties()
            ));

    public static final RegistryObject<Item> ASH_STEEL_SWORD = ITEMS.register("ash_steel_sword",
            () -> new SwordItem(ModItemTier.ASH_STEEL, 3, -2.3F,
                    (new Item.Properties())
            ));

    public static final RegistryObject<Item> ASH_STEEL_SHOVEL = ITEMS.register("ash_steel_shovel",
            () -> new ShovelItem(ModItemTier.ASH_STEEL, 1.5F, -2.9F,
                    (new Item.Properties())
            ));

    public static final RegistryObject<Item> ASH_STEEL_PICKAXE = ITEMS.register("ash_steel_pickaxe",
            () -> new PickaxeItem(ModItemTier.ASH_STEEL, 1, -2.7F,
                    (new Item.Properties())
            ));

    public static final RegistryObject<Item> ASH_STEEL_AXE = ITEMS.register("ash_steel_axe",
            () -> new AxeItem(ModItemTier.ASH_STEEL, 5.5F, -3.05F,
                    (new Item.Properties())
            ));

    public static final RegistryObject<Item> ASH_STEEL_HOE = ITEMS.register("ash_steel_hoe",
            () -> new HoeItem(ModItemTier.ASH_STEEL, -2, -0.9F,
                    (new Item.Properties())
            ));

    public static final RegistryObject<Item> TITANIUM_ALLOY_SWORD = ITEMS.register("titanium_alloy_sword",
            () -> new TitaniumAlloySword(ModItemTier.TITANIUM_ALLOY, 2, -2.0F,
                    (new Item.Properties()
                            .fireResistant())
            ));
    public static final RegistryObject<Item> TITANIUM_ALLOY_GREAT_SWORD = ITEMS.register("titanium_alloy_great_sword",
            () -> new TitaniumAlloyGreatSword(ModItemTier.TITANIUM_ALLOY, 7, -2.9F,
                    (new Item.Properties()
                            .fireResistant())
            ));

    public static final RegistryObject<Item> TITANIUM_ALLOY_PICKAXE = ITEMS.register("titanium_alloy_pickaxe",
            () -> new PickaxeItem(ModItemTier.TITANIUM_ALLOY, 1, -2.7F,
                    (new Item.Properties()
                            .fireResistant())
            ));

    // SP Weapons
    public static final RegistryObject<Item> GALE_OTAIJUTSU = ITEMS.register("gale_otaijutsu",
            () -> new GaleOtaijutsu(ModItemTier.TITANIUM_ALLOY, 2, -1.0F,
                    (new Item.Properties()
                            .fireResistant())
            ));

    public static final RegistryObject<Item> SUNBURN_MEGA_SWORD = ITEMS.register("sunburn_mega_sword",
            () -> new SunburnMegaSword(ModItemTier.TITANIUM_ALLOY, 9, -3.1F,
                    (new Item.Properties()
                            .fireResistant())
            ));

    public static final RegistryObject<Item> WHITE_LIGHTNING = ITEMS.register("white_lightning",
            () -> new WhiteLightning(ModItemTier.TITANIUM_ALLOY, 3, -2.0F,
                    (new Item.Properties()
                            .fireResistant())
            ));

    public static final RegistryObject<Item> LORD_OF_BLOOD = ITEMS.register("lord_of_blood",
            () -> new LordOfBlood(ModItemTier.TITANIUM_ALLOY, 4, -2.6F,
                    (new Item.Properties()
                            .fireResistant())
            ));

    public static final RegistryObject<Item> JUDGEMENT = ITEMS.register("judgement",
            () -> new Judgement(ModItemTier.TITANIUM_ALLOY, 12, -3.2F,
                    (new Item.Properties()
                            .fireResistant())
            ));

    // metals
    public static final RegistryObject<Item> ASH_STEEL_INGOT = ITEMS.register("ash_steel_ingot",
            () -> new Item(new Item.Properties()
                    .stacksTo(32)
            ));

    public static final RegistryObject<Item> TITANIUM_INGOT = ITEMS.register("titanium_ingot",
            () -> new Item(new Item.Properties()
                    .stacksTo(32)
            ));

    public static final RegistryObject<Item> COPPER_INGOT = ITEMS.register("copper_ingot",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> TIN_INGOT = ITEMS.register("tin_ingot",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> LEAD_INGOT = ITEMS.register("lead_ingot",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> NICKEL_INGOT = ITEMS.register("nickel_ingot",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> ALUMINUM_INGOT = ITEMS.register("aluminum_ingot",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> OSMIUM_INGOT = ITEMS.register("osmium_ingot",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> URANIUM_INGOT = ITEMS.register("uranium_ingot",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> TUNGSTEN_INGOT = ITEMS.register("tungsten_ingot",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> TITANIUM_TUNGSTEN_ALLOY = ITEMS.register("titanium_tungsten_alloy",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> TITANIUM_ALUMINUM_ALLOY = ITEMS.register("titanium_aluminum_alloy",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    // dusts
    public static final RegistryObject<Item> METAL_DUST = ITEMS.register("metal_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(48)
            ));

    public static final RegistryObject<Item> LIFE_DUST = ITEMS.register("life_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(48)
            ));

    public static final RegistryObject<Item> ORDER_DUST = ITEMS.register("order_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(48)
            ));

    public static final RegistryObject<Item> FIRE_DUST = ITEMS.register("fire_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(48)
            ));

    public static final RegistryObject<Item> EARTH_DUST = ITEMS.register("earth_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(48)
            ));

    public static final RegistryObject<Item> REPRODUCE_DUST = ITEMS.register("reproduce_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(24)
            ));

    public static final RegistryObject<Item> INHERIT_DUST = ITEMS.register("inherit_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(24)
            ));

    public static final RegistryObject<Item> METAMORPHOSE_DUST = ITEMS.register("metamorphose_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(24)
            ));

    public static final RegistryObject<Item> CRYSTALLIZE_DUST = ITEMS.register("crystallize_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(24)
            ));

    public static final RegistryObject<Item> SEEP_DUST = ITEMS.register("seep_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(24)
            ));

    public static final RegistryObject<Item> EXTINGUISH_DUST = ITEMS.register("extinguish_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(24)
            ));

    public static final RegistryObject<Item> ABSORB_DUST = ITEMS.register("absorb_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(24)
            ));

    public static final RegistryObject<Item> SMELT_DUST = ITEMS.register("smelt_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(24)
            ));

    public static final RegistryObject<Item> BLOCKING_DUST = ITEMS.register("blocking_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(24)
            ));

    public static final RegistryObject<Item> LACERATE_DUST = ITEMS.register("lacerate_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(24)
            ));

    // advance material
    public static final RegistryObject<Item> RAIN_CRYSTAL = ITEMS.register("rain_crystal",
            () -> new RainCrystal(new Item.Properties()
                    .stacksTo(1)
            ));

    public static final RegistryObject<Item> SUN_CRYSTAL = ITEMS.register("sun_crystal",
            () -> new SunCrystal(new Item.Properties()
                    .stacksTo(1)
            ));

    public static final RegistryObject<Item> ECHO_ACTIVATOR = ITEMS.register("echo_activator",
            () -> new EchoActivator(new Item.Properties()
                    .stacksTo(1)
                    .durability(2)
            ));

    public static final RegistryObject<Item> NANO_MACHINE = ITEMS.register("nano_machine",
            () -> new NanoMachine(new Item.Properties()
                    .stacksTo(3)
            ));

    public static final RegistryObject<Item> DUST_WITH_ENERGY = ITEMS.register("dust_with_energy",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> BOWL_WITH_DUST = ITEMS.register("bowl_with_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(16)
            ));

    public static final RegistryObject<Item> ENERGIZED_COBBLESTONE_BUCKET = ITEMS.register("energized_cobblestone_bucket",
            () -> new Item(new Item.Properties()
                    .stacksTo(1)
            ));

    public static final RegistryObject<Item> PURE_ENERGY = ITEMS.register("pure_energy",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> BASIC_MACHINE_FRAME = ITEMS.register("basic_machine_frame",
            () -> new Item(new Item.Properties()
                    .stacksTo(2)
            ));

    public static final RegistryObject<Item> SOUL_OF_LIGHT = ITEMS.register("soul_of_light",
            () -> new Item(new Item.Properties()
                    .stacksTo(2)
            ));

    public static final RegistryObject<Item> PURE_DARKNESS = ITEMS.register("pure_darkness",
            () -> new Item(new Item.Properties()
                    .stacksTo(2)
            ));

    // normal materials
    public static final RegistryObject<Item> BLOODY_FLINT = ITEMS.register("bloody_flint",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> ELECTRON = ITEMS.register("electron",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> CRUSHED_SEEDS = ITEMS.register("crushed_seeds",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));
    public static final RegistryObject<Item> ASH = ITEMS.register("ash",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> WOOD_ASH = ITEMS.register("wood_ash",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> RAW_ASH_STEEL = ITEMS.register("raw_ash_steel",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> BLOODY_FLINT_STICK = ITEMS.register("bloody_flint_stick",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> RAW_TITANIUM_SAND = ITEMS.register("raw_titanium_sand",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> TITANIUM_SAND = ITEMS.register("titanium_sand",
            () -> new Item(new Item.Properties()
                    .stacksTo(32)
            ));

    public static final RegistryObject<Item> TITANIUM_SCRAP = ITEMS.register("titanium_scrap",
            () -> new Item(new Item.Properties()
                    .stacksTo(32)
            ));
    public static final RegistryObject<Item> TITANIUM_CHUNK = ITEMS.register("titanium_chunk",
            () -> new Item(new Item.Properties()
                    .stacksTo(16)
            ));

    public static final RegistryObject<Item> WOODEN_TOOL_HANDLE = ITEMS.register("wooden_tool_handle",
            () -> new Item(new Item.Properties()
                    .stacksTo(4)
            ));

    public static final RegistryObject<Item> CARBON_DUST = ITEMS.register("carbon_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> RAW_CARBON_FIBER = ITEMS.register("raw_carbon_fiber",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));


    public static final RegistryObject<Item> CARBON_FIBER = ITEMS.register("carbon_fiber",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> CARBON_FIBER_PLATE = ITEMS.register("carbon_fiber_plate",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> TUNGSTEN_DUST = ITEMS.register("tungsten_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> SODIUM_DUST = ITEMS.register("sodium_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> MAGNET = ITEMS.register("magnet",
            () -> new Item(new Item.Properties()
                    .stacksTo(1)
            ));

    public static final RegistryObject<Item> MANTLE_MIXTURE = ITEMS.register("mantle_mixture",
            () -> new Item(new Item.Properties()
                    .stacksTo(32)
            ));

    public static final RegistryObject<Item> TITANIUM_HEAVY_PLATE = ITEMS.register("titanium_heavy_plate",
            () -> new Item(new Item.Properties()
                    .stacksTo(4)
            ));


    // container stuff
    public static final RegistryObject<Item> GLASS_CONTAINER = ITEMS.register("glass_container",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> WATER_MISCIBLE_SOLVENTS = ITEMS.register("water_miscible_solvents",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> GRIND_SOLVENTS = ITEMS.register("grind_solvents",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> BASE_OIL = ITEMS.register("base_oil",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> CHLORINE = ITEMS.register("chlorine",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> HYDROGEN = ITEMS.register("hydrogen",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> OXYGEN = ITEMS.register("oxygen",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> REDUCTANT = ITEMS.register("reductant",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> GRAPHITE_ELECTRODE = ITEMS.register("graphite_electrode",
            () -> new Item(new Item.Properties()
                    .stacksTo(4)
            ));

    // gears
    public static final RegistryObject<Item> GEAR_BOX = ITEMS.register("gear_box",
            () -> new Item(new Item.Properties()
                    .stacksTo(2)
            ));

    public static final RegistryObject<Item> IRON_GEAR = ITEMS.register("iron_gear",
            () -> new Item(new Item.Properties()
                    .stacksTo(32)
            ));

    public static final RegistryObject<Item> REDSTONE_GEAR = ITEMS.register("redstone_gear",
            () -> new Item(new Item.Properties()
                    .stacksTo(32)
            ));

    public static final RegistryObject<Item> ASH_STEEL_GEAR = ITEMS.register("ash_steel_gear",
            () -> new Item(new Item.Properties()
                    .stacksTo(32)
            ));

    // scraps
    public static final RegistryObject<Item> IRON_SCRAP = ITEMS.register("iron_scrap",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> REDSTONE_SCRAP = ITEMS.register("redstone_scrap",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> ASH_STEEL_SCRAP = ITEMS.register("ash_steel_scrap",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> CARBON_FIBER_SCRAP = ITEMS.register("carbon_fiber_scrap",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> TITANIUM_PLATE_SCRAP = ITEMS.register("titanium_plate_scrap",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
            ));

    // Element Crystal
    public static final RegistryObject<Item> WIND_CRYSTAL = ITEMS.register("wind_crystal",
            () -> new Item(new Item.Properties()
                    .stacksTo(4)
            ));

    public static final RegistryObject<Item> FIRE_CRYSTAL = ITEMS.register("fire_crystal",
            () -> new Item(new Item.Properties()
                    .stacksTo(4)
            ));

    public static final RegistryObject<Item> SILVER_CRYSTAL = ITEMS.register("silver_crystal",
            () -> new Item(new Item.Properties()
                    .stacksTo(4)
            ));

    public static final RegistryObject<Item> PARASITISM_CRYSTAL = ITEMS.register("parasitism_crystal",
            () -> new Item(new Item.Properties()
                    .stacksTo(4)
            ));

    public static final RegistryObject<Item> DEPRAVITY_CRYSTAL = ITEMS.register("depravity_crystal",
            () -> new Item(new Item.Properties()
                    .stacksTo(4)
            ));

    public static final RegistryObject<Item> APOTHEOSIS_CRYSTAL = ITEMS.register("apotheosis_crystal",
            () -> new Item(new Item.Properties()
                    .stacksTo(4)
            ));

    public static final RegistryObject<Item> ETERNAL_LIGHT_CRYSTAL = ITEMS.register("eternal_light_crystal",
            () -> new Item(new Item.Properties()
                    .stacksTo(4)
            ));

    public static final RegistryObject<Item> FOREVER_DARK_CRYSTAL = ITEMS.register("forever_dark_crystal",
            () -> new Item(new Item.Properties()
                    .stacksTo(4)
            ));

    public static final RegistryObject<Item> VOID_CRYSTAL = ITEMS.register("void_crystal",
            () -> new Item(new Item.Properties()
                    .stacksTo(4)
            ));


    public static void register(IEventBus eventBus) {

        ITEMS.register(eventBus);

    }

}
