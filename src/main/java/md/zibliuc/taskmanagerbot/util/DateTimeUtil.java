package md.zibliuc.taskmanagerbot.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm");

    public static String parseToString(LocalDateTime localDateTime) {
        return DATE_TIME_FORMATTER.format(localDateTime);
    }
}
