package earth.terrarium.momento.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import earth.terrarium.momento.api.Dialogue;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;

public class PlayCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection environment, CommandBuildContext context) {
        dispatcher.register(Commands.literal("dialogue")
            .then(Commands.argument("target", EntityArgument.players())
                .then(Commands.argument("dialogue", ResourceLocationArgument.id()).suggests(DialogueArgument.SUGGEST_DIALOGUE)
                    .executes(context1 -> {
                        Dialogue dialogue = DialogueArgument.getDialogue(context1, "dialogue");
                        EntityArgument.getPlayers(context1, "target").forEach(dialogue::play);
                        return 1;
                    })
                )
            )
        );
    }
}
