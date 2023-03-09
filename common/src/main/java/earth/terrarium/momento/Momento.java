package earth.terrarium.momento;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.momento.common.items.PlayerItem;
import earth.terrarium.momento.common.network.NetworkHandler;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;

public class Momento {
    public static final String MOD_ID = "momento";

    public static final ResourcefulRegistry<Item> ITEMS = ResourcefulRegistries.create(Registry.ITEM, MOD_ID);

    public static final RegistryEntry<Item> PLAYER = ITEMS.register("player", () -> new PlayerItem(new Item.Properties()));

    public static void init() {
        NetworkHandler.init();
        ITEMS.init();
    }
}