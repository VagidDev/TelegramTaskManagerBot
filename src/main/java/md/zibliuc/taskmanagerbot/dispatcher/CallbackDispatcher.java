package md.zibliuc.taskmanagerbot.dispatcher;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

@Component
public class CallbackDispatcher {
    public void dispatch(Update update) {
        //TODO delegate to necessary CallbackHandler
    }
}
