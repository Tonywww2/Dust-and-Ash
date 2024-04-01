package com.tonywww.dustandash.menu;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainerMenus {

    public static DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, DustAndAsh.MOD_ID);

    public static final RegistryObject<MenuType<IntegratedBlockContainerMenu>> INTEGRATED_BLOCK_CONTAINER = CONTAINERS.register("integrated_block_container",
            () -> IForgeMenuType.create(IntegratedBlockContainerMenu::new));

    public static final RegistryObject<MenuType<AshCollectorContainerMenu>> ASH_COLLECTOR_CONTAINER = CONTAINERS.register("ash_collector_container",
            () -> IForgeMenuType.create(AshCollectorContainerMenu::new));

    public static final RegistryObject<MenuType<MillingMachineContainerMenu>> MILLING_MACHINE_CONTAINER = CONTAINERS.register("milling_machine_container",
            () -> IForgeMenuType.create(MillingMachineContainerMenu::new));

    public static final RegistryObject<MenuType<CentrifugeContainerMenu>> CENTRIFUGE_CONTAINER = CONTAINERS.register("centrifuge_container",
            () -> IForgeMenuType.create(CentrifugeContainerMenu::new));

    public static final RegistryObject<MenuType<IonizerContainerMenu>> IONIZER_CONTAINER = CONTAINERS.register("ionizer_container",
            () -> IForgeMenuType.create(IonizerContainerMenu::new));

    public static final RegistryObject<MenuType<ItemSenderContainerMenu>> ITEM_SENDER_CONTAINER = CONTAINERS.register("item_sender_container",
            () -> IForgeMenuType.create(ItemSenderContainerMenu::new));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);

    }

}
