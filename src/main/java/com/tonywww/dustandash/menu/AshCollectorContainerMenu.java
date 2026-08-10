package com.tonywww.dustandash.menu;

import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.block.entity.AshCollectorEntity;
import com.tonywww.dustandash.registry.DAAContainerMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class AshCollectorContainerMenu extends AbstractMachineMenu<AshCollectorEntity> {

    public AshCollectorContainerMenu(int id, Inventory playerInventory, AshCollectorEntity tileEntity) {
        super(DAAContainerMenus.ASH_COLLECTOR_CONTAINER.get(), id, playerInventory, tileEntity,
                DAABlocks.ASH_COLLECTOR.get(), 2, 8, 86);
        tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            addSlot(new SlotItemHandler(handler, 0, 66, 29));
            addSlot(new SlotItemHandler(handler, 1, 94, 29));
        });
    }

    public AshCollectorContainerMenu(final int id,
                                     final Inventory playerInventory,
                                     final FriendlyByteBuf data) {
                        this(id, playerInventory, readBlockEntity(playerInventory, data, AshCollectorEntity.class));

    }

    public boolean shouldWork() {
        return AshCollectorEntity.shouldWork(tileEntity.getLevel(), tileEntity.getBlockPos());
    }

}
