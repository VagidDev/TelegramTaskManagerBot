package md.zibliuc.taskmanagerbot.dispatcher;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateDispatcher {
    private static final Logger LOGGER = LogManager.getLogger(UpdateDispatcher.class);

    private final TextMessageDispatcher textMessageDispatcher;
    private final CallbackDispatcher callbackDispatcher;

    public void dispatch(Update update) {
        //TODO: test if method receiving null from update.message() when sending callback
        if (update.message() != null && update.message().text() != null) {
            LOGGER.debug("Processing text message");
            textMessageDispatcher.dispatch(update.message());
        } else if (update.callbackQuery() != null) {
            LOGGER.debug("Processing callback message");
            callbackDispatcher.dispatch(update.callbackQuery());
        } else {
            LOGGER.warn("Undefined type of message -> {}", update);
        }
    }
}
