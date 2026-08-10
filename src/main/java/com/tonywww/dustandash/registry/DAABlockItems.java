package com.tonywww.dustandash.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public final class DAABlockItems {
    private static final List<RegistryObject<Item>> BLOCK_ITEMS = List.of(
            register("dust_source", DAABlocks.DUST_SOURCE),
            register("dust", DAABlocks.DUST),
            register("integrated_block", DAABlocks.INTEGRATED_BLOCK),
            register("energized_cobblestone", DAABlocks.ENERGIZED_COBBLESTONE),
            register("ash_collector", DAABlocks.ASH_COLLECTOR),
            register("milling_machine", DAABlocks.MILLING_MACHINE),
            register("centrifuge", DAABlocks.CENTRIFUGE),
            register("ionizer", DAABlocks.IONIZER),
            register("item_sender", DAABlocks.ITEM_SENDER),
            register("log_pile", DAABlocks.LOG_PILE),
            register("smooth_cobblestone", DAABlocks.SMOOTH_COBBLESTONE),
            register("block_of_ash_steel", DAABlocks.BLOCK_OF_ASH_STEEL),
            register("titanium_sand_block", DAABlocks.TITANIUM_SAND_BLOCK),
            register("integrated_frame_1", DAABlocks.INTEGRATED_FRAME_1),
            register("integrated_frame_2", DAABlocks.INTEGRATED_FRAME_2),
            register("integrated_frame_3", DAABlocks.INTEGRATED_FRAME_3),
            register("cooled_magma_block", DAABlocks.COOLED_MAGMA_BLOCK),
            register("netherite_mud", DAABlocks.NETHERITE_MUD),
            register("cobblestone_with_moss", DAABlocks.COBBLESTONE_WITH_MOSS),
            register("strengthened_cement", DAABlocks.STRENGTHENED_CEMENT),
            register("bracket", DAABlocks.BRACKET),
            register("fission_reactor_controller", DAABlocks.FISSION_REACTOR_CONTROLLER),
            register("fission_reactor_casing", DAABlocks.FISSION_REACTOR_CASING),
            register("fission_reactor_cooling_cell", DAABlocks.FISSION_REACTOR_COOLING_CELL),
            register("fission_reactor_fuel_cell", DAABlocks.FISSION_REACTOR_FUEL_CELL),
            register("fission_reactor_interface", DAABlocks.FISSION_REACTOR_INTERFACE)
    );

    private DAABlockItems() {
    }

    static void bootstrap() {
        if (BLOCK_ITEMS.size() != 26) {
            throw new IllegalStateException("Unexpected block item count: " + BLOCK_ITEMS.size());
        }
    }

    private static RegistryObject<Item> register(String name, Supplier<? extends Block> block) {
        return DAAItems.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}