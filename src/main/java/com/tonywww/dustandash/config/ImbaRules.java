package com.tonywww.dustandash.config;

import com.tonywww.dustandash.DustAndAshConfig;

public final class ImbaRules {
    private static final int NORMAL_WHITE_LIGHTNING_MAX_CHARGE = 16;
    private static final int IMBA_WHITE_LIGHTNING_MAX_CHARGE = 32;
    private static final int NORMAL_WHITE_LIGHTNING_MAX_ADVANCED_CHARGE = 10;
    private static final int IMBA_WHITE_LIGHTNING_MAX_ADVANCED_CHARGE = 20;

    private ImbaRules() {
    }

    public static boolean enabled() {
        return DustAndAshConfig.IMBA_MODE.get();
    }

    public static double galeDamageOffset() {
        return galeDamageOffset(enabled());
    }

    public static double galeDamageOffset(boolean imbaEnabled) {
        return imbaEnabled ? 5d : 1d;
    }

    public static double galeDamageMultiplier() {
        return galeDamageMultiplier(enabled());
    }

    public static double galeDamageMultiplier(boolean imbaEnabled) {
        return imbaEnabled ? 2.5d : DustAndAshConfig.WEAPONS.galeOtaijutsuDamageRate.get();
    }

    public static int galeCooldownTicks() {
        return galeCooldownTicks(enabled());
    }

    public static int galeCooldownTicks(boolean imbaEnabled) {
        return imbaEnabled ? 15 : 40;
    }

    public static int sunburnFireSeconds() {
        return sunburnFireSeconds(enabled());
    }

    public static int sunburnFireSeconds(boolean imbaEnabled) {
        return imbaEnabled ? 10 : 4;
    }

    public static int sunburnFireResistanceTicks() {
        return sunburnFireResistanceTicks(enabled());
    }

    public static int sunburnFireResistanceTicks(boolean imbaEnabled) {
        return imbaEnabled ? 600 : 200;
    }

    public static float sunburnExplosionPower() {
        return sunburnExplosionPower(enabled());
    }

    public static float sunburnExplosionPower(boolean imbaEnabled) {
        return imbaEnabled ? 5f : 2.25f;
    }

    public static int sunburnCooldownTicks() {
        return sunburnCooldownTicks(enabled());
    }

    public static int sunburnCooldownTicks(boolean imbaEnabled) {
        return imbaEnabled ? 5 : 35;
    }

    public static int rottenBladeRememberedTargets() {
        return rottenBladeRememberedTargets(enabled());
    }

    public static int rottenBladeRememberedTargets(boolean imbaEnabled) {
        return imbaEnabled ? 9 : 6;
    }

    public static float rottenBladeRecoilDamage() {
        return rottenBladeRecoilDamage(enabled());
    }

    public static float rottenBladeRecoilDamage(boolean imbaEnabled) {
        return imbaEnabled ? 0f : 3f;
    }

    public static int rottenBladeAreaCooldownTicks() {
        return rottenBladeAreaCooldownTicks(enabled());
    }

    public static int rottenBladeAreaCooldownTicks(boolean imbaEnabled) {
        return imbaEnabled ? 0 : 200;
    }

    public static int rottenBladeReleaseCooldownTicks() {
        return rottenBladeReleaseCooldownTicks(enabled());
    }

    public static int rottenBladeReleaseCooldownTicks(boolean imbaEnabled) {
        return imbaEnabled ? 0 : 60;
    }

    public static boolean rottenBladeAffectsWielder() {
        return !enabled();
    }

    public static int whiteLightningMaxCharge() {
        return whiteLightningMaxCharge(enabled());
    }

    public static int whiteLightningMaxCharge(boolean imbaEnabled) {
        return imbaEnabled ? IMBA_WHITE_LIGHTNING_MAX_CHARGE : NORMAL_WHITE_LIGHTNING_MAX_CHARGE;
    }

    public static int whiteLightningMaxAdvancedCharge() {
        return whiteLightningMaxAdvancedCharge(enabled());
    }

    public static int whiteLightningMaxAdvancedCharge(boolean imbaEnabled) {
        return imbaEnabled
                ? IMBA_WHITE_LIGHTNING_MAX_ADVANCED_CHARGE
                : NORMAL_WHITE_LIGHTNING_MAX_ADVANCED_CHARGE;
    }

    public static int whiteLightningHitsPerChargeGain() {
        return whiteLightningHitsPerChargeGain(enabled());
    }

    public static int whiteLightningHitsPerChargeGain(boolean imbaEnabled) {
        return imbaEnabled ? 2 : 3;
    }

    public static int whiteLightningChargeGain() {
        return 2;
    }

    public static int whiteLightningConversionCost() {
        return whiteLightningConversionCost(enabled());
    }

    public static int whiteLightningConversionCost(boolean imbaEnabled) {
        return imbaEnabled ? 4 : 8;
    }

    public static int whiteLightningConversionGain() {
        return 10;
    }

    public static boolean whiteLightningUsesMaximumHealth() {
        return enabled();
    }

    public static int lordOfBloodUseDurationTicks() {
        return lordOfBloodUseDurationTicks(enabled());
    }

    public static int lordOfBloodUseDurationTicks(boolean imbaEnabled) {
        return imbaEnabled ? 15 : 30;
    }

    public static boolean lordOfBloodUsesThirdStage() {
        return enabled();
    }

    public static boolean judgementReflectsDamage() {
        return enabled();
    }

    public static float judgementReflectionMultiplier() {
        return 1f;
    }

    public static boolean lightHaloIgnoresCooldown() {
        return enabled();
    }

    public static int lightHaloCooldownTicks(boolean imbaEnabled) {
        return imbaEnabled ? 0 : DustAndAshConfig.CURIOS.lightHaloCooldownTicks.get();
    }

    public static boolean darkHaloBlocksSourcelessDamage() {
        return enabled();
    }

    public static float darkHaloMarkedAttackerDamageMultiplier() {
        return darkHaloMarkedAttackerDamageMultiplier(enabled());
    }

    public static float darkHaloMarkedAttackerDamageMultiplier(boolean imbaEnabled) {
        return imbaEnabled ? 0.5f : 1f;
    }

    public static boolean voidRingGrantsResistance() {
        return enabled();
    }

    public static int voidRingResistanceDurationTicks() {
        return 1200;
    }

    public static int voidRingResistanceAmplifier() {
        return 4;
    }
}