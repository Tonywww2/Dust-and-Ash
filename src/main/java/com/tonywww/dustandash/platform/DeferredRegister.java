package com.tonywww.dustandash.platform;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import java.util.function.Supplier;
public final class DeferredRegister<T> {
//? if forge {
private final net.minecraftforge.registries.DeferredRegister<T> delegate;
//?} else {
/*private final net.neoforged.neoforge.registries.DeferredRegister<T> delegate;
*///?}

    private final String namespace;
    private DeferredRegister(ResourceKey<? extends Registry<T>> key, String namespace) {
        this.namespace = namespace;
//? if forge {
this.delegate = net.minecraftforge.registries.DeferredRegister.create(key, namespace);
//?} else {
/*this.delegate = net.neoforged.neoforge.registries.DeferredRegister.create(key, namespace);
*///?}
    }
    public static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> key, String namespace) {
        return new DeferredRegister<>(key, namespace);
    }
    public static <T> DeferredRegister<T> create(PlatformRegistries.RegistryHandle<T> registry, String namespace) {
        return create(registry.key(), namespace);
    }
    public <S extends T> RegistryObject<S> register(String name, Supplier<? extends S> factory) {
        var value = this.delegate.register(name, factory);
        return new RegistryObject<>(PlatformIdentifier.id(this.namespace, name), value::get);
    }
    public void register(PlatformEventBus bus) { this.delegate.register(bus.nativeBus()); }
}
