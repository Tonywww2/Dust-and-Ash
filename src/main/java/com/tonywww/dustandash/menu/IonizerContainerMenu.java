package com.tonywww.dustandash.menu;

import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.block.entity.IonizerEntity;
import com.tonywww.dustandash.registry.DAAContainerMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.Objects;

public class IonizerContainerMenu extends AbstractMachineMenu<IonizerEntity> {

    private final ContainerData data;

    public IonizerContainerMenu(int id, Inventory playerInventory, IonizerEntity tileEntity, ContainerData data) {
        super(DAAContainerMenus.IONIZER_CONTAINER.get(), id, playerInventory, tileEntity,
                DAABlocks.IONIZER.get(), 6, 8, 92);
        this.data = data;
        tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            addSlot(new SlotItemHandler(handler, 0, 16, 30));
            addSlot(new SlotItemHandler(handler, 1, 16, 50));
            addSlot(new SlotItemHandler(handler, 2, 6, 70));
            addSlot(new SlotItemHandler(handler, 3, 26, 70));
            addSlot(new SlotItemHandler(handler, 4, 62, 5));
            addSlot(new SlotItemHandler(handler, 5, 98, 5));
        });
        addDataSlots(data);

    }

    public float getProgressionRatio() {
        if (this.data.get(0) > 0) {
            return this.data.get(0) / (float) this.data.get(1);

        }
        return 0;
    }

    public IonizerEntity getTileEntity() {
        return tileEntity;

    }

    public boolean getBelowAvailable() {
        return this.data.get(2) > 0;

    }

    public IonizerContainerMenu(final int id,
                                final Inventory playerInventory,
                                final FriendlyByteBuf data) {
        this(id, playerInventory, readBlockEntity(playerInventory, data, IonizerEntity.class),
                new SimpleContainerData(3));
    }
}
