package com.tonywww.dustandash.platform;

import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
public final class PlatformEntities {
    private PlatformEntities() {}
    public static Entity parent(Entity entity) {
        //? if forge {
        return entity instanceof net.minecraftforge.entity.PartEntity<?> part ? part.getParent() : entity;
        //?} else {
        /*return entity instanceof net.neoforged.neoforge.entity.PartEntity<?> part ? part.getParent() : entity;
        *///?}
    }
    public static void ignite(Entity entity, int seconds) {
        //? if forge {
        entity.setSecondsOnFire(seconds);
        //?} else {
        /*entity.igniteForSeconds(seconds);
        *///?}
    }
    public static SoundEvent explosionSound() {
        //? if forge {
        return SoundEvents.GENERIC_EXPLODE;
        //?} else {
        /*return SoundEvents.GENERIC_EXPLODE.value();
        *///?}
    }
}
