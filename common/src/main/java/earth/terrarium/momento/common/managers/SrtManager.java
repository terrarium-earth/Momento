package earth.terrarium.momento.common.managers;

import com.mojang.serialization.Codec;
import earth.terrarium.momento.common.srt.SrtBlock;
import earth.terrarium.momento.common.srt.SrtFile;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SrtManager {

    private static final Map<ResourceLocation, SrtFile> FILES = new HashMap<>();

    public static final Codec<SrtFile> CODEC = ResourceLocation.CODEC.xmap(FILES::get, SrtFile::id);

    public static void addFiles(Map<ResourceLocation, List<SrtBlock>> files) {
        FILES.clear();
        for (var entry : files.entrySet()) {
            var id = entry.getKey();
            FILES.put(id, new SrtFile(id, entry.getValue()));
        }
    }

    @Nullable
    public static SrtFile get(ResourceLocation id) {
        return FILES.get(id);
    }

}
