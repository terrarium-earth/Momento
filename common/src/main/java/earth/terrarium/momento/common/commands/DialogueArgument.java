package earth.terrarium.momento.common.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import earth.terrarium.momento.Momento;
import earth.terrarium.momento.api.Dialogue;
import earth.terrarium.momento.common.managers.DialogueManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class DialogueArgument {

    private static final DynamicCommandExceptionType ERROR_UNKNOWN_DIALOGUE = new DynamicCommandExceptionType(
            (object) -> Component.translatable("dialogue.dialogueNotFound", object));

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_DIALOGUE = SuggestionProviders.register(
            new ResourceLocation(Momento.MOD_ID, "dialogue"),
            (commandContext, builder) -> SharedSuggestionProvider.suggestResource(DialogueManager.getDialogueIds(), builder)
    );

    public static Dialogue getDialogue(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        ResourceLocation resourceLocation = ResourceLocationArgument.getId(commandContext, string);
        Dialogue dialogue = DialogueManager.get(resourceLocation);
        if (dialogue == null) {
            throw ERROR_UNKNOWN_DIALOGUE.create(resourceLocation);
        }
        return dialogue;
    }
}
