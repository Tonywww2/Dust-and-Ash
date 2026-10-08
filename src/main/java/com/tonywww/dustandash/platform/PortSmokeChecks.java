package com.tonywww.dustandash.platform;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.entity.FissionReactor.NeutronContainerUpdater;
import com.tonywww.dustandash.item.WhiteLightning;
import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAAItems;
import com.tonywww.dustandash.registry.DAARecipe;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

// This class is excluded from ordinary builds. Enable with -Pdustandash.smokeChecks.
//? if forge {
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
@net.minecraftforge.fml.common.Mod.EventBusSubscriber(modid = DustAndAsh.MOD_ID)
//?} else {
/*import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.bus.api.SubscribeEvent;
@net.neoforged.fml.common.EventBusSubscriber(modid = DustAndAsh.MOD_ID)
*///?}
public final class PortSmokeChecks {
    private PortSmokeChecks() {}
    @SubscribeEvent public static void onServerStarted(ServerStartedEvent event) throws Exception {
        MinecraftServer server = event.getServer();
        try {
            recipes(server, DAARecipe.INTEGRATE_TYPE.get(), 37);
            recipes(server, DAARecipe.MILLING_TYPE.get(), 37);
            recipes(server, DAARecipe.CENTRIFUGE_TYPE.get(), 25);
            recipes(server, DAARecipe.IONIZER_TYPE.get(), 24);
            itemData();
            machines(server);
            Files.writeString(Path.of("port-smoke-result.txt"), "PASS: 123 custom recipes and network round trips; item data; 8 machine capability/save-load round trips; reactor energy\n");
            DustAndAsh.getLogger().info("PORT_SMOKE_PASS: recipes, components, capabilities and persistence");
        } catch (Throwable failure) {
            Files.writeString(Path.of("port-smoke-result.txt"), "FAIL: " + failure);
            throw new IllegalStateException("Port smoke checks failed", failure);
        } finally {
            server.halt(false);
        }
    }
    @SuppressWarnings("unchecked")
    private static <R extends PlatformRecipe> void recipes(MinecraftServer server, RecipeType<R> type, int expected) {
        List<R> recipes = PlatformRecipes.all(server.getRecipeManager(), type);
        require(recipes.size() == expected, "Recipe count for " + type + ": " + recipes.size());
        var ids = new HashSet<>();
        for (R recipe : recipes) {
            require(ids.add(recipe.getId()), "Duplicate recipe ID: " + recipe.getId());
            PlatformRecipeSerializer<R> serializer = (PlatformRecipeSerializer<R>) recipe.getSerializer();
            //? if forge {
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            //?} else {
            /*net.minecraft.network.RegistryFriendlyByteBuf buffer = new net.minecraft.network.RegistryFriendlyByteBuf(Unpooled.buffer(), server.registryAccess());
            *///?}
            try {
                //? if forge {
                serializer.toNetwork(buffer, recipe);
                R decoded = serializer.fromNetwork(recipe.getId(), buffer);
                //?} else {
                /*serializer.streamCodec().encode(buffer, recipe);
                R decoded = serializer.streamCodec().decode(buffer);
                serializer.codec().codec().encodeStart(com.mojang.serialization.JsonOps.INSTANCE, recipe).getOrThrow();
                *///?}
                require(decoded != null && !buffer.isReadable(), "Recipe packet round trip: " + recipe.getId());
                require(recipe.getIngredients().size() == decoded.getIngredients().size(), "Ingredient count changed");
                require(PlatformHooks.sameStack(recipe.getResultItem(server.registryAccess()), decoded.getResultItem(server.registryAccess())), "Recipe output changed: " + recipe.getId());
            } finally { buffer.release(); }
        }
    }
    private static void itemData() {
        ItemStack charged = DAAItems.WHITE_LIGHTNING.get().getDefaultInstance();
        WhiteLightning.setCharges(charged, 2);
        WhiteLightning.setAdvCharges(charged, 1);
        WhiteLightning.setAttackCounts(charged, 123456);
        require(WhiteLightning.getCharges(charged) == 2 && WhiteLightning.getAdvCharges(charged) == 1, "Weapon charge components");
        require(WhiteLightning.getAttackCounts(charged) == 123456, "Large weapon counter");
        ItemStack neutron = new ItemStack(Items.IRON_INGOT);
        CompoundTag tag = new CompoundTag();
        tag.putInt("neutron", 1279);
        tag.putString("extra", "preserved");
        PlatformItemData.write(neutron, tag);
        require(NeutronContainerUpdater.absorbOne(neutron).absorbed(), "Neutron absorption");
        require(NeutronContainerUpdater.getNeutron(neutron) == 1280 && "preserved".equals(PlatformItemData.read(neutron).getString("extra")), "Neutron copy and commit");
        //? if neoforge {
        /*require(new PlatformNbtIngredient(Ingredient.of(Items.IRON_INGOT), "{neutron:1280}").test(neutron), "Partial custom-data condition");
        *///?}
        ItemStack unbreakable = DAAItems.IRON_VACUUM.get().getDefaultInstance();
        PlatformItemData.makeUnbreakable(unbreakable);
        require(!unbreakable.isDamageableItem(), "Unbreakable component");
        require(!PlatformHooks.damageConsumable(unbreakable, 1, net.minecraft.util.RandomSource.create()), "Unbreakable durability");
    }
    private static void machines(MinecraftServer server) {
        var level = server.overworld();
        var blocks = List.of(DAABlocks.INTEGRATED_BLOCK, DAABlocks.ASH_COLLECTOR, DAABlocks.MILLING_MACHINE, DAABlocks.CENTRIFUGE, DAABlocks.IONIZER, DAABlocks.ITEM_SENDER, DAABlocks.FISSION_REACTOR_CONTROLLER, DAABlocks.FISSION_REACTOR_INTERFACE);
        int index = 0;
        for (var block : blocks) {
            BlockPos pos = new BlockPos(index++, 80, 0);
            var state = block.get().defaultBlockState();
            level.setBlockAndUpdate(pos, state);
            BlockEntity entity = level.getBlockEntity(pos);
            require(entity != null, "Missing machine " + block.getId());
            var inventory = PlatformCapabilities.items(entity, null).resolve().orElseThrow();
            inventory.insertItem(0, new ItemStack(Items.IRON_INGOT, 3), false);
            //? if forge {
            CompoundTag saved = entity.saveWithFullMetadata();
            BlockEntity restored = BlockEntity.loadStatic(pos, state, saved);
            //?} else {
            /*CompoundTag saved = entity.saveWithFullMetadata(server.registryAccess());
            BlockEntity restored = BlockEntity.loadStatic(pos, state, saved, server.registryAccess());
            *///?}
            require(restored != null, "Machine reload " + block.getId());
            restored.setLevel(level);
            var restoredInventory = ((PlatformBlockEntity) restored).getCapability(PlatformCapabilities.ITEM_HANDLER).resolve().orElseThrow();
            require(PlatformHooks.sameStack(inventory.getStackInSlot(0), restoredInventory.getStackInSlot(0)) && inventory.getStackInSlot(0).getCount() == restoredInventory.getStackInSlot(0).getCount(), "Machine inventory persistence " + block.getId());
            if (block == DAABlocks.FISSION_REACTOR_CONTROLLER) require(PlatformCapabilities.energy(entity, null).isPresent(), "Reactor energy capability");
            level.removeBlock(pos, false);
        }
    }
    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
