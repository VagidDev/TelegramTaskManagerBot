package md.zibliuc.taskmanagerbot.command;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.bot.TelegramSender;
import md.zibliuc.taskmanagerbot.config.CommandConfig;
import md.zibliuc.taskmanagerbot.config.MainMenuConfig;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CommandHandler {
    private final Map<String, ProceedCommand> mainMenuCommands;
    private final Set<String> supportedCommands;
    private final TelegramSender sender;

    public boolean supports(String command) {
        return supportedCommands.contains(command);
    }

    //TODO: delegate to required CommandHandler
    public void handle(IncomingMessage message) {
        ProceedCommand command = mainMenuCommands.get(message.text());
        if (command != null) {
            OutgoingMessage outgoingMessage = command.proceed(message);
            sender.send(outgoingMessage);
        }
    }
}
