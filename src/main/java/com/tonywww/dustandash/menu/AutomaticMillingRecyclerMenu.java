package com.tonywww.dustandash.menu;

import com.tonywww.dustandash.block.entity.AutomaticMillingRecyclerEntity;
import com.tonywww.dustandash.platform.SlotItemHandler;
import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAAContainerMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class AutomaticMillingRecyclerMenu extends AbstractMachineMenu<AutomaticMillingRecyclerEntity> {
    private final ContainerData gridData;

    public AutomaticMillingRecyclerMenu(int id, Inventory inventory, AutomaticMillingRecyclerEntity entity, ContainerData data) {
        super(DAAContainerMenus.AUTOMATIC_MILLING_RECYCLER_CONTAINER.get(), id, inventory, entity,
                DAABlocks.AUTOMATIC_MILLING_RECYCLER.get(), AutomaticMillingRecyclerEntity.INVENTORY_SIZE, 8, 145);
        checkContainerDataCount(data, AutomaticMillingRecyclerEntity.GRID_SIZE);
        this.gridData = data;
        for (int row = 0; row < 2; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new SlotItemHandler(entity.getInventory(), row * 9 + column, 8 + column * 18, 95 + row * 18));
            }
        }
        // Each cell is a separate short-safe value; a single 25-bit DataSlot would truncate.
        addDataSlots(data);
    }

    public AutomaticMillingRecyclerMenu(int id, Inventory inventory, FriendlyByteBuf buffer) {
        this(id, inventory, readBlockEntity(inventory, buffer, AutomaticMillingRecyclerEntity.class),
                new SimpleContainerData(AutomaticMillingRecyclerEntity.GRID_SIZE));
    }

    public boolean isSlotEnabled(int cell) {
        return cell >= 0 && cell < AutomaticMillingRecyclerEntity.GRID_SIZE && gridData.get(cell) != 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return super.stillValid(player) && !tileEntity.isRemoved()
                && player.level().getBlockEntity(tileEntity.getBlockPos()) == tileEntity
                && tileEntity.getConnectedMill() != null;
    }

    /** Uses vanilla's container-button packet, including its current container ID check. */
    @Override
    public boolean clickMenuButton(Player player, int cell) {
        if (player.level().isClientSide || player.isSpectator() || !stillValid(player)
                || cell < 0 || cell >= AutomaticMillingRecyclerEntity.GRID_SIZE) return false;
        tileEntity.setSlotEnabled(cell, !tileEntity.isSlotEnabled(cell));
        broadcastChanges();
        return true;
    }
}
