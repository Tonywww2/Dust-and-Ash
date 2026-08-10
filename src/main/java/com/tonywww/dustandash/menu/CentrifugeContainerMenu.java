package com.tonywww.dustandash.menu;

import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.block.entity.CentrifugeEntity;
import com.tonywww.dustandash.registry.DAAContainerMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class CentrifugeContainerMenu extends AbstractMachineMenu<CentrifugeEntity> {

    private final ContainerData data;

    public CentrifugeContainerMenu(int id, Inventory playerInventory, CentrifugeEntity tileEntity, ContainerData data) {
        super(DAAContainerMenus.CENTRIFUGE_CONTAINER.get(), id, playerInventory, tileEntity,
                DAABlocks.CENTRIFUGE.get(), 10, 8, 92);
        this.data = data;

        tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            addSlot(new SlotItemHandler(handler, 0, 80, 6));
            addSlot(new SlotItemHandler(handler, 1, 80, 27));

            addSlot(new SlotItemHandler(handler, 2, 22, 12));
            addSlot(new SlotItemHandler(handler, 3, 42, 30));
            addSlot(new SlotItemHandler(handler, 4, 22, 48));
            addSlot(new SlotItemHandler(handler, 5, 42, 66));

            addSlot(new SlotItemHandler(handler, 6, 138, 12));
            addSlot(new SlotItemHandler(handler, 7, 118, 30));
            addSlot(new SlotItemHandler(handler, 8, 138, 48));
            addSlot(new SlotItemHandler(handler, 9, 118, 66));
        });
        addDataSlots(data);

    }

    public float getProgressionRatio() {
        if (this.data.get(0) > 0) {
            return this.data.get(0) / (float) this.data.get(1);

        }
        return 0;
    }

    public CentrifugeContainerMenu(final int id,
                                   final Inventory playerInventory,
                                   final FriendlyByteBuf data) {
        this(id, playerInventory, readBlockEntity(playerInventory, data, CentrifugeEntity.class),
                new SimpleContainerData(2));
    }
}
