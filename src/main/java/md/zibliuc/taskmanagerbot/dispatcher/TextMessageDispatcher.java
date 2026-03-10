package md.zibliuc.taskmanagerbot.dispatcher;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.bot.Sender;
import md.zibliuc.taskmanagerbot.command.CommandHandler;
import md.zibliuc.taskmanagerbot.conversation.TaskConversationService;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.IncomingTextMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TextMessageDispatcher {
    private static final Logger LOGGER = LogManager.getLogger(TextMessageDispatcher.class);
    private final TaskConversationService taskConversationService;
    private final Sender sender;
    private final CommandHandler commandHandler;

    public void dispatch(IncomingTextMessage incomingTextMessage) {
        OutgoingMessage outgoingMessage;

        if (commandHandler.supports(incomingTextMessage.text())) {
            LOGGER.debug("Received command -> {}", incomingTextMessage.text());
            outgoingMessage = commandHandler.handle(incomingTextMessage);
        } else {
            outgoingMessage = taskConversationService.handle(incomingTextMessage);
        }

        sender.send(outgoingMessage);
    }
}
