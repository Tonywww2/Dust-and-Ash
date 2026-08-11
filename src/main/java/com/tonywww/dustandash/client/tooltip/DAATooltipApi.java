package com.tonywww.dustandash.client.tooltip;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class DAATooltipApi {
    private static final Map<Item, Definition> DEFINITIONS = new IdentityHashMap<>();

    private DAATooltipApi() {
    }

    public static void register(
            Item item,
            String translationBase,
            int detailLines,
            StatProvider statProvider) {
        Objects.requireNonNull(item, "item");
        Objects.requireNonNull(translationBase, "translationBase");
        Objects.requireNonNull(statProvider, "statProvider");
        Definition definition = new Definition(translationBase, detailLines, statProvider);
        if (DEFINITIONS.putIfAbsent(item, definition) != null) {
            throw new IllegalArgumentException("Duplicate tooltip definition for " + item);
        }
    }

    public static void apply(ItemStack stack, List<Component> tooltip) {
        Definition definition = DEFINITIONS.get(stack.getItem());
        if (definition == null) {
            return;
        }

        tooltip.removeIf(DAATooltipApi::isLegacyDustAndAshTooltip);
        List<Component> lines = new ArrayList<>();
        lines.add(Component.translatable(definition.translationBase() + ".summary")
                .withStyle(ChatFormatting.WHITE));

        if (Screen.hasShiftDown()) {
            lines.add(Component.translatable("tooltip.dustandash.system.values")
                    .withStyle(ChatFormatting.AQUA));
            definition.statProvider().add(stack, lines);
        } else {
            lines.add(keyPrompt("tooltip.dustandash.system.hold_shift", "Shift"));
        }

        if (Screen.hasAltDown()) {
            lines.add(Component.translatable("tooltip.dustandash.system.details")
                    .withStyle(ChatFormatting.AQUA));
            for (int index = 1; index <= definition.detailLines(); index++) {
                lines.add(Component.translatable(
                                definition.translationBase() + ".detail." + index)
                        .withStyle(ChatFormatting.GRAY));
            }
        } else {
            lines.add(keyPrompt("tooltip.dustandash.system.hold_alt", "Alt"));
        }

        tooltip.addAll(Math.min(1, tooltip.size()), lines);
    }

    private static Component keyPrompt(String translationKey, String keyName) {
        return Component.translatable(
                        translationKey,
                        Component.literal(keyName).withStyle(ChatFormatting.YELLOW))
                .withStyle(ChatFormatting.GRAY);
    }

    private static boolean isLegacyDustAndAshTooltip(Component component) {
        return component.getContents() instanceof TranslatableContents contents
                && contents.getKey().startsWith("tooltip.dustandash.");
    }

    public record Definition(
            String translationBase,
            int detailLines,
            StatProvider statProvider) {
        public Definition {
            if (detailLines < 1) {
                throw new IllegalArgumentException("detailLines must be positive");
            }
        }
    }

    @FunctionalInterface
    public interface StatProvider {
        void add(ItemStack stack, List<Component> tooltip);
    }
}