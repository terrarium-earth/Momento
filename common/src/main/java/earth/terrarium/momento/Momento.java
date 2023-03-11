package earth.terrarium.momento;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.momento.common.items.PlayerItem;
import earth.terrarium.momento.common.network.NetworkHandler;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class Momento {
    public static final String MOD_ID = "momento";

    public static final Supplier<Item> PLAYER = registerItem("player", () -> new PlayerItem(new Item.Properties().stacksTo(1)));

    public static void init() {
        NetworkHandler.init();
    }

    @ExpectPlatform
    private static Supplier<Item> registerItem(String id, Supplier<? extends Item> item) {
        throw new AssertionError();
    }
}