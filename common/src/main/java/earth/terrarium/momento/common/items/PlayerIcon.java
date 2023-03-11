package earth.terrarium.momento.common.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.Locale;

public enum PlayerIcon {
    BRASS_TAPE_RECORDER(0.1f),
    HAND_HELD_SPEAKER(0.2f),
    TEAL_PHONOGRAPH(0.3f),
    WOODEN_RADIO(0.4f),
    PURPLE_PHONOGRAPH(0.5f);

    public static final Codec<PlayerIcon> CODEC = Codec.STRING.comapFlatMap(
            s -> {
                try {
                    return DataResult.success(PlayerIcon.valueOf(s.toUpperCase(Locale.ROOT)));
                } catch (Exception e) {
                    return DataResult.error("Unknown PlayerIcon: " + s);
                }
            },
            PlayerIcon::name);

    private final float value;

    PlayerIcon(float value) {
        this.value = value;
    }

    public static PlayerIcon fromValue(String id) {
        for (PlayerIcon icon : values()) {
            if (icon.name().equals(id.toUpperCase(Locale.ROOT))) {
                return icon;
            }
        }
        return BRASS_TAPE_RECORDER;
    }

    public float getValue() {
        return value;
    }
}
