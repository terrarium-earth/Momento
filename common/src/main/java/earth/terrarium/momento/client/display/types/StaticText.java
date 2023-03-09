package earth.terrarium.momento.client.display.types;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public record StaticText(MutableComponent text, int x, int y) {

    public static final Codec<StaticText> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecExtras.passthrough(Component.Serializer::toJsonTree, Component.Serializer::fromJson).fieldOf("text").forGetter(StaticText::text),
            Codec.INT.fieldOf("x").orElse(0).forGetter(StaticText::x),
            Codec.INT.fieldOf("y").orElse(0).forGetter(StaticText::y)
    ).apply(instance, StaticText::new));

    public void render(Minecraft minecraft, PoseStack stack, int x, int y) {
        minecraft.font.drawShadow(stack, text, x + this.x, y + this.y, 0xFFFFFF);
    }
}
