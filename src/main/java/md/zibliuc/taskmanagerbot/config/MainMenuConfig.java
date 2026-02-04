package md.zibliuc.taskmanagerbot.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "ui.menu.main")
@RequiredArgsConstructor
@Getter
public class MainMenuConfig {
    private final String create;
    private final String all;
    private final String uncompleted;
    private final String help;
}
