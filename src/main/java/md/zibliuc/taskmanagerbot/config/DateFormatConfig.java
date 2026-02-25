package md.zibliuc.taskmanagerbot.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ui.util.date")
@RequiredArgsConstructor
@Getter
public class DateFormatConfig {
    private final String dateTimeFormat;
    private final String timeFormat;
    private final String dayFormat;
}
