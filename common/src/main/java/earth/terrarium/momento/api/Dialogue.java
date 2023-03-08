package earth.terrarium.momento.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.momento.client.display.types.CanvasDisplay;
import earth.terrarium.momento.client.display.types.DialogueDisplay;
import earth.terrarium.momento.client.display.types.Displays;
import earth.terrarium.momento.common.network.NetworkHandler;
import earth.terrarium.momento.common.network.packets.client.DialoguePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;

public record Dialogue(ResourceLocation id, SoundEvent sound, float volume, ResourceLocation srt, DialogueDisplay display) {

    public static Codec<Dialogue> codec(ResourceLocation id) {
        return RecordCodecBuilder.create(instance -> instance.group(
                RecordCodecBuilder.point(id),
                SoundEvent.CODEC.fieldOf("sound").forGetter(Dialogue::sound),
                Codec.FLOAT.fieldOf("volume").forGetter(Dialogue::volume),
                ResourceLocation.CODEC.fieldOf("srt").forGetter(Dialogue::srt),
                Displays.CODEC.fieldOf("display").orElse(CanvasDisplay.DEFAULT).forGetter(Dialogue::display)
        ).apply(instance, Dialogue::new));
    }

    public void play(Player player) {
        NetworkHandler.CHANNEL.sendToPlayer(new DialoguePacket(this.id), player);
    }
}
