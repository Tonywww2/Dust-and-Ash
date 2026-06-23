package com.tonywww.dustandash.cthulhu.entity;

import com.tonywww.dustandash.cthulhu.api.GraphemeAPI;
import com.tonywww.dustandash.cthulhu.fight.BossFightInstance;
import com.tonywww.dustandash.cthulhu.fight.BossFightManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class CthulhuGraphemeEntity extends Entity {

    private static final String TAG_LETTER = "Letter";
    private static final String TAG_PICKUP_DELAY = "PickupDelay";
    private static final EntityDataAccessor<String> DATA_LETTER =
            SynchedEntityData.defineId(CthulhuGraphemeEntity.class, EntityDataSerializers.STRING);
    private int pickupDelay = 20;
    private boolean pickedUp;

    public CthulhuGraphemeEntity(EntityType<? extends CthulhuGraphemeEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = false;
        this.setNoGravity(true);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_LETTER, "A");
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) {
            return;
        }

        if (pickupDelay > 0) {
            pickupDelay--;
        }

        Player nearest = pickupDelay <= 0 ? level().getNearestPlayer(this, 1.2d) : null;
        if (!pickedUp && nearest instanceof ServerPlayer serverPlayer && serverPlayer.isAlive()) {
            BossFightInstance instance = BossFightManager.get().getActiveFight((net.minecraft.server.level.ServerLevel) serverPlayer.level());
            if (instance == null || !instance.isParticipant(serverPlayer.getUUID())) {
                return;
            }

            pickedUp = true;
            GraphemeAPI.giveLetter(serverPlayer, getLetter());
            discard();
            return;
        }

        if (tickCount > 20 * 60) {
            discard();
        }
    }

    public void setLetter(char letter) {
        char normalized = Character.toUpperCase(letter);
        if (normalized < 'A' || normalized > 'Z') {
            normalized = 'A';
        }
        this.entityData.set(DATA_LETTER, String.valueOf(normalized));
    }

    public char getLetter() {
        String letter = this.entityData.get(DATA_LETTER);
        return letter.isEmpty() ? 'A' : letter.charAt(0);
    }

    public void setPickupDelay(int pickupDelay) {
        this.pickupDelay = Math.max(0, pickupDelay);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        setLetter(tag.getString(TAG_LETTER).isEmpty() ? 'A' : tag.getString(TAG_LETTER).charAt(0));
        pickupDelay = tag.getInt(TAG_PICKUP_DELAY);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putString(TAG_LETTER, String.valueOf(getLetter()));
        tag.putInt(TAG_PICKUP_DELAY, pickupDelay);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
