package com.tonywww.dustandash.entity;

import com.tonywww.dustandash.item.ModItems;
import com.tonywww.dustandash.item.custom.WhiteLightning;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import static com.tonywww.dustandash.item.custom.WhiteLightning.PARTICLE_BLUE;

public class LightningProjectileEntity extends ThrowableItemProjectile {

    private ItemStack source;
    private boolean isPowerful;

    public LightningProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public LightningProjectileEntity(Level level) {
        super(ModEntites.LIGHTNING_BULLET.get(), level);
    }

    public LightningProjectileEntity(Level level, LivingEntity livingEntity, ItemStack source, boolean isPowerful) {
        super(ModEntites.LIGHTNING_BULLET.get(), livingEntity, level);
        this.source = source;
        this.isPowerful = isPowerful;
        this.setNoGravity(true);

    }

    @Override
    protected void onHitEntity(EntityHitResult result) {

        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            Entity entity = result.getEntity();
            Entity owner = this.getOwner();
            if (entity instanceof LivingEntity livingEntity) {
                if (source == null || source.isEmpty()) {
                    source = ModItems.WHITE_LIGHTNING.get().getDefaultInstance();

                }
                float baseDamage =  WhiteLightning.getExtraDamage(source) + ((SwordItem) source.getItem()).getDamage();
                float extraDamage = 0;

                entity.invulnerableTime = 0;
                if (isPowerful) {
                    extraDamage = (livingEntity.getHealth() * WhiteLightning.getExtraPercentage(source));
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 4));
                    ((ServerLevel) this.level()).sendParticles(
                            PARTICLE_BLUE,
                            entity.getX(),
                            entity.getY() + 0.5d,
                            entity.getZ(),
                            5,
                            0.5d,
                            0.5d,
                            0.5d,
                            0
                    );
                    entity.hurt(owner.damageSources().indirectMagic(entity, owner), baseDamage + extraDamage);
                    entity.invulnerableTime = 0;

                }

                if (owner instanceof Player player) {
                    entity.hurt(owner.damageSources().playerAttack(player), baseDamage);
                    entity.invulnerableTime = 0;

                }


            }

        }

        super.onHitEntity(result);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!this.level().isClientSide()) {
            Explosion explosion = new Explosion(this.level(), getOwner(), result.getLocation().x(), result.getLocation().y() + 1.0d, result.getLocation().z(), 1.5f, true, Explosion.BlockInteraction.KEEP);
            ((ServerLevel) this.level()).sendParticles(
                    ParticleTypes.EXPLOSION,
                    result.getLocation().x(),
                    result.getLocation().y() + 0.25d,
                    result.getLocation().z(),
                    2,
                    0,
                    0,
                    0,
                    1
            );
            explosion.explode();
        }

        this.discard();
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.EMPTY.get();
    }
}
