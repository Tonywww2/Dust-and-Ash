package com.tonywww.dustandash.cthulhu.grapheme;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.cthulhu.api.HealthDrainAPI;
import com.tonywww.dustandash.cthulhu.entity.CthulhuPillarEntity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuStormGolemEntity;
import com.tonywww.dustandash.cthulhu.fight.BossFightInstance;
import com.tonywww.dustandash.cthulhu.fight.BossFightManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;

public final class CthulhuWordEffects {

    private static final String VOID_PROTECTION_UNTIL = DustAndAsh.MOD_ID + ".cthulhu_void_protection_until";
    private static final String SHIELD_CHARGES = DustAndAsh.MOD_ID + ".cthulhu_shield_charges";
    private static final String DEATH_SCRIPT_DEADLINE = DustAndAsh.MOD_ID + ".cthulhu_death_script_deadline";
    private static final int BREAK_DURATION_TICKS = 20 * 20;

    private CthulhuWordEffects() {
    }

    public static void applyVoid(ServerPlayer player) {
        player.getPersistentData().putInt(VOID_PROTECTION_UNTIL, player.server.getTickCount() + 200);
        player.sendSystemMessage(Component.literal("VOID: void and stasis fields rejected for 10 seconds."));
    }

    public static boolean hasVoidProtection(ServerPlayer player) {
        return player.getPersistentData().getInt(VOID_PROTECTION_UNTIL) > player.server.getTickCount();
    }

    public static void applyVital(ServerPlayer player) {
        HealthDrainAPI.clearVitalityDrain(player);
        player.getFoodData().setFoodLevel(Math.max(player.getFoodData().getFoodLevel(), 18));
        player.getFoodData().setSaturation(Math.max(player.getFoodData().getSaturationLevel(), 8.0f));
        player.sendSystemMessage(Component.literal("VITAL: vitality drain cleared and hunger stabilized."));
    }

    public static void applyShield(ServerPlayer player) {
        int charges = Math.min(3, player.getPersistentData().getInt(SHIELD_CHARGES) + 1);
        player.getPersistentData().putInt(SHIELD_CHARGES, charges);
        player.sendSystemMessage(Component.literal("SHIELD: one nearby law missile will be intercepted. Charges=" + charges));
    }

    public static boolean consumeShield(ServerPlayer player) {
        int charges = player.getPersistentData().getInt(SHIELD_CHARGES);
        if (charges <= 0) {
            return false;
        }

        player.getPersistentData().putInt(SHIELD_CHARGES, charges - 1);
        player.sendSystemMessage(Component.literal("SHIELD: law missile intercepted."));
        return true;
    }

    public static void applyBreak(ServerPlayer player) {
        BossFightInstance instance = BossFightManager.get().findParticipantFight(player);
        if (instance == null || !(player.level() instanceof ServerLevel serverLevel)) {
            player.sendSystemMessage(Component.literal("BREAK: no active pillar link found."));
            return;
        }

        List<CthulhuPillarEntity> pillars = serverLevel.getEntitiesOfClass(
                CthulhuPillarEntity.class,
                new AABB(player.blockPosition()).inflate(64.0d),
                CthulhuPillarEntity::isAlive
        );
        if (pillars.isEmpty()) {
            player.sendSystemMessage(Component.literal("BREAK: no pillar target in range."));
            return;
        }

        pillars.sort(Comparator.comparingDouble(pillar -> pillar.distanceToSqr(player)));
        int affected = 0;
        for (CthulhuPillarEntity pillar : pillars) {
            if (affected >= 2) {
                break;
            }
            pillar.applyBreak(BREAK_DURATION_TICKS);
            affected++;
        }
        player.sendSystemMessage(Component.literal("BREAK: severed " + affected + " pillar link(s) for 20 seconds."));
    }

    public static void markDeathScript(ServerPlayer player, int deadlineTick) {
        player.getPersistentData().putInt(DEATH_SCRIPT_DEADLINE, deadlineTick);
    }

    public static int getDeathScriptDeadline(ServerPlayer player) {
        return player.getPersistentData().getInt(DEATH_SCRIPT_DEADLINE);
    }

    public static void clearDeathScript(ServerPlayer player) {
        player.getPersistentData().remove(DEATH_SCRIPT_DEADLINE);
    }

    public static void applyDelete(ServerPlayer player) {
        clearDeathScript(player);
        player.sendSystemMessage(Component.literal("DELETE: death script erased."));
    }

    public static void applyModify(ServerPlayer player) {
        clearDeathScript(player);
        player.sendSystemMessage(Component.literal("MODIFY: death script rewritten."));
    }

    public static void applyExist(ServerPlayer player) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            player.sendSystemMessage(Component.literal("EXIST: no concept matrix found."));
            return;
        }

        List<CthulhuStormGolemEntity> stormGolems = serverLevel.getEntitiesOfClass(
                CthulhuStormGolemEntity.class,
                new AABB(player.blockPosition()).inflate(96.0d),
                CthulhuStormGolemEntity::isAlive
        );
        if (stormGolems.isEmpty()) {
            player.sendSystemMessage(Component.literal("EXIST: no absolute defense found."));
            return;
        }

        stormGolems.sort(Comparator.comparingDouble(stormGolem -> stormGolem.distanceToSqr(player)));
        stormGolems.get(0).clearAbsoluteDefense();
        player.sendSystemMessage(Component.literal("EXIST: absolute defense collapses."));
    }
}
