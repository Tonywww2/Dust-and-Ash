package com.tonywww.dustandash.cthulhu.client;

import com.tonywww.dustandash.cthulhu.fight.FightPhase;

import java.util.Optional;

public final class ClientCthulhuPhaseState {

    private static FightPhase phase;

    private ClientCthulhuPhaseState() {
    }

    public static void setPhase(FightPhase fightPhase) {
        phase = fightPhase;
    }

    public static void clear() {
        phase = null;
    }

    public static Optional<FightPhase> phase() {
        return Optional.ofNullable(phase);
    }

    public static boolean isFightActive() {
        return phase != null && phase != FightPhase.TERMINATED;
    }

    public static boolean isGridPhase() {
        return phase == FightPhase.PHASE_3 || phase == FightPhase.FINAL_TRUTH;
    }
}
