package com.tonywww.dustandash.platform;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.RecipeSerializer;
public abstract class PlatformRecipeSerializer<R extends PlatformRecipe> implements RecipeSerializer<R> {
    public abstract R fromJson(ResourceLocation id, JsonObject json);
    public abstract R fromNetwork(ResourceLocation id, FriendlyByteBuf buffer);
    public abstract void toNetwork(FriendlyByteBuf buffer, R recipe);
//? if forge {

//?} else {
/*private final java.util.Map<R,JsonObject> sourceJson=java.util.Collections.synchronizedMap(new java.util.WeakHashMap<>());
private final com.mojang.serialization.MapCodec<R> codec=new com.mojang.serialization.MapCodec<>() {
    public <T> java.util.stream.Stream<T> keys(com.mojang.serialization.DynamicOps<T> ops) { return java.util.stream.Stream.empty(); }
    public <T> com.mojang.serialization.DataResult<R> decode(com.mojang.serialization.DynamicOps<T> ops, com.mojang.serialization.MapLike<T> input) {
        try {
            JsonObject json=new JsonObject();
            input.entries().forEach(pair -> json.add(ops.getStringValue(pair.getFirst()).getOrThrow(), ops.convertTo(com.mojang.serialization.JsonOps.INSTANCE,pair.getSecond())));
            var jsonOps = ops instanceof net.minecraft.resources.RegistryOps<?> registryOps ? registryOps.withParent(com.mojang.serialization.JsonOps.INSTANCE) : com.mojang.serialization.JsonOps.INSTANCE;
            R recipe=PlatformRecipeIo.withOps(jsonOps, () -> fromJson(PlatformIdentifier.id("dustandash","decoded"),json));
            sourceJson.put(recipe,json);
            return com.mojang.serialization.DataResult.success(recipe);
        } catch (RuntimeException exception) { return com.mojang.serialization.DataResult.error(()->exception.getMessage()==null ? exception.toString() : exception.getMessage()); }
    }
    public <T> com.mojang.serialization.RecordBuilder<T> encode(R recipe, com.mojang.serialization.DynamicOps<T> ops, com.mojang.serialization.RecordBuilder<T> builder) {
        JsonObject json=sourceJson.get(recipe);
        if (json == null) return builder.withErrorsFrom(com.mojang.serialization.DataResult.error(()->"Recipe has no JSON source: "+recipe.getId()));
        json.entrySet().forEach(entry -> builder.add(entry.getKey(),com.mojang.serialization.JsonOps.INSTANCE.convertTo(ops,entry.getValue())));
        return builder;
    }
};
@Override public com.mojang.serialization.MapCodec<R> codec() { return codec; }
@Override public net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf,R> streamCodec() {
    return net.minecraft.network.codec.StreamCodec.of((buffer,recipe)->toNetwork(buffer,recipe),buffer->fromNetwork(PlatformIdentifier.id("dustandash","synced"),buffer));
}
*///?}
}
