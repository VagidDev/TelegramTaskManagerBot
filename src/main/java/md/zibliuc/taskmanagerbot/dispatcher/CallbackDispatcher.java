package md.zibliuc.taskmanagerbot.dispatcher;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import org.springframework.stereotype.Component;

@Component
public class CallbackDispatcher {
    public void dispatch(CallbackQuery callbackQuery) {
        //TODO delegate to necessary CallbackHandler
    }
}
