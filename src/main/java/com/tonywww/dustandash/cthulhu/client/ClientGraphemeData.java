package com.tonywww.dustandash.cthulhu.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ClientGraphemeData {

    private static final Map<Character, Integer> LETTERS = new HashMap<>();

    private ClientGraphemeData() {
    }

    public static void replaceLetters(Map<Character, Integer> letters) {
        LETTERS.clear();
        LETTERS.putAll(letters);
    }

    public static Map<Character, Integer> letters() {
        return Collections.unmodifiableMap(LETTERS);
    }

    public static boolean isEmpty() {
        return LETTERS.isEmpty();
    }
}
