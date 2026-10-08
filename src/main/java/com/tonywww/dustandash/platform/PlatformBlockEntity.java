package com.tonywww.dustandash.platform;


import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import com.tonywww.dustandash.platform.PlatformOptional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Copy from Farmer's Delight. Simple BlockEntity with networking boilerplate.
 */
public class PlatformBlockEntity extends BlockEntity {
    private final List<ManagedCapability<?>> managedCapabilities = new ArrayList<>();

    public PlatformBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    protected final <T> ManagedCapability<T> managedCapability(Supplier<T> supplier) {
        ManagedCapability<T> capability = new ManagedCapability<>(supplier);
        this.managedCapabilities.add(capability);
        return capability;
    }

    public <T> PlatformOptional<T> getCapability(PlatformCapability<T> cap) { return getCapability(cap,null); }
    public <T> PlatformOptional<T> getCapability(PlatformCapability<T> cap, net.minecraft.core.Direction side) {
        return PlatformOptional.empty();
    }
//? if forge {
@Override public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> cap, net.minecraft.core.Direction side) {
    if (cap == net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER) return getCapability(PlatformCapabilities.ITEM_HANDLER, side).nativeOptional().cast();
    if (cap == net.minecraftforge.common.capabilities.ForgeCapabilities.ENERGY) return getCapability(PlatformCapabilities.ENERGY, side).nativeOptional().cast();
    return super.getCapability(cap, side);
}
@Override public void invalidateCaps() { super.invalidateCaps(); managedCapabilities.forEach(ManagedCapability::invalidate); }
@Override public void reviveCaps() { super.reviveCaps(); managedCapabilities.forEach(ManagedCapability::revive); }
@Override public void load(CompoundTag tag) { super.load(tag); }
@Override protected void saveAdditional(CompoundTag tag) { super.saveAdditional(tag); }
@Override public CompoundTag getUpdateTag() { return saveWithoutMetadata(); }
@Override public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) { load(packet.getTag()); }
//?} else {
/*public void load(CompoundTag tag) {}
protected void saveAdditional(CompoundTag tag) {}
@Override public void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    PlatformHooks.withRegistries(registries, () -> load(tag));
}
@Override protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    PlatformHooks.withRegistries(registries, () -> saveAdditional(tag));
}
@Override public CompoundTag getUpdateTag(net.minecraft.core.HolderLookup.Provider registries) { return saveWithoutMetadata(registries); }
@Override public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet, net.minecraft.core.HolderLookup.Provider registries) {
    PlatformHooks.withRegistries(registries, () -> load(packet.getTag()));
}
*///?}
    @Override public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }

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
        private PlatformOptional<T> optional;

        private ManagedCapability(Supplier<T> supplier) {
            this.supplier = supplier;
            this.optional = createOptional();
        }

        public PlatformOptional<T> get() {
            return this.optional;
        }

        private PlatformOptional<T> createOptional() {
            return PlatformOptional.of(this.supplier::get);
        }

        private void invalidate() {
            this.optional.invalidate();
        }

        private void revive() {
            this.optional = createOptional();
        }
    }
}
