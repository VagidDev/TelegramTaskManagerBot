package md.zibliuc.taskmanagerbot.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ui.responses.command")
@RequiredArgsConstructor
@Getter
public class CommandResponseConfig {
    private final String start;
    private final String create;
    private final String help;
    private final String showTasks;
    private final String showTasksEmpty;
    private final String showTasksError;
    private final String showUncompletedTasks;
    private final String showUncompletedTasksEmpty;
    private final String showUncompletedTasksError;
}
