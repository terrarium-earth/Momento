package earth.terrarium.momento.common.managers;

import earth.terrarium.momento.api.Dialogue;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class DialogueManager {

    private static final Map<ResourceLocation, Dialogue> DIALOGUES = new HashMap<>();

    public static void addDialogue(Map<ResourceLocation, Dialogue> dialogues) {
        DIALOGUES.clear();
        DIALOGUES.putAll(dialogues);
    }

    @Nullable
    public static Dialogue get(ResourceLocation id) {
        return DIALOGUES.get(id);
    }
}
