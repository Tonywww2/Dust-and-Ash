package com.tonywww.dustandash.registry;

import com.tonywww.dustandash.item.FissionReactor.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public final class DAAReactorItems {
    public static final RegistryObject<Item> REFINED_URANIUM = DAAItems.simple("refined_uranium", 16);
    public static final RegistryObject<Item> EMPTY_FUEL_CONTAINER = DAAItems.simple("empty_fuel_container", 16);
    public static final RegistryObject<Item> U235_FUEL = DAAItems.register("u235_fuel",
            () -> new U235Fuel(new Item.Properties().stacksTo(1).durability(6400)));
    public static final RegistryObject<Item> U235_FUEL_METAL = DAAItems.register("u235_fuel_metal",
            () -> new U235FuelMetal(new Item.Properties().stacksTo(1).durability(4800)));
    public static final RegistryObject<Item> U235_FUEL_LIFE = DAAItems.register("u235_fuel_life",
            () -> new U235FuelLife(new Item.Properties().stacksTo(1).durability(9600)));
    public static final RegistryObject<Item> U235_FUEL_ORDER = DAAItems.register("u235_fuel_order",
            () -> new U235FuelOrder(new Item.Properties().stacksTo(1).durability(6400)));
    public static final RegistryObject<Item> U235_FUEL_FIRE = DAAItems.register("u235_fuel_fire",
            () -> new U235FuelFire(new Item.Properties().stacksTo(1).durability(6400)));
    public static final RegistryObject<Item> U235_FUEL_EARTH = DAAItems.register("u235_fuel_earth",
            () -> new U235FuelEarth(new Item.Properties().stacksTo(1).durability(8000)));
    public static final RegistryObject<Item> BASIC_COOLING = DAAItems.register("basic_cooling",
            () -> new BasicCooling(new Item.Properties().stacksTo(1).durability(6400)));
    public static final RegistryObject<Item> COOLING_TIER_1 = DAAItems.register("cooling_tier_1",
            () -> new CoolingTier1(new Item.Properties().stacksTo(1).durability(12800)));
    public static final RegistryObject<Item> COOLING_TIER_2 = DAAItems.register("cooling_tier_2",
            () -> new CoolingTier2(new Item.Properties().stacksTo(1).durability(19200)));
    public static final RegistryObject<Item> COOLING_TIER_3 = DAAItems.register("cooling_tier_3",
            () -> new CoolingTier3(new Item.Properties().stacksTo(1).durability(25600)));

    private DAAReactorItems() {
    }

    static void bootstrap() {
    }
}