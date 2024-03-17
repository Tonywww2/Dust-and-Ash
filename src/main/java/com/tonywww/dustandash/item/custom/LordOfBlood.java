package com.tonywww.dustandash.item.custom;

import com.tonywww.dustandash.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Predicate;

public class LordOfBlood extends SwordItem {

    static final float RADIUS = 8f;
    static final Vector3f COLOR = new Vector3f(1f, 0.1921568f, 0.2039215f);
    static final DustParticleOptions PARTICLE = new DustParticleOptions(COLOR, 2.0F);

    public LordOfBlood(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        Level world = player.level();
        boolean result = super.onLeftClickEntity(stack, player, entity);

        if (!world.isClientSide()) {
            if (player.getAttackStrengthScale(0.1f) >= 1) {
                player.heal(2.0f);

            }

            if (getCharges(stack) >= 3) {
                world.playSound(null, entity.blockPosition(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 1f, 1f);
                player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 20, 0));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 1));
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 1));
                setCharges(stack, 0);

            }

        }

        return result;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (getCharges(player.getItemInHand(hand)) < 3) {
            player.startUsingItem(hand);

        } else {
            level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 1f, 1f);

        }
        return super.use(level, player, hand);
    }

    public static void setCharges(ItemStack stack, int val) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        compoundtag.putInt("Charges", val);
    }

    public static int getCharges(ItemStack stack) {
        CompoundTag compoundtag = stack.getTag();
        if (compoundtag != null) {
            return compoundtag.getInt("Charges");

        }
        return 0;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int time) {
        super.onUseTick(level, entity, stack, time);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {

        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, getArea(entity.blockPosition()), VALID_ENTITY);
        DamageSource damageSource = level.damageSources().thorns(entity);

        setCharges(stack, getCharges(stack) + 1);
        int curStacks = getCharges(stack);
        switch (curStacks) {
            case 1 -> {
                level.playSound(null, entity.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1f, 1f);
                particle1((ServerLevel) level, entity, PARTICLE, RADIUS, 2, 64);
                hurtAllEntities(entities, damageSource, 2);
                effectAllEntities(entities, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0));
                entity.heal(2);
            }
            case 2 -> {
                level.playSound(null, entity.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1f, 1f);
                particle1((ServerLevel) level, entity, PARTICLE, RADIUS, 2, 64);
                particle1((ServerLevel) level, entity, PARTICLE, -RADIUS, 2, 64);
                hurtAllEntities(entities, damageSource, 4);
                effectAllEntities(entities, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
                effectAllEntities(entities, new MobEffectInstance(MobEffects.WEAKNESS, 60, 0));
                entity.heal(3);
            }
            case 3 -> {
                level.playSound(null, entity.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1f, 1f);
                particle1((ServerLevel) level, entity, PARTICLE, RADIUS, 2, 64);
                particle1((ServerLevel) level, entity, PARTICLE, -RADIUS, 2, 64);
                particle3((ServerLevel) level, entity, PARTICLE, RADIUS - 3f, 3, 32);
                hurtAllEntities(entities, damageSource, 6);
                effectAllEntities(entities, new MobEffectInstance(MobEffects.WEAKNESS, 60, 1));
                entity.heal(4);
            }
            default -> setCharges(stack, 0);
        }
        return super.finishUsingItem(stack, level, entity);
    }

    protected final Predicate<LivingEntity> VALID_ENTITY = LivingEntity::isAlive;

    private AABB getArea(BlockPos blockPos) {
        return new AABB(
                blockPos.offset(-(int) RADIUS, -2, -(int) RADIUS),
                blockPos.offset(1 + (int) RADIUS, 1 + (int) RADIUS, 1 + (int) RADIUS));
    }

    private void hurtAllEntities(List<LivingEntity> entities, DamageSource damageSource, float damage) {
        for (LivingEntity i : entities) {
            i.hurt(damageSource, damage);
            i.invulnerableTime = 0;

        }

    }

    private void effectAllEntities(List<LivingEntity> entities, MobEffectInstance effect) {
        for (LivingEntity i : entities) {
            i.addEffect(effect);

        }

    }

    private void particle1(ServerLevel level, LivingEntity entity, ParticleOptions particle, float radius, int thickness, int accuracy) {
        float t = 0.2f;
        float tFinal = (float) Math.PI - 0.2f;
        float step = (tFinal - t) / accuracy;

        for (int i = 1; i < accuracy; i++) {
            float x = (float) Math.cos(t + step * i) * radius;
            float z = (float) Math.sin(t + step * i) * radius;

            level.sendParticles(
                    particle,
                    entity.getX() + x,
                    entity.getY() + 0.125d,
                    entity.getZ() + z,
                    thickness,
                    0.015625,
                    0.015625,
                    0.015625,
                    0
            );

        }

    }

    private void particle3(ServerLevel level, LivingEntity entity, ParticleOptions particle, float radius, int thickness, int accuracy) {
        float step = 2 * radius / accuracy;

        for (int i = 1; i < accuracy; i++) {
            float x = i * step - radius;
            float z = 0;

            level.sendParticles(
                    particle,
                    entity.getX() + x,
                    entity.getY() + 0.125d,
                    entity.getZ() + z,
                    thickness,
                    0.1,
                    0.015625,
                    0.05,
                    0
            );

        }

    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int time) {

        super.releaseUsing(stack, level, entity, time);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 60;
    }
}
