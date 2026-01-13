package md.zibliuc.taskmanagerbot.handler;

import com.pengrad.telegrambot.model.Update;
import md.zibliuc.taskmanagerbot.service.MessageService;
import org.springframework.stereotype.Component;

@Component
public class MessageHandler {
    private final MessageService messageService;

    public MessageHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    public void handle(Update update) {
        if (update.message() != null && update.message().text() != null) {
            messageService.processTextMessage(update);
        } else if (update.callbackQuery() != null) {
            messageService.proceedCallbackQuery(update);
        }

    }
}
