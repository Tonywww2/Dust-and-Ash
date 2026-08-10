package com.tonywww.dustandash.registry;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class DAAItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DustAndAsh.MOD_ID);
    public static final RegistryObject<Item> EMPTY = simple("empty", 64);

    // DAAEquipmentItems
    public static final RegistryObject<Item> HAND_VACUUM = DAAEquipmentItems.HAND_VACUUM;
    public static final RegistryObject<Item> IRON_VACUUM = DAAEquipmentItems.IRON_VACUUM;
    public static final RegistryObject<Item> SHARPEN_FLINT = DAAEquipmentItems.SHARPEN_FLINT;
    public static final RegistryObject<Item> FLINT_PICKAXE = DAAEquipmentItems.FLINT_PICKAXE;
    public static final RegistryObject<Item> ASH_STEEL_BOOTS = DAAEquipmentItems.ASH_STEEL_BOOTS;
    public static final RegistryObject<Item> ASH_STEEL_CHESTPLATE = DAAEquipmentItems.ASH_STEEL_CHESTPLATE;
    public static final RegistryObject<Item> ASH_STEEL_LEGGING = DAAEquipmentItems.ASH_STEEL_LEGGING;
    public static final RegistryObject<Item> ASH_STEEL_HELMET = DAAEquipmentItems.ASH_STEEL_HELMET;
    public static final RegistryObject<Item> ASH_STEEL_SWORD = DAAEquipmentItems.ASH_STEEL_SWORD;
    public static final RegistryObject<Item> ASH_STEEL_SHOVEL = DAAEquipmentItems.ASH_STEEL_SHOVEL;
    public static final RegistryObject<Item> ASH_STEEL_PICKAXE = DAAEquipmentItems.ASH_STEEL_PICKAXE;
    public static final RegistryObject<Item> ASH_STEEL_AXE = DAAEquipmentItems.ASH_STEEL_AXE;
    public static final RegistryObject<Item> ASH_STEEL_HOE = DAAEquipmentItems.ASH_STEEL_HOE;
    public static final RegistryObject<Item> TITANIUM_ALLOY_SWORD = DAAEquipmentItems.TITANIUM_ALLOY_SWORD;
    public static final RegistryObject<Item> TITANIUM_ALLOY_GREAT_SWORD = DAAEquipmentItems.TITANIUM_ALLOY_GREAT_SWORD;
    public static final RegistryObject<Item> TITANIUM_ALLOY_PICKAXE = DAAEquipmentItems.TITANIUM_ALLOY_PICKAXE;
    public static final RegistryObject<Item> GALE_OTAIJUTSU = DAAEquipmentItems.GALE_OTAIJUTSU;
    public static final RegistryObject<Item> SUNBURN_MEGA_SWORD = DAAEquipmentItems.SUNBURN_MEGA_SWORD;
    public static final RegistryObject<Item> WHITE_LIGHTNING = DAAEquipmentItems.WHITE_LIGHTNING;
    public static final RegistryObject<Item> LORD_OF_BLOOD = DAAEquipmentItems.LORD_OF_BLOOD;
    public static final RegistryObject<Item> ROTTEN_BLADE = DAAEquipmentItems.ROTTEN_BLADE;
    public static final RegistryObject<Item> JUDGEMENT = DAAEquipmentItems.JUDGEMENT;

    // DAAElementItems
    public static final RegistryObject<Item> METAL_DUST = DAAElementItems.METAL_DUST;
    public static final RegistryObject<Item> LIFE_DUST = DAAElementItems.LIFE_DUST;
    public static final RegistryObject<Item> ORDER_DUST = DAAElementItems.ORDER_DUST;
    public static final RegistryObject<Item> FIRE_DUST = DAAElementItems.FIRE_DUST;
    public static final RegistryObject<Item> EARTH_DUST = DAAElementItems.EARTH_DUST;
    public static final RegistryObject<Item> REPRODUCE_DUST = DAAElementItems.REPRODUCE_DUST;
    public static final RegistryObject<Item> INHERIT_DUST = DAAElementItems.INHERIT_DUST;
    public static final RegistryObject<Item> METAMORPHOSE_DUST = DAAElementItems.METAMORPHOSE_DUST;
    public static final RegistryObject<Item> CRYSTALLIZE_DUST = DAAElementItems.CRYSTALLIZE_DUST;
    public static final RegistryObject<Item> SEEP_DUST = DAAElementItems.SEEP_DUST;
    public static final RegistryObject<Item> EXTINGUISH_DUST = DAAElementItems.EXTINGUISH_DUST;
    public static final RegistryObject<Item> ABSORB_DUST = DAAElementItems.ABSORB_DUST;
    public static final RegistryObject<Item> SMELT_DUST = DAAElementItems.SMELT_DUST;
    public static final RegistryObject<Item> BLOCKING_DUST = DAAElementItems.BLOCKING_DUST;
    public static final RegistryObject<Item> LACERATE_DUST = DAAElementItems.LACERATE_DUST;
    public static final RegistryObject<Item> METAL_GEM = DAAElementItems.METAL_GEM;
    public static final RegistryObject<Item> LIFE_GEM = DAAElementItems.LIFE_GEM;
    public static final RegistryObject<Item> ORDER_GEM = DAAElementItems.ORDER_GEM;
    public static final RegistryObject<Item> FIRE_GEM = DAAElementItems.FIRE_GEM;
    public static final RegistryObject<Item> EARTH_GEM = DAAElementItems.EARTH_GEM;
    public static final RegistryObject<Item> WIND_CRYSTAL = DAAElementItems.WIND_CRYSTAL;
    public static final RegistryObject<Item> FIRE_CRYSTAL = DAAElementItems.FIRE_CRYSTAL;
    public static final RegistryObject<Item> SILVER_CRYSTAL = DAAElementItems.SILVER_CRYSTAL;
    public static final RegistryObject<Item> PARASITISM_CRYSTAL = DAAElementItems.PARASITISM_CRYSTAL;
    public static final RegistryObject<Item> DEPRAVITY_CRYSTAL = DAAElementItems.DEPRAVITY_CRYSTAL;
    public static final RegistryObject<Item> APOTHEOSIS_CRYSTAL = DAAElementItems.APOTHEOSIS_CRYSTAL;
    public static final RegistryObject<Item> ETERNAL_LIGHT_CRYSTAL = DAAElementItems.ETERNAL_LIGHT_CRYSTAL;
    public static final RegistryObject<Item> FOREVER_DARK_CRYSTAL = DAAElementItems.FOREVER_DARK_CRYSTAL;
    public static final RegistryObject<Item> VOID_CRYSTAL = DAAElementItems.VOID_CRYSTAL;

    // DAAMachineItems
    public static final RegistryObject<Item> RAIN_CRYSTAL = DAAMachineItems.RAIN_CRYSTAL;
    public static final RegistryObject<Item> SUN_CRYSTAL = DAAMachineItems.SUN_CRYSTAL;
    public static final RegistryObject<Item> ECHO_ACTIVATOR = DAAMachineItems.ECHO_ACTIVATOR;
    public static final RegistryObject<Item> NANO_MACHINE = DAAMachineItems.NANO_MACHINE;
    public static final RegistryObject<Item> DUST_WITH_ENERGY = DAAMachineItems.DUST_WITH_ENERGY;
    public static final RegistryObject<Item> BOWL_WITH_DUST = DAAMachineItems.BOWL_WITH_DUST;
    public static final RegistryObject<Item> ENERGIZED_COBBLESTONE_BUCKET = DAAMachineItems.ENERGIZED_COBBLESTONE_BUCKET;
    public static final RegistryObject<Item> PURE_ENERGY = DAAMachineItems.PURE_ENERGY;
    public static final RegistryObject<Item> BASIC_MACHINE_FRAME = DAAMachineItems.BASIC_MACHINE_FRAME;
    public static final RegistryObject<Item> SOUL_OF_LIGHT = DAAMachineItems.SOUL_OF_LIGHT;
    public static final RegistryObject<Item> PURE_DARKNESS = DAAMachineItems.PURE_DARKNESS;
    public static final RegistryObject<Item> PLACEHOLDER = DAAMachineItems.PLACEHOLDER;
    public static final RegistryObject<Item> POSITION_SELECTOR = DAAMachineItems.POSITION_SELECTOR;
    public static final RegistryObject<Item> ROCK_SOLID = DAAMachineItems.ROCK_SOLID;
    public static final RegistryObject<Item> INDESTRUCTIBLE = DAAMachineItems.INDESTRUCTIBLE;

    // DAAMaterialItems
    public static final RegistryObject<Item> ASH_STEEL_INGOT = DAAMaterialItems.ASH_STEEL_INGOT;
    public static final RegistryObject<Item> TITANIUM_INGOT = DAAMaterialItems.TITANIUM_INGOT;
    public static final RegistryObject<Item> COPPER_INGOT = DAAMaterialItems.COPPER_INGOT;
    public static final RegistryObject<Item> TIN_INGOT = DAAMaterialItems.TIN_INGOT;
    public static final RegistryObject<Item> SILVER_INGOT = DAAMaterialItems.SILVER_INGOT;
    public static final RegistryObject<Item> LEAD_INGOT = DAAMaterialItems.LEAD_INGOT;
    public static final RegistryObject<Item> NICKEL_INGOT = DAAMaterialItems.NICKEL_INGOT;
    public static final RegistryObject<Item> ALUMINUM_INGOT = DAAMaterialItems.ALUMINUM_INGOT;
    public static final RegistryObject<Item> OSMIUM_INGOT = DAAMaterialItems.OSMIUM_INGOT;
    public static final RegistryObject<Item> URANIUM_INGOT = DAAMaterialItems.URANIUM_INGOT;
    public static final RegistryObject<Item> TUNGSTEN_INGOT = DAAMaterialItems.TUNGSTEN_INGOT;
    public static final RegistryObject<Item> TITANIUM_TUNGSTEN_ALLOY = DAAMaterialItems.TITANIUM_TUNGSTEN_ALLOY;
    public static final RegistryObject<Item> TITANIUM_ALUMINUM_ALLOY = DAAMaterialItems.TITANIUM_ALUMINUM_ALLOY;
    public static final RegistryObject<Item> BLOODY_FLINT = DAAMaterialItems.BLOODY_FLINT;
    public static final RegistryObject<Item> ELECTRON = DAAMaterialItems.ELECTRON;
    public static final RegistryObject<Item> CRUSHED_SEEDS = DAAMaterialItems.CRUSHED_SEEDS;
    public static final RegistryObject<Item> ASH = DAAMaterialItems.ASH;
    public static final RegistryObject<Item> WOOD_ASH = DAAMaterialItems.WOOD_ASH;
    public static final RegistryObject<Item> RAW_ASH_STEEL = DAAMaterialItems.RAW_ASH_STEEL;
    public static final RegistryObject<Item> BLOODY_FLINT_STICK = DAAMaterialItems.BLOODY_FLINT_STICK;
    public static final RegistryObject<Item> RAW_TITANIUM_SAND = DAAMaterialItems.RAW_TITANIUM_SAND;
    public static final RegistryObject<Item> TITANIUM_SAND = DAAMaterialItems.TITANIUM_SAND;
    public static final RegistryObject<Item> TITANIUM_SCRAP = DAAMaterialItems.TITANIUM_SCRAP;
    public static final RegistryObject<Item> TITANIUM_CHUNK = DAAMaterialItems.TITANIUM_CHUNK;
    public static final RegistryObject<Item> WOODEN_TOOL_HANDLE = DAAMaterialItems.WOODEN_TOOL_HANDLE;
    public static final RegistryObject<Item> CARBON_DUST = DAAMaterialItems.CARBON_DUST;
    public static final RegistryObject<Item> RAW_CARBON_FIBER = DAAMaterialItems.RAW_CARBON_FIBER;
    public static final RegistryObject<Item> CARBON_FIBER = DAAMaterialItems.CARBON_FIBER;
    public static final RegistryObject<Item> CARBON_FIBER_PLATE = DAAMaterialItems.CARBON_FIBER_PLATE;
    public static final RegistryObject<Item> TUNGSTEN_DUST = DAAMaterialItems.TUNGSTEN_DUST;
    public static final RegistryObject<Item> SODIUM_DUST = DAAMaterialItems.SODIUM_DUST;
    public static final RegistryObject<Item> MAGNET = DAAMaterialItems.MAGNET;
    public static final RegistryObject<Item> MANTLE_MIXTURE = DAAMaterialItems.MANTLE_MIXTURE;
    public static final RegistryObject<Item> ASH_STEEL_HEAVY_PLATE = DAAMaterialItems.ASH_STEEL_HEAVY_PLATE;
    public static final RegistryObject<Item> TITANIUM_HEAVY_PLATE = DAAMaterialItems.TITANIUM_HEAVY_PLATE;
    public static final RegistryObject<Item> GLASS_CONTAINER = DAAMaterialItems.GLASS_CONTAINER;
    public static final RegistryObject<Item> WATER_MISCIBLE_SOLVENTS = DAAMaterialItems.WATER_MISCIBLE_SOLVENTS;
    public static final RegistryObject<Item> GRIND_SOLVENTS = DAAMaterialItems.GRIND_SOLVENTS;
    public static final RegistryObject<Item> BASE_OIL = DAAMaterialItems.BASE_OIL;
    public static final RegistryObject<Item> CHLORINE = DAAMaterialItems.CHLORINE;
    public static final RegistryObject<Item> HYDROGEN = DAAMaterialItems.HYDROGEN;
    public static final RegistryObject<Item> OXYGEN = DAAMaterialItems.OXYGEN;
    public static final RegistryObject<Item> REDUCTANT = DAAMaterialItems.REDUCTANT;
    public static final RegistryObject<Item> GRAPHITE_ELECTRODE = DAAMaterialItems.GRAPHITE_ELECTRODE;
    public static final RegistryObject<Item> DARK_ENERGY_COLLAPSER = DAAMaterialItems.DARK_ENERGY_COLLAPSER;
    public static final RegistryObject<Item> IRON_GEAR = DAAMaterialItems.IRON_GEAR;
    public static final RegistryObject<Item> REDSTONE_GEAR = DAAMaterialItems.REDSTONE_GEAR;
    public static final RegistryObject<Item> ASH_STEEL_GEAR = DAAMaterialItems.ASH_STEEL_GEAR;
    public static final RegistryObject<Item> IRON_STRUCTURAL_COMPONENTS = DAAMaterialItems.IRON_STRUCTURAL_COMPONENTS;
    public static final RegistryObject<Item> ASH_STEEL_STRUCTURAL_COMPONENTS = DAAMaterialItems.ASH_STEEL_STRUCTURAL_COMPONENTS;
    public static final RegistryObject<Item> ASH_STEEL_CYLINDER = DAAMaterialItems.ASH_STEEL_CYLINDER;
    public static final RegistryObject<Item> ASH_STEEL_LEVER = DAAMaterialItems.ASH_STEEL_LEVER;
    public static final RegistryObject<Item> REACTION_CHAMBER = DAAMaterialItems.REACTION_CHAMBER;
    public static final RegistryObject<Item> POWER_CONVERTER = DAAMaterialItems.POWER_CONVERTER;
    public static final RegistryObject<Item> REDSTONE_VACUUM_TUBE = DAAMaterialItems.REDSTONE_VACUUM_TUBE;
    public static final RegistryObject<Item> IRON_SCRAP = DAAMaterialItems.IRON_SCRAP;
    public static final RegistryObject<Item> REDSTONE_SCRAP = DAAMaterialItems.REDSTONE_SCRAP;
    public static final RegistryObject<Item> ASH_STEEL_SCRAP = DAAMaterialItems.ASH_STEEL_SCRAP;
    public static final RegistryObject<Item> CARBON_FIBER_SCRAP = DAAMaterialItems.CARBON_FIBER_SCRAP;
    public static final RegistryObject<Item> TITANIUM_PLATE_SCRAP = DAAMaterialItems.TITANIUM_PLATE_SCRAP;

    // DAAReactorItems
    public static final RegistryObject<Item> REFINED_URANIUM = DAAReactorItems.REFINED_URANIUM;
    public static final RegistryObject<Item> EMPTY_FUEL_CONTAINER = DAAReactorItems.EMPTY_FUEL_CONTAINER;
    public static final RegistryObject<Item> U235_FUEL = DAAReactorItems.U235_FUEL;
    public static final RegistryObject<Item> U235_FUEL_METAL = DAAReactorItems.U235_FUEL_METAL;
    public static final RegistryObject<Item> U235_FUEL_LIFE = DAAReactorItems.U235_FUEL_LIFE;
    public static final RegistryObject<Item> U235_FUEL_ORDER = DAAReactorItems.U235_FUEL_ORDER;
    public static final RegistryObject<Item> U235_FUEL_FIRE = DAAReactorItems.U235_FUEL_FIRE;
    public static final RegistryObject<Item> U235_FUEL_EARTH = DAAReactorItems.U235_FUEL_EARTH;
    public static final RegistryObject<Item> BASIC_COOLING = DAAReactorItems.BASIC_COOLING;
    public static final RegistryObject<Item> COOLING_TIER_1 = DAAReactorItems.COOLING_TIER_1;
    public static final RegistryObject<Item> COOLING_TIER_2 = DAAReactorItems.COOLING_TIER_2;
    public static final RegistryObject<Item> COOLING_TIER_3 = DAAReactorItems.COOLING_TIER_3;

    private DAAItems() {
    }

    public static RegistryObject<Item> register(String name, Supplier<? extends Item> item) {
        return ITEMS.register(name, item);
    }

    public static RegistryObject<Item> simple(String name, int stackSize) {
        return register(name, () -> new Item(new Item.Properties().stacksTo(stackSize)));
    }

    static void bootstrap() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}