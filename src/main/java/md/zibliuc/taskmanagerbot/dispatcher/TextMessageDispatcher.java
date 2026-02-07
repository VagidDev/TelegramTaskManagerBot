package md.zibliuc.taskmanagerbot.dispatcher;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.command.CommandHandler;
import md.zibliuc.taskmanagerbot.conversation.TaskConversationService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TextMessageDispatcher {
    private final CommandHandler commandHandler;
    private final TaskConversationService taskConversationService;

    //TODO: check is it command or creating task flow; delegate to CommandHandler or to TaskConversationService
    public void dispatch(Update update) {
        Long chatId = update.message().chat().id();
        String text = update.message().text();

        if (commandHandler.supports(text)) {
            commandHandler.handle(chatId, text);
            return;
        }

        taskConversationService.handle(chatId, text);
    }
}
