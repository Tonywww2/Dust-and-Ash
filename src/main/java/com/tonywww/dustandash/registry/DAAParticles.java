package com.tonywww.dustandash.registry;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class DAAParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, DustAndAsh.MOD_ID);

    public static final RegistryObject<SimpleParticleType> DARK_SMOKE =
            PARTICLE_TYPES.register("dark_smoke", () -> new SimpleParticleType(false));

    private DAAParticles() {
    }

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}