package md.zibliuc.taskmanagerbot.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ui.responses.conversation")
@RequiredArgsConstructor
@Getter
public class ConversationResponseConfig {
    private final String onTitleCorrect;
    private final String onTimeCorrect;
    private final String onTimeIncorrectFormat;
    private final String onTimeError;
}
