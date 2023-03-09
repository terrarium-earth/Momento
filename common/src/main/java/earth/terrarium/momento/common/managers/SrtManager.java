package earth.terrarium.momento.common.managers;

import com.mojang.datafixers.util.Pair;
import earth.terrarium.momento.common.srt.SrtBlock;
import earth.terrarium.momento.common.srt.SrtFile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SrtManager {

    private static final Map<ResourceLocation, Map<String, SrtFile>> FILES = new HashMap<>();

    public static void addFiles(Map<ResourceLocation, List<SrtBlock>> files) {
        FILES.clear();
        for (var entry : files.entrySet()) {
            var ids = extractLangFromPath(entry.getKey());
            FILES.computeIfAbsent(ids.getSecond(), (key) -> new HashMap<>())
                    .put(ids.getFirst(), new SrtFile(ids.getSecond(), entry.getValue()));
        }
    }

    @Nullable
    public static SrtFile get(ResourceLocation id) {
        var langFiles = FILES.computeIfAbsent(id, (key) -> new HashMap<>());
        return langFiles.getOrDefault(getLangCode(), langFiles.get("en_us"));
    }

    private static Pair<String, ResourceLocation> extractLangFromPath(ResourceLocation id) {
        String[] split = id.getPath().split("/");
        if (split.length > 1) {
            return Pair.of(split[0], new ResourceLocation(id.getNamespace(), String.join("/", split).substring(split[0].length() + 1)));
        }
        throw new IllegalArgumentException("Invalid path for SRT file: " + id);
    }

    @Environment(EnvType.CLIENT)
    private static String getLangCode() {
        return Minecraft.getInstance().getLanguageManager().getSelected().getCode();
    }

}
