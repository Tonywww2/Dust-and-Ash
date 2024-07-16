package com.tonywww.dustandash.registeries;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.entity.*;
import com.tonywww.dustandash.block.entity.FissionReactor.FissionReactorControllerEntity;
import com.tonywww.dustandash.block.entity.FissionReactor.FissionReactorInterfaceEntity;
import com.tonywww.dustandash.registeries.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlockEntities {

    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DustAndAsh.MOD_ID);

    public static RegistryObject<BlockEntityType<IntegratedBlockEntity>> INTEGRATED_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("integrated_block",
                    () -> BlockEntityType.Builder.of(IntegratedBlockEntity::new, ModBlocks.INTEGRATED_BLOCK.get()).build(null));

    public static RegistryObject<BlockEntityType<AshCollectorEntity>> ASH_COLLECTOR_ENTITY =
            BLOCK_ENTITIES.register("ash_collector",
                    () -> BlockEntityType.Builder.of(AshCollectorEntity::new, ModBlocks.ASH_COLLECTOR.get()).build(null));

    public static RegistryObject<BlockEntityType<MillingMachineEntity>> MILLING_MACHINE_ENTITY =
            BLOCK_ENTITIES.register("milling_machine",
                    () -> BlockEntityType.Builder.of(MillingMachineEntity::new, ModBlocks.MILLING_MACHINE.get()).build(null));

    public static RegistryObject<BlockEntityType<CentrifugeEntity>> CENTRIFUGE_ENTITY =
            BLOCK_ENTITIES.register("centrifuge",
                    () -> BlockEntityType.Builder.of(CentrifugeEntity::new, ModBlocks.CENTRIFUGE.get()).build(null));

    public static RegistryObject<BlockEntityType<IonizerEntity>> IONIZER_ENTITY =
            BLOCK_ENTITIES.register("ionizer",
                    () -> BlockEntityType.Builder.of(IonizerEntity::new, ModBlocks.IONIZER.get()).build(null));

    public static RegistryObject<BlockEntityType<ItemSenderEntity>> ITEM_SENDER_ENTITY =
            BLOCK_ENTITIES.register("item_sender",
                    () -> BlockEntityType.Builder.of(ItemSenderEntity::new, ModBlocks.ITEM_SENDER.get()).build(null));

    public static RegistryObject<BlockEntityType<FissionReactorControllerEntity>> FISSION_REACTOR_CONTROLLER_ENTITY =
            BLOCK_ENTITIES.register("fission_reactor_controller",
                    () -> BlockEntityType.Builder.of(FissionReactorControllerEntity::new, ModBlocks.FISSION_REACTOR_CONTROLLER.get()).build(null));

    public static RegistryObject<BlockEntityType<FissionReactorInterfaceEntity>> FISSION_REACTOR_INTERFACE_ENTITY =
            BLOCK_ENTITIES.register("fission_reactor_interface",
                    () -> BlockEntityType.Builder.of(FissionReactorInterfaceEntity::new, ModBlocks.FISSION_REACTOR_INTERFACE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);

    }

}
