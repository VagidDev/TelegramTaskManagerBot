package md.zibliuc.taskmanagerbot.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ui.responses.notification")
@RequiredArgsConstructor
@Getter
public class NotificationResponseConfig {
    private final String sendTaskNotification;
}
