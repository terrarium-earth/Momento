package earth.terrarium.momento.common.srt;

/**
 * Represents a single block of an SRT file.
 * @param id The block ID
 * @param time The start and end time of the block
 * @param text The subtitle text
 */
public record SrtBlock(int id, SrtTime time, String text) {

    @Override
    public String toString() {
        return id + "\n" + time + "\n" + text;
    }
}
