package com.tonywww.dustandash.cthulhu.grapheme;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Locale;
import java.util.Set;

public final class WordDictionary {

    private static final Set<String> WORDS = Set.of(
            "VOID",
            "VITAL",
            "SHIELD",
            "BREAK",
            "DELETE",
            "MODIFY",
            "EXIST",
            "REALITY",
            "REALITY EXISTS"
    );

    private WordDictionary() {
    }

    public static boolean isKnown(String word) {
        return WORDS.contains(normalize(word));
    }

    public static String normalize(String word) {
        return word.trim().toUpperCase(Locale.ROOT);
    }

    public static void apply(ServerPlayer player, String word) {
        String normalized = normalize(word);
        switch (normalized) {
            case "VOID" -> CthulhuWordEffects.applyVoid(player);
            case "VITAL" -> CthulhuWordEffects.applyVital(player);
            case "SHIELD" -> CthulhuWordEffects.applyShield(player);
            case "BREAK" -> CthulhuWordEffects.applyBreak(player);
            case "DELETE" -> CthulhuWordEffects.applyDelete(player);
            case "MODIFY" -> CthulhuWordEffects.applyModify(player);
            case "EXIST" -> CthulhuWordEffects.applyExist(player);
            case "REALITY" -> player.sendSystemMessage(Component.literal("REALITY: the final truth resonates."));
            case "REALITY EXISTS" -> player.sendSystemMessage(Component.literal("REALITY EXISTS: the world remembers its shape."));
            default -> player.sendSystemMessage(Component.literal("Unknown law word: " + normalized));
        }
    }
}
