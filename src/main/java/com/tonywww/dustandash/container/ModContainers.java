package com.tonywww.dustandash.container;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {

    public static DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, DustAndAsh.MOD_ID);

    public static final RegistryObject<ContainerType<IntegratedBlockContainer>> INTEGRATED_BLOCK_CONTAINER = CONTAINERS.register("integrated_block_container",
            () -> IForgeContainerType.create(IntegratedBlockContainer::new));

    public static final RegistryObject<ContainerType<AshCollectorContainer>> ASH_COLLECTOR_CONTAINER = CONTAINERS.register("ash_collector_container",
            () -> IForgeContainerType.create(AshCollectorContainer::new));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);

    }

}
