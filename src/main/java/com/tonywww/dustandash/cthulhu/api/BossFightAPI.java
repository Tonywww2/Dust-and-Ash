package com.tonywww.dustandash.cthulhu.api;

import com.tonywww.dustandash.cthulhu.fight.BossFightInstance;
import com.tonywww.dustandash.cthulhu.fight.BossFightManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class BossFightAPI {

    private BossFightAPI() {
    }

    public static BossFightInstance startFight(ServerLevel level, BlockPos corePos) {
        return BossFightManager.get().startFight(level, corePos);
    }

    public static void terminateFight(BossFightInstance instance) {
        BossFightManager.get().terminateFight(instance);
    }

    public static BossFightInstance getActiveFight(ServerLevel level) {
        return BossFightManager.get().getActiveFight(level);
    }

    public static void banishPlayer(ServerPlayer player, BossFightInstance instance) {
        BossFightManager.get().banishPlayer(player, instance);
    }
}
