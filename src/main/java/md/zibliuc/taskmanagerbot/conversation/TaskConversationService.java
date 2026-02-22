package md.zibliuc.taskmanagerbot.conversation;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.bot.TelegramSender;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.service.KeyboardService;
import md.zibliuc.taskmanagerbot.service.TaskService;
import md.zibliuc.taskmanagerbot.service.TimeValidationService;
import md.zibliuc.taskmanagerbot.service.UserConversationStateService;
import md.zibliuc.taskmanagerbot.util.DateTimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TaskConversationService {
    private static final Logger LOGGER = LogManager.getLogger(TaskConversationService.class);

    private final UserConversationStateService conversationStateService;
    private final TimeValidationService timeValidationService;
    private final TelegramSender telegramSender;
    private final KeyboardService keyboardService;
    private final TaskService taskService;

    public void handle(IncomingMessage message) {
        LOGGER.info("Received message {}", message);
        ConversationContext ctx = conversationStateService.get(message.chatId());

        OutgoingMessage outgoingMessage = switch (ctx.getState()) {
            case WAITING_TITLE -> onTitle(message.chatId(), message.text(), ctx);
            case WAITING_TIME -> onTime(message.chatId(), message.text(), ctx);
            default -> {
                conversationStateService.reset(message.chatId());
                yield OutgoingMessage
                    .send(message.chatId(),"Unexpected value: " + ctx.getState())
                    .keyboard(keyboardService.menuKeyboard());
            }
        };

        telegramSender.send(outgoingMessage);
    }

    public OutgoingMessage onTitle(Long chatId, String text, ConversationContext ctx) {
        ctx.setTitle(text);
        ctx.setState(ConversationState.WAITING_DATE);
        return OutgoingMessage
                .send(chatId, "Выберите дату")
                .keyboard(keyboardService.dateKeyboard(3));
    }
    // Will be at callback processor
    // public void onDate(Long chatId, LocalDate date) {}

    public OutgoingMessage onTime(Long chatId, String text, ConversationContext ctx) {
        try {
            if (!timeValidationService.validate(text)) {
                return OutgoingMessage.send(chatId,"Вы ввели не правильный формат времени.\n" +
                        "Прошу введи время в формате HH:mm");
            }

            LocalTime time = LocalTime.parse(text);
            ctx.setTime(time);
            Task createdTask = taskService.createTask(
                    ctx.getChatId(),
                    ctx.getTitle(),
                    ctx.getDate(),
                    ctx.getTime()
            );

            conversationStateService.reset(chatId);

            if (createdTask != null) {
                return OutgoingMessage.send(chatId, "Задание создано успешно!\n"
                        + "Задание: " + createdTask.getName() + "\n"
                        + "Время: " + DateTimeUtil.parseDateTimeToString(createdTask.getDeadline()))
                        .keyboard(keyboardService.menuKeyboard());
            }
            LOGGER.error("Cannot create task for context -> {}", ctx);
            return OutgoingMessage.send(chatId, "Упс, не получилось создать задание(")
                    .keyboard(keyboardService.menuKeyboard());
        } catch (Exception e) {
            LOGGER.error("Error while processing text `{}` in onTime method", text, e);
            conversationStateService.reset(chatId);
            return OutgoingMessage.send(chatId,"Возникла ошибка во время создания таска(");
        }
    }

}
