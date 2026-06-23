package com.tonywww.dustandash.cthulhu.data;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.cthulhu.fight.BossFightInstance;
import com.tonywww.dustandash.cthulhu.fight.FightPhase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BossFightSavedData extends SavedData {

    private static final String DATA_NAME = DustAndAsh.MOD_ID + "_cthulhu_fights";
    private final List<FightRecord> fightRecords = new ArrayList<>();

    public static BossFightSavedData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(
                BossFightSavedData::load,
                BossFightSavedData::new,
                DATA_NAME
        );
    }

    public static BossFightSavedData load(CompoundTag tag) {
        BossFightSavedData data = new BossFightSavedData();
        ListTag fights = tag.getList("fights", Tag.TAG_COMPOUND);
        for (int i = 0; i < fights.size(); i++) {
            data.fightRecords.add(FightRecord.load(fights.getCompound(i)));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag fights = new ListTag();
        for (FightRecord record : fightRecords) {
            fights.add(record.save());
        }
        tag.put("fights", fights);
        return tag;
    }

    public List<FightRecord> getFightRecords() {
        return List.copyOf(fightRecords);
    }

    public void replaceFrom(Iterable<BossFightInstance> instances) {
        fightRecords.clear();
        for (BossFightInstance instance : instances) {
            if (instance.active()) {
                fightRecords.add(FightRecord.from(instance));
            }
        }
        setDirty();
    }

    public static class FightRecord {
        public boolean active;
        public String dimensionId;
        public BlockPos corePos;
        public FightPhase phase;
        public Set<UUID> participants = new HashSet<>();
        public Set<UUID> banishedPlayers = new HashSet<>();
        public Set<UUID> spawnedEntities = new HashSet<>();
        public Map<UUID, Integer> deathCounts = new HashMap<>();
        public Map<UUID, Map<Character, Integer>> letterInventories = new HashMap<>();
        public float bossHealth;

        public static FightRecord from(BossFightInstance instance) {
            FightRecord record = new FightRecord();
            record.active = instance.active();
            record.dimensionId = instance.dimension().location().toString();
            record.corePos = instance.corePos();
            record.phase = instance.phase();
            record.participants.addAll(instance.participants());
            record.banishedPlayers.addAll(instance.banishedPlayers());
            record.spawnedEntities.addAll(instance.spawnedEntities());
            record.deathCounts.putAll(instance.deathCounts());
            instance.letterInventories().forEach((uuid, letters) -> record.letterInventories.put(uuid, new HashMap<>(letters)));
            record.bossHealth = instance.bossHealth();
            return record;
        }

        public static FightRecord load(CompoundTag tag) {
            FightRecord record = new FightRecord();
            record.active = tag.getBoolean("active");
            record.dimensionId = tag.getString("dimensionId");
            record.corePos = BlockPos.of(tag.getLong("corePos"));
            record.phase = FightPhase.byName(tag.getString("phase"));
            record.bossHealth = tag.getFloat("bossHealth");

            ListTag participants = tag.getList("participants", Tag.TAG_STRING);
            for (int i = 0; i < participants.size(); i++) {
                record.participants.add(UUID.fromString(participants.getString(i)));
            }

            ListTag banished = tag.getList("banishedPlayers", Tag.TAG_STRING);
            for (int i = 0; i < banished.size(); i++) {
                record.banishedPlayers.add(UUID.fromString(banished.getString(i)));
            }

            ListTag entities = tag.getList("spawnedEntities", Tag.TAG_STRING);
            for (int i = 0; i < entities.size(); i++) {
                record.spawnedEntities.add(UUID.fromString(entities.getString(i)));
            }

            CompoundTag deaths = tag.getCompound("deathCounts");
            for (String key : deaths.getAllKeys()) {
                record.deathCounts.put(UUID.fromString(key), deaths.getInt(key));
            }

            CompoundTag inventories = tag.getCompound("letterInventories");
            for (String playerId : inventories.getAllKeys()) {
                CompoundTag lettersTag = inventories.getCompound(playerId);
                Map<Character, Integer> letters = new HashMap<>();
                for (String letter : lettersTag.getAllKeys()) {
                    if (!letter.isEmpty()) {
                        letters.put(letter.charAt(0), lettersTag.getInt(letter));
                    }
                }
                record.letterInventories.put(UUID.fromString(playerId), letters);
            }

            return record;
        }

        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("active", active);
            tag.putString("dimensionId", dimensionId);
            tag.putLong("corePos", corePos.asLong());
            tag.putString("phase", phase.name());
            tag.putFloat("bossHealth", bossHealth);

            ListTag participantList = new ListTag();
            for (UUID uuid : participants) {
                participantList.add(StringTag.valueOf(uuid.toString()));
            }
            tag.put("participants", participantList);

            ListTag banishedList = new ListTag();
            for (UUID uuid : banishedPlayers) {
                banishedList.add(StringTag.valueOf(uuid.toString()));
            }
            tag.put("banishedPlayers", banishedList);

            ListTag spawnedEntityList = new ListTag();
            for (UUID uuid : spawnedEntities) {
                spawnedEntityList.add(StringTag.valueOf(uuid.toString()));
            }
            tag.put("spawnedEntities", spawnedEntityList);

            CompoundTag deathTag = new CompoundTag();
            deathCounts.forEach((uuid, deaths) -> deathTag.putInt(uuid.toString(), deaths));
            tag.put("deathCounts", deathTag);

            CompoundTag inventories = new CompoundTag();
            letterInventories.forEach((uuid, letters) -> {
                CompoundTag lettersTag = new CompoundTag();
                letters.forEach((letter, count) -> lettersTag.putInt(String.valueOf(letter), count));
                inventories.put(uuid.toString(), lettersTag);
            });
            tag.put("letterInventories", inventories);

            return tag;
        }
    }
}
