package com.tonywww.dustandash.game;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

public final class TemporaryGameModeController {
    private static final String STATE_KEY = DustAndAsh.MOD_ID + ":temporary_game_mode";
    private static final String ORIGINAL_MODE_KEY = "OriginalMode";
    private static final String ENDS_AT_KEY = "EndsAtEpochMillis";
    private static final long MILLIS_PER_TICK = 50L;

    private TemporaryGameModeController() {
    }

    public static void enterSpectator(ServerPlayer player, int durationTicks) {
        if (durationTicks <= 0) {
            return;
        }

        CompoundTag playerData = player.getPersistentData();
        CompoundTag state;
        if (playerData.contains(STATE_KEY, Tag.TAG_COMPOUND)) {
            state = playerData.getCompound(STATE_KEY);
        } else {
            state = new CompoundTag();
            state.putInt(ORIGINAL_MODE_KEY, player.gameMode.getGameModeForPlayer().getId());
        }

        long durationMillis = (long) durationTicks * MILLIS_PER_TICK;
        state.putLong(ENDS_AT_KEY, saturatedAdd(System.currentTimeMillis(), durationMillis));
        playerData.put(STATE_KEY, state);
        enforceSpectator(player);
    }

    public static void reconcile(ServerPlayer player) {
        CompoundTag playerData = player.getPersistentData();
        if (!playerData.contains(STATE_KEY, Tag.TAG_COMPOUND)) {
            return;
        }

        CompoundTag state = playerData.getCompound(STATE_KEY);
        if (System.currentTimeMillis() >= state.getLong(ENDS_AT_KEY)) {
            GameType originalMode = GameType.byId(state.getInt(ORIGINAL_MODE_KEY));
            playerData.remove(STATE_KEY);
            if (player.gameMode.getGameModeForPlayer() != originalMode) {
                player.setGameMode(originalMode);
            }
            return;
        }

        enforceSpectator(player);
    }

    public static boolean isActive(Player player) {
        return player.getPersistentData().contains(STATE_KEY, Tag.TAG_COMPOUND);
    }

    public static void copyState(Player original, Player clone) {
        CompoundTag originalData = original.getPersistentData();
        if (originalData.contains(STATE_KEY, Tag.TAG_COMPOUND)) {
            clone.getPersistentData().put(STATE_KEY, originalData.getCompound(STATE_KEY).copy());
        }
    }

    private static void enforceSpectator(ServerPlayer player) {
        if (player.gameMode.getGameModeForPlayer() != GameType.SPECTATOR) {
            player.setGameMode(GameType.SPECTATOR);
        }
    }

    private static long saturatedAdd(long left, long right) {
        if (right > 0L && left > Long.MAX_VALUE - right) {
            return Long.MAX_VALUE;
        }
        return left + right;
    }
}