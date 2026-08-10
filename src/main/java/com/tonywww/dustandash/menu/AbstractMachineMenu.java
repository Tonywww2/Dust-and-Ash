package com.tonywww.dustandash.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.Objects;

public abstract class AbstractMachineMenu<T extends BlockEntity> extends AbstractContainerMenu {
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    protected static final int PLAYER_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

    protected final T tileEntity;

    private final ContainerLevelAccess access;
    private final Block validBlock;
    private final int machineSlotCount;

    protected AbstractMachineMenu(MenuType<?> menuType, int id, Inventory playerInventory, T tileEntity,
                                  Block validBlock, int machineSlotCount, int playerInventoryX, int playerInventoryY) {
        super(menuType, id);
        this.tileEntity = Objects.requireNonNull(tileEntity, "tileEntity");
        this.validBlock = Objects.requireNonNull(validBlock, "validBlock");
        this.machineSlotCount = machineSlotCount;

        Level level = Objects.requireNonNull(tileEntity.getLevel(), "tileEntity level");
        this.access = ContainerLevelAccess.create(level, tileEntity.getBlockPos());
        layoutPlayerInventory(new InvWrapper(playerInventory), playerInventoryX, playerInventoryY);
    }

    protected static <T extends BlockEntity> T readBlockEntity(Inventory playerInventory, FriendlyByteBuf data,
                                                                 Class<T> expectedType) {
        Objects.requireNonNull(playerInventory, "playerInventory");
        Objects.requireNonNull(data, "data");

        BlockEntity blockEntity = playerInventory.player.level().getBlockEntity(data.readBlockPos());
        if (!expectedType.isInstance(blockEntity)) {
            throw new IllegalStateException("Expected " + expectedType.getSimpleName() + ", found " + blockEntity);
        }
        return expectedType.cast(blockEntity);
    }

    private void layoutPlayerInventory(IItemHandler playerInventory, int left, int top) {
        addSlotBox(playerInventory, 9, left, top, PLAYER_INVENTORY_COLUMN_COUNT, 18,
                PLAYER_INVENTORY_ROW_COUNT, 18);
        addSlotRange(playerInventory, 0, left, top + 58, HOTBAR_SLOT_COUNT, 18);
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int columns, int dx, int rows, int dy) {
        for (int row = 0; row < rows; row++) {
            index = addSlotRange(handler, index, x, y, columns, dx);
            y += dy;
        }
        return index;
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int slot = 0; slot < amount; slot++) {
            addSlot(new SlotItemHandler(handler, index++, x, y));
            x += dx;
        }
        return index;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, this.validBlock);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (index < 0 || index >= this.slots.size()) {
            return ItemStack.EMPTY;
        }

        Slot sourceSlot = this.slots.get(index);
        if (!sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack originalStack = sourceStack.copy();
        int machineEnd = PLAYER_SLOT_COUNT + this.machineSlotCount;

        if (index < PLAYER_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, PLAYER_SLOT_COUNT, machineEnd, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < machineEnd) {
            if (!moveItemStackTo(sourceStack, 0, PLAYER_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        if (sourceStack.isEmpty()) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return originalStack;
    }
}