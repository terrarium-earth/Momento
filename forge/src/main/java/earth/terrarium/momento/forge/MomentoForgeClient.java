package earth.terrarium.momento.forge;

import earth.terrarium.momento.client.MomentoClient;
import earth.terrarium.momento.client.display.DisplayRenderer;
import earth.terrarium.momento.client.handlers.DialogueHandler;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.List;

public class MomentoForgeClient {

    public static final List<PropertyInfo> PROPERTIES = new ArrayList<>();

    public static void setup() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(MomentoForgeClient::onAddReloadListener);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(MomentoForgeClient::onClientSetup);
        MinecraftForge.EVENT_BUS.addListener(MomentoForgeClient::onHudRender);
        MinecraftForge.EVENT_BUS.addListener(MomentoForgeClient::onClientTick);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        MomentoClient.init();
        event.enqueueWork(() -> {
            for (PropertyInfo property : PROPERTIES) {
                ItemProperties.register(property.item(), property.id(), property.function());
            }
        });
    }

    private static void onAddReloadListener(RegisterClientReloadListenersEvent event) {
        MomentoClient.registerClientReloadListener((id, listener) -> event.registerReloadListener(listener));
    }

    private static void onHudRender(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            DisplayRenderer.render(event.getPartialTicks(), event.getMatrixStack());
        }
    }

    private static void onClientTick(TickEvent.ClientTickEvent event) {
        DialogueHandler.dialogueTick();
    }

    public record PropertyInfo(Item item, ResourceLocation id, ClampedItemPropertyFunction function){}
}
