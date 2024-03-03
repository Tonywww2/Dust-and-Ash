package com.tonywww.dustandash.tileentity;

import com.tonywww.dustandash.container.IonizerContainer;
import com.tonywww.dustandash.container.IonizerItemHandler;
import com.tonywww.dustandash.data.recipes.IonizerRecipe;
import com.tonywww.dustandash.data.recipes.ModRecipeTypes;
import com.tonywww.dustandash.item.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.item.ItemEntity;
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
import net.minecraft.util.IIntArray;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.tonywww.dustandash.config.DustAndAshConfig.centrifugeProgressPerTick;

public class IonizerTile extends TileEntity implements INamedContainerProvider, ITickableTileEntity {

    public ItemStackHandler invItemStackHandler;
    private final LazyOptional<ItemStackHandler> handler;
    private final LazyOptional<IonizerItemHandler> electrodeHandler;
    private final LazyOptional<IonizerItemHandler> inputHandler;

    private float currentProgression;
    private int targetProgression;
    private BlockState below;
    private int isBelowAvailable;

    protected final IIntArray dataAccess;

    private IonizerRecipe currRecipe;

    private double progressPerTick = centrifugeProgressPerTick.get();


    public IonizerTile(TileEntityType<?> tileEntityType) {
        super(tileEntityType);

        this.invItemStackHandler = createInputsHandler();

        this.handler = LazyOptional.of(() -> invItemStackHandler);
        this.electrodeHandler = LazyOptional.of(() -> new IonizerItemHandler(invItemStackHandler, Direction.UP));
        this.inputHandler = LazyOptional.of(() -> new IonizerItemHandler(invItemStackHandler, Direction.NORTH));

        currentProgression = -1f;
        targetProgression = 0;

        isBelowAvailable = 1;

        this.dataAccess = new IIntArray() {
            public int get(int pIndex) {
                switch (pIndex) {
                    case 0:
                        return (int) IonizerTile.this.currentProgression;
                    case 1:
                        return IonizerTile.this.targetProgression;
                    case 2:
                        return IonizerTile.this.isBelowAvailable;
                    default:
                        return 0;
                }
            }

            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0:
                        IonizerTile.this.currentProgression = pValue;
                        break;
                    case 1:
                        IonizerTile.this.targetProgression = pValue;
                        break;
                    case 2:
                        IonizerTile.this.isBelowAvailable = pValue;
                        break;
                }

            }

            public int getCount() {
                return 3;
            }
        };

    }

    // 0: power
    // 1: input
    // 2: container
    // 3: dust
    // 4 5: electrode
    private ItemStackHandler createInputsHandler() {
        return new ItemStackHandler(6) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot == 0) {
                    return ModItems.ELECTRON.get().equals(stack.getItem());
                }

                return !ModItems.ELECTRON.get().equals(stack.getItem());

            }

            @Override
            public int getSlotLimit(int slot) {
                if (slot >= 4) {
                    return 1;
                }
                return 64;
            }


        };
    }

    public IonizerTile() {
        this(ModTileEntities.IONIZER_TILE.get());
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
                return this.electrodeHandler.cast();
            }
            if (side == Direction.NORTH || side == Direction.EAST || side == Direction.SOUTH || side == Direction.WEST) {
                return this.inputHandler.cast();
            }

        }
        return super.getCapability(cap, side);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("screen.dustandash.ionizer");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new IonizerContainer(id, playerInventory, this, dataAccess);
    }

    @Override
    public void tick() {
        if (!this.getLevel().isClientSide) {
            updateBelow();
            craft();

        }

    }

    private void updateBelow() {
        this.below = this.getLevel().getBlockState(this.getBlockPos().below());
        if (this.below != null && this.below.getBlock() != Blocks.AIR) {
            this.isBelowAvailable = 1;
        } else {
            this.isBelowAvailable = 0;
        }

    }

    public void craft() {
        // 0 power 1-3 items 4-5 electrode
        Inventory inv = new Inventory(invItemStackHandler.getSlots());
        for (int i = 0; i < invItemStackHandler.getSlots(); i++) {
            inv.setItem(i, invItemStackHandler.getStackInSlot(i));

        }

        Optional<IonizerRecipe> recipe = this.getLevel().getRecipeManager().getRecipeFor(ModRecipeTypes.IONIZER_RECIPE, inv, this.getLevel());

        AtomicBoolean present = new AtomicBoolean(false);
        recipe.ifPresent(iRecipe -> {

            boolean isFullFluid = true;

            if (below.getBlock() instanceof FlowingFluidBlock) {
                FlowingFluidBlock fluidBlock = (FlowingFluidBlock) below.getBlock();
                if (below.getValue(fluidBlock.LEVEL) > 0) {
                    isFullFluid = false;

                }

            }
            if (isFullFluid && below.getBlock() == iRecipe.getInputBlock()) {
                present.set(true);

                if (currRecipe == null || currRecipe.getId() != iRecipe.getId()) {
                    currRecipe = iRecipe;
                    currentProgression = 1;
                    targetProgression = iRecipe.getTick();

                } else if (currRecipe.getId() == iRecipe.getId()) {
                    // complete crafting
                    if (currentProgression >= targetProgression) {
                        invItemStackHandler.extractItem(0, currRecipe.getPowerCost(), false);
                        invItemStackHandler.extractItem(1, 1, false);
                        invItemStackHandler.extractItem(2, 1, false);
                        invItemStackHandler.extractItem(3, 1, false);

                        if (currRecipe.isCostElectrodes()) {
                            invItemStackHandler.extractItem(4, 1, false);
                            invItemStackHandler.extractItem(5, 1, false);

                        }

                        // replace below
                        this.level.setBlock(this.getBlockPos().below(), currRecipe.getResultBlock().defaultBlockState(), 2);
                        this.level.updateNeighborsAt(this.getBlockPos().below(), currRecipe.getResultBlock());

                        // summon result
                        for (ItemStack i : currRecipe.getResultItemStacks()) {
                            ItemEntity itemEntity = new ItemEntity(
                                    this.level,
                                    this.getBlockPos().getX(),
                                    this.getBlockPos().getY() - 0.5f,
                                    this.getBlockPos().getZ(),
                                    i
                            );
                            this.level.addFreshEntity(itemEntity);

                        }

                        currentProgression = -1;
                        currRecipe = null;
                        this.level.playSound(null, this.getBlockPos(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.5f, 1f);

                    } else {
                        // ticking
                        currentProgression += progressPerTick;

                    }

                }
                setChanged();
            }

        });

        // reset
        if (!present.get()) {
            currentProgression = -1;
            targetProgression = 0;
            currRecipe = null;

        }

    }


}
