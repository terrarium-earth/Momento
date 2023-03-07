package earth.terrarium.momento.common.network;

import com.teamresourceful.resourcefullib.common.networking.NetworkChannel;
import com.teamresourceful.resourcefullib.common.networking.base.NetworkDirection;
import earth.terrarium.momento.Momento;
import earth.terrarium.momento.common.network.packets.client.DialoguePacket;

public final class NetworkHandler {

    public static final NetworkChannel CHANNEL = new NetworkChannel(Momento.MOD_ID, 1, "main");

    public static void init() {
        CHANNEL.registerPacket(NetworkDirection.SERVER_TO_CLIENT, DialoguePacket.ID, DialoguePacket.HANDLER, DialoguePacket.class);
    }
}
