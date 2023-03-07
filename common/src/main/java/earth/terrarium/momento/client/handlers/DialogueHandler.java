package earth.terrarium.momento.client.handlers;

import com.mojang.logging.LogUtils;
import earth.terrarium.momento.api.Dialogue;
import earth.terrarium.momento.client.sounds.DialogueSoundInstance;
import earth.terrarium.momento.common.managers.DialogueManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class DialogueHandler {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void playDialogue(ResourceLocation id) {
        Dialogue dialogue = DialogueManager.get(id);
        if (dialogue != null) {
            DialogueSoundInstance instance = new DialogueSoundInstance(Minecraft.getInstance().player, dialogue);
            Minecraft.getInstance().getSoundManager().play(instance);
        } else {
            LOGGER.error("Dialogue {} not found", id);
        }
    }
}
