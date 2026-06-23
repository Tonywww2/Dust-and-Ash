package com.tonywww.dustandash.cthulhu.client;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ClientHintManager {

    private static final List<HintEntry> HINTS = new ArrayList<>();

    private ClientHintManager() {
    }

    public static void add(Component component, int durationTicks) {
        long now = System.currentTimeMillis();
        HINTS.add(new HintEntry(component, now, Math.max(1, durationTicks) * 50L));
    }

    public static List<HintEntry> activeHints() {
        long now = System.currentTimeMillis();
        Iterator<HintEntry> iterator = HINTS.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isExpired(now)) {
                iterator.remove();
            }
        }
        return List.copyOf(HINTS);
    }

    public record HintEntry(Component component, long startMs, long durationMs) {
        public boolean isExpired(long now) {
            return now - startMs >= durationMs;
        }

        public float alpha(long now) {
            long age = now - startMs;
            float fadeMs = Math.min(400.0f, durationMs / 3.0f);
            if (age < fadeMs) {
                return age / fadeMs;
            }
            long remaining = durationMs - age;
            if (remaining < fadeMs) {
                return Math.max(0.0f, remaining / fadeMs);
            }
            return 1.0f;
        }
    }
}
