package com.tonywww.dustandash.cthulhu.entity;

import com.tonywww.dustandash.cthulhu.client.CthulhuRenderMode;
import com.tonywww.dustandash.cthulhu.fight.BossFightInstance;
import com.tonywww.dustandash.cthulhu.fight.BossFightManager;
import com.tonywww.dustandash.cthulhu.fight.FightPhase;
import com.tonywww.dustandash.cthulhu.render.CthulhuMinionRenderHelper;
import com.tonywww.dustandash.registeries.ModEntites;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

import java.util.List;

public class CthulhuBossPhase1Entity extends Monster {

    public CthulhuBossPhase1Entity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.setNoAi(true);
        this.setNoGravity(true);
        this.setInvisible(true);
        this.setPersistenceRequired();
        this.xpReward = 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 180.0d)
                .add(Attributes.MOVEMENT_SPEED, 0.0d)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0d)
                .add(Attributes.ARMOR, 14.0d);
    }

    @Override
    public void tick() {
        super.tick();
        setDeltaMovement(Vec3.ZERO);

        if (level().isClientSide()) {
            return;
        }

        BossFightInstance instance = BossFightManager.get().getActiveFight((ServerLevel) level());
        if (instance == null || instance.phase() != FightPhase.PHASE_1) {
            discard();
            return;
        }

        hoverNearCore(instance);
        if (tickCount % 60 == 0) {
            throwLawField(instance);
        }
        if (tickCount % 140 == 0) {
            spawnPressureMinion(instance);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            amount *= 0.25f;
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
            if (instance != null && instance.phase() == FightPhase.PHASE_1) {
                instance.setPhase(FightPhase.PHASE_2);
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

    private void hoverNearCore(BossFightInstance instance) {
        double pulse = Math.sin(tickCount * 0.05d) * 0.35d;
        setPos(
                instance.corePos().getX() + 0.5d,
                instance.corePos().getY() + 5.0d + pulse,
                instance.corePos().getZ() + 0.5d
        );
    }

    private void throwLawField(BossFightInstance instance) {
        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        ServerPlayer target = randomParticipant(instance);
        if (target == null) {
            return;
        }

        CthulhuLawFieldEntity.LawFieldType[] types = CthulhuLawFieldEntity.LawFieldType.values();
        String letters = "VOIDVITAL";
        CthulhuLawFieldEntity field = new CthulhuLawFieldEntity(ModEntites.CTHULHU_LAW_FIELD.get(), serverLevel);
        field.setFieldType(types[random.nextInt(types.length)]);
        field.setDropLetter(letters.charAt(random.nextInt(letters.length())));

        double angle = random.nextDouble() * Math.PI * 2.0d;
        double radius = 2.0d + random.nextDouble() * 5.0d;
        field.moveTo(
                target.getX() + Math.cos(angle) * radius,
                target.getY(),
                target.getZ() + Math.sin(angle) * radius,
                random.nextFloat() * 360.0f,
                0.0f
        );
        joinBossTeam(field);
        serverLevel.addFreshEntity(field);
    }

    private void spawnPressureMinion(BossFightInstance instance) {
        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        ServerPlayer target = randomParticipant(instance);
        if (target == null) {
            return;
        }

        Mob minion = random.nextBoolean()
                ? new Zombie(EntityType.ZOMBIE, serverLevel)
                : new Spider(EntityType.SPIDER, serverLevel);
        double angle = random.nextDouble() * Math.PI * 2.0d;
        minion.moveTo(
                target.getX() + Math.cos(angle) * 6.0d,
                target.getY(),
                target.getZ() + Math.sin(angle) * 6.0d,
                random.nextFloat() * 360.0f,
                0.0f
        );
        minion.setPersistenceRequired();
        joinBossTeam(minion);
        serverLevel.addFreshEntity(minion);
        CthulhuMinionRenderHelper.setRenderMode(minion, random.nextBoolean() ? CthulhuRenderMode.NOISE : CthulhuRenderMode.WIREFRAME);
    }

    private ServerPlayer randomParticipant(BossFightInstance instance) {
        List<ServerPlayer> players = ((ServerLevel) level()).players().stream()
                .filter(player -> instance.isParticipant(player.getUUID()))
                .filter(player -> !instance.isBanished(player.getUUID()))
                .filter(Entity::isAlive)
                .toList();
        if (players.isEmpty()) {
            return null;
        }
        return players.get(Mth.clamp(random.nextInt(players.size()), 0, players.size() - 1));
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
}
