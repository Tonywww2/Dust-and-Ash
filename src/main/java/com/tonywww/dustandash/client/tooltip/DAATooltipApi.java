package com.tonywww.dustandash.client.tooltip;

import com.tonywww.dustandash.client.config.ClientImbaMode;
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
            register(
                item,
                translationBase,
                null,
                detailLines,
                exactSummary,
                exactDetailLines,
                argumentProvider,
                argumentProvider);
            }

            public static void registerImba(
                Item item,
                String translationBase,
                String imbaTranslationBase,
                int detailLines,
                boolean exactSummary,
                int[] exactDetailLines,
                ArgumentProvider argumentProvider,
                ArgumentProvider imbaArgumentProvider) {
            register(
                item,
                translationBase,
                imbaTranslationBase,
                detailLines,
                exactSummary,
                exactDetailLines,
                argumentProvider,
                imbaArgumentProvider);
            }

            private static void register(
                Item item,
                String translationBase,
                String imbaTranslationBase,
                int detailLines,
                boolean exactSummary,
                int[] exactDetailLines,
                ArgumentProvider argumentProvider,
                ArgumentProvider imbaArgumentProvider) {
        Objects.requireNonNull(item, "item");
        Objects.requireNonNull(translationBase, "translationBase");
        Objects.requireNonNull(exactDetailLines, "exactDetailLines");
        Objects.requireNonNull(argumentProvider, "argumentProvider");
            Objects.requireNonNull(imbaArgumentProvider, "imbaArgumentProvider");
        Definition definition = new Definition(
                translationBase,
                imbaTranslationBase,
                detailLines,
                exactSummary,
                Arrays.stream(exactDetailLines).boxed().collect(Collectors.toUnmodifiableSet()),
                argumentProvider,
                imbaArgumentProvider);
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
        boolean imba = definition.imbaTranslationBase() != null && ClientImbaMode.enabled();
        String translationBase = imba
            ? definition.imbaTranslationBase()
            : definition.translationBase();
        boolean detailed = Screen.hasAltDown();
        boolean exact = Screen.hasShiftDown() && definition.hasExactText();
        ArgumentProvider argumentProvider = imba
            ? definition.imbaArgumentProvider()
            : definition.argumentProvider();
        Object[] arguments = exact ? argumentProvider.get(stack) : new Object[0];

        if (imba) {
            lines.add(Component.translatable("tooltip.dustandash.system.imba_active")
                .withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD));
        }

        if (detailed) {
            for (int index = 1; index <= definition.detailLines(); index++) {
                boolean exactLine = exact && definition.exactDetailLines().contains(index);
                lines.add(Component.translatable(
                        translationBase + ".detail." + index
                                        + (exactLine ? ".exact" : ""),
                                exactLine ? arguments : new Object[0])
                        .withStyle(ChatFormatting.GRAY));
            }
        } else {
            boolean exactLine = exact && definition.exactSummary();
            lines.add(Component.translatable(
                        translationBase + ".summary"
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
            String imbaTranslationBase,
            int detailLines,
            boolean exactSummary,
            Set<Integer> exactDetailLines,
            ArgumentProvider argumentProvider,
            ArgumentProvider imbaArgumentProvider) {
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