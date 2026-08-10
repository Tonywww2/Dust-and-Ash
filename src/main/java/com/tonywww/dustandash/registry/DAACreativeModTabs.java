package com.tonywww.dustandash.registry;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public final class DAACreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DustAndAsh.MOD_ID);

    private static final List<Supplier<? extends ItemLike>> DUST_TAB_ITEMS = List.of(
            DAAItems.METAL_DUST,
            DAAItems.LIFE_DUST,
            DAAItems.ORDER_DUST,
            DAAItems.FIRE_DUST,
            DAAItems.EARTH_DUST,
            DAAItems.REPRODUCE_DUST,
            DAAItems.INHERIT_DUST,
            DAAItems.METAMORPHOSE_DUST,
            DAAItems.CRYSTALLIZE_DUST,
            DAAItems.SEEP_DUST,
            DAAItems.EXTINGUISH_DUST,
            DAAItems.ABSORB_DUST,
            DAAItems.SMELT_DUST,
            DAAItems.BLOCKING_DUST,
            DAAItems.LACERATE_DUST,
            DAAItems.DUST_WITH_ENERGY,
            DAAItems.BOWL_WITH_DUST,
            DAAItems.ENERGIZED_COBBLESTONE_BUCKET,
            DAAItems.HAND_VACUUM,
            DAAItems.IRON_VACUUM,
            DAAItems.SHARPEN_FLINT,
            DAAItems.FLINT_PICKAXE,
            DAAItems.ASH_STEEL_BOOTS,
            DAAItems.ASH_STEEL_CHESTPLATE,
            DAAItems.ASH_STEEL_LEGGING,
            DAAItems.ASH_STEEL_HELMET,
            DAAItems.ASH_STEEL_SWORD,
            DAAItems.ASH_STEEL_SHOVEL,
            DAAItems.ASH_STEEL_PICKAXE,
            DAAItems.ASH_STEEL_AXE,
            DAAItems.ASH_STEEL_HOE,
            DAAItems.TITANIUM_ALLOY_SWORD,
            DAAItems.TITANIUM_ALLOY_GREAT_SWORD,
            DAAItems.TITANIUM_ALLOY_PICKAXE,
            DAAItems.BLOODY_FLINT,
            DAAItems.BLOODY_FLINT_STICK,
            DAAItems.METAL_GEM,
            DAAItems.LIFE_GEM,
            DAAItems.ORDER_GEM,
            DAAItems.FIRE_GEM,
            DAAItems.EARTH_GEM,
            DAAItems.WIND_CRYSTAL,
            DAAItems.FIRE_CRYSTAL,
            DAAItems.SILVER_CRYSTAL,
            DAAItems.PARASITISM_CRYSTAL,
            DAAItems.DEPRAVITY_CRYSTAL,
            DAAItems.APOTHEOSIS_CRYSTAL,
            DAAItems.ETERNAL_LIGHT_CRYSTAL,
            DAAItems.FOREVER_DARK_CRYSTAL,
            DAAItems.VOID_CRYSTAL,
            DAAItems.GALE_OTAIJUTSU,
            DAAItems.SUNBURN_MEGA_SWORD,
            DAAItems.WHITE_LIGHTNING,
            DAAItems.LORD_OF_BLOOD,
            DAAItems.ROTTEN_BLADE,
            DAAItems.JUDGEMENT
    );

    private static final List<Supplier<? extends ItemLike>> ASH_TAB_ITEMS = List.of(
            DAAItems.ASH,
            DAAItems.WOOD_ASH,
            DAAItems.RAIN_CRYSTAL,
            DAAItems.SUN_CRYSTAL,
            DAAItems.ECHO_ACTIVATOR,
            DAAItems.ASH_STEEL_INGOT,
            DAAItems.TITANIUM_INGOT,
            DAAItems.COPPER_INGOT,
            DAAItems.TIN_INGOT,
            DAAItems.SILVER_INGOT,
            DAAItems.LEAD_INGOT,
            DAAItems.NICKEL_INGOT,
            DAAItems.ALUMINUM_INGOT,
            DAAItems.OSMIUM_INGOT,
            DAAItems.URANIUM_INGOT,
            DAAItems.TUNGSTEN_INGOT,
            DAAItems.TITANIUM_TUNGSTEN_ALLOY,
            DAAItems.TITANIUM_ALUMINUM_ALLOY,
            DAAItems.PURE_ENERGY,
            DAAItems.BASIC_MACHINE_FRAME,
            DAAItems.SOUL_OF_LIGHT,
            DAAItems.PURE_DARKNESS,
            DAAItems.ELECTRON,
            DAAItems.CRUSHED_SEEDS,
            DAAItems.RAW_ASH_STEEL,
            DAAItems.RAW_TITANIUM_SAND,
            DAAItems.TITANIUM_SAND,
            DAAItems.TITANIUM_SCRAP,
            DAAItems.TITANIUM_CHUNK,
            DAAItems.WOODEN_TOOL_HANDLE,
            DAAItems.CARBON_DUST,
            DAAItems.RAW_CARBON_FIBER,
            DAAItems.CARBON_FIBER,
            DAAItems.CARBON_FIBER_PLATE,
            DAAItems.MAGNET,
            DAAItems.MANTLE_MIXTURE,
            DAAItems.ASH_STEEL_HEAVY_PLATE,
            DAAItems.TITANIUM_HEAVY_PLATE,
            DAAItems.PLACEHOLDER,
            DAAItems.POSITION_SELECTOR,
            DAAItems.GLASS_CONTAINER,
            DAAItems.WATER_MISCIBLE_SOLVENTS,
            DAAItems.GRIND_SOLVENTS,
            DAAItems.BASE_OIL,
            DAAItems.CHLORINE,
            DAAItems.HYDROGEN,
            DAAItems.OXYGEN,
            DAAItems.REDUCTANT,
            DAAItems.TUNGSTEN_DUST,
            DAAItems.SODIUM_DUST,
            DAAItems.GRAPHITE_ELECTRODE,
            DAAItems.DARK_ENERGY_COLLAPSER,
            DAAItems.IRON_GEAR,
            DAAItems.REDSTONE_GEAR,
            DAAItems.ASH_STEEL_GEAR,
            DAAItems.IRON_STRUCTURAL_COMPONENTS,
            DAAItems.ASH_STEEL_STRUCTURAL_COMPONENTS,
            DAAItems.ASH_STEEL_CYLINDER,
            DAAItems.ASH_STEEL_LEVER,
            DAAItems.REACTION_CHAMBER,
            DAAItems.POWER_CONVERTER,
            DAAItems.REDSTONE_VACUUM_TUBE,
            DAAItems.IRON_SCRAP,
            DAAItems.REDSTONE_SCRAP,
            DAAItems.ASH_STEEL_SCRAP,
            DAAItems.CARBON_FIBER_SCRAP,
            DAAItems.TITANIUM_PLATE_SCRAP,
            DAAItems.ROCK_SOLID,
            DAAItems.INDESTRUCTIBLE,
            DAAItems.NANO_MACHINE,
            DAAItems.REFINED_URANIUM,
            DAAItems.EMPTY_FUEL_CONTAINER,
            DAAItems.U235_FUEL,
            DAAItems.U235_FUEL_METAL,
            DAAItems.U235_FUEL_LIFE,
            DAAItems.U235_FUEL_ORDER,
            DAAItems.U235_FUEL_FIRE,
            DAAItems.U235_FUEL_EARTH,
            DAAItems.BASIC_COOLING,
            DAAItems.COOLING_TIER_1,
            DAAItems.COOLING_TIER_2,
            DAAItems.COOLING_TIER_3
    );

    private static final List<Supplier<? extends ItemLike>> BLOCKS_TAB_ITEMS = List.of(
            DAABlocks.DUST_SOURCE,
            DAABlocks.DUST,
            DAABlocks.INTEGRATED_BLOCK,
            DAABlocks.ENERGIZED_COBBLESTONE,
            DAABlocks.ASH_COLLECTOR,
            DAABlocks.MILLING_MACHINE,
            DAABlocks.CENTRIFUGE,
            DAABlocks.IONIZER,
            DAABlocks.ITEM_SENDER,
            DAABlocks.LOG_PILE,
            DAABlocks.SMOOTH_COBBLESTONE,
            DAABlocks.BLOCK_OF_ASH_STEEL,
            DAABlocks.TITANIUM_SAND_BLOCK,
            DAABlocks.INTEGRATED_FRAME_1,
            DAABlocks.INTEGRATED_FRAME_2,
            DAABlocks.INTEGRATED_FRAME_3,
            DAABlocks.COOLED_MAGMA_BLOCK,
            DAABlocks.NETHERITE_MUD,
            DAABlocks.COBBLESTONE_WITH_MOSS,
            DAABlocks.STRENGTHENED_CEMENT,
            DAABlocks.BRACKET,
            DAABlocks.FISSION_REACTOR_CONTROLLER,
            DAABlocks.FISSION_REACTOR_CASING,
            DAABlocks.FISSION_REACTOR_COOLING_CELL,
            DAABlocks.FISSION_REACTOR_FUEL_CELL,
            DAABlocks.FISSION_REACTOR_INTERFACE
    );

    public static final RegistryObject<CreativeModeTab> DUST_TAB = CREATIVE_MODE_TABS.register("dust_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(DAAItems.HAND_VACUUM.get()))
                    .title(Component.translatable("creativetab.dust_tab"))
                    .displayItems((parameters, output) -> acceptAll(output, DUST_TAB_ITEMS))
                    .build());

    public static final RegistryObject<CreativeModeTab> ASH_TAB = CREATIVE_MODE_TABS.register("ash_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(DAAItems.ASH.get()))
                    .title(Component.translatable("creativetab.ash_tab"))
                    .displayItems((parameters, output) -> acceptAll(output, ASH_TAB_ITEMS))
                    .build());

    public static final RegistryObject<CreativeModeTab> BLOCKS_TAB = CREATIVE_MODE_TABS.register("blocks_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(DAABlocks.INTEGRATED_BLOCK.get()))
                    .title(Component.translatable("creativetab.blocks_tab"))
                    .displayItems((parameters, output) -> acceptAll(output, BLOCKS_TAB_ITEMS))
                    .build());

    private DAACreativeModTabs() {
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    private static void acceptAll(CreativeModeTab.Output output,
                                  List<Supplier<? extends ItemLike>> entries) {
        entries.forEach(entry -> output.accept(entry.get()));
    }
}