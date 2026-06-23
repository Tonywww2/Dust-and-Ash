package com.tonywww.dustandash.cthulhu.api;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.cthulhu.config.CthulhuConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public final class HealthDrainAPI {

    private static final String VITALITY_DRAIN_KEY = DustAndAsh.MOD_ID + ".cthulhu_vitality_drain";
    private static final String SOUL_WITHER_KEY = DustAndAsh.MOD_ID + ".cthulhu_soul_wither";
    private static final UUID VITALITY_DRAIN_MODIFIER = UUID.fromString("1c680067-3f7c-45e4-b201-21219909db12");
    private static final UUID SOUL_WITHER_MODIFIER = UUID.fromString("31f6bcc4-a725-489a-945c-31d147e3f3ad");

    private HealthDrainAPI() {
    }

    public static void applyVitalityDrain(ServerPlayer player, float percent) {
        float value = Mth.clamp(getVitalityDrain(player) + percent, 0.0f, 0.99f);
        player.getPersistentData().putFloat(VITALITY_DRAIN_KEY, value);
        reapply(player);
    }

    public static void applySoulWither(ServerPlayer player, float percent) {
        float value = Mth.clamp(getSoulWither(player) + percent, 0.0f, CthulhuConfig.SOUL_WITHER_MAX);
        player.getPersistentData().putFloat(SOUL_WITHER_KEY, value);
        reapply(player);
    }

    public static void clearAllDrain(ServerPlayer player) {
        player.getPersistentData().remove(VITALITY_DRAIN_KEY);
        player.getPersistentData().remove(SOUL_WITHER_KEY);
        reapply(player);
    }

    public static void clearVitalityDrain(ServerPlayer player) {
        player.getPersistentData().remove(VITALITY_DRAIN_KEY);
        reapply(player);
    }

    public static void recoverSoulWither(ServerPlayer player, float percent) {
        float value = Mth.clamp(getSoulWither(player) - percent, 0.0f, CthulhuConfig.SOUL_WITHER_MAX);
        if (value <= 0.0f) {
            player.getPersistentData().remove(SOUL_WITHER_KEY);
        } else {
            player.getPersistentData().putFloat(SOUL_WITHER_KEY, value);
        }
        reapply(player);
    }

    public static float getVitalityDrain(ServerPlayer player) {
        return player.getPersistentData().getFloat(VITALITY_DRAIN_KEY);
    }

    public static float getSoulWither(ServerPlayer player) {
        return player.getPersistentData().getFloat(SOUL_WITHER_KEY);
    }

    public static void copyPersistentDrain(ServerPlayer oldPlayer, ServerPlayer newPlayer) {
        CompoundTag oldData = oldPlayer.getPersistentData();
        CompoundTag newData = newPlayer.getPersistentData();

        if (oldData.contains(VITALITY_DRAIN_KEY)) {
            newData.putFloat(VITALITY_DRAIN_KEY, oldData.getFloat(VITALITY_DRAIN_KEY));
        }
        if (oldData.contains(SOUL_WITHER_KEY)) {
            newData.putFloat(SOUL_WITHER_KEY, oldData.getFloat(SOUL_WITHER_KEY));
        }
        reapply(newPlayer);
    }

    public static void reapply(ServerPlayer player) {
        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth == null) {
            return;
        }

        maxHealth.removeModifier(VITALITY_DRAIN_MODIFIER);
        maxHealth.removeModifier(SOUL_WITHER_MODIFIER);

        float vitalityDrain = getVitalityDrain(player);
        float soulWither = getSoulWither(player);
        if (vitalityDrain > 0.0f) {
            maxHealth.addTransientModifier(new AttributeModifier(
                    VITALITY_DRAIN_MODIFIER,
                    "Azathoth vitality drain",
                    -vitalityDrain,
                    AttributeModifier.Operation.MULTIPLY_TOTAL
            ));
        }
        if (soulWither > 0.0f) {
            maxHealth.addTransientModifier(new AttributeModifier(
                    SOUL_WITHER_MODIFIER,
                    "Azathoth soul wither",
                    -soulWither,
                    AttributeModifier.Operation.MULTIPLY_TOTAL
            ));
        }

        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
        if (player.getMaxHealth() <= 0.0f) {
            player.kill();
        }
    }
}
