package com.tonywww.dustandash.damage;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public final class DAADamageTypes {
    public static final ResourceKey<DamageType> LIGHT = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            new ResourceLocation(DustAndAsh.MOD_ID, "light"));
        public static final ResourceKey<DamageType> JUDGEMENT_REFLECTION = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            new ResourceLocation(DustAndAsh.MOD_ID, "judgement_reflection"));

    private DAADamageTypes() {
    }

    public static DamageSource light(ServerLevel level, Entity directEntity, LivingEntity owner) {
        return new DamageSource(
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(LIGHT),
                directEntity,
                owner);
    }

    public static DamageSource judgementReflection(ServerLevel level, LivingEntity owner) {
        return new DamageSource(
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(JUDGEMENT_REFLECTION),
                owner,
                owner);
    }
}