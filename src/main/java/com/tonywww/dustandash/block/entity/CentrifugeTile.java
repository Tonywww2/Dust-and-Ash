package com.tonywww.dustandash.block.entity;

import com.tonywww.dustandash.menu.CentrifugeContainer;
import com.tonywww.dustandash.menu.CentrifugeItemHandler;
import com.tonywww.dustandash.data.recipes.CentrifugeRecipe;
import com.tonywww.dustandash.data.recipes.ModRecipe;
import com.tonywww.dustandash.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.tonywww.dustandash.config.DustAndAshConfig.centrifugeProgressPerTick;

public class CentrifugeTile extends SyncedBlockEntity implements MenuProvider {

    public ItemStackHandler invItemStackHandler;
    private final LazyOptional<ItemStackHandler> handler;
    private final LazyOptional<CentrifugeItemHandler> inputHandler;
    private final LazyOptional<CentrifugeItemHandler> outputHandler;

    private float currentProgression;
    private int targetProgression;

    protected final ContainerData dataAccess;

    private NonNullList<ItemStack> nextOutput;

    private double progressPerTick = centrifugeProgressPerTick.get();


    public CentrifugeTile(BlockPos pos, BlockState state) {
        super(ModTileEntities.CENTRIFUGE_TILE.get(), pos, state);

        this.invItemStackHandler = createInputsHandler();

        this.handler = LazyOptional.of(() -> invItemStackHandler);
        this.inputHandler = LazyOptional.of(() -> new CentrifugeItemHandler(invItemStackHandler, Direction.UP));
        this.outputHandler = LazyOptional.of(() -> new CentrifugeItemHandler(invItemStackHandler, Direction.DOWN));

        currentProgression = -1f;
        targetProgression = 0;

        this.dataAccess = new ContainerData() {
            public int get(int pIndex) {
                switch(pIndex) {
                    case 0:
                        return (int) CentrifugeTile.this.currentProgression;
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
                inventoryChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot == 0) {
                    return !stack.is(ModTags.Items.CENTRIFUGE_CATALYST);
                }
                if (slot == 1) {
                    return stack.is(ModTags.Items.CENTRIFUGE_CATALYST);
                }
                return false;

            }

            @Override
            public int getSlotLimit(int slot) {
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
    public TranslatableComponent getDisplayName() {
        return new TranslatableComponent("screen.dustandash.centrifuge");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new CentrifugeContainer(id, playerInventory, this, dataAccess);
    }


    public static void tick(Level level, BlockPos pos, BlockState state, CentrifugeTile be) {
        if (!be.getLevel().isClientSide) {
            if (isReadyForNext(be)) {
                craft(level, be);

            }
            tickProgression(level, pos, be);

        }

    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < invItemStackHandler.getSlots(); ++i) {
            drops.add(invItemStackHandler.getStackInSlot(i));
        }
        return drops;
    }

    private static void tickProgression(Level level, BlockPos pos, CentrifugeTile be) {
        if (be.currentProgression >= 1) {
            be.currentProgression += be.progressPerTick;

        }
        if (be.currentProgression > be.targetProgression) {
            be.currentProgression = -1f;
            be.targetProgression = 0;
            be.setOutput(level, pos, be);

        }

    }

    /**
     * Is not working
     *
     * @return
     */
    private static boolean isReadyForNext(CentrifugeTile be) {
        // is working
        if (be.currentProgression > 0) {
            return false;
        } else {
            // is output empty
            for (int i = 2; i <= 9; i++) {
                if (be.invItemStackHandler.getStackInSlot(i).getCount() > 0) {
                    return false;

                }

            }

        }
        return true;

    }

    public static void craft(Level level, CentrifugeTile be) {
        Container inv = new SimpleContainer(be.invItemStackHandler.getSlots());
        for (int i = 0; i < be.invItemStackHandler.getSlots(); i++) {
            inv.setItem(i, be.invItemStackHandler.getStackInSlot(i));

        }

        Optional<CentrifugeRecipe> recipe = level.getRecipeManager().getRecipeFor(CentrifugeRecipe.CentrifugeRecipeType.INSTANCE, inv, level);

        recipe.ifPresent(iRecipe -> {
            be.invItemStackHandler.extractItem(0, 1, false);
            be.invItemStackHandler.extractItem(1, 1, false);
            be.nextOutput = iRecipe.getResultItemStacks();

            be.currentProgression = 1;
            be.targetProgression = recipe.get().getTick();

            be.inventoryChanged();
        });

    }

    public static void setOutput(Level level, BlockPos pos, CentrifugeTile be) {
        for (int i = 2; i <= 9; i++) {
            ItemStack temp = be.nextOutput.get(i - 2);
            if (temp != ItemStack.EMPTY) {
                be.invItemStackHandler.setStackInSlot(i, temp.copy());

            }

        }
        level.playSound(null, pos, SoundEvents.WITCH_DRINK, SoundSource.BLOCKS, 0.5f, 1f);
        be.inventoryChanged();

    }

}
