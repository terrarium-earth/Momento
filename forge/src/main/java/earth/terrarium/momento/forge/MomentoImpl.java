package earth.terrarium.momento.forge;

import earth.terrarium.momento.Momento;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class MomentoImpl {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Momento.MOD_ID);

    public static Supplier<Item> registerItem(String id, Supplier<? extends Item> item) {
        return ITEMS.register(id, item);
    }
}
