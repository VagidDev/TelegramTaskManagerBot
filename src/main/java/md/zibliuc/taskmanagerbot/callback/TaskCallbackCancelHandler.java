package md.zibliuc.taskmanagerbot.callback;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.bot.TelegramSender;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.keyboard.KeyboardService;
import md.zibliuc.taskmanagerbot.service.UserConversationStateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskCallbackCancelHandler {
    private static final Logger LOGGER = LogManager.getLogger(TaskCallbackCancelHandler.class);

    private final TelegramSender telegramSender;
    private final UserConversationStateService userConversationStateService;
    private final KeyboardService keyboardService;

    public OutgoingMessage handle(IncomingMessage incomingMessage) {
        userConversationStateService.reset(incomingMessage.chatId());
        // Deleting message
        telegramSender.send(OutgoingMessage.delete(incomingMessage.chatId(), incomingMessage.messageId()));
        return OutgoingMessage
                .send(incomingMessage.chatId(), "Галя отмена...")
                .keyboard(keyboardService.menuKeyboard());
    }
}
