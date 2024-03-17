package com.tonywww.dustandash.item.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;


public class GaleOtaijutsu extends SwordItem {
    public GaleOtaijutsu(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        Level world = player.level();

        if (!world.isClientSide()) {
            if (entity instanceof LivingEntity) {
//                int effectLevel = (int) (player.fallDistance * 1.5);
                int effectLevel = Mth.clamp((int) (player.fallDistance * 1.5), 0, 100);
//                if (effectLevel > 100) {
//                    effectLevel = 100;
//                }
//                if (effectLevel < 0) {
//                    effectLevel = 0;
//                }

                player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 60, effectLevel));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 1));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0));

                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));

            }

            float extraDamage = (player.fallDistance + 1) * 1.25f;
            entity.hurt(player.damageSources().playerAttack(player), extraDamage);
            entity.invulnerableTime = 0;
            player.resetFallDistance();

        }

        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level world = context.getLevel();

        Player playerEntity = Objects.requireNonNull(context.getPlayer());

        double angle = playerEntity.getViewYRot(0) % 360;
        double xVel = Math.sin(Math.toRadians(angle));
        double zVel = Math.cos(Math.toRadians(angle));
        Vec3 vec = new Vec3(-xVel * 0.5, 1.0d, zVel * 0.5);

        if (!world.isClientSide) {
            if (!playerEntity.getCooldowns().isOnCooldown(this)) {
                ((ServerLevel) world).sendParticles(
                        ParticleTypes.CLOUD,
                        playerEntity.getX(),
                        playerEntity.getY() + 0.5d,
                        playerEntity.getZ(),
                        50,
                        0,
                        -0.5,
                        0,
                        1
                );

                world.playSound(null, playerEntity.blockPosition(), SoundEvents.PISTON_EXTEND, SoundSource.PLAYERS, 1f, 1f);
                playerEntity.getCooldowns().addCooldown(this, 40);
                if (playerEntity instanceof ServerPlayer) {
                    playerEntity.setDeltaMovement(vec);
                    ((ServerPlayer) playerEntity).connection.send(new ClientboundSetEntityMotionPacket(playerEntity));

                }
            }

        } else {
            if (!playerEntity.getCooldowns().isOnCooldown(this)) {
                playerEntity.setDeltaMovement(vec);

            }

        }
        return super.onItemUseFirst(stack, context);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        pTooltip.add(Component.translatable("tooltip.dustandash.gale_otaijutsu"));

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

}
