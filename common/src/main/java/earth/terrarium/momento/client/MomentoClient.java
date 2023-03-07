package earth.terrarium.momento.client;

import earth.terrarium.momento.Momento;
import earth.terrarium.momento.common.data.DialogueReloadListener;
import earth.terrarium.momento.common.data.SrtReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.BiConsumer;

public class MomentoClient {

    public static void registerClientReloadListener(BiConsumer<ResourceLocation, PreparableReloadListener> register) {
        register.accept(new ResourceLocation(Momento.MOD_ID, "srt"), new SrtReloadListener());
        register.accept(new ResourceLocation(Momento.MOD_ID, "dialogues"), new DialogueReloadListener());
    }
}
