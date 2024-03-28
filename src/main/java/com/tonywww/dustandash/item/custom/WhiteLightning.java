package com.tonywww.dustandash.item.custom;

import com.tonywww.dustandash.config.DustAndAshConfig;
import com.tonywww.dustandash.entity.LightningProjectileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class WhiteLightning extends SwordItem {

    private static final String ATTACK_COUNTS = "attack_counts";
    private static final String CHARGES_TAG = "charges";
    private static final String ADVANCED_CHARGES_TAG = "advanced_charges";
    private static final String EXTRA_DAMAGE_TAG = "extra_damage";
    private static final String EXTRA_PERCENTAGE_TAG = "extra_percentage";

    private static final int MAX_CHARGE = 16;
    private static final int ADVANCED_MAX_CHARGE = 10;

    public static final DustParticleOptions PARTICLE_BLUE = new DustParticleOptions(new Vector3f(0, 1f, 1f), 2.0F);

    public WhiteLightning(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);

    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        Level level = player.level();

        if (!level.isClientSide()) {
            boolean damageFlag = false;
            boolean lightningFlag = false;
            float targetHealth = 0;

            // normal mode
            if (entity instanceof LivingEntity livingEntity && player.getAttackStrengthScale(0.2f) >= 1) {
                if (getAttackCounts(stack) >= 2) {
                    setAttackCounts(stack, getAttackCounts(stack) - 2);
                    setCharges(stack, getCharges(stack) + 2);

                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 0));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 160, 0));
                    lightningFlag = true;

                } else {
                    setAttackCounts(stack, getAttackCounts(stack) + 1);

                }
                if (getAdvCharges(stack) > 0) {
                    // under release
                    damageFlag = true;
                    targetHealth = livingEntity.getHealth();

                    ((ServerLevel) level).sendParticles(
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

                    if (getAdvCharges(stack) <= 0) {
                        // no adv charges
                        level.playSound(null, player.blockPosition(), SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 1f, 1f);

                    }

                }

            }

            if (damageFlag) {
                float damage = (targetHealth * getExtraPercentage(stack)) + getExtraDamage(stack);
                entity.hurt(player.damageSources().indirectMagic(entity, player), damage);
                setAdvCharges(stack, getAdvCharges(stack) - 1);
//            setAttackCounts(stack, getAttackCounts(stack) + 1);
                level.playSound(null, player.blockPosition(), SoundEvents.ENDER_DRAGON_FLAP, SoundSource.PLAYERS, 1f, 1f);

                entity.invulnerableTime = 0;

            }

            if (lightningFlag) {
                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, getArea(entity.blockPosition(), (int) 1), VALID_ENTITY);
                hurtAllEntities(entities, player.damageSources().indirectMagic(entity, player), 5);
                level.playSound(null, entity.blockPosition(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 1f, 1f);
                ((ServerLevel) level).sendParticles(
                        ParticleTypes.SONIC_BOOM,
                        entity.getX(),
                        entity.getY() + 1.0d,
                        entity.getZ(),
                        16,
                        0.25,
                        0.25,
                        0.25,
                        10
                );

                entity.invulnerableTime = 0;
            }
        }


        boolean result = super.onLeftClickEntity(stack, player, entity);
        return result;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        // gun mode
        if (!level.isClientSide()) {
            ItemStack stack = player.getItemInHand(hand);
            if (getAdvCharges(stack) > 0) {
                // under release shooting
                LightningProjectileEntity projectile = new LightningProjectileEntity(
                        level,
                        player,
                        stack,
                        true
                );

                projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 25.0F, 0.0F);
                level.addFreshEntity(projectile);

                setAdvCharges(stack, getAdvCharges(stack) - 1);
                level.playSound(null, player.blockPosition(), SoundEvents.ENDER_DRAGON_SHOOT, SoundSource.PLAYERS, 1f, 1f);
                if (getAdvCharges(stack) <= 0) {
                    // no adv charges
                    level.playSound(null, player.blockPosition(), SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 1f, 1f);

                }

            } else {
                if (!player.isShiftKeyDown()) {
                    if (getCharges(stack) >= 1) {
                        // shooting
                        LightningProjectileEntity projectile = new LightningProjectileEntity(
                                level,
                                player,
                                stack,
                                false
                        );

                        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 5.0F, 0.0F);
                        level.addFreshEntity(projectile);
                        level.playSound(null, player.blockPosition(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 1f, 1f);

                        setCharges(stack, getCharges(stack) - 1);
                    } else {
                        // no bullet
                        level.playSound(null, player.blockPosition(), SoundEvents.VILLAGER_NO, SoundSource.PLAYERS, 1f, 1f);

                    }

                } else {
                    // release
                    if (getCharges(stack) >= 8) {
                        setCharges(stack, getCharges(stack) - 8);
                        setAdvCharges(stack, getAdvCharges(stack) + 10);
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 3));

                    } else {
                        // no bullet
                        level.playSound(null, player.blockPosition(), SoundEvents.VILLAGER_NO, SoundSource.PLAYERS, 1f, 1f);

                    }

                }
            }
            player.getCooldowns().addCooldown(this, 1);
        }
        return super.use(level, player, hand);
    }

    protected final Predicate<LivingEntity> VALID_ENTITY = LivingEntity::isAlive;

    private AABB getArea(BlockPos blockPos, int radius) {
        return new AABB(
                blockPos.offset(-radius, -2, -radius),
                blockPos.offset(1 + radius, 1 + radius, 1 + radius));
    }

    private void hurtAllEntities(List<LivingEntity> entities, DamageSource damageSource, float damage) {
        for (LivingEntity i : entities) {
            if (!i.equals(damageSource.getEntity())) {
                i.hurt(damageSource, damage);
                i.invulnerableTime = 0;

            }

        }

    }

    public static void setCharges(ItemStack stack, int val) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        compoundtag.putInt(CHARGES_TAG, Math.min(val, MAX_CHARGE));
    }

    public static int getCharges(ItemStack stack) {
        CompoundTag compoundtag = stack.getTag();
        if (compoundtag != null) {
            return compoundtag.getInt(CHARGES_TAG);

        }
        return 0;
    }

    public static void setAdvCharges(ItemStack stack, int val) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        compoundtag.putInt(ADVANCED_CHARGES_TAG, Math.min(val, ADVANCED_MAX_CHARGE));

    }

    public static int getAdvCharges(ItemStack stack) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        if (compoundtag.contains(ADVANCED_CHARGES_TAG)) {
            return compoundtag.getInt(ADVANCED_CHARGES_TAG);

        }
        return 0;
    }

    public static void setAttackCounts(ItemStack stack, int val) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        compoundtag.putInt(ATTACK_COUNTS, val);

    }

    public static int getAttackCounts(ItemStack stack) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        if (compoundtag.contains(ATTACK_COUNTS)) {
            return compoundtag.getInt(ATTACK_COUNTS);

        }
        return 0;
    }

    public static float getExtraDamage(ItemStack stack) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        if (compoundtag.contains(EXTRA_DAMAGE_TAG)) {
            return compoundtag.getFloat(EXTRA_DAMAGE_TAG);

        } else {
            float dam = DustAndAshConfig.whiteLightningExtraDamage.get().floatValue();
            compoundtag.putFloat(EXTRA_DAMAGE_TAG, dam);
            return dam;
        }

    }

    public static float getExtraPercentage(ItemStack stack) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        if (compoundtag.contains(EXTRA_PERCENTAGE_TAG)) {
            return compoundtag.getFloat(EXTRA_PERCENTAGE_TAG);

        } else {
            float per = DustAndAshConfig.whiteLightningExtraPercentage.get().floatValue();
            compoundtag.putFloat(EXTRA_PERCENTAGE_TAG, per);
            return per;
        }

    }

    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        pTooltip.add(Component.translatable("tooltip.dustandash.white_lightning"));

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }
}
