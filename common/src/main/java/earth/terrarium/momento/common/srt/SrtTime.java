package earth.terrarium.momento.common.srt;

/**
 * The start and end time of an SRT block.
 *
 * @param start The start time in milliseconds
 * @param end   The end time in milliseconds
 */
public record SrtTime(int start, int end) {
}
