package com.tonywww.dustandash.block.entity;

import com.tonywww.dustandash.DustAndAshConfig;
import com.tonywww.dustandash.menu.IonizerContainerMenu;
import com.tonywww.dustandash.menu.itemhandlers.RestrictedItemHandler;
import com.tonywww.dustandash.data.recipes.IonizerRecipe;
import com.tonywww.dustandash.registry.DAAItems;
import com.tonywww.dustandash.registry.DAABlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;

public class IonizerEntity extends SyncedBlockEntity implements MenuProvider, DroppableInventory {

    public ItemStackHandler invItemStackHandler;
    private final ManagedCapability<ItemStackHandler> handler;
    private final ManagedCapability<RestrictedItemHandler> electrodeHandler;
    private final ManagedCapability<RestrictedItemHandler> inputHandler;
    private final Container recipeInput;
    private final RecipeManager.CachedCheck<Container, IonizerRecipe> recipeCheck;

    private float currentProgression;
    private int targetProgression;
    private BlockState below;
    private int isBelowAvailable;

    protected final ContainerData dataAccess;

    private IonizerRecipe currRecipe;


    public IonizerEntity(BlockPos pos, BlockState state) {
        super(DAABlockEntities.IONIZER_ENTITY.get(), pos, state);

        this.invItemStackHandler = createInputsHandler();

        this.handler = managedCapability(() -> invItemStackHandler);
        this.electrodeHandler = managedCapability(() -> new RestrictedItemHandler(invItemStackHandler,
            slot -> slot >= 4, slot -> false));
        this.inputHandler = managedCapability(() -> new RestrictedItemHandler(invItemStackHandler,
            slot -> slot < 4, slot -> false));
        this.recipeInput = new ItemHandlerContainerView(this.invItemStackHandler);
        this.recipeCheck = RecipeManager.createCheck(IonizerRecipe.IonizerRecipeType.INSTANCE);

        currentProgression = -1f;
        targetProgression = 0;

        isBelowAvailable = 1;

        this.dataAccess = new ContainerData() {
            public int get(int pIndex) {
                switch (pIndex) {
                    case 0:
                        return (int) IonizerEntity.this.currentProgression;
                    case 1:
                        return IonizerEntity.this.targetProgression;
                    case 2:
                        return IonizerEntity.this.isBelowAvailable;
                    default:
                        return 0;
                }
            }

            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0:
                        IonizerEntity.this.currentProgression = pValue;
                        break;
                    case 1:
                        IonizerEntity.this.targetProgression = pValue;
                        break;
                    case 2:
                        IonizerEntity.this.isBelowAvailable = pValue;
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
                inventoryChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot == 0) {
                    return DAAItems.ELECTRON.get().equals(stack.getItem());
                }

                return !DAAItems.ELECTRON.get().equals(stack.getItem());

            }

            @Override
            public int getSlotLimit(int slot) {
                if (slot >= 4) {
                    return 3;
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
                return this.electrodeHandler.get().cast();
            }
            if (side == Direction.NORTH || side == Direction.EAST || side == Direction.SOUTH || side == Direction.WEST) {
                return this.inputHandler.get().cast();
            }

        }
        return super.getCapability(cap, side);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.dustandash.ionizer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new IonizerContainerMenu(id, playerInventory, this, dataAccess);
    }

    @Override
    public ItemStackHandler getInventory() {
        return this.invItemStackHandler;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, IonizerEntity be) {
        if (!level.isClientSide) {
            updateBelow(level, pos, be);
            craft(level, pos, be);

        }

    }

    private static void updateBelow(Level level, BlockPos pos, IonizerEntity be) {
        be.below = level.getBlockState(pos.below());
        if (be.below != null && be.below.getBlock() != Blocks.AIR) {
            be.isBelowAvailable = 1;
        } else {
            be.isBelowAvailable = 0;
        }

    }

    public static void craft(Level level, BlockPos pos, IonizerEntity be) {
        Optional<IonizerRecipe> recipe = be.recipeCheck.getRecipeFor(be.recipeInput, level);
        if (recipe.isEmpty()) {
            be.currentProgression = -1;
            be.targetProgression = 0;
            be.currRecipe = null;
            return;
        }

        IonizerRecipe ionizerRecipe = recipe.get();
        boolean fullFluidBlock = !(be.below.getBlock() instanceof LiquidBlock)
                || be.below.getValue(LiquidBlock.LEVEL) == 0;
        if (!fullFluidBlock || be.below.getBlock() != ionizerRecipe.getInputBlock()) {
            be.currentProgression = -1;
            be.targetProgression = 0;
            be.currRecipe = null;
            return;
        }

        if (be.currRecipe == null || !be.currRecipe.getId().equals(ionizerRecipe.getId())) {
            be.currRecipe = ionizerRecipe;
            be.currentProgression = 1;
            be.targetProgression = ionizerRecipe.getTick();
        } else if (be.currentProgression >= be.targetProgression) {
            be.invItemStackHandler.extractItem(0, be.currRecipe.getPowerCost(), false);
            be.invItemStackHandler.extractItem(1, 1, false);
            be.invItemStackHandler.extractItem(2, 1, false);
            be.invItemStackHandler.extractItem(3, 1, false);

            if (be.currRecipe.isCostElectrodes()) {
                be.invItemStackHandler.extractItem(4, 1, false);
                be.invItemStackHandler.extractItem(5, 1, false);
            }

            level.setBlock(pos.below(), be.currRecipe.getResultBlock().defaultBlockState(), 2);
            level.updateNeighborsAt(pos.below(), be.currRecipe.getResultBlock());

            for (ItemStack output : be.currRecipe.getResultItemStacks()) {
                if (!output.isEmpty()) {
                    level.addFreshEntity(new ItemEntity(
                            level,
                            pos.getX() + 0.5f,
                            pos.getY() - 0.5f,
                            pos.getZ() + 0.5f,
                            output.copy()
                    ));
                }
            }

            be.currentProgression = -1;
            be.currRecipe = null;
            level.playSound(null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 0.5f, 1f);
        } else {
            be.currentProgression += DustAndAshConfig.MACHINES.ionizerProgressPerTick.get();

        }
        be.inventoryChanged();

    }


}
