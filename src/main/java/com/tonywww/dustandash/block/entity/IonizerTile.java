package com.tonywww.dustandash.block.entity;

import com.tonywww.dustandash.menu.IonizerContainer;
import com.tonywww.dustandash.menu.IonizerItemHandler;
import com.tonywww.dustandash.data.recipes.IonizerRecipe;
import com.tonywww.dustandash.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
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
import java.util.concurrent.atomic.AtomicBoolean;

import static com.tonywww.dustandash.config.DustAndAshConfig.ionizerProgressPerTick;

public class IonizerTile extends SyncedBlockEntity implements MenuProvider {

    public ItemStackHandler invItemStackHandler;
    private final LazyOptional<ItemStackHandler> handler;
    private final LazyOptional<IonizerItemHandler> electrodeHandler;
    private final LazyOptional<IonizerItemHandler> inputHandler;

    private float currentProgression;
    private int targetProgression;
    private BlockState below;
    private int isBelowAvailable;

    protected final ContainerData dataAccess;

    private IonizerRecipe currRecipe;

    private double progressPerTick = ionizerProgressPerTick.get();


    public IonizerTile(BlockPos pos, BlockState state) {
        super(ModTileEntities.IONIZER_TILE.get(), pos, state);

        this.invItemStackHandler = createInputsHandler();

        this.handler = LazyOptional.of(() -> invItemStackHandler);
        this.electrodeHandler = LazyOptional.of(() -> new IonizerItemHandler(invItemStackHandler, Direction.UP));
        this.inputHandler = LazyOptional.of(() -> new IonizerItemHandler(invItemStackHandler, Direction.NORTH));

        currentProgression = -1f;
        targetProgression = 0;

        isBelowAvailable = 1;

        this.dataAccess = new ContainerData() {
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
                inventoryChanged();
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
    public Component getDisplayName() {
        return Component.translatable("screen.dustandash.ionizer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new IonizerContainer(id, playerInventory, this, dataAccess);
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < invItemStackHandler.getSlots(); ++i) {
            drops.add(invItemStackHandler.getStackInSlot(i));
        }
        return drops;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, IonizerTile be) {
        if (!level.isClientSide) {
            updateBelow(level, pos, be);
            craft(level, pos, be);

        }

    }

    private static void updateBelow(Level level, BlockPos pos, IonizerTile be) {
        be.below = level.getBlockState(pos.below());
        if (be.below != null && be.below.getBlock() != Blocks.AIR) {
            be.isBelowAvailable = 1;
        } else {
            be.isBelowAvailable = 0;
        }

    }

    public static void craft(Level level, BlockPos pos, IonizerTile be) {
        // 0 power 1-3 items 4-5 electrode
        Container inv = new SimpleContainer(be.invItemStackHandler.getSlots());
        for (int i = 0; i < be.invItemStackHandler.getSlots(); i++) {
            inv.setItem(i, be.invItemStackHandler.getStackInSlot(i));

        }

        Optional<IonizerRecipe> recipe = level.getRecipeManager().getRecipeFor(IonizerRecipe.IonizerRecipeType.INSTANCE, inv, level);

        AtomicBoolean present = new AtomicBoolean(false);
        recipe.ifPresent(iRecipe -> {

            boolean isFullFluid = true;

            if (be.below.getBlock() instanceof LiquidBlock) {
//                LiquidBlock fluidBlock = (LiquidBlock) be.below.getBlock();
                if (be.below.getValue(LiquidBlock.LEVEL) > 0) {
                    isFullFluid = false;

                }

            }
            if (isFullFluid && be.below.getBlock() == iRecipe.getInputBlock()) {
                present.set(true);

                if (be.currRecipe == null || be.currRecipe.getId() != iRecipe.getId()) {
                    be.currRecipe = iRecipe;
                    be.currentProgression = 1;
                    be.targetProgression = iRecipe.getTick();

                } else if (be.currRecipe.getId() == iRecipe.getId()) {
                    // complete crafting
                    if (be.currentProgression >= be.targetProgression) {
                        be.invItemStackHandler.extractItem(0, be.currRecipe.getPowerCost(), false);
                        be.invItemStackHandler.extractItem(1, 1, false);
                        be.invItemStackHandler.extractItem(2, 1, false);
                        be.invItemStackHandler.extractItem(3, 1, false);

                        if (be.currRecipe.isCostElectrodes()) {
                            be.invItemStackHandler.extractItem(4, 1, false);
                            be.invItemStackHandler.extractItem(5, 1, false);

                        }

                        // replace below
                        level.setBlock(pos.below(), be.currRecipe.getResultBlock().defaultBlockState(), 2);
                        level.updateNeighborsAt(pos.below(), be.currRecipe.getResultBlock());

                        // summon result
                        for (ItemStack i : be.currRecipe.getResultItemStacks()) {
                            ItemEntity itemEntity = new ItemEntity(
                                    level,
                                    pos.getX() + 0.5f,
                                    pos.getY() - 0.5f,
                                    pos.getZ() + 0.5f,
                                    i.copy()
                            );
                            level.addFreshEntity(itemEntity);

                        }

                        be.currentProgression = -1;
                        be.currRecipe = null;
                        level.playSound(null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 0.5f, 1f);

                    } else {
                        // ticking
                        be.currentProgression += be.progressPerTick;

                    }

                }
                be.inventoryChanged();
            }

        });

        // reset
        if (!present.get()) {
            be.currentProgression = -1;
            be.targetProgression = 0;
            be.currRecipe = null;

        }

    }


}
