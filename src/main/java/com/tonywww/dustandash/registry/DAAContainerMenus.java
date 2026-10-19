package com.tonywww.dustandash.registry;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.menu.*;
import net.minecraft.world.inventory.MenuType;
import com.tonywww.dustandash.platform.PlatformMenuType;
import com.tonywww.dustandash.platform.PlatformEventBus;
import com.tonywww.dustandash.platform.RegistryObject;
import com.tonywww.dustandash.platform.DeferredRegister;
import com.tonywww.dustandash.platform.PlatformRegistries;

public class DAAContainerMenus {

    public static DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(PlatformRegistries.MENU_TYPES, DustAndAsh.MOD_ID);

    public static final RegistryObject<MenuType<IntegratedBlockContainerMenu>> INTEGRATED_BLOCK_CONTAINER = CONTAINERS.register("integrated_block_container",
            () -> PlatformMenuType.create(IntegratedBlockContainerMenu::new));

    public static final RegistryObject<MenuType<AshCollectorContainerMenu>> ASH_COLLECTOR_CONTAINER = CONTAINERS.register("ash_collector_container",
            () -> PlatformMenuType.create(AshCollectorContainerMenu::new));

    public static final RegistryObject<MenuType<MillingMachineContainerMenu>> MILLING_MACHINE_CONTAINER = CONTAINERS.register("milling_machine_container",
            () -> PlatformMenuType.create(MillingMachineContainerMenu::new));

    public static final RegistryObject<MenuType<AutomaticMillingRecyclerMenu>> AUTOMATIC_MILLING_RECYCLER_CONTAINER = CONTAINERS.register("automatic_milling_recycler_container",
            () -> PlatformMenuType.create(AutomaticMillingRecyclerMenu::new));

    public static final RegistryObject<MenuType<CentrifugeContainerMenu>> CENTRIFUGE_CONTAINER = CONTAINERS.register("centrifuge_container",
            () -> PlatformMenuType.create(CentrifugeContainerMenu::new));

    public static final RegistryObject<MenuType<IonizerContainerMenu>> IONIZER_CONTAINER = CONTAINERS.register("ionizer_container",
            () -> PlatformMenuType.create(IonizerContainerMenu::new));

    public static final RegistryObject<MenuType<ItemSenderContainerMenu>> ITEM_SENDER_CONTAINER = CONTAINERS.register("item_sender_container",
            () -> PlatformMenuType.create(ItemSenderContainerMenu::new));

    public static final RegistryObject<MenuType<FissionReactorControllerContainerMenu>> FISSION_REACTOR_CONTROLLER_CONTAINER = CONTAINERS.register("fission_reactor_controller_container",
            () -> PlatformMenuType.create(FissionReactorControllerContainerMenu::new));

    public static final RegistryObject<MenuType<FissionReactorInterfaceContainerMenu>> FISSION_REACTOR_INTERFACE_CONTAINER = CONTAINERS.register("fission_reactor_interface_container",
            () -> PlatformMenuType.create(FissionReactorInterfaceContainerMenu::new));

    public static void register(PlatformEventBus eventBus) {
        CONTAINERS.register(eventBus);

    }

}
