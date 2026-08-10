package com.tonywww.dustandash.menu;

import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.block.entity.MillingMachineEntity;
import com.tonywww.dustandash.registry.DAAContainerMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class MillingMachineContainerMenu extends AbstractMachineMenu<MillingMachineEntity> {

    public MillingMachineContainerMenu(int id, Inventory playerInventory, MillingMachineEntity tileEntity) {
        super(DAAContainerMenus.MILLING_MACHINE_CONTAINER.get(), id, playerInventory, tileEntity,
                DAABlocks.MILLING_MACHINE.get(), 28, 8, 93);
        tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            addSlot(new SlotItemHandler(handler, 0, 28, 10));
            addSlot(new SlotItemHandler(handler, 1, 28, 38));
            addSlot(new SlotItemHandler(handler, 2, 28, 66));

            for (int row = 1; row <= 5; row++) {
                for (int column = 1; column <= 5; column++) {
                    addSlot(new SlotItemHandler(handler, (5 * row) + column - 3,
                            67 + (17 * column), (17 * row) - 15));
                }
            }
        });

    }

    public boolean isWorkSpaceEmpty() {
        return tileEntity.isWorkPlaceEmpty();
    }

    public MillingMachineContainerMenu(final int id,
                                       final Inventory playerInventory,
                                       final FriendlyByteBuf data) {
        this(id, playerInventory, readBlockEntity(playerInventory, data, MillingMachineEntity.class));
    }
}
