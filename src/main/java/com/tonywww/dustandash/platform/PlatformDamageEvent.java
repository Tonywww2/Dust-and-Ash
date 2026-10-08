package com.tonywww.dustandash.platform;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Supplier;
import java.util.function.Consumer;
public record PlatformDamageEvent(LivingEntity getEntity, DamageSource getSource, Supplier<Float> amount, Consumer<Float> damage, Consumer<Boolean> canceled) {
    public float getAmount() { return amount.get(); }
    public void setAmount(float value) { damage.accept(value); }
    public void setCanceled(boolean value) { canceled.accept(value); }
}
