package md.zibliuc.taskmanagerbot.command;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.config.CommandConfig;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.IncomingTextMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CommandHandler {
    private static final Logger LOGGER = LogManager.getLogger(CommandHandler.class);
    private final CommandConfig commandConfig;
    private final Set<String> supportedCommands;

    public boolean supports(String command) {
        return supportedCommands.contains(command);
    }

    //TODO: delegate to required CommandHandler
    public OutgoingMessage handle(IncomingTextMessage incomingTextMessage) {
        ProceedCommand command = commandConfig
                .getMainMenuConfig()
                .get(incomingTextMessage.text());
        if (command != null) {
            LOGGER.info(
                    "Processing command -> {} for input -> {}",
                    command.getClass().getSimpleName(),
                    incomingTextMessage.text()
            );
            OutgoingMessage outgoingMessage = command.proceed(incomingTextMessage);
            LOGGER.debug("Got message to send -> {}", outgoingMessage.getText());
            return outgoingMessage;
        } else {
            LOGGER.error("Cannot get command to proceed from user input -> {}", incomingTextMessage.text());
            return null;
        }
    }
}
