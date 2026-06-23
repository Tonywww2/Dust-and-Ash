package com.tonywww.dustandash.cthulhu.entity;

import com.tonywww.dustandash.cthulhu.fight.BossFightInstance;
import com.tonywww.dustandash.cthulhu.fight.BossFightManager;
import com.tonywww.dustandash.cthulhu.fight.FightPhase;
import com.tonywww.dustandash.cthulhu.grapheme.CthulhuWordEffects;
import com.tonywww.dustandash.registeries.ModEntites;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

import java.util.Comparator;
import java.util.List;

public class CthulhuStormGolemEntity extends Monster {

    private static final String TAG_ABSOLUTE_DEFENSE_UNTIL = "AbsoluteDefenseUntil";
    private int absoluteDefenseUntil;

    public CthulhuStormGolemEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.setNoAi(true);
        this.setNoGravity(true);
        this.setInvisible(true);
        this.setPersistenceRequired();
        this.xpReward = 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 260.0d)
                .add(Attributes.MOVEMENT_SPEED, 0.0d)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0d)
                .add(Attributes.ARMOR, 10.0d);
    }

    @Override
    public void tick() {
        super.tick();
        setDeltaMovement(Vec3.ZERO);

        if (level().isClientSide()) {
            return;
        }

        BossFightInstance instance = BossFightManager.get().getActiveFight((ServerLevel) level());
        if (instance == null || (instance.phase() != FightPhase.PHASE_3 && instance.phase() != FightPhase.FINAL_TRUTH)) {
            discard();
            return;
        }

        hoverNearCore(instance);
        checkDeathScripts(instance);
        if (tickCount % 80 == 0) {
            castTextBarrage(instance);
        }
        if (tickCount % 260 == 80) {
            writeDeathScript(instance);
        }
        if (tickCount % 360 == 140) {
            activateAbsoluteDefense(instance);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isAbsoluteDefenseActive()) {
            Entity attacker = source.getEntity();
            if (attacker instanceof ServerPlayer player) {
                player.sendSystemMessage(net.minecraft.network.chat.Component.literal("EXIST is required to collapse absolute defense."));
            }
            return false;
        }

        this.invulnerableTime = 0;
        boolean hurt = super.hurt(source, amount);
        this.invulnerableTime = 0;
        return hurt;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (level() instanceof ServerLevel serverLevel) {
            BossFightInstance instance = BossFightManager.get().getActiveFight(serverLevel);
            if (instance != null && instance.phase() == FightPhase.PHASE_3) {
                instance.setPhase(FightPhase.FINAL_TRUTH);
                BossFightManager.get().save(serverLevel.getServer());
            }
        }
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public boolean isAbsoluteDefenseActive() {
        return tickCount < absoluteDefenseUntil;
    }

    public int getAbsoluteDefenseTicksLeft() {
        return Math.max(0, absoluteDefenseUntil - tickCount);
    }

    public void clearAbsoluteDefense() {
        absoluteDefenseUntil = 0;
    }

    private void hoverNearCore(BossFightInstance instance) {
        double pulse = Math.sin(tickCount * 0.04d) * 0.55d;
        setPos(
                instance.corePos().getX() + 0.5d,
                instance.corePos().getY() + 7.0d + pulse,
                instance.corePos().getZ() + 0.5d
        );
    }

    private void castTextBarrage(BossFightInstance instance) {
        for (ServerPlayer player : fightPlayers(instance)) {
            if (player.distanceToSqr(this) <= 28.0d * 28.0d) {
                player.hurt(damageSources().mobAttack(this), 6.0f);
                player.sendSystemMessage(net.minecraft.network.chat.Component.literal("SLASH/BULLET: textual barrage tears through space."));
            }
        }
    }

    private void writeDeathScript(BossFightInstance instance) {
        ServerPlayer target = fightPlayers(instance).stream()
                .min(Comparator.comparingDouble(player -> player.distanceToSqr(this)))
                .orElse(null);
        if (target == null) {
            return;
        }

        CthulhuWordEffects.markDeathScript(target, target.server.getTickCount() + 100);
        target.sendSystemMessage(net.minecraft.network.chat.Component.literal(target.getGameProfile().getName() + " · DIE"));
        target.sendSystemMessage(net.minecraft.network.chat.Component.literal("Type DELETE or MODIFY within 5 seconds."));
    }

    private void checkDeathScripts(BossFightInstance instance) {
        for (ServerPlayer player : fightPlayers(instance)) {
            int deadline = CthulhuWordEffects.getDeathScriptDeadline(player);
            if (deadline > 0 && player.server.getTickCount() >= deadline) {
                CthulhuWordEffects.clearDeathScript(player);
                player.hurt(damageSources().magic(), Float.MAX_VALUE);
                if (player.isAlive()) {
                    player.kill();
                }
            }
        }
    }

    private void activateAbsoluteDefense(BossFightInstance instance) {
        absoluteDefenseUntil = tickCount + 200;
        spawnExistLetters();
        for (ServerPlayer player : fightPlayers(instance)) {
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Absolute defense: gather EXIST."));
        }
    }

    private void spawnExistLetters() {
        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        String letters = "EXIST";
        for (int i = 0; i < letters.length(); i++) {
            double angle = Math.PI * 2.0d * i / letters.length();
            CthulhuGraphemeEntity grapheme = new CthulhuGraphemeEntity(ModEntites.CTHULHU_GRAPHEME.get(), serverLevel);
            grapheme.setLetter(letters.charAt(i));
            grapheme.setPickupDelay(20);
            grapheme.moveTo(
                    getX() + Math.cos(angle) * 4.0d,
                    getY() - 2.0d,
                    getZ() + Math.sin(angle) * 4.0d,
                    random.nextFloat() * 360.0f,
                    0.0f
            );
            serverLevel.addFreshEntity(grapheme);
        }
    }

    private List<ServerPlayer> fightPlayers(BossFightInstance instance) {
        return ((ServerLevel) level()).players().stream()
                .filter(player -> instance.isParticipant(player.getUUID()))
                .filter(player -> !instance.isBanished(player.getUUID()))
                .filter(Entity::isAlive)
                .toList();
    }

    private void joinBossTeam(Entity entity) {
        Scoreboard scoreboard = level().getScoreboard();
        PlayerTeam team = scoreboard.getPlayerTeam("cthulhu_boss");
        if (team == null) {
            team = scoreboard.addPlayerTeam("cthulhu_boss");
        }
        team.setAllowFriendlyFire(false);
        team.setSeeFriendlyInvisibles(true);
        scoreboard.addPlayerToTeam(entity.getStringUUID(), team);
    }

    @Override
    public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(TAG_ABSOLUTE_DEFENSE_UNTIL, absoluteDefenseUntil);
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        absoluteDefenseUntil = tag.getInt(TAG_ABSOLUTE_DEFENSE_UNTIL);
    }
}
