package com.tonywww.dustandash.registry;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import com.tonywww.dustandash.platform.PlatformEventBus;
import com.tonywww.dustandash.platform.DeferredRegister;
import com.tonywww.dustandash.platform.PlatformRegistries;
import com.tonywww.dustandash.platform.RegistryObject;

public final class DAAParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(PlatformRegistries.PARTICLE_TYPES, DustAndAsh.MOD_ID);

    public static final RegistryObject<SimpleParticleType> DARK_SMOKE =
            PARTICLE_TYPES.register("dark_smoke", () -> new SimpleParticleType(false));

    private DAAParticles() {
    }

    public static void register(PlatformEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
