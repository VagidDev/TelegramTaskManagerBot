package md.zibliuc.taskmanagerbot.dispatcher;

import com.pengrad.telegrambot.model.CallbackQuery;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.bot.CallbackDataParser;
import md.zibliuc.taskmanagerbot.bot.TelegramSender;
import md.zibliuc.taskmanagerbot.callback.TaskCallbackDateHandler;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import md.zibliuc.taskmanagerbot.dto.CallbackData;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.keyboard.KeyboardService;
import md.zibliuc.taskmanagerbot.service.TaskService;
import md.zibliuc.taskmanagerbot.service.UserConversationStateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CallbackDispatcher {
    private static final Logger LOGGER = LogManager.getLogger(CallbackDispatcher.class);

    private final TelegramSender telegramSender;
    private final CallbackDataParser callbackDataParser;
    private final TaskCallbackDateHandler taskCallbackDateHandler;
    private final UserConversationStateService userConversationStateService;
    private final KeyboardService keyboardService;
    private final TaskService taskService;

    public void dispatch(CallbackQuery callbackQuery) {
        CallbackData callbackData = callbackDataParser.parse(callbackQuery.data());

        IncomingMessage incomingMessage = new IncomingMessage(
                callbackQuery.maybeInaccessibleMessage().chat().id(),
                callbackQuery.maybeInaccessibleMessage().messageId(),
                callbackQuery.data(),
                callbackData,
                null
        );

        OutgoingMessage message = switch (callbackData.type()) {
            case DATE -> taskCallbackDateHandler.handle(incomingMessage);
            case TASK -> {
                //TODO: move it to special handler
                //TODO:also implement for other actions (cases)
                Task task = taskService.get(callbackData.asLong());
                yield OutgoingMessage
                    .edit(incomingMessage.chatId(), incomingMessage.messageId(), "Задание: ");
            }
            case ACTION -> {
                LOGGER.warn("Not implemented yet! Incoming message received -> {}", incomingMessage);
                yield null;
            }
            case CANCEL -> {
                userConversationStateService.reset(incomingMessage.chatId());
                // Deleting message
                telegramSender.send(OutgoingMessage.delete(incomingMessage.chatId(), incomingMessage.messageId()));
                yield OutgoingMessage
                        .send(incomingMessage.chatId(), "Галя отмена...")
                        .keyboard(keyboardService.menuKeyboard());
            }
            case UNDEFINED -> {
                LOGGER.warn("Undefined callback! Incoming message with callback received -> {}", incomingMessage);
                yield OutgoingMessage.send(incomingMessage.chatId(), "Вот это поворот, что-то новенькое....");
            }
        };

        telegramSender.send(message);
        telegramSender.proceededCallbackQuery(callbackQuery.id());
        //TODO delegate to necessary CallbackHandler
    }
}
