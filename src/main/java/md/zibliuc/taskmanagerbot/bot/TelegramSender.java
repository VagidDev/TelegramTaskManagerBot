package md.zibliuc.taskmanagerbot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import md.zibliuc.taskmanagerbot.keyboard.KeyboardService;
import md.zibliuc.taskmanagerbot.service.MessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramSender {
    private static final Logger LOGGER = LogManager.getLogger(TelegramSender.class);
    private final TelegramBot telegramBot;

    public void send(OutgoingMessage message) {
        switch (message.getAction()) {
            case SEND -> sendMessage(message);
            case EDIT -> editMessage(message);
            case DELETE -> deleteMessage(message);
            default -> LOGGER.warn("Undefined message action -> {}", message.getAction());
        }
    }

    private void sendMessage(OutgoingMessage msg) {
        LOGGER.info(
                "Sending message `{}` to chat id `{}`",
                msg.getText(),
                msg.getChatId()
        );
        SendMessage sendMessage = new SendMessage(
                msg.getChatId().longValue(),
                msg.getText()
        );

        if (msg.getKeyboard() != null) {
            LOGGER.info("Adding keyboard to message `{}`", msg.getText());
            sendMessage.replyMarkup(msg.getKeyboard());
        }

        telegramBot.execute(sendMessage);
    }

    private void editMessage(OutgoingMessage msg) {
        LOGGER.info(
                "Editing message with id `{}` with new text `{}` in chat with id `{}`",
                msg.getMessageId(),
                msg.getText(),
                msg.getChatId()
        );

        EditMessageText sendMessage = new EditMessageText(
                msg.getChatId(),
                msg.getMessageId(),
                msg.getText()
        );
        telegramBot.execute(sendMessage);
    }

    private void deleteMessage(OutgoingMessage msg) {
        LOGGER.info(
                "Deleting message with id `{}` in chat with id `{}`",
                msg.getMessageId(),
                msg.getChatId()
        );

        DeleteMessage sendMessage = new DeleteMessage(
                msg.getChatId(),
                msg.getMessageId()
        );
        telegramBot.execute(sendMessage);
    }
}
