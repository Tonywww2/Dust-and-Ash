package com.tonywww.dustandash.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
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
        if (entity instanceof LivingEntity) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 1));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0));

            ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));

        }

        float extraDamage = player.fallDistance;
        entity.hurt(player.damageSources().playerAttack(player), extraDamage);
        entity.invulnerableTime = 0;
        player.fallDistance = 0;

        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level world = context.getLevel();

        Player playerEntity = Objects.requireNonNull(context.getPlayer());

        double angle = playerEntity.getViewYRot(0) % 360;
        double xVel = Math.sin(Math.toRadians(angle));
        double zVel = Math.cos(Math.toRadians(angle));
        Vec3 vec = new Vec3(-xVel * 0.75, 1.25d, zVel * 0.75);


        if (!world.isClientSide) {
            if (!playerEntity.getCooldowns().isOnCooldown(this)) {
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
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        pTooltip.add(Component.translatable("tooltip.dustandash.gale_otaijutsu"));

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

}
