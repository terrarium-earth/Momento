package earth.terrarium.momento.api;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record SidedValue(int top, int bottom, int left, int right) {

    private static final Codec<SidedValue> SINGLE_CODEC = Codec.INT.xmap(SidedValue::new, SidedValue::top);
    private static final Codec<SidedValue> LIST_CODEC = Codec.INT.listOf().xmap(
            list -> new SidedValue(list.get(0), list.get(1), list.get(2), list.get(3)),
            padding -> List.of(padding.top(), padding.bottom(), padding.left(), padding.right())
    );
    private static final Codec<SidedValue> OBJ_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("top").orElse(0).forGetter(SidedValue::top),
            Codec.INT.fieldOf("bottom").orElse(0).forGetter(SidedValue::bottom),
            Codec.INT.fieldOf("left").orElse(0).forGetter(SidedValue::left),
            Codec.INT.fieldOf("right").orElse(0).forGetter(SidedValue::right)
    ).apply(instance, SidedValue::new));

    public static final Codec<SidedValue> CODEC = Codec.either(SINGLE_CODEC, Codec.either(LIST_CODEC, OBJ_CODEC)).xmap(
            either -> either.map(
                value -> value,
                either1 -> either1.map(
                        value -> value,
                        value -> value
                )
            ),
            value -> Either.right(Either.left(value))
    );

    public SidedValue(int all) {
        this(all, all, all, all);
    }
}
