package com.tonywww.dustandash.registry;

import com.tonywww.dustandash.item.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public final class DAAMachineItems {
    public static final RegistryObject<Item> RAIN_CRYSTAL = DAAItems.register("rain_crystal",
            () -> new RainCrystal(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUN_CRYSTAL = DAAItems.register("sun_crystal",
            () -> new SunCrystal(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ECHO_ACTIVATOR = DAAItems.register("echo_activator",
            () -> new EchoActivator(new Item.Properties().stacksTo(1).durability(2)));
    public static final RegistryObject<Item> NANO_MACHINE = DAAItems.register("nano_machine",
            () -> new NanoMachine(new Item.Properties().stacksTo(3)));

    public static final RegistryObject<Item> DUST_WITH_ENERGY = DAAItems.simple("dust_with_energy", 64);
    public static final RegistryObject<Item> BOWL_WITH_DUST = DAAItems.simple("bowl_with_dust", 16);
    public static final RegistryObject<Item> ENERGIZED_COBBLESTONE_BUCKET = DAAItems.simple("energized_cobblestone_bucket", 1);
    public static final RegistryObject<Item> PURE_ENERGY = DAAItems.simple("pure_energy", 64);
    public static final RegistryObject<Item> BASIC_MACHINE_FRAME = DAAItems.simple("basic_machine_frame", 2);
    public static final RegistryObject<Item> SOUL_OF_LIGHT = DAAItems.simple("soul_of_light", 2);
    public static final RegistryObject<Item> PURE_DARKNESS = DAAItems.simple("pure_darkness", 2);
    public static final RegistryObject<Item> PLACEHOLDER = DAAItems.simple("placeholder", 1);
    public static final RegistryObject<Item> POSITION_SELECTOR = DAAItems.register("position_selector",
            () -> new PositionSelector(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ROCK_SOLID = DAAItems.register("rock_solid",
            () -> new RockSolid(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> INDESTRUCTIBLE = DAAItems.register("indestructible",
            () -> new Indestructible(new Item.Properties().stacksTo(1)));

    private DAAMachineItems() {
    }

    static void bootstrap() {
    }
}