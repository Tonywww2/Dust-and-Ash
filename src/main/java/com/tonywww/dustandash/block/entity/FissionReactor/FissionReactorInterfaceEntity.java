package com.tonywww.dustandash.block.entity.FissionReactor;

import com.tonywww.dustandash.block.entity.SyncedBlockEntity;
import com.tonywww.dustandash.item.FissionReactor.FissionReactorCoolingUnit;
import com.tonywww.dustandash.item.FissionReactor.FissionReactorFuelUnit;
import com.tonywww.dustandash.menu.FissionReactorInterfaceContainerMenu;
import com.tonywww.dustandash.registeries.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FissionReactorInterfaceEntity extends SyncedBlockEntity implements MenuProvider {

    public final ItemStackHandler itemStackHandler = createHandler();
    private final LazyOptional<ItemStackHandler> handler = LazyOptional.of(() -> itemStackHandler);

    public FissionReactorInterfaceEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FISSION_REACTOR_INTERFACE_ENTITY.get(), pos, state);
    }

    // 0: fuel
    // 1: cooling
    // 2: fuel out
    // 3: cooling out
    private ItemStackHandler createHandler() {
        return new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return (slot == 0 && stack.getItem() instanceof FissionReactorFuelUnit)
                        || (slot == 1 && stack.getItem() instanceof FissionReactorCoolingUnit)
                        || slot == 2
                        || slot == 3;
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
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.handler.cast();

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

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < itemStackHandler.getSlots(); ++i) {
            drops.add(itemStackHandler.getStackInSlot(i));
        }
        return drops;
    }
}
