package md.zibliuc.taskmanagerbot.command;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.config.MainMenuConfig;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CommandHandler {
    private final MainMenuConfig mainMenuConfig;
    private final Set<String> supportedCommands;

    public boolean supports(String command) {
        return supportedCommands.contains(command);
    }

    //TODO: delegate to required CommandHandler
    public void handle(Long chatId, String text) {
        switch (text) {
            //TODO: create logic for identifying command from config
            //case mainMenuConfig.getAllCommand() ->
        }
    }
}
