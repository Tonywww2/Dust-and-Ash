package com.tonywww.dustandash.block.entity;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {

    public static DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, DustAndAsh.MOD_ID);

    public static RegistryObject<BlockEntityType<IntegratedBlockTile>> INTEGRATED_BLOCK_TILE =
            TILE_ENTITIES.register("integrated_block",
                    () -> BlockEntityType.Builder.of(IntegratedBlockTile::new, ModBlocks.INTEGRATED_BLOCK.get()).build(null));

    public static RegistryObject<BlockEntityType<AshCollectorTile>> ASH_COLLECTOR_TILE =
            TILE_ENTITIES.register("ash_collector",
                    () -> BlockEntityType.Builder.of(AshCollectorTile::new, ModBlocks.ASH_COLLECTOR.get()).build(null));

    public static RegistryObject<BlockEntityType<MillingMachineTile>> MILLING_MACHINE_TILE =
            TILE_ENTITIES.register("milling_machine",
                    () -> BlockEntityType.Builder.of(MillingMachineTile::new, ModBlocks.MILLING_MACHINE.get()).build(null));

    public static RegistryObject<BlockEntityType<CentrifugeTile>> CENTRIFUGE_TILE =
            TILE_ENTITIES.register("centrifuge",
                    () -> BlockEntityType.Builder.of(CentrifugeTile::new, ModBlocks.CENTRIFUGE.get()).build(null));

    public static RegistryObject<BlockEntityType<IonizerTile>> IONIZER_TILE =
            TILE_ENTITIES.register("ionizer",
                    () -> BlockEntityType.Builder.of(IonizerTile::new, ModBlocks.IONIZER.get()).build(null));

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);

    }

}
