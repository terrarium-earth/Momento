package earth.terrarium.momento.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import earth.terrarium.momento.client.display.types.CanvasDisplay;
import earth.terrarium.momento.client.display.types.DialogueDisplay;
import earth.terrarium.momento.client.display.types.Displays;
import earth.terrarium.momento.common.items.PlayerIcon;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public record Dialogue(
        ResourceLocation id,
        MutableComponent name, SoundEvent sound, float volume,
        ResourceLocation srt,
        DialogueDisplay display, PlayerIcon icon) {

    public static Codec<Dialogue> codec(ResourceLocation id) {
        return RecordCodecBuilder.create(instance -> instance.group(
                RecordCodecBuilder.point(id),
                CodecExtras.passthrough(Component.Serializer::toJsonTree, Component.Serializer::fromJson).fieldOf("name").forGetter(Dialogue::name),
                SoundEvent.CODEC.fieldOf("sound").forGetter(Dialogue::sound),
                Codec.FLOAT.fieldOf("volume").forGetter(Dialogue::volume),
                ResourceLocation.CODEC.fieldOf("srt").forGetter(Dialogue::srt),
                Displays.CODEC.fieldOf("display").orElse(CanvasDisplay.DEFAULT).forGetter(Dialogue::display),
                PlayerIcon.CODEC.fieldOf("icon").orElse(PlayerIcon.BRASS_TAPE_RECORDER).forGetter(Dialogue::icon)
        ).apply(instance, Dialogue::new));
    }
}
