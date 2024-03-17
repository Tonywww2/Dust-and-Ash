package com.tonywww.dustandash.item.custom;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;


public class SunburnMegaSword extends SwordItem {
    public SunburnMegaSword(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        Level world = player.level();

        if (!world.isClientSide()) {
            if (entity instanceof LivingEntity) {
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0));
                entity.setSecondsOnFire(4);

            }
            if (!player.getCooldowns().isOnCooldown(this)) {
                Explosion explosion = new Explosion(player.level(), player, player.getX(), player.getY() + 0.125d, player.getZ(), 2.25f, true, Explosion.BlockInteraction.KEEP);
                player.level().playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1f, 1f);
                ((ServerLevel) player.level()).sendParticles(
                        ParticleTypes.EXPLOSION,
                        player.getX(),
                        player.getY() + 0.25d,
                        player.getZ(),
                        3,
                        0,
                        0,
                        0,
                        1
                );
                explosion.explode();
                entity.invulnerableTime = 0;
                player.getCooldowns().addCooldown(this, 40);

            }
        }

        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {

        return super.onItemUseFirst(stack, context);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        pTooltip.add(Component.translatable("tooltip.dustandash.sunburn_mega_sword"));

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

}
