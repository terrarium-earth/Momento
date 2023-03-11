package earth.terrarium.momento.fabric;

import earth.terrarium.momento.Momento;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class MomentoImpl {
    public static Supplier<Item> registerItem(String id, Supplier<? extends Item> item) {
        var out = Registry.register(Registry.ITEM, new ResourceLocation(Momento.MOD_ID, id), item.get());
        return () -> out;
    }
}
