package earth.terrarium.momento.client.display.types;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.color.Color;
import com.teamresourceful.resourcefullib.common.color.ConstantColors;
import earth.terrarium.momento.api.SidedValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public record CanvasDisplay(Color color, int alpha, int width, SidedValue padding) implements DialogueDisplay {

    public static final CanvasDisplay DEFAULT = new CanvasDisplay(ConstantColors.black, 128, 50, new SidedValue(10));

    public static final Codec<CanvasDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Color.CODEC.fieldOf("color").forGetter(CanvasDisplay::color),
            Codec.intRange(0, 255).fieldOf("alpha").orElse(128).forGetter(CanvasDisplay::width),
            Codec.intRange(0, 100).fieldOf("width").forGetter(CanvasDisplay::width),
            SidedValue.CODEC.fieldOf("padding").forGetter(CanvasDisplay::padding)
    ).apply(instance, CanvasDisplay::new));

    @Override
    public String id() {
        return "canvas";
    }

    @Override
    public void render(Minecraft minecraft, float partialTicks, PoseStack stack, List<String> text) {
        final Font font = minecraft.font;
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        screenHeight -= 90;
        int fullWidth = (int)(screenWidth * (width / 100f));
        int xPos = (int)((screenWidth / 2f) - (fullWidth / 2f));
        FormattedText formattedText = text.stream().map(FormattedText::of).reduce(FormattedText::composite).orElse(FormattedText.EMPTY);
        List<FormattedCharSequence> lines = ComponentRenderUtils.wrapComponents(formattedText, fullWidth, font);
        int height = lines.size() * font.lineHeight;

        GuiComponent.fill(stack, xPos - padding.left(), screenHeight - (height + padding.top()), xPos + fullWidth + padding.right(), screenHeight + padding.bottom(), getColor());
        for (FormattedCharSequence line : lines) {
            font.drawShadow(stack, line, xPos, screenHeight - height, 0xFFFFFF);
            height -= font.lineHeight;
        }
    }

    private int getColor() {
        int r = color.getIntRed();
        int g = color.getIntGreen();
        int b = color.getIntBlue();
        return (alpha << 24) | (r << 16) | (g << 8) | b;
    }
}
