package com.tonywww.dustandash.data.datagen.loot;

import com.tonywww.dustandash.registry.DAABlocks;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.data.loot.BlockLootSubProvider;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModBlockLootSubProvider extends BlockLootSubProvider {

    private static final List<Block> DROP_SELF_BLOCKS = List.of(
            DAABlocks.ASH_COLLECTOR.get(),
            DAABlocks.BLOCK_OF_ASH_STEEL.get(),
            DAABlocks.CENTRIFUGE.get(),
            DAABlocks.COBBLESTONE_WITH_MOSS.get(),
            DAABlocks.COOLED_MAGMA_BLOCK.get(),
            DAABlocks.DUST_SOURCE.get(),
            DAABlocks.ENERGIZED_COBBLESTONE.get(),
            DAABlocks.FISSION_REACTOR_CASING.get(),
            DAABlocks.FISSION_REACTOR_CONTROLLER.get(),
            DAABlocks.FISSION_REACTOR_COOLING_CELL.get(),
            DAABlocks.FISSION_REACTOR_FUEL_CELL.get(),
            DAABlocks.FISSION_REACTOR_INTERFACE.get(),
            DAABlocks.INTEGRATED_BLOCK.get(),
            DAABlocks.INTEGRATED_FRAME_1.get(),
            DAABlocks.INTEGRATED_FRAME_2.get(),
            DAABlocks.INTEGRATED_FRAME_3.get(),
            DAABlocks.IONIZER.get(),
            DAABlocks.ITEM_SENDER.get(),
            DAABlocks.LOG_PILE.get(),
            DAABlocks.MILLING_MACHINE.get(),
            DAABlocks.NETHERITE_MUD.get(),
            DAABlocks.SMOOTH_COBBLESTONE.get(),
            DAABlocks.STRENGTHENED_CEMENT.get(),
            DAABlocks.TITANIUM_SAND_BLOCK.get()
    );

    public ModBlockLootSubProvider() {
        // No blocks are explosion-resistant here; instead every generated block is registered as
        // "resistant" so BlockLootSubProvider#dropSelf produces the same minimal json (no explosion_decay
        // function) as the hand-written loot tables it replaces.
        super(explosionResistantSelf(), FeatureFlags.REGISTRY.allFlags());
    }

    private static Set<Item> explosionResistantSelf() {

        return DROP_SELF_BLOCKS.stream().map(Block::asItem).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    protected void generate() {
        DROP_SELF_BLOCKS.forEach(this::dropSelf);

        // The dust block has no drop of its own (matches the previous empty loot_tables/blocks/dust.json).
        add(DAABlocks.DUST.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        List<Block> known = new java.util.ArrayList<>(DROP_SELF_BLOCKS);
        known.add(DAABlocks.DUST.get());
        return known;
    }
}
