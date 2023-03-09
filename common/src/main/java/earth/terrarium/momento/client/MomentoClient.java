package earth.terrarium.momento.client;

import com.teamresourceful.resourcefullib.common.color.Color;
import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.momento.Momento;
import earth.terrarium.momento.common.commands.DialogueArgument;
import earth.terrarium.momento.common.data.DialogueReloadListener;
import earth.terrarium.momento.common.data.SrtReloadListener;
import earth.terrarium.momento.common.items.PlayerItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class MomentoClient {

    public static void init() {
        Color.initRainbow();
        DialogueArgument.init();
        registerProperty(Momento.PLAYER.get(), new ResourceLocation(Momento.MOD_ID, "type"),
                (ItemStack stack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) ->
                        PlayerItem.getIcon(stack).getValue());
    }

    public static void registerClientReloadListener(BiConsumer<ResourceLocation, PreparableReloadListener> register) {
        register.accept(new ResourceLocation(Momento.MOD_ID, "srt"), new SrtReloadListener());
        register.accept(new ResourceLocation(Momento.MOD_ID, "dialogues"), new DialogueReloadListener());
    }

    @ExpectPlatform
    public static void registerProperty(Item item, ResourceLocation resourceLocation, ClampedItemPropertyFunction clampedItemPropertyFunction) {
        throw new NotImplementedException();
    }
}
