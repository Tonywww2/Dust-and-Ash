package com.tonywww.dustandash.block.entity;

import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.menu.IntegratedBlockContainer;
import com.tonywww.dustandash.data.recipes.IntegratedBlockRecipe;
import com.tonywww.dustandash.util.ModTags;
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
import net.minecraft.world.inventory.BeaconMenu;
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

public class IntegratedBlockTile extends SyncedBlockEntity implements MenuProvider {

    public static int radius = 1;

    public ItemStackHandler itemStackHandler;
    private LazyOptional<ItemStackHandler> handler;

    public int currentLevel;
    private int coolDownTime;

    public IntegratedBlockTile(BlockPos pos, BlockState state) {
        super(ModTileEntities.INTEGRATED_BLOCK_TILE.get(), pos, state);
        this.itemStackHandler = createHandler();
        this.handler = LazyOptional.of(() -> itemStackHandler);
        this.currentLevel = 0;
        this.coolDownTime = -1;

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

    public static boolean isBeaconOn(Level level, BlockPos pos) {
        if (level.getBlockState(pos.below(2)).getBlock() == Blocks.BEACON) {
            //TODO
//            BlockState blockState = level.getBlockState(pos.below(2));

            return true;

        }
        return false;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.dustandash.integrated_block");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new IntegratedBlockContainer(id, playerInventory, this);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, IntegratedBlockTile be) {
        if (!level.isClientSide) {
            if (be.coolDownTime <= 0) {
                be.currentLevel = getStructureLevel(level, pos);
                be.coolDownTime = 4;

            }

            craft(level, pos, state, be);
            float chance = 0.2f;
            if (level.random.nextDouble() < chance) {
                List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, getArea(pos), VALID_ITEM_ENTITY);
                for (ItemEntity item : items) {
                    for (int i = 0; i < be.itemStackHandler.getSlots(); i++) {
                        if (be.itemStackHandler.getStackInSlot(i).getCount() == 0) {
                            be.itemStackHandler.insertItem(i, item.getItem().copy(), false);
                            item.getItem().setCount(item.getItem().getCount() - 1);
                            level.playSound(null, pos, SoundEvents.CHICKEN_HURT, SoundSource.BLOCKS, 0.25f, 1f);

                        }

                        if (item.getItem().getCount() == 0) {
                            break;
                        }
                    }

                }


            }
            be.coolDownTime--;

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

    public static void craft(Level level, BlockPos pos, BlockState state, IntegratedBlockTile be) {
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
                playParticles(level, pos, getStructureLevel(level, pos));
                ItemEntity itemEntity = new ItemEntity(
                        level,
                        pos.getX(),
                        pos.getY() + 1,
                        pos.getZ(),
                        output.copy()
                );

                level.addFreshEntity(itemEntity);
                if (isBeaconOn(level, pos)) {
                    level.addFreshEntity(itemEntity.copy());
                }

            }

            be.inventoryChanged();
        });

    }

    public static void playParticles(Level level, BlockPos pos, int lv) {
        switch (lv) {
            case 1:
                level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1f, 1f);
                break;

            case 2:
                level.playSound(null, pos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 1f, 1f);
                break;

            case 3:
                level.playSound(null, pos, SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS, 1f, 1f);
                break;

            default:
                level.playSound(null, pos, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 1f, 1f);
                break;
        }

    }

}