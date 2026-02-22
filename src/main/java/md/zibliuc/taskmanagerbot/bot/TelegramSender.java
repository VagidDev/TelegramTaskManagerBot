package md.zibliuc.taskmanagerbot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.*;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramSender {
    private static final Logger LOGGER = LogManager.getLogger(TelegramSender.class);
    private final TelegramBot telegramBot;

    public void send(OutgoingMessage message) {
        if (message == null) {
            LOGGER.warn("Cannot send null message");
            return;
        }

        switch (message.getAction()) {
            case SEND -> sendMessage(message);
            case EDIT -> editMessage(message);
            case DELETE -> deleteMessage(message);
            default -> LOGGER.warn("Undefined message action -> {}", message.getAction());
        }
    }

    public void proceededCallbackQuery(String callbackQueryId) {
        telegramBot.execute(new AnswerCallbackQuery(callbackQueryId));
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

        if (msg.getKeyboard() != null && msg.getKeyboard() instanceof InlineKeyboardMarkup) {
            LOGGER.info("Adding keyboard to edited message message `{}`", msg.getText());
            sendMessage.replyMarkup((InlineKeyboardMarkup) msg.getKeyboard());
        }
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
