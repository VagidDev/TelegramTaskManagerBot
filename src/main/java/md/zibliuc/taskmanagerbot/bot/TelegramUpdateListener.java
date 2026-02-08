package md.zibliuc.taskmanagerbot.bot;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.dispatcher.UpdateDispatcher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramUpdateListener {
    private final UpdateDispatcher updateDispatcher;

    public void onUpdate(Update update) {
        updateDispatcher.dispatch(update);
    }
}
