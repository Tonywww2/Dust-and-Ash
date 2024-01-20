package com.tonywww.dustandash.block;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.custom.*;
import com.tonywww.dustandash.item.ModItemGroup;
import com.tonywww.dustandash.item.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DustAndAsh.MOD_ID);

    //Blocks
    public static final RegistryObject<Block> DUST_SOURCE = registerBlocks("dust_source",
            () -> new DustSource(AbstractBlock.Properties
                    .of(Material.DIRT)
                    .harvestTool(ToolType.SHOVEL)
                    .requiresCorrectToolForDrops()
                    .strength(2f, 2f)
                    .randomTicks()
            ));

    public static final RegistryObject<Block> DUST = registerBlocks("dust",
            () -> new Dust(AbstractBlock.Properties
                    .of(Material.TOP_SNOW)
                    .harvestTool(ToolType.SHOVEL)
                    .strength(1f, 1f)
            ));

    public static final RegistryObject<Block> INTEGRATED_BLOCK = registerBlocks("integrated_block",
            () -> new IntegratedBlock(AbstractBlock.Properties
                    .of(Material.GLASS)
                    .harvestTool(ToolType.PICKAXE)
                    .strength(3f, 8f)
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> ENERGIZED_COBBLESTONE = registerBlocks("energized_cobblestone",
            () -> new EnergizedCobblestone(AbstractBlock.Properties
                    .of(Material.STONE)
                    .harvestTool(ToolType.PICKAXE)
                    .requiresCorrectToolForDrops()
                    .strength(2f, 5f)
                    .lightLevel((l) -> {
                        return 4;
                    })
            ));

    public static final RegistryObject<Block> ASH_COLLECTOR = registerBlocks("ash_collector",
            () -> new AshCollector(AbstractBlock.Properties
                    .of(Material.STONE)
                    .harvestTool(ToolType.PICKAXE)
                    .strength(2f, 8f)
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> MILLING_MACHINE = registerBlocks("milling_machine",
            () -> new MillingMachine(AbstractBlock.Properties
                    .of(Material.STONE)
                    .harvestTool(ToolType.PICKAXE)
                    .strength(2f, 8f)
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> CENTRIFUGE = registerBlocks("centrifuge",
            () -> new Centrifuge(AbstractBlock.Properties
                    .of(Material.HEAVY_METAL)
                    .harvestTool(ToolType.PICKAXE)
                    .strength(2f, 8f)
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> LOG_PILE = registerBlocks("log_pile",
            () -> new Block(AbstractBlock.Properties
                    .of(Material.WOOD)
                    .strength(2f)
                    .sound(SoundType.WOOD)
            ));

    public static final RegistryObject<Block> BLOCK_OF_ASH_STEEL = registerBlocks("block_of_ash_steel",
            () -> new Block(AbstractBlock.Properties
                    .of(Material.WOOD)
                    .strength(2f)
                    .sound(SoundType.WOOD)
            ));


    private static <T extends Block> RegistryObject<T> registerBlocks(String name, Supplier<T> block) {
        RegistryObject<T> tRegistryObject = BLOCKS.register(name, block);

        registerBlockItem(name, tRegistryObject);

        return tRegistryObject;

    }

    private static <T extends Block> void registerBlockItem(String name, Supplier<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(ModItemGroup.BLOCKS_TAB)));

    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);

    }

}
