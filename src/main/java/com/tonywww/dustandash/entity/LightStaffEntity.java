package com.tonywww.dustandash.entity;

import com.tonywww.dustandash.DustAndAshConfig;
import com.tonywww.dustandash.damage.DAADamageTypes;
import com.tonywww.dustandash.registry.DAAEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkHooks;

import java.util.UUID;

public final class LightStaffEntity extends Entity {
    private static final EntityDataAccessor<Float> START_Y = SynchedEntityData.defineId(
            LightStaffEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> IMPACT_Y = SynchedEntityData.defineId(
            LightStaffEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> FALL_TICKS = SynchedEntityData.defineId(
            LightStaffEntity.class, EntityDataSerializers.INT);
        private static final EntityDataAccessor<Boolean> IMPACTED = SynchedEntityData.defineId(
            LightStaffEntity.class, EntityDataSerializers.BOOLEAN);

    private UUID ownerUuid;
    private UUID primaryTargetUuid;
    private float attackDamage;
        private int impactTicks;

    public LightStaffEntity(EntityType<? extends LightStaffEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public LightStaffEntity(ServerLevel level, Player owner, LivingEntity primaryTarget) {
        this(DAAEntities.LIGHT_STAFF.get(), level);
        this.ownerUuid = owner.getUUID();
        this.primaryTargetUuid = primaryTarget.getUUID();
        this.attackDamage = (float) owner.getAttributeValue(Attributes.ATTACK_DAMAGE);

        float impactY = (float) primaryTarget.getY();
        float startY = impactY + DustAndAshConfig.CURIOS.lightStaffFallHeight.get().floatValue();
        this.entityData.set(START_Y, startY);
        this.entityData.set(IMPACT_Y, impactY);
        this.entityData.set(FALL_TICKS, DustAndAshConfig.CURIOS.lightStaffFallTicks.get());
        this.setPos(primaryTarget.getX(), startY, primaryTarget.getZ());
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(START_Y, 0f);
        this.entityData.define(IMPACT_Y, 0f);
        this.entityData.define(FALL_TICKS, 1);
        this.entityData.define(IMPACTED, false);
    }

    @Override
    public void tick() {
        super.tick();
        int fallTicks = Math.max(1, this.entityData.get(FALL_TICKS));
        boolean impacted = this.entityData.get(IMPACTED);
        float progress = impacted ? 1f : Mth.clamp((float) this.tickCount / fallTicks, 0f, 1f);
        double y = Mth.lerp(progress, this.entityData.get(START_Y), this.entityData.get(IMPACT_Y));
        this.setPos(this.getX(), y, this.getZ());

        if (this.level().isClientSide()) {
            return;
        }
        if (!impacted && this.tickCount >= fallTicks) {
            this.entityData.set(IMPACTED, true);
            this.impact((ServerLevel) this.level());
            if (DustAndAshConfig.CURIOS.lightStaffImpactLingerTicks.get() == 0) {
                this.discard();
            }
            return;
        }
        if (impacted
                && ++this.impactTicks >= DustAndAshConfig.CURIOS.lightStaffImpactLingerTicks.get()) {
            this.discard();
        }
    }

    private void impact(ServerLevel level) {
        if (this.ownerUuid == null) {
            return;
        }

        Player owner = level.getPlayerByUUID(this.ownerUuid);
        if (owner == null) {
            return;
        }

        DamageSource damageSource = DAADamageTypes.light(level, this, owner);
        float attackDamage = this.attackDamage > 0f
            ? this.attackDamage
            : (float) owner.getAttributeValue(Attributes.ATTACK_DAMAGE);
        Entity primaryTarget = this.primaryTargetUuid == null ? null : level.getEntity(this.primaryTargetUuid);
        if (primaryTarget instanceof LivingEntity livingTarget && canDamage(owner, livingTarget)) {
            hurtIgnoringInvulnerability(
                    livingTarget,
                    damageSource,
                    attackDamage * DustAndAshConfig.CURIOS.lightStaffDirectDamageMultiplier.get().floatValue());
        }

        double radius = DustAndAshConfig.CURIOS.lightStaffRadius.get();
        AABB area = new AABB(
                this.getX() - radius,
                this.getY() - radius,
                this.getZ() - radius,
                this.getX() + radius,
                this.getY() + radius,
                this.getZ() + radius);
        float areaDamage = attackDamage * DustAndAshConfig.CURIOS.lightStaffAreaDamageMultiplier.get().floatValue();
        for (LivingEntity target : level.getEntitiesOfClass(
                LivingEntity.class,
                area,
                target -> canDamage(owner, target)
                        && target.distanceToSqr(this.getX(), this.getY(), this.getZ()) <= radius * radius)) {
            hurtIgnoringInvulnerability(target, damageSource, areaDamage);
        }
    }

    private static boolean canDamage(Player owner, LivingEntity target) {
        return target != owner
                && target.isAlive()
                && !owner.isAlliedTo(target)
                && !target.isAlliedTo(owner);
    }

    private static void hurtIgnoringInvulnerability(
            LivingEntity target, DamageSource damageSource, float amount) {
        target.invulnerableTime = 0;
        target.hurt(damageSource, amount);
        target.invulnerableTime = 0;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("Owner")) {
            this.ownerUuid = tag.getUUID("Owner");
        }
        if (tag.hasUUID("PrimaryTarget")) {
            this.primaryTargetUuid = tag.getUUID("PrimaryTarget");
        }
        this.entityData.set(START_Y, tag.getFloat("StartY"));
        this.entityData.set(IMPACT_Y, tag.getFloat("ImpactY"));
        this.entityData.set(FALL_TICKS, Math.max(1, tag.getInt("FallTicks")));
        this.entityData.set(IMPACTED, tag.getBoolean("Impacted"));
        this.attackDamage = tag.getFloat("AttackDamage");
        this.impactTicks = Math.max(0, tag.getInt("ImpactTicks"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (this.ownerUuid != null) {
            tag.putUUID("Owner", this.ownerUuid);
        }
        if (this.primaryTargetUuid != null) {
            tag.putUUID("PrimaryTarget", this.primaryTargetUuid);
        }
        tag.putFloat("StartY", this.entityData.get(START_Y));
        tag.putFloat("ImpactY", this.entityData.get(IMPACT_Y));
        tag.putInt("FallTicks", this.entityData.get(FALL_TICKS));
        tag.putBoolean("Impacted", this.entityData.get(IMPACTED));
        tag.putFloat("AttackDamage", this.attackDamage);
        tag.putInt("ImpactTicks", this.impactTicks);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}