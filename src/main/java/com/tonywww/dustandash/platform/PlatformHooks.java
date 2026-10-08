package com.tonywww.dustandash.platform;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.RandomSource;
//? if forge {
import net.minecraftforge.server.ServerLifecycleHooks;
//?} else {
/*import net.neoforged.neoforge.server.ServerLifecycleHooks;
*///?}

public final class PlatformHooks {
    private static final ThreadLocal<HolderLookup.Provider> REGISTRIES = new ThreadLocal<>();
    private PlatformHooks() {}
    public static HolderLookup.Provider registries() {
        if (REGISTRIES.get() != null) return REGISTRIES.get();
        var server = ServerLifecycleHooks.getCurrentServer();
        return server == null ? RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY) : server.registryAccess();
    }
    public static void withRegistries(HolderLookup.Provider registries, Runnable work) {
        var previous = REGISTRIES.get();
        REGISTRIES.set(registries);
        try { work.run(); } finally { if (previous == null) REGISTRIES.remove(); else REGISTRIES.set(previous); }
    }
    public static boolean sameStack(ItemStack first, ItemStack second) {
//? if forge {
return ItemStack.isSameItemSameTags(first, second);
//?} else {
/*return ItemStack.isSameItemSameComponents(first, second);
*///?}
    }
    public static void damageItem(ItemStack stack, int damage, LivingEntity holder, InteractionHand hand) {
//? if forge {
stack.hurtAndBreak(damage, holder, entity -> entity.broadcastBreakEvent(hand));
//?} else {
/*stack.hurtAndBreak(damage, holder, hand == InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
*///?}
    }
    public static boolean damageConsumable(ItemStack stack, int damage, RandomSource random) {
//? if forge {
return stack.hurt(damage, random, null);
//?} else {
/*if (!stack.isDamageableItem()) return false;
var server = ServerLifecycleHooks.getCurrentServer();
if (server != null && damage > 0) damage = net.minecraft.world.item.enchantment.EnchantmentHelper.processDurabilityChange(server.overworld(), stack, damage);
stack.setDamageValue(stack.getDamageValue() + damage);
return stack.getDamageValue() >= stack.getMaxDamage();
*///?}
    }
}
