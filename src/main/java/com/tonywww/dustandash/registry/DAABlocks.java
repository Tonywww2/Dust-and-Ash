package com.tonywww.dustandash.registry;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.custom.*;
import com.tonywww.dustandash.block.custom.FissionReactor.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class DAABlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DustAndAsh.MOD_ID);

    //Blocks
        public static final RegistryObject<Block> DUST_SOURCE = registerBlock("dust_source",
            () -> new DustSource(BlockBehaviour.Properties
                    .copy(Blocks.DIRT)
                    .requiresCorrectToolForDrops()
                    .strength(2f, 2f)
                    .randomTicks()
                    .noOcclusion()
            ));

        public static final RegistryObject<Block> DUST = registerBlock("dust",
            () -> new Dust(BlockBehaviour.Properties
                    .copy(Blocks.SNOW)
                    .strength(1f, 1f)
                    .noOcclusion()
                    .noCollission()
            ));

        public static final RegistryObject<Block> INTEGRATED_BLOCK = registerBlock("integrated_block",
            () -> new IntegratedBlock(BlockBehaviour.Properties
                    .copy(Blocks.GLASS)
                    .strength(3f, 8f)
                    .noOcclusion()
            ));

        public static final RegistryObject<Block> ENERGIZED_COBBLESTONE = registerBlock("energized_cobblestone",
            () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(2f, 5f)
                    .lightLevel((l) -> {
                        return 4;
                    })
            ));

        public static final RegistryObject<Block> ASH_COLLECTOR = registerBlock("ash_collector",
            () -> new AshCollector(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .strength(2f, 8f)
                    .noOcclusion()
            ));

        public static final RegistryObject<Block> MILLING_MACHINE = registerBlock("milling_machine",
            () -> new MillingMachine(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .strength(2f, 8f)
                    .noOcclusion()
            ));

        public static final RegistryObject<Block> CENTRIFUGE = registerBlock("centrifuge",
            () -> new Centrifuge(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .strength(2f, 8f)
                    .noOcclusion()
            ));

        public static final RegistryObject<Block> IONIZER = registerBlock("ionizer",
            () -> new Ionizer(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .strength(2f, 8f)
                    .noOcclusion()
            ));

        public static final RegistryObject<Block> ITEM_SENDER = registerBlock("item_sender",
            () -> new ItemSender(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .strength(2f, 8f)
                    .noOcclusion()
            ));

        public static final RegistryObject<Block> LOG_PILE = registerBlock("log_pile",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .strength(2f)
                    .sound(SoundType.WOOD)
            ));

        public static final RegistryObject<Block> SMOOTH_COBBLESTONE = registerBlock("smooth_cobblestone",
            () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
            ));

        public static final RegistryObject<Block> BLOCK_OF_ASH_STEEL = registerBlock("block_of_ash_steel",
            () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .strength(2f)
                    .sound(SoundType.WOOD)
            ));

        public static final RegistryObject<Block> TITANIUM_SAND_BLOCK = registerBlock("titanium_sand_block",
            () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .strength(1f, 6f)
                    .sound(SoundType.STONE)
            ));

        public static final RegistryObject<Block> INTEGRATED_FRAME_1 = registerBlock("integrated_frame_1",
            () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.IRON_BLOCK)
                    .strength(15f, 1200f)
                    .sound(SoundType.STONE)
            ));

        public static final RegistryObject<Block> INTEGRATED_FRAME_2 = registerBlock("integrated_frame_2",
            () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.IRON_BLOCK)
                    .strength(30f, 1200f)
                    .sound(SoundType.STONE)
            ));

        public static final RegistryObject<Block> INTEGRATED_FRAME_3 = registerBlock("integrated_frame_3",
            () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.IRON_BLOCK)
                    .strength(45f, 1200f)
                    .sound(SoundType.STONE)
            ));

        public static final RegistryObject<Block> COOLED_MAGMA_BLOCK = registerBlock("cooled_magma_block",
            () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)
            ));

        public static final RegistryObject<Block> NETHERITE_MUD = registerBlock("netherite_mud",
            () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(50f, 1200f)
            ));

        public static final RegistryObject<Block> COBBLESTONE_WITH_MOSS = registerBlock("cobblestone_with_moss",
            () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(1f, 2f)
            ));

        public static final RegistryObject<Block> STRENGTHENED_CEMENT = registerBlock("strengthened_cement",
            () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .strength(2f, 4f)
                    .noOcclusion()
            ));

        public static final RegistryObject<Block> BRACKET = registerBlock("bracket",
            () -> new Bracket(BlockBehaviour.Properties
                    .copy(Blocks.IRON_BLOCK)
                    .strength(2f, 4f)
                    .noOcclusion()
            ));

    // Fission Reactor
        public static final RegistryObject<Block> FISSION_REACTOR_CONTROLLER = registerBlock("fission_reactor_controller",
            () -> new FissionReactorController(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .strength(2f, 8f)
                    .noOcclusion()
            ));

        public static final RegistryObject<Block> FISSION_REACTOR_CASING = registerBlock("fission_reactor_casing",
            () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .strength(2f, 4f)
            ));

        public static final RegistryObject<Block> FISSION_REACTOR_COOLING_CELL = registerBlock("fission_reactor_cooling_cell",
            () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .strength(2f, 4f)
                    .noOcclusion()
            ));

        public static final RegistryObject<Block> FISSION_REACTOR_FUEL_CELL = registerBlock("fission_reactor_fuel_cell",
            () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .strength(2f, 4f)
                    .noOcclusion()
            ));

        public static final RegistryObject<Block> FISSION_REACTOR_INTERFACE = registerBlock("fission_reactor_interface",
            () -> new FissionReactorInterface(BlockBehaviour.Properties
                    .copy(Blocks.STONE)
                    .strength(2f, 4f)
                    .noOcclusion()
            ));



        private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
                return BLOCKS.register(name, block);
    }

        static void bootstrap() {
        }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);

    }

}
