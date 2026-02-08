package md.zibliuc.taskmanagerbot.dispatcher;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateDispatcher {
    private final TextMessageDispatcher textMessageDispatcher;
    private final CallbackDispatcher callbackDispatcher;

    public void dispatch(Update update) {
        //TODO: test if method receiving null from update.message() when sending callback
        if (update.message() != null && update.message().text() != null) {
            textMessageDispatcher.dispatch(update.message());
        } else if (update.callbackQuery() != null) {
            callbackDispatcher.dispatch(update.callbackQuery());
        }
        //TODO: delegate by update type
    }
}
