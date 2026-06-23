package com.tonywww.dustandash.cthulhu.fight;

import com.tonywww.dustandash.cthulhu.config.CthulhuConfig;
import com.tonywww.dustandash.cthulhu.data.BossFightSavedData;
import com.tonywww.dustandash.cthulhu.entity.CthulhuBossPhase1Entity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuGraphemeEntity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuLawFieldEntity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuPillarEntity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuStormGolemEntity;
import com.tonywww.dustandash.cthulhu.client.CthulhuRenderMode;
import com.tonywww.dustandash.cthulhu.network.GraphemeUpdatePacket;
import com.tonywww.dustandash.cthulhu.api.HealthDrainAPI;
import com.tonywww.dustandash.cthulhu.api.ShaderHelper;
import com.tonywww.dustandash.cthulhu.api.UIHintAPI;
import com.tonywww.dustandash.network.PacketHandler;
import com.tonywww.dustandash.registeries.ModEntites;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BossFightInstance {

    private final ServerLevel level;
    private final BlockPos corePos;
    private final Set<UUID> participants = new LinkedHashSet<>();
    private final Set<UUID> banishedPlayers = new HashSet<>();
    private final Set<UUID> spawnedEntities = new HashSet<>();
    private final Map<UUID, Integer> deathCounts = new HashMap<>();
    private final Map<UUID, Map<Character, Integer>> letterInventories = new HashMap<>();
    private final Map<UUID, Integer> typingCooldownUntil = new HashMap<>();
    private final Set<UUID> finalTruthSubmitted = new HashSet<>();
    private FightPhase phase = FightPhase.PHASE_1;
    private boolean active = true;
    private float bossHealth = 1.0f;
    private int age;
    private int finalTruthDeadlineAge;

    public BossFightInstance(ServerLevel level, BlockPos corePos) {
        this.level = level;
        this.corePos = corePos.immutable();
    }

    public static BossFightInstance restore(ServerLevel level, BossFightSavedData.FightRecord record) {
        BossFightInstance instance = new BossFightInstance(level, record.corePos);
        instance.phase = record.phase;
        instance.active = record.active;
        instance.bossHealth = record.bossHealth;
        instance.participants.addAll(record.participants);
        instance.banishedPlayers.addAll(record.banishedPlayers);
        instance.spawnedEntities.addAll(record.spawnedEntities);
        instance.deathCounts.putAll(record.deathCounts);
        record.letterInventories.forEach((uuid, letters) -> instance.letterInventories.put(uuid, new HashMap<>(letters)));
        return instance;
    }

    public void tick() {
        if (!active) {
            return;
        }

        age++;
        for (ServerPlayer player : level.players()) {
            if (!isBanished(player.getUUID())) {
                addParticipant(player);
            }
        }

        if (phase == FightPhase.PHASE_1) {
            ensurePhase1Boss();
        }
        if (phase == FightPhase.PHASE_2 && !spawnedEntities.isEmpty() && !hasAliveTrackedPillar()) {
            completePhase2();
        }
        if (phase == FightPhase.PHASE_3) {
            ensurePhase3Boss();
        }
        if (phase == FightPhase.FINAL_TRUTH) {
            tickFinalTruth();
        }
        if (phase == FightPhase.PHASE_1 && age % 100 == 0) {
            spawnPhase1LawField();
        }
    }

    public void addParticipant(ServerPlayer player) {
        UUID uuid = player.getUUID();
        if (isBanished(uuid)) {
            return;
        }

        boolean added = participants.add(uuid);
        letterInventories.computeIfAbsent(uuid, ignored -> new HashMap<>());
        deathCounts.putIfAbsent(uuid, 0);

        if (added) {
            player.sendSystemMessage(Component.literal("Azathoth fight joined: " + phase.name()));
        }
        syncLetters(player);
        ShaderHelper.applyPhaseShader(player, phase);
    }

    public void removeParticipant(UUID playerId) {
        participants.remove(playerId);
    }

    public boolean isParticipant(UUID playerId) {
        return participants.contains(playerId);
    }

    public int recordDeath(ServerPlayer player) {
        UUID uuid = player.getUUID();
        addParticipant(player);
        int deaths = deathCounts.getOrDefault(uuid, 0) + 1;
        deathCounts.put(uuid, deaths);
        if (deaths >= CthulhuConfig.MAX_DEATHS_BEFORE_BANISH) {
            banishedPlayers.add(uuid);
            participants.remove(uuid);
            player.sendSystemMessage(Component.literal("You have been banished from this dimension."));
        }
        return deaths;
    }

    public boolean shouldBanish(UUID playerId) {
        return banishedPlayers.contains(playerId);
    }

    public boolean isBanished(UUID playerId) {
        return banishedPlayers.contains(playerId);
    }

    public void giveLetter(ServerPlayer player, char letter) {
        addParticipant(player);
        char normalized = Character.toUpperCase(letter);
        if (normalized < 'A' || normalized > 'Z') {
            return;
        }

        Map<Character, Integer> letters = letterInventories.computeIfAbsent(player.getUUID(), ignored -> new HashMap<>());
        letters.put(normalized, letters.getOrDefault(normalized, 0) + 1);
        player.sendSystemMessage(Component.literal("Grapheme acquired: " + normalized));
        syncLetters(player);
    }

    public boolean canSpell(ServerPlayer player, String word) {
        Map<Character, Integer> available = new HashMap<>(letterInventories.getOrDefault(player.getUUID(), Collections.emptyMap()));
        String normalized = word.toUpperCase(Locale.ROOT);
        for (int i = 0; i < normalized.length(); i++) {
            char c = normalized.charAt(i);
            if (c == ' ') {
                continue;
            }
            if (c < 'A' || c > 'Z') {
                return false;
            }
            int count = available.getOrDefault(c, 0);
            if (count <= 0) {
                return false;
            }
            available.put(c, count - 1);
        }
        return true;
    }

    public boolean consumeWord(ServerPlayer player, String word) {
        if (!canSpell(player, word)) {
            return false;
        }

        Map<Character, Integer> letters = letterInventories.computeIfAbsent(player.getUUID(), ignored -> new HashMap<>());
        String normalized = word.toUpperCase(Locale.ROOT);
        for (int i = 0; i < normalized.length(); i++) {
            char c = normalized.charAt(i);
            if (c == ' ') {
                continue;
            }
            int remaining = letters.getOrDefault(c, 0) - 1;
            if (remaining <= 0) {
                letters.remove(c);
            } else {
                letters.put(c, remaining);
            }
        }
        syncLetters(player);
        return true;
    }

    public boolean isTypingReady(ServerPlayer player) {
        return getTypingCooldownLeft(player) <= 0;
    }

    public int getTypingCooldownLeft(ServerPlayer player) {
        int until = typingCooldownUntil.getOrDefault(player.getUUID(), 0);
        return Math.max(0, until - level.getServer().getTickCount());
    }

    public void markTypingCooldown(ServerPlayer player) {
        typingCooldownUntil.put(player.getUUID(), level.getServer().getTickCount() + CthulhuConfig.GLOBAL_TYPE_COOLDOWN_TICKS);
    }

    public void clearLetters(ServerPlayer player) {
        letterInventories.remove(player.getUUID());
        syncLetters(player);
    }

    public void dropLetters(ServerPlayer player) {
        Map<Character, Integer> letters = letterInventories.get(player.getUUID());
        if (letters == null || letters.isEmpty()) {
            return;
        }

        List<Character> droppedLetters = new ArrayList<>();
        for (Map.Entry<Character, Integer> entry : letters.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                droppedLetters.add(entry.getKey());
            }
        }

        letters.clear();
        syncLetters(player);

        ServerLevel dropLevel = (ServerLevel) player.level();
        for (int index = 0; index < droppedLetters.size(); index++) {
            double angle = Math.PI * 2.0d * index / Math.max(1, droppedLetters.size());
            double radius = 1.2d + (index % 4) * 0.3d;
            CthulhuGraphemeEntity grapheme = new CthulhuGraphemeEntity(ModEntites.CTHULHU_GRAPHEME.get(), dropLevel);
            grapheme.setLetter(droppedLetters.get(index));
            grapheme.setPickupDelay(60);
            grapheme.moveTo(
                    player.getX() + Math.cos(angle) * radius,
                    player.getY() + 0.5d,
                    player.getZ() + Math.sin(angle) * radius,
                    dropLevel.random.nextFloat() * 360.0f,
                    0.0f
            );
            dropLevel.addFreshEntity(grapheme);
        }
    }

    public void syncLetters(ServerPlayer player) {
        Map<Character, Integer> letters = letterInventories.getOrDefault(player.getUUID(), Collections.emptyMap());
        PacketHandler.sendToPlayer(player, new GraphemeUpdatePacket(letters));
    }

    public void setPhase(FightPhase phase) {
        if (this.phase == phase) {
            return;
        }

        this.phase = phase;
        broadcast(Component.literal("Azathoth phase changed: " + phase.name()));
        UIHintAPI.broadcastHint(this, Component.literal(phaseTitle(phase)), 80);
        ShaderHelper.broadcastPhaseShader(this, phase);
        if (phase == FightPhase.PHASE_3) {
            for (ServerPlayer player : level.players()) {
                if (isParticipant(player.getUUID())) {
                    for (int i = 0; i < CthulhuConfig.PHASE3_FALLBACK_LETTERS.length(); i++) {
                        giveLetter(player, CthulhuConfig.PHASE3_FALLBACK_LETTERS.charAt(i));
                    }
                }
            }
        }
        if (phase == FightPhase.PHASE_2) {
            spawnPhase2Pillars();
        }
        if (phase == FightPhase.FINAL_TRUTH) {
            finalTruthSubmitted.clear();
            finalTruthDeadlineAge = age + 200;
            broadcast(Component.literal("Final Truth begins: REALITY EXISTS."));
        }
    }

    public boolean submitFinalTruth(ServerPlayer player) {
        if (phase != FightPhase.FINAL_TRUTH || !participants.contains(player.getUUID()) || isBanished(player.getUUID())) {
            return false;
        }

        finalTruthSubmitted.add(player.getUUID());
        int required = activeParticipantCount();
        broadcast(Component.literal("REALITY EXISTS: " + finalTruthSubmitted.size() + "/" + required));
        if (finalTruthSubmitted.size() >= required) {
            broadcast(Component.literal("Reality remembers its shape."));
            return true;
        }
        return false;
    }

    public void terminate() {
        active = false;
        phase = FightPhase.TERMINATED;
        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
            if (participants.contains(player.getUUID())) {
                letterInventories.remove(player.getUUID());
                syncLetters(player);
                ShaderHelper.clearShaders(player);
                HealthDrainAPI.clearVitalityDrain(player);
            }
        }
        cleanupFightEntities();
        broadcast(Component.literal("Azathoth fight terminated."));
    }

    public void broadcast(Component component) {
        for (ServerPlayer player : level.players()) {
            if (participants.contains(player.getUUID())) {
                player.sendSystemMessage(component);
            }
        }
    }

    public ServerLevel level() {
        return level;
    }

    public ResourceKey<Level> dimension() {
        return level.dimension();
    }

    public BlockPos corePos() {
        return corePos;
    }

    public FightPhase phase() {
        return phase;
    }

    public boolean active() {
        return active;
    }

    public float bossHealth() {
        return bossHealth;
    }

    public int age() {
        return age;
    }

    public Set<UUID> participants() {
        return Collections.unmodifiableSet(participants);
    }

    public Set<UUID> banishedPlayers() {
        return Collections.unmodifiableSet(banishedPlayers);
    }

    public Set<UUID> spawnedEntities() {
        return Collections.unmodifiableSet(spawnedEntities);
    }

    public Map<UUID, Integer> deathCounts() {
        return Collections.unmodifiableMap(deathCounts);
    }

    public Map<UUID, Map<Character, Integer>> letterInventories() {
        return Collections.unmodifiableMap(letterInventories);
    }

    public Map<Character, Integer> letterInventory(UUID playerId) {
        return Collections.unmodifiableMap(letterInventories.getOrDefault(playerId, Collections.emptyMap()));
    }

    private void spawnPhase2Pillars() {
        if (hasAliveTrackedPillar()) {
            return;
        }

        spawnedEntities.clear();
        spawnPillar(corePos.north(16), "AMIN", CthulhuPillarEntity.PillarType.FAMINE);
        spawnPillar(corePos.east(16), "RLTY", CthulhuPillarEntity.PillarType.ZERO);
        spawnPillar(corePos.south(16), "EXIS", CthulhuPillarEntity.PillarType.DESPAIR);
        spawnPillar(corePos.west(16), "DLTE", CthulhuPillarEntity.PillarType.EROSION);
    }

    private void ensurePhase1Boss() {
        if (hasAlivePhase1Boss()) {
            return;
        }

        CthulhuBossPhase1Entity boss = new CthulhuBossPhase1Entity(ModEntites.CTHULHU_BOSS_PHASE1.get(), level);
        boss.moveTo(corePos.getX() + 0.5d, corePos.getY() + 5.0d, corePos.getZ() + 0.5d, 0.0f, 0.0f);
        joinBossTeam(boss);
        level.addFreshEntity(boss);
        broadcast(Component.literal("Azathoth, the Idiot Side, has manifested."));
    }

    private void ensurePhase3Boss() {
        if (hasAlivePhase3Boss()) {
            return;
        }

        CthulhuStormGolemEntity stormGolem = new CthulhuStormGolemEntity(ModEntites.CTHULHU_STORM_GOLEM.get(), level);
        stormGolem.moveTo(corePos.getX() + 0.5d, corePos.getY() + 7.0d, corePos.getZ() + 0.5d, 0.0f, 0.0f);
        joinBossTeam(stormGolem);
        level.addFreshEntity(stormGolem);
        broadcast(Component.literal("The text storm enters the Grid."));
    }

    private void spawnPhase1LawField() {
        if (level.getEntitiesOfClass(CthulhuLawFieldEntity.class, new net.minecraft.world.phys.AABB(corePos).inflate(96.0d)).size() >= 6) {
            return;
        }

        List<ServerPlayer> availablePlayers = level.players().stream()
                .filter(player -> participants.contains(player.getUUID()))
                .toList();
        if (availablePlayers.isEmpty()) {
            return;
        }

        ServerPlayer target = availablePlayers.get(level.random.nextInt(availablePlayers.size()));
        CthulhuLawFieldEntity.LawFieldType[] types = CthulhuLawFieldEntity.LawFieldType.values();
        String letters = "VOIDVITAL";
        CthulhuLawFieldEntity field = new CthulhuLawFieldEntity(ModEntites.CTHULHU_LAW_FIELD.get(), level);
        field.setFieldType(types[level.random.nextInt(types.length)]);
        field.setDropLetter(letters.charAt(level.random.nextInt(letters.length())));
        double x = target.getX() + (level.random.nextDouble() - 0.5d) * 14.0d;
        double z = target.getZ() + (level.random.nextDouble() - 0.5d) * 14.0d;
        field.moveTo(x, target.getY(), z, level.random.nextFloat() * 360.0f, 0.0f);
        joinBossTeam(field);
        level.addFreshEntity(field);
    }

    private void completePhase2() {
        spawnedEntities.clear();
        for (ServerPlayer player : level.players()) {
            if (participants.contains(player.getUUID())) {
                HealthDrainAPI.clearVitalityDrain(player);
            }
        }
        broadcast(Component.literal("The four pillars collapse. Vitality returns."));
        setPhase(FightPhase.PHASE_3);
    }

    private void tickFinalTruth() {
        if (finalTruthDeadlineAge <= 0) {
            finalTruthDeadlineAge = age + 200;
        }

        if (age % 40 == 0) {
            spawnFinalMissileRainLetters();
        }

        int left = Math.max(0, finalTruthDeadlineAge - age);
        if (left % 40 == 0) {
            broadcast(Component.literal("Final Truth countdown: " + (left / 20) + "s"));
        }

        if (age >= finalTruthDeadlineAge) {
            for (ServerPlayer player : level.players()) {
                if (participants.contains(player.getUUID()) && !isBanished(player.getUUID())) {
                    player.hurt(player.damageSources().magic(), 8.0f);
                }
            }
            finalTruthSubmitted.clear();
            finalTruthDeadlineAge = age + 200;
            broadcast(Component.literal("The truth was incomplete. The countdown restarts."));
        }
    }

    private void spawnFinalMissileRainLetters() {
        String letters = "REALITYEXISTS";
        for (ServerPlayer player : level.players()) {
            if (!participants.contains(player.getUUID()) || isBanished(player.getUUID())) {
                continue;
            }

            player.hurt(player.damageSources().mobAttack(player), 3.0f);
            char letter = letters.charAt(level.random.nextInt(letters.length()));
            giveLetter(player, letter);
            player.sendSystemMessage(Component.literal("Final missile rain burns a grapheme into you: " + letter));
        }
    }

    private int activeParticipantCount() {
        int count = 0;
        for (ServerPlayer player : level.players()) {
            if (participants.contains(player.getUUID()) && !isBanished(player.getUUID())) {
                count++;
            }
        }
        return Math.max(1, count);
    }

    private static String phaseTitle(FightPhase phase) {
        return switch (phase) {
            case PHASE_1 -> "First Phase: Lawfall";
            case PHASE_2 -> "Second Phase: Four Pillars";
            case PHASE_3 -> "Third Phase: The Grid";
            case FINAL_TRUTH -> "Final Truth: REALITY EXISTS";
            case TERMINATED -> "Azathoth fight terminated";
        };
    }

    private boolean hasAliveTrackedPillar() {
        for (UUID uuid : spawnedEntities) {
            Entity entity = ((ServerLevel) level).getEntity(uuid);
            if (entity instanceof CthulhuPillarEntity && entity.isAlive()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasAlivePhase1Boss() {
        return !level.getEntitiesOfClass(
                CthulhuBossPhase1Entity.class,
                new net.minecraft.world.phys.AABB(corePos).inflate(96.0d),
                Entity::isAlive
        ).isEmpty();
    }

    private boolean hasAlivePhase3Boss() {
        return !level.getEntitiesOfClass(
                CthulhuStormGolemEntity.class,
                new net.minecraft.world.phys.AABB(corePos).inflate(128.0d),
                Entity::isAlive
        ).isEmpty();
    }

    private void spawnPillar(BlockPos pos, String letters, CthulhuPillarEntity.PillarType pillarType) {
        CthulhuPillarEntity pillar = new CthulhuPillarEntity(ModEntites.CTHULHU_PILLAR.get(), level);
        pillar.setDropLetters(letters);
        pillar.setPillarType(pillarType);
        pillar.moveTo(pos.getX() + 0.5d, pos.getY(), pos.getZ() + 0.5d, 0.0f, 0.0f);
        joinBossTeam(pillar);
        level.addFreshEntity(pillar);
        spawnedEntities.add(pillar.getUUID());
    }

    private void joinBossTeam(Entity entity) {
        Scoreboard scoreboard = level.getScoreboard();
        PlayerTeam team = scoreboard.getPlayerTeam("cthulhu_boss");
        if (team == null) {
            team = scoreboard.addPlayerTeam("cthulhu_boss");
        }
        team.setAllowFriendlyFire(false);
        team.setSeeFriendlyInvisibles(true);
        scoreboard.addPlayerToTeam(entity.getStringUUID(), team);
    }

    private void cleanupFightEntities() {
        Scoreboard scoreboard = level.getScoreboard();
        PlayerTeam team = scoreboard.getPlayerTeam("cthulhu_boss");
        List<Entity> toRemove = new ArrayList<>();
        for (Entity entity : level.getAllEntities()) {
            if (isFightOwnedEntity(entity, team)) {
                toRemove.add(entity);
            }
        }

        for (Entity entity : toRemove) {
            entity.discard();
            if (team != null) {
                scoreboard.removePlayerFromTeam(entity.getStringUUID(), team);
            }
        }
        spawnedEntities.clear();
        typingCooldownUntil.clear();
    }

    private static boolean isFightOwnedEntity(Entity entity, PlayerTeam team) {
        if (entity instanceof CthulhuBossPhase1Entity
                || entity instanceof CthulhuPillarEntity
                || entity instanceof CthulhuStormGolemEntity
                || entity instanceof CthulhuLawFieldEntity
                || entity instanceof CthulhuGraphemeEntity) {
            return true;
        }
        if (team != null && entity.getTeam() == team) {
            return true;
        }
        return entity instanceof net.minecraft.world.entity.LivingEntity livingEntity
                && CthulhuRenderMode.fromEntity(livingEntity).isPresent()
                && team != null
                && livingEntity.getTeam() == team;
    }
}
