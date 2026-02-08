package md.zibliuc.taskmanagerbot.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Set;


@ConfigurationProperties(prefix = "ui.menu.main")
@RequiredArgsConstructor
@Getter
public class MainMenuConfig {
    private final String createCommand;
    private final String allCommand;
    private final String uncompletedCommand;
    private final String helpCommand;

    @Bean
    public Set<String> supportedCommands() {
        //мне стыдно за этот костыль, но а кому сейчас легко?
        return Set.of("/start",createCommand, allCommand, uncompletedCommand, helpCommand);
    }
}
