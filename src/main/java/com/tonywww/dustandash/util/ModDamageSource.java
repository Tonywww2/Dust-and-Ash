package com.tonywww.dustandash.util;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;


public class ModDamageSource {

    private final Registry<DamageType> damageTypes;
    private final DamageSource sharpenFlint;

    public ModDamageSource(RegistryAccess registryAccess) {
        this.damageTypes = registryAccess.registryOrThrow(Registries.DAMAGE_TYPE);
        this.sharpenFlint = this.source(DamageTypes.MAGIC);
    }

    private DamageSource source(ResourceKey<DamageType> damageType) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(damageType));
    }

    public DamageSource sharpenFlint() {
        return this.sharpenFlint;
    }


}
