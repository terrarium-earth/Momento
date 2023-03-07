package earth.terrarium.momento.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

public class Commands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, net.minecraft.commands.Commands.CommandSelection environment, CommandBuildContext context) {
        PlayCommand.register(dispatcher, environment, context);
    }
}
