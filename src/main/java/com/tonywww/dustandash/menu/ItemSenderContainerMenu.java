package com.tonywww.dustandash.menu;

import com.tonywww.dustandash.block.entity.ItemSenderEntity;
import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAAContainerMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class ItemSenderContainerMenu extends AbstractMachineMenu<ItemSenderEntity> {
    private final ContainerData data;

    public ItemSenderContainerMenu(int id, Inventory playerInventory, ItemSenderEntity tileEntity, ContainerData data) {
        super(DAAContainerMenus.ITEM_SENDER_CONTAINER.get(), id, playerInventory, tileEntity,
                DAABlocks.ITEM_SENDER.get(), 5, 8, 92);
        this.data = data;

        tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            addSlot(new SlotItemHandler(handler, 0, 151, 8));
            addSlot(new SlotItemHandler(handler, 1, 10, 17));
            addSlot(new SlotItemHandler(handler, 2, 80, 17));
            addSlot(new SlotItemHandler(handler, 3, 10, 46));
            addSlot(new SlotItemHandler(handler, 4, 80, 46));
        });
        addDataSlots(data);
    }

    public ItemSenderContainerMenu(int id, Inventory playerInventory, FriendlyByteBuf data) {
        this(id, playerInventory, readBlockEntity(playerInventory, data, ItemSenderEntity.class),
                new SimpleContainerData(3));
    }

    public ItemSenderEntity getTileEntity() {
        return this.tileEntity;
    }

    public int getTargetX() {
        return this.data.get(0);
    }

    public int getTargetY() {
        return this.data.get(1);
    }

    public int getTargetZ() {
        return this.data.get(2);
    }
}