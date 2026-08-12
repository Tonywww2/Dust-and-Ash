package com.tonywww.dustandash.item;

import com.tonywww.dustandash.DustAndAshConfig;
import com.tonywww.dustandash.config.ImbaRules;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;

public class RottenBlade extends SwordItem {

    private static final String[] ENTITIES = {
            "e0", "e1", "e2", "e3", "e4", "e5", "e6", "e7", "e8"
    };

    public int entitiesSize = 6;

    private static final DustParticleOptions PARTICLE_GREEN = new DustParticleOptions(new Vector3f(0, 1f, 0f), 2.0F);


    public RottenBlade(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        Level world = player.level();

        if (!world.isClientSide) {
                if (!DustAndAshConfig.WEAPONS.rottenBladeCooldownCheck.get()
                    || player.getAttackStrengthScale(0.2f) >= 1) {
                ServerLevel serverWorld = (ServerLevel) world;
                CompoundTag tag = stack.getOrCreateTag();

                damageEntityByUUID(player, serverWorld, tag,
                    DustAndAshConfig.WEAPONS.rottenBladeExtraDamage.get().floatValue(),
                    ImbaRules.rottenBladeRememberedTargets());

                tag.putUUID(ENTITIES[0], entity.getUUID());

            }

        }

        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            ServerLevel serverWorld = (ServerLevel) level;

            if (player.isShiftKeyDown()) {
                int radius = DustAndAshConfig.WEAPONS.rottenBladeRadius.get();
                int height = DustAndAshConfig.WEAPONS.rottenBladeHeight.get();
                AABB region = new AABB(player.blockPosition().north(radius).east(radius).above(height),
                    player.blockPosition().south(radius).west(radius).below(height));

                for (LivingEntity i : serverWorld.getEntitiesOfClass(LivingEntity.class, region)) {
                    if (i == player) {
                        if (ImbaRules.rottenBladeAffectsWielder()) {
                            i.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0));
                            i.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 1));
                        }
                        continue;
                    }
                    i.hurt(player.damageSources().indirectMagic(i, player), 8);
                    i.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0));
                    i.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, 3));

                    serverWorld.sendParticles(
                            PARTICLE_GREEN,
                            i.getX(),
                            i.getY() + 0.5d,
                            i.getZ(),
                            3,
                            0.75d,
                            0.75d,
                            0.75d,
                            0
                    );

                }

                int cooldown = ImbaRules.rottenBladeAreaCooldownTicks();
                if (cooldown > 0) {
                    player.getCooldowns().addCooldown(this, cooldown);
                }

            } else {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 60, 2));
                damageEntityByUUID(player, serverWorld, stack.getOrCreateTag(),
                    DustAndAshConfig.WEAPONS.rottenBladeExtraDamage.get().floatValue() / 2,
                    ImbaRules.rottenBladeRememberedTargets());
                float recoilDamage = ImbaRules.rottenBladeRecoilDamage();
                if (recoilDamage > 0f) {
                    player.hurt(player.damageSources().indirectMagic(player, player), recoilDamage);
                }
                int cooldown = ImbaRules.rottenBladeReleaseCooldownTicks();
                if (cooldown > 0) {
                    player.getCooldowns().addCooldown(this, cooldown);
                }

            }


        }

        return super.use(level, player, hand);
    }

    private void damageEntityByUUID(
            Player player,
            ServerLevel serverWorld,
            CompoundTag tag,
            float damage,
            int rememberedTargets) {
        for (int index = 0; index < rememberedTargets; index++) {
            String entityTag = ENTITIES[index];
            if (tag.contains(entityTag)) {
                Entity entity = serverWorld.getEntity(tag.getUUID(entityTag));

                if (entity != null) {
                    entity.invulnerableTime = 0;
                    entity.hurt(player.damageSources().outOfBorder(), damage);

                    serverWorld.sendParticles(
                            PARTICLE_GREEN,
                            entity.getX(),
                            entity.getY() + 0.5d,
                            entity.getZ(),
                            6,
                            0.75d,
                            0.75d,
                            0.75d,
                            0
                    );

                }
            }

        }

        for (int i = rememberedTargets - 1; i > 0; i--) {
            if (tag.contains(ENTITIES[i - 1])) {
                tag.putUUID(ENTITIES[i], tag.getUUID(ENTITIES[i - 1]));

            }
        }
    }
}
