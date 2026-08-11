package com.tonywww.dustandash.cooldown;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.network.CurioCooldownSyncPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public final class CurioCooldownManager {
    public static final ResourceLocation JUDGEMENT = id("judgement");
    public static final ResourceLocation LIGHT_FORGED_HALO = id("light_forged_halo");
    public static final ResourceLocation VOID_RING = id("void_ring");

    private static final String COOLDOWNS_KEY = DustAndAsh.MOD_ID + ":curio_cooldowns";
    private static final String ENDS_AT_KEY = "EndsAtEpochMillis";
    private static final String DURATION_KEY = "DurationTicks";
    private static final long MILLIS_PER_TICK = 50L;

    private CurioCooldownManager() {
    }

    public static boolean isOnCooldown(Player player, ResourceLocation cooldownId) {
        CompoundTag cooldowns = getCooldowns(player);
        String key = cooldownId.toString();
        if (!cooldowns.contains(key, Tag.TAG_COMPOUND)) {
            return false;
        }

        CompoundTag cooldown = cooldowns.getCompound(key);
        if (cooldown.getLong(ENDS_AT_KEY) <= System.currentTimeMillis()) {
            cooldowns.remove(key);
            saveCooldowns(player, cooldowns);
            return false;
        }
        return true;
    }

    public static void start(ServerPlayer player, ResourceLocation cooldownId, int durationTicks) {
        CompoundTag cooldowns = getCooldowns(player);
        String key = cooldownId.toString();
        if (durationTicks <= 0) {
            cooldowns.remove(key);
        } else {
            CompoundTag cooldown = new CompoundTag();
            cooldown.putInt(DURATION_KEY, durationTicks);
            cooldown.putLong(
                    ENDS_AT_KEY,
                    saturatedAdd(System.currentTimeMillis(), (long) durationTicks * MILLIS_PER_TICK));
            cooldowns.put(key, cooldown);
        }
        saveCooldowns(player, cooldowns);
        sync(player);
    }

    public static void sync(ServerPlayer player) {
        CurioCooldownSyncPacket.send(player, snapshots(player));
    }

    public static void copyState(Player original, Player clone) {
        CompoundTag originalData = original.getPersistentData();
        if (originalData.contains(COOLDOWNS_KEY, Tag.TAG_COMPOUND)) {
            clone.getPersistentData().put(
                    COOLDOWNS_KEY,
                    originalData.getCompound(COOLDOWNS_KEY).copy());
        }
    }

    private static List<CooldownSnapshot> snapshots(Player player) {
        CompoundTag cooldowns = getCooldowns(player);
        long now = System.currentTimeMillis();
        List<CooldownSnapshot> snapshots = new ArrayList<>();
        boolean changed = false;

        for (String key : new ArrayList<>(cooldowns.getAllKeys())) {
            if (!cooldowns.contains(key, Tag.TAG_COMPOUND)) {
                cooldowns.remove(key);
                changed = true;
                continue;
            }

            ResourceLocation cooldownId = ResourceLocation.tryParse(key);
            CompoundTag cooldown = cooldowns.getCompound(key);
            long remainingMillis = cooldown.getLong(ENDS_AT_KEY) - now;
            int durationTicks = cooldown.getInt(DURATION_KEY);
            if (cooldownId == null || remainingMillis <= 0L || durationTicks <= 0) {
                cooldowns.remove(key);
                changed = true;
                continue;
            }

            long remainingTicks = Math.max(1L, (remainingMillis + MILLIS_PER_TICK - 1L) / MILLIS_PER_TICK);
            snapshots.add(new CooldownSnapshot(
                    cooldownId,
                    (int) Math.min(Integer.MAX_VALUE, remainingTicks),
                    durationTicks));
        }

        if (changed) {
            saveCooldowns(player, cooldowns);
        }
        return snapshots;
    }

    private static CompoundTag getCooldowns(Player player) {
        return player.getPersistentData().getCompound(COOLDOWNS_KEY);
    }

    private static void saveCooldowns(Player player, CompoundTag cooldowns) {
        player.getPersistentData().put(COOLDOWNS_KEY, cooldowns);
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(DustAndAsh.MOD_ID, path);
    }

    private static long saturatedAdd(long left, long right) {
        if (right > 0L && left > Long.MAX_VALUE - right) {
            return Long.MAX_VALUE;
        }
        return left + right;
    }

    public record CooldownSnapshot(ResourceLocation id, int remainingTicks, int durationTicks) {
    }
}