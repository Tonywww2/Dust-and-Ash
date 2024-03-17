package com.tonywww.dustandash.item;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DustAndAsh.MOD_ID);

    public static RegistryObject<CreativeModeTab> DUST_TAB = CREATIVE_MODE_TABS.register("dust_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.HAND_VACUUM.get()))
                    .title(Component.translatable("creativetab.dust_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.METAL_DUST.get());
                        pOutput.accept(ModItems.LIFE_DUST.get());
                        pOutput.accept(ModItems.ORDER_DUST.get());
                        pOutput.accept(ModItems.FIRE_DUST.get());
                        pOutput.accept(ModItems.EARTH_DUST.get());

                        pOutput.accept(ModItems.REPRODUCE_DUST.get());
                        pOutput.accept(ModItems.INHERIT_DUST.get());
                        pOutput.accept(ModItems.METAMORPHOSE_DUST.get());
                        pOutput.accept(ModItems.CRYSTALLIZE_DUST.get());
                        pOutput.accept(ModItems.SEEP_DUST.get());
                        pOutput.accept(ModItems.EXTINGUISH_DUST.get());
                        pOutput.accept(ModItems.ABSORB_DUST.get());
                        pOutput.accept(ModItems.SMELT_DUST.get());
                        pOutput.accept(ModItems.BLOCKING_DUST.get());
                        pOutput.accept(ModItems.LACERATE_DUST.get());

                        pOutput.accept(ModItems.DUST_WITH_ENERGY.get());
                        pOutput.accept(ModItems.BOWL_WITH_DUST.get());
                        pOutput.accept(ModItems.ENERGIZED_COBBLESTONE_BUCKET.get());

                        pOutput.accept(ModItems.HAND_VACUUM.get());
                        pOutput.accept(ModItems.IRON_VACUUM.get());
                        pOutput.accept(ModItems.SHARPEN_FLINT.get());
                        pOutput.accept(ModItems.FLINT_PICKAXE.get());

                        pOutput.accept(ModItems.ASH_STEEL_BOOTS.get());
                        pOutput.accept(ModItems.ASH_STEEL_CHESTPLATE.get());
                        pOutput.accept(ModItems.ASH_STEEL_LEGGING.get());
                        pOutput.accept(ModItems.ASH_STEEL_HELMET.get());

                        pOutput.accept(ModItems.ASH_STEEL_SWORD.get());
                        pOutput.accept(ModItems.ASH_STEEL_SHOVEL.get());
                        pOutput.accept(ModItems.ASH_STEEL_PICKAXE.get());
                        pOutput.accept(ModItems.ASH_STEEL_AXE.get());
                        pOutput.accept(ModItems.ASH_STEEL_HOE.get());

                        pOutput.accept(ModItems.TITANIUM_ALLOY_SWORD.get());
                        pOutput.accept(ModItems.TITANIUM_ALLOY_GREAT_SWORD.get());
                        pOutput.accept(ModItems.TITANIUM_ALLOY_PICKAXE.get());

                        pOutput.accept(ModItems.BLOODY_FLINT.get());
                        pOutput.accept(ModItems.BLOODY_FLINT_STICK.get());

                        pOutput.accept(ModItems.WIND_CRYSTAL.get());
                        pOutput.accept(ModItems.FIRE_CRYSTAL.get());
                        pOutput.accept(ModItems.SLIVER_CRYSTAL.get());
                        pOutput.accept(ModItems.PARASITISM_CRYSTAL.get());
                        pOutput.accept(ModItems.DEPRAVITY_CRYSTAL.get());
                        pOutput.accept(ModItems.APOTHEOSIS_CRYSTAL.get());
                        pOutput.accept(ModItems.ETERNAL_LIGHT_CRYSTAL.get());
                        pOutput.accept(ModItems.FOREVER_DARK_CRYSTAL.get());
                        pOutput.accept(ModItems.VOID_CRYSTAL.get());

                        pOutput.accept(ModItems.GALE_OTAIJUTSU.get());
                        pOutput.accept(ModItems.SUNBURN_MEGA_SWORD.get());

                    })
                    .build());

    public static RegistryObject<CreativeModeTab> ASH_TAB = CREATIVE_MODE_TABS.register("ash_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ASH.get()))
                    .title(Component.translatable("creativetab.ash_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.ASH.get());
                        pOutput.accept(ModItems.WOOD_ASH.get());

                        pOutput.accept(ModItems.RAIN_CRYSTAL.get());
                        pOutput.accept(ModItems.SUN_CRYSTAL.get());
                        pOutput.accept(ModItems.ECHO_ACTIVATOR.get());

                        pOutput.accept(ModItems.ASH_STEEL_INGOT.get());
                        pOutput.accept(ModItems.TITANIUM_INGOT.get());
                        pOutput.accept(ModItems.COPPER_INGOT.get());
                        pOutput.accept(ModItems.TIN_INGOT.get());
                        pOutput.accept(ModItems.SILVER_INGOT.get());
                        pOutput.accept(ModItems.LEAD_INGOT.get());
                        pOutput.accept(ModItems.NICKEL_INGOT.get());
                        pOutput.accept(ModItems.ALUMINUM_INGOT.get());
                        pOutput.accept(ModItems.OSMIUM_INGOT.get());
                        pOutput.accept(ModItems.URANIUM_INGOT.get());
                        pOutput.accept(ModItems.TUNGSTEN_INGOT.get());

                        pOutput.accept(ModItems.TITANIUM_TUNGSTEN_ALLOY.get());
                        pOutput.accept(ModItems.TITANIUM_ALUMINUM_ALLOY.get());

                        pOutput.accept(ModItems.PURE_ENERGY.get());
                        pOutput.accept(ModItems.BASIC_MACHINE_FRAME.get());
                        pOutput.accept(ModItems.SOUL_OF_LIGHT.get());
                        pOutput.accept(ModItems.PURE_DARKNESS.get());
                        pOutput.accept(ModItems.ELECTRON.get());
                        pOutput.accept(ModItems.CRUSHED_SEEDS.get());
                        pOutput.accept(ModItems.RAW_ASH_STEEL.get());
                        pOutput.accept(ModItems.RAW_TITANIUM_SAND.get());
                        pOutput.accept(ModItems.TITANIUM_SAND.get());
                        pOutput.accept(ModItems.TITANIUM_SCRAP.get());
                        pOutput.accept(ModItems.TITANIUM_CHUNK.get());
                        pOutput.accept(ModItems.WOODEN_TOOL_HANDLE.get());
                        pOutput.accept(ModItems.CARBON_DUST.get());
                        pOutput.accept(ModItems.RAW_CARBON_FIBER.get());
                        pOutput.accept(ModItems.CARBON_FIBER.get());
                        pOutput.accept(ModItems.CARBON_FIBER_PLATE.get());
                        pOutput.accept(ModItems.MAGNET.get());
                        pOutput.accept(ModItems.MANTLE_MIXTURE.get());
                        pOutput.accept(ModItems.TITANIUM_HEAVY_PLATE.get());

                        pOutput.accept(ModItems.GLASS_CONTAINER.get());
                        pOutput.accept(ModItems.WATER_MISCIBLE_SOLVENTS.get());
                        pOutput.accept(ModItems.GRIND_SOLVENTS.get());
                        pOutput.accept(ModItems.BASE_OIL.get());
                        pOutput.accept(ModItems.CHLORINE.get());
                        pOutput.accept(ModItems.HYDROGEN.get());
                        pOutput.accept(ModItems.OXYGEN.get());
                        pOutput.accept(ModItems.REDUCTANT.get());

                        pOutput.accept(ModItems.TUNGSTEN_DUST.get());
                        pOutput.accept(ModItems.SODIUM_DUST.get());
                        pOutput.accept(ModItems.GRAPHITE_ELECTRODE.get());

                        pOutput.accept(ModItems.GEAR_BOX.get());
                        pOutput.accept(ModItems.IRON_GEAR.get());
                        pOutput.accept(ModItems.REDSTONE_GEAR.get());
                        pOutput.accept(ModItems.ASH_STEEL_GEAR.get());

                        pOutput.accept(ModItems.IRON_SCRAP.get());
                        pOutput.accept(ModItems.REDSTONE_SCRAP.get());
                        pOutput.accept(ModItems.ASH_STEEL_SCRAP.get());
                        pOutput.accept(ModItems.CARBON_FIBER_SCRAP.get());
                        pOutput.accept(ModItems.TITANIUM_PLATE_SCRAP.get());


                    })
                    .build());

    public static RegistryObject<CreativeModeTab> BLOCKS_TAB = CREATIVE_MODE_TABS.register("blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.INTEGRATED_BLOCK.get()))
                    .title(Component.translatable("creativetab.blocks_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.DUST_SOURCE.get());
                        pOutput.accept(ModBlocks.DUST.get());
                        pOutput.accept(ModBlocks.INTEGRATED_BLOCK.get());
                        pOutput.accept(ModBlocks.ENERGIZED_COBBLESTONE.get());
                        pOutput.accept(ModBlocks.ASH_COLLECTOR.get());
                        pOutput.accept(ModBlocks.MILLING_MACHINE.get());
                        pOutput.accept(ModBlocks.CENTRIFUGE.get());
                        pOutput.accept(ModBlocks.IONIZER.get());
                        pOutput.accept(ModBlocks.LOG_PILE.get());
                        pOutput.accept(ModBlocks.BLOCK_OF_ASH_STEEL.get());
                        pOutput.accept(ModBlocks.TITANIUM_SAND_BLOCK.get());
                        pOutput.accept(ModBlocks.INTEGRATED_FRAME_1.get());
                        pOutput.accept(ModBlocks.INTEGRATED_FRAME_2.get());
                        pOutput.accept(ModBlocks.INTEGRATED_FRAME_3.get());
                        pOutput.accept(ModBlocks.COOLED_MAGMA_BLOCK.get());
                        pOutput.accept(ModBlocks.NETHERITE_MUD.get());
                        pOutput.accept(ModBlocks.COBBLESTONE_WITH_MOSS.get());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}
