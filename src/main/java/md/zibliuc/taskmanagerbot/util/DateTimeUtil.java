package md.zibliuc.taskmanagerbot.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.config.DateFormatConfig;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class DateTimeUtil {
    private static final Locale LOCALE = new Locale("RU");
    private final DateFormatConfig dateFormatConfig;

    private DateTimeFormatter dateTimeFormatter;
    private DateTimeFormatter dayFormatter;
    private DateTimeFormatter timeFormatter;

    @PostConstruct
    void init() {
        dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormatConfig.getDateTimeFormat(), LOCALE);
        dayFormatter = DateTimeFormatter.ofPattern(dateFormatConfig.getDayFormat(), LOCALE);
        timeFormatter =  DateTimeFormatter.ofPattern(dateFormatConfig.getTimeFormat(), LOCALE);
    }

    public String parseDateTimeToDateTimeString(LocalDateTime localDateTime) {
        return dateTimeFormatter.format(localDateTime);
    }

    public String parseDateToDayString(LocalDate localDate) {
        return dayFormatter.format(localDate);
    }

    public String parseDateTimeToTimeString(LocalDateTime localDateTime) {
        return timeFormatter.format(localDateTime);
    }
}
