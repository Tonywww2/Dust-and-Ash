package com.tonywww.dustandash.platform;

import net.minecraft.resources.ResourceLocation;
import java.util.function.Supplier;
public record RegistryObject<T>(ResourceLocation getId, Supplier<T> supplier) implements Supplier<T> {
    @Override public T get() { return supplier.get(); }
    public ResourceLocation getId() { return getId; }
    public boolean isPresent() { return get() != null; }
}
