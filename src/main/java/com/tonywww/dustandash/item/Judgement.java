package com.tonywww.dustandash.item;

import com.tonywww.dustandash.gecko.render.JudgementRenderer;
import com.tonywww.dustandash.tag.ModTags;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class Judgement extends PickaxeItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final int MAX_CD = 300;
    private final int MAX_DURATION = 40;

    public Judgement(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            triggerAnim(player, GeoItem.getOrAssignId(player.getItemInHand(hand), (ServerLevel) level), "use", "use");
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
                    cd -= (int) (MAX_CD * 0.2);
                }
                if (usedTick < MAX_DURATION * 0.5) {
                    cd -= (int) (MAX_CD * 0.15);
                }
                if (usedTick < MAX_DURATION * 0.7) {
                    cd -= (int) (MAX_CD * 0.1);
                }

                player.removeEffect(MobEffects.ABSORPTION);
                player.getCooldowns().addCooldown(this, cd);

            }
            use.stop();
            triggerAnim(entity, GeoItem.getOrAssignId(stack, (ServerLevel) level), "idle", "idle");

        }
        super.releaseUsing(stack, level, entity, tick);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide()) {
            if (entity instanceof Player player) {
                player.getCooldowns().addCooldown(this, MAX_CD);
                AtomicReference<Double> totalHP = new AtomicReference<>(0d);
                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(7), (livingEntity) -> {
                    if (livingEntity.getType().is(ModTags.EntityTypes.JUDGEMENT_BLACKLIST))
                        return false;
                    if ((livingEntity.isAlliedTo(player) || player.isAlliedTo(livingEntity)) && livingEntity != player)
                        return false;
                    totalHP.updateAndGet(v -> (v + livingEntity.getHealth()));
                    return true;
                });

                if (!entities.isEmpty()) {
                    double targetHp = totalHP.get() / entities.size();

                    entities.forEach((livingEntity -> {
                        float health = livingEntity.getHealth();
                        if (health > targetHp)
                            livingEntity.hurt(player.damageSources().playerAttack(player), (float) (health - targetHp));
                        else
                            livingEntity.heal((float) (targetHp - health));
                    }));
                }

            }
            use.stop();
            triggerAnim(entity, GeoItem.getOrAssignId(stack, (ServerLevel) level), "idle", "idle");

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
    AnimationController<Judgement> use = new AnimationController<>(this, "use", 0, state -> PlayState.STOP)
            .triggerableAnim("use", RawAnimation.begin().thenPlay("animation.judgement.use"));

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(idle);
        controllers.add(use);

    }

    @Override
    public double getTick(Object itemStack) {
        return RenderUtils.getCurrentTick();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

}
