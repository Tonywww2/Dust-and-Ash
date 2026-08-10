package com.tonywww.dustandash.item;

import com.tonywww.dustandash.DustAndAshConfig;
import com.tonywww.dustandash.registry.DAAItems;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Indestructible extends Item {
    public Indestructible(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide) {
            player.getCooldowns().addCooldown(this, DustAndAshConfig.TOOLS.indestructibleCooldown.get());
            player.getCooldowns().addCooldown(DAAItems.ROCK_SOLID.get(), DustAndAshConfig.TOOLS.rockSolidCooldown.get());
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 120, 5));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 160, 3));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0));

            level.playSound(null, player.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1f, 1f);

        }

        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> pTooltip, TooltipFlag pFlag) {

        pTooltip.add(Component.translatable("tooltip.dustandash.indestructible"));

        super.appendHoverText(stack, level, pTooltip, pFlag);
    }
}
