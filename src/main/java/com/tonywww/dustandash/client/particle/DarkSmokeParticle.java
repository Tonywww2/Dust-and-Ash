package com.tonywww.dustandash.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public final class DarkSmokeParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    private DarkSmokeParticle(
            ClientLevel level,
            double x,
            double y,
            double z,
            double velocityX,
            double velocityY,
            double velocityZ,
            SpriteSet sprites) {
        super(level, x, y, z, velocityX, velocityY, velocityZ);
        this.sprites = sprites;
        this.friction = 0.92f;
        this.gravity = -0.002f;
        this.lifetime = 16 + this.random.nextInt(9);
        this.quadSize = 0.12f + this.random.nextFloat() * 0.08f;
        this.rCol = 0.025f;
        this.gCol = 0.02f;
        this.bCol = 0.035f;
        this.alpha = 0.82f;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed) {
            this.setSpriteFromAge(this.sprites);
            this.alpha = 0.82f * (1f - (float) this.age / this.lifetime);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static final class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(
                SimpleParticleType type,
                ClientLevel level,
                double x,
                double y,
                double z,
                double velocityX,
                double velocityY,
                double velocityZ) {
            return new DarkSmokeParticle(
                    level, x, y, z, velocityX, velocityY, velocityZ, this.sprites);
        }
    }
}