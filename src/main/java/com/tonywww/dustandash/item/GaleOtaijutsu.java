package com.tonywww.dustandash.item;

import com.tonywww.dustandash.DustAndAshConfig;
import com.tonywww.dustandash.gecko.render.GaleOtaijutsuRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
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
import java.util.Objects;
import java.util.function.Consumer;

public class GaleOtaijutsu extends SwordItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public GaleOtaijutsu(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        Level world = player.level();

        if (!world.isClientSide()) {
            if (entity instanceof LivingEntity) {
                int effectLevel = Mth.clamp((int) (player.fallDistance * 1.75), 0, 100);

                player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 90, effectLevel));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 1));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0));

                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));

            }

                float extraDamage = (float) ((player.fallDistance + 1)
                    * DustAndAshConfig.WEAPONS.galeOtaijutsuDamageRate.get());
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


        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GaleOtaijutsuRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new GaleOtaijutsuRenderer();

                return this.renderer;
            }
        });
    }

    private PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().then("animation.gale_otaijutsu.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;

    }

    AnimationController<GaleOtaijutsu> idle = new AnimationController<>(this, "idle", 0, this::predicate);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(idle);

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
