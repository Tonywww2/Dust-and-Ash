package com.tonywww.dustandash.block.entity;

import com.tonywww.dustandash.DustAndAshConfig;
import com.tonywww.dustandash.menu.CentrifugeContainerMenu;
import com.tonywww.dustandash.menu.itemhandlers.RestrictedItemHandler;
import com.tonywww.dustandash.data.recipes.CentrifugeRecipe;
import com.tonywww.dustandash.registry.DAABlockEntities;
import com.tonywww.dustandash.tag.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class CentrifugeEntity extends BasicMachineEntity implements MenuProvider, DroppableInventory {

    public ItemStackHandler invItemStackHandler;
    private final ManagedCapability<ItemStackHandler> handler;
    private final ManagedCapability<RestrictedItemHandler> inputHandler;
    private final ManagedCapability<RestrictedItemHandler> outputHandler;
    private final Container recipeInput;
    private final RecipeManager.CachedCheck<Container, CentrifugeRecipe> recipeCheck;

    private float currentProgression;
    private int targetProgression;

    protected final ContainerData dataAccess;

    private NonNullList<ItemStack> nextOutput;


    public CentrifugeEntity(BlockPos pos, BlockState state) {
        super(DAABlockEntities.CENTRIFUGE_ENTITY.get(), pos, state);

        this.invItemStackHandler = createInputsHandler();

        this.handler = managedCapability(() -> invItemStackHandler);
        this.inputHandler = managedCapability(() -> new RestrictedItemHandler(invItemStackHandler,
            slot -> slot < 2, slot -> false));
        this.outputHandler = managedCapability(() -> new RestrictedItemHandler(invItemStackHandler,
            slot -> false, slot -> slot >= 2));
        this.recipeInput = new ItemHandlerContainerView(this.invItemStackHandler);
        this.recipeCheck = RecipeManager.createCheck(CentrifugeRecipe.CentrifugeRecipeType.INSTANCE);

        this.currentProgression = -1f;
        this.targetProgression = 0;


        this.dataAccess = new ContainerData() {
            public int get(int pIndex) {
                switch (pIndex) {
                    case 0:
                        return (int) CentrifugeEntity.this.currentProgression;
                    case 1:
                        return CentrifugeEntity.this.targetProgression;
                    default:
                        return 0;
                }
            }

            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0:
                        CentrifugeEntity.this.currentProgression = pValue;
                        break;
                    case 1:
                        CentrifugeEntity.this.targetProgression = pValue;
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
        if (!this.remove && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return this.handler.get().cast();
            }
            if (side == Direction.UP) {
                return this.inputHandler.get().cast();
            }
            if (side == Direction.DOWN) {
                return this.outputHandler.get().cast();
            }

        }
        return super.getCapability(cap, side);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.dustandash.centrifuge");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new CentrifugeContainerMenu(id, playerInventory, this, dataAccess);
    }


    public static void tick(Level level, BlockPos pos, BlockState state, CentrifugeEntity be) {
        if (!level.isClientSide) {
            if (be.advanceWorkCycle(DustAndAshConfig.MACHINES.centrifugeProgressPerTick.get())) {
                if (isReadyForNext(be)) {
                    craft(level, be);

                }

                be.resetWorkCycle();

            }
            tickProgression(level, pos, be);


        }

    }

    @Override
    public ItemStackHandler getInventory() {
        return this.invItemStackHandler;
    }

    private static void tickProgression(Level level, BlockPos pos, CentrifugeEntity be) {
        if (be.currentProgression >= 1) {
            be.currentProgression += DustAndAshConfig.MACHINES.centrifugeProgressPerTick.get();

        }
        if (be.currentProgression > be.targetProgression) {
            be.currentProgression = -1f;
            be.targetProgression = 0;
            setOutput(level, pos, be);

        }

    }

    /**
     * Is not working
     *
     * @return
     */
    private static boolean isReadyForNext(CentrifugeEntity be) {
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

    public static void craft(Level level, CentrifugeEntity be) {
        Optional<CentrifugeRecipe> recipe = be.recipeCheck.getRecipeFor(be.recipeInput, level);

        recipe.ifPresent(iRecipe -> {
            be.invItemStackHandler.extractItem(0, 1, false);
            be.invItemStackHandler.extractItem(1, 1, false);
            be.nextOutput = iRecipe.getResultItemStacks();

            be.currentProgression = 1;
            be.targetProgression = iRecipe.getTick();

            be.inventoryChanged();
        });

    }

    public static void setOutput(Level level, BlockPos pos, CentrifugeEntity be) {
        for (int i = 2; i <= 9; i++) {
            ItemStack temp = be.nextOutput.get(i - 2);
            if (!temp.isEmpty()) {
                be.invItemStackHandler.setStackInSlot(i, temp.copy());

            }

        }
        level.playSound(null, pos, SoundEvents.BRUSH_SAND, SoundSource.BLOCKS, 0.5f, 1f);
        be.inventoryChanged();

    }

}
