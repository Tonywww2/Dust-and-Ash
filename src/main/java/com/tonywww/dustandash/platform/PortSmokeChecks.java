package com.tonywww.dustandash.platform;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.entity.FissionReactor.NeutronContainerUpdater;
import com.tonywww.dustandash.block.custom.AbstractHorizontalMachineBlock;
import com.tonywww.dustandash.block.entity.AutomaticMillingRecyclerEntity;
import com.tonywww.dustandash.block.entity.CentrifugeEntity;
import com.tonywww.dustandash.block.entity.MillingMachineEntity;
import com.tonywww.dustandash.menu.AutomaticMillingRecyclerMenu;
import com.tonywww.dustandash.tag.ModTags;
import com.tonywww.dustandash.item.WhiteLightning;
import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAAItems;
import com.tonywww.dustandash.registry.DAARecipe;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
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
            attachments(server);
            Files.writeString(Path.of("port-smoke-result.txt"), "PASS: 123 custom recipes and network round trips; item data; 9 machine capability/save-load round trips; reactor energy; accelerator tiers/timing/non-stacking; recycler facing/filter/capacity/persistence/menu synchronization/access/drop checks\n");
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
        var blocks = List.of(DAABlocks.INTEGRATED_BLOCK, DAABlocks.ASH_COLLECTOR, DAABlocks.MILLING_MACHINE, DAABlocks.CENTRIFUGE, DAABlocks.IONIZER, DAABlocks.ITEM_SENDER, DAABlocks.FISSION_REACTOR_CONTROLLER, DAABlocks.FISSION_REACTOR_INTERFACE, DAABlocks.AUTOMATIC_MILLING_RECYCLER);
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
    private static void attachments(MinecraftServer server) {
        var level = server.overworld();
        // Keep item-drop assertions in the spawn area's entity-tracked chunks on both loaders.
        BlockPos center = level.getSharedSpawnPos().atY(100);
        var tiers = List.of(DAABlocks.CENTRIFUGE_ACCELERATOR_1, DAABlocks.CENTRIFUGE_ACCELERATOR_2,
                DAABlocks.CENTRIFUGE_ACCELERATOR_3, DAABlocks.CENTRIFUGE_ACCELERATOR_4);
        int[] durations = {130, 70, 30, 20};
        level.setBlockAndUpdate(center, DAABlocks.CENTRIFUGE.get().defaultBlockState());
        var centrifuge = (CentrifugeEntity) level.getBlockEntity(center);
        require(centrifuge.getAcceleratedDuration(200) == 200, "Unboosted centrifuge duration");
        int progress = DustAndAshConfig.MACHINES.centrifugeProgressPerTick.get();
        require(progress > 0, "Smoke checks require a running centrifuge configuration");
        for (int tier = 0; tier < tiers.size(); tier++) {
            for (Direction direction : Direction.values()) {
                BlockPos adjacent = center.relative(direction);
                level.setBlockAndUpdate(adjacent, tiers.get(tier).get().defaultBlockState());
                require(centrifuge.getAcceleratedDuration(200) == durations[tier], "Accelerator tier/direction " + tier + "/" + direction);
                require(centrifuge.getAcceleratedDuration(1) == 1, "Minimum processing duration");
                level.removeBlock(adjacent, false);
            }
            level.setBlockAndUpdate(center.above(), tiers.get(tier).get().defaultBlockState());
            level.setBlockAndUpdate(center.below(), DAABlocks.CENTRIFUGE_ACCELERATOR_1.get().defaultBlockState());
            require(centrifuge.getAcceleratedDuration(200) == durations[tier], "Accelerators must not stack");
            centrifuge.invItemStackHandler.setStackInSlot(0, new ItemStack(DAAItems.CRUSHED_SEEDS.get()));
            centrifuge.invItemStackHandler.setStackInSlot(1, new ItemStack(DAAItems.GLASS_CONTAINER.get()));
            CentrifugeEntity.craft(level, centrifuge);
            // The duration is fixed at start, so removal must not change an in-flight operation.
            level.removeBlock(center.above(), false);
            level.removeBlock(center.below(), false);
            int elapsed = 0;
            while (centrifuge.invItemStackHandler.getStackInSlot(6).isEmpty() && elapsed < 220) {
                CentrifugeEntity.tick(level, center, centrifuge.getBlockState(), centrifuge);
                elapsed++;
            }
            require(elapsed == (durations[tier] + progress - 1) / progress, "Actual accelerator processing ticks: tier " + tier + " = " + elapsed);
            require(centrifuge.invItemStackHandler.getStackInSlot(6).is(DAAItems.BASE_OIL.get()), "Accelerated recipe output");
            centrifuge.invItemStackHandler.setStackInSlot(6, ItemStack.EMPTY);
        }
        level.removeBlock(center, false);

        BlockPos pos = center.offset(8, 0, 0);
        level.setBlockAndUpdate(pos, DAABlocks.AUTOMATIC_MILLING_RECYCLER.get().defaultBlockState());
        var recycler = (AutomaticMillingRecyclerEntity) level.getBlockEntity(pos);
        var millState = DAABlocks.MILLING_MACHINE.get().defaultBlockState();
        for (int cell = 0; cell < 25; cell++) require(recycler.isSlotEnabled(cell), "Default grid cell " + cell);
        // All six neighbours exist; exactly the rear one must be used in all four rotations.
        for (Direction direction : Direction.values()) level.setBlockAndUpdate(pos.relative(direction), millState);
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            level.setBlockAndUpdate(pos, recycler.getBlockState().setValue(AbstractHorizontalMachineBlock.FACING, facing));
            require(recycler.getConnectedMill() == level.getBlockEntity(pos.relative(facing.getOpposite())), "Rear-only connection " + facing);
            level.removeBlock(pos.relative(facing.getOpposite()), false);
            require(recycler.getConnectedMill() == null, "No fallback to other neighbouring mills");
            level.setBlockAndUpdate(pos.relative(facing.getOpposite()), millState);
        }
        level.setBlockAndUpdate(pos, recycler.getBlockState().setValue(AbstractHorizontalMachineBlock.FACING, Direction.NORTH));
        for (Direction direction : Direction.values()) {
            var capability = PlatformCapabilities.items(recycler, direction).resolve().orElseThrow();
            require(capability.insertItem(0, new ItemStack(Items.DIAMOND, 2), false).isEmpty(), "All-face insertion " + direction);
            require(capability.extractItem(0, 2, false).getCount() == 2, "All-face extraction " + direction);
        }
        var mill = recycler.getConnectedMill();
        require(mill != null, "Connected mill exists");
        var scraps = List.of(DAAItems.IRON_SCRAP, DAAItems.REDSTONE_SCRAP, DAAItems.ASH_STEEL_SCRAP,
                DAAItems.CARBON_FIBER_SCRAP, DAAItems.TITANIUM_PLATE_SCRAP);
        for (int i = 0; i < scraps.size(); i++) {
            ItemStack scrap = new ItemStack(scraps.get(i).get());
            require(scrap.is(ModTags.Items.SCRAPS), "Scrap tag membership " + scraps.get(i).getId());
            mill.invItemStackHandler.setStackInSlot(3 + i, scrap);
            recycler.setSlotEnabled(i, false);
        }
        recycler.setSlotEnabled(24, false);
        mill.invItemStackHandler.setStackInSlot(27, new ItemStack(DAAItems.IRON_SCRAP.get()));
        mill.invItemStackHandler.setStackInSlot(8, new ItemStack(DAAItems.IRON_SCRAP.get())); // Enabled.
        mill.invItemStackHandler.setStackInSlot(9, new ItemStack(Items.DIAMOND)); // Disabled, non-scrap.
        recycler.setSlotEnabled(6, false);
        for (int i = 0; i < 3; i++) mill.invItemStackHandler.setStackInSlot(i, new ItemStack(DAAItems.IRON_SCRAP.get()));
        for (Direction direction : Direction.values()) {
            if (direction != Direction.SOUTH) ((MillingMachineEntity)level.getBlockEntity(pos.relative(direction)))
                    .invItemStackHandler.setStackInSlot(3, new ItemStack(DAAItems.IRON_SCRAP.get()));
        }
        tickRecycler(level, recycler);
        for (int i = 0; i < 5; i++) require(mill.invItemStackHandler.getStackInSlot(3 + i).isEmpty(), "Collect disabled grid cell " + i);
        require(mill.invItemStackHandler.getStackInSlot(27).isEmpty(), "Collect high bit/cell 24");
        require(countItems(recycler.getInventory()) == 6, "No duplicated or lost scraps");
        require(!mill.invItemStackHandler.getStackInSlot(8).isEmpty(), "Enabled cell preserved");
        require(mill.invItemStackHandler.getStackInSlot(9).is(Items.DIAMOND), "Non-scrap preserved");
        for (int i = 0; i < 3; i++) require(!mill.invItemStackHandler.getStackInSlot(i).isEmpty(), "Input/output slots preserved");
        for (Direction direction : Direction.values()) if (direction != Direction.SOUTH) {
            var other = (MillingMachineEntity)level.getBlockEntity(pos.relative(direction));
            require(!other.invItemStackHandler.getStackInSlot(3).isEmpty(), "Other mill untouched " + direction);
        }
        for (int slot = 0; slot < 18; slot++) recycler.getInventory().setStackInSlot(slot, new ItemStack(Items.COBBLESTONE, 64));
        mill.invItemStackHandler.setStackInSlot(3, new ItemStack(DAAItems.IRON_SCRAP.get()));
        tickRecycler(level, recycler);
        require(mill.invItemStackHandler.getStackInSlot(3).getCount() == 1, "Full inventory must leave scrap in place");
        recycler.getInventory().setStackInSlot(17, new ItemStack(DAAItems.IRON_SCRAP.get(), 63));
        tickRecycler(level, recycler);
        require(mill.invItemStackHandler.getStackInSlot(3).isEmpty() && recycler.getInventory().getStackInSlot(17).getCount() == 64,
                "Last free capacity accepts exactly one scrap");

        // Save/load must retain high bits as well as all 18 storage slots.
        CompoundTag saved = new CompoundTag();
        ((PlatformBlockEntity) recycler).saveAdditional(saved);
        var restored = new AutomaticMillingRecyclerEntity(pos, recycler.getBlockState());
        restored.load(saved);
        require(!restored.isSlotEnabled(0) && !restored.isSlotEnabled(24) && restored.isSlotEnabled(5), "Grid persistence");
        require(countItems(restored.getInventory()) == 18 * 64, "Recycler inventory persistence");
        var blank = new AutomaticMillingRecyclerEntity(pos, recycler.getBlockState());
        blank.load(new CompoundTag());
        require(blank.isSlotEnabled(24), "Missing grid NBT retains enabled defaults");

        // Exercise the same menu entry point used by vanilla's button packet handler.
        //? if forge {
        var player = net.minecraftforge.common.util.FakePlayerFactory.getMinecraft(level);
        //?} else {
        /*var player = net.neoforged.neoforge.common.util.FakePlayerFactory.getMinecraft(level);
        *///?}
        player.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        var menu = (AutomaticMillingRecyclerMenu)recycler.createMenu(7, player.getInventory(), player);
        require(menu != null && menu.stillValid(player), "Connected menu valid");
        player.containerMenu = menu;
        require(menu.slots.size() == 54, "18 machine slots plus 36 player slots");
        int[] received = new int[25];
        menu.addSlotListener(new ContainerListener() {
            @Override public void slotChanged(AbstractContainerMenu ignored, int slot, ItemStack stack) {}
            @Override public void dataChanged(AbstractContainerMenu ignored, int cell, int value) { received[cell] = value; }
        });
        require(received[24] == 0 && received[5] == 1, "Initial menu data sync");
        require(menu.clickMenuButton(player, 24) && received[24] == 1, "Button change and high-cell sync");
        require(!menu.clickMenuButton(player, -1) && !menu.clickMenuButton(player, 25), "Out-of-range button IDs rejected");
        var secondMenu = (AutomaticMillingRecyclerMenu)recycler.createMenu(8, player.getInventory(), player);
        require(secondMenu.isSlotEnabled(24), "Reopened/second viewer sees current settings");
        require(menu.clickMenuButton(player, 24), "Second click toggles off");
        require(!secondMenu.isSlotEnabled(24), "Other viewer observes shared server data");
        FriendlyByteBuf buttonBuffer = new FriendlyByteBuf(Unpooled.buffer());
        try {
            var buttonPacket = new ServerboundContainerButtonClickPacket(menu.containerId, 24);
            //? if forge {
            buttonPacket.write(buttonBuffer);
            var decodedButton = new ServerboundContainerButtonClickPacket(buttonBuffer);
            require(decodedButton.getContainerId() == menu.containerId && decodedButton.getButtonId() == 24,
                    "Container button packet round trip");
            //?} else {
            /*ServerboundContainerButtonClickPacket.STREAM_CODEC.encode(buttonBuffer, buttonPacket);
            var decodedButton = ServerboundContainerButtonClickPacket.STREAM_CODEC.decode(buttonBuffer);
            require(decodedButton.containerId() == menu.containerId && decodedButton.buttonId() == 24,
                    "Container button packet round trip");
            *///?}
            require(!buttonBuffer.isReadable(), "Button packet consumed completely");
        } finally { buttonBuffer.release(); }
        player.setPos(pos.getX() + 100, pos.getY(), pos.getZ());
        require(!menu.stillValid(player) && !menu.clickMenuButton(player, 24), "Remote player rejected");
        player.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        level.removeBlock(pos.south(), false);
        require(!menu.stillValid(player) && !menu.clickMenuButton(player, 24), "Disconnected menu rejected");
        require(recycler.createMenu(9, player.getInventory(), player) == null, "Cannot open disconnected recycler");
        player.containerMenu = player.inventoryMenu;

        // Measure only the recycler's stored types in a clean, bounded area.
        level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(2)).forEach(ItemEntity::discard);
        level.destroyBlock(pos, false);
        int dropped = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(2)).stream()
                .map(ItemEntity::getItem).filter(stack -> stack.is(Items.COBBLESTONE) || stack.is(DAAItems.IRON_SCRAP.get()))
                .mapToInt(ItemStack::getCount).sum();
        require(dropped == 18 * 64, "Breaking recycler drops all inventory: " + dropped);
        require(!menu.stillValid(player), "Removed block invalidates menu");
        level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(2)).forEach(ItemEntity::discard);
        for (Direction direction : Direction.values()) level.setBlockAndUpdate(pos.relative(direction), Blocks.AIR.defaultBlockState());
        DustAndAsh.getLogger().info("MACHINE_ATTACHMENTS_PASS: timing, facing, filtering, capacity, persistence, menu sync/access and drops");
    }

    private static void tickRecycler(net.minecraft.world.level.Level level, AutomaticMillingRecyclerEntity recycler) {
        for (int tick = 0; tick < 4; tick++) AutomaticMillingRecyclerEntity.tick(level, recycler.getBlockPos(), recycler.getBlockState(), recycler);
    }

    private static int countItems(IItemHandler inventory) {
        int count = 0;
        for (int slot = 0; slot < inventory.getSlots(); slot++) count += inventory.getStackInSlot(slot).getCount();
        return count;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
