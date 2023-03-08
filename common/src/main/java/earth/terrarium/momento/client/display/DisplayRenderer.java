package earth.terrarium.momento.client.display;

import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.momento.client.sounds.DialogueSoundInstance;
import earth.terrarium.momento.common.srt.SrtBlock;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class DisplayRenderer {

    private static final List<String> BLOCKS = new ArrayList<>();
    private static DialogueSoundInstance dialogue;

    public static void render(float partialTicks, PoseStack stack) {
        if (dialogue == null) return;
        dialogue.dialogue().display().render(Minecraft.getInstance(), partialTicks, stack, BLOCKS);
    }

    public static void set(DialogueSoundInstance dialogue) {
        DisplayRenderer.dialogue = dialogue;
        update(dialogue);
    }

    public static void update(DialogueSoundInstance dialogue) {
        if (DisplayRenderer.dialogue == dialogue && dialogue != null) {
            DisplayRenderer.BLOCKS.clear();
            for (SrtBlock block : dialogue.blocks()) {
                DisplayRenderer.BLOCKS.add(block.text());
            }
        }
    }
}
