package earth.terrarium.momento.client.display.types;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.momento.api.SidedValue;
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
        SidedValue padding,
        SidedValue margin,
        Optional<StaticText> text) implements DialogueDisplay {

    public static final Codec<TextureDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TextureData.CODEC.fieldOf("top").forGetter(TextureDisplay::top),
            TextureData.CODEC.fieldOf("background").forGetter(TextureDisplay::background),
            TextureData.CODEC.fieldOf("bottom").forGetter(TextureDisplay::bottom),
            SidedValue.CODEC.fieldOf("padding").orElse(new SidedValue(0)).forGetter(TextureDisplay::padding),
            SidedValue.CODEC.fieldOf("margin").orElse(new SidedValue(0, -80, 0, 0)).forGetter(TextureDisplay::padding),
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
        screenHeight += this.margin.top() + this.margin.bottom();
        int x = (int)((screenWidth / 2f) - (this.top.width() / 2f)) + this.margin.left() + this.margin.right();

        FormattedText formattedText = text.stream().map(FormattedText::of).reduce(FormattedText::composite).orElse(FormattedText.EMPTY);
        List<FormattedCharSequence> lines = ComponentRenderUtils.wrapComponents(formattedText, this.background.width() - this.padding.left() - this.padding.right(), font);
        int height = Mth.ceil((lines.size() * (float)font.lineHeight) / (float)this.background.height());

        int y = screenHeight - (this.top.height() + (height * this.background.height()) + this.padding.top() + this.bottom.height());

        final int topY = y;

        this.top.draw(stack, x, y);
        y += this.top.height();

        for (int i = 0; i < height; i++) {
            this.background.draw(stack, x, y);
            y += this.background.height();
        }
        this.bottom.draw(stack, x, y);

        y = screenHeight - ((height * this.background.height()) + this.bottom.height()) + this.padding.top() + this.background.y();
        int x1 = x + this.background.x() + this.padding.left();
        for (FormattedCharSequence line : lines) {
            font.drawShadow(stack, line, x1, y, 0xFFFFFF);
            y += font.lineHeight;
        }

        this.text.ifPresent(staticText -> staticText.render(minecraft, stack, x, topY));
    }
}
