package earth.terrarium.momento.common.srt;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class SrtParser {

    private static final Pattern TIME_PATTERN = Pattern.compile("(?<shr>\\d{2}):(?<smn>\\d{2}):(?<ssc>\\d{2}),(?<smm>\\d{3}) --> (?<ehr>\\d{2}):(?<emn>\\d{2}):(?<esc>\\d{2}),(?<emm>\\d{3})");

    /*
        Example SRT file:
        1
        00:00:03,400 --> 00:00:06,177
        This is a subtitle file.

        2
        00:00:06,177 --> 00:00:10,009
        It can support multiple lines,
        as well as multiple blocks.

        3
        00:00:10,009 --> 00:00:13,655
        It is a very simple format,
        and is easy to parse.
     */

    public static List<SrtBlock> parse(String data) throws SrtSyntaxException {
        return parse(data.lines());
    }

    public static List<SrtBlock> parse(Stream<String> lines) throws SrtSyntaxException {
        return parse(lines.toList());
    }

    public static List<SrtBlock> parse(List<String> lines) throws SrtSyntaxException {
        List<String> copy = new ArrayList<>(lines);
        copy.add("");
        List<SrtBlock> sections = new ArrayList<>();
        int i = 0;
        int id = -1;
        SrtTime time = null;
        StringBuilder text = null;

        try {
            while (i < copy.size()) {
                String line = copy.get(i);
                if (line.isBlank()) {
                    assert !(id == -1 || time == null || text == null);
                    sections.add(new SrtBlock(id, time, text.toString()));
                    id = -1;
                    time = null;
                    text = null;
                } else if (id == -1) {
                    id = Integer.parseInt(line);
                } else if (time == null) {
                    time = parseTime(line);
                } else if (text == null) {
                    text = new StringBuilder(line);
                } else {
                    text.append("\n").append(line);
                }
                i++;
            }
        } catch (Exception e) {
            if (e instanceof SrtSyntaxException) {
                throw new SrtSyntaxException(i + 1, copy.get(i), e);
            }
            throw new SrtSyntaxException(i + 1, copy.get(i));
        }
        return sections;
    }

    private static SrtTime parseTime(String time) {
        Matcher matcher = TIME_PATTERN.matcher(time);
        if (!matcher.matches()) {
            throw new SrtSyntaxException("Invalid time format");
        }
        int start = Integer.parseInt(matcher.group("shr")) * 3600000 +
                Integer.parseInt(matcher.group("smn")) * 60000 +
                Integer.parseInt(matcher.group("ssc")) * 1000 +
                Integer.parseInt(matcher.group("smm"));
        int end = Integer.parseInt(matcher.group("ehr")) * 3600000 +
                Integer.parseInt(matcher.group("emn")) * 60000 +
                Integer.parseInt(matcher.group("esc")) * 1000 +
                Integer.parseInt(matcher.group("emm"));
        return new SrtTime(start, end);
    }
}
