package earth.terrarium.momento.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public class Commands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        PlayCommand.register(dispatcher);
    }
}
