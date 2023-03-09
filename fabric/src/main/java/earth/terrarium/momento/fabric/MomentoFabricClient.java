package earth.terrarium.momento.fabric;

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
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class MomentoFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MomentoClient.init();
        MomentoClient.registerClientReloadListener((id, listener) ->
                ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new FabricListener(id, listener))
        );
        HudRenderCallback.EVENT.register((stack, partialTicks) -> DisplayRenderer.render(partialTicks, stack));
        ClientTickEvents.END_CLIENT_TICK.register(client -> DialogueHandler.checkDialogue());
    }

    private record FabricListener(ResourceLocation id, PreparableReloadListener listener) implements IdentifiableResourceReloadListener {

        @Override
        public ResourceLocation getFabricId() {
            return id;
        }

        @Override
        public @NotNull CompletableFuture<Void> reload(@NotNull PreparationBarrier barrier, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler, @NotNull ProfilerFiller profiler1, @NotNull Executor executor, @NotNull Executor executor2) {
            return listener.reload(barrier, manager, profiler, profiler1, executor, executor2);
        }
    }
}
