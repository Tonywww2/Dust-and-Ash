package com.tonywww.dustandash.platform;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
public final class PlatformOptional<T> {
    private final Supplier<T> supplier;
    private boolean valid = true;
    private boolean resolved;
    private T value;
    //? if forge {
    private net.minecraftforge.common.util.LazyOptional<T> nativeOptional;
    net.minecraftforge.common.util.LazyOptional<T> nativeOptional() {
        if (nativeOptional == null) {
            nativeOptional = resolve().isPresent() ? net.minecraftforge.common.util.LazyOptional.of(() -> value) : net.minecraftforge.common.util.LazyOptional.empty();
        }
        return nativeOptional;
    }
    //?}
    private PlatformOptional(Supplier<T> supplier) { this.supplier = supplier; }
    public static <T> PlatformOptional<T> of(Supplier<T> supplier) { return new PlatformOptional<>(supplier); }
    public static <T> PlatformOptional<T> empty() { return of(() -> null); }
    public Optional<T> resolve() {
        if (!valid) return Optional.empty();
        if (!resolved) { value = supplier.get(); resolved = true; }
        return Optional.ofNullable(value);
    }
    public void ifPresent(Consumer<? super T> action) { resolve().ifPresent(action); }
    public T orElse(T fallback) { return resolve().orElse(fallback); }
    public boolean isPresent() { return resolve().isPresent(); }
    public void invalidate() {
        valid = false;
        //? if forge {
        if (nativeOptional != null) nativeOptional.invalidate();
        //?}
    }
    @SuppressWarnings("unchecked") public <R> PlatformOptional<R> cast() { return (PlatformOptional<R>) this; }
}
