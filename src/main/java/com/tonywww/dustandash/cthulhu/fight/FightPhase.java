package com.tonywww.dustandash.cthulhu.fight;

import java.util.Locale;

public enum FightPhase {
    PHASE_1,
    PHASE_2,
    PHASE_3,
    FINAL_TRUTH,
    TERMINATED;

    public static FightPhase byName(String name) {
        return FightPhase.valueOf(name.toUpperCase(Locale.ROOT));
    }
}
