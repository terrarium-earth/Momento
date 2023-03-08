package earth.terrarium.momento.client.display.types;

import com.mojang.serialization.Codec;

public class Displays {

    public static final Codec<DialogueDisplay> CODEC = Codec.STRING.dispatch(DialogueDisplay::id, Displays::get);

    private static Codec<? extends DialogueDisplay> get(String id) {
        return switch (id) {
            case "canvas" -> CanvasDisplay.CODEC;
            default -> throw new IllegalArgumentException("Unknown display type: " + id);
        };
    }
}
