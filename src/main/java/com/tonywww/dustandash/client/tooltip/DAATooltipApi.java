package com.tonywww.dustandash.client.tooltip;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class DAATooltipApi {
    private static final Map<Item, Definition> DEFINITIONS = new IdentityHashMap<>();

    private DAATooltipApi() {
    }

    public static void register(
            Item item,
            String translationBase,
            int detailLines,
            boolean exactSummary,
            int[] exactDetailLines,
            ArgumentProvider argumentProvider) {
        Objects.requireNonNull(item, "item");
        Objects.requireNonNull(translationBase, "translationBase");
        Objects.requireNonNull(exactDetailLines, "exactDetailLines");
        Objects.requireNonNull(argumentProvider, "argumentProvider");
        Definition definition = new Definition(
                translationBase,
                detailLines,
                exactSummary,
                Arrays.stream(exactDetailLines).boxed().collect(Collectors.toUnmodifiableSet()),
                argumentProvider);
        if (DEFINITIONS.putIfAbsent(item, definition) != null) {
            throw new IllegalArgumentException("Duplicate tooltip definition for " + item);
        }
    }

    public static void apply(ItemStack stack, List<Component> tooltip) {
        Definition definition = DEFINITIONS.get(stack.getItem());
        if (definition == null) {
            return;
        }

        List<Component> lines = new ArrayList<>();
        boolean detailed = Screen.hasAltDown();
        boolean exact = Screen.hasShiftDown() && definition.hasExactText();
        Object[] arguments = exact ? definition.argumentProvider().get(stack) : new Object[0];

        if (detailed) {
            for (int index = 1; index <= definition.detailLines(); index++) {
                boolean exactLine = exact && definition.exactDetailLines().contains(index);
                lines.add(Component.translatable(
                                definition.translationBase() + ".detail." + index
                                        + (exactLine ? ".exact" : ""),
                                exactLine ? arguments : new Object[0])
                        .withStyle(ChatFormatting.GRAY));
            }
        } else {
            boolean exactLine = exact && definition.exactSummary();
            lines.add(Component.translatable(
                            definition.translationBase() + ".summary"
                                    + (exactLine ? ".exact" : ""),
                            exactLine ? arguments : new Object[0])
                    .withStyle(ChatFormatting.WHITE));
        }

        if (!detailed) {
            lines.add(keyPrompt("tooltip.dustandash.system.hold_alt", "Alt"));
        }
        if (definition.hasExactText() && !Screen.hasShiftDown()) {
            lines.add(keyPrompt("tooltip.dustandash.system.hold_shift", "Shift"));
        }

        tooltip.addAll(Math.min(1, tooltip.size()), lines);
    }

    private static Component keyPrompt(String translationKey, String keyName) {
        return Component.translatable(
                        translationKey,
                        Component.literal(keyName).withStyle(ChatFormatting.YELLOW))
                .withStyle(ChatFormatting.GRAY);
    }

    public record Definition(
            String translationBase,
            int detailLines,
            boolean exactSummary,
            Set<Integer> exactDetailLines,
            ArgumentProvider argumentProvider) {
        public Definition {
            if (detailLines < 1) {
                throw new IllegalArgumentException("detailLines must be positive");
            }
            if (exactDetailLines.stream().anyMatch(index -> index < 1 || index > detailLines)) {
                throw new IllegalArgumentException("Exact detail line is outside the registered range");
            }
        }

        public boolean hasExactText() {
            return this.exactSummary || !this.exactDetailLines.isEmpty();
        }
    }

    @FunctionalInterface
    public interface ArgumentProvider {
        Object[] get(ItemStack stack);
    }
}