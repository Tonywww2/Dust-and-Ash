package com.tonywww.dustandash.platform;

//? if neoforge {
/*import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import java.util.Arrays;
import java.util.stream.Stream;

// Preserves Forge partial-NBT matching when item data moves into CUSTOM_DATA.
public final class PlatformNbtIngredient implements ICustomIngredient {
    public static final MapCodec<PlatformNbtIngredient> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
        Ingredient.CODEC.fieldOf("base").forGetter(value -> value.base),
        Codec.STRING.fieldOf("nbt").forGetter(value -> value.snbt)
    ).apply(builder, PlatformNbtIngredient::new));
    private static final net.neoforged.neoforge.registries.DeferredRegister<IngredientType<?>> TYPES = net.neoforged.neoforge.registries.DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, "dustandash");
    private static final java.util.function.Supplier<IngredientType<PlatformNbtIngredient>> TYPE = TYPES.register("custom_data", () -> new IngredientType<>(CODEC));
    private final Ingredient base;
    private final String snbt;
    private final CompoundTag pattern;
    public PlatformNbtIngredient(Ingredient base, String snbt) {
        this.base = base;
        this.snbt = snbt;
        try { this.pattern = TagParser.parseTag(snbt); }
        catch (com.mojang.brigadier.exceptions.CommandSyntaxException exception) { throw new IllegalArgumentException("Invalid ingredient NBT", exception); }
    }
    public static void register(PlatformEventBus bus) { TYPES.register(bus.nativeBus()); }
    @Override public boolean test(ItemStack stack) {
        return base.test(stack) && NbtUtils.compareNbt(pattern, PlatformItemData.read(stack), true);
    }
    @Override public Stream<ItemStack> getItems() {
        return Arrays.stream(base.getItems()).map(stack -> {
            ItemStack copy = stack.copy();
            PlatformItemData.write(copy, pattern.copy());
            return copy;
        });
    }
    @Override public boolean isSimple() { return false; }
    @Override public IngredientType<?> getType() { return TYPE.get(); }
}
*///?}
