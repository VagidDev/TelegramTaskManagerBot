package md.zibliuc.taskmanagerbot.config;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.command.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CommandConfig {
    //mainMenu
    private final MainMenuConfig menuConfig;

    private final StartCommand startCommand;
    private final CreateCommand createCommand;
    private final ShowUncompletedTasksCommand showUncompletedTasksCommand;
    private final ShowTasksCommand showTasksCommand;
    private final HelpCommand helpCommand;
    //soon crud menu

    @Bean
    public Map<String, ProceedCommand> getMainMenuConfig() {
        Map<String, ProceedCommand> commands = new HashMap<>();
        //kostili
        commands.put("/start", startCommand);
        commands.put(menuConfig.getCreateCommand(), createCommand);
        commands.put(menuConfig.getUncompletedCommand(), showUncompletedTasksCommand);
        commands.put(menuConfig.getAllCommand(), showTasksCommand);
        commands.put(menuConfig.getHelpCommand(), helpCommand);

        return commands;
    }

}
