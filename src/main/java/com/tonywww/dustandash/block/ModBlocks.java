package com.tonywww.dustandash.block;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.custom.*;
import com.tonywww.dustandash.item.ModItemGroup;
import com.tonywww.dustandash.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DustAndAsh.MOD_ID);

    //Blocks
    public static final RegistryObject<Block> DUST_SOURCE = registerBlocks("dust_source",
            () -> new DustSource(BlockBehaviour.Properties
                    .of(Material.DIRT)
                    .requiresCorrectToolForDrops()
                    .strength(2f, 2f)
                    .randomTicks()
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> DUST = registerBlocks("dust",
            () -> new Dust(BlockBehaviour.Properties
                    .of(Material.TOP_SNOW)
                    .strength(1f, 1f)
                    .noOcclusion()
                    .noCollission()
            ));

    public static final RegistryObject<Block> INTEGRATED_BLOCK = registerBlocks("integrated_block",
            () -> new IntegratedBlock(BlockBehaviour.Properties
                    .of(Material.GLASS)
                    .strength(3f, 8f)
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> ENERGIZED_COBBLESTONE = registerBlocks("energized_cobblestone",
            () -> new Block(BlockBehaviour.Properties
                    .of(Material.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(2f, 5f)
                    .lightLevel((l) -> {
                        return 4;
                    })
            ));

    public static final RegistryObject<Block> ASH_COLLECTOR = registerBlocks("ash_collector",
            () -> new AshCollector(BlockBehaviour.Properties
                    .of(Material.STONE)
                    .strength(2f, 8f)
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> MILLING_MACHINE = registerBlocks("milling_machine",
            () -> new MillingMachine(BlockBehaviour.Properties
                    .of(Material.STONE)
                    .strength(2f, 8f)
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> CENTRIFUGE = registerBlocks("centrifuge",
            () -> new Centrifuge(BlockBehaviour.Properties
                    .of(Material.HEAVY_METAL)
                    .strength(2f, 8f)
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> IONIZER = registerBlocks("ionizer",
            () -> new Ionizer(BlockBehaviour.Properties
                    .of(Material.HEAVY_METAL)
                    .strength(2f, 8f)
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> LOG_PILE = registerBlocks("log_pile",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties
                    .of(Material.WOOD)
                    .strength(2f)
                    .sound(SoundType.WOOD)
            ));

    public static final RegistryObject<Block> BLOCK_OF_ASH_STEEL = registerBlocks("block_of_ash_steel",
            () -> new Block(BlockBehaviour.Properties
                    .of(Material.WOOD)
                    .strength(2f)
                    .sound(SoundType.WOOD)
            ));

    public static final RegistryObject<Block> TITANIUM_SAND_BLOCK = registerBlocks("titanium_sand_block",
            () -> new Block(BlockBehaviour.Properties
                    .of(Material.STONE)
                    .strength(1f, 6f)
                    .sound(SoundType.STONE)
            ));

    public static final RegistryObject<Block> INTEGRATED_FRAME_1 = registerBlocks("integrated_frame_1",
            () -> new Block(BlockBehaviour.Properties
                    .of(Material.STONE, MaterialColor.COLOR_BLACK)
                    .strength(15f, 1200f)
                    .sound(SoundType.STONE)
            ));

    public static final RegistryObject<Block> INTEGRATED_FRAME_2 = registerBlocks("integrated_frame_2",
            () -> new Block(BlockBehaviour.Properties
                    .of(Material.STONE, MaterialColor.COLOR_BLACK)
                    .strength(30f, 1200f)
                    .sound(SoundType.STONE)
            ));

    public static final RegistryObject<Block> INTEGRATED_FRAME_3 = registerBlocks("integrated_frame_3",
            () -> new Block(BlockBehaviour.Properties
                    .of(Material.STONE, MaterialColor.COLOR_BLACK)
                    .strength(45f, 1200f)
                    .sound(SoundType.STONE)
            ));

    public static final RegistryObject<Block> COOLED_MAGMA_BLOCK = registerBlocks("cooled_magma_block",
            () -> new Block(BlockBehaviour.Properties
                    .of(Material.STONE, MaterialColor.NETHER)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)
            ));

    public static final RegistryObject<Block> NETHERITE_MUD = registerBlocks("netherite_mud",
            () -> new Block(BlockBehaviour.Properties
                    .of(Material.DIRT, MaterialColor.NETHER)
                    .requiresCorrectToolForDrops()
                    .strength(50f, 1200f)
            ));

    public static final RegistryObject<Block> COBBLESTONE_WITH_MOSS = registerBlocks("cobblestone_with_moss",
            () -> new Block(BlockBehaviour.Properties
                    .of(Material.STONE, MaterialColor.NETHER)
                    .requiresCorrectToolForDrops()
                    .strength(1f, 2f)
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
