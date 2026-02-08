package md.zibliuc.taskmanagerbot.dispatcher;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.command.CommandHandler;
import md.zibliuc.taskmanagerbot.conversation.TaskConversationService;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.TelegramUserData;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TextMessageDispatcher {
    private final CommandHandler commandHandler;
    private final TaskConversationService taskConversationService;

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
                userData
        );

        if (commandHandler.supports(message.text())) {
            commandHandler.handle(incomingMessage);
            return;
        }

        taskConversationService.handle(incomingMessage);
    }

    //TODO: check is it command or creating task flow; delegate to CommandHandler or to TaskConversationService
    /*public void dispatch(Update update) {
        Long chatId = update.message().chat().id();
        String text = update.message().text();

        if (commandHandler.supports(text)) {
            commandHandler.handle(chatId, text);
            return;
        }

        taskConversationService.handle(chatId, text);
    }*/
}
