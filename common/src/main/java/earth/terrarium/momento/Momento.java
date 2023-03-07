package earth.terrarium.momento;

import earth.terrarium.momento.common.network.NetworkHandler;

public class Momento {
    public static final String MOD_ID = "momento";

    public static void init() {
        NetworkHandler.init();
    }
}