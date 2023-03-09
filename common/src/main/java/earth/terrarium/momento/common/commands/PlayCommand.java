package earth.terrarium.momento.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import earth.terrarium.momento.common.network.packets.client.DialoguePacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;

public class PlayCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("dialogue")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("target", EntityArgument.players())
                        .then(Commands.argument("dialogue", ResourceLocationArgument.id()).suggests(DialogueArgument.SUGGEST_DIALOGUE)
                                .executes(context1 -> {
                                    var id = ResourceLocationArgument.getId(context1, "dialogue");
                                    EntityArgument.getPlayers(context1, "target")
                                            .forEach(player -> DialoguePacket.play(player, id));
                                    return 1;
                                })
                        )
                )
        );
    }
}
