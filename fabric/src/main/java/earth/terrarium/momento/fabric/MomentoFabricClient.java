package earth.terrarium.momento.fabric;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.momento.client.MomentoClient;
import earth.terrarium.momento.client.display.DisplayRenderer;
import earth.terrarium.momento.client.handlers.DialogueHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class MomentoFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MomentoClient.init();
        Color.initRainbow();
        MomentoClient.registerClientReloadListener((id, listener) -> {
            ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
                    .registerReloadListener(new IdentifiableResourceReloadListener() {
                        @Override
                        public ResourceLocation getFabricId() {
                            return id;
                        }

                        @Override
                        public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
                            return listener.reload(preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2);
                        }
                    });
        });
        HudRenderCallback.EVENT.register((stack, partialTicks) -> DisplayRenderer.render(partialTicks, stack));
        ClientTickEvents.END_CLIENT_TICK.register(client -> DialogueHandler.checkDialogue());
    }
}
