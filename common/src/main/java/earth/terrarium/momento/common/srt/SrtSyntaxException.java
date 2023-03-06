package earth.terrarium.momento.common.srt;

public class SrtSyntaxException extends RuntimeException {

    public SrtSyntaxException(String message) {
        super(message);
    }

    public SrtSyntaxException(int line, String message) {
        super(String.format("Error parsing line [%d]: '%s'", line, message));
    }

    public SrtSyntaxException(int line, String message, Throwable cause) {
        super(String.format("Error parsing line [%d]: '%s'", line, message), cause);
    }
}
