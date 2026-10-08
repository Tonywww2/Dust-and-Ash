package com.tonywww.dustandash.platform;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
public abstract class PlatformEntity extends Entity {
    protected PlatformEntity(EntityType<?> type, Level level) { super(type, level); }
    protected abstract void defineData(PlatformEntityData data);
    //? if forge {
    @Override protected void defineSynchedData() { defineData(new PlatformEntityData(entityData)); }
    @Override public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
        return net.minecraftforge.network.NetworkHooks.getEntitySpawningPacket(this);
    }
    //?} else {
    /*@Override protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
        defineData(new PlatformEntityData(builder));
    }
    *///?}
}
