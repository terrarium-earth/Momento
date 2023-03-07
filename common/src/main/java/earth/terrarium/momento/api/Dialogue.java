package earth.terrarium.momento.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import earth.terrarium.momento.common.managers.SrtManager;
import earth.terrarium.momento.common.srt.SrtFile;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;

public record Dialogue(MutableComponent display, SoundEvent sound, SrtFile srt) {

    public static final Codec<Dialogue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecExtras.passthrough(Component.Serializer::toJsonTree, Component.Serializer::fromJson).fieldOf("display").forGetter(Dialogue::display),
            SoundEvent.CODEC.fieldOf("sound").forGetter(Dialogue::sound),
            SrtManager.CODEC.fieldOf("srt").forGetter(Dialogue::srt)
    ).apply(instance, Dialogue::new));
}
