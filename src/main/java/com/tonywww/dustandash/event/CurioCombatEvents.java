package com.tonywww.dustandash.event;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.DustAndAshConfig;
import com.tonywww.dustandash.cooldown.CurioCooldownManager;
import com.tonywww.dustandash.entity.LightStaffEntity;
import com.tonywww.dustandash.registry.DAAItems;
import com.tonywww.dustandash.registry.DAAParticles;
import com.tonywww.dustandash.tag.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = DustAndAsh.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CurioCombatEvents {
    private static final String JUDGEMENT_INVULNERABLE_UNTIL = DustAndAsh.MOD_ID + ":judgement_invulnerable_until";
    private static final String DARK_HALO_MARKS = DustAndAsh.MOD_ID + ":dark_halo_marks";

    private CurioCombatEvents() {
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        long gameTime = player.level().getGameTime();
        CompoundTag data = player.getPersistentData();
        if (!isEquipped(player, DAAItems.JUDGEMENT.get())) {
            return;
        }
        if (gameTime < data.getLong(JUDGEMENT_INVULNERABLE_UNTIL)) {
            event.setCanceled(true);
            return;
        }

        if (CurioCooldownManager.isOnCooldown(player, CurioCooldownManager.JUDGEMENT)) {
            return;
        }

        event.setCanceled(true);
        int cooldown = DustAndAshConfig.CURIOS.judgementCooldownTicks.get();
        data.putLong(JUDGEMENT_INVULNERABLE_UNTIL,
                gameTime + DustAndAshConfig.CURIOS.judgementInvulnerabilityTicks.get());
        CurioCooldownManager.start(player, CurioCooldownManager.JUDGEMENT, cooldown);
        player.getCooldowns().addCooldown(DAAItems.JUDGEMENT.get(), cooldown);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity().level().isClientSide() || event.getAmount() <= 0f) {
            return;
        }

        LivingEntity target = event.getEntity();
        Entity sourceEntity = event.getSource().getEntity();
        if (sourceEntity instanceof ServerPlayer attacker) {
            healFromMarkedTarget(attacker, target, event.getAmount());
            trySummonLightStaff(attacker, target, event);
        }

        if (target instanceof ServerPlayer wearer) {
            triggerDarkHalo(wearer, sourceEntity, event);
        }
    }

    private static void trySummonLightStaff(
            ServerPlayer attacker, LivingEntity target, LivingDamageEvent event) {
        if (attacker == target
                || event.getSource().is(ModTags.DamageTypes.LIGHT_HALO_EXCLUDED)
                || !isEquipped(attacker, DAAItems.LIGHT_FORGED_HALO.get())) {
            return;
        }

        if (CurioCooldownManager.isOnCooldown(attacker, CurioCooldownManager.LIGHT_FORGED_HALO)) {
            return;
        }

        int threshold = DustAndAshConfig.CURIOS.lightHaloBrightnessThreshold.get();
        if (!VoidRingEvents.bypassesHaloBrightness(attacker)
            && brightness(attacker) < threshold
            && brightness(target) < threshold) {
            return;
        }

        int cooldown = DustAndAshConfig.CURIOS.lightHaloCooldownTicks.get();
        CurioCooldownManager.start(attacker, CurioCooldownManager.LIGHT_FORGED_HALO, cooldown);
        attacker.getCooldowns().addCooldown(DAAItems.LIGHT_FORGED_HALO.get(), cooldown);
        attacker.level().addFreshEntity(new LightStaffEntity(
                (net.minecraft.server.level.ServerLevel) attacker.level(), attacker, target));
    }

    private static void triggerDarkHalo(
            ServerPlayer wearer, Entity sourceEntity, LivingDamageEvent event) {
        if (event.getSource().is(ModTags.DamageTypes.DARK_HALO_EXCLUDED)
                || !isEquipped(wearer, DAAItems.DARK_FORGED_HALO.get())) {
            return;
        }

        LivingEntity livingSource = sourceEntity instanceof LivingEntity living ? living : null;
        int threshold = DustAndAshConfig.CURIOS.darkHaloBrightnessThreshold.get();
        if (!VoidRingEvents.bypassesHaloBrightness(wearer)
            && brightness(wearer) > threshold
                && (livingSource == null || brightness(livingSource) > threshold)) {
            return;
        }

        if (livingSource == null) {
            float healing = event.getAmount()
                    * DustAndAshConfig.CURIOS.darkHaloSourcelessHealingMultiplier.get().floatValue();
            event.setAmount(Math.max(0f, event.getAmount() - healing));
            return;
        }

        sendDarkSmokeLine(wearer, livingSource);
        applyRandomEffects(wearer, livingSource);
        CompoundTag marks = livingSource.getPersistentData().getCompound(DARK_HALO_MARKS);
        marks.putLong(
                wearer.getStringUUID(),
                wearer.level().getGameTime() + DustAndAshConfig.CURIOS.darkHaloMarkDurationTicks.get());
        livingSource.getPersistentData().put(DARK_HALO_MARKS, marks);
    }

    private static void healFromMarkedTarget(Player attacker, LivingEntity target, float damage) {
        if (!isEquipped(attacker, DAAItems.DARK_FORGED_HALO.get())) {
            return;
        }

        CompoundTag marks = target.getPersistentData().getCompound(DARK_HALO_MARKS);
        String attackerId = attacker.getStringUUID();
        long expiresAt = marks.getLong(attackerId);
        if (expiresAt <= attacker.level().getGameTime()) {
            if (marks.contains(attackerId)) {
                marks.remove(attackerId);
                target.getPersistentData().put(DARK_HALO_MARKS, marks);
            }
            return;
        }

        attacker.heal(damage * DustAndAshConfig.CURIOS.darkHaloLifeStealMultiplier.get().floatValue());
    }

    private static void applyRandomEffects(Player wearer, LivingEntity target) {
        Registry<MobEffect> registry = wearer.level().registryAccess().registryOrThrow(Registries.MOB_EFFECT);
        List<Holder<MobEffect>> effects = registry.getTag(ModTags.MobEffects.DARK_HALO_EFFECTS)
                .map(tag -> new ArrayList<>(tag.stream().toList()))
                .orElseGet(ArrayList::new);
        int effectCount = Math.min(DustAndAshConfig.CURIOS.darkHaloEffectCount.get(), effects.size());
        int configuredMinimum = DustAndAshConfig.CURIOS.darkHaloMinimumEffectLevel.get();
        int configuredMaximum = DustAndAshConfig.CURIOS.darkHaloMaximumEffectLevel.get();
        int minimumLevel = Math.min(configuredMinimum, configuredMaximum);
        int maximumLevel = Math.max(configuredMinimum, configuredMaximum);

        for (int index = 0; index < effectCount; index++) {
            Holder<MobEffect> effect = effects.remove(wearer.getRandom().nextInt(effects.size()));
            int level = minimumLevel + wearer.getRandom().nextInt(maximumLevel - minimumLevel + 1);
            target.addEffect(new MobEffectInstance(
                    effect.value(),
                    DustAndAshConfig.CURIOS.darkHaloEffectDurationTicks.get(),
                    level - 1), wearer);
        }
    }

    private static void sendDarkSmokeLine(Player wearer, LivingEntity source) {
        net.minecraft.server.level.ServerLevel level = (net.minecraft.server.level.ServerLevel) wearer.level();
        net.minecraft.world.phys.Vec3 look = wearer.getLookAngle();
        net.minecraft.world.phys.Vec3 start = wearer.position()
                .add(-look.x * 0.65d, wearer.getBbHeight() * 0.65d, -look.z * 0.65d);
        net.minecraft.world.phys.Vec3 delta = source.getEyePosition().subtract(start);
        int steps = Math.min(
            DustAndAshConfig.CURIOS.darkHaloMaximumSmokeParticles.get(),
            Math.max(1, (int) Math.ceil(delta.length()
                * DustAndAshConfig.CURIOS.darkHaloSmokeParticlesPerBlock.get())));
        net.minecraft.world.phys.Vec3 step = delta.scale(1d / steps);
        for (int index = 0; index <= steps; index++) {
            net.minecraft.world.phys.Vec3 point = start.add(step.scale(index));
            level.sendParticles(DAAParticles.DARK_SMOKE.get(), point.x, point.y, point.z, 1, 0d, 0d, 0d, 0d);
        }
    }

    private static int brightness(LivingEntity entity) {
        int blockLight = entity.level().getBrightness(LightLayer.BLOCK, entity.blockPosition());
        int skyLight = Math.max(
                0,
                entity.level().getBrightness(LightLayer.SKY, entity.blockPosition())
                        - entity.level().getSkyDarken());
        return Math.max(blockLight, skyLight);
    }

    private static boolean isEquipped(LivingEntity entity, net.minecraft.world.item.Item item) {
        return CuriosApi.getCuriosInventory(entity)
                .map(handler -> handler.isEquipped(item))
                .orElse(false);
    }
}