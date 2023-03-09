package earth.terrarium.momento.client.forge;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class MomentoClientImpl {
    public static void registerProperty(Item item, ResourceLocation resourceLocation, ClampedItemPropertyFunction clampedItemPropertyFunction) {
        ItemProperties.register(item, resourceLocation, clampedItemPropertyFunction);
    }
}
