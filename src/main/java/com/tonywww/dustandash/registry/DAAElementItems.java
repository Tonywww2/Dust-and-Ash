package com.tonywww.dustandash.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public final class DAAElementItems {
    public static final RegistryObject<Item> METAL_DUST = DAAItems.simple("metal_dust", 48);
    public static final RegistryObject<Item> LIFE_DUST = DAAItems.simple("life_dust", 48);
    public static final RegistryObject<Item> ORDER_DUST = DAAItems.simple("order_dust", 48);
    public static final RegistryObject<Item> FIRE_DUST = DAAItems.simple("fire_dust", 48);
    public static final RegistryObject<Item> EARTH_DUST = DAAItems.simple("earth_dust", 48);
    public static final RegistryObject<Item> REPRODUCE_DUST = DAAItems.simple("reproduce_dust", 24);
    public static final RegistryObject<Item> INHERIT_DUST = DAAItems.simple("inherit_dust", 24);
    public static final RegistryObject<Item> METAMORPHOSE_DUST = DAAItems.simple("metamorphose_dust", 24);
    public static final RegistryObject<Item> CRYSTALLIZE_DUST = DAAItems.simple("crystallize_dust", 24);
    public static final RegistryObject<Item> SEEP_DUST = DAAItems.simple("seep_dust", 24);
    public static final RegistryObject<Item> EXTINGUISH_DUST = DAAItems.simple("extinguish_dust", 24);
    public static final RegistryObject<Item> ABSORB_DUST = DAAItems.simple("absorb_dust", 24);
    public static final RegistryObject<Item> SMELT_DUST = DAAItems.simple("smelt_dust", 24);
    public static final RegistryObject<Item> BLOCKING_DUST = DAAItems.simple("blocking_dust", 24);
    public static final RegistryObject<Item> LACERATE_DUST = DAAItems.simple("lacerate_dust", 24);

    public static final RegistryObject<Item> METAL_GEM = DAAItems.simple("metal_gem", 16);
    public static final RegistryObject<Item> LIFE_GEM = DAAItems.simple("life_gem", 16);
    public static final RegistryObject<Item> ORDER_GEM = DAAItems.simple("order_gem", 16);
    public static final RegistryObject<Item> FIRE_GEM = DAAItems.simple("fire_gem", 16);
    public static final RegistryObject<Item> EARTH_GEM = DAAItems.simple("earth_gem", 16);
    public static final RegistryObject<Item> WIND_CRYSTAL = DAAItems.simple("wind_crystal", 4);
    public static final RegistryObject<Item> FIRE_CRYSTAL = DAAItems.simple("fire_crystal", 4);
    public static final RegistryObject<Item> SILVER_CRYSTAL = DAAItems.simple("silver_crystal", 4);
    public static final RegistryObject<Item> PARASITISM_CRYSTAL = DAAItems.simple("parasitism_crystal", 4);
    public static final RegistryObject<Item> DEPRAVITY_CRYSTAL = DAAItems.simple("depravity_crystal", 4);
    public static final RegistryObject<Item> APOTHEOSIS_CRYSTAL = DAAItems.simple("apotheosis_crystal", 4);
    public static final RegistryObject<Item> ETERNAL_LIGHT_CRYSTAL = DAAItems.simple("eternal_light_crystal", 4);
    public static final RegistryObject<Item> FOREVER_DARK_CRYSTAL = DAAItems.simple("forever_dark_crystal", 4);
    public static final RegistryObject<Item> VOID_CRYSTAL = DAAItems.simple("void_crystal", 4);

    private DAAElementItems() {
    }

    static void bootstrap() {
    }
}