package com.tonywww.dustandash.tileentity;

import com.tonywww.dustandash.container.MillingMachineContainer;
import com.tonywww.dustandash.container.MillingMachineItemHandler;
import com.tonywww.dustandash.data.recipes.MillingMachineRecipe;
import com.tonywww.dustandash.data.recipes.ModRecipeTypes;
import com.tonywww.dustandash.util.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class MillingMachineTile extends TileEntity implements INamedContainerProvider, ITickableTileEntity {

    public ItemStackHandler inputItemStackHandler;
    private final LazyOptional<ItemStackHandler> handler;
    private final LazyOptional<MillingMachineItemHandler> inputHandler;
    private final LazyOptional<MillingMachineItemHandler> outputHandler;

    public MillingMachineTile(TileEntityType<?> tileEntityType) {
        super(tileEntityType);

        this.inputItemStackHandler = createInputsHandler();

        this.handler = LazyOptional.of(() -> inputItemStackHandler);
        this.inputHandler = LazyOptional.of(() -> new MillingMachineItemHandler(inputItemStackHandler, Direction.UP));
        this.outputHandler = LazyOptional.of(() -> new MillingMachineItemHandler(inputItemStackHandler, Direction.DOWN));

    }

    // 0: input 1
    // 1: input 2
    // 2: output
    // 3-27: workplace
    private ItemStackHandler createInputsHandler() {
        return new ItemStackHandler(28) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot == 2) {
                    return false;
                } else if (slot > 2) {
                    return ModTags.Items.MILLING_INLAY.contains(stack.getItem());

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

    public MillingMachineTile() {
        this(ModTileEntities.MILLING_MACHINE_TILE.get());
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compoundNBT) {
        inputItemStackHandler.deserializeNBT(compoundNBT.getCompound("inv_inp"));
//        workPlaceItemStackHandler.deserializeNBT(compoundNBT.getCompound("inv_wp"));
        super.load(blockState, compoundNBT);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put("inv_inp", inputItemStackHandler.serializeNBT());
//        compound.put("inv_wp", workPlaceItemStackHandler.serializeNBT());
        return super.save(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == null) {
                return this.handler.cast();
            }
            if (side == Direction.UP) {
                return this.inputHandler.cast();
            }
            if (side == Direction.DOWN) {
                return this.outputHandler.cast();
            }

        }
        return super.getCapability(cap, side);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("screen.dustandash.milling_machine");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new MillingMachineContainer(id, playerInventory, this);
    }

    @Override
    public void tick() {
        if (!this.getLevel().isClientSide) {
            craft();

        }


    }


    public void craft() {
        Inventory inv = new Inventory(inputItemStackHandler.getSlots());
        for (int i = 0; i < inputItemStackHandler.getSlots(); i++) {
            inv.setItem(i, inputItemStackHandler.getStackInSlot(i));

        }

        Optional<MillingMachineRecipe> recipe = this.getLevel().getRecipeManager().getRecipeFor(ModRecipeTypes.MILLING_RECIPE, inv, this.getLevel());

        recipe.ifPresent(iRecipe -> {
            ItemStack output = iRecipe.getResultItem();

            if (iRecipe.isStep1()) {
//                if (isWorkPlaceEmpty()) {
                // Step1 option
                inputItemStackHandler.extractItem(0, 1, false);
                ItemStack temp = new ItemStack(output.getItem(), 1);
                for (int i = 3; i <= 27; i++) {
                    inputItemStackHandler.setStackInSlot(i, temp.copy());

                }

//                }


            } else if (inputItemStackHandler.getStackInSlot(2).getCount() == 0) {
                inputItemStackHandler.extractItem(1, 1, false);
                for (int i = 3; i <= 27; i++) {
                    inputItemStackHandler.extractItem(i, 1, false);

                }

                inputItemStackHandler.setStackInSlot(2, output.copy());


            }

            setChanged();
        });

    }

    public boolean isWorkPlaceEmpty() {
        for (int i = 3; i <= 27; i++) {
            if (inputItemStackHandler.getStackInSlot(i).getCount() > 0) {
                return false;
            }

        }
        return true;
    }


}
