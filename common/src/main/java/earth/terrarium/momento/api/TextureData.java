package earth.terrarium.momento.api;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.client.utils.RenderUtils;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

public record TextureData(
        int x, int y,
        int u, int v,
        int width, int height,
        int textureWidth, int textureHeight,
        ResourceLocation texture
) {

    public static final Codec<TextureData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").orElse(0).forGetter(TextureData::x),
            Codec.INT.fieldOf("y").orElse(0).forGetter(TextureData::y),
            Codec.INT.fieldOf("u").orElse(0).forGetter(TextureData::u),
            Codec.INT.fieldOf("v").orElse(0).forGetter(TextureData::v),
            Codec.INT.fieldOf("width").orElse(256).forGetter(TextureData::width),
            Codec.INT.fieldOf("height").orElse(256).forGetter(TextureData::height),
            Codec.INT.fieldOf("textureWidth").orElse(256).forGetter(TextureData::textureWidth),
            Codec.INT.fieldOf("textureHeight").orElse(256).forGetter(TextureData::textureHeight),
            ResourceLocation.CODEC.fieldOf("texture").forGetter(TextureData::texture)
    ).apply(instance, TextureData::new));

    public void draw(PoseStack stack, int x, int y) {
        RenderSystem.enableBlend();
        RenderUtils.bindTexture(this.texture);
        GuiComponent.blit(stack,
                x + this.x, y + this.y,
                this.u, this.v,
                this.width, this.height,
                this.textureWidth, this.textureHeight
        );
        RenderSystem.disableBlend();
    }
}
