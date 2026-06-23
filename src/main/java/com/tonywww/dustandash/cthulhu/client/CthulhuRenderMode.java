package com.tonywww.dustandash.cthulhu.client;

import net.minecraft.world.entity.LivingEntity;

import java.util.Locale;
import java.util.Optional;

public enum CthulhuRenderMode {
    NOISE("noise"),
    WIREFRAME("wireframe"),
    TEXT_STATIC("text_static");

    public static final String PERSISTENT_DATA_KEY = "cthulhu_render";

    private final String serializedName;

    CthulhuRenderMode(String serializedName) {
        this.serializedName = serializedName;
    }

    public static Optional<CthulhuRenderMode> fromEntity(LivingEntity entity) {
        String rawValue = entity.getPersistentData().getString(PERSISTENT_DATA_KEY);
        return fromName(rawValue);
    }

    public static Optional<CthulhuRenderMode> fromName(String rawValue) {
        if (rawValue.isBlank()) {
            return Optional.empty();
        }

        String normalizedValue = rawValue.toLowerCase(Locale.ROOT);
        for (CthulhuRenderMode mode : values()) {
            if (mode.serializedName.equals(normalizedValue)) {
                return Optional.of(mode);
            }
        }
        return Optional.empty();
    }

    public String serializedName() {
        return serializedName;
    }
}
