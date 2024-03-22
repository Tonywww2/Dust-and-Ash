package com.tonywww.dustandash.item.custom;

import com.tonywww.dustandash.config.DustAndAshConfig;
import com.tonywww.dustandash.render.LordOfBloodRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.joml.Vector3f;
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
import java.util.function.Predicate;


public class LordOfBlood extends SwordItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final String RADIUS_TAG = "radius";
    private static final String CHARGES_TAG = "charges";

    static final Vector3f COLOR_RED = new Vector3f(1f, 0.1921568f, 0.2039215f);
    static final Vector3f COLOR_GREY = new Vector3f(0.7529411f, 0.7529411f, 0.7529411f);
    static final DustParticleOptions PARTICLE_RED = new DustParticleOptions(COLOR_RED, 2.0F);
    static final DustParticleOptions PARTICLE_GREY = new DustParticleOptions(COLOR_GREY, 2.0F);

    public LordOfBlood(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);

    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        Level level = player.level();
        boolean result = super.onLeftClickEntity(stack, player, entity);

        if (!level.isClientSide()) {
            if (player.getAttackStrengthScale(0.1f) >= 1) {
                player.heal(2.0f);

            }

            if (getCharges(stack) >= 3) {
                level.playSound(null, entity.blockPosition(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 1f, 1f);
                player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 20, 0));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 1));
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 1));
                setCharges(stack, 0);

                ((ServerLevel) level).sendParticles(
                        PARTICLE_RED,
                        entity.getX(),
                        entity.getY() + 0.5d,
                        entity.getZ(),
                        64,
                        0.5d,
                        0.5d,
                        0.5d,
                        0
                );

            }

        }

        return result;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            if (getCharges(player.getItemInHand(hand)) < 3) {
                use.stop();
                triggerAnim(player, GeoItem.getOrAssignId(player.getItemInHand(hand), (ServerLevel) level), "use", "use");
                player.startUsingItem(hand);

            } else {
                level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 1f, 1f);

            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int tick) {

        if (!level.isClientSide()) {
            if (tick % 4 == 0) {
                int charges = getCharges(stack) + 1;
                ((ServerLevel) level).sendParticles(
                        PARTICLE_RED,
                        entity.getX(),
                        entity.getY() + 3.5d,
                        entity.getZ(),
                        charges * 4,
                        0.5d * (charges / 2d),
                        0.5d * (charges / 2d),
                        0.5d * (charges / 2d),
                        0
                );

            }

        }

        super.onUseTick(level, entity, stack, tick);
    }

    public static void setCharges(ItemStack stack, int val) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        compoundtag.putInt(CHARGES_TAG, val);
    }

    public static int getCharges(ItemStack stack) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        if (compoundtag.contains(CHARGES_TAG)) {
            return compoundtag.getInt(CHARGES_TAG);

        }
        return 0;
    }

    public static float getRadius(ItemStack stack) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        if (compoundtag.contains(RADIUS_TAG)) {
            return compoundtag.getFloat(RADIUS_TAG);

        } else {
            float radius = DustAndAshConfig.lordOfBloodRadius.get().floatValue();
            compoundtag.putFloat(RADIUS_TAG, radius);
            return radius;
        }

    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
//        return UseAnim.DRINK;
        return UseAnim.BOW;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {

        if (!level.isClientSide()) {

            float radius = getRadius(stack);
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, getArea(entity.blockPosition(), (int) radius), VALID_ENTITY);
            DamageSource damageSource = level.damageSources().thorns(entity);

            setCharges(stack, getCharges(stack) + 1);
            int curStacks = getCharges(stack);
            switch (curStacks) {
                case 1 -> {

                    if (!level.isClientSide()) {
                        level.playSound(null, entity.blockPosition(), SoundEvents.WARDEN_ROAR, SoundSource.PLAYERS, 1f, 1f);

                        particle1((ServerLevel) level, entity, PARTICLE_RED, radius, 2, 64);

                        particle1((ServerLevel) level, entity, PARTICLE_GREY, -radius, 1, 64);
                        particle3((ServerLevel) level, entity, PARTICLE_GREY, radius - 3f, 1, 32);

                    }


                    hurtAllEntities(entities, damageSource, 2);
                    effectAllEntities(entities, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0), entity);
                    entity.heal(3);
                }
                case 2 -> {
                    if (!level.isClientSide()) {
                        level.playSound(null, entity.blockPosition(), SoundEvents.WARDEN_ANGRY, SoundSource.PLAYERS, 1f, 1f);

                        particle1((ServerLevel) level, entity, PARTICLE_RED, radius, 2, 64);
                        particle1((ServerLevel) level, entity, PARTICLE_RED, -radius, 2, 64);

                        particle3((ServerLevel) level, entity, PARTICLE_GREY, radius - 3f, 1, 32);

                    }

                    hurtAllEntities(entities, damageSource, 4);
                    effectAllEntities(entities, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1), entity);
                    effectAllEntities(entities, new MobEffectInstance(MobEffects.WEAKNESS, 60, 0), entity);
                    entity.heal(4);
                }
                case 3 -> {
                    if (!level.isClientSide()) {
                        level.playSound(null, entity.blockPosition(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 1f, 1f);

                        particle1((ServerLevel) level, entity, PARTICLE_RED, radius, 2, 64);
                        particle1((ServerLevel) level, entity, PARTICLE_RED, -radius, 2, 64);
                        particle3((ServerLevel) level, entity, PARTICLE_RED, radius - 3f, 3, 32);

                    }

                    hurtAllEntities(entities, damageSource, 6);
                    effectAllEntities(entities, new MobEffectInstance(MobEffects.WEAKNESS, 60, 1), entity);
                    entity.heal(5);

                }
                default -> setCharges(stack, 0);
            }
            if (entity instanceof Player player) {
                player.getCooldowns().addCooldown(this, 15);

            }
        }
        return super.finishUsingItem(stack, level, entity);
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

    private void effectAllEntities(List<LivingEntity> entities, MobEffectInstance effect, Entity source) {
        for (LivingEntity i : entities) {
            if (!i.equals(source)) {
                i.addEffect(effect);

            }

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
                    0.015625,
                    0.015625,
                    0.015625,
                    0
            );

        }

    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int time) {
        if (!level.isClientSide()) {
            use.stop();

        }
        super.releaseUsing(stack, level, entity, time);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 60;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        pTooltip.add(Component.translatable("tooltip.dustandash.lord_of_blood"));

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private LordOfBloodRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new LordOfBloodRenderer();

                return this.renderer;
            }
        });
    }

    private PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().then("animation.lord_of_blood.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;

    }

    AnimationController<LordOfBlood> idle = new AnimationController<>(this, "idle", 0, this::predicate);
    AnimationController<LordOfBlood> use = new AnimationController<>(this, "use", 0, state -> PlayState.STOP)
            .triggerableAnim("use", RawAnimation.begin().thenPlay("animation.lord_of_blood.use"));

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
