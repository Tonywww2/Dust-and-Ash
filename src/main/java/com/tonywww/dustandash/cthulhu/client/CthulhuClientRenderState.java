package com.tonywww.dustandash.cthulhu.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class CthulhuClientRenderState {

    private static final Map<Integer, CthulhuRenderMode> ENTITY_MODES = new HashMap<>();

    private CthulhuClientRenderState() {
    }

    public static void setMode(int entityId, CthulhuRenderMode mode) {
        ENTITY_MODES.put(entityId, mode);
    }

    public static void removeMode(int entityId) {
        ENTITY_MODES.remove(entityId);
    }

    public static Optional<CthulhuRenderMode> getMode(int entityId) {
        return Optional.ofNullable(ENTITY_MODES.get(entityId));
    }

    public static void clear() {
        ENTITY_MODES.clear();
    }
}
