package com.tonywww.dustandash.platform;

import java.util.function.Supplier;
public final class PlatformConfigSpec {
    private final Object nativeSpec;
    private PlatformConfigSpec(Object nativeSpec) { this.nativeSpec=nativeSpec; }
    public Object nativeSpec() { return nativeSpec; }
    public record IntValue(Supplier<Integer> supplier) { public Integer get() { return supplier.get(); } }
    public record DoubleValue(Supplier<Double> supplier) { public Double get() { return supplier.get(); } }
    public record BooleanValue(Supplier<Boolean> supplier) { public Boolean get() { return supplier.get(); } }
    public static final class Builder {
//? if forge {
private final net.minecraftforge.common.ForgeConfigSpec.Builder delegate = new net.minecraftforge.common.ForgeConfigSpec.Builder();
//?} else {
/*private final net.neoforged.neoforge.common.ModConfigSpec.Builder delegate = new net.neoforged.neoforge.common.ModConfigSpec.Builder();
*///?}

        public Builder comment(String... comments) { delegate.comment(comments); return this; }
        public Builder push(String path) { delegate.push(path); return this; }
        public Builder pop() { delegate.pop(); return this; }
        public Builder pop(int count) { delegate.pop(count); return this; }
        public BooleanValue define(String name, boolean value) { var setting=delegate.define(name,value); return new BooleanValue(setting::get); }
        public IntValue defineInRange(String name, int value, int min, int max) { var setting=delegate.defineInRange(name,value,min,max); return new IntValue(setting::get); }
        public DoubleValue defineInRange(String name, double value, double min, double max) { var setting=delegate.defineInRange(name,value,min,max); return new DoubleValue(setting::get); }
        public PlatformConfigSpec build() { return new PlatformConfigSpec(delegate.build()); }
    }
}
