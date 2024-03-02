package com.tonywww.dustandash.tileentity;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {

    public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, DustAndAsh.MOD_ID);

    public static RegistryObject<TileEntityType<IntegratedBlockTile>> INTEGRATED_BLOCK_TILE =
            TILE_ENTITIES.register("integrated_block",
                    () -> TileEntityType.Builder.of(IntegratedBlockTile::new, ModBlocks.INTEGRATED_BLOCK.get()).build(null));

    public static RegistryObject<TileEntityType<AshCollectorTile>> ASH_COLLECTOR_TILE =
            TILE_ENTITIES.register("ash_collector",
                    () -> TileEntityType.Builder.of(AshCollectorTile::new, ModBlocks.ASH_COLLECTOR.get()).build(null));

    public static RegistryObject<TileEntityType<MillingMachineTile>> MILLING_MACHINE_TILE =
            TILE_ENTITIES.register("milling_machine",
                    () -> TileEntityType.Builder.of(MillingMachineTile::new, ModBlocks.MILLING_MACHINE.get()).build(null));

    public static RegistryObject<TileEntityType<CentrifugeTile>> CENTRIFUGE_TILE =
            TILE_ENTITIES.register("centrifuge",
                    () -> TileEntityType.Builder.of(CentrifugeTile::new, ModBlocks.CENTRIFUGE.get()).build(null));

    public static RegistryObject<TileEntityType<IonizerTile>> IONIZER_TILE =
            TILE_ENTITIES.register("ionizer",
                    () -> TileEntityType.Builder.of(IonizerTile::new, ModBlocks.IONIZER.get()).build(null));

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);

    }

}
