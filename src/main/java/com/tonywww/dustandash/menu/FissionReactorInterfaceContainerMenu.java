package com.tonywww.dustandash.menu;

import com.tonywww.dustandash.block.entity.FissionReactor.FissionReactorInterfaceEntity;
import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAAContainerMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class FissionReactorInterfaceContainerMenu extends AbstractMachineMenu<FissionReactorInterfaceEntity> {

    public FissionReactorInterfaceContainerMenu(int id, Inventory playerInventory, FissionReactorInterfaceEntity tileEntity) {
        super(DAAContainerMenus.FISSION_REACTOR_INTERFACE_CONTAINER.get(), id, playerInventory, tileEntity,
                DAABlocks.FISSION_REACTOR_INTERFACE.get(), 4, 8, 86);
        tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            addSlot(new SlotItemHandler(handler, 0, 66, 21));
            addSlot(new SlotItemHandler(handler, 1, 94, 21));
            addSlot(new SlotItemHandler(handler, 2, 66, 49));
            addSlot(new SlotItemHandler(handler, 3, 94, 49));
        });
    }

    public FissionReactorInterfaceContainerMenu(final int id,
                                                final Inventory playerInventory,
                                                final FriendlyByteBuf data) {
        this(id, playerInventory,
                readBlockEntity(playerInventory, data, FissionReactorInterfaceEntity.class));
    }
}
