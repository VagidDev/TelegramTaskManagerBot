package md.zibliuc.taskmanagerbot.callback;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.conversation.ConversationContext;
import md.zibliuc.taskmanagerbot.conversation.ConversationState;
import md.zibliuc.taskmanagerbot.dto.CallbackData;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
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
    private final KeyboardService keyboardService;

    public OutgoingMessage handle(IncomingMessage incomingMessage) {
        CallbackType callbackType = incomingMessage.callbackData().type();

        return switch (callbackType) {
            case DATE -> onDateSelected(incomingMessage);
            case DATE_FORWARD -> onDateForward(incomingMessage);
            case DATE_BACKWARD -> onDateBackward(incomingMessage);
            default -> {
                LOGGER.error(
                        "Unexpected callback type received. Callback -> {} | Message -> {}",
                        callbackType,
                        incomingMessage.text()
                );
                yield OutgoingMessage.send(incomingMessage.chatId(), "Я вообще этот запрос обрабатывать не буду");
            }
        };
    }

    private OutgoingMessage onDateSelected(IncomingMessage incomingMessage) {
        //TODO: handle choice of task
        ConversationContext ctx = conversationStateService.get(incomingMessage.chatId());
        CallbackData callbackData = incomingMessage.callbackData();
        if (ctx.getState() == ConversationState.WAITING_DATE) {
            ctx.setDate(callbackData.asDate());
            ctx.setState(ConversationState.WAITING_TIME);
            return OutgoingMessage.edit(
                    incomingMessage.chatId(),
                    incomingMessage.messageId(),
                    "Введите время (HH:mm)"
            );
        }
        conversationStateService.reset(incomingMessage.chatId());
        return OutgoingMessage.send(incomingMessage.chatId(), "Не понятно что ты хочешь от меня...");
    }

    private OutgoingMessage onDateForward(IncomingMessage incomingMessage) {
        LocalDate date = incomingMessage.callbackData().asDate();
        return OutgoingMessage.edit(
                incomingMessage.chatId(),
                incomingMessage.messageId(),
                "Выберите дату"
        ).keyboard(keyboardService.dateRangeKeyboard(date, STEP_OF_DAYS));
    }

    private OutgoingMessage onDateBackward(IncomingMessage incomingMessage) {
        LocalDate date = incomingMessage.callbackData().asDate();
        return OutgoingMessage.edit(
                incomingMessage.chatId(),
                incomingMessage.messageId(),
                "Выберите дату"
        ).keyboard(keyboardService.dateRangeKeyboard(date, STEP_OF_DAYS * -1));
    }
}
