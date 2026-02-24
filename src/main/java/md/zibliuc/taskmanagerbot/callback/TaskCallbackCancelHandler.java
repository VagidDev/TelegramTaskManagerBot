package md.zibliuc.taskmanagerbot.callback;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.config.CallbackResponseConfig;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.service.KeyboardService;
import md.zibliuc.taskmanagerbot.service.UserConversationStateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskCallbackCancelHandler {
    private static final Logger LOGGER = LogManager.getLogger(TaskCallbackCancelHandler.class);

    private final CallbackResponseConfig callbackResponseConfig;
    private final UserConversationStateService userConversationStateService;
    private final KeyboardService keyboardService;

    public OutgoingMessage handle(IncomingMessage incomingMessage) {
        userConversationStateService.reset(incomingMessage.chatId());
        return OutgoingMessage
                .send(incomingMessage.chatId(), callbackResponseConfig.getCancel())
                .keyboard(keyboardService.menuKeyboard());
    }
}
