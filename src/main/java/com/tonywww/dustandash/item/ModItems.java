package com.tonywww.dustandash.item;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.item.custom.EnergizedCobblestoneBucket;
import com.tonywww.dustandash.item.custom.HandVacuum;
import com.tonywww.dustandash.item.custom.IronVacuum;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DustAndAsh.MOD_ID);

    //Items
    //tools
    public static final RegistryObject<Item> HAND_VACUUM = ITEMS.register("hand_vacuum",
            () -> new HandVacuum(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(1)
                    .durability(24)
            ));

    public static final RegistryObject<Item> IRON_VACUUM = ITEMS.register("iron_vacuum",
            () -> new IronVacuum(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(1)
                    .durability(271)
            ));

    //materials
    public static final RegistryObject<Item> METAL_DUST = ITEMS.register("metal_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(48)
            ));

    public static final RegistryObject<Item> LIFE_DUST = ITEMS.register("life_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(48)
            ));

    public static final RegistryObject<Item> ORDER_DUST = ITEMS.register("order_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(48)
            ));

    public static final RegistryObject<Item> FIRE_DUST = ITEMS.register("fire_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(48)
            ));

    public static final RegistryObject<Item> EARTH_DUST = ITEMS.register("earth_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(48)
            ));

    public static final RegistryObject<Item> PROTON_ONE = ITEMS.register("proton_one",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(42)
            ));

    public static final RegistryObject<Item> PROTON_TWO = ITEMS.register("proton_two",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(42)
            ));

    public static final RegistryObject<Item> PROTON_THREE = ITEMS.register("proton_three",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(42)
            ));

    public static final RegistryObject<Item> NEUTRON_ONE = ITEMS.register("neutron_one",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(42)
            ));

    public static final RegistryObject<Item> NEUTRON_TWO = ITEMS.register("neutron_two",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(42)
            ));

    public static final RegistryObject<Item> NEUTRON_THREE = ITEMS.register("neutron_three",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(42)
            ));

    public static final RegistryObject<Item> ELECTRON = ITEMS.register("electron",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> REPRODUCE_DUST = ITEMS.register("reproduce_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(24)
            ));

    public static final RegistryObject<Item> INHERIT_DUST = ITEMS.register("inherit_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(24)
            ));

    public static final RegistryObject<Item> METAMORPHOSE_DUST = ITEMS.register("metamorphose_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(24)
            ));

    public static final RegistryObject<Item> CRYSTALLIZE_DUST = ITEMS.register("crystallize_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(24)
            ));

    public static final RegistryObject<Item> SEEP_DUST = ITEMS.register("seep_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(24)
            ));

    public static final RegistryObject<Item> EXTINGUISH_DUST = ITEMS.register("extinguish_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(24)
            ));

    public static final RegistryObject<Item> ABSORB_DUST = ITEMS.register("absorb_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(24)
            ));

    public static final RegistryObject<Item> SMELT_DUST = ITEMS.register("smelt_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(24)
            ));

    public static final RegistryObject<Item> BLOCKING_DUST = ITEMS.register("blocking_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(24)
            ));

    public static final RegistryObject<Item> LACERATE_DUST = ITEMS.register("lacerate_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(24)
            ));

    // advance material
    public static final RegistryObject<Item> DUST_WITH_ENERGY = ITEMS.register("dust_with_energy",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> BOWL_WITH_DUST = ITEMS.register("bowl_with_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(1)
            ));

    public static final RegistryObject<Item> ENERGIZED_COBBLESTONE_BUCKET = ITEMS.register("energized_cobblestone_bucket",
            () -> new EnergizedCobblestoneBucket(new Item.Properties()
                    .tab(ModItemGroup.DUST_TAB)
                    .stacksTo(1)
            ));

    // ash
    public static final RegistryObject<Item> ASH = ITEMS.register("ash",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.ASH_TAB)
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> WOOD_ASH = ITEMS.register("wood_ash",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.ASH_TAB)
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> ASH_STEEL_INGOT = ITEMS.register("ash_steel_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.ASH_TAB)
                    .stacksTo(32)
            ));

    public static final RegistryObject<Item> RAW_ASH_STEEL = ITEMS.register("raw_ash_steel",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.ASH_TAB)
                    .stacksTo(64)
            ));

    public static final RegistryObject<Item> IRON_SCRAP = ITEMS.register("iron_scrap",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.ASH_TAB)
                    .stacksTo(64)
            ));

    public static void register(IEventBus eventBus) {

        ITEMS.register(eventBus);

    }

}
