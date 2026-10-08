package com.tonywww.dustandash.platform;

//? if forge {

//?} else {
/*import com.tonywww.dustandash.registry.DAABlockEntities;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.minecraft.world.level.block.entity.BlockEntityType;
public final class PlatformCapabilityRegistration {
    public static void register(RegisterCapabilitiesEvent event) {
        items(event, DAABlockEntities.INTEGRATED_BLOCK_ENTITY.get());
        items(event, DAABlockEntities.ASH_COLLECTOR_ENTITY.get());
        items(event, DAABlockEntities.MILLING_MACHINE_ENTITY.get());
        items(event, DAABlockEntities.CENTRIFUGE_ENTITY.get());
        items(event, DAABlockEntities.IONIZER_ENTITY.get());
        items(event, DAABlockEntities.ITEM_SENDER_ENTITY.get());
        items(event, DAABlockEntities.FISSION_REACTOR_INTERFACE_ENTITY.get());
        items(event, DAABlockEntities.FISSION_REACTOR_CONTROLLER_ENTITY.get());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, DAABlockEntities.FISSION_REACTOR_CONTROLLER_ENTITY.get(), (be, side) -> be.getCapability(PlatformCapabilities.ENERGY, side).orElse(null));
    }
    private static <T extends PlatformBlockEntity> void items(RegisterCapabilitiesEvent event, BlockEntityType<T> type) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, (be, side) -> be.getCapability(PlatformCapabilities.ITEM_HANDLER, side).orElse(null));
    }
}
*///?}
