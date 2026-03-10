package md.zibliuc.taskmanagerbot.command;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.config.CommandResponseConfig;
import md.zibliuc.taskmanagerbot.conversation.ConversationState;
import md.zibliuc.taskmanagerbot.dto.IncomingTextMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.service.KeyboardService;
import md.zibliuc.taskmanagerbot.service.UserConversationStateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateCommand implements ProceedCommand {
    private static final Logger LOGGER = LogManager.getLogger(CreateCommand.class);

    private final UserConversationStateService conversationStateService;
    private final CommandResponseConfig commandResponseConfig;
    private final KeyboardService keyboardService;

    @Override
    public OutgoingMessage proceed(IncomingTextMessage incomingTextMessage) {
        conversationStateService.get(incomingTextMessage.chatId()).setState(ConversationState.WAITING_TITLE);
        LOGGER.info("Changing context for chat id {} to {}",
                incomingTextMessage.chatId(),
                conversationStateService.get(incomingTextMessage.chatId()));
        return OutgoingMessage
                .send(incomingTextMessage.chatId(), commandResponseConfig.getCreate())
                .keyboard(keyboardService.removeKeyboard());
    }
}
