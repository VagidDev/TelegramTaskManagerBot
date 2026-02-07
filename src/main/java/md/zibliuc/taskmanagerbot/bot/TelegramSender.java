package md.zibliuc.taskmanagerbot.bot;

import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramSender {
    private final TelegramBot telegramBot;

    public void send(OutgoingMessage message) {

    }
}
