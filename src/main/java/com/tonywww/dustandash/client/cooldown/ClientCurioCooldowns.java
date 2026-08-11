package com.tonywww.dustandash.client.cooldown;

import com.tonywww.dustandash.cooldown.CurioCooldownManager.CooldownSnapshot;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ClientCurioCooldowns {
    private static final long MILLIS_PER_TICK = 50L;
    private static final Map<ResourceLocation, ActiveCooldown> ACTIVE = new LinkedHashMap<>();

    private ClientCurioCooldowns() {
    }

    public static void replace(List<CooldownSnapshot> snapshots) {
        long now = System.currentTimeMillis();
        ACTIVE.clear();
        for (CooldownSnapshot snapshot : snapshots) {
            if (snapshot.remainingTicks() > 0 && snapshot.durationTicks() > 0) {
                ACTIVE.put(snapshot.id(), new ActiveCooldown(
                        now + (long) snapshot.remainingTicks() * MILLIS_PER_TICK,
                        snapshot.durationTicks()));
            }
        }
    }

    public static Map<ResourceLocation, ActiveCooldown> active() {
        long now = System.currentTimeMillis();
        ACTIVE.entrySet().removeIf(entry -> entry.getValue().endsAtEpochMillis() <= now);
        return Map.copyOf(ACTIVE);
    }

    public record ActiveCooldown(long endsAtEpochMillis, int durationTicks) {
        public float progress(long now) {
            long durationMillis = (long) this.durationTicks * MILLIS_PER_TICK;
            if (durationMillis <= 0L) {
                return 0f;
            }
            float normalized = (float) (this.endsAtEpochMillis - now) / (float) durationMillis;
            return Math.max(0f, Math.min(1f, normalized));
        }
    }
}