package md.zibliuc.taskmanagerbot.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "ui.menu.crud")
@RequiredArgsConstructor
@Getter
public class CrudMenuConfig {
    private final String complete;
    private final String delete;
    private final String edit;
    private final String cancel;
    private final String postpone;
}
