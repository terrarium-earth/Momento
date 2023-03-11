package earth.terrarium.momento.client.forge;

import earth.terrarium.momento.forge.MomentoForgeClient;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.ClientRegistry;

public class MomentoClientImpl {
    public static void registerProperty(Item item, ResourceLocation resourceLocation, ClampedItemPropertyFunction clampedItemPropertyFunction) {
        MomentoForgeClient.PROPERTIES.add(new MomentoForgeClient.PropertyInfo(item, resourceLocation, clampedItemPropertyFunction));
    }

    public static void registerMapping(KeyMapping keyMapping) {
        ClientRegistry.registerKeyBinding(keyMapping);
    }
}
