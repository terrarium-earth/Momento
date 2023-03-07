package earth.terrarium.momento.fabric;

import earth.terrarium.momento.Momento;
import earth.terrarium.momento.common.commands.Commands;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class MomentoFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Momento.init();

        CommandRegistrationCallback.EVENT.register((dispatcher, access, env) -> {
            Commands.register(dispatcher, env, access);
        });
    }
}