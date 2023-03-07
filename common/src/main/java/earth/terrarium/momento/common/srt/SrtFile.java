package earth.terrarium.momento.common.srt;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record SrtFile(ResourceLocation id, List<SrtBlock> blocks) {
}
