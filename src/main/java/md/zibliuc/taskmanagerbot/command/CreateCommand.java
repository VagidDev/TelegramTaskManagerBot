package md.zibliuc.taskmanagerbot.command;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.conversation.ConversationState;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.keyboard.KeyboardService;
import md.zibliuc.taskmanagerbot.service.UserConversationStateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateCommand implements ProceedCommand {
    private static final Logger LOGGER = LogManager.getLogger(CreateCommand.class);

    private final UserConversationStateService conversationStateService;
    private final KeyboardService keyboardService;

    @Override
    public OutgoingMessage proceed(IncomingMessage message) {
        conversationStateService.get(message.chatId()).setState(ConversationState.WAITING_TITLE);
        LOGGER.info("Changing context for chat id {} to {}",
                message.chatId(),
                conversationStateService.get(message.chatId()));
        return OutgoingMessage
                .send(message.chatId(), "Введите название задачи:")
                .keyboard(keyboardService.removeKeyboard());
    }
}
