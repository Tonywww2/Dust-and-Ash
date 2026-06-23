package com.tonywww.dustandash.cthulhu.entity;

import com.tonywww.dustandash.cthulhu.config.CthulhuConfig;
import com.tonywww.dustandash.cthulhu.api.HealthDrainAPI;
import com.tonywww.dustandash.cthulhu.client.CthulhuRenderMode;
import com.tonywww.dustandash.cthulhu.fight.BossFightInstance;
import com.tonywww.dustandash.cthulhu.fight.BossFightManager;
import com.tonywww.dustandash.cthulhu.grapheme.CthulhuWordEffects;
import com.tonywww.dustandash.cthulhu.render.CthulhuMinionRenderHelper;
import com.tonywww.dustandash.registeries.ModEntites;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class CthulhuPillarEntity extends Monster {

    private static final String TAG_OUTPUT_WINDOW_END = "OutputWindowEnd";
    private static final String TAG_INVUL_END = "InvulEnd";
    private static final String TAG_DROP_LETTERS = "DropLetters";
    private static final String TAG_PILLAR_TYPE = "PillarType";
    private static final String TAG_BREAK_UNTIL = "BreakUntil";
    private static final String TAG_PENDING_INVUL_TICKS = "PendingInvulTicks";
    private static final net.minecraft.network.syncher.EntityDataAccessor<String> DATA_PILLAR_TYPE =
            net.minecraft.network.syncher.SynchedEntityData.defineId(CthulhuPillarEntity.class, net.minecraft.network.syncher.EntityDataSerializers.STRING);

    private int outputWindowEnd;
    private int invulEnd;
    private int breakUntil;
    private int pendingInvulTicks = CthulhuConfig.PILLAR_INVUL_TICKS;
    private String dropLetters = "";
    private PillarType pillarType = PillarType.GENERIC;

    public CthulhuPillarEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.setNoAi(true);
        this.setPersistenceRequired();
        this.setNoGravity(true);
        this.xpReward = 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 120.0d)
                .add(Attributes.MOVEMENT_SPEED, 0.0d)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0d)
                .add(Attributes.ARMOR, 8.0d);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PILLAR_TYPE, PillarType.GENERIC.name());
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(Vec3.ZERO);

        if (!level().isClientSide()) {
            if (outputWindowEnd > 0 && tickCount >= outputWindowEnd && tickCount >= invulEnd) {
                if (isLinkBroken()) {
                    invulEnd = 0;
                } else {
                    invulEnd = tickCount + Math.max(0, pendingInvulTicks);
                }
                outputWindowEnd = 0;
            }
            runPillarMechanics();
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.isAlive() || this.level().isClientSide()) {
            return super.hurt(source, amount);
        }

        if (tickCount < invulEnd) {
            return false;
        }

        if (pillarType == PillarType.DESPAIR && isFarPhysicalHit(source)) {
            Entity attacker = source.getEntity();
            if (attacker instanceof ServerPlayer player) {
                player.sendSystemMessage(net.minecraft.network.chat.Component.literal("DESPAIR: distant physical attacks are denied."));
            }
            return false;
        }

        pendingInvulTicks = calculateInvulTicks(source);
        amount = calculateDamage(source, amount);
        this.invulnerableTime = 0;
        boolean hurt = super.hurt(source, amount);
        if (hurt) {
            outputWindowEnd = tickCount + CthulhuConfig.PILLAR_OUTPUT_WINDOW_TICKS;
            invulEnd = isLinkBroken() ? 0 : invulEnd;
            this.invulnerableTime = 0;
        }
        return hurt;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (level() instanceof ServerLevel serverLevel) {
            spawnDropLetters(serverLevel);
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

    public void setDropLetters(String dropLetters) {
        this.dropLetters = dropLetters == null ? "" : dropLetters.toUpperCase();
    }

    public String getDropLetters() {
        return dropLetters;
    }

    public void setPillarType(PillarType pillarType) {
        this.pillarType = pillarType == null ? PillarType.GENERIC : pillarType;
        this.entityData.set(DATA_PILLAR_TYPE, this.pillarType.name());
    }

    public PillarType getPillarType() {
        if (level().isClientSide()) {
            return PillarType.byName(this.entityData.get(DATA_PILLAR_TYPE));
        }
        return pillarType;
    }

    public boolean isCustomInvulnerable() {
        return tickCount < invulEnd;
    }

    public boolean isLinkBroken() {
        return tickCount < breakUntil;
    }

    public int getBreakTicksLeft() {
        return Math.max(0, breakUntil - tickCount);
    }

    public void applyBreak(int durationTicks) {
        breakUntil = Math.max(breakUntil, tickCount + Math.max(0, durationTicks));
        invulEnd = 0;
        outputWindowEnd = 0;
    }

    public int getOutputWindowTicksLeft() {
        return Math.max(0, outputWindowEnd - tickCount);
    }

    public int getCustomInvulTicksLeft() {
        return Math.max(0, invulEnd - tickCount);
    }

    private void spawnDropLetters(ServerLevel level) {
        for (int i = 0; i < dropLetters.length(); i++) {
            char letter = dropLetters.charAt(i);
            if (letter < 'A' || letter > 'Z') {
                continue;
            }

            double angle = Math.PI * 2.0d * i / Math.max(1, dropLetters.length());
            CthulhuGraphemeEntity grapheme = new CthulhuGraphemeEntity(ModEntites.CTHULHU_GRAPHEME.get(), level);
            grapheme.setLetter(letter);
            grapheme.moveTo(
                    getX() + Mth.cos((float) angle) * 0.8d,
                    getY() + 1.0d,
                    getZ() + Mth.sin((float) angle) * 0.8d,
                    random.nextFloat() * 360.0f,
                    0.0f
            );
            level.addFreshEntity(grapheme);
        }
    }

    private void runPillarMechanics() {
        if (isLinkBroken()) {
            return;
        }

        switch (pillarType) {
            case FAMINE -> runFamine();
            case EROSION -> runErosion();
            case ZERO, DESPAIR, GENERIC -> {
            }
        }
    }

    private void runFamine() {
        if (tickCount % 20 == 0) {
            for (ServerPlayer player : fightPlayers()) {
                player.causeFoodExhaustion(0.12f);
            }
        }
        if (tickCount % 60 == 0) {
            fireLawMissile();
        }
        if (tickCount % 160 == 0) {
            spawnFamineMinion();
        }
    }

    private void runErosion() {
        if (tickCount % 300 != 0) {
            return;
        }

        for (ServerPlayer player : fightPlayers()) {
            HealthDrainAPI.applyVitalityDrain(player, CthulhuConfig.VITALITY_DRAIN_PERCENT);
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal("EROSION: maximum vitality reduced."));
        }
    }

    private void fireLawMissile() {
        List<ServerPlayer> players = fightPlayers();
        if (players.isEmpty()) {
            return;
        }

        ServerPlayer target = players.stream()
                .min(Comparator.comparingDouble(player -> player.distanceToSqr(this)))
                .orElse(null);
        if (target == null || target.distanceToSqr(this) > 32.0d * 32.0d) {
            return;
        }

        if (CthulhuWordEffects.consumeShield(target)) {
            return;
        }
        target.hurt(damageSources().mobAttack(this), 5.0f);
        target.sendSystemMessage(net.minecraft.network.chat.Component.literal("FAMINE: law missile hit."));
    }

    private void spawnFamineMinion() {
        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Mob minion = random.nextBoolean()
                ? new Zombie(EntityType.ZOMBIE, serverLevel)
                : new Spider(EntityType.SPIDER, serverLevel);
        double angle = random.nextDouble() * Math.PI * 2.0d;
        minion.moveTo(
                getX() + Math.cos(angle) * 3.0d,
                getY(),
                getZ() + Math.sin(angle) * 3.0d,
                random.nextFloat() * 360.0f,
                0.0f
        );
        minion.setPersistenceRequired();
        joinBossTeam(minion);
        serverLevel.addFreshEntity(minion);
        CthulhuMinionRenderHelper.setRenderMode(minion, random.nextBoolean() ? CthulhuRenderMode.NOISE : CthulhuRenderMode.WIREFRAME);
    }

    private List<ServerPlayer> fightPlayers() {
        if (!(level() instanceof ServerLevel serverLevel)) {
            return List.of();
        }

        BossFightInstance instance = BossFightManager.get().getActiveFight(serverLevel);
        if (instance == null) {
            return List.of();
        }

        return serverLevel.players().stream()
                .filter(player -> instance.isParticipant(player.getUUID()))
                .filter(player -> !instance.isBanished(player.getUUID()))
                .toList();
    }

    private int calculateInvulTicks(DamageSource source) {
        if (isLinkBroken()) {
            return 0;
        }
        if (pillarType != PillarType.ZERO || source.getEntity() == null) {
            return CthulhuConfig.PILLAR_INVUL_TICKS;
        }

        double distance = source.getEntity().distanceTo(this);
        double closeness = Mth.clamp(1.0d - distance / 12.0d, 0.0d, 1.0d);
        return CthulhuConfig.PILLAR_INVUL_TICKS + (int) Math.round(closeness * 30.0d);
    }

    private float calculateDamage(DamageSource source, float amount) {
        if (pillarType != PillarType.ZERO || source.getEntity() == null) {
            return amount;
        }

        double distance = source.getEntity().distanceTo(this);
        float multiplier = Mth.clamp((float) (distance / 12.0d), 0.25f, 1.0f);
        return amount * multiplier;
    }

    private boolean isFarPhysicalHit(DamageSource source) {
        Entity attacker = source.getEntity();
        return attacker != null && attacker.distanceToSqr(this) > 6.0d * 6.0d && !source.isIndirect();
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
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(TAG_OUTPUT_WINDOW_END, outputWindowEnd);
        tag.putInt(TAG_INVUL_END, invulEnd);
        tag.putInt(TAG_BREAK_UNTIL, breakUntil);
        tag.putInt(TAG_PENDING_INVUL_TICKS, pendingInvulTicks);
        tag.putString(TAG_DROP_LETTERS, dropLetters);
        tag.putString(TAG_PILLAR_TYPE, pillarType.name());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        outputWindowEnd = tag.getInt(TAG_OUTPUT_WINDOW_END);
        invulEnd = tag.getInt(TAG_INVUL_END);
        breakUntil = tag.getInt(TAG_BREAK_UNTIL);
        pendingInvulTicks = tag.contains(TAG_PENDING_INVUL_TICKS) ? tag.getInt(TAG_PENDING_INVUL_TICKS) : CthulhuConfig.PILLAR_INVUL_TICKS;
        dropLetters = tag.getString(TAG_DROP_LETTERS);
        setPillarType(PillarType.byName(tag.getString(TAG_PILLAR_TYPE)));
    }

    public enum PillarType {
        GENERIC,
        FAMINE,
        ZERO,
        DESPAIR,
        EROSION;

        public static PillarType byName(String name) {
            if (name == null || name.isBlank()) {
                return GENERIC;
            }
            try {
                return valueOf(name.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException exception) {
                return GENERIC;
            }
        }
    }
}
