package earth.terrarium.momento.fabric;

import earth.terrarium.momento.Momento;
import net.fabricmc.api.ModInitializer;

public class MomentoFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Momento.init();
    }
}