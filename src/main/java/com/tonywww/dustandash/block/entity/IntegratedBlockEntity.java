package com.tonywww.dustandash.block.entity;

import com.tonywww.dustandash.registeries.ModBlockEntities;
import com.tonywww.dustandash.registeries.ModBlocks;
import com.tonywww.dustandash.menu.IntegratedBlockContainerMenu;
import com.tonywww.dustandash.data.recipes.IntegratedBlockRecipe;
import com.tonywww.dustandash.tag.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class IntegratedBlockEntity extends BasicMachineEntity implements MenuProvider {

    public static int radius = 1;

    public ItemStackHandler itemStackHandler;
    private LazyOptional<ItemStackHandler> handler;
    private int currentLevel;
    private boolean isBeaconOn = false;

    protected final ContainerData dataAccess;

    public IntegratedBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.INTEGRATED_BLOCK_ENTITY.get(), pos, state);
        this.itemStackHandler = createHandler();
        this.handler = LazyOptional.of(() -> itemStackHandler);
        this.currentLevel = 0;

        this.dataAccess = new ContainerData() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0 -> {
                        return currentLevel;
                    }
                    case 1 -> {
                        return isBeaconOn ? 1 : 0;
                    }
                }

                return -1;
            }

            @Override
            public void set(int index, int val) {
                switch (index) {
                    case 0:
                        currentLevel = val;

                    case 1:
                        isBeaconOn = true;

                }

            }

            @Override
            public int getCount() {
                return 2;
            }
        };

    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(8) {
            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
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

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null || side == Direction.DOWN) {
                return this.handler.cast();

            }
        }
        return super.getCapability(cap, side);
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < itemStackHandler.getSlots(); ++i) {
            drops.add(itemStackHandler.getStackInSlot(i));
        }
        return drops;
    }

    /**
     * get the structure level
     *
     * @return the level (int)
     */
    public static int getStructureLevel(Level level, BlockPos pos) {
        int out = 0;
        if (
                level.getBlockState(pos.north()).getBlock() == ModBlocks.INTEGRATED_FRAME_1.get() &&
                        level.getBlockState(pos.east()).getBlock() == ModBlocks.INTEGRATED_FRAME_1.get() &&
                        level.getBlockState(pos.south()).getBlock() == ModBlocks.INTEGRATED_FRAME_1.get() &&
                        level.getBlockState(pos.west()).getBlock() == ModBlocks.INTEGRATED_FRAME_1.get()
        ) {
            out++;

            if (
                    level.getBlockState(pos.north().east()).getBlock() == ModBlocks.INTEGRATED_FRAME_2.get() &&
                            level.getBlockState(pos.north().west()).getBlock() == ModBlocks.INTEGRATED_FRAME_2.get() &&
                            level.getBlockState(pos.south().east()).getBlock() == ModBlocks.INTEGRATED_FRAME_2.get() &&
                            level.getBlockState(pos.south().west()).getBlock() == ModBlocks.INTEGRATED_FRAME_2.get()
            ) {
                out++;

                if (
                        level.getBlockState(pos.north(2)).getBlock() == ModBlocks.INTEGRATED_FRAME_3.get() &&
                                level.getBlockState(pos.east(2)).getBlock() == ModBlocks.INTEGRATED_FRAME_3.get() &&
                                level.getBlockState(pos.south(2)).getBlock() == ModBlocks.INTEGRATED_FRAME_3.get() &&
                                level.getBlockState(pos.west(2)).getBlock() == ModBlocks.INTEGRATED_FRAME_3.get()
                ) {
                    out++;
                }
            }
        }


        return out;
    }

    public boolean isBeaconOn() {
        return isBeaconOn;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.dustandash.integrated_block");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new IntegratedBlockContainerMenu(id, playerInventory, this, dataAccess);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, IntegratedBlockEntity be) {
        if (!level.isClientSide) {
            BasicMachineEntity.tick(be, 1);

            if (BasicMachineEntity.isWorkingTick(be)) {
                be.currentLevel = getStructureLevel(level, pos);
                be.isBeaconOn = level.getBlockEntity(be.getBlockPos().below(2)) instanceof BeaconBlockEntity;

                craft(level, pos, be);
                if (!level.hasNeighborSignal(pos)) {
                    collect(level, pos, be);

                }

                BasicMachineEntity.resetTicker(be);

            }


        }


    }

    private static void collect(Level level, BlockPos pos, IntegratedBlockEntity be) {
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, getArea(pos), VALID_ITEM_ENTITY);

        for (ItemEntity item : items) {
            for (int i = 0; i < be.itemStackHandler.getSlots(); i++) {
                if (be.itemStackHandler.getStackInSlot(i).getCount() == 0) {
                    be.itemStackHandler.insertItem(i, item.getItem().copy(), false);
                    item.getItem().shrink(1);
                    level.playSound(null, pos, SoundEvents.CHICKEN_HURT, SoundSource.BLOCKS, 0.25f, 1f);

                }

                if (item.getItem().getCount() == 0) {
                    break;
                }
            }

        }
    }

    // from thermal
    protected static final Predicate<ItemEntity> VALID_ITEM_ENTITY = item -> {
        if (!item.isAlive()) {
//        if (!item.isAlive() || item.hasPickUpDelay()) {
            return false;
        }

        return item.getItem().is(ModTags.Items.CRAFT_MATERIAL);
    };

    public static AABB getArea(BlockPos blockPos) {
        return new AABB(blockPos.offset(-radius, -1, -radius), blockPos.offset(1 + radius, 1 + radius, 1 + radius));
    }

    public static void craft(Level level, BlockPos pos, IntegratedBlockEntity be) {
        Container inv = new SimpleContainer(be.itemStackHandler.getSlots());
        for (int i = 0; i < be.itemStackHandler.getSlots(); i++) {
            inv.setItem(i, be.itemStackHandler.getStackInSlot(i));

        }

        Optional<IntegratedBlockRecipe> recipe = level.getRecipeManager().getRecipeFor(IntegratedBlockRecipe.IntegrateRecipeType.INSTANCE, inv, level);

        recipe.ifPresent(iRecipe -> {
            ItemStack output = iRecipe.getResultItem(null);

            if (be.currentLevel >= iRecipe.getLevel()) {
                // crafting
                for (int i = 0; i < be.itemStackHandler.getSlots(); i++) {
                    be.itemStackHandler.extractItem(i, 1, false);

                }
                be.playSound();
                ItemEntity itemEntity = new ItemEntity(
                        level,
                        pos.getX() + 0.5d,
                        pos.getY() + 1d,
                        pos.getZ() + 0.5d,
                        output.copy()
                );

                level.addFreshEntity(itemEntity);
                if (be.isBeaconOn()) {
                    level.addFreshEntity(itemEntity.copy());
                }

            }

            be.inventoryChanged();
        });

    }

    public void playSound() {
        switch (this.currentLevel) {
            case 1:
                level.playSound(null, this.getBlockPos(), SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.5f, 0.5f);
                break;

            case 2:
                level.playSound(null, this.getBlockPos(), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.5f, 0.5f);
                break;

            case 3:
                level.playSound(null, this.getBlockPos(), SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS, 0.5f, 0.5f);
                break;

            default:
                level.playSound(null, this.getBlockPos(), SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 0.5f, 0.5f);
                break;
        }

    }

}