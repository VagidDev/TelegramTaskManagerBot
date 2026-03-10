package md.zibliuc.taskmanagerbot.dispatcher;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.dto.IncomingCallbackMessage;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.dto.IncomingTextMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateDispatcher {
    private static final Logger LOGGER = LogManager.getLogger(UpdateDispatcher.class);

    private final TextMessageDispatcher textMessageDispatcher;
    private final CallbackDispatcher callbackDispatcher;

    public void dispatch(IncomingMessage incomingMessage) {
        if (incomingMessage instanceof IncomingTextMessage incomingTextMessage) {
            textMessageDispatcher.dispatch(incomingTextMessage);
        } else if (incomingMessage instanceof IncomingCallbackMessage incomingCallbackMessage) {
            callbackDispatcher.dispatch(incomingCallbackMessage);
        } else {
            LOGGER.warn("Cannot find instance of incoming message -> {}", incomingMessage.getClass().getSimpleName());
        }
    }
}
