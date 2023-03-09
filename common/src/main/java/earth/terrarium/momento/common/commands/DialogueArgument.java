package earth.terrarium.momento.common.commands;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import earth.terrarium.momento.Momento;
import earth.terrarium.momento.common.managers.DialogueManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.resources.ResourceLocation;

public class DialogueArgument {

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_DIALOGUE = SuggestionProviders.register(
            new ResourceLocation(Momento.MOD_ID, "dialogue"),
            (commandContext, builder) -> SharedSuggestionProvider.suggestResource(DialogueManager.getDialogueIds(), builder)
    );

    public static void init() {
    }
}
