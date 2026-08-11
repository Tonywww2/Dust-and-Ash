package com.tonywww.dustandash.data.datagen;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.tag.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                 ExistingFileHelper existingFileHelper) {

        super(output, lookupProvider, DustAndAsh.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Blocks.DUST_ABLE)
                .addTag(BlockTags.LOGS)
                .addTag(BlockTags.PLANKS)
                .addTag(BlockTags.SLABS)
                .addTag(BlockTags.WOOL)
                .addTag(BlockTags.STONE_BRICKS)
                .addTag(Tags.Blocks.COBBLESTONE)
                .addTag(Tags.Blocks.GRAVEL)
                .add(Blocks.STONE)
                .add(DAABlocks.SMOOTH_COBBLESTONE.get());

        tag(ModTags.Blocks.NOT_DUST_ABLE)
                .add(Blocks.GRAY_STAINED_GLASS)
                .add(Blocks.GRAY_CONCRETE);

        tag(ModTags.Blocks.FISSION_REACTOR_WALL)
                .addTag(Tags.Blocks.STORAGE_BLOCKS);

        tag(BlockTags.MOSS_REPLACEABLE)
                .add(DAABlocks.COBBLESTONE_WITH_MOSS.get());

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(DAABlocks.LOG_PILE.get());

        // Kept empty to mirror the previous hand-written data/minecraft/tags/blocks/mineable/hoe.json
        tag(BlockTags.MINEABLE_WITH_HOE);

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(DAABlocks.INTEGRATED_BLOCK.get())
                .add(DAABlocks.ENERGIZED_COBBLESTONE.get())
                .add(DAABlocks.ASH_COLLECTOR.get())
                .add(DAABlocks.MILLING_MACHINE.get())
                .add(DAABlocks.CENTRIFUGE.get())
                .add(DAABlocks.IONIZER.get())
                .add(DAABlocks.BLOCK_OF_ASH_STEEL.get())
                .add(DAABlocks.INTEGRATED_FRAME_1.get())
                .add(DAABlocks.INTEGRATED_FRAME_2.get())
                .add(DAABlocks.INTEGRATED_FRAME_3.get())
                .add(DAABlocks.COOLED_MAGMA_BLOCK.get())
                .add(DAABlocks.COBBLESTONE_WITH_MOSS.get())
                .add(DAABlocks.ITEM_SENDER.get())
                .add(DAABlocks.STRENGTHENED_CEMENT.get())
                .add(DAABlocks.FISSION_REACTOR_CONTROLLER.get())
                .add(DAABlocks.FISSION_REACTOR_CASING.get())
                .add(DAABlocks.FISSION_REACTOR_COOLING_CELL.get())
                .add(DAABlocks.FISSION_REACTOR_FUEL_CELL.get())
                .add(DAABlocks.FISSION_REACTOR_INTERFACE.get())
                .add(DAABlocks.SMOOTH_COBBLESTONE.get());

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(DAABlocks.DUST_SOURCE.get())
                .add(DAABlocks.TITANIUM_SAND_BLOCK.get())
                .add(DAABlocks.NETHERITE_MUD.get());
    }
}
