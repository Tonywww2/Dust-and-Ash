package com.tonywww.dustandash.platform;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
//? if forge {
public record PlatformEntityData(SynchedEntityData data) {
//?} else {
/*public record PlatformEntityData(SynchedEntityData.Builder data) {
*///?}
    public <T> void define(EntityDataAccessor<T> accessor, T value) { data.define(accessor, value); }
}
