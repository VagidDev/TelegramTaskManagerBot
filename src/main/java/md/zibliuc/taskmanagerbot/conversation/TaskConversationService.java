package md.zibliuc.taskmanagerbot.conversation;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.config.ConversationResponseConfig;
import md.zibliuc.taskmanagerbot.config.RandomFunnyResponseConfig;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.service.*;
import md.zibliuc.taskmanagerbot.util.DateTimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TaskConversationService {
    private static final Logger LOGGER = LogManager.getLogger(TaskConversationService.class);

    private final ConversationResponseConfig conversationResponseConfig;
    private final UserConversationStateService conversationStateService;
    private final RandomFunnyResponseConfig randomFunnyResponseConfig;
    private final TimeValidationService timeValidationService;
    private final TimeFormatterService timeFormatterService;
    private final KeyboardService keyboardService;
    private final DateTimeUtil dateTimeUtil;
    private final TaskService taskService;

    public OutgoingMessage handle(IncomingMessage message) {
        LOGGER.info("Received message {}", message);
        ConversationContext ctx = conversationStateService.get(message.chatId());

        return switch (ctx.getState()) {
            case WAITING_TITLE -> onTitle(message.chatId(), message.text(), ctx);
            case WAITING_TIME -> onTime(message.chatId(), message.text(), ctx);
            default -> {
                LOGGER.warn("Resetting context due to unexpected state -> {}", ctx.getState());
                conversationStateService.reset(message.chatId());
                String response = randomFunnyResponseConfig.getFunnyResponse();
                LOGGER.info("Sending to user funny response -> {}", response);
                yield OutgoingMessage
                    .send(message.chatId(),response)
                    .keyboard(keyboardService.menuKeyboard());
            }
        };
    }

    private OutgoingMessage onTitle(Long chatId, String text, ConversationContext ctx) {
        ctx.setTitle(text);
        ctx.setState(ConversationState.WAITING_DATE);
        return OutgoingMessage
                .send(chatId, conversationResponseConfig.getOnTitleCorrect())
                .keyboard(keyboardService.dateKeyboard(3));
    }

    private OutgoingMessage onTime(Long chatId, String text, ConversationContext ctx) {
        try {
            //double-checking time format, after formatting input
            if (!timeValidationService.validate(text)) {
                LOGGER.info("Invalid time format! Trying to autoformat the input `{}`", text);
                text = timeFormatterService.format(text);
                LOGGER.info("Formatted text -> `{}`", text);
                if (!timeValidationService.validate(text)) {
                    LOGGER.warn("Invalid time format after autoformatting! Received text `{}`", text);
                    return OutgoingMessage.send(chatId, conversationResponseConfig.getOnTimeIncorrectFormat());
                }
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
                String response = conversationResponseConfig
                        .getOnTimeCorrect()
                        .formatted(createdTask.getName(), dateTimeUtil.parseDateTimeToDateTimeString(createdTask.getDeadline()));
                return OutgoingMessage.send(chatId, response)
                        .keyboard(keyboardService.menuKeyboard());
            }
            LOGGER.error("Cannot create task for context -> {}", ctx);
            return OutgoingMessage.send(chatId, conversationResponseConfig.getOnTimeError())
                    .keyboard(keyboardService.menuKeyboard());
        } catch (Exception e) {
            LOGGER.error("Error while processing text `{}` in onTime method", text, e);
            conversationStateService.reset(chatId);
            return OutgoingMessage.send(chatId, conversationResponseConfig.getOnTimeError());
        }
    }

}
