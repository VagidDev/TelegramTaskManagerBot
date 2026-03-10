package md.zibliuc.taskmanagerbot.bot;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.dispatcher.UpdateDispatcher;
import md.zibliuc.taskmanagerbot.dto.IncomingMessage;
import md.zibliuc.taskmanagerbot.mapper.UpdateMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramUpdateListener {
    private final UpdateMapper updateMapper;
    private final UpdateDispatcher updateDispatcher;

    public void onUpdate(Update update) {
        IncomingMessage incomingMessage = updateMapper.map(update);
        updateDispatcher.dispatch(incomingMessage);
    }
}
