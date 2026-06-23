package com.tonywww.dustandash.cthulhu.entity;

import com.tonywww.dustandash.registeries.ModEntites;
import com.tonywww.dustandash.cthulhu.grapheme.CthulhuWordEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Locale;

public class CthulhuLawFieldEntity extends Monster {

    private static final String TAG_FIELD_TYPE = "FieldType";
    private static final String TAG_DROP_LETTER = "DropLetter";

    private LawFieldType fieldType = LawFieldType.SILENCE;
    private char dropLetter = 'V';

    public CthulhuLawFieldEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.setNoAi(true);
        this.setNoGravity(true);
        this.setPersistenceRequired();
        this.xpReward = 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 24.0d)
                .add(Attributes.MOVEMENT_SPEED, 0.0d)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0d);
    }

    @Override
    public void tick() {
        super.tick();
        setDeltaMovement(Vec3.ZERO);

        if (level().isClientSide()) {
            return;
        }

        if (tickCount % 20 == 0) {
            applyFieldEffect();
        }
        if (tickCount > 20 * 20) {
            discard();
        }
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (level() instanceof ServerLevel serverLevel) {
            CthulhuGraphemeEntity grapheme = new CthulhuGraphemeEntity(ModEntites.CTHULHU_GRAPHEME.get(), serverLevel);
            grapheme.setLetter(dropLetter);
            grapheme.setPickupDelay(10);
            grapheme.moveTo(getX(), getY() + 0.5d, getZ(), random.nextFloat() * 360.0f, 0.0f);
            serverLevel.addFreshEntity(grapheme);
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public void setFieldType(LawFieldType fieldType) {
        this.fieldType = fieldType;
    }

    public LawFieldType getFieldType() {
        return fieldType;
    }

    public void setDropLetter(char dropLetter) {
        char normalized = Character.toUpperCase(dropLetter);
        this.dropLetter = normalized >= 'A' && normalized <= 'Z' ? normalized : 'V';
    }

    public char getDropLetter() {
        return dropLetter;
    }

    private void applyFieldEffect() {
        List<ServerPlayer> players = level().getEntitiesOfClass(
                ServerPlayer.class,
                new AABB(blockPosition()).inflate(8.0d)
        );
        for (ServerPlayer player : players) {
            if ((fieldType == LawFieldType.VOID || fieldType == LawFieldType.STASIS) && CthulhuWordEffects.hasVoidProtection(player)) {
                continue;
            }
            switch (fieldType) {
                case SILENCE -> applyCooldown(player);
                case EVAPORATION -> pushAway(player);
                case VOID -> player.hurt(damageSources().magic(), 2.0f);
                case STASIS -> {
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1));
                    player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 40, 1));
                }
            }
        }
    }

    private static void applyCooldown(ServerPlayer player) {
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty()) {
                player.getCooldowns().addCooldown(stack.getItem(), 20);
            }
        }
    }

    private void pushAway(ServerPlayer player) {
        Vec3 push = player.position().subtract(position());
        if (push.lengthSqr() < 0.001d) {
            push = new Vec3(0.0d, 0.2d, 0.0d);
        }
        player.push(push.normalize().x * 1.2d, 0.35d, push.normalize().z * 1.2d);
        player.hurtMarked = true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString(TAG_FIELD_TYPE, fieldType.name());
        tag.putString(TAG_DROP_LETTER, String.valueOf(dropLetter));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        try {
            fieldType = LawFieldType.valueOf(tag.getString(TAG_FIELD_TYPE).toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            fieldType = LawFieldType.SILENCE;
        }
        String letter = tag.getString(TAG_DROP_LETTER);
        setDropLetter(letter.isEmpty() ? 'V' : letter.charAt(0));
    }

    public enum LawFieldType {
        SILENCE,
        EVAPORATION,
        VOID,
        STASIS
    }
}
