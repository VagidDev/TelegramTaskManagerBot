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
        if (update.message() != null && update.message().text() != null) {
            textMessageDispatcher.dispatch(update);
        } else if (update.callbackQuery() != null) {
            callbackDispatcher.dispatch(update);
        }
        //TODO: delegate by update type
    }
}
