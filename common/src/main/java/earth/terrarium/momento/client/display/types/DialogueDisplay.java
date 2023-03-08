package earth.terrarium.momento.client.display.types;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

import java.util.List;

public interface DialogueDisplay {

    String id();

    void render(Minecraft minecraft, float partialTicks, PoseStack stack, List<String> text);
}
