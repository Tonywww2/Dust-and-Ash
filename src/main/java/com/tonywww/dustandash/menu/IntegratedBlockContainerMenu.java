package com.tonywww.dustandash.menu;

import com.tonywww.dustandash.registeries.ModBlocks;
import com.tonywww.dustandash.block.entity.IntegratedBlockEntity;
import com.tonywww.dustandash.registeries.ModContainerMenus;
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

public class IntegratedBlockContainerMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess canInteractWithCallable;

    private final ContainerData data;

    public IntegratedBlockContainerMenu(int id, Inventory playerInventory, IntegratedBlockEntity tileEntity, ContainerData data) {
        super(ModContainerMenus.INTEGRATED_BLOCK_CONTAINER.get(), id);
        this.canInteractWithCallable = ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos());

        this.data = data;

        tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
            addSlot(new SlotItemHandler(h, 0, 36, 42));
            addSlot(new SlotItemHandler(h, 1, 124, 42));

            addSlot(new SlotItemHandler(h, 2, 58, 31));
            addSlot(new SlotItemHandler(h, 3, 80, 31));
            addSlot(new SlotItemHandler(h, 4, 102, 31));
            addSlot(new SlotItemHandler(h, 5, 58, 53));
            addSlot(new SlotItemHandler(h, 6, 80, 53));
            addSlot(new SlotItemHandler(h, 7, 102, 53));

            addDataSlots(data);

        });

        int i, j;

        for (i = 0; i < 3; i++) {
            for (j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
            }
        }

        for (j = 0; j < 9; j++) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 144));
        }

    }

    public IntegratedBlockContainerMenu(final int id,
                                        final Inventory playerInventory,
                                        final FriendlyByteBuf data) {
        this(id, playerInventory, getTileEntity(playerInventory, data), new SimpleContainerData(2));

    }

    private static IntegratedBlockEntity getTileEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final BlockEntity tileAtPos = playerInventory.player.level().getBlockEntity(data.readBlockPos());
        if (tileAtPos instanceof IntegratedBlockEntity) {
            return (IntegratedBlockEntity) tileAtPos;
        }
        throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
    }

    public int getStructureLevel() {
        return data.get(0);

    }

    public boolean isBeaconOn() {
        return data.get(1) == 1;

    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(canInteractWithCallable, pPlayer, ModBlocks.INTEGRATED_BLOCK.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotNumber) {
        var itemstack = ItemStack.EMPTY;
        var slot = this.slots.get(slotNumber);

        if (slot.hasItem()) {
            var itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (slotNumber == 0) {
                if (!this.moveItemStackTo(itemstack1, 8, 44, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (slotNumber >= 8 && slotNumber < 44) {
                if (!this.moveItemStackTo(itemstack1, 0, 8, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 8, 44, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
}