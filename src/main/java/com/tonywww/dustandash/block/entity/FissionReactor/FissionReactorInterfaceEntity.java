package com.tonywww.dustandash.block.entity.FissionReactor;

import com.tonywww.dustandash.block.entity.SyncedBlockEntity;
import com.tonywww.dustandash.block.entity.DroppableInventory;
import com.tonywww.dustandash.item.FissionReactor.FissionReactorCoolingUnit;
import com.tonywww.dustandash.item.FissionReactor.FissionReactorFuelUnit;
import com.tonywww.dustandash.menu.FissionReactorInterfaceContainerMenu;
import com.tonywww.dustandash.registry.DAABlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import com.tonywww.dustandash.platform.PlatformCapability;
import com.tonywww.dustandash.platform.PlatformCapabilities;
import com.tonywww.dustandash.platform.PlatformOptional;
import com.tonywww.dustandash.platform.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FissionReactorInterfaceEntity extends SyncedBlockEntity implements MenuProvider, DroppableInventory {
    public static final int FUEL_INPUT_SLOT = 0;
    public static final int COOLING_INPUT_SLOT = 1;
    public static final int FUEL_OUTPUT_SLOT = 2;
    public static final int COOLING_OUTPUT_SLOT = 3;
    public static final int SLOT_COUNT = 4;

    public final ItemStackHandler itemStackHandler = createHandler();
    private final ManagedCapability<ItemStackHandler> handler = managedCapability(() -> itemStackHandler);

    public FissionReactorInterfaceEntity(BlockPos pos, BlockState state) {
        super(DAABlockEntities.FISSION_REACTOR_INTERFACE_ENTITY.get(), pos, state);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(SLOT_COUNT) {
            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return (slot == FUEL_INPUT_SLOT && stack.getItem() instanceof FissionReactorFuelUnit)
                    || (slot == COOLING_INPUT_SLOT && stack.getItem() instanceof FissionReactorCoolingUnit);
            }

            @Override
            public int getSlotLimit(int slot) {
                return 64;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

                if (!isItemValid(slot, stack)) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Nonnull
    @Override
    public <T> PlatformOptional<T> getCapability(@Nonnull PlatformCapability<T> cap, @Nullable Direction side) {
        if (cap == PlatformCapabilities.ITEM_HANDLER) {
            return this.handler.get().cast();

        }
        return super.getCapability(cap, side);
    }

    @Override
    public void load(CompoundTag compoundNBT) {
        itemStackHandler.deserializeNBT(compoundNBT.getCompound("inv"));
        super.load(compoundNBT);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.put("inv", itemStackHandler.serializeNBT());
        super.saveAdditional(compound);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.dustandash.fission_reactor_interface");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new FissionReactorInterfaceContainerMenu(id, playerInventory, this);
    }

    @Override
    public ItemStackHandler getInventory() {
        return this.itemStackHandler;
    }
}
