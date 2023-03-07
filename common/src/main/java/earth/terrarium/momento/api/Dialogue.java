package earth.terrarium.momento.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import earth.terrarium.momento.common.managers.SrtManager;
import earth.terrarium.momento.common.network.NetworkHandler;
import earth.terrarium.momento.common.network.packets.client.DialoguePacket;
import earth.terrarium.momento.common.srt.SrtFile;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;

public record Dialogue(ResourceLocation id, MutableComponent display, SoundEvent sound, float volume, SrtFile srt) {

    public static Codec<Dialogue> codec(ResourceLocation id) {
        return RecordCodecBuilder.create(instance -> instance.group(
                RecordCodecBuilder.point(id),
                CodecExtras.passthrough(Component.Serializer::toJsonTree, Component.Serializer::fromJson).fieldOf("display").forGetter(Dialogue::display),
                SoundEvent.CODEC.fieldOf("sound").forGetter(Dialogue::sound),
                Codec.FLOAT.fieldOf("volume").forGetter(Dialogue::volume),
                SrtManager.CODEC.fieldOf("srt").forGetter(Dialogue::srt)
        ).apply(instance, Dialogue::new));
    }

    public void play(Player player) {
        NetworkHandler.CHANNEL.sendToPlayer(new DialoguePacket(this.id), player);
    }
}
