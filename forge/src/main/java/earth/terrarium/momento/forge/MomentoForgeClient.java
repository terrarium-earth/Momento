package earth.terrarium.momento.forge;

import earth.terrarium.momento.client.MomentoClient;
import earth.terrarium.momento.client.display.DisplayRenderer;
import earth.terrarium.momento.client.handlers.DialogueHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.List;

public class MomentoForgeClient {

    public static final List<PropertyInfo> PROPERTIES = new ArrayList<>();
    public static final List<KeyMapping> MAPPINGS = new ArrayList<>();

    public static void setup() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(MomentoForgeClient::onAddReloadListener);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(MomentoForgeClient::onClientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(MomentoForgeClient::onRegisterKeys);
        MinecraftForge.EVENT_BUS.addListener(MomentoForgeClient::onHudRender);
        MinecraftForge.EVENT_BUS.addListener(MomentoForgeClient::onClientTick);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        MomentoClient.init();
        event.enqueueWork(() -> {
            for (PropertyInfo info : PROPERTIES) {
                ItemProperties.register(info.item(), info.id(), info.function());
            }
            PROPERTIES.clear();
        });
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
        DialogueHandler.dialogueTick();
    }

    private static void onRegisterKeys(RegisterKeyMappingsEvent event) {
        for (KeyMapping mapping : MAPPINGS) {
            event.register(mapping);
        }
        MAPPINGS.clear();
    }

    public record PropertyInfo(Item item, ResourceLocation id, ClampedItemPropertyFunction function){}
}
