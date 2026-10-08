package com.tonywww.dustandash.platform;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
public final class PlatformCapabilities {
    public static final PlatformCapability<IItemHandler> ITEM_HANDLER = new PlatformCapability<>();
    public static final PlatformCapability<IEnergyStorage> ENERGY = new PlatformCapability<>();
    private PlatformCapabilities() {}
    public static PlatformOptional<IItemHandler> items(BlockEntity entity, Direction side) {
//? if forge {
return PlatformOptional.of(() -> entity.getCapability(net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER, side).resolve().map(IItemHandler::wrap).orElse(null));
//?} else {
/*return PlatformOptional.of(() -> {
        if (entity.getLevel() == null) return null;
        var handler = entity.getLevel().getCapability(net.neoforged.neoforge.capabilities.Capabilities.ItemHandler.BLOCK, entity.getBlockPos(), side);
        return handler == null ? null : IItemHandler.wrap(handler);
    });
*///?}
    }
    public static PlatformOptional<IEnergyStorage> energy(BlockEntity entity, Direction side) {
//? if forge {
var storage = entity.getCapability(net.minecraftforge.common.capabilities.ForgeCapabilities.ENERGY, side).orElse(null);
//?} else {
/*var storage = entity.getLevel() == null ? null : entity.getLevel().getCapability(net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage.BLOCK, entity.getBlockPos(), side);
*///?}

        if (storage == null) return PlatformOptional.empty();
        return PlatformOptional.of(() -> new IEnergyStorage() {
            public int receiveEnergy(int amount, boolean simulate) { return storage.receiveEnergy(amount, simulate); }
            public int extractEnergy(int amount, boolean simulate) { return storage.extractEnergy(amount, simulate); }
            public int getEnergyStored() { return storage.getEnergyStored(); }
            public int getMaxEnergyStored() { return storage.getMaxEnergyStored(); }
            public boolean canExtract() { return storage.canExtract(); }
            public boolean canReceive() { return storage.canReceive(); }
        });
    }
}
