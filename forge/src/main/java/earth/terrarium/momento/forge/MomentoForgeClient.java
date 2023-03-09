package earth.terrarium.momento.forge;

import earth.terrarium.momento.client.MomentoClient;
import earth.terrarium.momento.client.display.DisplayRenderer;
import earth.terrarium.momento.client.handlers.DialogueHandler;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class MomentoForgeClient {

    public static void setup() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(MomentoForgeClient::onAddReloadListener);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(MomentoForgeClient::onClientSetup);
        MinecraftForge.EVENT_BUS.addListener(MomentoForgeClient::onHudRender);
        MinecraftForge.EVENT_BUS.addListener(MomentoForgeClient::onClientTick);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        MomentoClient.init();
    }

    private static void onAddReloadListener(RegisterClientReloadListenersEvent event) {
        MomentoClient.registerClientReloadListener((id, listener) -> event.registerReloadListener(listener));
    }

    private static void onHudRender(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() == VanillaGuiOverlay.PLAYER_LIST.type()) {
            DisplayRenderer.render(event.getPartialTick(), event.getPoseStack());
        }
    }

    private static void onClientTick(TickEvent.ClientTickEvent event) {
        DialogueHandler.checkDialogue();
    }
}
