package com.tonywww.dustandash.cthulhu.api;

import com.tonywww.dustandash.cthulhu.fight.BossFightInstance;
import com.tonywww.dustandash.cthulhu.fight.BossFightManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

public final class GraphemeAPI {

    private GraphemeAPI() {
    }

    public static void giveLetter(ServerPlayer player, char letter) {
        BossFightInstance instance = BossFightManager.get().getActiveFight((ServerLevel) player.level());
        if (instance != null) {
            instance.giveLetter(player, letter);
            BossFightManager.get().save(player.server);
        }
    }

    public static boolean canSpell(ServerPlayer player, String word) {
        BossFightInstance instance = BossFightManager.get().getActiveFight((ServerLevel) player.level());
        return instance != null && instance.canSpell(player, word);
    }

    public static boolean trySpell(ServerPlayer player, String word, Consumer<ServerPlayer> successAction) {
        BossFightInstance instance = BossFightManager.get().getActiveFight((ServerLevel) player.level());
        if (instance == null || !instance.consumeWord(player, word)) {
            return false;
        }

        successAction.accept(player);
        BossFightManager.get().save(player.server);
        return true;
    }

    public static void clearLetters(ServerPlayer player) {
        BossFightInstance instance = BossFightManager.get().getActiveFight((ServerLevel) player.level());
        if (instance != null) {
            instance.clearLetters(player);
            BossFightManager.get().save(player.server);
        }
    }
}
