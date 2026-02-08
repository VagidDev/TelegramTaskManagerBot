package md.zibliuc.taskmanagerbot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.keyboard.KeyboardService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramSender {
    private final TelegramBot telegramBot;

    public void send(OutgoingMessage message) {
        switch (message.getAction()) {
            case SEND -> sendMessage(message);
            case EDIT -> editMessage(message);
            case DELETE -> deleteMessage(message);
            //TODO: add logger for default -> ...
        }
    }

    private void sendMessage(OutgoingMessage msg) {
        SendMessage sendMessage = new SendMessage(
                msg.getChatId().longValue(),
                msg.getText()
        );

        if (msg.getKeyboard() != null)
            sendMessage.replyMarkup(msg.getKeyboard());

        telegramBot.execute(sendMessage);
    }

    private void editMessage(OutgoingMessage msg) {
        EditMessageText sendMessage = new EditMessageText(
                msg.getChatId(),
                msg.getMessageId(),
                msg.getText()
        );
        telegramBot.execute(sendMessage);
    }

    private void deleteMessage(OutgoingMessage msg) {
        DeleteMessage sendMessage = new DeleteMessage(
                msg.getChatId(),
                msg.getMessageId()
        );
        telegramBot.execute(sendMessage);
    }
}
