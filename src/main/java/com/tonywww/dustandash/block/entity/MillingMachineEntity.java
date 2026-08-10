package com.tonywww.dustandash.block.entity;

import com.tonywww.dustandash.menu.MillingMachineContainerMenu;
import com.tonywww.dustandash.menu.itemhandlers.RestrictedItemHandler;
import com.tonywww.dustandash.data.recipes.MillingMachineRecipe;
import com.tonywww.dustandash.registry.DAABlockEntities;
import com.tonywww.dustandash.tag.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class MillingMachineEntity extends BasicMachineEntity implements MenuProvider, DroppableInventory {

    public ItemStackHandler invItemStackHandler;
    private final ManagedCapability<ItemStackHandler> handler;
    private final ManagedCapability<RestrictedItemHandler> inputHandler;
    private final ManagedCapability<RestrictedItemHandler> outputHandler;
    private final ManagedCapability<RestrictedItemHandler> workspaceHandler;
    private final Container recipeInput;
    private final RecipeManager.CachedCheck<Container, MillingMachineRecipe> recipeCheck;

    public MillingMachineEntity(BlockPos pos, BlockState state) {
        super(DAABlockEntities.MILLING_MACHINE_ENTITY.get(), pos, state);

        this.invItemStackHandler = createInputsHandler();

        this.handler = managedCapability(() -> invItemStackHandler);
        this.inputHandler = managedCapability(() -> new RestrictedItemHandler(invItemStackHandler,
            slot -> slot < 2, slot -> false));
        this.outputHandler = managedCapability(() -> new RestrictedItemHandler(invItemStackHandler,
            slot -> false, slot -> slot == 2));
        this.workspaceHandler = managedCapability(() -> new RestrictedItemHandler(invItemStackHandler,
            slot -> slot > 2, slot -> slot > 2));
        this.recipeInput = new ItemHandlerContainerView(this.invItemStackHandler);
        this.recipeCheck = RecipeManager.createCheck(MillingMachineRecipe.MillingRecipeType.INSTANCE);

    }

    // 0: input 1
    // 1: input 2
    // 2: output
    // 3-27: workplace
    private ItemStackHandler createInputsHandler() {
        return new ItemStackHandler(28) {
            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot == 2) {
                    return false;
                } else if (slot > 2) {
                    return !stack.is(ModTags.Items.MILLING_BLACKLIST);

                }
                return true;
            }

            @Override
            public int getSlotLimit(int slot) {
                if (slot > 2) {
                    return 1;
                }
                return 64;
            }


        };
    }

    @Override
    public void load(CompoundTag compoundNBT) {
        invItemStackHandler.deserializeNBT(compoundNBT.getCompound("inv"));
        super.load(compoundNBT);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.put("inv", invItemStackHandler.serializeNBT());
        super.saveAdditional(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return this.handler.get().cast();
            }
            if (side == Direction.UP) {
                return this.inputHandler.get().cast();
            }
            if (side == Direction.DOWN) {
                return this.outputHandler.get().cast();
            } else {
                return this.workspaceHandler.get().cast();
            }

        }
        return super.getCapability(cap, side);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.dustandash.milling_machine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new MillingMachineContainerMenu(id, playerInventory, this);
    }

    @Override
    public ItemStackHandler getInventory() {
        return this.invItemStackHandler;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MillingMachineEntity be) {
        if (!level.isClientSide) {
            if (be.advanceWorkCycle(1)) {
                craft(level, pos, be);
                be.resetWorkCycle();

            }

        }


    }


    public static void craft(Level level, BlockPos pos, MillingMachineEntity be) {
        Optional<MillingMachineRecipe> recipe = be.recipeCheck.getRecipeFor(be.recipeInput, level);

        recipe.ifPresent(iRecipe -> {
            ItemStack output = iRecipe.getResultItem(null);

            if (iRecipe.isStep1()) {
//                if (isWorkPlaceEmpty()) {
                // Step1 option
                be.invItemStackHandler.extractItem(0, 1, false);
                ItemStack temp = new ItemStack(output.getItem(), 1);
                for (int i = 3; i <= 27; i++) {
                    be.invItemStackHandler.setStackInSlot(i, temp.copy());

                }

//                }


            } else if (be.invItemStackHandler.getStackInSlot(2).getCount() == 0) {
                be.invItemStackHandler.extractItem(1, 1, false);
                for (int i = 3; i <= 27; i++) {
                    be.invItemStackHandler.extractItem(i, 1, false);

                }

                be.invItemStackHandler.setStackInSlot(2, output.copy());
                level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.5f, 1f);


            }


            be.inventoryChanged();
        });

    }

    public boolean isWorkPlaceEmpty() {
        for (int i = 3; i <= 27; i++) {
            if (invItemStackHandler.getStackInSlot(i).getCount() > 0) {
                return false;
            }

        }
        return true;
    }


}
