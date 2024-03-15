package com.tonywww.dustandash.menu;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModMenus {

    public static DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, DustAndAsh.MOD_ID);

    public static final RegistryObject<MenuType<IntegratedBlockContainer>> INTEGRATED_BLOCK_CONTAINER = CONTAINERS.register("integrated_block_container",
            () -> IForgeMenuType.create(IntegratedBlockContainer::new));

    public static final RegistryObject<MenuType<AshCollectorContainer>> ASH_COLLECTOR_CONTAINER = CONTAINERS.register("ash_collector_container",
            () -> IForgeMenuType.create(AshCollectorContainer::new));

    public static final RegistryObject<MenuType<MillingMachineContainer>> MILLING_MACHINE_CONTAINER = CONTAINERS.register("milling_machine_container",
            () -> IForgeMenuType.create(MillingMachineContainer::new));

    public static final RegistryObject<MenuType<CentrifugeContainer>> CENTRIFUGE_CONTAINER = CONTAINERS.register("centrifuge_container",
            () -> IForgeMenuType.create(CentrifugeContainer::new));

    public static final RegistryObject<MenuType<IonizerContainer>> IONIZER_CONTAINER = CONTAINERS.register("ionizer_container",
            () -> IForgeMenuType.create(IonizerContainer::new));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);

    }

}
