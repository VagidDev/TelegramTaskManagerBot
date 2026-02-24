package md.zibliuc.taskmanagerbot.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ui.menu.date")
@RequiredArgsConstructor
@Getter
public class DateMenuConfig {
    private final String today;
    private final String forward;
    private final String backward;
    private final String cancel;
}
