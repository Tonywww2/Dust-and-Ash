package com.tonywww.dustandash.cthulhu.fight;

import com.tonywww.dustandash.cthulhu.config.CthulhuConfig;
import com.tonywww.dustandash.cthulhu.data.BossFightSavedData;
import com.tonywww.dustandash.cthulhu.api.HealthDrainAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class BossFightManager {

    private static final BossFightManager INSTANCE = new BossFightManager();

    private final Map<ResourceKey<Level>, BossFightInstance> activeFights = new HashMap<>();
    private boolean loaded;

    private BossFightManager() {
    }

    public static BossFightManager get() {
        return INSTANCE;
    }

    public BossFightInstance startFight(ServerLevel level, BlockPos corePos) {
        ensureLoaded(level.getServer());
        BossFightInstance existing = activeFights.get(level.dimension());
        if (existing != null && existing.active()) {
            return existing;
        }

        BossFightInstance instance = new BossFightInstance(level, corePos);
        activeFights.put(level.dimension(), instance);
        for (ServerPlayer player : level.players()) {
            instance.addParticipant(player);
        }
        save(level.getServer());
        return instance;
    }

    public void terminateFight(BossFightInstance instance) {
        instance.terminate();
        activeFights.remove(instance.dimension());
        save(instance.level().getServer());
    }

    public int terminateAll(MinecraftServer server) {
        ensureLoaded(server);
        List<BossFightInstance> instances = new ArrayList<>(activeFights.values());
        for (BossFightInstance instance : instances) {
            instance.terminate();
        }
        activeFights.clear();
        save(server);
        return instances.size();
    }

    public BossFightInstance getActiveFight(ServerLevel level) {
        ensureLoaded(level.getServer());
        BossFightInstance instance = activeFights.get(level.dimension());
        return instance != null && instance.active() ? instance : null;
    }

    public BossFightInstance getActiveFight(ResourceKey<Level> dimension) {
        BossFightInstance instance = activeFights.get(dimension);
        return instance != null && instance.active() ? instance : null;
    }

    public Collection<BossFightInstance> activeFights() {
        return activeFights.values();
    }

    public void tick(MinecraftServer server) {
        ensureLoaded(server);
        for (BossFightInstance instance : activeFights.values()) {
            instance.tick();
        }
        if (server.getTickCount() % 100 == 0) {
            save(server);
        }
    }

    public int recordDeath(ServerPlayer player) {
        BossFightInstance instance = getActiveFight((ServerLevel) player.level());
        if (instance == null || !instance.isParticipant(player.getUUID())) {
            instance = findParticipantFight(player);
        }
        if (instance == null || !instance.isParticipant(player.getUUID())) {
            return 0;
        }

        instance.dropLetters(player);
        int deaths = instance.recordDeath(player);
        if (instance.participants().isEmpty() && !instance.banishedPlayers().isEmpty()) {
            terminateFight(instance);
        }
        save(player.server);
        return deaths;
    }

    public void banishPlayer(ServerPlayer player, BossFightInstance instance) {
        HealthDrainAPI.clearVitalityDrain(player);
        ServerLevel target = getBanishTarget(player, instance);
        BlockPos pos = target.getSharedSpawnPos();
        ResourceKey<Level> respawnDimension = player.getRespawnDimension();
        BlockPos respawnPosition = player.getRespawnPosition();
        double targetY;
        if (respawnPosition != null && respawnDimension.equals(target.dimension())) {
            pos = respawnPosition;
            targetY = pos.getY() + 1.0d;
        } else if (target.dimension().equals(Level.NETHER)) {
            pos = new BlockPos(pos.getX(), CthulhuConfig.BANISH_NETHER_Y, pos.getZ());
            targetY = CthulhuConfig.BANISH_NETHER_Y;
        } else {
            targetY = pos.getY() + 1.0d;
        }

        player.teleportTo(target, pos.getX() + 0.5d, targetY, pos.getZ() + 0.5d, player.getYRot(), player.getXRot());
        player.sendSystemMessage(Component.literal("The dimension rejects your return."));
        save(player.server);
    }

    public boolean isBlockedFromDimension(ServerPlayer player, ResourceKey<Level> targetDimension) {
        for (BossFightInstance instance : activeFights.values()) {
            if (instance.dimension().equals(targetDimension) && instance.isBanished(player.getUUID())) {
                return true;
            }
        }
        return false;
    }

    public BossFightInstance findBanishSource(ServerPlayer player) {
        UUID uuid = player.getUUID();
        for (BossFightInstance instance : activeFights.values()) {
            if (instance.isBanished(uuid)) {
                return instance;
            }
        }
        return null;
    }

    public BossFightInstance findParticipantFight(ServerPlayer player) {
        UUID uuid = player.getUUID();
        for (BossFightInstance instance : activeFights.values()) {
            if (instance.isParticipant(uuid)) {
                return instance;
            }
        }
        return null;
    }

    public void clearClientLetters(ServerPlayer player) {
        com.tonywww.dustandash.network.PacketHandler.sendToPlayer(
                player,
                new com.tonywww.dustandash.cthulhu.network.GraphemeUpdatePacket(Map.of())
        );
    }

    private ServerLevel getBanishTarget(ServerPlayer player, BossFightInstance instance) {
        MinecraftServer server = player.server;
        ResourceKey<Level> targetDimension = player.getRespawnDimension();
        if (targetDimension.equals(instance.dimension())) {
            targetDimension = instance.dimension().equals(Level.OVERWORLD)
                    ? ResourceKey.create(Registries.DIMENSION, new ResourceLocation(CthulhuConfig.BANISH_ALT_DIM))
                    : Level.OVERWORLD;
        }

        ServerLevel target = server.getLevel(targetDimension);
        if (target == null) {
            target = server.overworld();
        }
        return target;
    }

    private void ensureLoaded(MinecraftServer server) {
        if (loaded) {
            return;
        }

        loaded = true;
        BossFightSavedData data = BossFightSavedData.get(server);
        for (BossFightSavedData.FightRecord record : data.getFightRecords()) {
            if (!record.active) {
                continue;
            }

            ResourceLocation dimensionId = ResourceLocation.tryParse(record.dimensionId);
            if (dimensionId == null) {
                continue;
            }

            ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, dimensionId);
            ServerLevel level = server.getLevel(dimension);
            if (level != null) {
                activeFights.put(dimension, BossFightInstance.restore(level, record));
            }
        }
    }

    public void save(MinecraftServer server) {
        if (server == null) {
            return;
        }
        BossFightSavedData.get(server).replaceFrom(activeFights.values());
    }
}
