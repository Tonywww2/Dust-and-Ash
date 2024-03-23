package com.tonywww.dustandash.block.entity;

import com.tonywww.dustandash.menu.MillingMachineContainer;
import com.tonywww.dustandash.menu.MillingMachineItemHandler;
import com.tonywww.dustandash.data.recipes.MillingMachineRecipe;
import com.tonywww.dustandash.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
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

public class MillingMachineTile extends SyncedBlockEntity implements MenuProvider {

    public ItemStackHandler invItemStackHandler;
    private final LazyOptional<ItemStackHandler> handler;
    private final LazyOptional<MillingMachineItemHandler> inputHandler;
    private final LazyOptional<MillingMachineItemHandler> outputHandler;
    private final LazyOptional<MillingMachineItemHandler> workspaceHandler;

    public MillingMachineTile(BlockPos pos, BlockState state) {
        super(ModTileEntities.MILLING_MACHINE_TILE.get(), pos, state);

        this.invItemStackHandler = createInputsHandler();

        this.handler = LazyOptional.of(() -> invItemStackHandler);
        this.inputHandler = LazyOptional.of(() -> new MillingMachineItemHandler(invItemStackHandler, Direction.UP));
        this.outputHandler = LazyOptional.of(() -> new MillingMachineItemHandler(invItemStackHandler, Direction.DOWN));
        this.workspaceHandler = LazyOptional.of(() -> new MillingMachineItemHandler(invItemStackHandler, Direction.NORTH));

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
                    return stack.is(ModTags.Items.MILLING_INLAY);

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
                return this.handler.cast();
            }
            if (side == Direction.UP) {
                return this.inputHandler.cast();
            }
            if (side == Direction.DOWN) {
                return this.outputHandler.cast();
            } else {
                return this.workspaceHandler.cast();
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
        return new MillingMachineContainer(id, playerInventory, this);
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < invItemStackHandler.getSlots(); ++i) {
            drops.add(invItemStackHandler.getStackInSlot(i));
        }
        return drops;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MillingMachineTile be) {
        if (!level.isClientSide) {
            craft(level, pos, be);

        }


    }


    public static void craft(Level level, BlockPos pos, MillingMachineTile be) {
        Container inv = new SimpleContainer(be.invItemStackHandler.getSlots());
        for (int i = 0; i < be.invItemStackHandler.getSlots(); i++) {
            inv.setItem(i, be.invItemStackHandler.getStackInSlot(i));

        }

        Optional<MillingMachineRecipe> recipe = level.getRecipeManager().getRecipeFor(MillingMachineRecipe.MillingRecipeType.INSTANCE, inv, level);

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
