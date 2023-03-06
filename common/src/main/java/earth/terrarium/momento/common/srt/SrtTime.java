package earth.terrarium.momento.common.srt;

import net.minecraft.util.Mth;

/**
 * The start and end time of an SRT block.
 * @param start The start time in milliseconds
 * @param end The end time in milliseconds
 */
public record SrtTime(int start, int end) {

    /**
     * @return The start time in ticks, rounded down to the nearest tick.
     */
    public int startAsTicks() {
        return Mth.floor(start / 50f);
    }

    /**
     * @return The end time in ticks, rounded up to the nearest tick.
     */
    public int endAsTicks() {
        return Mth.ceil(end / 50f);
    }
}
