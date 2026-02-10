package md.zibliuc.taskmanagerbot.conversation;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.bot.TelegramSender;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.keyboard.KeyboardService;
import md.zibliuc.taskmanagerbot.service.TaskService;
import md.zibliuc.taskmanagerbot.service.UserConversationStateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TaskConversationService {
    private static final Logger LOGGER = LogManager.getLogger(TaskConversationService.class);

    private final UserConversationStateService conversationStateService;
    private final TelegramSender telegramSender;
    private final KeyboardService keyboardService;
    private final TaskService taskService;

    public void handle(IncomingMessage message) {
        LOGGER.info("Received message {}", message);
        ConversationContext ctx = conversationStateService.get(message.chatId());

        OutgoingMessage outgoingMessage = switch (ctx.getState()) {
            case WAITING_TITLE -> onTitle(message.chatId(), message.text(), ctx);
            //case WAITING_TIME -> onTime();
            default -> OutgoingMessage
                    .send(message.chatId(),"Unexpected value: " + ctx.getState())
                    .keyboard(keyboardService.menuKeyboard());
        };

        telegramSender.send(outgoingMessage);
    }

    public OutgoingMessage onTitle(Long chatId, String text, ConversationContext ctx) {
        ctx.setTitle(text);
        ctx.setState(ConversationState.WAITING_DATE);
        return OutgoingMessage
                .send(chatId, "Выберите дату")
                .keyboard(keyboardService.dateKeyboard());
    }
    // Will be at callback processor
    // public void onDate(Long chatId, LocalDate date) {}
    public OutgoingMessage onTime(Long chatId, LocalTime time) {
        return null;
    }

}
