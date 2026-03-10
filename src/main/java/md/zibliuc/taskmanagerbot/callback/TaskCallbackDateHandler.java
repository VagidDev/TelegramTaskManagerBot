package md.zibliuc.taskmanagerbot.callback;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.config.CallbackResponseConfig;
import md.zibliuc.taskmanagerbot.conversation.ConversationContext;
import md.zibliuc.taskmanagerbot.conversation.ConversationState;
import md.zibliuc.taskmanagerbot.dto.CallbackData;
import md.zibliuc.taskmanagerbot.dto.IncomingCallbackMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.service.KeyboardService;
import md.zibliuc.taskmanagerbot.service.UserConversationStateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TaskCallbackDateHandler {
    private static final Logger LOGGER = LogManager.getLogger(TaskCallbackDateHandler.class);
    private static final int STEP_OF_DAYS = 3;

    private final UserConversationStateService conversationStateService;
    private final CallbackResponseConfig callbackResponseConfig;
    private final KeyboardService keyboardService;

    public OutgoingMessage handle(IncomingCallbackMessage incomingCallbackMessage) {
        CallbackType callbackType = incomingCallbackMessage.callbackData().type();

        return switch (callbackType) {
            case DATE -> onDateSelected(incomingCallbackMessage);
            case DATE_FORWARD -> onDateForward(incomingCallbackMessage);
            case DATE_BACKWARD -> onDateBackward(incomingCallbackMessage);
            default -> {
                LOGGER.error(
                        "Unexpected callback type received. Callback -> {} | Message -> {}",
                        callbackType,
                        incomingCallbackMessage.text()
                );
                yield OutgoingMessage.send(
                        incomingCallbackMessage.chatId(),
                        callbackResponseConfig.getDatePickingError()
                );
            }
        };
    }

    private OutgoingMessage onDateSelected(IncomingCallbackMessage incomingCallbackMessage) {
        //TODO: handle choice of task
        ConversationContext ctx = conversationStateService.get(incomingCallbackMessage.chatId());
        CallbackData callbackData = incomingCallbackMessage.callbackData();
        if (ctx.getState() == ConversationState.WAITING_DATE) {
            ctx.setDate(callbackData.asDate());
            ctx.setState(ConversationState.WAITING_TIME);
            return OutgoingMessage.edit(
                    incomingCallbackMessage.chatId(),
                    incomingCallbackMessage.messageId(),
                    "Введите время (HH:mm)"
            );
        }
        conversationStateService.reset(incomingCallbackMessage.chatId());
        return OutgoingMessage.send(incomingCallbackMessage.chatId(), "Не понятно что ты хочешь от меня...");
    }

    private OutgoingMessage onDateForward(IncomingCallbackMessage incomingCallbackMessage) {
        LocalDate date = incomingCallbackMessage.callbackData().asDate();
        return OutgoingMessage.edit(
                incomingCallbackMessage.chatId(),
                incomingCallbackMessage.messageId(),
                callbackResponseConfig.getDatePicking()
        ).keyboard(keyboardService.dateRangeKeyboard(date, STEP_OF_DAYS));
    }

    private OutgoingMessage onDateBackward(IncomingCallbackMessage incomingCallbackMessage) {
        LocalDate date = incomingCallbackMessage.callbackData().asDate();
        return OutgoingMessage.edit(
                incomingCallbackMessage.chatId(),
                incomingCallbackMessage.messageId(),
                callbackResponseConfig.getDatePicking()
        ).keyboard(keyboardService.dateRangeKeyboard(date, STEP_OF_DAYS * -1));
    }
}
