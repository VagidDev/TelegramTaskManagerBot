package md.zibliuc.taskmanagerbot.dispatcher;

import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.bot.Sender;
import md.zibliuc.taskmanagerbot.command.CommandHandler;
import md.zibliuc.taskmanagerbot.conversation.TaskConversationService;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.dto.TelegramUserData;
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

    public void dispatch(Message message) {
        TelegramUserData userData = new TelegramUserData(
                message.chat().id(),
                message.from().firstName(),
                message.from().lastName(),
                message.from().username()
        );

        IncomingMessage incomingMessage = new IncomingMessage(
                message.chat().id(),
                message.messageId(),
                message.text(),
                null,
                userData
        );

        OutgoingMessage outgoingMessage;

        if (commandHandler.supports(message.text())) {
            LOGGER.debug("Received command -> {}", incomingMessage.text());
            outgoingMessage = commandHandler.handle(incomingMessage);
        } else {
            outgoingMessage = taskConversationService.handle(incomingMessage);
        }

        sender.send(outgoingMessage);
    }
}
