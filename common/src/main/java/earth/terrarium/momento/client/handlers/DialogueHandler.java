package earth.terrarium.momento.client.handlers;

import com.mojang.logging.LogUtils;
import earth.terrarium.momento.api.Dialogue;
import earth.terrarium.momento.client.MomentoClient;
import earth.terrarium.momento.client.display.DisplayRenderer;
import earth.terrarium.momento.client.sounds.DialogueSoundInstance;
import earth.terrarium.momento.common.managers.DialogueManager;
import earth.terrarium.momento.common.managers.SrtManager;
import earth.terrarium.momento.common.srt.SrtFile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class DialogueHandler {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Set<ResourceLocation> queuedIds = new HashSet<>();
    private static final Queue<DialogueSoundInstance> queue = new ArrayDeque<>();
    private static DialogueSoundInstance currentDialogue;

    public static void playDialogue(ResourceLocation id) {
        if (queuedIds.contains(id) || (currentDialogue != null && currentDialogue.dialogue().id().equals(id))) {
            return;
        }
        Dialogue dialogue = DialogueManager.get(id);
        if (dialogue != null) {
            SrtFile file = SrtManager.get(dialogue.srt());
            if (file != null) {
                DialogueSoundInstance instance = new DialogueSoundInstance(Minecraft.getInstance().player, dialogue, file);
                instance.resolve(Minecraft.getInstance().getSoundManager()); //Need this or else the line below will break
                if (instance.getSound().shouldStream()) {
                    LOGGER.error("Dialogue {} is a stream, which is not supported", id);
                } else if (instance.getSound() != SoundManager.EMPTY_SOUND) {
                    DialogueHandler.queue.add(instance);
                    queuedIds.add(id);
                    checkQueue();
                }
            } else {
                LOGGER.error("Srt file {} not found", dialogue.srt());
            }
        } else {
            LOGGER.error("Dialogue {} not found", id);
        }
    }

    public static void checkQueue() {
        if (currentDialogue == null || currentDialogue.isStopped() || currentDialogue.isNotPlaying()) {
            playNext();
        }
    }

    public static void playNext() {
        if (queue.isEmpty()) {
            stop();
            return;
        }
        if (currentDialogue != null) {
            Minecraft.getInstance().getSoundManager().stop(currentDialogue);
            currentDialogue.stopInternal();
        }

        currentDialogue = queue.poll();
        queuedIds.remove(currentDialogue.dialogue().id());
        Minecraft.getInstance().getSoundManager().play(currentDialogue);
        DisplayRenderer.set(currentDialogue);
    }

    private static void stop() {
        if (currentDialogue != null) {
            currentDialogue = null;
            DisplayRenderer.set(null);
        }
    }

    public static void dialogueTick() {
        if (currentDialogue != null && MomentoClient.KEY.consumeClick()) {
            currentDialogue.jumpToNextBlock();
        }
        if (currentDialogue != null && (currentDialogue.isStopped() || currentDialogue.isNotPlaying())) {
            stop();
        }
        checkQueue();
    }
}
