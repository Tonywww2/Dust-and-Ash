package com.tonywww.dustandash.cthulhu.network;

import com.tonywww.dustandash.cthulhu.api.GraphemeAPI;
import com.tonywww.dustandash.cthulhu.fight.BossFightInstance;
import com.tonywww.dustandash.cthulhu.fight.BossFightManager;
import com.tonywww.dustandash.cthulhu.fight.FightPhase;
import com.tonywww.dustandash.cthulhu.grapheme.WordDictionary;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class WordSpellPacket {

    private final String word;

    public WordSpellPacket(String word) {
        this.word = word;
    }

    public WordSpellPacket(FriendlyByteBuf buffer) {
        this(buffer.readUtf(32));
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.word, 32);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) {
                return;
            }

            String normalized = WordDictionary.normalize(word);
            if (!WordDictionary.isKnown(normalized)) {
                player.sendSystemMessage(Component.literal("Unknown law word: " + normalized));
                return;
            }

            BossFightInstance instance = BossFightManager.get().getActiveFight((net.minecraft.server.level.ServerLevel) player.level());
            if (instance == null) {
                player.sendSystemMessage(Component.literal("No active Azathoth fight in this dimension."));
                return;
            }
            if (!instance.isTypingReady(player)) {
                player.sendSystemMessage(Component.literal("Typing cooldown: " + instance.getTypingCooldownLeft(player) + " tick(s)."));
                return;
            }

            boolean success = GraphemeAPI.trySpell(player, normalized, p -> WordDictionary.apply(p, normalized));
            if (!success) {
                player.sendSystemMessage(Component.literal("Missing graphemes for: " + normalized));
                return;
            }

            instance.markTypingCooldown(player);
            if (instance != null && instance.phase() == FightPhase.FINAL_TRUTH && "REALITY EXISTS".equals(normalized)
                    && instance.submitFinalTruth(player)) {
                BossFightManager.get().terminateFight(instance);
            }
        });
        context.get().setPacketHandled(true);
    }
}
