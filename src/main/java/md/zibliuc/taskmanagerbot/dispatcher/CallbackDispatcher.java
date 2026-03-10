package md.zibliuc.taskmanagerbot.dispatcher;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.bot.CallbackDataParser;
import md.zibliuc.taskmanagerbot.bot.Sender;
import md.zibliuc.taskmanagerbot.callback.TaskCallbackActionHandler;
import md.zibliuc.taskmanagerbot.callback.TaskCallbackCancelHandler;
import md.zibliuc.taskmanagerbot.callback.TaskCallbackDateHandler;
import md.zibliuc.taskmanagerbot.callback.TaskCallbackSelectHandler;
import md.zibliuc.taskmanagerbot.config.CallbackResponseConfig;
import md.zibliuc.taskmanagerbot.dto.IncomingCallbackMessage;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CallbackDispatcher {
    private static final Logger LOGGER = LogManager.getLogger(CallbackDispatcher.class);

    private final Sender sender;
    private final TaskCallbackDateHandler taskCallbackDateHandler;
    private final TaskCallbackSelectHandler taskCallbackSelectHandler;
    private final TaskCallbackActionHandler taskCallbackActionHandler;
    private final TaskCallbackCancelHandler taskCallbackCancelHandler;
    private final CallbackResponseConfig callbackResponseConfig;

    public void dispatch(IncomingCallbackMessage incomingCallbackMessage) {
        OutgoingMessage message = switch (incomingCallbackMessage.callbackData().type()) {
            case DATE, DATE_FORWARD, DATE_BACKWARD -> taskCallbackDateHandler.handle(incomingCallbackMessage);
            case TASK -> taskCallbackSelectHandler.handle(incomingCallbackMessage);
            //TODO: implement logic
            case COMPLETE, POSTPONE, DELETE -> {
                //TODO: maybe change to edit, need to think more about this
                sender.send(OutgoingMessage.delete(incomingCallbackMessage.chatId(), incomingCallbackMessage.messageId()));
                yield taskCallbackActionHandler.handle(incomingCallbackMessage);
            }
            case EDIT -> {
                LOGGER.warn("Not implemented yet! Incoming message received -> {}", incomingCallbackMessage);
                yield null;
            }
            case CANCEL -> {
                sender.send(OutgoingMessage.delete(incomingCallbackMessage.chatId(), incomingCallbackMessage.messageId()));
                yield taskCallbackCancelHandler.handle(incomingCallbackMessage);
            }
            case UNDEFINED -> {
                LOGGER.warn("Undefined callback! Incoming message with callback received -> {}", incomingCallbackMessage);
                yield OutgoingMessage.send(incomingCallbackMessage.chatId(), callbackResponseConfig.getUndefined());
            }
            default -> {
                LOGGER.warn("Not implemented callback. Incoming message -> {}", incomingCallbackMessage);
                yield OutgoingMessage.send(incomingCallbackMessage.chatId(), callbackResponseConfig.getUndefined());
            }
        };

        sender.send(message);
        sender.proceededCallbackQuery(incomingCallbackMessage.callbackId());
    }
}
