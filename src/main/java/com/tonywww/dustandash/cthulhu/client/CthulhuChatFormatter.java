package com.tonywww.dustandash.cthulhu.client;

import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.HashMap;
import java.util.Map;

public final class CthulhuChatFormatter {

    private static final Style OWNED = Style.EMPTY.withColor(0xFFFFFF);
    private static final Style MISSING = Style.EMPTY.withColor(0x7A2020);

    private CthulhuChatFormatter() {
    }

    public static FormattedCharSequence format(String text, int offset) {
        if (ClientGraphemeData.isEmpty()) {
            return FormattedCharSequence.forward(text, Style.EMPTY);
        }

        Map<Character, Integer> remaining = new HashMap<>(ClientGraphemeData.letters());
        return sink -> {
            int visualIndex = 0;
            for (int i = 0; i < text.length(); i++) {
                char raw = text.charAt(i);
                char letter = Character.toUpperCase(raw);
                Style style = MISSING;
                if (letter >= 'A' && letter <= 'Z') {
                    int count = remaining.getOrDefault(letter, 0);
                    if (count > 0) {
                        remaining.put(letter, count - 1);
                        style = OWNED;
                    }
                }
                if (!sink.accept(visualIndex++, style, raw)) {
                    return false;
                }
            }
            return true;
        };
    }
}
