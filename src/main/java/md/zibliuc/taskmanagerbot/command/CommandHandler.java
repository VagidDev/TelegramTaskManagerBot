package md.zibliuc.taskmanagerbot.command;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.bot.TelegramSender;
import md.zibliuc.taskmanagerbot.config.CommandConfig;
import md.zibliuc.taskmanagerbot.config.MainMenuConfig;
import md.zibliuc.taskmanagerbot.dispatcher.TextMessageDispatcher;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CommandHandler {
    private static final Logger LOGGER = LogManager.getLogger(CommandHandler.class);
    private final CommandConfig commandConfig;
    private final Set<String> supportedCommands;
    private final TelegramSender sender;

    public boolean supports(String command) {
        return supportedCommands.contains(command);
    }

    //TODO: delegate to required CommandHandler
    public void handle(IncomingMessage message) {
        ProceedCommand command = commandConfig
                .getMainMenuConfig()
                .get(message.text());
        if (command != null) {
            LOGGER.info(
                    "Processing command -> {} for input -> {}",
                    command.getClass().getSimpleName(),
                    message.text()
            );
            OutgoingMessage outgoingMessage = command.proceed(message);
            LOGGER.debug("Got message to send -> {}", outgoingMessage.getText());
            sender.send(outgoingMessage);
        } else {
            LOGGER.warn("Received null for input {}", message.text());
        }
    }
}
