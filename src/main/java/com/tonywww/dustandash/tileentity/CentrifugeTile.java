package com.tonywww.dustandash.tileentity;

import com.tonywww.dustandash.container.CentrifugeContainer;
import com.tonywww.dustandash.container.CentrifugeItemHandler;
import com.tonywww.dustandash.data.recipes.CentrifugeRecipe;
import com.tonywww.dustandash.data.recipes.ModRecipeTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class CentrifugeTile extends TileEntity implements INamedContainerProvider, ITickableTileEntity {

    public ItemStackHandler invItemStackHandler;
    private final LazyOptional<ItemStackHandler> handler;
    private final LazyOptional<CentrifugeItemHandler> inputHandler;
    private final LazyOptional<CentrifugeItemHandler> outputHandler;

    private int currentProgression;
    private int targetProgression;

    protected final IIntArray dataAccess;

    private NonNullList<Ingredient> nextOutput;


    public CentrifugeTile(TileEntityType<?> tileEntityType) {
        super(tileEntityType);

        this.invItemStackHandler = createInputsHandler();

        this.handler = LazyOptional.of(() -> invItemStackHandler);
        this.inputHandler = LazyOptional.of(() -> new CentrifugeItemHandler(invItemStackHandler, Direction.UP));
        this.outputHandler = LazyOptional.of(() -> new CentrifugeItemHandler(invItemStackHandler, Direction.DOWN));

        currentProgression = -1;
        targetProgression = 0;

        this.dataAccess = new IIntArray() {
            public int get(int pIndex) {
                switch(pIndex) {
                    case 0:
                        return CentrifugeTile.this.currentProgression;
                    case 1:
                        return CentrifugeTile.this.targetProgression;
                    default:
                        return 0;
                }
            }

            public void set(int pIndex, int pValue) {
                switch(pIndex) {
                    case 0:
                        CentrifugeTile.this.currentProgression = pValue;
                        break;
                    case 1:
                        CentrifugeTile.this.targetProgression = pValue;
                        break;
                }

            }

            public int getCount() {
                return 2;
            }
        };

    }

    // 0: input 1
    // 1: input 2
    // 2-9: output
    private ItemStackHandler createInputsHandler() {
        return new ItemStackHandler(10) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return slot < 2;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 64;
            }


        };
    }

    public CentrifugeTile() {
        this(ModTileEntities.CENTRIFUGE_TILE.get());
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compoundNBT) {
        invItemStackHandler.deserializeNBT(compoundNBT.getCompound("inv"));
        super.load(blockState, compoundNBT);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put("inv", invItemStackHandler.serializeNBT());
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
        return new TranslationTextComponent("screen.dustandash.centrifuge");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new CentrifugeContainer(id, playerInventory, this, dataAccess);
    }

    @Override
    public void tick() {
        if (!this.getLevel().isClientSide) {
            if (isReadyForNext()) {
                craft();

            }
            tickProgression();

        }

    }

    private void tickProgression() {
        if (currentProgression >= 1) {
            currentProgression++;

        }
        if (currentProgression > targetProgression) {
            currentProgression = -1;
            targetProgression = 0;
            setOutput();

        }

    }

    /**
     * Is not working
     *
     * @return
     */
    private boolean isReadyForNext() {
        // is working
        if (currentProgression > 0) {
            return false;
        } else {
            // is output empty
            for (int i = 2; i <= 7; i++) {
                if (invItemStackHandler.getStackInSlot(i).getCount() > 0) {
                    return false;

                }

            }

        }
        return true;

    }

    public void craft() {
        Inventory inv = new Inventory(invItemStackHandler.getSlots());
        for (int i = 0; i < invItemStackHandler.getSlots(); i++) {
            inv.setItem(i, invItemStackHandler.getStackInSlot(i));

        }

        Optional<CentrifugeRecipe> recipe = this.getLevel().getRecipeManager().getRecipeFor(ModRecipeTypes.CENTRIFUGE_RECIPE, inv, this.getLevel());

        recipe.ifPresent(iRecipe -> {
            invItemStackHandler.extractItem(0, 1, false);
            invItemStackHandler.extractItem(1, 1, false);
            nextOutput = iRecipe.getResultIngredient();

            currentProgression = 1;
            targetProgression = recipe.get().getTick();

            setChanged();
        });

    }

    public void setOutput() {
        for (int i = 2; i <= 9; i++) {
            Ingredient temp = nextOutput.get(i - 2);
            if (!temp.isEmpty()) {
                ItemStack itemStack = temp.getItems()[0].copy();
                itemStack.setCount(1);
                invItemStackHandler.setStackInSlot(i, itemStack);

            }

        }
        setChanged();

    }

}
