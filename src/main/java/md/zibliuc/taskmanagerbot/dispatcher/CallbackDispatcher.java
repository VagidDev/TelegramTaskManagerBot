package md.zibliuc.taskmanagerbot.dispatcher;

import com.pengrad.telegrambot.model.CallbackQuery;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.bot.CallbackDataParser;
import md.zibliuc.taskmanagerbot.bot.TelegramSender;
import md.zibliuc.taskmanagerbot.callback.TaskCallbackActionHandler;
import md.zibliuc.taskmanagerbot.callback.TaskCallbackCancelHandler;
import md.zibliuc.taskmanagerbot.callback.TaskCallbackDateHandler;
import md.zibliuc.taskmanagerbot.callback.TaskCallbackSelectHandler;
import md.zibliuc.taskmanagerbot.config.CallbackResponseConfig;
import md.zibliuc.taskmanagerbot.dto.CallbackData;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.service.TaskService;
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
    private final TaskCallbackSelectHandler taskCallbackSelectHandler;
    private final TaskCallbackActionHandler taskCallbackActionHandler;
    private final TaskCallbackCancelHandler taskCallbackCancelHandler;
    private final CallbackResponseConfig callbackResponseConfig;

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
            case DATE, DATE_FORWARD, DATE_BACKWARD -> taskCallbackDateHandler.handle(incomingMessage);
            case TASK -> taskCallbackSelectHandler.handle(incomingMessage);
            //TODO: implement logic
            case COMPLETE, POSTPONE, DELETE -> {
                //TODO: maybe change to edit, need to think more about this
                telegramSender.send(OutgoingMessage.delete(incomingMessage.chatId(), incomingMessage.messageId()));
                yield taskCallbackActionHandler.handle(incomingMessage);
            }
            case EDIT -> {
                LOGGER.warn("Not implemented yet! Incoming message received -> {}", incomingMessage);
                yield null;
            }
            case CANCEL -> {
                telegramSender.send(OutgoingMessage.delete(incomingMessage.chatId(), incomingMessage.messageId()));
                yield taskCallbackCancelHandler.handle(incomingMessage);
            }
            case UNDEFINED -> {
                LOGGER.warn("Undefined callback! Incoming message with callback received -> {}", incomingMessage);
                yield OutgoingMessage.send(incomingMessage.chatId(), callbackResponseConfig.getUndefined());
            }
            default -> {
                LOGGER.warn("Not implemented callback. Incoming message -> {}", incomingMessage);
                yield OutgoingMessage.send(incomingMessage.chatId(), callbackResponseConfig.getUndefined());
            }
        };

        telegramSender.send(message);
        telegramSender.proceededCallbackQuery(callbackQuery.id());
        //TODO delegate to necessary CallbackHandler
    }
}
