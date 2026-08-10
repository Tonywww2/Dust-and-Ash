package com.tonywww.dustandash.block.entity;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Copy from Farmer's Delight. Simple BlockEntity with networking boilerplate.
 */
public class SyncedBlockEntity extends BlockEntity {
    private final List<ManagedCapability<?>> managedCapabilities = new ArrayList<>();

    public SyncedBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    protected final <T> ManagedCapability<T> managedCapability(Supplier<T> supplier) {
        ManagedCapability<T> capability = new ManagedCapability<>(supplier);
        this.managedCapabilities.add(capability);
        return capability;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.managedCapabilities.forEach(ManagedCapability::invalidate);
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.managedCapabilities.forEach(ManagedCapability::revive);
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

    public void inventoryChanged() {
        setChanged();
    }

    public void syncToClient() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    protected static final class ManagedCapability<T> {
        private final Supplier<T> supplier;
        private LazyOptional<T> optional;

        private ManagedCapability(Supplier<T> supplier) {
            this.supplier = supplier;
            this.optional = createOptional();
        }

        public LazyOptional<T> get() {
            return this.optional;
        }

        private LazyOptional<T> createOptional() {
            return LazyOptional.of(this.supplier::get);
        }

        private void invalidate() {
            this.optional.invalidate();
        }

        private void revive() {
            this.optional = createOptional();
        }
    }
}