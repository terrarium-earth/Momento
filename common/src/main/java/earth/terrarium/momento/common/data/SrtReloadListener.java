package earth.terrarium.momento.common.data;

import com.mojang.logging.LogUtils;
import earth.terrarium.momento.Momento;
import earth.terrarium.momento.common.managers.SrtManager;
import earth.terrarium.momento.common.srt.SrtBlock;
import earth.terrarium.momento.common.srt.SrtParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SrtReloadListener extends SimplePreparableReloadListener<Map<ResourceLocation, List<SrtBlock>>> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String FILE_EXTENSION = ".srt";
    private static final String DIRECTORY = Momento.MOD_ID + "/srt";

    @Override
    protected @NotNull Map<ResourceLocation, List<SrtBlock>> prepare(@NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        profiler.startTick();
        final Map<ResourceLocation, List<SrtBlock>> map = new HashMap<>();
        final var entries = manager.listResources(DIRECTORY, file -> file.getPath().endsWith(FILE_EXTENSION)).entrySet();

        for (var entry : entries) {
            var file = entry.getKey();
            var id = new ResourceLocation(file.getNamespace(), file.getPath().substring(DIRECTORY.length() + 1, file.getPath().length() - FILE_EXTENSION.length()));

            try {
                try (var reader = entry.getValue().openAsReader()) {
                    var data = IOUtils.toString(reader);
                    var srt = SrtParser.parse(data);
                    if (srt.isEmpty()) {
                        LOGGER.error("Couldn't load data file {} from {} as it's null or empty", id, file);
                    } else {
                        var out = map.put(id, srt);
                        if (out != null) {
                            throw new  IllegalStateException("Duplicate data file ignored with ID {}" + id);
                        }
                    }
                }
            } catch (Exception err) {
                LOGGER.error("Couldn't parse data file {} from {}", id, file, err);
            }
        }
        profiler.endTick();
        return map;
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, List<SrtBlock>> data, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        profiler.startTick();
        profiler.push("addFiles");
        SrtManager.addFiles(data);
        profiler.pop();
        profiler.endTick();
    }
}
