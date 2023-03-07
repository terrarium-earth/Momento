package earth.terrarium.momento.common.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import earth.terrarium.momento.api.Dialogue;
import earth.terrarium.momento.common.managers.DialogueManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class DialogueArgument {

    private static final DynamicCommandExceptionType ERROR_UNKNOWN_DIALOGUE = new DynamicCommandExceptionType(
            (object) -> Component.translatable("dialogue.dialogueNotFound", object));

    public static Dialogue getDialogue(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        ResourceLocation resourceLocation = ResourceLocationArgument.getId(commandContext, string);
        Dialogue dialogue = DialogueManager.get(resourceLocation);
        if (dialogue == null) {
            throw ERROR_UNKNOWN_DIALOGUE.create(resourceLocation);
        }
        return dialogue;
    }
}
