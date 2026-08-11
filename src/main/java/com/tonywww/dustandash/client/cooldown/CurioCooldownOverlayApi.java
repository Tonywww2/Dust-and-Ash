package com.tonywww.dustandash.client.cooldown;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class CurioCooldownOverlayApi {
    private static final Map<ResourceLocation, Definition> DEFINITIONS = new LinkedHashMap<>();

    private CurioCooldownOverlayApi() {
    }

    public static void register(
            ResourceLocation cooldownId,
            Supplier<? extends Item> item,
            int trackColor,
            int fillColor) {
        Objects.requireNonNull(cooldownId, "cooldownId");
        Objects.requireNonNull(item, "item");
        if (DEFINITIONS.putIfAbsent(
                cooldownId,
                new Definition(cooldownId, item, trackColor, fillColor)) != null) {
            throw new IllegalArgumentException("Duplicate curio cooldown overlay id: " + cooldownId);
        }
    }

    public static List<VisibleCooldown> visibleCooldowns() {
        long now = System.currentTimeMillis();
        Map<ResourceLocation, ClientCurioCooldowns.ActiveCooldown> active = ClientCurioCooldowns.active();
        List<VisibleCooldown> visible = new ArrayList<>();
        for (Definition definition : DEFINITIONS.values()) {
            ClientCurioCooldowns.ActiveCooldown cooldown = active.get(definition.id());
            if (cooldown != null) {
                visible.add(new VisibleCooldown(definition, cooldown.progress(now)));
            }
        }
        return List.copyOf(visible);
    }

    public record Definition(
            ResourceLocation id,
            Supplier<? extends Item> item,
            int trackColor,
            int fillColor) {
    }

    public record VisibleCooldown(Definition definition, float progress) {
    }
}