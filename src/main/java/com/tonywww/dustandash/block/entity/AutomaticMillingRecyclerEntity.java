package com.tonywww.dustandash.block.entity;

import com.tonywww.dustandash.block.custom.AbstractHorizontalMachineBlock;
import com.tonywww.dustandash.menu.AutomaticMillingRecyclerMenu;
import com.tonywww.dustandash.platform.ItemStackHandler;
import com.tonywww.dustandash.platform.PlatformCapabilities;
import com.tonywww.dustandash.platform.PlatformCapability;
import com.tonywww.dustandash.platform.PlatformOptional;
import com.tonywww.dustandash.registry.DAABlockEntities;
import com.tonywww.dustandash.tag.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AutomaticMillingRecyclerEntity extends BasicMachineEntity implements MenuProvider, DroppableInventory {
    public static final int GRID_SIZE = 25;
    public static final int INVENTORY_SIZE = 18;
    private static final int ALL_ENABLED = (1 << GRID_SIZE) - 1;
    private int enabledSlots = ALL_ENABLED;

    private final ItemStackHandler inventory = new ItemStackHandler(INVENTORY_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            inventoryChanged();
        }
    };
    private final ManagedCapability<ItemStackHandler> handler = managedCapability(() -> inventory);
    private final ContainerData gridData = new ContainerData() {
        @Override public int get(int index) { return isSlotEnabled(index) ? 1 : 0; }
        @Override public void set(int index, int value) { setSlotEnabled(index, value != 0); }
        @Override public int getCount() { return GRID_SIZE; }
    };

    public AutomaticMillingRecyclerEntity(BlockPos pos, BlockState state) {
        super(DAABlockEntities.AUTOMATIC_MILLING_RECYCLER_ENTITY.get(), pos, state);
    }

    public boolean isSlotEnabled(int slot) {
        return slot >= 0 && slot < GRID_SIZE && (enabledSlots & (1 << slot)) != 0;
    }

    public void setSlotEnabled(int slot, boolean enabled) {
        if (slot < 0 || slot >= GRID_SIZE || (level != null && level.isClientSide)) return;
        int updated = enabled ? enabledSlots | (1 << slot) : enabledSlots & ~(1 << slot);
        if (updated != enabledSlots) {
            enabledSlots = updated;
            syncToClient();
        }
    }

    /** Facing points at the control panel; the connector is on the opposite face. */
    @Nullable
    public MillingMachineEntity getConnectedMill() {
        if (level == null || isRemoved()) return null;
        BlockPos behind = worldPosition.relative(getBlockState().getValue(AbstractHorizontalMachineBlock.FACING).getOpposite());
        // Never load a neighbouring chunk merely to operate an attachment.
        if (!level.hasChunkAt(behind)) return null;
        return level.getBlockEntity(behind) instanceof MillingMachineEntity mill && !mill.isRemoved() ? mill : null;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AutomaticMillingRecyclerEntity recycler) {
        if (level.isClientSide || !recycler.advanceWorkCycle(1)) return;
        recycler.resetWorkCycle();
        MillingMachineEntity mill = recycler.getConnectedMill();
        if (mill == null) return;

        for (int cell = 0; cell < GRID_SIZE; cell++) {
            if (recycler.isSlotEnabled(cell)) continue;
            int sourceSlot = cell + 3; // Row-major, identical to the milling machine menu.
            ItemStack source = mill.invItemStackHandler.getStackInSlot(sourceSlot);
            if (source.isEmpty() || !source.is(ModTags.Items.SCRAPS)) continue;
            for (int destination = 0; destination < INVENTORY_SIZE && !source.isEmpty(); destination++) {
                ItemStack remainder = recycler.inventory.insertItem(destination, source, true);
                int accepted = source.getCount() - remainder.getCount();
                if (accepted == 0) continue;
                // Both inventories are owned machine handlers on the server thread. Only extract
                // after reserving capacity so a full recycler never deletes workspace material.
                ItemStack extracted = mill.invItemStackHandler.extractItem(sourceSlot, accepted, false);
                recycler.inventory.insertItem(destination, extracted, false);
                source = mill.invItemStackHandler.getStackInSlot(sourceSlot);
            }
        }
    }

    @Override
    public ItemStackHandler getInventory() { return inventory; }

    @Nonnull
    @Override
    public <T> PlatformOptional<T> getCapability(@Nonnull PlatformCapability<T> cap, @Nullable Direction side) {
        if (!isRemoved() && cap == PlatformCapabilities.ITEM_HANDLER) return handler.get().cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("inv")) inventory.deserializeNBT(tag.getCompound("inv"));
        enabledSlots = tag.contains("enabledSlots") ? tag.getInt("enabledSlots") & ALL_ENABLED : ALL_ENABLED;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inv", inventory.serializeNBT());
        tag.putInt("enabledSlots", enabledSlots);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.dustandash.automatic_milling_recycler");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return getConnectedMill() == null ? null : new AutomaticMillingRecyclerMenu(id, playerInventory, this, gridData);
    }
}
