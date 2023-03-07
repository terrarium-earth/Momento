package earth.terrarium.momento.common.network.packets.client;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.momento.Momento;
import earth.terrarium.momento.client.handlers.DialogueHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record DialoguePacket(ResourceLocation id) implements Packet<DialoguePacket> {

    public static final ResourceLocation ID = new ResourceLocation(Momento.MOD_ID, "play");
    public static final PacketHandler<DialoguePacket> HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<DialoguePacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<DialoguePacket> {

        @Override
        public void encode(DialoguePacket message, FriendlyByteBuf buffer) {
            buffer.writeResourceLocation(message.id);
        }

        @Override
        public DialoguePacket decode(FriendlyByteBuf buffer) {
            return new DialoguePacket(buffer.readResourceLocation());
        }

        @Override
        public PacketContext handle(DialoguePacket message) {
            return (player, level) -> {
                if (level.isClientSide()) {
                    DialogueHandler.playDialogue(message.id);
                }
            };
        }
    }
}
