package earth.terrarium.momento.client.display.types;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.momento.api.TextureData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.Optional;

public record TextureDisplay(
        TextureData top, TextureData background, TextureData bottom,
        int padding, Optional<StaticText> text) implements DialogueDisplay {

    public static final Codec<TextureDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TextureData.CODEC.fieldOf("top").forGetter(TextureDisplay::top),
            TextureData.CODEC.fieldOf("background").forGetter(TextureDisplay::background),
            TextureData.CODEC.fieldOf("bottom").forGetter(TextureDisplay::bottom),
            Codec.INT.fieldOf("padding").orElse(0).forGetter(TextureDisplay::padding),
            StaticText.CODEC.optionalFieldOf("text").forGetter(TextureDisplay::text)
    ).apply(instance, TextureDisplay::new));

    @Override
    public String id() {
        return "texture";
    }

    @Override
    public void render(Minecraft minecraft, float partialTicks, PoseStack stack, List<String> text) {
        final Font font = minecraft.font;
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        screenHeight -= 90;
        int x = (int)((screenWidth / 2f) - (this.top.width() / 2f));

        FormattedText formattedText = text.stream().map(FormattedText::of).reduce(FormattedText::composite).orElse(FormattedText.EMPTY);
        List<FormattedCharSequence> lines = ComponentRenderUtils.wrapComponents(formattedText, this.background.width() - this.padding * 2, font);
        int height = Mth.ceil((lines.size() * (float)font.lineHeight) / (float)this.background.height());

        int y = screenHeight - (this.top.height() + (height * this.background.height()) + this.bottom.height());

        final int topY = y;

        this.top.draw(stack, x, y);
        y += this.top.height();

        for (int i = 0; i < height; i++) {
            this.background.draw(stack, x, y);
            y += this.background.height();
        }
        this.bottom.draw(stack, x, y);

        y = screenHeight - ((height * this.background.height()) + this.bottom.height()) + this.padding + this.background.y();
        int x1 = x + this.background.x() + this.padding;
        for (FormattedCharSequence line : lines) {
            font.drawShadow(stack, line, x1, y, 0xFFFFFF);
            y += font.lineHeight;
        }

        this.text.ifPresent(staticText -> staticText.render(minecraft, stack, x, topY));
    }
}
