package com.tonywww.dustandash.entity;

import com.tonywww.dustandash.event.ModEntites;
import com.tonywww.dustandash.item.ModItems;
import com.tonywww.dustandash.item.custom.WhiteLightning;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

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
                float damage =  WhiteLightning.getExtraDamage(source) + 2F;
                if (isPowerful) {
                    damage += (livingEntity.getHealth() * WhiteLightning.getExtraPercentage(source));

                }
                entity.hurt(owner.damageSources().indirectMagic(entity, owner), damage);

            }

        }

        super.onHitEntity(result);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        this.discard();
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.EMPTY.get();
    }
}
