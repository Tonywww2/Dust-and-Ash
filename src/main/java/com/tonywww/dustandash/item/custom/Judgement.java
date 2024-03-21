package com.tonywww.dustandash.item.custom;

import com.tonywww.dustandash.render.JudgementRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class Judgement extends PickaxeItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final int MAX_CD = 200;
    private final int MAX_DURATION = 100;

    public Judgement(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            player.startUsingItem(hand);

        }
        return super.use(level, player, hand);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int tick) {
        if (!level.isClientSide()) {
            entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 10, 9));
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10, 9));

        }
        super.onUseTick(level, entity, stack, tick);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int tick) {
        if (!level.isClientSide()) {
            if (entity instanceof Player player) {
                int usedTick = MAX_DURATION - tick;
                int cd = MAX_CD;

                if (usedTick < MAX_DURATION * 0.3) {
                    cd -= MAX_CD * 0.2;
                }
                if (usedTick < MAX_DURATION * 0.5) {
                    cd -= MAX_CD * 0.15;
                }
                if (usedTick < MAX_DURATION * 0.7) {
                    cd -= MAX_CD * 0.1;
                }

                player.getCooldowns().addCooldown(this, cd);

            }
        }
        super.releaseUsing(stack, level, entity, tick);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide()) {
            if (entity instanceof Player player) {
                player.getCooldowns().addCooldown(this, MAX_CD);

            }
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return MAX_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        pTooltip.add(Component.translatable("tooltip.dustandash.judgement"));

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private JudgementRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new JudgementRenderer();

                return this.renderer;
            }
        });
    }

    private PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().then("animation.judgement.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;

    }

    AnimationController<Judgement> idle = new AnimationController<>(this, "idle", 0, this::predicate);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(idle);

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public double getTick(Object itemStack) {
        return RenderUtils.getCurrentTick();
    }

}
