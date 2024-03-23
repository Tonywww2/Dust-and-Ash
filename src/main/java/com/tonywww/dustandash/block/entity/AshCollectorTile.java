package com.tonywww.dustandash.block.entity;

import com.tonywww.dustandash.menu.AshCollectorContainer;
import com.tonywww.dustandash.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AshCollectorTile extends SyncedBlockEntity implements MenuProvider{

    public final ItemStackHandler itemStackHandler = createHandler();
    private final LazyOptional<ItemStackHandler> handler = LazyOptional.of(() -> itemStackHandler);
    private int coolDownTime = -1;

    public AshCollectorTile(BlockPos pos, BlockState state) {
        super(ModTileEntities.ASH_COLLECTOR_TILE.get(), pos, state);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return (slot == 0 && stack.getItem() == ModItems.ASH.get()) ||
                        (slot == 1);
            }

            @Override
            public int getSlotLimit(int slot) {
                return 64;
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

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.handler.cast();

        }
        return super.getCapability(cap, side);
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

    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.dustandash.ash_collector");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new AshCollectorContainer(id, playerInventory, this);
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < itemStackHandler.getSlots(); ++i) {
            drops.add(itemStackHandler.getStackInSlot(i));
        }
        return drops;
    }

//    @Override
    public static void tick(Level level, BlockPos pos, BlockState state, AshCollectorTile be) {
        float chance = 0.001f;
        if (level != null && !level.isClientSide) {
            suckInItem(level, pos, state, be);

            if (level.random.nextDouble() < chance) {
                if (shouldWork(level, pos, state, be)) {
                    ItemStack itemStack = new ItemStack(ModItems.ASH.get());
                    be.itemStackHandler.insertItem(0, itemStack.copy(), false);
                    level.playSound(null, pos, SoundEvents.BEE_HURT, SoundSource.BLOCKS, 0.5f, 1f);
                    be.inventoryChanged();

                }

            }
        }
        be.coolDownTime--;

    }

    public static boolean shouldWork(Level level, BlockPos pos, BlockState state, AshCollectorTile be) {
        if (level.getBlockState(pos.above()).getBlock() instanceof AbstractFurnaceBlock) {
            BlockState block = level.getBlockState(pos.above());

            return block.getValue(BlockStateProperties.LIT);

        }
        return false;

    }

    private static void suckInItem(Level level, BlockPos pos, BlockState state, AshCollectorTile be) {
        if (be.coolDownTime <= 0 && be.itemStackHandler.getStackInSlot(1).getCount() < be.itemStackHandler.getSlotLimit(1)) {
            if (level.getBlockEntity(pos.above()) instanceof AbstractFurnaceBlockEntity tile) {
                ItemStack target = tile.getItem(2);

                if (be.itemStackHandler.getStackInSlot(1).isEmpty() || target.getItem() == be.itemStackHandler.getStackInSlot(1).getItem()) {
                    be.itemStackHandler.insertItem(1, tile.removeItem(2, 1), false);
                    be.coolDownTime = 4;

                }

            }
        }

    }

}
