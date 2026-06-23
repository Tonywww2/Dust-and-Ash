package com.tonywww.dustandash.cthulhu.network;

import com.tonywww.dustandash.cthulhu.client.ClientGraphemeData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GraphemeUpdatePacket {

    private final Map<Character, Integer> letters;

    public GraphemeUpdatePacket(Map<Character, Integer> letters) {
        this.letters = new HashMap<>(letters);
    }

    public GraphemeUpdatePacket(FriendlyByteBuf buffer) {
        int size = buffer.readVarInt();
        this.letters = new HashMap<>();
        for (int i = 0; i < size; i++) {
            char letter = buffer.readChar();
            int count = buffer.readVarInt();
            this.letters.put(letter, count);
        }
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(letters.size());
        letters.forEach((letter, count) -> {
            buffer.writeChar(letter);
            buffer.writeVarInt(count);
        });
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> ClientGraphemeData.replaceLetters(letters));
        context.get().setPacketHandled(true);
    }
}
