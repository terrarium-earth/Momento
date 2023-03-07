package earth.terrarium.momento.common.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import earth.terrarium.momento.Momento;
import earth.terrarium.momento.api.Dialogue;
import earth.terrarium.momento.common.managers.DialogueManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class DialogueReloadListener extends SimpleJsonResourceReloadListener {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();

    public DialogueReloadListener() {
        super(GSON, Momento.MOD_ID + "/dialogue");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> data, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        final Map<ResourceLocation, Dialogue> map = new HashMap<>();
        for (var entry : data.entrySet()) {
            var id = entry.getKey();
            var json = entry.getValue();
            try {
                var dialogue = Dialogue.codec(id).decode(JsonOps.INSTANCE, json)
                        .getOrThrow(false, LOGGER::error)
                        .getFirst();
                map.put(id, dialogue);
            } catch (Exception err) {
                LOGGER.error("Couldn't parse data file {}", id, err);
            }
        }
        DialogueManager.addDialogue(map);
    }
}
